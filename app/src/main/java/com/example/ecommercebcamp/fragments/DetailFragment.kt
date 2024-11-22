package com.example.ecommercebcamp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.adapters.SimilarProductAdapter
import com.example.ecommercebcamp.databinding.FragmentDetailBinding
import com.example.ecommercebcamp.db.ProductDatabase
import com.example.ecommercebcamp.db.ProductRepository
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.viewModel.DetailViewModel
import com.example.ecommercebcamp.viewModel.DetailViewModelFactory
import com.example.ecommercebcamp.viewModel.HomeViewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var similarProductAdapter: SimilarProductAdapter

    private var product: ProductsModelItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = ProductDatabase.getInstance(requireContext()).productDao
        val repository = ProductRepository(dao)
        val factory = DetailViewModelFactory(repository)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        similarProductAdapter = SimilarProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarActions()

        product = arguments?.getParcelable<ProductsModelItem>(HomeFragment.PRODUCT_ID)
        val similarProducts = arguments?.getParcelableArrayList<ProductsModelItem>(HomeFragment.SIMILAR_PRODUCTS)

        similarProducts?.let {
            similarProductAdapter.setProducts(it)
        }

        product?.let {
            bindProductDetails(it)

        }

        onBackImgClick()
        onFavoriteClick()

        bindRecyclerView()
        observeSimilarProducts()
    }

    private fun setupToolbarActions() {
        binding.customDetailToolbar.imgArrow.setOnClickListener {
            Log.d("DetailFragment", "Back arrow clicked")
            findNavController().navigateUp()
        }
    }

    private fun bindProductDetails(product: ProductsModelItem) {
        Glide.with(this)
            .load(product.image)
            .into(binding.imgProduct)

        binding.tvInstructions.text = product.title
        binding.tvRatingText.text = product.rating?.rate.toString()
        binding.tvReviewCount.text = product.rating?.count.toString()

        binding.tvCategoryText.text = "Category: ${product.category}"

        binding.tvDescriptionText.text = product.description
        binding.priceText.text = "$${product.price}"

        binding.ratingBar.rating = product.rating?.rate?.toFloat()!!
    }

    private fun onBackImgClick(){
        binding.customDetailToolbar.imgArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onFavoriteClick(){
        binding.btnAddToFav.setOnClickListener {
            product?.let {
                detailViewModel.insertProduct(it)
                Toast.makeText(requireContext(), "Product saved to favorites", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun bindRecyclerView() {
        binding.rvSimilarProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL,false)
            adapter = similarProductAdapter
        }
    }

    private fun observeSimilarProducts() {
        detailViewModel.similarProducts.observe(viewLifecycleOwner) { similarProducts ->
            similarProductAdapter.setProducts(similarProducts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}