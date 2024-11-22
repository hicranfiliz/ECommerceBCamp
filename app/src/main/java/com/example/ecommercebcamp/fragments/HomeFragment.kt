package com.example.ecommercebcamp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercebcamp.adapters.ProductAdapter
import com.example.ecommercebcamp.adapters.ProductCategoryAdapter
import com.example.ecommercebcamp.databinding.FragmentHomeBinding
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var productCategoryAdapter: ProductCategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        productCategoryAdapter = ProductCategoryAdapter{ category ->
            onCategorySelected(category)
        }
        productAdapter = ProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showCategoryItemsRV()
        showProductItemsRV()

        homeViewModel.fetchProducts()

        observeProductCategories()
        observeProduct()
    }

    private fun showCategoryItemsRV() {
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = productCategoryAdapter
        }
    }

    private fun showProductItemsRV() {
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
    }

    private fun observeProductCategories() {
        homeViewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            if (categories != null) {
                productCategoryAdapter.setCategories(categories)
            }
        })
    }

    private fun observeProduct() {
        homeViewModel.productsByCategory.observe(viewLifecycleOwner) { groupedProducts ->
            val firstCategory = groupedProducts.keys.firstOrNull()
            firstCategory?.let {
                productAdapter.setProducts(groupedProducts[it] ?: emptyList())
            }
        }
    }

    private fun onCategorySelected(category: String) {
        homeViewModel.productsByCategory.value?.let { groupedProducts ->
            productAdapter.setProducts(groupedProducts[category] ?: emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}