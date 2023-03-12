package com.example.testapp.ui.home.apadters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testapp.R
import com.example.testapp.databinding.PhotoThumbnailBinding
import com.example.testapp.ui.home.data.Preview

class PreviewsAdapter(
    private val previews: List<Preview>,
    private val onClick: (Uri, Int) -> Unit
) : ListAdapter<Preview, PreviewsAdapter.PreviewViewHolder>(DiffCallback) {

    inner class PreviewViewHolder(private var binding: PhotoThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Preview, position: Int) {

            val imgUri = item.uriString.toUri().buildUpon().scheme("https").build()

            if (item.isChecked) {
                binding.imageHolderDefault.visibility = View.INVISIBLE

                binding.imageHolderSelected.visibility = View.VISIBLE
                try {
                    binding.photoSelected.load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                } catch (e: Exception) {
                    Log.e("PreviewAdapter", "Error: ${e.message}")
                }
            } else {
                binding.imageHolderSelected.visibility = View.INVISIBLE

                binding.imageHolderDefault.visibility = View.VISIBLE
                try {
                    binding.photoDefault.load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                } catch (e: Exception) {
                    Log.e("PreviewAdapter", "Error: ${e.message}")
                }
            }

            itemView.setOnClickListener { onClick(imgUri, position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val binding = PhotoThumbnailBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Preview>() {
        override fun areItemsTheSame(oldItem: Preview, newItem: Preview): Boolean {
            return oldItem.uriString == newItem.uriString
        }

        override fun areContentsTheSame(oldItem: Preview, newItem: Preview): Boolean {
            return oldItem == newItem
        }
    }

}