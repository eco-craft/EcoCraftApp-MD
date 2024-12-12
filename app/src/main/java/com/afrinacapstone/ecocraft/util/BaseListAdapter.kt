package com.afrinacapstone.ecocraft.util

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T, VH : ViewBinding>(
    diffUtil: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseListAdapter.BaseViewHolder<VH>>(diffUtil) {
    class BaseViewHolder<VH : ViewBinding>(val binding: VH) : RecyclerView.ViewHolder(binding.root)

    abstract fun inflateViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun bindItem(binding: VH, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VH> {
        return BaseViewHolder(inflateViewHolder(parent, viewType))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VH>, position: Int) {
        bindItem(holder.binding, position)
    }
}
