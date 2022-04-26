package com.edwinderepuesto.jpclient.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.edwinderepuesto.jpclient.data.dto.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(posts: List<Post>)
}