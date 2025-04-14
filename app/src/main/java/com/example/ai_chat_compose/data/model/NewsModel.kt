package com.example.ai_chat_compose.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsModel(
    val articles: List<Article> = listOf(),
    val status: String = "",
    val totalResults: Int = 0
) {
    @Serializable
    data class Article(
        val author: String? = "",
        val content: String? = "",
        val description: String? = "",
        val publishedAt: String? = "",
        val source: Source = Source(),
        val title: String? = "",
        val url: String? = "",
        val urlToImage: String? = ""
    ) {
        @Serializable
        data class Source(
            val id: String? = "",
            val name: String = ""
        )
    }
}