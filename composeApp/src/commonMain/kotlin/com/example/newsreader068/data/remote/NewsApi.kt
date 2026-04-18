package com.example.newsreader068.data.remote

import com.example.newsreader068.data.model.Article
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsApi(private val client: HttpClient) {
    private val baseUrl = "https://jsonplaceholder.typicode.com"

    suspend fun getArticles(): List<Article> =
        client.get("$baseUrl/posts").body()

    suspend fun getArticleById(id: Int): Article =
        client.get("$baseUrl/posts/$id").body()
}