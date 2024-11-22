package com.example.ecommercebcamp.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.databinding.CategoryItemsBinding
import com.example.ecommercebcamp.model.ProductsModelItem

class ProductCategoryViewHolder(
    val binding: CategoryItemsBinding,
    private val onCategoryClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category : String, isSelected: Boolean){
        binding.tvCategoryName.text = category
        binding.root.setOnClickListener {
            onCategoryClick(category)
        }

        if (isSelected) {
            binding.tvCategoryName.setBackgroundResource(R.drawable.selected_category_background)
            binding.tvCategoryName.setTextColor(binding.root.context.getColor(R.color.white))
        } else {
            binding.tvCategoryName.setBackgroundResource(R.drawable.default_category_background)
            binding.tvCategoryName.setTextColor(binding.root.context.getColor(R.color.black))
        }
    }
}