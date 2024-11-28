package com.example.ecommercebcamp.retrofit

import com.example.ecommercebcamp.model.Categories
import com.example.ecommercebcamp.model.ProductsModel
import com.example.ecommercebcamp.model.ProductsModelItem
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("2d9740dd-22e4-4a55-8a53-fa82e47de25d")
    suspend fun getAllProducts(): Response<ProductsModel>

    @GET("7eca17fa-2226-4eb2-9adf-61f5f37f2748")
    suspend fun getCategories(): Response<Categories>

    @GET("240c8d90-473a-4a25-ac7a-5ebc86bcc3ce")
    suspend fun getJeweleries(): Response<ProductsModel>

    @GET("cb58de83-e4a5-43e8-b154-817781613d1e")
    suspend fun getMensClothings(): Response<ProductsModel>

    @GET("feb38300-2763-41ce-8825-f58831fa0416")
    suspend fun getElectronics(): Response<ProductsModel>

    @GET("0232cb2d-b809-4944-8d19-211169992102")
    suspend fun getWomensClothings(): Response<ProductsModel>

}