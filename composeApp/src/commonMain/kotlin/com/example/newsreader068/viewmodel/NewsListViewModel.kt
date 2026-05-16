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

    // Parameter Paginasi
    private var currentPage = 1
    private val limitPerPage = 10
    private var isLastPage = false
    private var isLoadingNextPage = false

    init { loadArticles() }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            currentPage = 1
            isLastPage = false
            repository.getArticles(page = currentPage, limit = limitPerPage, forceRefresh = false)
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Gagal memuat berita.") }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            currentPage = 1
            isLastPage = false
            repository.getArticles(page = currentPage, limit = limitPerPage, forceRefresh = true)
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Gagal memperbarui berita.") }
            _isRefreshing.value = false
        }
    }

    /**
     * Memuat halaman berikutnya saat pengguna men-scroll ke bawah (Infinite Scroll).
     */
    fun loadNextPage() {
        // Cegah spamming request jika sedang loading atau sudah di halaman terakhir
        if (isLoadingNextPage || isLastPage) return

        isLoadingNextPage = true
        currentPage++

        viewModelScope.launch {
            repository.getArticles(page = currentPage, limit = limitPerPage, forceRefresh = false)
                .onSuccess { data ->
                    // Ambil jumlah item sebelumnya untuk mengecek apakah data baru bertambah
                    val oldSize = (_uiState.value as? UiState.Success)?.data?.size ?: 0
                    if (data.size == oldSize) {
                        isLastPage = true // Tidak ada data baru dari API
                    } else {
                        _uiState.value = UiState.Success(data)
                    }
                    isLoadingNextPage = false
                }
                .onFailure {
                    isLoadingNextPage = false
                    // Tangani error secara diam-diam tanpa merusak UI yang sudah ada
                }
        }
    }
}