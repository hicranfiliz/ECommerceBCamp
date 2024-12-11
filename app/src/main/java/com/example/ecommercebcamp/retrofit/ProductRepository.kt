package com.example.ecommercebcamp.retrofit

import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.utils.Category

class ProductRepository(private val productService: ProductService) : BaseRepository() {

    suspend fun getCategories(): List<String> {
        return apiCall { productService.getCategories() }
    }

    suspend fun getAllProducts(): List<ProductsModelItem> {
        return apiCall { productService.getAllProducts() }
    }

    suspend fun getProductsByCategory(category: Category): List<ProductsModelItem> {
        return when (category) {
            Category.JEWELERY -> apiCall { productService.getJeweleries() }
            Category.MENS_CLOTHING -> apiCall { productService.getMensClothings() }
            Category.ELECTRONICS -> apiCall { productService.getElectronics() }
            Category.WOMENS_CLOTHING -> apiCall { productService.getWomensClothings() }
        }
    }
}


