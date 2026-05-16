package com.example.newsreader068.data.remote

import com.example.newsreader068.data.model.Article
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Kelas NewsApi menangani komunikasi langsung dengan server (REST API).
 * Menggunakan Ktor Client untuk melakukan HTTP requests.
 */
class NewsApi(private val client: HttpClient) {
    private val baseUrl = "https://jsonplaceholder.typicode.com"

    /**
     * Mengambil daftar artikel dengan dukungan paginasi.
     * @param page Nomor halaman yang ingin diambil.
     * @param limit Jumlah maksimal artikel per halaman.
     * @return List of [Article] hasil parsing JSON.
     */
    suspend fun getArticles(page: Int = 1, limit: Int = 10): List<Article> =
        client.get("$baseUrl/posts?_page=$page&_limit=$limit").body()

    /**
     * Mengambil detail satu artikel berdasarkan ID dari endpoint /posts/{id}.
     * @param id ID unik artikel.
     * @return Objek [Article] tunggal.
     */
    suspend fun getArticleById(id: Int): Article =
        client.get("$baseUrl/posts/$id").body()
}