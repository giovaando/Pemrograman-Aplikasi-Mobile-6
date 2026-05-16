package com.example.newsreader068.data.repository

import com.example.newsreader068.data.model.Article
import com.example.newsreader068.data.remote.NewsApi

/**
 * Repository layer untuk mengelola sumber data berita.
 * Mengabstraksi sumber data (API vs Cache lokal) dari ViewModel.
 * Telah mengimplementasikan caching di memori untuk dukungan offline sederhana.
 */
class NewsRepository(private val api: NewsApi) {

    // Menyimpan data di memori selama aplikasi berjalan (Level 1 Cache)
    private var cachedArticles: List<Article>? = null
    private val cachedArticleDetails = mutableMapOf<Int, Article>()

    /**
     * Mengambil daftar artikel. Akan memprioritaskan cache jika tersedia dan
     * tidak dipaksa melakukan penyegaran jaringan.
     *
     * @param forceRefresh Jika true, paksa ambil dari jaringan dan perbarui cache.
     * @return [Result] berisi List of [Article] jika sukses, atau Exception jika gagal.
     */
    suspend fun getArticles(forceRefresh: Boolean = false): Result<List<Article>> {
        // Mendukung offline: Jika tidak memaksa refresh dan cache ada, kembalikan cache
        if (!forceRefresh && cachedArticles != null) {
            return Result.success(cachedArticles!!)
        }

        return try {
            // Ambil data terbaru dari jaringan
            val networkData = api.getArticles()
            // Simpan ke cache lokal
            cachedArticles = networkData
            Result.success(networkData)
        } catch (e: Exception) {
            // Fallback Offline: Jika internet mati tapi kita punya cache, tampilkan cache
            if (cachedArticles != null) {
                Result.success(cachedArticles!!)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Mengambil detail artikel. Mencari di cache lokal terlebih dahulu
     * untuk mencegah pemuatan (loading) yang tidak perlu saat offline.
     *
     * @param id Identifier unik dari artikel.
     */
    suspend fun getArticleById(id: Int): Result<Article> {
        // Cek apakah detail artikel sudah pernah dibuka dan tersimpan di cache
        if (cachedArticleDetails.containsKey(id)) {
            return Result.success(cachedArticleDetails[id]!!)
        }

        // Atau cek apakah artikel ini ada di daftar cache artikel umum
        val articleFromListCache = cachedArticles?.find { it.id == id }
        if (articleFromListCache != null) {
            cachedArticleDetails[id] = articleFromListCache
            return Result.success(articleFromListCache)
        }

        return try {
            val networkData = api.getArticleById(id)
            cachedArticleDetails[id] = networkData // Simpan ke cache
            Result.success(networkData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}