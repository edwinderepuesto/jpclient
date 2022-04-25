package com.edwinderepuesto.jpclient.data.repository

import android.util.Log
import com.edwinderepuesto.jpclient.data.api.JsonPlaceholderApi
import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.data.dto.PostComment
import com.edwinderepuesto.jpclient.data.dto.User
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MainRepository {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Ktor Logger ->", message)
                }
            }
            level = LogLevel.ALL
        }
    }

    private val jsonPlaceholderApi = JsonPlaceholderApi(httpClient)

    suspend fun getPosts(): List<Post> {
        return jsonPlaceholderApi.getPosts()
    }

    suspend fun getCommentsByPostId(postId: Int): List<PostComment> {
        return jsonPlaceholderApi.getCommentsByPostId(postId)
    }

    suspend fun getUserById(userId: Int): User {
        return jsonPlaceholderApi.getUserById(userId)
    }
}