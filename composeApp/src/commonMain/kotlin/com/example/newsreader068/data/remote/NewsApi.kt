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
     * Mengambil daftar semua artikel (berita) dari endpoint /posts.
     * @return List of [Article] hasil parsing JSON.
     */
    suspend fun getArticles(): List<Article> =
        client.get("$baseUrl/posts").body()

    /**
     * Mengambil detail satu artikel berdasarkan ID dari endpoint /posts/{id}.
     * @param id ID unik artikel.
     * @return Objek [Article] tunggal.
     */
    suspend fun getArticleById(id: Int): Article =
        client.get("$baseUrl/posts/$id").body()
}