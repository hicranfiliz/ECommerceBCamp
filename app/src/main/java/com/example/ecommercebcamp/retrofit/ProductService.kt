package com.example.ecommercebcamp.retrofit

import com.example.ecommercebcamp.model.ProductsModel
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("products")
    suspend fun getProducts(): Response<ProductsModel>
}