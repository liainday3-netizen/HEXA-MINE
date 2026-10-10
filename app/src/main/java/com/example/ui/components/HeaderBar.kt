package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.ui.theme.HexQuantumPurple
import com.example.ui.theme.HexRed
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard

@Composable
fun HeaderBar(
    isHut8Mode: Boolean,
    isCurtailmentActive: Boolean,
    walletAddress: String,
    onToggleHut8Curtailment: () -> Unit,
    onOpenConnectInfra: () -> Unit,
    onOpenAdvancedAuth: () -> Unit,
    onOpenWalletConnect: () -> Unit,
    onOpenIntegrations: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToBreakerCore: () -> Unit = {},
    marketState: com.example.market.MarketFeedState? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val blinkAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blinkAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF050810).copy(alpha = 0.95f))
            .border(width = 1.dp, color = HexamineBorder)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(HexamineCard)
                        .border(1.dp, HexGold, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Hub,
                        contentDescription = "Logo",
                        tint = HexGoldBright,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "HEXAMINE",
                    color = HexGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = " // BREAKER",
                    color = HexTeal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }

            // Right header buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Connect Infra Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x22FFAA00))
                        .border(1.dp, HexGold, RoundedCornerShape(4.dp))
                        .clickable { onOpenConnectInfra() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Connect",
                            tint = HexGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "CONNECT",
                            color = HexGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Hut 8 Mode / Curtailment Toggle Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isCurtailmentActive) HexRed.copy(alpha = 0.2f) else HexTeal.copy(alpha = 0.15f))
                        .border(1.dp, if (isCurtailmentActive) HexRed else HexTeal, RoundedCornerShape(4.dp))
                        .clickable { onToggleHut8Curtailment() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isCurtailmentActive) "CURTAILED -80%" else "⬡ HUT 8 DISPATCH",
                        color = if (isCurtailmentActive) HexRed else HexTeal,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal scrolling status telemetry pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stratum Ready Pill
            StatusPill(
                color = HexGreen,
                text = "● STRATUM V2 READY",
                alpha = blinkAlpha
            )

            // Breaker Status Pill
            StatusPill(
                color = HexGold,
                text = "⚡ BREAKER: ARMED",
                alpha = 1f
            )

            // Pool Pill
            StatusPill(
                color = HexTeal,
                text = "POOL: HUT 8 SECURE",
                alpha = 1f
            )

            // USDT Price Pill
            StatusPill(
                color = HexUsdtGreen,
                text = "USDT: \$1.000",
                alpha = 1f
            )

            // BTC Price Pill
            StatusPill(
                color = HexGoldBright,
                text = "BTC: \$94,820",
                alpha = 1f
            )

            // Quantum Pill
            StatusPill(
                color = HexQuantumPurple,
                text = "⚛ DILITHIUM-5 SAFE",
                alpha = 1f
            )

            // Advanced Auth Pill button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(HexBlue.copy(alpha = 0.15f))
                    .border(0.8.dp, HexBlue, RoundedCornerShape(3.dp))
                    .clickable { onOpenAdvancedAuth() }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Auth",
                        tint = HexBlue,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "ADVANCED AUTH",
                        color = HexBlue,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Breaker Core Ancient Geometry Mechanical Engineering Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(HexGold.copy(alpha = 0.25f))
                    .border(1.dp, HexGoldBright, RoundedCornerShape(3.dp))
                    .clickable { onNavigateToBreakerCore() }
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(RoundedCornerShape(2.5.dp))
                            .background(HexGoldBright)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "BREAKER CORE (GEOMETRY)",
                        color = HexGoldBright,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Coinbase Live Market Price Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF0052FF).copy(alpha = 0.2f))
                    .border(0.8.dp, Color(0xFF0052FF), RoundedCornerShape(3.dp))
                    .clickable { onNavigateToBreakerCore() }
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                val btcPriceStr = marketState?.let { "\$${"%,.0f".format(it.btc.priceUsd)}" } ?: "\$82,614"
                Text(
                    text = "COINBASE: $btcPriceStr",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            // In-App Wallet Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(HexGold.copy(alpha = 0.2f))
                    .border(0.8.dp, HexGold, RoundedCornerShape(3.dp))
                    .clickable { onNavigateToWallet() }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "WALLET: ${walletAddress.take(6)}...${walletAddress.takeLast(4)}",
                    color = HexGoldBright,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            // WalletConnect Modal opener
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(HexBlue.copy(alpha = 0.15f))
                    .border(0.8.dp, HexBlue, RoundedCornerShape(3.dp))
                    .clickable { onOpenWalletConnect() }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "WALLETCONNECT",
                    color = HexBlue,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            // Slack Connect Quick Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(HexTeal.copy(alpha = 0.15f))
                    .border(0.8.dp, HexTeal, RoundedCornerShape(3.dp))
                    .clickable { onOpenIntegrations() }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "SLACK: CONNECTED",
                    color = HexTeal,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            // Integrations / API / WS Pill button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF0D1526))
                    .border(0.8.dp, HexamineBorder, RoundedCornerShape(3.dp))
                    .clickable { onOpenIntegrations() }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "INFRA APIS / WS",
                    color = Color.LightGray,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StatusPill(color: Color, text: String, alpha: Float = 1f) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .background(color.copy(alpha = 0.12f))
            .border(0.8.dp, color.copy(alpha = 0.5f), RoundedCornerShape(3.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
            .alpha(alpha)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium
        )
    }
}
