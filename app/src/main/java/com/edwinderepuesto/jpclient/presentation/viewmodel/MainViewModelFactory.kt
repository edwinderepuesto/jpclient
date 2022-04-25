package com.edwinderepuesto.jpclient.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edwinderepuesto.jpclient.data.repository.MainRepository

class MainViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(MainRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}