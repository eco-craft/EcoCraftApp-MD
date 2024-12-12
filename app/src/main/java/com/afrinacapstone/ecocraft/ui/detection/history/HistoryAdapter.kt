package com.afrinacapstone.ecocraft.ui.detection.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.ItemHistoryBinding
import com.afrinacapstone.ecocraft.model.PredictionHistory
import com.afrinacapstone.ecocraft.util.BaseListAdapter

class HistoryAdapter : BaseListAdapter<PredictionHistory, ItemHistoryBinding>(diffUtil) {

    var onItemClicked: ((PredictionHistory) -> Unit)? = null

    override fun inflateViewHolder(parent: ViewGroup, viewType: Int): ItemHistoryBinding {
        return ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindItem(binding: ItemHistoryBinding, position: Int) {
        val item = getItem(position)
        binding.apply {
            val detectionNo = position + 1
            historyDetectionNumber.text = root.context.getString(R.string.detection_ke, detectionNo)
            historyDate.text = item.createdAt

            root.setOnClickListener {
                onItemClicked?.invoke(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PredictionHistory>() {
            override fun areItemsTheSame(
                oldItem: PredictionHistory,
                newItem: PredictionHistory
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PredictionHistory,
                newItem: PredictionHistory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}