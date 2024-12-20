package com.example.ecommercebcamp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.ecommercebcamp.db.ProductDbRepository
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.retrofit.ProductRepository
import com.example.ecommercebcamp.utils.Category
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val favRepository: ProductDbRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> get() = _categories

    private val _allProducts = MutableLiveData<List<ProductsModelItem>>()
    val allProducts: LiveData<List<ProductsModelItem>> get() = _allProducts

    private val _filteredProducts = MutableLiveData<List<ProductsModelItem>>()
    val filteredProducts: LiveData<List<ProductsModelItem>> get() = _filteredProducts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    val favorites = favRepository.products

    private val _favoriteCount = MediatorLiveData<Int>()
    val favoriteCount: LiveData<Int> get() = _favoriteCount

    init {
        fetchCategories()
        fetchAllProducts()

        _favoriteCount.addSource(favorites) { favoriteList ->
            _favoriteCount.value = favoriteList.size
        }
    }


    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRepository.getCategories()
                _categories.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching categories: ${e.message}")
                _errorMessage.value = "Failed to fetch categories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchAllProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRepository.getAllProducts()
                _allProducts.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching products: ${e.message}")
                _errorMessage.value = "Failed to fetch products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProductsByCategory(category: Category) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRepository.getProductsByCategory(category)
                _filteredProducts.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching products by category: ${e.message}")
                _errorMessage.value = "Failed to fetch products by category: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchProducts(query: String) {
        val filteredList = _allProducts.value?.filter {
            it.title?.contains(query, ignoreCase = true) == true ||
                    it.category?.contains(query, ignoreCase = true) == true
        } ?: emptyList()
        _filteredProducts.value = filteredList
    }
}