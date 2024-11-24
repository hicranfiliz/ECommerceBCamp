package com.example.ecommercebcamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.databinding.CategoryItemsBinding
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewHolders.ProductCategoryViewHolder

class ProductCategoryAdapter(
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<ProductCategoryViewHolder>() {

    private val categories = mutableListOf<String>()
    private var selectedPosition: Int = -1

    fun setCategories(categories : List<String>){
        this.categories.clear()
        this.categories.addAll(categories)
        selectedPosition = if (categories.isNotEmpty()) 0 else -1
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryViewHolder {
        val binding = CategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductCategoryViewHolder(binding, onCategoryClick)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ProductCategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, position == selectedPosition)

        holder.binding.root.setOnClickListener {
            if (selectedPosition == position) return@setOnClickListener

            val previousPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onCategoryClick(category)
        }
    }
}