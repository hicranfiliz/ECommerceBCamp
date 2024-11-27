package com.example.ecommercebcamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.databinding.ItemLayoutProductBinding
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewHolders.FavoriteProductsViewHolder
import com.example.ecommercebcamp.viewHolders.ProductViewHolder

class FavoriteProductsAdapter(
) : RecyclerView.Adapter<FavoriteProductsViewHolder>() {

    lateinit var onProductClick : ((ProductsModelItem) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<ProductsModelItem>() {
        override fun areItemsTheSame(oldItem: ProductsModelItem, newItem: ProductsModelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductsModelItem, newItem: ProductsModelItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductsViewHolder {
        return FavoriteProductsViewHolder(ItemLayoutProductBinding.inflate(LayoutInflater.from(parent.context), parent, false), onProductClick)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteProductsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}