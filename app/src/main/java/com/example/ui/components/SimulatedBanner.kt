package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppMode

/** Always-visible banner shown above every screen while simulated mode is on. */
@Composable
fun SimulatedBanner(modifier: Modifier = Modifier) {
    val simulated by AppMode.isSimulated.collectAsState()
    if (!simulated) return
    Text(
        text = "SIMULATED MODE · ALL RIGS, SHARES, EARNINGS & PAYOUTS ARE DEMO DATA · NO REAL FUNDS",
        color = Color.Black,
        fontSize = 10.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFC107))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
