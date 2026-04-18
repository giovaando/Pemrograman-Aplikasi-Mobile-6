package com.example.newsreader068.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("📰 News Reader", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = state.data, key = { it.id }) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article.id) }
                            )
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