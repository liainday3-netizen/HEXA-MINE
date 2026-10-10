package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.HexamineDao
import com.example.data.HexamineDatabase
import com.example.data.LogEntity
import com.example.data.PayoutEntity
import com.example.data.PoolEntity
import com.example.data.RigEntity
import com.example.config.AppMode
import com.example.mining.MiningMath
import com.example.model.SolidityContracts
import com.example.quantum.QuantumSecurityEngine
import com.example.quantum.QuantumTelemetry
import com.example.stratum.StratumEngine
import com.example.stratum.StratumPacket
import com.example.stratum.StratumState
import com.example.market.MarketDataFeedEngine
import com.example.market.MarketFeedState
import com.example.web3.AdvancedAuthPayload
import com.example.web3.InAppWalletData
import com.example.web3.Web3Manager
import com.example.web3.Web3Network
import com.example.web3.Web3Wallet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class OperationsKpis(
    val fleetHashrateTh: Double = 1007.5,
    val activeRigsCount: Int = 6,
    val warningRigsCount: Int = 1,
    val totalPowerKw: Double = 19.8,
    val efficiencyJPerTh: Double = 19.65,
    val earnedUsdt24h: Double = 238.45,
    val earnedBtc24h: Double = 0.00251,
    val isFleetMode: Boolean = true,
    val isCurtailmentActive: Boolean = false,
    val electricityRateKwh: Double = 0.045,
    val netDailyProfitUsd: Double = 217.06
)

class HexamineViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        HexamineDatabase::class.java,
        "hexamine_mining.db"
    ).fallbackToDestructiveMigration().build()

    val dao: HexamineDao = db.hexamineDao()

    val rigs: StateFlow<List<RigEntity>> = dao.getAllRigs().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val pools: StateFlow<List<PoolEntity>> = dao.getAllPools().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val payouts: StateFlow<List<PayoutEntity>> = dao.getAllPayouts().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val logs: StateFlow<List<LogEntity>> = dao.getRecentLogs().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val _kpis = MutableStateFlow(OperationsKpis())
    val kpis: StateFlow<OperationsKpis> = _kpis.asStateFlow()

    private val _web3Wallet = MutableStateFlow(Web3Wallet())
    val web3Wallet: StateFlow<Web3Wallet> = _web3Wallet.asStateFlow()

    private val _inAppWallet = MutableStateFlow(InAppWalletData())
    val inAppWallet: StateFlow<InAppWalletData> = _inAppWallet.asStateFlow()

    private val _activeNetwork = MutableStateFlow(Web3Manager.DEFAULT_NETWORK)
    val activeNetwork: StateFlow<Web3Network> = _activeNetwork.asStateFlow()

    private val _stratumState = MutableStateFlow(StratumState())
    val stratumState: StateFlow<StratumState> = _stratumState.asStateFlow()

    private val _quantumTelemetry = MutableStateFlow(
        QuantumTelemetry(
            entropySeedHex = QuantumSecurityEngine.generateQuantumEntropy(),
            postQuantumAddress = QuantumSecurityEngine.generatePostQuantumAddress("HEXA_GENESIS_SEED")
        )
    )
    val quantumTelemetry: StateFlow<QuantumTelemetry> = _quantumTelemetry.asStateFlow()

    private val _pendingAuthPayload = MutableStateFlow<AdvancedAuthPayload?>(null)
    val pendingAuthPayload: StateFlow<AdvancedAuthPayload?> = _pendingAuthPayload.asStateFlow()

    val marketFeed: StateFlow<MarketFeedState> = MarketDataFeedEngine.feedState

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _activeTab = MutableStateFlow("dashboard") // dashboard, breaker_core, wallet, rigs, etc.
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    init {
        viewModelScope.launch {
            seedInitialDataIfEmpty()
            MarketDataFeedEngine.startPolling(viewModelScope)
            startRealSimulationLoop()
        }
    }

    fun refreshMarketFeed() {
        viewModelScope.launch {
            MarketDataFeedEngine.refreshNow()
            _toastMessage.value = "Coinbase & EVM feeds updated"
        }
    }

    fun setMarketSource(source: String) {
        MarketDataFeedEngine.setSource(source)
        _toastMessage.value = "Switched price feed to $source"
    }

    fun setTab(tab: String) {
        _activeTab.value = tab
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun selectNetwork(network: Web3Network) {
        if (AppMode.isSimulated.value && !network.isTestnet) {
            _toastMessage.value = "Mainnets are disabled in simulated mode"
            return
        }
        _activeNetwork.value = network
        viewModelScope.launch {
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "INFO",
                    category = "WEB3",
                    message = "Switched EVM RPC to ${network.name} (ChainID: ${network.chainId})"
                )
            )
        }
        _toastMessage.value = "Connected to ${network.name}"
    }

    private suspend fun seedInitialDataIfEmpty() {
        // Seed default rigs if empty
        val defaultRigs = listOf(
            RigEntity("rig-01", "HEXA-S21-Alpha", "Antminer S21", "192.168.1.101", 200.0, 3500, 61, 5600, "ONLINE", "HEXA Stratum Pool", 4890, 3, "NORMAL"),
            RigEntity("rig-02", "HEXA-S21-Beta", "Antminer S21", "192.168.1.102", 198.5, 3480, 63, 5700, "ONLINE", "HEXA Stratum Pool", 4810, 5, "NORMAL"),
            RigEntity("rig-03", "HEXA-S19-Gamma", "Antminer S19 Pro+", "192.168.1.103", 122.0, 3050, 68, 6100, "ONLINE", "HEXA Stratum Pool", 3120, 4, "NORMAL"),
            RigEntity("rig-04", "HEXA-M50-Delta", "Whatsminer M50S+", "192.168.1.104", 136.0, 3260, 65, 5900, "ONLINE", "HEXA Stratum Pool", 3350, 2, "NORMAL"),
            RigEntity("rig-05", "HEXA-A14-Epsilon", "Avalon A1466", "192.168.1.105", 150.0, 3230, 71, 6300, "WARNING", "HEXA Stratum Pool", 3620, 8, "OVERCLOCK"),
            RigEntity("rig-06", "HEXA-S21-Zeta", "Antminer S21", "192.168.1.106", 201.0, 3510, 59, 5400, "ONLINE", "HEXA Stratum Pool", 4940, 2, "NORMAL")
        )
        dao.insertRigs(defaultRigs)

        val defaultPools = listOf(
            PoolEntity("pool-01", "HEXA Stratum Node", "stratum+tcp://hexa.hexamine.pool", 3333, "hexa_operator.rig_master", 0.8, true, "STRATUM_V2"),
            PoolEntity("pool-02", "Foundry USA Pool", "stratum+tcp://stratum.foundryusa.com", 3333, "hexa_foundry.main", 1.0, false, "STRATUM_V1"),
            PoolEntity("pool-03", "AntPool Global", "stratum+tcp://stratum.antpool.com", 3333, "hexamine.ant01", 1.5, false, "STRATUM_V1"),
            PoolEntity("pool-04", "Local Stratum V2 Node", "stratum+tcp://127.0.0.1", 34255, "hexamine_local", 0.0, false, "STRATUM_V2")
        )
        dao.insertPools(defaultPools)

        val defaultPayouts = listOf(
            PayoutEntity("pay-01", "0x8f4c2919aa74620b7218d6e32e8419ba603938d1", 21458890, 142.50, "USDT", com.example.web3.PLACEHOLDER_ADDRESS, System.currentTimeMillis() - 7200000, "SIMULATED", 15.2, "executeAutomatedPayoutBatch"),
            PayoutEntity("pay-02", "0x3a19b84bbcf219d9018420bfed8924016a5090fa", 21458410, 128.80, "USDT", com.example.web3.PLACEHOLDER_ADDRESS, System.currentTimeMillis() - 28800000, "SIMULATED", 16.8, "executeAutomatedPayoutBatch"),
            PayoutEntity("pay-03", "0x712a88e144a95b8209cbff610934027781a912cc", 21457920, 95.00, "USDT", com.example.web3.PLACEHOLDER_ADDRESS, System.currentTimeMillis() - 86400000, "SIMULATED", 14.9, "executeAutomatedPayoutBatch")
        )
        defaultPayouts.forEach { dao.insertPayout(it) }

        val defaultLogs = listOf(
            LogEntity(timestamp = System.currentTimeMillis() - 120000, level = "OK", category = "STRATUM", message = "Noise protocol handshake complete with stratum+tcp://hexa.hexamine.pool:3333"),
            LogEntity(timestamp = System.currentTimeMillis() - 90000, level = "INFO", category = "WEB3", message = "EVM Protocol Contract bound: ${SolidityContracts.MINING_PROTOCOL_ADDRESS}"),
            LogEntity(timestamp = System.currentTimeMillis() - 60000, level = "OK", category = "QUANTUM", message = "CRYSTALS-Dilithium5 Post-Quantum Hybrid Signature engine operational (256-bit Grover defense)"),
            LogEntity(timestamp = System.currentTimeMillis() - 30000, level = "INFO", category = "PAYOUT", message = "Automated USDT Payout engine active: Threshold = 10.00 USDT via Vault")
        )
        defaultLogs.forEach { dao.insertLog(it) }
    }

    private fun startRealSimulationLoop() {
        viewModelScope.launch {
            while (true) {
                delay(3500)
                // Real Path simulation step
                val currentRigs = rigs.value
                if (currentRigs.isNotEmpty()) {
                    val activeRigs = currentRigs.filter { it.status != "OFFLINE" }
                    val totalHash = activeRigs.sumOf { MiningMath.effectiveHashrateTh(it.hashrateTh, it.profile) }
                    val totalPower = activeRigs.sumOf { MiningMath.effectivePowerWatts(it.powerWatts, it.profile) }
                    val powerKw = totalPower / 1000.0
                    val eff = MiningMath.efficiencyJPerTh(totalPower, totalHash)

                    // Increment random rig share
                    val randomRig = activeRigs.randomOrNull()
                    if (randomRig != null) {
                        val accepted = Random.nextInt(1, 4)
                        dao.updateRig(randomRig.copy(sharesAccepted = randomRig.sharesAccepted + accepted))

                        // Stratum packet simulation
                        val jobId = "job_${Random.nextInt(100, 999)}"
                        val nonce = "%08x".format(Random.nextInt())
                        val submitPkt = StratumEngine.createSubmitPacket(randomRig.name, jobId, nonce)
                        StratumEngine.emitPacket(submitPkt)
                        delay(25)
                        val respPkt = StratumEngine.createSubmitResponse(true)
                        StratumEngine.emitPacket(respPkt)

                        // Update stratum state
                        _stratumState.value = _stratumState.value.copy(
                            acceptedSharesCount = _stratumState.value.acceptedSharesCount + accepted,
                            lastJobId = jobId,
                            pingMs = Random.nextInt(18, 26)
                        )

                        // Add small SIMULATED payout accrual in USDT
                        val accruedUsdt = MiningMath.accruedUsdt(accepted)
                        _kpis.value = _kpis.value.copy(
                            fleetHashrateTh = totalHash + Random.nextDouble(-2.0, 2.0),
                            activeRigsCount = activeRigs.size,
                            warningRigsCount = currentRigs.count { it.status == "WARNING" },
                            totalPowerKw = powerKw,
                            efficiencyJPerTh = eff,
                            earnedUsdt24h = _kpis.value.earnedUsdt24h + accruedUsdt,
                            earnedBtc24h = _kpis.value.earnedBtc24h + MiningMath.usdtToBtc(accruedUsdt, 95000.0)
                        )
                    }

                    // Log Stratum / Quantum activity occasionally
                    if (Random.nextInt(10) < 3) {
                        val entropy = QuantumSecurityEngine.generateQuantumEntropy()
                        _quantumTelemetry.value = _quantumTelemetry.value.copy(entropySeedHex = entropy)
                        dao.insertLog(
                            LogEntity(
                                timestamp = System.currentTimeMillis(),
                                level = "OK",
                                category = "STRATUM",
                                message = "Stratum V2 share accepted: diff 16384 | Q-Entropy: 0x${entropy.take(8)}..."
                            )
                        )
                    }
                }
            }
        }
    }

    fun toggleFleetCurtailment() {
        val newState = !_kpis.value.isCurtailmentActive
        _kpis.value = _kpis.value.copy(isCurtailmentActive = newState)
        viewModelScope.launch {
            val updatedRigs = rigs.value.map {
                it.copy(profile = if (newState) "CURTAILED" else "NORMAL")
            }
            dao.insertRigs(updatedRigs)
            val msg = if (newState) "HEXA GRID CURTAILMENT ACTIVATED (-80% Power draw during peak power tariff)"
            else "HEXA NORMAL DISPATCH RESUMED (Full fleet operational)"
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = if (newState) "WARN" else "INFO",
                    category = "RIG",
                    message = msg
                )
            )
            _toastMessage.value = if (newState) "Curtailment Mode ON" else "Standard Dispatch Restored"
        }
    }

    fun setRigProfile(rigId: String, newProfile: String) {
        viewModelScope.launch {
            val rig = rigs.value.find { it.id == rigId } ?: return@launch
            dao.updateRig(rig.copy(profile = newProfile))
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "INFO",
                    category = "RIG",
                    message = "${rig.name} profile tuned to $newProfile"
                )
            )
            _toastMessage.value = "${rig.name} tuned to $newProfile"
        }
    }

    fun rebootRig(rigId: String) {
        viewModelScope.launch {
            val rig = rigs.value.find { it.id == rigId } ?: return@launch
            dao.updateRig(rig.copy(status = "WARNING"))
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "WARN",
                    category = "RIG",
                    message = "Rebooting ASIC controller on ${rig.name} (IP: ${rig.ipAddress})"
                )
            )
            delay(1500)
            dao.updateRig(rig.copy(status = "ONLINE"))
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "RIG",
                    message = "${rig.name} ASIC rebooted successfully. Hashing resumed."
                )
            )
            _toastMessage.value = "${rig.name} rebooted"
        }
    }

    fun addRig(name: String, model: String, ip: String, hashrate: Double, power: Int) {
        viewModelScope.launch {
            val id = "rig-${System.currentTimeMillis() % 10000}"
            val newRig = RigEntity(
                id = id,
                name = name.ifBlank { "HEXA-ASIC-${id.takeLast(4)}" },
                model = model.ifBlank { "Antminer S21" },
                ipAddress = ip.ifBlank { "192.168.1.${Random.nextInt(110, 220)}" },
                hashrateTh = hashrate.takeIf { it > 0 } ?: 200.0,
                powerWatts = power.takeIf { it > 0 } ?: 3500,
                tempC = Random.nextInt(58, 66),
                fanRpm = Random.nextInt(5200, 5800),
                status = "ONLINE",
                activePool = "HEXA Stratum Pool",
                sharesAccepted = 0,
                sharesRejected = 0,
                profile = "NORMAL"
            )
            dao.insertRig(newRig)
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "RIG",
                    message = "Added new mining rig ${newRig.name} (${newRig.model}, ${newRig.hashrateTh} TH/s)"
                )
            )
            _toastMessage.value = "Rig ${newRig.name} registered"
        }
    }

    fun deleteRig(rigId: String) {
        viewModelScope.launch {
            dao.deleteRig(rigId)
            _toastMessage.value = "Rig removed from fleet"
        }
    }

    fun setActivePool(poolId: String) {
        viewModelScope.launch {
            dao.setActivePool(poolId)
            val pool = pools.value.find { it.id == poolId }
            if (pool != null) {
                _stratumState.value = _stratumState.value.copy(
                    connectedPool = "${pool.name} (${pool.url}:${pool.port})",
                    protocolVersion = pool.protocolVersion
                )
                dao.insertLog(
                    LogEntity(
                        timestamp = System.currentTimeMillis(),
                        level = "INFO",
                        category = "STRATUM",
                        message = "Switched active stratum endpoint to ${pool.name} (${pool.url}:${pool.port})"
                    )
                )
                _toastMessage.value = "Stratum switched to ${pool.name}"
            }
        }
    }

    // Advanced Web3 Authentication & Confirmation Flow
    fun requestAdvancedAuth(
        actionName: String,
        contractAddress: String,
        functionSig: String,
        params: String
    ) {
        val payload = Web3Manager.createAdvancedAuthPayload(
            actionName = actionName,
            contractAddress = contractAddress,
            functionSig = functionSig,
            params = params
        )
        _pendingAuthPayload.value = payload
    }

    fun dismissAdvancedAuth() {
        _pendingAuthPayload.value = null
    }

    fun confirmAdvancedAuth() {
        val payload = _pendingAuthPayload.value ?: return
        viewModelScope.launch {
            val txHash = Web3Manager.generateTxHash()
            val block = _activeNetwork.value.blockHeight + 1

            when (payload.actionName) {
                "AUTOMATED_USDT_PAYOUT" -> {
                    val payoutAmount = 50.0 // Default payout trigger amount
                    val newPayout = PayoutEntity(
                        id = "pay-${System.currentTimeMillis() % 100000}",
                        txHash = txHash,
                        blockNumber = block,
                        amount = payoutAmount,
                        asset = "USDT",
                        recipientAddress = _web3Wallet.value.address,
                        timestamp = System.currentTimeMillis(),
                        status = "SIMULATED",
                        gasUsedGwei = payload.gasEstimateGwei,
                        contractMethod = "executeAutomatedPayoutBatch"
                    )
                    dao.insertPayout(newPayout)
                    _web3Wallet.value = _web3Wallet.value.copy(
                        usdtBalance = _web3Wallet.value.usdtBalance + payoutAmount
                    )
                    dao.insertLog(
                        LogEntity(
                            timestamp = System.currentTimeMillis(),
                            level = "OK",
                            category = "PAYOUT",
                            message = "Smart Contract Payout: +$payoutAmount USDT dispatched to ${_web3Wallet.value.address.take(10)}... (TX: ${txHash.take(12)}...)"
                        )
                    )
                    _toastMessage.value = "Automated Payout Confirmed! +$payoutAmount USDT"
                }

                "ALLOCATE_HASH_POWER" -> {
                    dao.insertLog(
                        LogEntity(
                            timestamp = System.currentTimeMillis(),
                            level = "OK",
                            category = "WEB3",
                            message = "Hash Power Allocated on-chain: ${payload.paramSummary} | Verified with Dilithium Proof"
                        )
                    )
                    _toastMessage.value = "Hash Power Allocation Confirmed on EVM"
                }

                "SUBMIT_SHARE_BATCH" -> {
                    dao.insertLog(
                        LogEntity(
                            timestamp = System.currentTimeMillis(),
                            level = "OK",
                            category = "WEB3",
                            message = "Stratum Share Batch verified on-chain: ${payload.paramSummary}"
                        )
                    )
                    _toastMessage.value = "Stratum Shares Proof verified on Smart Contract"
                }

                "CLAIM_GATED_TOKENS" -> {
                    val claimedTokens = 50000.0
                    _web3Wallet.value = _web3Wallet.value.copy(
                        hexaBalance = _web3Wallet.value.hexaBalance + claimedTokens
                    )
                    dao.insertPayout(
                        PayoutEntity(
                            id = "claim-${System.currentTimeMillis() % 100000}",
                            txHash = txHash,
                            blockNumber = block,
                            amount = claimedTokens,
                            asset = "HEXA",
                            recipientAddress = _web3Wallet.value.address,
                            timestamp = System.currentTimeMillis(),
                            status = "SIMULATED",
                            gasUsedGwei = payload.gasEstimateGwei,
                            contractMethod = "claimGatedTokens"
                        )
                    )
                    dao.insertLog(
                        LogEntity(
                            timestamp = System.currentTimeMillis(),
                            level = "OK",
                            category = "WEB3",
                            message = "ON-CHAIN GATE UNLOCKED: Claimed +50,000 \$HEXA into wallet (TX: ${txHash.take(12)}...)"
                        )
                    )
                    _toastMessage.value = "Gate Unlocked! +50,000 \$HEXA Claimed"
                }

                else -> {
                    dao.insertLog(
                        LogEntity(
                            timestamp = System.currentTimeMillis(),
                            level = "OK",
                            category = "WEB3",
                            message = "Executed ${payload.actionName} (TX: ${txHash.take(12)}...)"
                        )
                    )
                    _toastMessage.value = "${payload.actionName} executed successfully"
                }
            }

            _pendingAuthPayload.value = null
        }
    }

    fun triggerManualPayoutInUsdt(amountUsdt: Double = 50.0) {
        requestAdvancedAuth(
            actionName = "AUTOMATED_USDT_PAYOUT",
            contractAddress = SolidityContracts.PAYOUT_VAULT_ADDRESS,
            functionSig = "executeAutomatedPayoutBatch(address[],uint256[],uint256)",
            params = "Recipients: [${_web3Wallet.value.address}], Amount: ${amountUsdt} USDT, Epoch: 48"
        )
    }

    fun triggerHashPowerAllocationOnChain(poolId: String, hashrateTh: Double) {
        requestAdvancedAuth(
            actionName = "ALLOCATE_HASH_POWER",
            contractAddress = SolidityContracts.MINING_PROTOCOL_ADDRESS,
            functionSig = "allocateHashPower(bytes32,uint256)",
            params = "Pool: $poolId, Hashrate: $hashrateTh TH/s"
        )
    }

    fun triggerShareBatchVerificationOnChain() {
        val totalAccepted = rigs.value.sumOf { it.sharesAccepted }
        requestAdvancedAuth(
            actionName = "SUBMIT_SHARE_BATCH",
            contractAddress = SolidityContracts.MINING_PROTOCOL_ADDRESS,
            functionSig = "submitStratumShareBatch(bytes32,uint256,uint256,bytes32,bytes,bytes)",
            params = "SharesCount: $totalAccepted, Difficulty: 16384, QuantumEntropy: 0x${_quantumTelemetry.value.entropySeedHex.take(10)}..."
        )
    }

    fun triggerAiAutonomousPoolCreationAndFunding() {
        viewModelScope.launch {
            val newPoolId = "pool-ai-${System.currentTimeMillis() % 1000}"
            val seedUsdt = 250.0
            val newPool = PoolEntity(
                id = newPoolId,
                name = "HEXA AI Autonomous Yield Pool",
                url = "stratum+tcp://hexa.ai-yield.hexamine.pool",
                port = 3333,
                workerName = "ai_sentinel.auto01",
                feePercent = 0.0,
                isActive = true,
                protocolVersion = "STRATUM_V2"
            )
            dao.insertPool(newPool)
            dao.setActivePool(newPoolId)

            // Re-point fleet rigs
            val updatedRigs = rigs.value.map { it.copy(activePool = newPool.name) }
            dao.insertRigs(updatedRigs)

            // Record funding transaction on-chain
            val txHash = Web3Manager.generateTxHash()
            dao.insertPayout(
                PayoutEntity(
                    id = "fund-${System.currentTimeMillis() % 100000}",
                    txHash = txHash,
                    blockNumber = _activeNetwork.value.blockHeight + 1,
                    amount = seedUsdt,
                    asset = "USDT",
                    recipientAddress = SolidityContracts.POOL_FACTORY_ADDRESS,
                    timestamp = System.currentTimeMillis(),
                    status = "SIMULATED",
                    gasUsedGwei = 16.4,
                    contractMethod = "createAndFundAutonomousPool"
                )
            )

            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "WEB3",
                    message = "AI SENTINEL: Mining yield low -> Created & funded new pool '${newPool.name}' (+250 USDT Seed Liquidity). Fleet re-routed."
                )
            )
            _toastMessage.value = "AI Created & Funded New Pool (+250 USDT Seed)"
        }
    }

    fun executeDexSwap(fromAsset: String, toAsset: String, fromAmount: Double, toAmount: Double) {
        viewModelScope.launch {
            val currentWallet = _web3Wallet.value
            val updatedWallet = when {
                fromAsset == "wBTC" && toAsset == "USDT" -> currentWallet.copy(
                    wbtcBalance = maxOf(0.0, currentWallet.wbtcBalance - fromAmount),
                    usdtBalance = currentWallet.usdtBalance + toAmount
                )
                fromAsset == "ETH" && toAsset == "USDT" -> currentWallet.copy(
                    ethBalance = maxOf(0.0, currentWallet.ethBalance - fromAmount),
                    usdtBalance = currentWallet.usdtBalance + toAmount
                )
                fromAsset == "HEXA" && toAsset == "USDT" -> currentWallet.copy(
                    hexaBalance = maxOf(0.0, currentWallet.hexaBalance - fromAmount),
                    usdtBalance = currentWallet.usdtBalance + toAmount
                )
                fromAsset == "USDT" && toAsset == "wBTC" -> currentWallet.copy(
                    usdtBalance = maxOf(0.0, currentWallet.usdtBalance - fromAmount),
                    wbtcBalance = currentWallet.wbtcBalance + toAmount
                )
                else -> currentWallet.copy(
                    usdtBalance = currentWallet.usdtBalance + toAmount
                )
            }
            _web3Wallet.value = updatedWallet

            val txHash = Web3Manager.generateTxHash()
            dao.insertPayout(
                PayoutEntity(
                    id = "swap-${System.currentTimeMillis() % 100000}",
                    txHash = txHash,
                    blockNumber = _activeNetwork.value.blockHeight + 1,
                    amount = toAmount,
                    asset = toAsset,
                    recipientAddress = currentWallet.address,
                    timestamp = System.currentTimeMillis(),
                    status = "SIMULATED",
                    gasUsedGwei = 19.8,
                    contractMethod = "dexSwapUniswapV3Router"
                )
            )

            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "WEB3",
                    message = "DEX SWAP: $fromAmount $fromAsset -> ${"%.2f".format(toAmount)} $toAsset (TX: ${txHash.take(12)}...)"
                )
            )
            _toastMessage.value = "Swapped to ${"%.2f".format(toAmount)} $toAsset"
        }
    }

    fun connectWallet(providerName: String, address: String) {
        _web3Wallet.value = _web3Wallet.value.copy(
            address = address,
            isConnected = true
        )
        viewModelScope.launch {
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "WEB3",
                    message = "Wallet connected via $providerName: ${address.take(12)}..."
                )
            )
        }
        _toastMessage.value = "Connected via $providerName"
    }

    fun disconnectWallet() {
        _web3Wallet.value = _web3Wallet.value.copy(isConnected = false)
        _toastMessage.value = "Wallet disconnected"
    }

    // --- IN-APP WALLET END-TO-END METHODS ---

    fun generateNewInAppWallet() {
        val newWallet = Web3Manager.generateNewInAppWallet()
        _inAppWallet.value = newWallet
        _web3Wallet.value = _web3Wallet.value.copy(
            address = newWallet.address,
            isConnected = true
        )
        viewModelScope.launch {
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "WALLET",
                    message = "Created new In-App Wallet: ${newWallet.address} with 12-word mnemonic & Dilithium-5 keys"
                )
            )
        }
        _toastMessage.value = "Generated New In-App Wallet: ${newWallet.address.take(10)}..."
    }

    fun importInAppWallet(input: String) {
        if (!Web3Manager.isImportAllowed()) {
            _toastMessage.value = "Seed/private-key import is disabled in simulated mode. Never paste a real seed phrase here."
            return
        }
        val imported = Web3Manager.importInAppWallet(input)
        _inAppWallet.value = imported
        _web3Wallet.value = _web3Wallet.value.copy(
            address = imported.address,
            isConnected = true
        )
        viewModelScope.launch {
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "WALLET",
                    message = "Imported In-App Wallet: ${imported.address} (Keys bound)"
                )
            )
        }
        _toastMessage.value = "Wallet Imported: ${imported.address.take(10)}..."
    }

    fun markWalletBackedUp() {
        _inAppWallet.value = _inAppWallet.value.copy(isBackedUp = true)
        _toastMessage.value = "Seed phrase backup verified"
    }

    fun setInAppWalletAsPayoutRecipient() {
        val inAppAddr = _inAppWallet.value.address
        _inAppWallet.value = _inAppWallet.value.copy(isLinkedForUsdtPayouts = true)
        _web3Wallet.value = _web3Wallet.value.copy(address = inAppAddr, isConnected = true)
        viewModelScope.launch {
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "PAYOUT",
                    message = "In-App Wallet linked to HexaminePayoutVault as recipient: $inAppAddr"
                )
            )
            sendSlackAlert("Linked In-App Wallet ($inAppAddr) as automated USDT payout recipient in Hexamine Payout Vault.")
        }
        _toastMessage.value = "In-App Wallet set as Payout Recipient"
    }

    fun sendTokens(recipient: String, asset: String, amount: Double, gasPriceGwei: Double = 16.5) {
        if (!recipient.startsWith("0x") || recipient.length != 42) {
            _toastMessage.value = "Invalid recipient address (must be 42 chars starting with 0x)"
            return
        }
        if (amount <= 0) {
            _toastMessage.value = "Amount must be greater than 0"
            return
        }

        val curWallet = _web3Wallet.value
        val hasSufficientBalance = when (asset) {
            "USDT" -> curWallet.usdtBalance >= amount
            "ETH" -> curWallet.ethBalance >= amount
            "wBTC" -> curWallet.wbtcBalance >= amount
            "HEXA" -> curWallet.hexaBalance >= amount
            else -> false
        }

        if (!hasSufficientBalance) {
            _toastMessage.value = "Insufficient $asset balance"
            return
        }

        // Deduct balance
        val updated = when (asset) {
            "USDT" -> curWallet.copy(usdtBalance = curWallet.usdtBalance - amount)
            "ETH" -> curWallet.copy(ethBalance = curWallet.ethBalance - amount)
            "wBTC" -> curWallet.copy(wbtcBalance = curWallet.wbtcBalance - amount)
            "HEXA" -> curWallet.copy(hexaBalance = curWallet.hexaBalance - amount)
            else -> curWallet
        }
        _web3Wallet.value = updated

        val txHash = Web3Manager.generateTxHash()
        val block = _activeNetwork.value.blockHeight + 1

        viewModelScope.launch {
            dao.insertPayout(
                PayoutEntity(
                    id = "tx-${System.currentTimeMillis() % 100000}",
                    txHash = txHash,
                    blockNumber = block,
                    amount = amount,
                    asset = asset,
                    recipientAddress = recipient,
                    timestamp = System.currentTimeMillis(),
                    status = "SIMULATED",
                    gasUsedGwei = gasPriceGwei,
                    contractMethod = "transferERC20"
                )
            )

            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "OK",
                    category = "WALLET",
                    message = "IN-APP WALLET SEND: Sent $amount $asset to ${recipient.take(10)}... (TX: ${txHash.take(12)}...)"
                )
            )

            sendSlackAlert("💸 In-App Wallet Outgoing Transfer: Sent $amount $asset to $recipient (TX: $txHash)")
        }

        _toastMessage.value = "Sent $amount $asset successfully!"
    }

    fun sendSlackAlert(message: String, channel: String = "#hexamine-mining-ops") {
        viewModelScope.launch {
            dao.insertLog(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = "INFO",
                    category = "SLACK",
                    message = "[$channel] Slack Alert Dispatched: $message"
                )
            )
        }
    }

    fun clearSystemLogs() {
        viewModelScope.launch {
            dao.clearLogs()
            _toastMessage.value = "Terminal logs cleared"
        }
    }
}
