package com.example.newsreader068.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int,
    @SerialName("userId") val userId: Int,
    val title: String,
    @SerialName("body") val description: String
) {
    val imageUrl: String
        get() = "https://picsum.photos/seed/$id/600/400"

    val shortDescription: String
        get() = if (description.length > 100) description.take(100) + "..." else description
}