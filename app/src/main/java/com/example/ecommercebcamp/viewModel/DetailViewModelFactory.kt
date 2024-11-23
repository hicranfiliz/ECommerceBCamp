package com.example.ecommercebcamp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercebcamp.db.ProductRepository

class DetailViewModelFactory(private val repository: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}