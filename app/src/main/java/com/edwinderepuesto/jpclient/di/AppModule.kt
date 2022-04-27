package com.edwinderepuesto.jpclient.di

import android.content.Context
import com.edwinderepuesto.jpclient.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.lang.ref.WeakReference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMainRepository(@ApplicationContext appContext: Context): MainRepository {
        return MainRepository(WeakReference(appContext))
    }
}