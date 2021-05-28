package com.decagon.android.sq007.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.decagon.android.sq007.repository.PostRepository

// this viewmodel factory class help to instantiate viewmodel class with constructor parameters
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostViewModel(repository) as T
    }
}
