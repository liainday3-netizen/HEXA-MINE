package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.quantum.QuantumTelemetry
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexQuantumPurple
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard

@Composable
fun QuantumScreen(
    telemetry: QuantumTelemetry,
    onRegenerateEntropy: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

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
                text = "QUANTUM TECHNOLOGY & SECURITY",
                color = HexGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(
                text = "POST-QUANTUM DILITHIUM-5 CRYPTOGRAPHY · QUANTUM ANNEALING CLOCK TUNER",
                color = Color(0x99FFAA00),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Post-Quantum Status Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(HexamineCard)
                .border(1.dp, HexQuantumPurple.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = "Shield", tint = HexQuantumPurple, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("POST-QUANTUM CRYPTOGRAPHY", color = HexQuantumPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("SHOR-RESISTANT", color = HexGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("SIGNATURE SCHEME", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(telemetry.pqSignatureType, color = HexGoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text("GROVER DEFENSE", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${telemetry.groverDefenseBits}-BIT SECURE", color = HexGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text("QUBIT FIDELITY", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${telemetry.qubitFidelityPercent}%", color = HexTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // Quantum Entropy QRNG Card
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
                    Text("QUANTUM ENTROPY SEED (QRNG)", color = HexGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Text(
                        "REFRESH SEED",
                        color = HexTeal,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.clickable { onRegenerateEntropy() }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "0x${telemetry.entropySeedHex}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Used for on-chain Dilithium-5 batch proof generation & nonces",
                    color = Color.Gray,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Post-Quantum Address
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF080D1A))
                .border(0.8.dp, HexamineBorder, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column {
                Text("POST-QUANTUM EVM MINING ADDRESS", color = HexQuantumPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(telemetry.postQuantumAddress, color = HexGoldBright, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = HexTeal,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString(telemetry.postQuantumAddress))
                                Toast.makeText(context, "PQ Address copied", Toast.LENGTH_SHORT).show()
                            }
                    )
                }
            }
        }

        // Quantum Annealing ASIC Optimization
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(HexamineCard)
                .border(0.8.dp, HexTeal.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Quantum Tune", tint = HexTeal, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("QUANTUM ANNEALING CLOCK TUNING", color = HexTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Adiabatic heuristic optimizes clock frequency per ASIC board against thermal gradients.",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("EFFICIENCY GAIN", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Text("+${telemetry.quantumAnnealingGainPercent}% J/TH Gain", color = HexGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
