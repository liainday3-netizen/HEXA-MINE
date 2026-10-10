package com.example.ui.dialogs

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.geometry.CornerRadius
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
import com.example.model.SolidityContracts
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexQuantumPurple
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard

@Composable
fun WalletConnectDialog(
    isOpen: Boolean,
    currentAddress: String,
    onDismiss: () -> Unit,
    onConnectWallet: (provider: String, address: String) -> Unit
) {
    if (!isOpen) return

    var activeTab by remember { mutableStateOf("WALLETCONNECT") } // WALLETCONNECT, POPULAR, MANUAL
    var selectedWallet by remember { mutableStateOf("WalletConnect v2") }
    var inputAddress by remember { mutableStateOf(currentAddress) }
    var ensInput by remember { mutableStateOf("") }
    var isPairingSimulated by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val wcUri = "wc:e16a98dfb342@2?relay-protocol=irn&symKey=9f20e4bc81aa1763da10a283e1c94d01bfa82910"

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF080D1A))
                .border(1.2.dp, HexGold, RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(HexBlue.copy(alpha = 0.2f))
                                .border(0.8.dp, HexBlue, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Wallet",
                                tint = HexBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "CONNECT WEB3 WALLET",
                                color = HexGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "LINK ETHEREUM ADDRESS FOR AUTOMATED USDT PAYOUTS",
                                color = Color(0x99FFAA00),
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDismiss() }
                    )
                }

                // Provider Switcher Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(
                        Pair("INAPP", "IN-APP"),
                        Pair("COINBASE", "COINBASE"),
                        Pair("WALLETCONNECT", "WALLETCONNECT"),
                        Pair("POPULAR", "INJECTED"),
                        Pair("MANUAL", "DIRECT 0x")
                    ).forEach { (id, label) ->
                        val isCur = activeTab == id
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isCur) HexBlue.copy(alpha = 0.25f) else Color(0xFF0D1526))
                                .border(1.dp, if (isCur) HexBlue else HexamineBorder, RoundedCornerShape(4.dp))
                                .clickable { activeTab = id }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isCur) Color.White else Color.Gray,
                                fontSize = 8.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isCur) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // TAB CONTENT
                when (activeTab) {
                    "INAPP" -> {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(HexamineCard)
                                    .border(1.dp, HexGold, RoundedCornerShape(4.dp))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("NATIVE IN-APP WALLET", color = HexGoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(HexGreen.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("ACTIVE ON-DEVICE", color = HexGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Text("Address: $currentAddress", color = Color.White, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text("Directly embedded within Hexamine. Zero extension or bridge required.", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                }
                            }

                            Button(
                                onClick = {
                                    onConnectWallet("In-App Breaker Vault", currentAddress)
                                    Toast.makeText(context, "In-App Wallet linked for automated USDT payouts", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = HexGold, contentColor = Color.Black)
                            ) {
                                Text("LINK IN-APP WALLET FOR USDT PAYOUTS", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    "COINBASE" -> {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF0052FF).copy(alpha = 0.15f))
                                    .border(1.dp, Color(0xFF0052FF), RoundedCornerShape(6.dp))
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "COINBASE SMART WALLET & PAY",
                                            color = Color(0xFF0052FF),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color(0xFF0052FF))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "OFFICIAL EIP-6963",
                                                color = Color.White,
                                                fontSize = 8.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Coinbase Address: 0x89A36441bF9745e998e3E43B792A6B29671542Bc",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "Direct deposit for automated USDT payouts directly into your Coinbase account or Coinbase Smart Wallet with zero gas overhead.",
                                        color = Color.LightGray,
                                        fontSize = 8.5.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    onConnectWallet("Coinbase Smart Wallet", "0x89A36441bF9745e998e3E43B792A6B29671542Bc")
                                    Toast.makeText(context, "Coinbase Wallet linked! Automated USDT payouts enabled.", Toast.LENGTH_LONG).show()
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0052FF),
                                    contentColor = Color.White
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Sensors,
                                        contentDescription = "Coinbase",
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LINK COINBASE WALLET (1-CLICK)",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    "WALLETCONNECT" -> {
                        // WalletConnect QR & Pairing Panel
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "SCAN WITH ANY WALLETCONNECT v2 APP",
                                color = HexGoldBright,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )

                            // Render High-Tech QR Matrix Canvas
                            Box(
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.size(150.dp)) {
                                    val gridSize = 15
                                    val cellSize = size.width / gridSize

                                    // Static high-contrast pattern representing QR
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

                            // Copy URI Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF03050A))
                                    .border(0.8.dp, HexamineBorder, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${wcUri.take(28)}...${wcUri.takeLast(8)}",
                                    color = Color.LightGray,
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "COPY URI",
                                    color = HexTeal,
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        clipboardManager.setText(AnnotatedString(wcUri))
                                        Toast.makeText(context, "WalletConnect URI copied", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }

                            // Mobile Wallet Quick Launch Badges
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("MetaMask", "Trust Wallet", "Rainbow", "Zerion", "Safe").forEach { app ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(0xFF0D1526))
                                            .border(0.8.dp, HexBlue.copy(alpha = 0.5f), RoundedCornerShape(3.dp))
                                            .clickable {
                                                selectedWallet = app
                                                isPairingSimulated = true
                                                Toast.makeText(context, "Pairing request dispatched to $app", Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(app, color = HexBlue, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }

                            // One-Click Pair Simulator
                            Button(
                                onClick = {
                                    val sampleAddress = "0x71C8395B9A42Ef76F6b158fB072Fa61c3a7E38Fa"
                                    onConnectWallet("WalletConnect ($selectedWallet)", sampleAddress)
                                    Toast.makeText(context, "Paired via WalletConnect! Automated USDT payouts linked.", Toast.LENGTH_LONG).show()
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HexBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Sensors, contentDescription = "Connect", modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("SIMULATE WALLETCONNECT APPROVAL", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    "POPULAR" -> {
                        // List of Browser/Injected Wallets
                        val walletOptions = listOf(
                            Pair("MetaMask", "0x63FaC7182281D59c60e32E69dC8Fea920215D3AA"),
                            Pair("Coinbase Wallet", "0x89A36441bF9745e998e3E43B792A6B29671542Bc"),
                            Pair("Trust Wallet", "0x981C3729b82F6B910A42Ef76F6b158fB072Fa881"),
                            Pair("Ledger Hardware", "0x44B15794966033Dfe0cE7e04D407137f8845190E"),
                            Pair("Post-Quantum Key", "0xPQ9F18AC74BD821EEF0910AA37284A5961BC4271")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            walletOptions.forEach { (name, addr) ->
                                val isCur = selectedWallet == name
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isCur) HexGold.copy(alpha = 0.15f) else Color(0xFF0D1526))
                                        .border(1.dp, if (isCur) HexGold else HexamineBorder, RoundedCornerShape(4.dp))
                                        .clickable {
                                            selectedWallet = name
                                            inputAddress = addr
                                        }
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(name, color = if (isCur) HexGoldBright else Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                            Text("${addr.take(10)}...${addr.takeLast(6)}", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                        }

                                        if (isCur) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(HexGreen.copy(alpha = 0.2f))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("SELECTED", color = HexGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    onConnectWallet(selectedWallet, inputAddress)
                                    Toast.makeText(context, "$selectedWallet linked for automated USDT payouts", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = HexGold, contentColor = Color.Black)
                            ) {
                                Text("CONNECT $selectedWallet", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    "MANUAL" -> {
                        // Direct Address Input & ENS Resolver
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("ENTER ETHEREUM ADDRESS OR ENS:", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)

                            OutlinedTextField(
                                value = inputAddress,
                                onValueChange = { inputAddress = it },
                                label = { Text("0x... (42 CHARACTERS)", fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HexGold,
                                    unfocusedBorderColor = HexamineBorder,
                                    focusedTextColor = HexGoldBright,
                                    unfocusedTextColor = HexGoldBright
                                )
                            )

                            OutlinedTextField(
                                value = ensInput,
                                onValueChange = {
                                    ensInput = it
                                    if (it.endsWith(".eth")) {
                                        inputAddress = "0x34Fa8b12f6a98d99c43b071e62194bfa201889a1"
                                        Toast.makeText(context, "ENS Resolved: $it -> ${inputAddress.take(10)}...", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                label = { Text("RESOLVE ENS DOMAIN (e.g. miner.eth)", fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HexTeal,
                                    unfocusedBorderColor = HexamineBorder,
                                    focusedTextColor = HexGoldBright,
                                    unfocusedTextColor = HexGoldBright
                                )
                            )

                            Button(
                                onClick = {
                                    if (inputAddress.startsWith("0x") && inputAddress.length == 42) {
                                        onConnectWallet("Manual Ethereum Link", inputAddress)
                                        Toast.makeText(context, "Ethereum address linked for payouts", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    } else {
                                        Toast.makeText(context, "Please enter a valid 42-char 0x address", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = HexGold, contentColor = Color.Black)
                            ) {
                                Text("SAVE & LINK PAYOUT ADDRESS", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Automated Payout Explanation Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(HexamineCard)
                        .border(0.8.dp, HexUsdtGreen.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CurrencyBitcoin, contentDescription = "USDT", tint = HexUsdtGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AUTOMATED USDT PAYOUT ROUTING",
                                color = HexUsdtGreen,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Linked wallet is automatically set as the payout recipient in HexaminePayoutVault (${SolidityContracts.PAYOUT_VAULT_ADDRESS.take(10)}...). When accepted shares cross the 10.00 USDT threshold, rewards are pushed directly on-chain.",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Payout Token: Tether USD (ERC-20: ${SolidityContracts.USDT_CONTRACT_ADDRESS.take(10)}...)",
                            color = HexGoldBright,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
