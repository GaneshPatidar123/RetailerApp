package com.example.paypointretailer.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.paypointretailer.Base.BaseBindingAdapter
import com.example.paypointretailer.Base.BaseBindingViewHolder
import com.example.paypointretailer.Model.Favourite
import com.example.paypointretailer.View.Activity.addLimit.AddLimitActivity
import com.example.paypointretailer.databinding.AdapterAddlimitLayoutBinding

class AddLimitAdapter(
    private val CardClickListener: OnClikcListnerAdapterInterface,
    private val activity: Any,
) : BaseBindingAdapter<Favourite>() {

    interface OnClikcListnerAdapterInterface {
        fun onSelectPlan(position: Int,data: Favourite)
    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return AdapterAddlimitLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as AdapterAddlimitLayoutBinding
        val data = items?.get(position)

        binding.root.setOnClickListener {
            CardClickListener.onSelectPlan(position, data!!)
        }
        binding.tvTitle.text = data?.name
        binding.tvSubTitle.text = data?.subTitle
    }
}