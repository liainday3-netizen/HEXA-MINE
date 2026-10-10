package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.web3.Web3Manager
import com.example.web3.Web3Network
import com.example.web3.Web3Wallet

@Composable
fun Web3Screen(
    wallet: Web3Wallet,
    activeNetwork: Web3Network,
    onSelectNetwork: (Web3Network) -> Unit,
    onRequestAuth: (actionName: String, contract: String, funcSig: String, params: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSourceCode by remember { mutableStateOf(false) }
    var selectedContractSource by remember { mutableStateOf("PROTOCOL") }
    var isEditingContracts by remember { mutableStateOf(false) }
    var customProtocolAddr by remember { mutableStateOf(SolidityContracts.MINING_PROTOCOL_ADDRESS) }
    var customVaultAddr by remember { mutableStateOf(SolidityContracts.PAYOUT_VAULT_ADDRESS) }
    var customUsdtAddr by remember { mutableStateOf(SolidityContracts.USDT_CONTRACT_ADDRESS) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Header
        Column {
            Text(
                text = "WEB3 MINING INFRASTRUCTURE",
                color = HexGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(
                text = "SOLIDITY SMART CONTRACTS · HASH POWER ALLOCATION · AUTOMATED EVM VAULTS",
                color = Color(0x99FFAA00),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // EVM Network Selector Bar
        Text(
            text = "EVM RPC NETWORK:",
            color = Color(0x66FFAA00),
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Web3Manager.availableNetworks(com.example.config.AppMode.isSimulated.value).forEach { net ->
                val isCur = net.id == activeNetwork.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isCur) HexBlue.copy(alpha = 0.25f) else Color(0xFF080D1A))
                        .border(1.dp, if (isCur) HexBlue else HexamineBorder, RoundedCornerShape(4.dp))
                        .clickable { onSelectNetwork(net) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = net.name,
                        color = if (isCur) Color.White else Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = if (isCur) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Miner Web3 Wallet Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(HexamineCard)
                .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Link, contentDescription = "Wallet", tint = HexGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("MINER WALLET", color = HexGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("CONNECTED", color = HexGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(wallet.address, color = HexGoldBright, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = HexTeal,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString(wallet.address))
                                Toast.makeText(context, "Wallet address copied", Toast.LENGTH_SHORT).show()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Balances Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("USDT (TETHER)", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${"%.2f".format(wallet.usdtBalance)} USDT", color = HexUsdtGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text("ETH BALANCE", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${wallet.ethBalance} ETH", color = HexBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text("wBTC BALANCE", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${wallet.wbtcBalance} BTC", color = HexGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // Smart Contracts & Functions
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF080D1A))
                .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = "DEPLOYED SOLIDITY SMART CONTRACTS",
                    color = HexGold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("Mining Protocol: ${SolidityContracts.MINING_PROTOCOL_ADDRESS}", color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                Text("Payout Vault: ${SolidityContracts.PAYOUT_VAULT_ADDRESS}", color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                Text("USDT Token: ${SolidityContracts.USDT_CONTRACT_ADDRESS}", color = HexUsdtGreen, fontSize = 9.sp, fontFamily = FontFamily.Monospace)

                Spacer(modifier = Modifier.height(12.dp))

                // Custom Contract Addresses Fill/Bind Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF050810))
                        .border(0.8.dp, HexGold.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CONTRACT BINDINGS (FILL AT LATER DATE)",
                                color = HexGoldBright,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isEditingContracts) "DONE" else "EDIT ADDRESSES",
                                color = HexTeal,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    isEditingContracts = !isEditingContracts
                                    if (!isEditingContracts) {
                                        Toast.makeText(context, "Custom contract bindings saved", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }

                        if (isEditingContracts) {
                            OutlinedTextField(
                                value = customProtocolAddr,
                                onValueChange = { customProtocolAddr = it },
                                label = { Text("MINING PROTOCOL CONTRACT", fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HexGold,
                                    unfocusedBorderColor = HexamineBorder,
                                    focusedTextColor = HexGoldBright,
                                    unfocusedTextColor = HexGoldBright
                                )
                            )
                            OutlinedTextField(
                                value = customVaultAddr,
                                onValueChange = { customVaultAddr = it },
                                label = { Text("PAYOUT VAULT CONTRACT", fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HexGold,
                                    unfocusedBorderColor = HexamineBorder,
                                    focusedTextColor = HexGoldBright,
                                    unfocusedTextColor = HexGoldBright
                                )
                            )
                            OutlinedTextField(
                                value = customUsdtAddr,
                                onValueChange = { customUsdtAddr = it },
                                label = { Text("USDT TOKEN CONTRACT", fontSize = 8.sp, fontFamily = FontFamily.Monospace) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HexGold,
                                    unfocusedBorderColor = HexamineBorder,
                                    focusedTextColor = HexGoldBright,
                                    unfocusedTextColor = HexGoldBright
                                )
                            )
                        } else {
                            Text(
                                text = "Protocol: $customProtocolAddr\nVault: $customVaultAddr\nUSDT: $customUsdtAddr",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive On-Chain Contract Actions
                Text("ON-CHAIN OPERATIONS:", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = {
                            onRequestAuth(
                                "ALLOCATE_HASH_POWER",
                                SolidityContracts.MINING_PROTOCOL_ADDRESS,
                                "allocateHashPower(bytes32 poolId, uint256 targetHashrateTh)",
                                "Target: HEXA Pool, 1007.5 TH/s"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HexBlue.copy(alpha = 0.2f), contentColor = HexBlue)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = "Allocate", modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("1. ALLOCATE HASH POWER ON-CHAIN (SOLIDITY)", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            onRequestAuth(
                                "SUBMIT_SHARE_BATCH",
                                SolidityContracts.MINING_PROTOCOL_ADDRESS,
                                "submitStratumShareBatch(bytes32,uint256,uint256,bytes32,bytes,bytes)",
                                "Batch: #48, 18,420 Shares, Difficulty: 16384"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HexTeal.copy(alpha = 0.2f), contentColor = HexTeal)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = "Submit", modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("2. SUBMIT STRATUM SHARE BATCH PROOF", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            onRequestAuth(
                                "AUTOMATED_USDT_PAYOUT",
                                SolidityContracts.PAYOUT_VAULT_ADDRESS,
                                "executeAutomatedPayoutBatch(address[],uint256[],uint256)",
                                "Vault Release: +50.00 USDT to ${wallet.address.take(10)}..."
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HexUsdtGreen.copy(alpha = 0.25f), contentColor = HexGreen)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Hub, contentDescription = "Vault", modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("3. EXECUTE AUTOMATED USDT PAYOUT VIA VAULT", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Toggle Solidity Source Code Viewer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(HexamineCard)
                .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                .clickable { showSourceCode = !showSourceCode }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Code, contentDescription = "Code", tint = HexGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("VIEW SOLIDITY SMART CONTRACT CODE", color = HexGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                Text(if (showSourceCode) "▲ HIDE" else "▼ EXPAND", color = HexGoldBright, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
        }

        if (showSourceCode) {
            // Source Code Container
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (selectedContractSource == "PROTOCOL") HexGold.copy(alpha = 0.2f) else Color(0xFF080D1A))
                        .clickable { selectedContractSource = "PROTOCOL" }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("HexamineMiningProtocol.sol", color = HexGoldBright, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (selectedContractSource == "VAULT") HexGold.copy(alpha = 0.2f) else Color(0xFF080D1A))
                        .clickable { selectedContractSource = "VAULT" }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("HexaminePayoutVault.sol", color = HexGoldBright, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }

            val source = if (selectedContractSource == "PROTOCOL") SolidityContracts.MINING_PROTOCOL_SOURCE else SolidityContracts.PAYOUT_VAULT_SOURCE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF03050A))
                    .border(0.8.dp, HexamineBorder, RoundedCornerShape(4.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "COPY CODE",
                            color = HexTeal,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.clickable {
                                clipboardManager.setText(AnnotatedString(source))
                                Toast.makeText(context, "Solidity code copied", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    Text(
                        text = source,
                        color = Color(0xFFDDDCB4),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
