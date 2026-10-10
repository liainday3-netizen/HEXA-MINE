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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PoolEntity
import com.example.stratum.StratumEngine
import com.example.stratum.StratumState
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexRed
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexamineBorder
import com.example.ui.theme.HexamineCard

@Composable
fun StratumScreen(
    stratumState: StratumState,
    pools: List<PoolEntity>,
    onSelectPool: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Column {
            Text(
                text = "STRATUM MINING PROTOCOL ENGINE",
                color = HexGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(
                text = "STRATUM V1 / V2 PROTOCOL STREAM · NOISE HANDSHAKE · SHARE VERIFICATION",
                color = Color(0x99FFAA00),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Stratum Status Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(HexamineCard)
                .border(0.8.dp, HexTeal.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sensors, contentDescription = "Sensors", tint = HexTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ACTIVE STRATUM CHANNEL", color = HexTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HexGreen.copy(alpha = 0.2f))
                            .border(0.5.dp, HexGreen, RoundedCornerShape(3.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(stratumState.protocolVersion, color = HexGreen, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(stratumState.connectedPool, color = HexGoldBright, fontSize = 10.sp, fontFamily = FontFamily.Monospace)

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("DIFFICULTY", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${stratumState.currentDifficulty}", color = HexGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text("ACCEPTED SHARES", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${stratumState.acceptedSharesCount}", color = HexGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text("REJECTED", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${stratumState.rejectedSharesCount}", color = HexRed, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text("PING", color = Color(0x66FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("${stratumState.pingMs} ms", color = HexTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // Pools List
        Text(
            text = "MINING POOL ENDPOINTS:",
            color = HexGold,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(pools) { pool ->
                val isActive = pool.isActive
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isActive) HexGreen.copy(alpha = 0.08f) else Color(0xFF080D1A))
                        .border(1.dp, if (isActive) HexGreen.copy(alpha = 0.6f) else HexamineBorder, RoundedCornerShape(4.dp))
                        .clickable { onSelectPool(pool.id) }
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isActive) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = HexGreen, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(pool.name, color = if (isActive) HexGreen else HexGoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${pool.url}:${pool.port}", color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            Text("Worker: ${pool.workerName} · Fee: ${pool.feePercent}%", color = Color(0x99FFAA00), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isActive) HexGreen.copy(alpha = 0.2f) else HexamineCard)
                                .border(0.8.dp, if (isActive) HexGreen else HexamineBorder, RoundedCornerShape(3.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isActive) "CONNECTED" else "CONNECT",
                                color = if (isActive) HexGreen else Color.LightGray,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
