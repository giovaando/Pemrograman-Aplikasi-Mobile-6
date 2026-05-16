package com.example.newsreader068.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsreader068.ui.components.ArticleCard
import com.example.newsreader068.ui.components.ErrorScreen
import com.example.newsreader068.ui.components.LoadingScreen
import com.example.newsreader068.viewmodel.NewsListViewModel
import com.example.newsreader068.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(viewModel: NewsListViewModel, onArticleClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    // Design Token Colors
    val bgColor = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF1A1A1A)

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("NewsReader068", fontWeight = FontWeight.Bold, color = textPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = textPrimary)
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
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.refresh() },
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp) // Spacing yang lebih lebar
                    ) {
                        items(items = state.data, key = { it.id }) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article.id) }
                            )
                        }

                        // Footer: Stay Informed Newsletter Card
                        item {
                            StayInformedSection()
                        }
                    }
                }
            }

            is UiState.Error -> ErrorScreen(
                message = state.message,
                onRetry = { viewModel.loadArticles() },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun StayInformedSection() {
    val surfaceDim = Color(0xFFF9F9F9)
    val borderColor = Color(0xFFE5E5E5)
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF414754)
    val accentBlue = Color(0xFF0070F3)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceDim),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Stay Informed",
                color = textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Get daily updates on technology, architecture, and global news.",
                color = textSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(24.dp))

            // TextField dengan border 4px radius sesuai dokumen
            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("email@example.com", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentBlue,
                    unfocusedBorderColor = borderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))

            // Tombol Solid Accent Blue 4px radius
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentBlue)
            ) {
                Text("Subscribe", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}