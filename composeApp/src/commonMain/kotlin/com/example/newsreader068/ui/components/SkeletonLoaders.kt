package com.example.newsreader068.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

// Warna Token untuk Shimmer
val SkeletonGray = Color(0xFFEEEEEE)
val BorderColor = Color(0xFFE5E5E5)

/**
 * Komponen dasar untuk efek visual "denyut" atau "shimmer" multiplatform.
 */
@Composable
fun ShimmerContainer(
    modifier: Modifier = Modifier,
    content: @Composable (alpha: Float) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    content(alpha)
}

/**
 * Komponen Skeleton yang meniru struktur ArticleCard yang sudah diperbarui.
 */
@Composable
fun ArticleCardSkeleton(modifier: Modifier = Modifier) {
    ShimmerContainer(modifier = modifier) { alpha ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                // Image Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .graphicsLayer(alpha = alpha)
                        .background(SkeletonGray)
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    // Category Pseudo Tags (Small)
                    Row {
                        SkeletonBox(width = 80.dp, height = 12.dp, alpha = alpha)
                        Spacer(Modifier.width(8.dp))
                        SkeletonBox(width = 60.dp, height = 12.dp, alpha = alpha)
                    }

                    Spacer(Modifier.height(12.dp))

                    // Title Placeholders (Dua baris)
                    SkeletonBox(width = Modifier.fillMaxWidth(0.9f), height = 20.dp, alpha = alpha)
                    Spacer(Modifier.height(8.dp))
                    SkeletonBox(width = Modifier.fillMaxWidth(0.6f), height = 20.dp, alpha = alpha)

                    Spacer(Modifier.height(16.dp))

                    // Description Placeholders (Dua baris redup)
                    SkeletonBox(width = Modifier.fillMaxWidth(), height = 16.dp, alpha = alpha)
                    Spacer(Modifier.height(6.dp))
                    SkeletonBox(width = Modifier.fillMaxWidth(0.8f), height = 16.dp, alpha = alpha)

                    Spacer(Modifier.height(16.dp))

                    // "Read More →" Placeholder
                    SkeletonBox(width = 100.dp, height = 14.dp, alpha = alpha, color = Color(0xFFD0E5FE))
                }
            }
        }
    }
}

/**
 * Sub-komponen generik untuk kotak abu-abu skeleton.
 */
@Composable
fun SkeletonBox(
    width: Modifier,
    height: androidx.compose.ui.unit.Dp,
    alpha: Float,
    color: Color = SkeletonGray
) {
    Box(
        modifier = width
            .height(height)
            .graphicsLayer(alpha = alpha)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
    )
}

// Overload untuk lebar fixed DP
@Composable
fun SkeletonBox(
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    alpha: Float,
    color: Color = SkeletonGray
) {
    SkeletonBox(width = Modifier.width(width), height = height, alpha = alpha, color = color)
}