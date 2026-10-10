package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun AiSentinelScreen(
    onTriggerAiCalibration: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isAutoAdjustEnabled by remember { mutableStateOf(true) }
    var isPredictiveMaintenanceEnabled by remember { mutableStateOf(true) }
    var isGasTimingEnabled by remember { mutableStateOf(true) }
    var isThermalCurtailmentEnabled by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "eyePulse")
    val eyePulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eyePulseScale"
    )
    val eyeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000),
            repeatMode = RepeatMode.Restart
        ),
        label = "eyeRotation"
    )

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
                text = "THE EVOLVING EYE — AI AUTOPILOT",
                color = HexGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(
                text = "NEURAL AUTO-ADJUST & AUTONOMOUS INFRASTRUCTURE MAINTENANCE",
                color = Color(0x99FFAA00),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // The Evolving Eye Visual Sentinel Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF131D33),
                            Color(0xFF080D1A),
                            Color(0xFF03050A)
                        )
                    )
                )
                .border(1.2.dp, HexTeal.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Evolution Stage Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexTeal.copy(alpha = 0.2f))
                            .border(0.8.dp, HexTeal, RoundedCornerShape(3.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("EVOLUTION: LEVEL 4 (SENTIENT SIGHT)", color = HexTeal, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    Text("FITNESS: 99.82%", color = HexGreen, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // The Cybernetic Eye Graphic Canvas
                Box(
                    modifier = Modifier
                        .size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(130.dp)) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = size.minDimension / 2f

                        // Outer orbital neural ring
                        drawCircle(
                            color = HexTeal.copy(alpha = 0.25f),
                            radius = radius * 0.95f,
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Middle pulsing ring
                        drawCircle(
                            color = HexGold.copy(alpha = 0.35f),
                            radius = radius * 0.75f * eyePulseScale,
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Inner Iris Gradient
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(HexGoldBright, HexTeal, Color(0xFF050810)),
                                center = center,
                                radius = radius * 0.55f
                            ),
                            radius = radius * 0.55f,
                            center = center
                        )

                        // Center Pupil aperture
                        drawCircle(
                            color = Color(0xFF03050A),
                            radius = radius * 0.25f / eyePulseScale,
                            center = center
                        )

                        // Center glowing spark
                        drawCircle(
                            color = HexGoldBright,
                            radius = radius * 0.08f,
                            center = center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "AUTONOMOUS EYE ACTIVELY PRESERVING FLEET HEALTH",
                    color = HexGoldBright,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Analyzing 48,000 telemetry points/sec · Predicting ASIC thermal throttling · Tuning clock frequencies",
                    color = Color(0x99FFAA00),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Auto-Adjust & Maintenance Switches
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(HexamineCard)
                .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "AUTONOMOUS MAINTENANCE DIRECTIVES",
                    color = HexGold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                MaintenanceToggleRow(
                    title = "AI Neural Auto-Adjust",
                    desc = "Dynamically adjusts voltage & clock frequencies to maximize J/TH efficiency",
                    checked = isAutoAdjustEnabled,
                    onCheckedChange = { isAutoAdjustEnabled = it }
                )

                MaintenanceToggleRow(
                    title = "Predictive Silicon Maintenance",
                    desc = "Detects micro-thermal variance before chip burnout, shifts hash workload",
                    checked = isPredictiveMaintenanceEnabled,
                    onCheckedChange = { isPredictiveMaintenanceEnabled = it }
                )

                MaintenanceToggleRow(
                    title = "Web3 Gas-Optimized Maintenance",
                    desc = "Times automated USDT vault payouts during off-peak EVM gas (<15 Gwei)",
                    checked = isGasTimingEnabled,
                    onCheckedChange = { isGasTimingEnabled = it }
                )

                MaintenanceToggleRow(
                    title = "Grid Tariff Curtailment Reflex",
                    desc = "Instantaneous demand-response curtailment during regional electricity price spikes",
                    checked = isThermalCurtailmentEnabled,
                    onCheckedChange = { isThermalCurtailmentEnabled = it }
                )
            }
        }

        // Recent Maintenance Operations Log executed by the Eye
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF080D1A))
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
                        text = "MAINTENANCE ACTIONS EXECUTED BY THE EYE",
                        color = HexTeal,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "LIVE ACTIONS",
                        color = HexGreen,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                val actions = listOf(
                    "Rig-05 Avalon A1466: Detected thermal elevation (71°C) -> Increased Fan Fan-2 +400 RPM, downclocked hashboard-3 -25MHz. Temp normalized to 65°C.",
                    "Stratum Protocol: Detected 2 stale shares on backup pool -> Re-routed hash connection to primary HEXA Stratum V2 endpoint.",
                    "Web3 Payout Vault: EVM gas dipped to 14.2 Gwei -> Triggered automated batch release of 142.50 USDT to miners, saving \$18.40 in gas fees.",
                    "Quantum Entropy Engine: Seeded new QRNG entropy digest 0x9f18ac... into Dilithium-5 verification contract.",
                    "Fleet Rebalance: Overclocked Rig-01 (+15%) during off-peak midnight electricity tariff (\$0.028/kWh)."
                )

                actions.forEach { act ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Done",
                            tint = HexGreen,
                            modifier = Modifier
                                .size(13.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = act,
                            color = Color(0xFFF5E6B3).copy(alpha = 0.9f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Calibrate Button
        Button(
            onClick = onTriggerAiCalibration,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HexTeal,
                contentColor = Color.Black
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Calibrate", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "RE-CALIBRATE AI NEURAL EYE NOW",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MaintenanceToggleRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = HexGoldBright,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = desc,
                color = Color.Gray,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = HexTeal,
                checkedTrackColor = HexTeal.copy(alpha = 0.3f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color(0xFF050810)
            )
        )
    }
}
