package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.HexamineViewModel
import com.example.ui.components.HeaderBar
import com.example.ui.components.SimulatedBanner
import com.example.ui.components.NavigationTabBar
import com.example.ui.dialogs.AdvancedAuthDialog
import com.example.ui.dialogs.ApiAndIntegrationsDialog
import com.example.ui.dialogs.ConnectInfraDialog
import com.example.ui.dialogs.WalletConnectDialog
import com.example.ui.screens.AiSentinelScreen
import com.example.ui.screens.BlockchainTxScreen
import com.example.ui.screens.BreakerMechanicalCoreScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DexScreen
import com.example.ui.screens.InAppWalletScreen
import com.example.ui.screens.LogsScreen
import com.example.ui.screens.PayoutsScreen
import com.example.ui.screens.QuantumScreen
import com.example.ui.screens.RigsScreen
import com.example.ui.screens.StratumScreen
import com.example.ui.screens.Web3Screen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HexamineApp()
            }
        }
    }
}

@Composable
fun HexamineApp(
    viewModel: HexamineViewModel = viewModel()
) {
    val context = LocalContext.current
    val kpis by viewModel.kpis.collectAsState()
    val rigs by viewModel.rigs.collectAsState()
    val pools by viewModel.pools.collectAsState()
    val payouts by viewModel.payouts.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val web3Wallet by viewModel.web3Wallet.collectAsState()
    val inAppWallet by viewModel.inAppWallet.collectAsState()
    val activeNetwork by viewModel.activeNetwork.collectAsState()
    val stratumState by viewModel.stratumState.collectAsState()
    val quantumTelemetry by viewModel.quantumTelemetry.collectAsState()
    val pendingAuthPayload by viewModel.pendingAuthPayload.collectAsState()
    val marketState by viewModel.marketFeed.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()

    // Dialog visibility states
    var isConnectInfraOpen by remember { mutableStateOf(false) }
    var isWalletConnectOpen by remember { mutableStateOf(false) }
    var isApiIntegrationsOpen by remember { mutableStateOf(false) }

    // Toast listener
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = Color(0xFF050810)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF050810))
        ) {
            // Simulated-mode banner (visible on every screen)
            SimulatedBanner()

            // Header Bar
            HeaderBar(
                isFleetMode = kpis.isFleetMode,
                isCurtailmentActive = kpis.isCurtailmentActive,
                walletAddress = web3Wallet.address,
                onToggleFleetCurtailment = { viewModel.toggleFleetCurtailment() },
                onOpenConnectInfra = { isConnectInfraOpen = true },
                onOpenAdvancedAuth = {
                    viewModel.requestAdvancedAuth(
                        actionName = "AUTHENTICATE_OPERATOR",
                        contractAddress = com.example.model.SolidityContracts.MINING_PROTOCOL_ADDRESS,
                        functionSig = "authenticateOperator(address,bytes32)",
                        params = "Operator: ${web3Wallet.address}, Dilithium-5 Proof Active"
                    )
                },
                onOpenWalletConnect = { isWalletConnectOpen = true },
                onOpenIntegrations = { isApiIntegrationsOpen = true },
                onNavigateToWallet = { viewModel.setTab("wallet") },
                onNavigateToBreakerCore = { viewModel.setTab("breaker_core") },
                marketState = marketState
            )

            // Navigation Tab Bar
            NavigationTabBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.setTab(it) },
                activeRigsCount = rigs.count { it.status == "ONLINE" }
            )

            // Main Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (activeTab) {
                    "dashboard" -> DashboardScreen(
                        kpis = kpis,
                        rigs = rigs,
                        logs = logs,
                        onOpenConnect = { isConnectInfraOpen = true },
                        onNavigateToRigs = { viewModel.setTab("rigs") },
                        onNavigateToWeb3 = { viewModel.setTab("web3") },
                        onNavigateToPayouts = { viewModel.setTab("payouts") },
                        onTriggerPayout = { viewModel.triggerManualPayoutInUsdt() },
                        onTriggerAllocation = { viewModel.triggerHashPowerAllocationOnChain("HEXA Pool", kpis.fleetHashrateTh) },
                        onToggleCurtailment = { viewModel.toggleFleetCurtailment() },
                        marketState = marketState,
                        onNavigateToBreakerCore = { viewModel.setTab("breaker_core") },
                        onRefreshMarketFeed = { viewModel.refreshMarketFeed() }
                    )

                    "breaker_core" -> BreakerMechanicalCoreScreen(
                        kpis = kpis,
                        marketState = marketState,
                        sharesAccepted = stratumState.acceptedSharesCount,
                        onRefreshMarketFeed = { viewModel.refreshMarketFeed() }
                    )

                    "wallet" -> InAppWalletScreen(
                        inAppWallet = inAppWallet,
                        web3Wallet = web3Wallet,
                        activeNetwork = activeNetwork,
                        recentTxs = payouts,
                        onSendTokens = { rec, ast, amt -> viewModel.sendTokens(rec, ast, amt) },
                        onLinkAsPayoutRecipient = { viewModel.setInAppWalletAsPayoutRecipient() },
                        onGenerateNewWallet = { viewModel.generateNewInAppWallet() },
                        onImportWallet = { input -> viewModel.importInAppWallet(input) },
                        onMarkBackedUp = { viewModel.markWalletBackedUp() }
                    )

                    "rigs" -> RigsScreen(
                        rigs = rigs,
                        onOpenAddRig = { isConnectInfraOpen = true },
                        onSetProfile = { rigId, profile -> viewModel.setRigProfile(rigId, profile) },
                        onRebootRig = { rigId -> viewModel.rebootRig(rigId) },
                        onDeleteRig = { rigId -> viewModel.deleteRig(rigId) }
                    )

                    "stratum" -> StratumScreen(
                        stratumState = stratumState,
                        pools = pools,
                        onSelectPool = { viewModel.setActivePool(it) }
                    )

                    "web3" -> Web3Screen(
                        wallet = web3Wallet,
                        activeNetwork = activeNetwork,
                        onSelectNetwork = { viewModel.selectNetwork(it) },
                        onRequestAuth = { action, contract, sig, params ->
                            viewModel.requestAdvancedAuth(action, contract, sig, params)
                        }
                    )

                    "payouts" -> PayoutsScreen(
                        payouts = payouts,
                        connectedWalletAddress = web3Wallet.address,
                        onOpenConnectWallet = { isWalletConnectOpen = true },
                        onTriggerPayout = { viewModel.triggerManualPayoutInUsdt() }
                    )

                    "gated_tokens" -> com.example.ui.screens.GatedTokensScreen(
                        currentHashrateTh = kpis.fleetHashrateTh,
                        currentSharesCount = stratumState.acceptedSharesCount,
                        onClaimGate = { gateId, tokenAmt ->
                            viewModel.requestAdvancedAuth(
                                actionName = "CLAIM_GATED_TOKENS",
                                contractAddress = com.example.model.SolidityContracts.GATED_TOKEN_VAULT_ADDRESS,
                                functionSig = "claimGatedTokens(uint256,bytes)",
                                params = "Gate #$gateId, Unlocking +${tokenAmt.toInt()} \$HEXA with Quantum Proof"
                            )
                        }
                    )

                    "dex" -> DexScreen(
                        wallet = web3Wallet,
                        onExecuteSwap = { from, to, inAmt, outAmt ->
                            viewModel.executeDexSwap(from, to, inAmt, outAmt)
                        }
                    )

                    "blockchain_tx" -> BlockchainTxScreen(
                        activeNetwork = activeNetwork,
                        payouts = payouts,
                        onBroadcastNewTx = {
                            viewModel.requestAdvancedAuth(
                                actionName = "BROADCAST_TEST_TRANSACTION",
                                contractAddress = com.example.model.SolidityContracts.PAYOUT_VAULT_ADDRESS,
                                functionSig = "simulateMempoolTransaction(bytes32,uint256)",
                                params = "ChainId: ${activeNetwork.chainId}, Gas: ${activeNetwork.gasPriceGwei} Gwei"
                            )
                        }
                    )

                    "ai_sentinel" -> AiSentinelScreen(
                        onTriggerAiCalibration = {
                            viewModel.triggerAiAutonomousPoolCreationAndFunding()
                        }
                    )

                    "quantum" -> QuantumScreen(
                        telemetry = quantumTelemetry,
                        onRegenerateEntropy = {
                            viewModel.triggerShareBatchVerificationOnChain()
                        }
                    )

                    "logs" -> LogsScreen(
                        logs = logs,
                        onClearLogs = { viewModel.clearSystemLogs() }
                    )
                }
            }
        }

        // Dialogs
        AdvancedAuthDialog(
            payload = pendingAuthPayload,
            onDismiss = { viewModel.dismissAdvancedAuth() },
            onConfirm = { viewModel.confirmAdvancedAuth() }
        )

        ConnectInfraDialog(
            isOpen = isConnectInfraOpen,
            onDismiss = { isConnectInfraOpen = false },
            onAddRig = { name, model, ip, hash, power ->
                viewModel.addRig(name, model, ip, hash, power)
            }
        )

        WalletConnectDialog(
            isOpen = isWalletConnectOpen,
            currentAddress = web3Wallet.address,
            onDismiss = { isWalletConnectOpen = false },
            onConnectWallet = { prov, addr ->
                viewModel.connectWallet(prov, addr)
            }
        )

        ApiAndIntegrationsDialog(
            isOpen = isApiIntegrationsOpen,
            onDismiss = { isApiIntegrationsOpen = false },
            onTestWebSocket = {
                // Handled in dialog
            },
            onTestSlackAlert = { webhook, channel ->
                viewModel.sendSlackAlert("🔔 Breaker Mining Ops Test: Fleet Hashrate: ${"%.1f".format(kpis.fleetHashrateTh)} TH/s · Active Rigs: ${kpis.activeRigsCount} · Automated USDT Payouts Armed.", channel)
                Toast.makeText(context, "Slack Alert Sent to $channel", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
