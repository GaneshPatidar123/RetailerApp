package com.example.paypointretailer.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.paypointretailer.Base.BaseBindingAdapter
import com.example.paypointretailer.Base.BaseBindingViewHolder
import com.example.paypointretailer.Model.Response.MoneyTransfer.BeneficiariesList
import com.example.paypointretailer.R
import com.example.paypointretailer.View.Activity.MoneyTransfer.MoneyTransferActivity
import com.example.paypointretailer.View.Fragement.DashBoardFragment
import com.example.paypointretailer.databinding.AdapterRechargeBinding
import com.example.paypointretailer.databinding.LayoutBenificatialListBinding

class beneficiariesAdapter (
    private val CardClickListener: ClikcListnerAdapterInterface,
    private val activity: MoneyTransferActivity,
) : BaseBindingAdapter<BeneficiariesList>() {

    interface ClikcListnerAdapterInterface {
        fun onClickDatalistner(position: Int,text :String)

    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return LayoutBenificatialListBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as LayoutBenificatialListBinding
        val data = items?.get(position)
        binding.tvName.text = data?.BeneficiaryName
        binding.tvAccountNumber.text = data?.AccountNo
        binding.tvNameBank.text =data?.Bank

        if(data?.IsAcValidate==1){
            binding.tvStatus.text= "Yes"
            binding.tvStatus.setTextColor(activity.getColor(R.color.green))
        }else{
            binding.tvStatus.text= "No"
            binding.tvStatus.setTextColor(activity.getColor(R.color.red))
        }
        binding.root.setOnClickListener {
            CardClickListener.onClickDatalistner(position,data.toString())

        }
        if(position == 0){
            binding.ivSelected.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.ic_radio_checked))
        }else{
            binding.ivSelected.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.ic_radio_unchecked))
        }
    }

}