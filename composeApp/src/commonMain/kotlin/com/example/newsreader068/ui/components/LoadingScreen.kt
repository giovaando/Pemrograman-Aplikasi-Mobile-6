package com.example.newsreader068.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Halaman Loading utama yang menampilkan daftar ArticleCardSkeleton.
 * Meniru struktur layout NewsListScreen.kt agar Shimmer terlihat kohesif.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Scaffold(
        containerColor = Color.White,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        // Layout Skeleton meniru NewsListScreen (jarak spacedBy=24.dp)
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            userScrollEnabled = false // Matikan scroll saat loading
        ) {
            // Tampilkan 3 kartu skeleton berdenyut
            items(3) {
                ArticleCardSkeleton()
            }
        }
    }
}