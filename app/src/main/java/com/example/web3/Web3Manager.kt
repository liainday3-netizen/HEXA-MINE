package com.example.web3

import com.example.quantum.QuantumSecurityEngine
import java.security.MessageDigest
import kotlin.random.Random

data class Web3Network(
    val id: String,
    val name: String,
    val chainId: Long,
    val rpcUrl: String,
    val currency: String,
    val blockExplorer: String,
    val blockHeight: Long = 21458920,
    val gasPriceGwei: Double = 16.5
)

data class Web3Wallet(
    val address: String = "0x63FaC7182281D59c60e32E69dC8Fea920215D3AA",
    val usdtBalance: Double = 1482.50,
    val ethBalance: Double = 1.845,
    val wbtcBalance: Double = 0.1420,
    val hexaBalance: Double = 25000.0,
    val isConnected: Boolean = true,
    val postQuantumAddress: String = "0xPQ9F18AC74BD821EEF0910AA37284A5961BC4271"
)

data class InAppWalletData(
    val address: String = "0x63FaC7182281D59c60e32E69dC8Fea920215D3AA",
    val privateKey: String = "0x4a8f9c2d1e0b573a69dc84fe02157a39b4f2c018a7d369e5f1b20489c71a3d02",
    val mnemonic: List<String> = listOf(
        "cyber", "breaker", "matrix", "quantum", "vault", "flame",
        "orbit", "mining", "strata", "pulse", "neon", "shield"
    ),
    val isBackedUp: Boolean = true,
    val isLinkedForUsdtPayouts: Boolean = true,
    val walletName: String = "Breaker Primary Vault",
    val postQuantumKeyId: String = "DILITHIUM5-BRK-9921"
)

data class AdvancedAuthPayload(
    val actionName: String,
    val targetContract: String,
    val functionSignature: String,
    val paramSummary: String,
    val nonce: Long,
    val gasEstimateGwei: Double,
    val eip712DomainSeparator: String,
    val ecdsaSignature: String,
    val postQuantumProof: String
)

object Web3Manager {

    val SUPPORTED_NETWORKS = listOf(
        Web3Network("eth_mainnet", "Ethereum Mainnet", 1, "https://eth.llamarpc.com", "ETH", "https://etherscan.io", 21458920, 18.2),
        Web3Network("arbitrum", "Arbitrum One", 42161, "https://arb1.arbitrum.io/rpc", "ETH", "https://arbiscan.io", 280911440, 0.15),
        Web3Network("base", "Base L2", 8453, "https://mainnet.base.org", "ETH", "https://basescan.org", 24190820, 0.08),
        Web3Network("sepolia", "Sepolia Testnet", 11155111, "https://rpc.sepolia.org", "SepoliaETH", "https://sepolia.etherscan.io", 7421900, 2.5),
        Web3Network("hut8_subnet", "Hut 8 Enterprise EVM", 8888, "https://rpc.hut8.hexamine.network", "HUT8", "https://explorer.hut8.network", 894100, 0.01)
    )

    private val MNEMONIC_DICTIONARY = listOf(
        "matrix", "breaker", "quantum", "entropy", "stratum", "solidity",
        "orbital", "cipher", "flux", "genesis", "nexus", "pulse",
        "vault", "crypto", "flame", "sharding", "asics", "hashrate",
        "tether", "plasma", "shield", "beacon", "relay", "vector"
    )

    fun generateNewInAppWallet(): InAppWalletData {
        val mnemonicWords = (0 until 12).map { MNEMONIC_DICTIONARY.random() }
        val seed = mnemonicWords.joinToString(":")
        val privKeyHash = keccak256("BREAKER_PRIV_KEY:$seed:${System.currentTimeMillis()}")
        val addressHash = keccak256("BREAKER_PUB_KEY:$privKeyHash").take(40)
        val address = "0x" + addressHash

        return InAppWalletData(
            address = address,
            privateKey = "0x$privKeyHash",
            mnemonic = mnemonicWords,
            isBackedUp = false,
            isLinkedForUsdtPayouts = false,
            walletName = "Breaker Vault #${Random.nextInt(100, 999)}",
            postQuantumKeyId = "DILITHIUM5-BRK-${Random.nextInt(1000, 9999)}"
        )
    }

    fun importInAppWallet(input: String): InAppWalletData {
        val clean = input.trim()
        val words = clean.split("\\s+".toRegex())
        val (mnemonic, privKey, address) = if (words.size >= 12) {
            val privHash = keccak256("IMPORT_SEED:${clean}")
            val addr = "0x" + keccak256("IMPORT_PUB:${privHash}").take(40)
            Triple(words.take(12), "0x$privHash", addr)
        } else {
            val key = if (clean.startsWith("0x")) clean else "0x$clean"
            val addr = "0x" + keccak256("KEY_TO_ADDR:$key").take(40)
            Triple(listOf("imported", "private", "key", "account", "secure", "breaker", "mining", "vault", "evm", "wallet", "live", "node"), key, addr)
        }

        return InAppWalletData(
            address = address,
            privateKey = privKey,
            mnemonic = mnemonic,
            isBackedUp = true,
            isLinkedForUsdtPayouts = true,
            walletName = "Imported Web3 Wallet",
            postQuantumKeyId = "DILITHIUM5-IMP-${Random.nextInt(1000, 9999)}"
        )
    }

    fun createAdvancedAuthPayload(
        actionName: String,
        contractAddress: String,
        functionSig: String,
        params: String,
        nonce: Long = Random.nextLong(100, 9999)
    ): AdvancedAuthPayload {
        val domain = "EIP712Domain(string name,string version,uint256 chainId,address verifyingContract)"
        val domainHash = keccak256("$domain:HexamineMiningProtocol:2.0:1:$contractAddress")
        val messageHash = keccak256("$actionName:$contractAddress:$functionSig:$params:$nonce")
        val ecdsaSig = "0x" + keccak256("ECDSA:$domainHash:$messageHash") + "1b"
        val pqProof = QuantumSecurityEngine.signWithPostQuantumDilithium(messageHash, "HEXAMINE_SEED_${System.currentTimeMillis()}")

        return AdvancedAuthPayload(
            actionName = actionName,
            targetContract = contractAddress,
            functionSignature = functionSig,
            paramSummary = params,
            nonce = nonce,
            gasEstimateGwei = Random.nextDouble(12.0, 24.0),
            eip712DomainSeparator = "0x$domainHash",
            ecdsaSignature = ecdsaSig,
            postQuantumProof = pqProof
        )
    }

    fun generateTxHash(): String {
        val raw = "tx_${System.currentTimeMillis()}_${Random.nextLong()}"
        return "0x" + keccak256(raw)
    }

    fun keccak256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(input.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
