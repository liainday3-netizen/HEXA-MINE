package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Refresh
import com.example.data.LogEntity
import com.example.data.RigEntity
import com.example.market.MarketFeedState
import com.example.ui.OperationsKpis
import com.example.ui.components.AncientGeometryMechanicalCore
import com.example.ui.components.MechanicalGearRatio
import com.example.ui.components.SacredGeometryMode
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
fun DashboardScreen(
    kpis: OperationsKpis,
    rigs: List<RigEntity>,
    logs: List<LogEntity>,
    onOpenConnect: () -> Unit,
    onNavigateToRigs: () -> Unit,
    onNavigateToWeb3: () -> Unit,
    onNavigateToPayouts: () -> Unit,
    onTriggerPayout: () -> Unit,
    onTriggerAllocation: () -> Unit,
    onToggleCurtailment: () -> Unit,
    marketState: MarketFeedState = MarketFeedState(),
    onNavigateToBreakerCore: () -> Unit = {},
    onRefreshMarketFeed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
                    text = "OPERATIONS DASHBOARD",
                    color = HexGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "REAL-TIME MINING OVERVIEW · HUT 8 FRAMEWORK · EVM INTEGRATED",
                    color = Color(0x99FFAA00),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x22FFAA00))
                        .border(0.8.dp, HexGold, RoundedCornerShape(4.dp))
                        .clickable { onOpenConnect() }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text("+ ADD RIG", color = HexGold, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }

        // COINBASE OFFICIAL REAL-TIME MARKET ORACLE RIBBON
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF0052FF).copy(alpha = 0.12f))
                .border(1.dp, Color(0xFF0052FF).copy(alpha = 0.55f), RoundedCornerShape(6.dp))
                .padding(10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF0052FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "COINBASE",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "REAL-TIME SPOT ORACLE",
                            color = Color(0xFF0052FF),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${marketState.pingLatencyMs}ms",
                            color = HexGreen,
                            fontSize = 8.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF0052FF).copy(alpha = 0.2f))
                                .border(0.8.dp, Color(0xFF0052FF), RoundedCornerShape(3.dp))
                                .clickable { onRefreshMarketFeed() }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "REFRESH",
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("BTC/USD SPOT", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            text = "\$${"%,.2f".format(marketState.btc.priceUsd)}",
                            color = HexGoldBright,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Column {
                        Text("ETH SPOT", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            text = "\$${"%,.2f".format(marketState.eth.priceUsd)}",
                            color = HexBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Column {
                        Text("HEXA (BREAKER)", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            text = "\$${"%.2f".format(marketState.hexa.priceUsd)}",
                            color = HexGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Column {
                        Text("SPREAD", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            text = "\$${marketState.coinbase.spreadUsd}",
                            color = HexTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // SPECTACULAR BREAKER ANCIENT GEOMETRY MECHANICAL APPARATUS CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF080D1A))
                .border(1.dp, HexGold.copy(alpha = 0.65f), RoundedCornerShape(6.dp))
                .padding(10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PrecisionManufacturing,
                            contentDescription = "Breaker Core",
                            tint = HexGoldBright,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BREAKER CORE: SACRED GEOMETRY APPARATUS",
                            color = HexGoldBright,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexTeal.copy(alpha = 0.2f))
                            .border(0.8.dp, HexTeal, RoundedCornerShape(3.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "432 Hz Φ RESONANCE",
                            color = HexTeal,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Interactive Canvas Embedded View (Height 210dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clickable { onNavigateToBreakerCore() }
                ) {
                    AncientGeometryMechanicalCore(
                        modifier = Modifier.fillMaxSize(),
                        mode = SacredGeometryMode.MASTER_SYNTHESIS,
                        gearRatio = MechanicalGearRatio.GOLDEN_PHI,
                        speedMultiplier = (kpis.fleetHashrateTh / 800f).toFloat().coerceIn(0.8f, 2.8f),
                        isOverdrive = false,
                        fleetHashrateTh = kpis.fleetHashrateTh,
                        activeSharesAccepted = 18420L
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Antikythera Epicyclic Gears · Metatron's Cube · Breaker Camshaft Pistons",
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = onNavigateToBreakerCore,
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HexGold.copy(alpha = 0.25f),
                            contentColor = HexGoldBright
                        )
                    ) {
                        Text(
                            text = "EXPAND CORE APPARATUS ➔",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Live Process Pipeline Card
        val infiniteTransition = rememberInfiniteTransition(label = "pipelinePulse")
        val flowPulse by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200),
                repeatMode = RepeatMode.Reverse
            ),
            label = "flowPulse"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF080D1A))
                .border(0.8.dp, HexTeal.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                .padding(10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE OPERATIONS PIPELINE (AUTONOMOUS BREAKER)",
                        color = HexGoldBright,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "● FLOW ACTIVE",
                        color = HexGreen,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PipelineNode("1. ASICs", "${"%.0f".format(kpis.fleetHashrateTh)} TH/s", HexGold)
                    Text("➔", color = HexTeal.copy(alpha = flowPulse), fontSize = 12.sp)
                    PipelineNode("2. Stratum V2", "Noise P2P", HexTeal)
                    Text("➔", color = HexTeal.copy(alpha = flowPulse), fontSize = 12.sp)
                    PipelineNode("3. The Eye", "Auto-Tuned", HexQuantumPurple)
                    Text("➔", color = HexTeal.copy(alpha = flowPulse), fontSize = 12.sp)
                    PipelineNode("4. EVM Proof", "Dilithium", HexBlue)
                    Text("➔", color = HexTeal.copy(alpha = flowPulse), fontSize = 12.sp)
                    PipelineNode("5. Vault", "USDT Paid", HexUsdtGreen)
                }
            }
        }

        // Curtailment Alert Banner if active
        if (kpis.isCurtailmentActive) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(HexRed.copy(alpha = 0.15f))
                    .border(1.dp, HexRed, RoundedCornerShape(6.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Curtailment",
                        tint = HexRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "HUT 8 GRID CURTAILMENT PROTOCOL ACTIVE",
                            color = HexRed,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Mining rigs throttled to 20% to avoid spot price grid spikes. Click to restore dispatch.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // 4 Primary KPI Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KpiCard(
                title = "FLEET HASHRATE",
                value = "${"%.1f".format(kpis.fleetHashrateTh)} TH/s",
                subtext = if (kpis.isCurtailmentActive) "Throttled (Grid Response)" else "≈ ${(kpis.fleetHashrateTh / 1000).let { "%.2f".format(it) }} PH/s nominal",
                accentColor = HexGold,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "ACTIVE RIGS",
                value = "${kpis.activeRigsCount}",
                subtext = "${kpis.activeRigsCount} online · ${kpis.warningRigsCount} warn",
                accentColor = HexGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KpiCard(
                title = "24H REVENUE",
                value = "\$${"%.2f".format(kpis.earnedUsdt24h)} USDT",
                subtext = "≈ ${"%.5f".format(kpis.earnedBtc24h)} BTC",
                accentColor = HexUsdtGreen,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "POWER DRAW",
                value = "${"%.1f".format(kpis.totalPowerKw)} kW",
                subtext = "${"%.1f".format(kpis.efficiencyJPerTh)} J/TH efficiency",
                accentColor = HexTeal,
                modifier = Modifier.weight(1f)
            )
        }

        // Sparkline Hashrate History Card
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
                    Text(
                        text = "HASHRATE TELEMETRY (REAL-PATH 24H)",
                        color = Color(0x99FFAA00),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "STABLE · 99.8% SHARES ACCEPTED",
                        color = HexGreen,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Canvas Sparkline Chart
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                ) {
                    val points = listOf(
                        0.7f, 0.72f, 0.71f, 0.74f, 0.73f, 0.75f, 0.74f,
                        0.76f, 0.75f, 0.77f, 0.76f, 0.78f, 0.77f, 0.79f, 0.81f
                    )
                    val width = size.width
                    val height = size.height
                    val step = width / (points.size - 1)

                    val path = Path()
                    points.forEachIndexed { index, fraction ->
                        val x = index * step
                        val y = height - (fraction * height)
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }

                    drawPath(
                        path = path,
                        color = HexGold,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Draw baseline grid lines
                    drawLine(
                        color = HexamineBorder,
                        start = Offset(0f, height * 0.25f),
                        end = Offset(width, height * 0.25f),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = HexamineBorder,
                        start = Offset(0f, height * 0.75f),
                        end = Offset(width, height * 0.75f),
                        strokeWidth = 1f
                    )
                }
            }
        }

        // Web3 Smart Contract Quick Actions
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
                    text = "WEB3 SMART CONTRACT OPERATIONS",
                    color = HexGold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Execute Solidity Hash Power Allocation & Automated USDT Payouts on-chain",
                    color = Color(0x99FFAA00),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onTriggerAllocation,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HexBlue.copy(alpha = 0.2f),
                            contentColor = HexBlue
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bolt, contentDescription = "Allocate", modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ALLOCATE TH/S", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onTriggerPayout,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HexUsdtGreen.copy(alpha = 0.25f),
                            contentColor = HexGreen
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CurrencyBitcoin, contentDescription = "Payout", modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("TRIGGER USDT", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Live Mini Terminal Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF020408))
                .border(0.8.dp, HexBlue.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                .padding(10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE STRATUM & WEB3 EVENTS",
                        color = HexBlue,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "REAL-PATH STREAM",
                        color = HexTeal,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                logs.take(4).forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "[${log.category}]",
                            color = when (log.category) {
                                "STRATUM" -> HexTeal
                                "WEB3" -> HexBlue
                                "QUANTUM" -> HexQuantumPurple
                                "PAYOUT" -> HexUsdtGreen
                                else -> HexGold
                            },
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = log.message,
                            color = Color(0xFFF5E6B3).copy(alpha = 0.85f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtext: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(HexamineCard)
            .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = title,
                color = Color(0x66FFAA00),
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = accentColor,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun PipelineNode(stage: String, metric: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.12f))
            .border(0.8.dp, color.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stage, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Text(metric, color = Color.White, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
        }
    }
}
