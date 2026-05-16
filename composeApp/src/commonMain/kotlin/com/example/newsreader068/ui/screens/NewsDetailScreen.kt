package com.example.newsreader068.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.newsreader068.ui.components.ErrorScreen
import com.example.newsreader068.ui.components.LoadingScreen
import com.example.newsreader068.viewmodel.NewsDetailViewModel
import com.example.newsreader068.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(articleId: Int, viewModel: NewsDetailViewModel, onBack: () -> Unit) {
    LaunchedEffect(articleId) { viewModel.loadArticle(articleId) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Design Token Colors
    val bgColor = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF414754)
    val borderColor = Color(0xFFE5E5E5)
    val accentBlue = Color(0xFF0070F3)

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("NewsReader068", fontWeight = FontWeight.Bold, color = textPrimary, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = textPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    scrolledContainerColor = bgColor
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is UiState.Loading -> LoadingScreen(Modifier.padding(paddingValues))

            is UiState.Success -> {
                val article = state.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 1. Hero Image Full Width
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.padding(24.dp)) {
                        // 2. Kategori & Waktu Baca
                        val category = if (article.userId % 2 == 0) "TECHNOLOGY" else "LIFESTYLE"
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFFF0F0F0),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = category,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textSecondary,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("• 8 min read", fontSize = 12.sp, color = textSecondary)
                        }

                        Spacer(Modifier.height(16.dp))

                        // 3. Judul Artikel
                        Text(
                            text = article.title.replaceFirstChar { it.uppercase() },
                            color = textPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 34.sp
                        )

                        Spacer(Modifier.height(24.dp))

                        // 4. Author Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE5E5E5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Author ID: @user_${article.userId}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                                Text("Oct 27, 2023", fontSize = 12.sp, color = textSecondary)
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Default.BookmarkBorder, contentDescription = "Bookmark", tint = textSecondary)
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = textSecondary)
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(color = borderColor)
                        Spacer(Modifier.height(24.dp))

                        // 5. Body Artikel
                        Text(
                            text = article.description + "\n\n" + article.description, // Didoberlkan agar terlihat lebih panjang
                            color = textPrimary,
                            fontSize = 18.sp,
                            lineHeight = 28.sp, // Line height lega sesuai desain Linear
                            fontWeight = FontWeight.Normal
                        )

                        // Pseudo Blockquote
                        Spacer(Modifier.height(24.dp))
                        Row(modifier = Modifier.padding(vertical = 8.dp)) {
                            Box(modifier = Modifier.width(4.dp).height(60.dp).background(accentBlue))
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text = "\"Design is not just what it looks like and feels like. Design is how it works when everything unnecessary is removed.\"",
                                color = textPrimary,
                                fontSize = 20.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                lineHeight = 28.sp
                            )
                        }

                        Spacer(Modifier.height(32.dp))
                        HorizontalDivider(color = borderColor)
                        Spacer(Modifier.height(24.dp))

                        // 6. Tags Section
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TagChip("#DesignSystem")
                            TagChip("#UX")
                            TagChip("#Minimalism")
                        }

                        Spacer(Modifier.height(32.dp))

                        // 7. Stay Informed (Horizontal Version)
                        StayInformedHorizontal()

                        Spacer(Modifier.height(48.dp))

                        // 8. Minimalist Footer
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("NEWSREADER068", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = textSecondary)
                            Spacer(Modifier.height(8.dp))
                            Text("Designed for clarity. Engineered for speed.", fontSize = 12.sp, color = textSecondary)
                            Spacer(Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text("Archive", fontSize = 12.sp, color = textSecondary)
                                Text("About", fontSize = 12.sp, color = textSecondary)
                                Text("Support", fontSize = 12.sp, color = textSecondary)
                                Text("Terms", fontSize = 12.sp, color = textSecondary)
                            }
                        }
                    }
                }
            }

            is UiState.Error -> ErrorScreen(
                message = state.message,
                onRetry = { viewModel.retry(articleId) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun TagChip(text: String) {
    Surface(
        color = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = Color(0xFF414754)
        )
    }
}

@Composable
fun StayInformedHorizontal() {
    val surfaceDim = Color(0xFFF9F9F9)
    val borderColor = Color(0xFFE5E5E5)
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF414754)
    val accentBlue = Color(0xFF0070F3)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceDim),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "Stay Informed", color = textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(text = "Get the weekly digest of deep-tech news.", color = textSecondary, fontSize = 14.sp)

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                var email by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email address", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 0.dp, bottomEnd = 0.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 4.dp, bottomEnd = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentBlue),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    Text("Join", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}