package com.example.newsreader068.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreader068.data.model.Article
import com.example.newsreader068.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengatur UI state pada layar detail berita (NewsDetailScreen).
 */
class NewsDetailViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Article>>(UiState.Loading)
    val uiState: StateFlow<UiState<Article>> = _uiState.asStateFlow()

    /**
     * Meminta data detail artikel dari repository berdasarkan ID.
     */
    fun loadArticle(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getArticleById(id)
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Gagal memuat detail artikel. Periksa koneksi Anda.") }
        }
    }

    fun retry(id: Int) = loadArticle(id)
}