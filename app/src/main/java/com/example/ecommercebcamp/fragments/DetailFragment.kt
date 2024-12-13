package com.example.ecommercebcamp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.adapters.SimilarProductAdapter
import com.example.ecommercebcamp.databinding.FragmentDetailBinding
import com.example.ecommercebcamp.db.ProductDatabase
import com.example.ecommercebcamp.db.ProductDbRepository
import com.example.ecommercebcamp.fragments.HomeFragment.Companion.PRODUCT_ID
import com.example.ecommercebcamp.model.ProductsModelItem
import com.example.ecommercebcamp.retrofit.ProductService
import com.example.ecommercebcamp.retrofit.RetrofitInstance
import com.example.ecommercebcamp.utils.Category
import com.example.ecommercebcamp.viewModel.DetailViewModel
import com.example.ecommercebcamp.viewModel.DetailViewModelFactory
import com.example.ecommercebcamp.viewModel.HomeViewModel
import com.example.ecommercebcamp.viewModel.HomeViewModelFactory

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var similarProductAdapter: SimilarProductAdapter

    private lateinit var homeViewModel: HomeViewModel

    private var product: ProductsModelItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = ProductDatabase.getInstance(requireContext()).productDao
        val repository = ProductDbRepository(dao)
        val factory = DetailViewModelFactory(repository)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val serviceRepository = com.example.ecommercebcamp.retrofit.ProductRepository(
            RetrofitInstance.getRetrofitInstance().create(ProductService::class.java)
        )
        val homeFactory = HomeViewModelFactory(serviceRepository, repository)
        homeViewModel = ViewModelProvider(this, homeFactory)[HomeViewModel::class.java]
        similarProductAdapter = SimilarProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        //_binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = arguments?.getParcelable<ProductsModelItem>(PRODUCT_ID)
        detailViewModel.checkFavorite(product?.id.toString())

        product?.let { selectedProduct ->
            bindProductDetails(selectedProduct)

            selectedProduct.category?.let { categoryStr ->
                try {
                    val category = Category.fromValue(categoryStr)
                    homeViewModel.fetchProductsByCategory(category)
                }catch (e: IllegalArgumentException){
                    Log.e("", "")
                }

            }
        }

        setFavButton()

        onBackImgClick()
        onFavoriteClick()

        onSimilarProductClick()

        bindRecyclerView()
        observeSimilarProducts()
    }

    private fun setFavButton() {
        detailViewModel.isFavorite.observe(viewLifecycleOwner){ isFav ->
            val fab = binding.btnAddToFav
            if (isFav){
                fab.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.accent))
                fab.setImageResource(R.drawable.heart)
            } else{
                fab.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white))
                fab.setImageResource(R.drawable.heart)
            }
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
        binding.toolbar.imgArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onFavoriteClick(){
        binding.btnAddToFav.setOnClickListener {
            product?.let {
                detailViewModel.toggleFavorite(it)
                //detailViewModel.insertProduct(it)
                //Toast.makeText(requireContext(), "Product saved to favorites", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onSimilarProductClick() {
        similarProductAdapter.onProductClick = {product ->

            val bundle = Bundle().apply {
                putParcelable(PRODUCT_ID, product)
            }
            findNavController().navigate(R.id.action_detailFragment_self, bundle)
        }
    }

    private fun bindRecyclerView() {
        binding.rvSimilarProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL,false)
            adapter = similarProductAdapter
        }
    }

    private fun observeSimilarProducts() {
        homeViewModel.filteredProducts.observe(viewLifecycleOwner) { similarProducts ->
            val sProducts = similarProducts.take(4)
            similarProductAdapter.setProducts(sProducts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}