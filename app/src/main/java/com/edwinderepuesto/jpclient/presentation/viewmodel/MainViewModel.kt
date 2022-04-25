package com.edwinderepuesto.jpclient.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwinderepuesto.jpclient.common.MyResult
import com.edwinderepuesto.jpclient.data.api.JsonPlaceholderApi
import com.edwinderepuesto.jpclient.data.dto.Post
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
    private val _uiState = MutableStateFlow<MyResult<List<Post>>>(MyResult.Loading(false))
    val uiState: StateFlow<MyResult<List<Post>>> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    private val apiClient = JsonPlaceholderApi(HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
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
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch() {
            try {
                _uiState.update {
                    MyResult.Loading(true)
                }

                val posts = apiClient.getPosts()

                _uiState.update {
                    MyResult.Success(posts)
                }
            } catch (ioException: IOException) {
                Log.d("ktor", "Error fetching posts:")
                ioException.printStackTrace()
                _uiState.update {
                    MyResult.Error(ioException.message ?: "Unknown error")
                }
            }
        }
    }
}