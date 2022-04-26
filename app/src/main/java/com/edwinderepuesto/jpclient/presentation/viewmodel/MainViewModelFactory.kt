package com.edwinderepuesto.jpclient.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edwinderepuesto.jpclient.data.repository.MainRepository
import java.lang.ref.WeakReference

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(MainRepository(WeakReference(context))) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}