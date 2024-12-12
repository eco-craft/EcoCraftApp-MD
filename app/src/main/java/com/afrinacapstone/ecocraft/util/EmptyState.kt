package com.afrinacapstone.ecocraft.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.afrinacapstone.ecocraft.databinding.ItemEmptyStateBinding

class EmptyState @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ItemEmptyStateBinding.inflate(LayoutInflater.from(context), this, true)

    fun setEmptyState(title: String) {
        binding.tvEmpty.text = title
    }

}