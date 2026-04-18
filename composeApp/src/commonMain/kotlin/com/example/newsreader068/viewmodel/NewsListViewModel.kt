package com.example.newsreader068.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreader068.data.model.Article
import com.example.newsreader068.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsListViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Article>>> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init { loadArticles() }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getArticles()
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Gagal memuat berita") }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getArticles()
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Gagal memuat berita") }
            _isRefreshing.value = false
        }
    }
}