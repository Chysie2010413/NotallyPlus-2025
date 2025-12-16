package com.notally.plus.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.notally.plus.databinding.RecyclerColorBinding
import com.notally.plus.miscellaneous.Operations
import com.notally.plus.recyclerview.ItemListener
import com.notally.plus.room.Color

class ColorVH(private val binding: RecyclerColorBinding, listener: ItemListener) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.CardView.setOnClickListener {
            listener.onClick(adapterPosition)
        }
    }

    fun bind(color: Color) {
        val value = Operations.extractColor(color, binding.root.context)
        binding.CardView.setCardBackgroundColor(value)
    }
}