package com.example.paypointretailer.Base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseBindingViewHolder(var binding: ViewDataBinding, private val clickListener: ClickListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    interface ClickListener {
        fun onViewClick(position: Int)
    }

    init {
        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        clickListener.onViewClick(adapterPosition)
    }
}