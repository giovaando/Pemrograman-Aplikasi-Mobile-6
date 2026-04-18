package com.example.newsreader068.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null,
                modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
            Text("Terjadi Kesalahan", style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error)
            Text(message, style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = onRetry) { Text("Coba Lagi") }
        }
    }
}