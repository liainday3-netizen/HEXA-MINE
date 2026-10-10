package com.example.ui.dialogs

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Router
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexamineBorder

@Composable
fun ConnectInfraDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onAddRig: (name: String, model: String, ip: String, hashrate: Double, power: Int) -> Unit
) {
    if (!isOpen) return

    var rigName by remember { mutableStateOf("HEXA-S21-Gamma") }
    var selectedModel by remember { mutableStateOf("Antminer S21 (200 TH/s)") }
    var ipAddress by remember { mutableStateOf("192.168.1.115") }
    var stratumPort by remember { mutableStateOf("3333") }
    var workerName by remember { mutableStateOf("hexa_operator.rig_gamma") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF0D1526))
                .border(1.2.dp, HexGold, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Bolt",
                            tint = HexGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONNECT INFRASTRUCTURE",
                            color = HexGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
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

                Text(
                    text = "Provision HEXA Stratum V1/V2 ASIC Rig or Subnet Discovery",
                    color = Color(0x99FFAA00),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Input Fields
                OutlinedTextField(
                    value = rigName,
                    onValueChange = { rigName = it },
                    label = { Text("RIG IDENTIFIER", fontFamily = FontFamily.Monospace, fontSize = 9.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HexGold,
                        unfocusedBorderColor = HexamineBorder,
                        focusedTextColor = HexGoldBright,
                        unfocusedTextColor = HexGoldBright
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text("IP ADDRESS / ASIC HOST", fontFamily = FontFamily.Monospace, fontSize = 9.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HexGold,
                        unfocusedBorderColor = HexamineBorder,
                        focusedTextColor = HexGoldBright,
                        unfocusedTextColor = HexGoldBright
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = stratumPort,
                    onValueChange = { stratumPort = it },
                    label = { Text("STRATUM PORT (3333 / 34255)", fontFamily = FontFamily.Monospace, fontSize = 9.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HexGold,
                        unfocusedBorderColor = HexamineBorder,
                        focusedTextColor = HexGoldBright,
                        unfocusedTextColor = HexGoldBright
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = workerName,
                    onValueChange = { workerName = it },
                    label = { Text("STRATUM WORKER / USER", fontFamily = FontFamily.Monospace, fontSize = 9.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HexGold,
                        unfocusedBorderColor = HexamineBorder,
                        focusedTextColor = HexGoldBright,
                        unfocusedTextColor = HexGoldBright
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Model Selection Chips
                Text(
                    text = "HARDWARE MODEL PRESET:",
                    color = Color(0x66FFAA00),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                val models = listOf("Antminer S21", "Whatsminer M50S+", "Avalon A1466", "Antminer S19 Pro+")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    models.take(2).forEach { m ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (selectedModel.startsWith(m)) HexGold.copy(alpha = 0.2f) else Color(0xFF080D1A))
                                .border(1.dp, if (selectedModel.startsWith(m)) HexGold else HexamineBorder, RoundedCornerShape(4.dp))
                                .clickable { selectedModel = m }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = m,
                                color = if (selectedModel.startsWith(m)) HexGoldBright else Color.Gray,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
                    ) {
                        Text("CANCEL", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            val (hashrate, power) = when {
                                selectedModel.contains("S21") -> Pair(200.0, 3500)
                                selectedModel.contains("M50") -> Pair(136.0, 3260)
                                selectedModel.contains("A14") -> Pair(150.0, 3230)
                                else -> Pair(122.0, 3050)
                            }
                            onAddRig(rigName, selectedModel, ipAddress, hashrate, power)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HexGold,
                            contentColor = Color.Black
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Router, contentDescription = "Add", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("BIND RIG", fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
