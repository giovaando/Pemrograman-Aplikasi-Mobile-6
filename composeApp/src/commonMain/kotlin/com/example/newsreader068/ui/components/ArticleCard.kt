package com.example.newsreader068.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.newsreader068.data.model.Article

@Composable
fun ArticleCard(article: Article, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // Design Token Colors
    val surfaceColor = Color(0xFFFFFFFF)
    val borderColor = Color(0xFFE5E5E5)
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF414754)
    val accentBlue = Color(0xFF0070F3)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Kunci desain flat
    ) {
        Column {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                // Label Kategori Pseudo (berdasarkan userID agar terlihat dinamis)
                val category = if (article.userId % 2 == 0) "TECHNOLOGY" else "LIFESTYLE"
                Text(
                    text = category,
                    color = textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = article.title.replaceFirstChar { it.uppercase() },
                    color = textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = article.shortDescription,
                    color = textSecondary,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Read more →",
                    color = accentBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}