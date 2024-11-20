package com.example.ecommercebcamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.databinding.CategoryItemsBinding
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewHolders.ProductCategoryViewHolder

class ProductCategoryAdapter() : RecyclerView.Adapter<ProductCategoryViewHolder>() {

    private var productList = ArrayList<ProductsModelItem>()
    fun setProducts(productList : List<ProductsModelItem?>){
        this.productList = productList as ArrayList<ProductsModelItem>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryViewHolder {
        return ProductCategoryViewHolder(CategoryItemsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductCategoryViewHolder, position: Int) {
        holder.bind(productList[position])
    }
}