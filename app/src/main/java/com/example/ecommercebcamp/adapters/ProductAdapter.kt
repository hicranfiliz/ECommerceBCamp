package com.example.ecommercebcamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.databinding.ItemLayoutProductBinding
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewHolders.ProductViewHolder

class ProductAdapter() : RecyclerView.Adapter<ProductViewHolder>() {
    lateinit var onProductClick: ((ProductsModelItem) -> Unit)

    private var productList = mutableListOf<ProductsModelItem>()

    fun setProducts(productList : List<ProductsModelItem>){
        this.productList.clear()
        this.productList.addAll(productList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemLayoutProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, onProductClick)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }
}