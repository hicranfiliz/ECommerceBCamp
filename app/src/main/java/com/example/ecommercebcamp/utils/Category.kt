package com.example.ecommercebcamp.utils

enum class Category(val value: String) {
    JEWELERY("jewelery"),
    MENS_CLOTHING("men's clothing"),
    ELECTRONICS("electronics"),
    WOMENS_CLOTHING("women's clothing");

    companion object {
        fun fromValue(value: String): Category {
            return values().find { it.value == value }
                ?: throw IllegalArgumentException("Unknown category: $value")
        }
    }
}
