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

    private val _products = MutableLiveData<List<ProductsModelItem?>>()
    val products: LiveData<List<ProductsModelItem?>> get() = _products

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = productService.getProducts()
                if (response.isSuccessful) {
                    _products.value = response.body()
                } else {
                    Log.e("HomeViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
            }
        }
    }
}