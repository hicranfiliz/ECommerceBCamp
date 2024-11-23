package com.example.ecommercebcamp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.ecommercebcamp.model.ProductsModelItem

@Database(entities = [ProductsModelItem::class], version = 1)
@TypeConverters(com.example.ecommercebcamp.db.TypeConverter::class)
abstract class ProductDatabase: RoomDatabase() {

    abstract val productDao : ProductDao

    companion object{
        @Volatile
        var INSTANCE: ProductDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : ProductDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    ProductDatabase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as ProductDatabase
        }
    }
}