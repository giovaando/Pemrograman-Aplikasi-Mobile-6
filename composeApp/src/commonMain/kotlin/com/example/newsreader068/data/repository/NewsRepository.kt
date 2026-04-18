package com.example.newsreader068.data.repository

import com.example.newsreader068.data.model.Article
import com.example.newsreader068.data.remote.NewsApi

class NewsRepository(private val api: NewsApi) {

    suspend fun getArticles(): Result<List<Article>> = try {
        Result.success(api.getArticles())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getArticleById(id: Int): Result<Article> = try {
        Result.success(api.getArticleById(id))
    } catch (e: Exception) {
        Result.failure(e)
    }
}