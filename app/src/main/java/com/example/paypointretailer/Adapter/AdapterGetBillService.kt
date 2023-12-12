package com.example.paypointretailer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.paypointretailer.Base.BaseBindingAdapter
import com.example.paypointretailer.Base.BaseBindingViewHolder
import com.example.paypointretailer.Model.Favourite
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.databinding.AdapterAddlimitLayoutBinding

class AdapterGetBillService (
    private val CardClickListener: OnClikcListnerAdapterInterface,
    private val activity: Any,
) : BaseBindingAdapter<GetServiceList>() {

    interface OnClikcListnerAdapterInterface {
        fun onSelectPlan(position: Int,data: GetServiceList)
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
        binding.tvTitle.text = data?.Product
        binding.tvSubTitle.visibility=View.GONE
    }
}