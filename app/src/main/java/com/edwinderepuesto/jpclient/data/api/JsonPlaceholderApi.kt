package com.edwinderepuesto.jpclient.data.api

import com.edwinderepuesto.jpclient.common.Constants
import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.data.dto.PostComment
import com.edwinderepuesto.jpclient.data.dto.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class JsonPlaceholderApi(private val client: HttpClient) {
    suspend fun getPosts(): List<Post> =
        client.get(Constants.BASE_URL + "/posts").body()

    suspend fun getCommentsByPostId(postId: Int): List<PostComment> =
        client.get(Constants.BASE_URL + "/comments?postId=$postId").body()

    suspend fun getUserById(userId: Int): User =
        client.get(Constants.BASE_URL + "/users/$userId").body()
}