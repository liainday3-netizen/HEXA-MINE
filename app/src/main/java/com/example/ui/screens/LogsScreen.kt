package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.LogEntity
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexQuantumPurple
import com.example.ui.theme.HexRed
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import com.example.ui.theme.HexamineBorder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LogsScreen(
    logs: List<LogEntity>,
    onClearLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("ALL") }

    val filteredLogs = if (selectedCategory == "ALL") {
        logs
    } else {
        logs.filter { it.category == selectedCategory }
    }

    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SYSTEM TERMINAL LOGS",
                    color = HexGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "REAL-TIME STRATUM, WEB3, & ASIC HARDWARE TELEMETRY",
                    color = Color(0x99FFAA00),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                text = "CLEAR LOGS",
                color = HexRed,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(HexRed.copy(alpha = 0.15f))
                    .border(0.8.dp, HexRed, RoundedCornerShape(3.dp))
                    .clickable { onClearLogs() }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
            )
        }

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL", "STRATUM", "WEB3", "RIG", "PAYOUT", "QUANTUM").forEach { cat ->
                val isCur = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isCur) HexGold.copy(alpha = 0.25f) else Color(0xFF080D1A))
                        .border(0.8.dp, if (isCur) HexGold else HexamineBorder, RoundedCornerShape(3.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (isCur) HexGoldBright else Color.Gray,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (isCur) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Terminal Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF020408))
                .border(1.dp, HexBlue.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                .padding(10.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredLogs) { log ->
                    val levelColor = when (log.level) {
                        "OK" -> HexGreen
                        "WARN" -> HexGold
                        "ERR" -> HexRed
                        else -> HexTeal
                    }
                    val catColor = when (log.category) {
                        "STRATUM" -> HexTeal
                        "WEB3" -> HexBlue
                        "QUANTUM" -> HexQuantumPurple
                        "PAYOUT" -> HexUsdtGreen
                        else -> HexGold
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = timeFormat.format(Date(log.timestamp)),
                            color = HexBlue.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "[${log.category}]",
                            color = catColor,
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
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
