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

class AdapterMoneyTransfer(
    private val CardClickListener: ClikcListnerAdapterInterface,
    private val dashBoardFragment: DashBoardFragment,
) : BaseBindingAdapter<String>() {

    interface ClikcListnerAdapterInterface {
        fun onClickMoneyTransfer(position: Int, text: String)

    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return AdapterRechargeBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as AdapterRechargeBinding
        val data = items?.get(position)

        if (data.equals("AEPS")) {
            binding.ivImage.background = dashBoardFragment.resources.getDrawable(R.drawable.ic_dth)
            binding.tvNew.visibility = View.VISIBLE
        } else if (data.equals("Aadhar Pay")) {
            binding.ivImage.background =
                dashBoardFragment.resources.getDrawable(R.drawable.ic_mobile)
            binding.tvNew.visibility = View.GONE
        } else if (data.equals("Money Transfer")) {
            binding.ivImage.background =
                dashBoardFragment.resources.getDrawable(R.drawable.ic_broadband)
            binding.tvNew.visibility = View.GONE
        } else if (data.equals("Nepal Money Transfer")) {
            binding.ivImage.background =
                dashBoardFragment.resources.getDrawable(R.drawable.ic_landline)
            binding.tvNew.visibility = View.GONE
        }

        binding.tvName.text = data.toString()
        binding.root.setOnClickListener {
            CardClickListener.onClickMoneyTransfer(position, data.toString())
        }

    }


}