package com.example.testapp.ui.home

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.testapp.App
import com.example.testapp.R
import com.example.testapp.databinding.FragmentPage2Binding
import com.example.testapp.ui.home.apadters.ColorsAdapter
import com.example.testapp.ui.home.apadters.PreviewsAdapter
import com.example.testapp.ui.home.data.ColorThumbnail
import com.example.testapp.ui.home.data.Preview

class Page2Fragment : Fragment() {

    private var _binding: FragmentPage2Binding? = null
    private val binding get() = _binding!!
    private val vm: TradeItemsViewModel by viewModels {
        TradeItemsViewModelFactory((activity?.application as App).repository)
    }

    private var count = 0

    private val previewsAdapter = PreviewsAdapter(
        listOf(),
        onClick = ::onPreviewClick
    )

    private val colorsAdapter = ColorsAdapter(
        listOf(),
        onClick = ::onColorClick
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPage2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getItemDescription()

        binding.apply {
            photoThumbnails.adapter = previewsAdapter
            colors.adapter = colorsAdapter
        }

        vm.itemDescription.observe(viewLifecycleOwner) { item ->

            val imgUri = item.imageUrls[0].toUri().buildUpon().scheme("https").build()
            val priceString = "$ ${item.price}"
            val reviewString = "(${item.reviews} reviews)"

            binding.apply {
                mainImage.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
                descriptionTitle.text = item.name
                price.text = priceString
                descriptionText.text = item.description
                rating.text = item.rating.toString()
                reviews.text = reviewString

                buttonAdd.setOnClickListener {
                    count++
                    val newPrice = "#${count * item.price}"
                    textTotalPrice.text = newPrice
                }
                buttonRemove.setOnClickListener {
                    if (count > 0) count--
                    val newPrice = "#${count * item.price}"
                    textTotalPrice.text = newPrice
                }
            }
            previewsAdapter.submitList(getPreviews(item.imageUrls))
            colorsAdapter.submitList(getColorsList(item.colors))
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getColorsList(colors: List<String>): List<ColorThumbnail> {
        val result = mutableListOf<ColorThumbnail>()
        for (item in colors) {
            result.add(ColorThumbnail(colorString = item))
        }
        return result.toList()
    }

    private fun getPreviews(previews: List<String>): List<Preview> {
        val result = mutableListOf<Preview>()
        for (i in 0..previews.lastIndex) {
            if (i == 0)
                result.add(Preview( true, previews[i]))
            else
                result.add(Preview(uriString = previews[i]))
        }
        return result.toList()
    }

    private fun onColorClick(position: Int) {

        val newList = mutableListOf<ColorThumbnail>()
        for (i in 0..colorsAdapter.currentList.lastIndex) {
            val item = colorsAdapter.currentList[i]
            newList.add(
                if (i == position)
                    ColorThumbnail(true, item.colorString)
                else
                    ColorThumbnail(colorString = item.colorString)
            )
        }
        colorsAdapter.submitList(newList.toList())
    }

    private fun onPreviewClick(uri: Uri, position: Int) {


        binding.mainImage.load(uri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }

        val newList = mutableListOf<Preview>()
        for (i in 0..previewsAdapter.currentList.lastIndex) {
            val item = previewsAdapter.currentList[i]
            newList.add(
                if (i == position) Preview(
                    true,
                    item.uriString
                ) else Preview(uriString = item.uriString)
            )
        }
        previewsAdapter.submitList(newList.toList())
    }
}