package com.example.testapp.ui.home.apadters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testapp.R
import com.example.testapp.databinding.LatestListitemBinding
import com.example.testapp.network.TradeItem

class LatestAdapter(
    private val tradeItems: List<TradeItem>,
    private val onClick: (String) -> Unit,
    private val addToCart: (String) -> Unit
) : ListAdapter<TradeItem, LatestAdapter.LatestViewHolder>(DiffCallback) {

    inner class LatestViewHolder(private var binding: LatestListitemBinding) :
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

            binding.apply {
                category.text = item.category
                name.text = item.name
                price.text = priceString
                buttonAdd.setOnClickListener { addToCart(item.name) }
            }

            itemView.setOnClickListener { onClick(item.name) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestViewHolder {
        val binding = LatestListitemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return LatestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LatestViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TradeItem>() {
        override fun areItemsTheSame(oldItem: TradeItem, newItem: TradeItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: TradeItem, newItem: TradeItem): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }
    }


}