package com.example.ecommercebcamp.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercebcamp.R
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

    companion object {
        const val PRODUCT_ID = "com.example.easyfooddemo.fragments.id"
        const val SIMILAR_PRODUCTS = "com.example.easyfooddemo.fragments.similar_products"
    }

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

        setUpToolbarInfo()
        setUpToolbarFavAction()
        setUpToolbarSearchAction()
        showCategoryItemsRV()
        showProductItemsRV()

        homeViewModel.fetchProducts()

        observeLoadingState()
        observeProductCategories()
        observeProduct()
        observeFilteredProducts()
        onProductClick()
    }

    private fun setUpToolbarInfo() {
        val sharedPrefs = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("userName", "Hicran")
        val userGender = sharedPrefs.getString("userGender", "other")

        binding.customToolbarr.tvUserName.text = "Hi, $userName"

        val genderIcon = when (userGender){
            "male" -> R.drawable.man_s
            "female" -> R.drawable.woman_s
            else -> R.drawable.user_s
        }

        binding.customToolbarr.imgUser.setImageResource(genderIcon)
    }

    private fun setUpToolbarFavAction() {
        binding.customToolbarr.imgFav.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
        }
    }

    private fun setUpToolbarSearchAction() {
        binding.customToolbarr.imgSearch.setOnClickListener {
            showSearchDialog()
        }
    }

    private fun showSearchDialog() {
        val searchDialog = AlertDialog.Builder(requireContext())
        val searchInput = EditText(requireContext()).apply {
            hint = "Search for a product..."
        }
        searchDialog.setTitle("Search")
            .setView(searchInput)
            .setPositiveButton("Search") { _, _ ->
                val query = searchInput.text.toString()
                searchProducts(query)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun searchProducts(query: String) {
        homeViewModel.searchProducts(query)
    }

    private fun onProductClick() {
        productAdapter.onProductClick = {product ->
            val allProductsByCategory = homeViewModel.productsByCategory.value ?: emptyMap()
            val similarProducts = allProductsByCategory[product.category]?.take(4)

            val bundle = Bundle().apply {
                putParcelable(PRODUCT_ID, product)
                putParcelableArrayList(
                    SIMILAR_PRODUCTS,
                    ArrayList(similarProducts ?: emptyList())
                )
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
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

    private fun observeFilteredProducts() {
        homeViewModel.filteredProducts.observe(viewLifecycleOwner) { filteredProducts ->
            productAdapter.setProducts(filteredProducts)
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

    private fun observeLoadingState() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvCategories.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.rvProducts.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.tvCategories.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.tvProductss.visibility = if (isLoading) View.GONE else View.VISIBLE
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