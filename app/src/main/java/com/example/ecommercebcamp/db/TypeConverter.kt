package com.example.ecommercebcamp.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.ecommercebcamp.model.Rating

class TypeConverter {

    @TypeConverter
    fun fromRating(rating: Rating?): String? {
        return Gson().toJson(rating)
    }

    @TypeConverter
    fun toRating(ratingString: String?): Rating? {
        val type = object : TypeToken<Rating>() {}.type
        return Gson().fromJson(ratingString, type)
    }
}