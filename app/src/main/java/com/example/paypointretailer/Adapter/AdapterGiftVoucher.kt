package com.example.paypointretailer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.paypointretailer.Base.BaseBindingAdapter
import com.example.paypointretailer.Base.BaseBindingViewHolder
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.View.Activity.GiftVoucherActivity
import com.example.paypointretailer.databinding.AdapterAddlimitLayoutBinding
import com.example.paypointretailer.databinding.AdapterGiftVoucherLayoutBinding

class AdapterGiftVoucher  (
    private val CardClickListener: OnClikcListnerAdapterInterface,
    private val activity: GiftVoucherActivity,
    private val isfrom :String,
) : BaseBindingAdapter<ListGiftVoucherResponse>() {

    interface OnClikcListnerAdapterInterface {
        fun onSelectPlan(position: Int,data: ListGiftVoucherResponse)
    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return AdapterGiftVoucherLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as AdapterGiftVoucherLayoutBinding
        val data = items?.get(position)

        binding.root.setOnClickListener {
            CardClickListener.onSelectPlan(position, data!!)
        }
        //{{ "https://paypointindia.co.in/images/gv/" + gv.Key + ".png"}}
        val imageUrl ="https://paypointindia.co.in/images/gv/" + data?.Key + ".png"
        Glide.with(activity)
            .load(imageUrl)
            .into(binding.ivGiftImage)

        if(!isfrom.equals("Gift")){
            binding.tvName.text=data?.Name
            binding.tvName.visibility=View.VISIBLE
            binding.tvBuyNow.textSize = 15f
        }

    }
}