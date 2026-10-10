package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexQuantumPurple
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder

data class TabItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val badge: String? = null,
    val badgeColor: Color = HexGreen
)

@Composable
fun NavigationTabBar(
    activeTab: String,
    onTabSelected: (String) -> Unit,
    activeRigsCount: Int,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        TabItem("dashboard", "Dashboard", Icons.Default.Assessment),
        TabItem("breaker_core", "Breaker Core", Icons.Default.PrecisionManufacturing, badge = "GEOMETRY", badgeColor = HexGoldBright),
        TabItem("wallet", "In-App Wallet", Icons.Default.AccountBalanceWallet, badge = "E2E", badgeColor = HexGoldBright),
        TabItem("rigs", "Rigs Fleet", Icons.Default.Memory, badge = "$activeRigsCount", badgeColor = HexGreen),
        TabItem("stratum", "Stratum", Icons.Default.Sensors, badge = "V2", badgeColor = HexTeal),
        TabItem("web3", "Web3 Contracts", Icons.Default.Link, badge = "EVM", badgeColor = HexBlue),
        TabItem("payouts", "USDT Payouts", Icons.Default.CurrencyBitcoin, badge = "AUTO", badgeColor = HexUsdtGreen),
        TabItem("gated_tokens", "Gated Tokens", Icons.Default.Lock, badge = "UNRELEASED", badgeColor = HexGreen),
        TabItem("dex", "DEX Swap", Icons.Default.SwapHoriz, badge = "UNI-V3", badgeColor = HexGold),
        TabItem("blockchain_tx", "Blockchain TX", Icons.Default.ReceiptLong, badge = "LIVE", badgeColor = HexGoldBright),
        TabItem("ai_sentinel", "The Eye (AI)", Icons.Default.RemoveRedEye, badge = "LVL 4", badgeColor = HexTeal),
        TabItem("quantum", "Quantum Sec", Icons.Default.Hub, badge = "PQ", badgeColor = HexQuantumPurple),
        TabItem("logs", "Terminal Logs", Icons.Default.Terminal)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF080D1A))
            .border(width = 0.8.dp, color = HexamineBorder)
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = activeTab == tab.id
                val itemBg = if (isSelected) HexGold.copy(alpha = 0.15f) else Color.Transparent
                val itemBorder = if (isSelected) HexGold else Color.Transparent
                val textColor = if (isSelected) HexGoldBright else Color(0x99FFAA00)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(itemBg)
                        .border(1.dp, itemBorder, RoundedCornerShape(4.dp))
                        .clickable { onTabSelected(tab.id) }
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = textColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tab.title,
                            color = textColor,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontFamily = FontFamily.Monospace
                        )
                        if (tab.badge != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(tab.badgeColor.copy(alpha = 0.2f))
                                    .border(0.5.dp, tab.badgeColor, RoundedCornerShape(2.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = tab.badge,
                                    color = tab.badgeColor,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
