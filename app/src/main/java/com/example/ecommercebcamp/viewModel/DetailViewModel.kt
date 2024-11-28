package com.example.ecommercebcamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebcamp.db.ProductRepository
import com.example.ecommercebcamp.model.ProductsModelItem
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: ProductRepository): ViewModel() {

    val favorites = repository.products

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun checkFavorite(productId: String) {
        viewModelScope.launch {
            _isFavorite.value = repository.isProductFavorite(productId)
        }
    }

    fun toggleFavorite(product: ProductsModelItem) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                repository.delete(product)
                _isFavorite.value = false
            } else {
                repository.insert(product)
                _isFavorite.value = true
            }
        }
    }

    fun insertProduct(product : ProductsModelItem){
        viewModelScope.launch {
            repository.insert(product)
        }
    }

    fun deleteProduct(product: ProductsModelItem){
        viewModelScope.launch {
            repository.delete(product)
        }
    }
}