package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.SwapVert
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard
import com.example.web3.Web3Wallet

@Composable
fun DexScreen(
    wallet: Web3Wallet,
    onExecuteSwap: (fromAsset: String, toAsset: String, fromAmount: Double, toAmount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var fromAsset by remember { mutableStateOf("wBTC") }
    var toAsset by remember { mutableStateOf("USDT") }
    var inputAmountStr by remember { mutableStateOf("0.05") }
    var slippagePercent by remember { mutableStateOf(0.5) }

    val btcPriceUsdt = 94820.0
    val ethPriceUsdt = 2640.0
    val hexaPriceUsdt = 0.45

    val inputAmount = inputAmountStr.toDoubleOrNull() ?: 0.0
    val outputAmount = when {
        fromAsset == "wBTC" && toAsset == "USDT" -> inputAmount * btcPriceUsdt
        fromAsset == "ETH" && toAsset == "USDT" -> inputAmount * ethPriceUsdt
        fromAsset == "HEXA" && toAsset == "USDT" -> inputAmount * hexaPriceUsdt
        fromAsset == "USDT" && toAsset == "wBTC" -> if (inputAmount > 0) inputAmount / btcPriceUsdt else 0.0
        fromAsset == "USDT" && toAsset == "ETH" -> if (inputAmount > 0) inputAmount / ethPriceUsdt else 0.0
        fromAsset == "USDT" && toAsset == "HEXA" -> if (inputAmount > 0) inputAmount / hexaPriceUsdt else 0.0
        else -> inputAmount
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Column {
            Text(
                text = "MINING REWARDS DEX",
                color = HexGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(
                text = "SWAP MINED REWARDS DIRECTLY INTO USDT · UNISWAP V3 ROUTER",
                color = Color(0x99FFAA00),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Swap Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(HexamineCard)
                .border(1.dp, HexamineBorder, RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // From Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF080D1A))
                        .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("YOU PAY", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            Text(
                                "BALANCE: ${
                                    when (fromAsset) {
                                        "wBTC" -> "${wallet.wbtcBalance} wBTC"
                                        "ETH" -> "${wallet.ethBalance} ETH"
                                        "HEXA" -> "${wallet.hexaBalance} HEXA"
                                        else -> "${wallet.usdtBalance} USDT"
                                    }
                                }",
                                color = HexGoldBright,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputAmountStr,
                                onValueChange = { inputAmountStr = it },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HexGold,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = HexGoldBright,
                                    unfocusedTextColor = HexGoldBright
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            // Asset Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(HexGold.copy(alpha = 0.2f))
                                    .border(0.8.dp, HexGold, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(fromAsset, color = HexGoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }

                // Invert Button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0D1526))
                            .border(1.dp, HexGold, RoundedCornerShape(16.dp))
                            .clickable {
                                val temp = fromAsset
                                fromAsset = toAsset
                                toAsset = temp
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = HexGold, modifier = Modifier.size(18.dp))
                    }
                }

                // To Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF080D1A))
                        .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("YOU RECEIVE (ESTIMATED)", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            Text("LIQUIDITY: \$24.8M POOL", color = HexGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "%.4f".format(outputAmount),
                                color = HexUsdtGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(HexUsdtGreen.copy(alpha = 0.2f))
                                    .border(0.8.dp, HexUsdtGreen, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(toAsset, color = HexGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }

                // Slippage Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("SLIPPAGE TOLERANCE:", color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(0.1, 0.5, 1.0).forEach { s ->
                            val isCur = slippagePercent == s
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (isCur) HexGold.copy(alpha = 0.2f) else Color(0xFF080D1A))
                                    .border(0.5.dp, if (isCur) HexGold else HexamineBorder, RoundedCornerShape(3.dp))
                                    .clickable { slippagePercent = s }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("$s%", color = if (isCur) HexGoldBright else Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }

                // Swap Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Rate", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("1 $fromAsset ≈ ${"%.2f".format(if (fromAsset == "wBTC") btcPriceUsdt else if (fromAsset == "ETH") ethPriceUsdt else hexaPriceUsdt)} $toAsset", color = HexGoldBright, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Price Impact", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("< 0.01%", color = HexGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Router Contract", color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("Uniswap V3 SwapRouter02 (0x68b3...c842)", color = HexBlue, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                // Swap Button
                Button(
                    onClick = {
                        if (inputAmount > 0) {
                            onExecuteSwap(fromAsset, toAsset, inputAmount, outputAmount)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HexGold,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        "EXECUTE ON-CHAIN DEX SWAP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
