package com.example.ecommercebcamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.databinding.CategoryItemsBinding
import com.example.ecommercebcamp.model.ProductsModel
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewHolders.ProductViewHolder

class ProductAdapter() : RecyclerView.Adapter<ProductViewHolder>() {

    private var productList = ArrayList<ProductsModelItem>()
    fun setProducts(productList : List<ProductsModelItem?>){
        this.productList = productList as ArrayList<ProductsModelItem>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(CategoryItemsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }
}