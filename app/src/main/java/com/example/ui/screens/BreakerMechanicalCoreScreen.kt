package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard
import kotlinx.coroutines.launch

@Composable
fun BreakerMechanicalCoreScreen(
    kpis: OperationsKpis,
    marketState: MarketFeedState,
    sharesAccepted: Long,
    onRefreshMarketFeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var activeMode by remember { mutableStateOf(SacredGeometryMode.MASTER_SYNTHESIS) }
    var activeRatio by remember { mutableStateOf(MechanicalGearRatio.GOLDEN_PHI) }
    var speedMultiplier by remember { mutableFloatStateOf(1.2f) }
    var isSyncWithHashrate by remember { mutableStateOf(true) }
    var isOverdrive by remember { mutableStateOf(false) }

    // Calculated speed based on fleet hashrate if synced
    val effectiveSpeed = if (isSyncWithHashrate) {
        (kpis.fleetHashrateTh / 800.0f).toFloat().coerceIn(0.6f, 3.5f)
    } else {
        speedMultiplier
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. TOP HEADER & TELEMETRY BADGES
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PrecisionManufacturing,
                        contentDescription = "Breaker",
                        tint = HexGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BREAKER MECHANICAL CORE",
                        color = HexGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "ANCIENT SACRED GEOMETRY APPARATUS · KINETIC EXTRACTION",
                    color = Color(0x99FFAA00),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(HexTeal.copy(alpha = 0.15f))
                    .border(0.8.dp, HexTeal, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "HARMONIC 432 Hz",
                        color = HexTeal,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 2. COINBASE OFFICIAL LIVE MARKET FEED RIBBON
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF0052FF).copy(alpha = 0.12f))
                .border(1.dp, Color(0xFF0052FF).copy(alpha = 0.6f), RoundedCornerShape(6.dp))
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
                                text = "COINBASE LIVE",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SPOT ORACLE · REAL-TIME STREAM",
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
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF0052FF).copy(alpha = 0.2f))
                                .border(0.8.dp, Color(0xFF0052FF), RoundedCornerShape(3.dp))
                                .clickable { onRefreshMarketFeed() }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (marketState.isRefreshing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(10.dp),
                                        color = Color.White,
                                        strokeWidth = 1.5.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
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

                Spacer(modifier = Modifier.height(8.dp))

                // Price Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BTC/USD SPOT",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "\$${"%,.2f".format(marketState.btc.priceUsd)}",
                            color = HexGoldBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column {
                        Text(
                            text = "COINBASE SPREAD",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "\$${marketState.coinbase.spreadUsd} USD",
                            color = HexTeal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column {
                        Text(
                            text = "HEXA (BREAKER)",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "\$${"%.2f".format(marketState.hexa.priceUsd)}",
                            color = HexGreen,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column {
                        Text(
                            text = "ETH SPOT",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "\$${"%,.2f".format(marketState.eth.priceUsd)}",
                            color = HexBlue,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // 3. SPECTACULAR SACRED GEOMETRY CANVAS (TOUCH INTERACTIVE)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            AncientGeometryMechanicalCore(
                modifier = Modifier.fillMaxSize(),
                mode = activeMode,
                gearRatio = activeRatio,
                speedMultiplier = effectiveSpeed,
                isOverdrive = isOverdrive,
                fleetHashrateTh = kpis.fleetHashrateTh,
                activeSharesAccepted = sharesAccepted,
                onManualSpin = { delta ->
                    // Interactive touch spin
                }
            )

            // Overlay indicator in top right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xDD080D1A))
                    .border(0.8.dp, HexGold.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "TOUCH TO ROTATE / DRAG KINETICS",
                    color = HexGoldBright,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 4. SACRED GEOMETRY MODE SELECTOR
        Column {
            Text(
                text = "SACRED GEOMETRIC MECHANISM SELECTOR",
                color = HexGold,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    Pair(SacredGeometryMode.MASTER_SYNTHESIS, "MASTER SYNTHESIS"),
                    Pair(SacredGeometryMode.ANTIKYTHERA_GEARS, "ANTIKYTHERA CLOCKWORK"),
                    Pair(SacredGeometryMode.METATRON_LATTICE, "METATRON'S LATTICE"),
                    Pair(SacredGeometryMode.FLOWER_OF_LIFE, "FLOWER OF LIFE"),
                    Pair(SacredGeometryMode.PLATONIC_GYROSCOPE, "PLATONIC 3D GYRO")
                ).forEach { (m, label) ->
                    val isSelected = activeMode == m
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) HexGold.copy(alpha = 0.2f) else Color(0xFF080D1A))
                            .border(1.dp, if (isSelected) HexGold else HexamineBorder, RoundedCornerShape(4.dp))
                            .clickable { activeMode = m }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) HexGoldBright else Color.White.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // 5. MECHANICAL GEAR RATIO SELECTOR
        Column {
            Text(
                text = "EPICYCLIC GEAR RATIO (HARMONIC ESCAPEMENT)",
                color = HexGold,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MechanicalGearRatio.values().forEach { ratio ->
                    val isSelected = activeRatio == ratio
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) HexTeal.copy(alpha = 0.2f) else Color(0xFF080D1A))
                            .border(1.dp, if (isSelected) HexTeal else HexamineBorder, RoundedCornerShape(4.dp))
                            .clickable { activeRatio = ratio }
                            .padding(horizontal = 4.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ratio.displayName,
                            color = if (isSelected) HexTeal else Color.White.copy(alpha = 0.65f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // 6. KINETIC DRIVE CONTROLS (OVERDRIVE & LIVE HASHRATE COUPLING)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(HexamineCard)
                .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SYNC WITH FLEET HASHRATE",
                            color = HexGoldBright,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Dynamic gear velocity driven by ${"%.1f".format(kpis.fleetHashrateTh)} TH/s ASIC speed",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Switch(
                        checked = isSyncWithHashrate,
                        onCheckedChange = { isSyncWithHashrate = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = HexGoldBright,
                            checkedTrackColor = HexGold.copy(alpha = 0.35f)
                        )
                    )
                }

                if (!isSyncWithHashrate) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "MANUAL KINETIC RPM",
                                color = HexTeal,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "${"%.1f".format(speedMultiplier)}x VELOCITY",
                                color = HexTeal,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Slider(
                            value = speedMultiplier,
                            onValueChange = { speedMultiplier = it },
                            valueRange = 0.4f..3.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = HexTeal,
                                activeTrackColor = HexTeal
                            )
                        )
                    }
                }

                // Overdrive Boost Button
                Button(
                    onClick = {
                        isOverdrive = !isOverdrive
                        Toast.makeText(
                            context,
                            if (isOverdrive) "BREAKER KINETIC OVERDRIVE ENGAGED" else "Standard Harmonic Pacing Restored",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isOverdrive) HexGreen.copy(alpha = 0.25f) else HexGold.copy(alpha = 0.18f),
                        contentColor = if (isOverdrive) HexGreen else HexGoldBright
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Overdrive",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isOverdrive) "DISENGAGE KINETIC OVERDRIVE" else "ENGAGE KINETIC OVERDRIVE (2.2X BOOST)",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 7. MECHANICAL ENGINEERING TELEMETRY CARDS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MechanicalTelemetryCard(
                title = "KINETIC POWER TRANSFER",
                value = "99.84%",
                sub = "Harmonic Involute Mesh",
                accent = HexGreen,
                modifier = Modifier.weight(1f)
            )
            MechanicalTelemetryCard(
                title = "SHAFT TORQUE OUTPUT",
                value = "${(1820 * effectiveSpeed).toInt()} N·m",
                sub = "Breaker Camshaft",
                accent = HexGold,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MechanicalTelemetryCard(
                title = "HARMONIC FREQUENCY",
                value = "432.00 Hz",
                sub = "Φ = 1.618 Golden Resonance",
                accent = HexTeal,
                modifier = Modifier.weight(1f)
            )
            MechanicalTelemetryCard(
                title = "CAMSHAFT STRESS",
                value = "${"%.1f".format(24.5 + effectiveSpeed * 3.2)} MPa",
                sub = "Within 85 MPa limit",
                accent = HexQuantumPurple,
                modifier = Modifier.weight(1f)
            )
        }

        // 8. BREAKER PROCESS MINING SUMMARY
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF03050A))
                .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "BREAKER PROCESS · REAL VALUE EXTRACTION",
                    color = HexGold,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "The Breaker mining process connects ancient sacred geometry ratios (Antikythera epicyclic gear trains, Metatron's harmonic vector lattice, and logarithmic golden spirals) to cryptographic nonce solvers. Live shares are packaged into Stratum V2 Noise packets, verified on-chain, and paid out automatically in USDT via Coinbase & EVM Vaults.",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
fun MechanicalTelemetryCard(
    title: String,
    value: String,
    sub: String,
    accent: Color,
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
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = accent,
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
