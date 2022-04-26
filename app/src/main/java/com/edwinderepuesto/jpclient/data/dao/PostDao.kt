package com.edwinderepuesto.jpclient.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edwinderepuesto.jpclient.data.dto.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)

    @Query("SELECT * FROM post")
    fun loadAllPosts(): List<Post>
}