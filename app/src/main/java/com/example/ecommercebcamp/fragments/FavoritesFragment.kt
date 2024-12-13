package com.example.ecommercebcamp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.adapters.FavoriteProductsAdapter
import com.example.ecommercebcamp.databinding.FragmentFavoritesBinding
import com.example.ecommercebcamp.db.ProductDatabase
import com.example.ecommercebcamp.db.ProductDbRepository
import com.example.ecommercebcamp.fragments.HomeFragment.Companion.PRODUCT_ID
import com.example.ecommercebcamp.viewModel.DetailViewModel
import com.example.ecommercebcamp.viewModel.DetailViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailViewModel
    private lateinit var favoriteAdapter: FavoriteProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = ProductDatabase.getInstance(requireContext()).productDao
        val repository = ProductDbRepository(dao)
        val factory = DetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        favoriteAdapter = FavoriteProductsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        bindRecyclerView()
        observeFavorites()

        swipeToDelete()
        onFavProductClick()
    }

    private fun onFavProductClick() {
        favoriteAdapter.onProductClick = { product ->
            val bundle = Bundle().apply {
                putParcelable(PRODUCT_ID, product)
            }
            findNavController().navigate(R.id.action_favoritesFragment_to_detailFragment, bundle)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun swipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedProduct = favoriteAdapter.differ.currentList[position]
                viewModel.deleteProduct(swipedProduct)

                Snackbar.make(requireView(), "Product deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertProduct(swipedProduct)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }

    private fun bindRecyclerView() {
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.favorites.observe(viewLifecycleOwner, Observer { fav ->
            if (fav.isNullOrEmpty()) {
                binding.tvEmptyFavorites.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvEmptyFavorites.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
                favoriteAdapter.differ.submitList(fav)
            }
        })
    }
}