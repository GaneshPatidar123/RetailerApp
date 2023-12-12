package com.example.paypointretailer.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.paypointretailer.Base.BaseBindingAdapter
import com.example.paypointretailer.Base.BaseBindingViewHolder
import com.example.paypointretailer.Model.Response.MobileRecharge.Result
import com.example.paypointretailer.R
import com.example.paypointretailer.View.Fragement.DashBoardFragment
import com.example.paypointretailer.View.Fragement.PlanListFragment
import com.example.paypointretailer.databinding.AdapterPlanlistBinding
import com.example.paypointretailer.databinding.AdapterRechargeBinding

class PlanListAdapter(
    private val CardClickListener: ClikcListnerAdapterInterface,
    private val dashBoardFragment: Any,
) : BaseBindingAdapter<Result>() {

    interface ClikcListnerAdapterInterface {
        fun onSelectPlan(position: Int,result: Result)
    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return AdapterPlanlistBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as AdapterPlanlistBinding
        val data = items?.get(position)

        binding.root.setOnClickListener {
            CardClickListener.onSelectPlan(position, data!!)
        }
        binding.tvPlanAmount.text = data?.Amount
        binding.tvValidityAns.text = data?.Validity
        binding.tvPLanTypeDiscrption.text =data?.Description
    }
}