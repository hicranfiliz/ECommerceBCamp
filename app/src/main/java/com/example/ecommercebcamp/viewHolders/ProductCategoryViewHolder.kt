package com.example.ecommercebcamp.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.databinding.CategoryItemsBinding
import com.example.ecommercebcamp.model.ProductsModelItem

class ProductCategoryViewHolder(val binding: CategoryItemsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product : ProductsModelItem){
        binding.tvCategoryName.text = product.category
    }
}