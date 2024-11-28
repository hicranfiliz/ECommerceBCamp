package com.example.ecommercebcamp.retrofit

import com.example.ecommercebcamp.model.ProductsModelItem
class ProductRepository(private val productService: ProductService) {

    suspend fun getCategories(): List<String> {
        val response = productService.getCategories()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch categories: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getAllProducts(): List<ProductsModelItem> {
        val response = productService.getAllProducts()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch products: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getProductsByCategory(category: String): List<ProductsModelItem> {
        val response = when (category) {
            "jewelery" -> productService.getJeweleries()
            "men's clothing" -> productService.getMensClothings()
            "electronics" -> productService.getElectronics()
            "women's clothing" -> productService.getWomensClothings()
            else -> throw IllegalArgumentException("Unknown category: $category")
        }
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch products by category: ${response.errorBody()?.string()}")
        }
    }
}


