package com.example.ecommercebcamp.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.adapters.FavoriteProductsAdapter
import com.example.ecommercebcamp.adapters.ProductAdapter
import com.example.ecommercebcamp.adapters.ProductCategoryAdapter
import com.example.ecommercebcamp.databinding.FragmentHomeBinding
import com.example.ecommercebcamp.db.ProductDatabase
import com.example.ecommercebcamp.db.ProductDbRepository
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.retrofit.ProductRepository
import com.example.ecommercebcamp.retrofit.ProductService
import com.example.ecommercebcamp.retrofit.RetrofitInstance
import com.example.ecommercebcamp.utils.Category
import com.example.ecommercebcamp.utils.Gender
import com.example.ecommercebcamp.viewModel.DetailViewModel
import com.example.ecommercebcamp.viewModel.DetailViewModelFactory
import com.example.ecommercebcamp.viewModel.HomeViewModel
import com.example.ecommercebcamp.viewModel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel : HomeViewModel by viewModels {
        val repository = ProductRepository(RetrofitInstance.getRetrofitInstance().create(ProductService::class.java))
        val dao = ProductDatabase.getInstance(requireContext()).productDao
        val favRepository = ProductDbRepository(dao)
        HomeViewModelFactory(repository, favRepository)
    }

    private val productCategoryAdapter by lazy { ProductCategoryAdapter{category ->
        onCategorySelected(category)} }
    private val productAdapter by lazy { ProductAdapter() }

    companion object {
        const val PRODUCT_ID = "com.example.easyfooddemo.fragments.id"
        const val SIMILAR_PRODUCTS = "com.example.easyfooddemo.fragments.similar_products"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setUpSearchAction()
        showCategoryItemsRV()
        showProductItemsRV()
        setUpFavIconCount()

        homeViewModel.fetchCategories()
        homeViewModel.fetchAllProducts()

        observeLoadingState()
        observeProductCategories()
        observeProduct()
        observeFilteredProducts()
        onProductClick()
    }

    private fun setUpFavIconCount() {
        homeViewModel.favoriteCount.observe(viewLifecycleOwner){count ->
            val badgetv = binding.customToolbarr.tvFavBadge

            if (count > 0){
                badgetv.text = count.toString()
                badgetv.visibility = View.VISIBLE
            } else{
                badgetv.visibility = View.GONE
            }
        }
    }

    private fun setUpToolbarInfo() {
        val sharedPrefs = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("userName", "Hicran")
        val userGender = sharedPrefs.getString("userGender", "other")

        val userGenderEnum = try{
            if (userGender != null) {
                Gender.valueOf(userGender)
            } else {
                userGender == ""
            }
        } catch (e: IllegalArgumentException){
            null
        }

        binding.customToolbarr.tvUserName.text = "Hi, $userName"

        val genderIcon = when (userGenderEnum){
            Gender.male -> R.drawable.man_s
            Gender.female -> R.drawable.woman_s
            else -> R.drawable.user_s
        }

        binding.customToolbarr.imgUser.setImageResource(genderIcon)
    }

    private fun setUpToolbarFavAction() {
        binding.customToolbarr.imgFav.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_favoritesFragment)
        }
    }

    private fun setUpSearchAction(){
        binding.customToolbarr.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onProductClick() {
        productAdapter.onProductClick = {product ->
            val bundle = Bundle().apply {
                putParcelable(PRODUCT_ID, product)
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
        homeViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            if (products != null){
                productAdapter.setProducts(products)
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
        if (category.isEmpty()) {
            homeViewModel.fetchAllProducts()
        } else {
            try {
                val selectedCategory = Category.fromValue(category)
                homeViewModel.fetchProductsByCategory(selectedCategory)
            } catch (e: IllegalArgumentException){
                Log.e("","")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}