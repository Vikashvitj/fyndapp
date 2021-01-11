package com.demoapp.fyndapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.demoapp.fyndapp.data_classes.Movie
import com.demoapp.fyndapp.databinding.ItemImageListBinding

class ImageListAdapter(val onItemClick: (Movie) -> Unit) :
    BaseListAdapter<Movie, ItemImageListBinding>(DiffCallBack()) {
    override fun createBinding(parent: ViewGroup) =
        ItemImageListBinding.inflate(LayoutInflater.from(parent.context))

    override fun bind(binding: ItemImageListBinding, item: Movie?) {
        binding.topSongItem = item
        binding.root.setOnClickListener {
            onItemClick(item!!)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object {
        class DiffCallBack : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}