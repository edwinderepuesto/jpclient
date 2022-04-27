package com.edwinderepuesto.jpclient.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.edwinderepuesto.jpclient.data.api.JsonPlaceholderApi
import com.edwinderepuesto.jpclient.data.database.AppDatabase
import com.edwinderepuesto.jpclient.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMainRepository(@ApplicationContext appContext: Context): MainRepository {
        val httpClient = HttpClient {
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
        }

        val api = JsonPlaceholderApi(httpClient)

        val postDao = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "jpclient-db"
        ).build().postDao()

        return MainRepository(api, postDao)
    }
}