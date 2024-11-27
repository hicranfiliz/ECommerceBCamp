package com.example.ecommercebcamp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommercebcamp.model.ProductsModelItem

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product : ProductsModelItem)

    @Delete
    suspend fun delete(product: ProductsModelItem)

    @Query("SELECT * FROM productTable")
    fun getAll(): LiveData<List<ProductsModelItem>>

    @Query("SELECT COUNT(*) FROM productTable WHERE id = :productId")
    suspend fun isProductFavorite(productId: String): Int
}