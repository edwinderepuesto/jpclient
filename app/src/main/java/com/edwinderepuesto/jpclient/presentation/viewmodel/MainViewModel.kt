package com.edwinderepuesto.jpclient.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwinderepuesto.jpclient.common.MyResult
import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.data.dto.PostComment
import com.edwinderepuesto.jpclient.data.dto.User
import com.edwinderepuesto.jpclient.data.repository.MainRepository
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postsState = MutableStateFlow<MyResult<List<Post>>>(MyResult.Loading(false))
    val postsState: StateFlow<MyResult<List<Post>>> = _postsState.asStateFlow()

    private val _commentsState =
        MutableStateFlow<MyResult<List<PostComment>>>(MyResult.Loading(false))
    val commentsState: StateFlow<MyResult<List<PostComment>>> = _commentsState.asStateFlow()

    private val _userState =
        MutableStateFlow<MyResult<User>>(MyResult.Loading(false))
    val userState: StateFlow<MyResult<User>> = _userState.asStateFlow()

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost.asStateFlow()

    private var postsJob: Job? = null
    private var commentsJob: Job? = null
    private var userJob: Job? = null

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        postsJob?.cancel()

        postsJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                _postsState.update {
                    MyResult.Loading(true)
                }

                val posts = repository.getPosts()

                _postsState.update {
                    MyResult.Success(posts)
                }
            } catch (exception: Exception) {
                Log.d("ktor", "Error fetching posts:")
                exception.printStackTrace()
                _postsState.update {
                    MyResult.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun fetchSelectedPostDetails() {
        val selectedPost = _selectedPost.value ?: return

        commentsJob?.cancel()
        commentsJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                _commentsState.update {
                    MyResult.Loading(true)
                }

                val comments = repository.getCommentsByPostId(selectedPost.id)

                _commentsState.update {
                    MyResult.Success(comments)
                }
            } catch (exception: Exception) {
                Log.d("ktor", "Error fetching comments:")
                exception.printStackTrace()
                _commentsState.update {
                    MyResult.Error(exception.message ?: "Unknown error")
                }
            }
        }

        userJob?.cancel()
        userJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                _userState.update {
                    MyResult.Loading(true)
                }

                val user = repository.getUserById(selectedPost.userId)

                _userState.update {
                    MyResult.Success(user)
                }
            } catch (exception: Exception) {
                Log.d("ktor", "Error fetching user:")
                exception.printStackTrace()
                _userState.update {
                    MyResult.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun toggleFavoriteStatus(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePost(post)
        }
    }

    fun removePostIdFromDataSet(postIdToDelete: Int) {
        (_postsState.value as? MyResult.Success)?.let { value ->
            val reducedList = value.data.toMutableList()
            reducedList.removeIf { item -> item.id == postIdToDelete }
            _postsState.update {
                MyResult.Success(reducedList)
            }
        }
    }

    fun clearPostsDataSet() {
        _postsState.update {
            MyResult.Success(emptyList())
        }
    }

    fun markPostAsSelected(post: Post) {
        _selectedPost.update {
            post
        }
    }
}