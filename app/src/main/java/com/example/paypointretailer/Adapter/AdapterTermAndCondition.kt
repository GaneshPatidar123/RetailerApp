package com.example.paypointretailer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.paypointretailer.Base.BaseBindingAdapter
import com.example.paypointretailer.Base.BaseBindingViewHolder
import com.example.paypointretailer.R
import com.example.paypointretailer.View.Fragement.DashBoardFragment
import com.example.paypointretailer.databinding.AdapterRechargeBinding
import com.example.paypointretailer.databinding.LayoutTermAndConditionBinding

class AdapterTermAndCondition (
    private val dashBoardFragment: Any,
) : BaseBindingAdapter<String>() {

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return LayoutTermAndConditionBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as LayoutTermAndConditionBinding
        val data = items?.get(position)

        binding.tvName.text = data.toString()

    }
}