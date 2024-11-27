package com.example.ecommercebcamp.db

import com.example.ecommercebcamp.model.ProductsModelItem

class ProductRepository(private val dao: ProductDao) {

    val products = dao.getAll()

    suspend fun insert(product: ProductsModelItem){
        return dao.insert(product)
    }

    suspend fun delete(product: ProductsModelItem){
        return dao.delete(product)
    }

    suspend fun isProductFavorite(productId: String): Boolean {
        return dao.isProductFavorite(productId) > 0
    }
}