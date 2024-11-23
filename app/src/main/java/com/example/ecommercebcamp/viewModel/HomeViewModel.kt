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

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = productService.getProducts()
                if (response.isSuccessful) {
                    val productList = response.body() ?: emptyList()
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
            }
        }
    }
}