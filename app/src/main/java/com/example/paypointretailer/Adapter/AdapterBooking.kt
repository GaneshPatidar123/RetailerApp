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

class AdapterBooking(
    private val CardClickListener: ClikcListnerAdapterInterface,
    private val dashBoardFragment: DashBoardFragment,
) : BaseBindingAdapter<String>() {

    interface ClikcListnerAdapterInterface {
        fun onBookinglistner(position: Int,text :String)

    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return AdapterRechargeBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as AdapterRechargeBinding
        val data = items?.get(position)
            if(data.equals("Metro")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_metro)
            }else if(data.equals("Bus")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_bus)
            }else if(data.equals("Flight")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.plan)
            }else if(data.equals("Train")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_train)
            }else if(data.equals("Hotel")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_hotel)
            }
            binding.root.setOnClickListener {
                CardClickListener.onBookinglistner(position,data.toString())
            }
            binding.tvName.text = data.toString()

    }
}