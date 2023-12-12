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

class AdapterRecharge(
    private val CardClickListener: ClikcListnerAdapterInterface,
    private val dashBoardFragment: DashBoardFragment,
    private val isfrom: String,
) : BaseBindingAdapter<String>() {

    interface ClikcListnerAdapterInterface {
        fun onClickRechargelistner(position: Int,text :String)

    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return AdapterRechargeBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as AdapterRechargeBinding
        val data = items?.get(position)
        if(isfrom.equals("Insurence")){
            if(data.equals("Health")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_health_insurance)
            }else if(data.equals("Term life")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_life_insurance)
            }else if(data.equals("Car")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_car_insurence)
            }
            binding.tvName.text = data.toString()
        }else{
            if(data.equals("DTH")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_dth)
                binding.tvNew.visibility=View.VISIBLE
            }else if(data.equals("Mobile")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_mobile)
                binding.tvNew.visibility=View.GONE
            }else if(data.equals("Broadband")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_broadband)
                binding.tvNew.visibility=View.GONE
            }else if(data.equals("Landline")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_landline)
                binding.tvNew.visibility=View.GONE
            }else if(data.equals("Datacard")){
                binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.datacard)
                binding.tvNew.visibility=View.VISIBLE
                binding.tvNew.background = dashBoardFragment.resources.getDrawable(R.drawable.background_green_rounded)
                binding.tvNew.text="Active"
            }

            binding.tvName.text = data.toString()
        }
        binding.root.setOnClickListener {
            CardClickListener.onClickRechargelistner(position,data.toString())
        }

    }

}