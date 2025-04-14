package com.example.ai_chat_compose.data.ktor

import com.example.ai_chat_compose.data.model.NewsModel
import com.example.ai_chat_compose.util.Const
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url


object KtorEndPoints {
    suspend fun getNewsData(query: String, page: Int): NewsModel {
        return KtorClient.httpClient.get {
            url("${Const.BASE_URL}v2/everything")
            parameter("q", query)
            parameter("page", page)
            parameter("apiKey", Const.NEWS_API_KEY)
        }.body()
    }
}
