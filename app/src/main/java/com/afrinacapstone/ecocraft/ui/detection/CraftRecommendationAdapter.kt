package com.afrinacapstone.ecocraft.ui.detection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.afrinacapstone.ecocraft.databinding.ItemCraftBinding
import com.afrinacapstone.ecocraft.model.Craft
import com.afrinacapstone.ecocraft.util.BaseListAdapter
import com.afrinacapstone.ecocraft.util.loadFromUrl

class CraftRecommendationAdapter : BaseListAdapter<Craft, ItemCraftBinding>(diffUtil) {

    var onClickListener: ((Craft) -> Unit)? = null

    override fun inflateViewHolder(parent: ViewGroup, viewType: Int): ItemCraftBinding {
        return ItemCraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindItem(binding: ItemCraftBinding, position: Int) {
        val item = getItem(position)
        binding.apply {
            ivCraft.loadFromUrl(item.image)
            tvCraft.text = item.title

            root.setOnClickListener {
                onClickListener?.invoke(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Craft>() {
            override fun areItemsTheSame(oldItem: Craft, newItem: Craft): Boolean {
                return newItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: Craft, newItem: Craft): Boolean {
                return newItem == oldItem
            }
        }
    }
}