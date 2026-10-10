package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.PayoutEntity
import com.example.model.SolidityContracts
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexQuantumPurple
import com.example.ui.theme.HexRed
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard
import com.example.web3.InAppWalletData
import com.example.web3.Web3Network
import com.example.web3.Web3Wallet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InAppWalletScreen(
    inAppWallet: InAppWalletData,
    web3Wallet: Web3Wallet,
    activeNetwork: Web3Network,
    recentTxs: List<PayoutEntity>,
    onSendTokens: (recipient: String, asset: String, amount: Double) -> Unit,
    onLinkAsPayoutRecipient: () -> Unit,
    onGenerateNewWallet: () -> Unit,
    onImportWallet: (input: String) -> Unit,
    onMarkBackedUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // Dialog states
    var isSendDialogOpen by remember { mutableStateOf(false) }
    var isReceiveDialogOpen by remember { mutableStateOf(false) }
    var isBackupDialogOpen by remember { mutableStateOf(false) }
    var isImportDialogOpen by remember { mutableStateOf(false) }
    var preselectedAsset by remember { mutableStateOf("USDT") }

    // Calculated portfolio total in USD
    val btcPrice = 94820.0
    val ethPrice = 2840.0
    val totalUsdValue = web3Wallet.usdtBalance +
        (web3Wallet.ethBalance * ethPrice) +
        (web3Wallet.wbtcBalance * btcPrice) +
        (web3Wallet.hexaBalance * 0.10) // $HEXA pegged ecosystem estimate

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "IN-APP WEB3 WALLET (END-TO-END)",
                    color = HexGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "NATIVE EVM VAULT · 12-WORD BIP-39 · AUTOMATED BREAKER MINING PAYOUTS",
                    color = Color(0x99FFAA00),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(HexTeal.copy(alpha = 0.2f))
                    .border(0.8.dp, HexTeal, RoundedCornerShape(3.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = activeNetwork.name.take(12),
                    color = HexTeal,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Master Portfolio Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(HexamineCard)
                .border(1.2.dp, HexGold, RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(HexGold.copy(alpha = 0.2f))
                                .border(1.dp, HexGold, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet", tint = HexGold, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(inAppWallet.walletName, color = HexGoldBright, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Text(
                                text = "${inAppWallet.address.take(8)}...${inAppWallet.address.takeLast(6)}",
                                color = Color.LightGray,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Payout Link status badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (inAppWallet.isLinkedForUsdtPayouts) HexUsdtGreen.copy(alpha = 0.2f) else HexRed.copy(alpha = 0.2f))
                            .border(0.8.dp, if (inAppWallet.isLinkedForUsdtPayouts) HexUsdtGreen else HexRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (inAppWallet.isLinkedForUsdtPayouts) "● USDT PAYOUT LINKED" else "○ PAYOUT UNLINKED",
                            color = if (inAppWallet.isLinkedForUsdtPayouts) HexUsdtGreen else HexRed,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Total Value Header
                Column {
                    Text("TOTAL VAULT VALUE", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "\$${"%,.2f".format(totalUsdValue)} USD",
                        color = HexGoldBright,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Quick Action Buttons Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Send
                    Button(
                        onClick = {
                            preselectedAsset = "USDT"
                            isSendDialogOpen = true
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HexGold, contentColor = Color.Black)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = "Send", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SEND", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Receive
                    Button(
                        onClick = { isReceiveDialogOpen = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HexTeal, contentColor = Color.Black)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = "Receive", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("RECEIVE", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Link Payouts
                    Button(
                        onClick = { onLinkAsPayoutRecipient() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inAppWallet.isLinkedForUsdtPayouts) HexUsdtGreen else HexBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Link, contentDescription = "Link", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (inAppWallet.isLinkedForUsdtPayouts) "LINKED" else "SET RECIPIENT", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Secondary row actions (Backup & Import/New)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (inAppWallet.isBackedUp) "✓ 12-Word Seed Phrase Backed Up" else "⚠ Seed Phrase Needs Backup",
                        color = if (inAppWallet.isBackedUp) HexGreen else HexRed,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.clickable { isBackupDialogOpen = true }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "BACKUP SEED",
                            color = HexTeal,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { isBackupDialogOpen = true }
                        )
                        Text(
                            text = "IMPORT / NEW",
                            color = HexGold,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { isImportDialogOpen = true }
                        )
                    }
                }
            }
        }

        // Automated Breaker Mining Payout Gateway Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF080D1A))
                .border(1.dp, HexUsdtGreen.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CurrencyBitcoin, contentDescription = "Breaker", tint = HexUsdtGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("BREAKER MINING AUTOMATED PAYOUT GATEWAY", color = HexUsdtGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Text("VAULT: ACTIVE", color = HexGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "Incoming Breaker ASIC mining yields automatically stream in Tether USD (USDT ERC-20) into this in-app wallet whenever accepted stratum shares hit 10.00 USDT threshold.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Target Vault: ${SolidityContracts.PAYOUT_VAULT_ADDRESS.take(12)}...", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "Recipient: ${inAppWallet.address.take(10)}...",
                        color = HexGoldBright,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Asset Holdings Breakdown
        Text(
            text = "HOLDINGS & CRYPTOGRAPHIC ASSETS:",
            color = HexGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        // USDT Asset Card
        AssetHoldingRow(
            symbol = "USDT",
            name = "Tether USD (ERC-20)",
            balance = "${"%,.2f".format(web3Wallet.usdtBalance)} USDT",
            usdValue = "\$${"%,.2f".format(web3Wallet.usdtBalance)}",
            badge = "PAYOUT ASSET",
            badgeColor = HexUsdtGreen,
            onSend = {
                preselectedAsset = "USDT"
                isSendDialogOpen = true
            },
            onReceive = { isReceiveDialogOpen = true }
        )

        // ETH Asset Card
        AssetHoldingRow(
            symbol = "ETH",
            name = "Ethereum (Native Gas)",
            balance = "${web3Wallet.ethBalance} ETH",
            usdValue = "\$${"%,.2f".format(web3Wallet.ethBalance * ethPrice)}",
            badge = "EVM GAS",
            badgeColor = HexBlue,
            onSend = {
                preselectedAsset = "ETH"
                isSendDialogOpen = true
            },
            onReceive = { isReceiveDialogOpen = true }
        )

        // wBTC Asset Card
        AssetHoldingRow(
            symbol = "wBTC",
            name = "Wrapped Bitcoin",
            balance = "${web3Wallet.wbtcBalance} BTC",
            usdValue = "\$${"%,.2f".format(web3Wallet.wbtcBalance * btcPrice)}",
            badge = "BREAKER SHARES",
            badgeColor = HexGold,
            onSend = {
                preselectedAsset = "wBTC"
                isSendDialogOpen = true
            },
            onReceive = { isReceiveDialogOpen = true }
        )

        // $HEXA Gated Ecosystem Token
        AssetHoldingRow(
            symbol = "\$HEXA",
            name = "Hexamine Breaker Protocol",
            balance = "${"%,.0f".format(web3Wallet.hexaBalance)} HEXA",
            usdValue = "\$${"%,.2f".format(web3Wallet.hexaBalance * 0.10)}",
            badge = "UNRELEASED GATED",
            badgeColor = HexGreen,
            onSend = {
                preselectedAsset = "HEXA"
                isSendDialogOpen = true
            },
            onReceive = { isReceiveDialogOpen = true }
        )

        // Recent In-App Wallet Transactions Ledger
        Text(
            text = "RECENT WALLET TRANSACTIONS & TRANSFERS:",
            color = HexGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        val dateFormat = SimpleDateFormat("HH:mm:ss · dd MMM", Locale.US)

        if (recentTxs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF080D1A))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No transactions recorded yet in this wallet", color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                recentTxs.take(6).forEach { tx ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF080D1A))
                            .border(0.8.dp, HexamineBorder, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ReceiptLong,
                                        contentDescription = "Tx",
                                        tint = if (tx.asset == "USDT") HexUsdtGreen else HexBlue,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${tx.amount} ${tx.asset}",
                                        color = if (tx.asset == "USDT") HexUsdtGreen else HexGoldBright,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tx.contractMethod,
                                        color = Color.Gray,
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Text(
                                    text = "TX: ${tx.txHash.take(14)}... · ${dateFormat.format(Date(tx.timestamp))}",
                                    color = Color.LightGray,
                                    fontSize = 7.5.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(HexGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(tx.status, color = HexGreen, fontSize = 7.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // --- SEND TOKENS DIALOG ---
    if (isSendDialogOpen) {
        SendTokensDialog(
            availableAsset = preselectedAsset,
            web3Wallet = web3Wallet,
            activeNetwork = activeNetwork,
            onDismiss = { isSendDialogOpen = false },
            onConfirmSend = { rec, ast, amt ->
                onSendTokens(rec, ast, amt)
                isSendDialogOpen = false
            }
        )
    }

    // --- RECEIVE TOKENS DIALOG ---
    if (isReceiveDialogOpen) {
        ReceiveTokensDialog(
            inAppAddress = inAppWallet.address,
            activeNetwork = activeNetwork,
            onDismiss = { isReceiveDialogOpen = false }
        )
    }

    // --- SEED BACKUP DIALOG ---
    if (isBackupDialogOpen) {
        SeedBackupDialog(
            mnemonicWords = inAppWallet.mnemonic,
            privateKey = inAppWallet.privateKey,
            onDismiss = { isBackupDialogOpen = false },
            onConfirmBackedUp = {
                onMarkBackedUp()
                isBackupDialogOpen = false
            }
        )
    }

    // --- IMPORT / NEW WALLET DIALOG ---
    if (isImportDialogOpen) {
        ImportOrNewWalletDialog(
            onDismiss = { isImportDialogOpen = false },
            onGenerateNew = {
                onGenerateNewWallet()
                isImportDialogOpen = false
            },
            onImport = { input ->
                onImportWallet(input)
                isImportDialogOpen = false
            }
        )
    }
}

@Composable
fun AssetHoldingRow(
    symbol: String,
    name: String,
    balance: String,
    usdValue: String,
    badge: String,
    badgeColor: Color,
    onSend: () -> Unit,
    onReceive: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF080D1A))
            .border(0.8.dp, HexamineBorder, RoundedCornerShape(4.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(symbol, color = HexGoldBright, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(badgeColor.copy(alpha = 0.2f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(badge, color = badgeColor, fontSize = 7.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
                Text(name, color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(balance, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Text(usdValue, color = HexTeal, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexGold.copy(alpha = 0.15f))
                            .clickable { onSend() }
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text("SEND", color = HexGold, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexTeal.copy(alpha = 0.15f))
                            .clickable { onReceive() }
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text("RECV", color = HexTeal, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SendTokensDialog(
    availableAsset: String,
    web3Wallet: Web3Wallet,
    activeNetwork: Web3Network,
    onDismiss: () -> Unit,
    onConfirmSend: (recipient: String, asset: String, amount: Double) -> Unit
) {
    var selectedAsset by remember { mutableStateOf(availableAsset) }
    var recipientAddress by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val maxAmount = when (selectedAsset) {
        "USDT" -> web3Wallet.usdtBalance
        "ETH" -> web3Wallet.ethBalance
        "wBTC" -> web3Wallet.wbtcBalance
        "HEXA" -> web3Wallet.hexaBalance
        else -> 0.0
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF080D1A))
                .border(1.2.dp, HexGold, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Send", tint = HexGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SEND ASSETS (ON-CHAIN)", color = HexGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp).clickable { onDismiss() }
                    )
                }

                // Asset Selector Chips
                Text("SELECT CRYPTOGRAPHIC ASSET:", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("USDT", "ETH", "wBTC", "HEXA").forEach { ast ->
                        val isCur = selectedAsset == ast
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isCur) HexGold.copy(alpha = 0.25f) else Color(0xFF050810))
                                .border(1.dp, if (isCur) HexGold else HexamineBorder, RoundedCornerShape(4.dp))
                                .clickable { selectedAsset = ast }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(ast, color = if (isCur) HexGoldBright else Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Recipient Address Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("RECIPIENT ETHEREUM ADDRESS:", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "PASTE",
                        color = HexTeal,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            val clip = clipboardManager.getText()?.text
                            if (!clip.isNullOrBlank()) {
                                recipientAddress = clip
                                Toast.makeText(context, "Pasted address", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                OutlinedTextField(
                    value = recipientAddress,
                    onValueChange = { recipientAddress = it },
                    placeholder = { Text("0x... (42 chars)", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HexGold,
                        unfocusedBorderColor = HexamineBorder,
                        focusedTextColor = HexGoldBright,
                        unfocusedTextColor = HexGoldBright
                    )
                )

                // Amount Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("AMOUNT TO TRANSFER:", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "MAX: ${"%.2f".format(maxAmount)} $selectedAsset",
                        color = HexTeal,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            amountInput = maxAmount.toString()
                        }
                    )
                }
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    placeholder = { Text("0.00", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HexGold,
                        unfocusedBorderColor = HexamineBorder,
                        focusedTextColor = HexGoldBright,
                        unfocusedTextColor = HexGoldBright
                    )
                )

                // Network & Gas fee preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF050810))
                        .border(0.8.dp, HexamineBorder, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Network:", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            Text(activeNetwork.name, color = Color.White, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Gas Estimate:", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            Text("${activeNetwork.gasPriceGwei} Gwei (≈ \$0.38 USD)", color = HexTeal, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Post-Quantum Proof:", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            Text("Dilithium-5 Attached", color = HexQuantumPurple, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                // Submit Button
                Button(
                    onClick = {
                        val parsed = amountInput.toDoubleOrNull() ?: 0.0
                        onConfirmSend(recipientAddress, selectedAsset, parsed)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HexGold, contentColor = Color.Black)
                ) {
                    Text("SIGN & BROADCAST TRANSACTION", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReceiveTokensDialog(
    inAppAddress: String,
    activeNetwork: Web3Network,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF080D1A))
                .border(1.2.dp, HexTeal, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.QrCode, contentDescription = "Receive", tint = HexTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("RECEIVE ASSETS & PAYOUTS", color = HexTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp).clickable { onDismiss() }
                    )
                }

                Text(
                    text = "SEND ONLY ERC-20 / EVM ASSETS ON ${activeNetwork.name.uppercase()}",
                    color = HexGoldBright,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                // High-Tech QR Matrix Canvas
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(144.dp)) {
                        val gridSize = 15
                        val cellSize = size.width / gridSize
                        val pattern = listOf(
                            "111110010011111",
                            "100010101010001",
                            "101010011010101",
                            "100010100010001",
                            "111110101011111",
                            "000000110000000",
                            "101101010110101",
                            "010011101110010",
                            "101101010110101",
                            "000000110000000",
                            "111110101011111",
                            "100010011010001",
                            "101010100010101",
                            "100010111010001",
                            "111110001011111"
                        )
                        for (r in 0 until gridSize) {
                            for (c in 0 until gridSize) {
                                if (pattern[r][c] == '1') {
                                    drawRect(
                                        color = Color(0xFF080D1A),
                                        topLeft = Offset(c * cellSize, r * cellSize),
                                        size = Size(cellSize, cellSize)
                                    )
                                }
                            }
                        }
                    }
                }

                // Address display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF050810))
                        .border(0.8.dp, HexamineBorder, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = inAppAddress,
                        color = HexGoldBright,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Copy button
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(inAppAddress))
                        Toast.makeText(context, "Wallet address copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HexTeal, contentColor = Color.Black)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("COPY ETHEREUM ADDRESS", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SeedBackupDialog(
    mnemonicWords: List<String>,
    privateKey: String,
    onDismiss: () -> Unit,
    onConfirmBackedUp: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var showPrivateKey by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF080D1A))
                .border(1.2.dp, HexQuantumPurple, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = "Backup", tint = HexQuantumPurple, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("BACKUP SEED & SECRET KEYS", color = HexQuantumPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp).clickable { onDismiss() }
                    )
                }

                Text(
                    text = "SIMULATED WALLET: THESE ARE NOT REAL KEYS. NEVER SEND FUNDS HERE AND NEVER ENTER A REAL SEED PHRASE IN THIS APP.",
                    color = HexRed,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                // 12-Word Mnemonic Grid (4 rows of 3)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    mnemonicWords.chunked(3).forEachIndexed { rowIndex, chunk ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            chunk.forEachIndexed { colIndex, word ->
                                val wordNum = (rowIndex * 3) + colIndex + 1
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color(0xFF050810))
                                        .border(0.8.dp, HexQuantumPurple.copy(alpha = 0.5f), RoundedCornerShape(3.dp))
                                        .padding(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "$wordNum. $word",
                                        color = HexGoldBright,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Copy Seed Phrase Button
                OutlinedButton(
                    onClick = {
                        val allWords = mnemonicWords.joinToString(" ")
                        clipboardManager.setText(AnnotatedString(allWords))
                        Toast.makeText(context, "12-word seed phrase copied", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HexQuantumPurple)
                ) {
                    Text("COPY 12-WORD MNEMONIC", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }

                // Private key reveal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("RAW EVM PRIVATE KEY:", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = if (showPrivateKey) "HIDE" else "SHOW",
                        color = HexTeal,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { showPrivateKey = !showPrivateKey }
                    )
                }

                if (showPrivateKey) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF03050A))
                            .border(0.8.dp, HexamineBorder, RoundedCornerShape(4.dp))
                            .padding(6.dp)
                    ) {
                        Text(
                            text = privateKey,
                            color = HexGoldBright,
                            fontSize = 7.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Confirm Backed Up Button
                Button(
                    onClick = onConfirmBackedUp,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HexGreen, contentColor = Color.Black)
                ) {
                    Text("I HAVE SAVED MY SEED PHRASE", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ImportOrNewWalletDialog(
    onDismiss: () -> Unit,
    onGenerateNew: () -> Unit,
    onImport: (input: String) -> Unit
) {
    var importInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF080D1A))
                .border(1.2.dp, HexGold, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("NEW / IMPORT WALLET", color = HexGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp).clickable { onDismiss() }
                    )
                }

                // Option A: Generate New
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF050810))
                        .border(0.8.dp, HexTeal.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("OPTION A: CREATE FRESH HD WALLET", color = HexTeal, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Text("Generates a new 12-word seed phrase, Dilithium-5 post-quantum keypair, and Ethereum address.", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Button(
                            onClick = onGenerateNew,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HexTeal, contentColor = Color.Black)
                        ) {
                            Text("GENERATE NEW IN-APP WALLET", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Option B: Import Seed or Key
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF050810))
                        .border(0.8.dp, HexGold.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("OPTION B: IMPORT SEED OR PRIVATE KEY", color = HexGoldBright, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        OutlinedTextField(
                            value = importInput,
                            onValueChange = { importInput = it },
                            placeholder = { Text("Enter 12 words or 0x private key", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HexGold,
                                unfocusedBorderColor = HexamineBorder,
                                focusedTextColor = HexGoldBright,
                                unfocusedTextColor = HexGoldBright
                            )
                        )
                        Button(
                            onClick = {
                                if (importInput.isNotBlank()) {
                                    onImport(importInput)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HexGold, contentColor = Color.Black)
                        ) {
                            Text("IMPORT WALLET", fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
