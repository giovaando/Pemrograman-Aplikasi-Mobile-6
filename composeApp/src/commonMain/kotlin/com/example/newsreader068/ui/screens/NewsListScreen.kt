package com.example.newsreader068.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(viewModel: NewsListViewModel, onArticleClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val bgColor = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF1A1A1A)
    val accentBlue = Color(0xFF0070F3)

    // Deteksi Status Scroll
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Logika Scroll to Top: Tampilkan tombol jika sudah scroll melewati item indeks ke-3
    val showScrollToTopFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 3 }
    }

    // Logika Infinite Scroll: Muat data baru saat tersisa 2 item sebelum layar paling bawah
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItem >= totalItems - 2
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage()
        }
    }

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = { Text("NewsReader068", fontWeight = FontWeight.Bold, color = textPrimary) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) { Icon(Icons.Default.Menu, "Menu", tint = textPrimary) }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) { Icon(Icons.Default.Search, "Search", tint = textPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor, scrolledContainerColor = bgColor)
            )
        },
        floatingActionButton = {
            if (showScrollToTopFab) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            // Animasi halus kembali ke paling atas
                            listState.animateScrollToItem(0)
                        }
                    },
                    containerColor = accentBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to top")
                }
            }
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
                        state = listState, // Pasang state pemantauan di sini
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(items = state.data, key = { it.id }) { article ->
                            ArticleCard(article = article, onClick = { onArticleClick(article.id) })
                        }

                        // Footer: Loading indikator mini untuk paginasi (opsional) atau form Newsletter
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
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceDim),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Stay Informed", color = textPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Get daily updates on technology, architecture, and global news.", color = textSecondary, fontSize = 14.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                placeholder = { Text("email@example.com", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = accentBlue, unfocusedBorderColor = borderColor),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp), colors = ButtonDefaults.buttonColors(containerColor = accentBlue)
            ) { Text("Subscribe", color = Color.White, fontSize = 16.sp) }
        }
    }
}