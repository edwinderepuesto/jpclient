package com.edwinderepuesto.jpclient.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.edwinderepuesto.jpclient.data.api.JsonPlaceholderApi
import com.edwinderepuesto.jpclient.data.database.AppDatabase
import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.data.dto.PostComment
import com.edwinderepuesto.jpclient.data.dto.User
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.lang.ref.WeakReference

class MainRepository(
    contextRef: WeakReference<Context>,
) {
    private val postDao = Room.databaseBuilder(
        contextRef.get()!!,
        AppDatabase::class.java, "jpclient-db"
    ).build().postDao()

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
        val storedPosts = postDao.loadAllPosts()

        return storedPosts.ifEmpty {
            val posts = jsonPlaceholderApi.getPosts()
            postDao.insertAll(posts)
            posts
        }
    }

    suspend fun getCommentsByPostId(postId: Int): List<PostComment> {
        return jsonPlaceholderApi.getCommentsByPostId(postId)
    }

    suspend fun getUserById(userId: Int): User {
        return jsonPlaceholderApi.getUserById(userId)
    }
}