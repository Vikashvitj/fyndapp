package com.demoapp.encoraitunes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.demoapp.encoraitunes.data_classes.Entry
import com.demoapp.encoraitunes.databinding.ItemImageListBinding

class ImageListAdapter(val onItemClick: (Entry) -> Unit) :
    BaseListAdapter<Entry, ItemImageListBinding>(DiffCallBack()) {
    override fun createBinding(parent: ViewGroup) =
        ItemImageListBinding.inflate(LayoutInflater.from(parent.context))

    override fun bind(binding: ItemImageListBinding, item: Entry?) {
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
        class DiffCallBack : DiffUtil.ItemCallback<Entry>() {
            override fun areItemsTheSame(
                oldItem: Entry,
                newItem: Entry
            ): Boolean {
                return oldItem.id.attributes.imId == newItem.id.attributes.imId
            }

            override fun areContentsTheSame(
                oldItem: Entry,
                newItem: Entry
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}