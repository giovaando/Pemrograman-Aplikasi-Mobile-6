package com.example.newsreader068.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null // Opsional untuk tombol kembali ke Feed
) {
    // Design Tokens
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF414754)
    val accentBlue = Color(0xFF0070F3)
    val errorRed = Color(0xFFBA1A1A)

    Box(
        modifier = modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ikon Koneksi Terputus
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = "Connection Error",
                modifier = Modifier.size(56.dp),
                tint = errorRed
            )

            Spacer(Modifier.height(24.dp))

            // Judul
            Text(
                text = "Connection Lost",
                color = textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // Pesan Deskriptif (Sesuai Desain) + Detail Error Sistem
            Text(
                text = "We're having trouble reaching our servers. Please check your internet connection and try refreshing the page.\n\n($message)",
                color = textSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(32.dp))

            // Tombol Utama Sesuai Desain (Radius 4px, Solid Blue)
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentBlue)
            ) {
                Text("Try Again", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            // Tombol Sekunder (Kembali ke Feed) hanya muncul jika fungsi dilempar
            if (onGoBack != null) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "← Return to Feed",
                    color = textSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { onGoBack() }
                        .padding(8.dp)
                )
            }
        }
    }
}