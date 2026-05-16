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
 * ViewModel untuk mengatur UI state pada layar daftar berita (NewsListScreen).
 * Memisahkan state menjadi Loading, Success, dan Error untuk UI yang reaktif.
 */
class NewsListViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Article>>> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init { loadArticles() }

    /**
     * Memuat artikel saat aplikasi pertama kali dibuka.
     * Memanfaatkan cache jika sudah ada untuk menghemat kuota dan mempercepat pemuatan.
     */
    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getArticles(forceRefresh = false)
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Gagal memuat berita. Periksa koneksi Anda.") }
        }
    }

    /**
     * Memuat ulang artikel dengan memaksa pengambilan data terbaru dari jaringan.
     * Fungsi ini dipicu saat pengguna melakukan gesture pull-to-refresh.
     */
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getArticles(forceRefresh = true)
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Gagal memperbarui berita. Periksa koneksi Anda.") }
            _isRefreshing.value = false
        }
    }
}