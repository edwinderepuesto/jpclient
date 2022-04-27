package com.edwinderepuesto.jpclient.data.repository

import com.edwinderepuesto.jpclient.data.api.JsonPlaceholderApi
import com.edwinderepuesto.jpclient.data.dao.PostDao
import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.data.dto.PostComment
import com.edwinderepuesto.jpclient.data.dto.User

class MainRepository(
    private val api: JsonPlaceholderApi,
    private val postDao: PostDao
) {
    suspend fun getPosts(): List<Post> {
        val storedPosts = postDao.loadAllPosts()

        return storedPosts.ifEmpty {
            val posts = api.getPosts()
            postDao.insertAll(posts)
            posts
        }
    }

    suspend fun getCommentsByPostId(postId: Int): List<PostComment> {
        return api.getCommentsByPostId(postId)
    }

    suspend fun getUserById(userId: Int): User {
        return api.getUserById(userId)
    }

    fun updatePost(post: Post) {
        postDao.insertAll(listOf(post))
    }
}