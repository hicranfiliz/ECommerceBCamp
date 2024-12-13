package com.example.ecommercebcamp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.adapters.ProductAdapter
import com.example.ecommercebcamp.adapters.ProductCategoryAdapter
import com.example.ecommercebcamp.databinding.FragmentSearchBinding
import com.example.ecommercebcamp.db.ProductDatabase
import com.example.ecommercebcamp.db.ProductDbRepository
import com.example.ecommercebcamp.fragments.HomeFragment.Companion.PRODUCT_ID
import com.example.ecommercebcamp.retrofit.ProductRepository
import com.example.ecommercebcamp.retrofit.ProductService
import com.example.ecommercebcamp.retrofit.RetrofitInstance
import com.example.ecommercebcamp.viewModel.HomeViewModel
import com.example.ecommercebcamp.viewModel.HomeViewModelFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ProductRepository(RetrofitInstance.getRetrofitInstance().create(ProductService::class.java))
        val dao = ProductDatabase.getInstance(requireContext()).productDao
        val favRepository = ProductDbRepository(dao)
        val factory = HomeViewModelFactory(repository, favRepository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        productAdapter = ProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter.onProductClick = {product ->

            val bundle = Bundle().apply {
                putParcelable(PRODUCT_ID, product)
            }
            findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
        }

        setUpBackButton()
        setUpRecyclerView()

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    homeViewModel.searchProducts(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    homeViewModel.searchProducts(it)
                }
                return true
            }
        })

        observeFilteredProducts()
    }

    private fun setUpBackButton() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpRecyclerView() {
        binding.rvSearchResults.apply {
            //layoutManager = LinearLayoutManager(requireContext())
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun observeFilteredProducts() {
        homeViewModel.filteredProducts.observe(viewLifecycleOwner) { products ->
            productAdapter.setProducts(products)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
