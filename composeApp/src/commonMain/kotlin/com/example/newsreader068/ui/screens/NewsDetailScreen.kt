package com.example.newsreader068.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Artikel") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        modifier = Modifier.fillMaxWidth().height(240.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Artikel #${article.id} · User ${article.userId}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = article.title.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(12.dp))
                        Text(article.description, style = MaterialTheme.typography.bodyLarge,
                            lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.6)
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