package com.notally.plus.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.notally.plus.databinding.ErrorBinding
import com.notally.plus.image.ImageError

class ErrorVH(private val binding: ErrorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(error: ImageError) {
        binding.Name.text = error.name
        binding.Description.text = error.description
    }
}