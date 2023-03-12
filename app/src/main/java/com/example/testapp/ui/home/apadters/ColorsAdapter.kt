package com.example.testapp.ui.home.apadters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.databinding.ColorThumbnailBinding
import com.example.testapp.ui.home.data.ColorThumbnail

class ColorsAdapter(
    private val colors: List<ColorThumbnail>,
    private val onClick: (Int) -> Unit
) : ListAdapter<ColorThumbnail, ColorsAdapter.ColorViewHolder>(DiffCallback) {

    inner class ColorViewHolder(private var binding: ColorThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ColorThumbnail, position: Int) {

            binding.colorThumbnail.setCardBackgroundColor(item.colorString.toColorInt())

            val scale = itemView.context.resources.displayMetrics.density
            val strokeWidth = (2 * scale + 0.5f).toInt()
            if (item.isChecked)
                binding.colorThumbnail.strokeWidth = strokeWidth
            else
                binding.colorThumbnail.strokeWidth = 0

            itemView.setOnClickListener {
                onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ColorThumbnailBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ColorThumbnail>() {
        override fun areItemsTheSame(oldItem: ColorThumbnail, newItem: ColorThumbnail): Boolean {
            return oldItem.colorString == newItem.colorString
        }

        override fun areContentsTheSame(oldItem: ColorThumbnail, newItem: ColorThumbnail): Boolean {
            return oldItem == newItem
        }
    }
}