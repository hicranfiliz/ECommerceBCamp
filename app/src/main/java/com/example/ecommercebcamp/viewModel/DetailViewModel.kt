package com.example.ecommercebcamp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebcamp.db.ProductRepository
import com.example.ecommercebcamp.model.ProductsModelItem
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: ProductRepository): ViewModel() {

    fun insertProduct(product : ProductsModelItem){
        viewModelScope.launch {
            repository.insert(product)
        }
    }
}