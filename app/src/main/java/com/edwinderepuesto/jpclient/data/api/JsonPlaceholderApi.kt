package com.edwinderepuesto.jpclient.data.api

import com.edwinderepuesto.jpclient.data.dto.Post
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class JsonPlaceholderApi(private val client: HttpClient) {
    suspend fun getPosts(): List<Post> =
        client.get("https://jsonplaceholder.typicode.com/posts").body()
}