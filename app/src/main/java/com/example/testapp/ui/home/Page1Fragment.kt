package com.example.testapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.testapp.App
import com.example.testapp.databinding.FragmentPage1Binding
import com.example.testapp.network.TradeItem
import com.example.testapp.ui.home.apadters.CategoriesAdapter
import com.example.testapp.ui.home.apadters.FlashSaleAdapter
import com.example.testapp.ui.home.apadters.LatestAdapter


class Page1Fragment : Fragment() {
    private var _binding: FragmentPage1Binding? = null
    private val binding get() = _binding!!
    private val vm: TradeItemsViewModel by viewModels {
        TradeItemsViewModelFactory((activity?.application as App).repository)
    }

    private var latestList = listOf<TradeItem>()
    private var flashSaleList = listOf<TradeItem>()
    private var brandsList = listOf<TradeItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPage1Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getCategories()
        vm.getTradeItems()

        val latestAdapter = LatestAdapter(
            listOf(),
            onClick = ::onClick,
            addToCart = ::addToCart
        )

        val flashSaleAdapter = FlashSaleAdapter(
            listOf(),
            onClick = ::onClick,
            addToCart = ::addToCart,
            addToFavourite = ::addToFavourite
        )

        val brandsAdapter = LatestAdapter(
            listOf(),
            onClick = ::onClick,
            addToCart = ::addToCart
        )

        val categoriesAdapter = CategoriesAdapter(
            listOf()
        )

        vm.categories.observe(viewLifecycleOwner) {
            categoriesAdapter.submitList(it)
        }

        vm.latest.observe(viewLifecycleOwner) {
            latestList = it
        }

        vm.flashSale.observe(viewLifecycleOwner) {
            flashSaleList = it
        }

        vm.brands.observe(viewLifecycleOwner) {
            brandsList = it
        }

        vm.readyToShow.observe(viewLifecycleOwner) {
            if (it) {
                latestAdapter.submitList(latestList)
                flashSaleAdapter.submitList(flashSaleList)
                brandsAdapter.submitList(brandsList)
            }
            binding.swipeRefresh.isRefreshing = false
        }

        binding.apply {
            swipeRefresh.setOnRefreshListener { vm.getTradeItems() }
            latest.adapter = latestAdapter
            flash.adapter = flashSaleAdapter
            brands.adapter = brandsAdapter
            categories.adapter = categoriesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick(name: String) {
        Toast.makeText(context, "$name clicked", Toast.LENGTH_SHORT).show()
        findNavController().navigate(Page1FragmentDirections.actionPage1FragmentToPage2Fragment())
    }

    private fun addToCart(name: String) {
        Toast.makeText(context, "$name added to cart", Toast.LENGTH_SHORT).show()
    }

    private fun addToFavourite(name: String) {
        Toast.makeText(context, "$name added to favourite", Toast.LENGTH_SHORT).show()
    }
}