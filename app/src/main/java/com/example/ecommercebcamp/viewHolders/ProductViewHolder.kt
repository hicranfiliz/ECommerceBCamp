package com.example.ecommercebcamp.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommercebcamp.databinding.ItemLayoutProductBinding
import com.example.ecommercebcamp.model.ProductsModelItem

class ProductViewHolder(val binding: ItemLayoutProductBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: ProductsModelItem){
        Glide.with(binding.root.context)
            .load(product.image)
            .into(binding.imgProduct)
        binding.tvProductName.text = product.title
        binding.tvProductCost.text = "$ " + product.price.toString()
    }
}