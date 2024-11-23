package com.example.ecommercebcamp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.databinding.FragmentDetailBinding
import com.example.ecommercebcamp.model.ProductsModelItem

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = arguments?.getParcelable<ProductsModelItem>(HomeFragment.PRODUCT_ID)

        product?.let {
            bindProductDetails(it)
        }

        onBackImgClick()
    }

    private fun bindProductDetails(product: ProductsModelItem) {
        Glide.with(this)
            .load(product.image)
            .into(binding.imgProduct)

        binding.tvInstructions.text = product.title
        binding.tvRatingText.text = product.rating.rate.toString()
        binding.tvReviewCount.text = product.rating.count.toString()

        binding.tvCategoryText.text = "Category: ${product.category}"

        binding.tvDescriptionText.text = product.description
        binding.priceText.text = "$${product.price}"

        binding.ratingBar.rating = product.rating.rate.toFloat()
    }

    private fun onBackImgClick(){
        binding.customDetailToolbar.imgArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}