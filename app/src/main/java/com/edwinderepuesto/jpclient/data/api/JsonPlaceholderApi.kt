package com.edwinderepuesto.jpclient.data.api

import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.data.dto.PostComment
import com.edwinderepuesto.jpclient.data.dto.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class JsonPlaceholderApi(private val client: HttpClient) {
    suspend fun getPosts(): List<Post> =
        client.get("https://jsonplaceholder.typicode.com/posts").body()

    suspend fun getCommentsByPostId(postId: Int): List<PostComment> =
        client.get("https://jsonplaceholder.typicode.com/comments?postId=$postId").body()

    suspend fun getUserById(userId: Int): User =
        client.get("https://jsonplaceholder.typicode.com/users/$userId").body()
}