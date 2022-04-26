package com.edwinderepuesto.jpclient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edwinderepuesto.jpclient.data.dao.PostDao
import com.edwinderepuesto.jpclient.data.dto.Post

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}