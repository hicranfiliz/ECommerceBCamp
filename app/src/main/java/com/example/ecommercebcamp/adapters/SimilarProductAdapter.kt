package com.example.ecommercebcamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.databinding.ItemLayoutProductBinding
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewHolders.SimilarProductViewHolder

class SimilarProductAdapter() : RecyclerView.Adapter<SimilarProductViewHolder>() {

    lateinit var onProductClick : ((ProductsModelItem) -> Unit)

    private var similarityList = mutableListOf<ProductsModelItem>()

    fun setProducts(productList: List<ProductsModelItem>) {
        this.similarityList.clear()
        this.similarityList.addAll(productList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarProductViewHolder {
        return SimilarProductViewHolder(
            ItemLayoutProductBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick
        )
    }

    override fun getItemCount(): Int {
        return similarityList.size
    }

    override fun onBindViewHolder(holder: SimilarProductViewHolder, position: Int) {
        holder.bind(similarityList[position])
    }
}