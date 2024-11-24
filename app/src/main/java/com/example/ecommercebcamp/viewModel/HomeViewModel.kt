package com.example.ecommercebcamp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.retrofit.ProductService
import com.example.ecommercebcamp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    private val productService = RetrofitInstance.getRetrofitInstance().create(ProductService::class.java)

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> get() = _categories

    private val _productsByCategory = MutableLiveData<Map<String, List<ProductsModelItem>>>()
    val productsByCategory: LiveData<Map<String, List<ProductsModelItem>>> get() = _productsByCategory

    private val _allProducts = MutableLiveData<List<ProductsModelItem>>()
    val allProducts: LiveData<List<ProductsModelItem>> get() = _allProducts

    private val _filteredProducts = MutableLiveData<List<ProductsModelItem>>()
    val filteredProducts: LiveData<List<ProductsModelItem>> get() = _filteredProducts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productService.getProducts()
                if (response.isSuccessful) {
                    val productList = response.body() ?: emptyList()
                    _allProducts.value = productList
                    _categories.value = productList.map {
                        it.category
                    }.
                    filterNotNull().distinct()
                    _productsByCategory.value = productList.groupBy { it.category ?: "" }
                } else {
                    Log.e("HomeViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
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