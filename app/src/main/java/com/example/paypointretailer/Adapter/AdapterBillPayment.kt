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

class AdapterBillPayment(
    private val CardClickListener: ClikcListnerAdapterInterface,
    private val dashBoardFragment: DashBoardFragment,
) : BaseBindingAdapter<String>() {

    interface ClikcListnerAdapterInterface {
        fun onBillPaymentlistner(position: Int,text :String)

    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return AdapterRechargeBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as AdapterRechargeBinding
        val data = items?.get(position)

        if(data.equals("Telecom")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_mobile)
            binding.tvNew.visibility= View.GONE
        }else if(data.equals("Municipal Taxes")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.municipal_taxes)
            binding.tvNew.visibility= View.GONE
        }else if(data.equals("Electricity")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_electricity_bill)
            binding.tvNew.visibility= View.VISIBLE
        }else if(data.equals("Broadband")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_broadband)
            binding.tvNew.visibility= View.GONE
        }else if(data.equals("Piped Gas")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_gas)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Housing Society")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_housing_society)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Gas Cylinder")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_gas_bottle)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Education Fees")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_school_fees)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Water")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_waterdrop)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Hospital")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_hospital)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Credit Card")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_credit_card)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Cable TV")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_cable_tv)
            binding.tvNew.visibility= View.GONE
        } else if(data.equals("Landline")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_landline)
            binding.tvNew.visibility=View.GONE
        } else if(data.equals("LIC")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_life_insurance)
            binding.tvNew.visibility=View.GONE
        } else if(data.equals("Municipal Service")){
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.municipal_taxes)
            binding.tvNew.visibility=View.GONE
        }
        binding.tvName.text = data.toString()
        binding.root.setOnClickListener {
            CardClickListener.onBillPaymentlistner(position,data.toString())
        }
    }
}