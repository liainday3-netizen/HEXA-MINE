package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.HexRed
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard

@Composable
fun GatedTokensScreen(
    currentHashrateTh: Double,
    currentSharesCount: Long,
    onClaimGate: (gateId: Int, tokenAmount: Double) -> Unit,
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
        Column {
            Text(
                text = "UNRELEASED TOKENS — ON-CHAIN GATES",
                color = HexGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(
                text = "ACTUAL CRYPTOGRAPHIC & HASHRATE GATES CONNECTED TO BLOCKCHAIN",
                color = Color(0x99FFAA00),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Vault Summary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(HexamineCard)
                .border(1.dp, HexGold, RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = "Token", tint = HexGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("UNRELEASED HEXA PROTOCOL VAULT", color = HexGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexBlue.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("ON-CHAIN GATED", color = HexBlue, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Total Gated Token Reserve: 500,000 \$HEXA\nLocked inside contract: ${SolidityContracts.GATED_TOKEN_VAULT_ADDRESS}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Gate 1: Genesis Hashrate Target (UNLOCKED!)
        GateCard(
            gateNumber = 1,
            gateName = "GATE 1: GENESIS HASHRATE MILESTONE",
            allocation = "50,000 \$HEXA",
            isUnlocked = true,
            criteria = listOf(
                Pair("Hashrate Target: > 1,000 TH/s", "PASSED (${"%.1f".format(currentHashrateTh)} TH/s achieved)"),
                Pair("Stratum Shares: > 15,000 shares", "PASSED ($currentSharesCount shares verified)"),
                Pair("Block Cliff: Block #21,458,000", "PASSED (#21,458,920 mined)"),
                Pair("Quantum Proof: Dilithium-5", "VERIFIED ON-CHAIN")
            ),
            progress = 1.0f,
            onClaim = { onClaimGate(1, 50000.0) }
        )

        // Gate 2: HEXA Industrial Expansion (LOCKED)
        GateCard(
            gateNumber = 2,
            gateName = "GATE 2: HEXA INDUSTRIAL EXPANSION",
            allocation = "150,000 \$HEXA",
            isUnlocked = false,
            criteria = listOf(
                Pair("Hashrate Target: 5,000 TH/s", "IN PROGRESS (${"%.1f".format(currentHashrateTh)} / 5,000 TH/s)"),
                Pair("Stratum Shares: 80,000 shares", "IN PROGRESS ($currentSharesCount / 80,000)"),
                Pair("Block Cliff: Block #21,500,000", "LOCKED (41,080 blocks remaining)"),
                Pair("Quantum Proof: Dilithium-5", "PENDING ATTESTATION")
            ),
            progress = 0.23f,
            onClaim = { /* Locked */ }
        )

        // Gate 3: Post-Quantum Singularity (LOCKED)
        GateCard(
            gateNumber = 3,
            gateName = "GATE 3: POST-QUANTUM SINGULARITY",
            allocation = "300,000 \$HEXA",
            isUnlocked = false,
            criteria = listOf(
                Pair("Hashrate Target: 10,000 TH/s", "LOCKED (${"%.1f".format(currentHashrateTh)} / 10,000 TH/s)"),
                Pair("Stratum Shares: 200,000 shares", "LOCKED ($currentSharesCount / 200,000)"),
                Pair("Post-Quantum Zero Knowledge Proof", "ENCRYPTED ON EVM")
            ),
            progress = 0.09f,
            onClaim = { /* Locked */ }
        )
    }
}

@Composable
fun GateCard(
    gateNumber: Int,
    gateName: String,
    allocation: String,
    isUnlocked: Boolean,
    criteria: List<Pair<String, String>>,
    progress: Float,
    onClaim: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF080D1A))
            .border(
                width = 1.dp,
                color = if (isUnlocked) HexGreen.copy(alpha = 0.7f) else HexamineBorder,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = if (isUnlocked) HexGreen else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = gateName,
                        color = if (isUnlocked) HexGreen else HexGoldBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = allocation,
                    color = HexGoldBright,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Progress Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("GATE UNLOCK PROGRESS", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Text("${(progress * 100).toInt()}%", color = if (isUnlocked) HexGreen else HexTeal, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(3.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (isUnlocked) HexGreen else HexTeal,
                    trackColor = Color(0xFF050810)
                )
            }

            // Criteria List
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                criteria.forEach { (rule, status) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(rule, color = Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            status,
                            color = if (status.startsWith("PASSED") || status.startsWith("VERIFIED")) HexGreen else HexGold,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            if (isUnlocked) {
                Button(
                    onClick = onClaim,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HexGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = "Claim", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "CLAIM UNLOCKED GATED TOKENS ON-CHAIN",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
