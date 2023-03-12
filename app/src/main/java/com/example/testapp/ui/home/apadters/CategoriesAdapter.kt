package com.example.testapp.ui.home.apadters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.databinding.CategoriesItemBinding
import com.example.testapp.ui.home.data.Category

class CategoriesAdapter(private val categories: List<Category>) :
    ListAdapter<Category, CategoriesAdapter.CategoriesViewHolder>(DiffCallback) {

    inner class CategoriesViewHolder(private var binding: CategoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.description.text = itemView.context.getString(category.description)
            binding.icon.setImageResource(category.icon)
            binding.icon.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    itemView.context.getString(R.string.not_implemented),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = CategoriesItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    override fun getItemCount() = currentList.size

    companion object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.description == newItem.description
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.icon == newItem.icon
        }
    }
}