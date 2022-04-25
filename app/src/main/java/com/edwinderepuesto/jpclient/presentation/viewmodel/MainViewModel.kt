package com.edwinderepuesto.jpclient.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwinderepuesto.jpclient.common.MyResult
import com.edwinderepuesto.jpclient.data.api.JsonPlaceholderApi
import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.data.dto.PostComment
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainViewModel : ViewModel() {
    private val _postsState = MutableStateFlow<MyResult<List<Post>>>(MyResult.Loading(false))
    val postsState: StateFlow<MyResult<List<Post>>> = _postsState.asStateFlow()

    private val _commentsState =
        MutableStateFlow<MyResult<List<PostComment>>>(MyResult.Loading(false))
    val commentsState: StateFlow<MyResult<List<PostComment>>> = _commentsState.asStateFlow()

    private var postsJob: Job? = null
    private var commentsJob: Job? = null

    private val apiClient = JsonPlaceholderApi(HttpClient {
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
    })

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        postsJob?.cancel()

        postsJob = viewModelScope.launch {
            try {
                _postsState.update {
                    MyResult.Loading(true)
                }

                val posts = apiClient.getPosts()

                _postsState.update {
                    MyResult.Success(posts)
                }
            } catch (ioException: IOException) {
                Log.d("ktor", "Error fetching posts:")
                ioException.printStackTrace()
                _postsState.update {
                    MyResult.Error(ioException.message ?: "Unknown error")
                }
            }
        }
    }

    fun fetchComments(postId: Int) {
        commentsJob?.cancel()

        commentsJob = viewModelScope.launch {
            try {
                _commentsState.update {
                    MyResult.Loading(true)
                }

                val comments = apiClient.getCommentsByPostId(postId)

                _commentsState.update {
                    MyResult.Success(comments)
                }
            } catch (ioException: IOException) {
                Log.d("ktor", "Error fetching comments:")
                ioException.printStackTrace()
                _commentsState.update {
                    MyResult.Error(ioException.message ?: "Unknown error")
                }
            }
        }
    }
}