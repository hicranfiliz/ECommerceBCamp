package com.example.ecommercebcamp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "productTable")
data class ProductsModelItem(
    val category: String?,
    val description: String?,
    @PrimaryKey
    val id: Int,
    val image: String?,
    val price: Double?,
    val rating: Rating?,
    val title: String?
): Parcelable