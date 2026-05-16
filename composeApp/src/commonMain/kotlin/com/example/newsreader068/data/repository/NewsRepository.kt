package com.example.newsreader068.data.repository

import com.example.newsreader068.data.model.Article
import com.example.newsreader068.data.remote.NewsApi

/**
 * Repository layer untuk mengelola sumber data berita dengan fitur
 * Paginasi, Caching In-Memory, dan Offline Fallback.
 */
class NewsRepository(private val api: NewsApi) {

    // Menggunakan MutableList untuk menampung hasil paginasi bertahap
    private val cachedArticles = mutableListOf<Article>()
    private val cachedArticleDetails = mutableMapOf<Int, Article>()

    /**
     * Mengambil daftar artikel berdasarkan halaman.
     *
     * @param page Halaman yang akan dimuat.
     * @param limit Jumlah artikel per halaman.
     * @param forceRefresh Jika true, bersihkan cache lama dan muat ulang dari halaman 1.
     * @return [Result] berisi List of [Article] (gabungan data lama & baru).
     */
    suspend fun getArticles(page: Int, limit: Int, forceRefresh: Boolean = false): Result<List<Article>> {
        // Jika offline & tidak memaksa refresh, kembalikan cache jika ada
        if (!forceRefresh && cachedArticles.isNotEmpty() && page == 1) {
            return Result.success(cachedArticles.toList())
        }

        return try {
            val networkData = api.getArticles(page, limit)

            // Logika Replace: Bersihkan memori lama jika refresh atau muat awal
            if (forceRefresh || page == 1) {
                cachedArticles.clear()
            }

            // Logika Append: Tambahkan data baru ke daftar yang sudah ada
            cachedArticles.addAll(networkData)
            Result.success(cachedArticles.toList())
        } catch (e: Exception) {
            // Fallback Offline
            if (cachedArticles.isNotEmpty()) {
                Result.success(cachedArticles.toList())
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getArticleById(id: Int): Result<Article> {
        if (cachedArticleDetails.containsKey(id)) {
            return Result.success(cachedArticleDetails[id]!!)
        }
        val articleFromListCache = cachedArticles.find { it.id == id }
        if (articleFromListCache != null) {
            cachedArticleDetails[id] = articleFromListCache
            return Result.success(articleFromListCache)
        }
        return try {
            val networkData = api.getArticleById(id)
            cachedArticleDetails[id] = networkData
            Result.success(networkData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}