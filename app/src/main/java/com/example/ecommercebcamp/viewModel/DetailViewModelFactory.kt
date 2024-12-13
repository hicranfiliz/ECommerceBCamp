package com.example.ecommercebcamp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercebcamp.db.ProductDbRepository

class DetailViewModelFactory(private val repository: ProductDbRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}