package com.example.ecommercebcamp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercebcamp.db.ProductDbRepository

class HomeViewModelFactory(
    private val repository: com.example.ecommercebcamp.retrofit.ProductRepository,
    private val favRepository: ProductDbRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, favRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}
