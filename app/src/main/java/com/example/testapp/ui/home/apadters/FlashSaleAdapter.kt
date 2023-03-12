package com.example.testapp.ui.home.apadters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testapp.R
import com.example.testapp.databinding.FlashListitemBinding
import com.example.testapp.network.TradeItem

class FlashSaleAdapter(
    private val tradeItems: List<TradeItem>,
    private val onClick: (String) -> Unit,
    private val addToFavourite: (String) -> Unit,
    private val addToCart: (String) -> Unit
) :
    ListAdapter<TradeItem, FlashSaleAdapter.FlashViewHolder>(DiffCallback) {

    inner class FlashViewHolder(private var binding: FlashListitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TradeItem) {
            item.imgSrcUrl?.let {
                val imgUri = it.toUri().buildUpon().scheme("https").build()
                binding.previewImage.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }

            val priceString = "\$ ${item.price}"
            val discountString = "${item.discount}% off"
            binding.apply {
                category.text = item.category
                name.text = item.name
                price.text = priceString
                discount.text = discountString
                buttonAdd.setOnClickListener { addToCart(item.name) }
                buttonAddToFavourite.setOnClickListener { addToFavourite(item.name) }
            }

            itemView.setOnClickListener { onClick(item.name) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashViewHolder {
        val binding = FlashListitemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    override fun getItemCount() = currentList.size

    companion object DiffCallback : DiffUtil.ItemCallback<TradeItem>() {
        override fun areItemsTheSame(oldItem: TradeItem, newItem: TradeItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: TradeItem, newItem: TradeItem): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }
    }


}