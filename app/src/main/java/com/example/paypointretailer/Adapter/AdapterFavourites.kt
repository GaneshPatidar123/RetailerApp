package com.example.paypointretailer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.paypointretailer.Base.BaseBindingAdapter
import com.example.paypointretailer.Base.BaseBindingViewHolder
import com.example.paypointretailer.Model.Favourite
import com.example.paypointretailer.R
import com.example.paypointretailer.View.Activity.AddFavouriteActivity
import com.example.paypointretailer.View.Fragement.DashBoardFragment
import com.example.paypointretailer.databinding.LayoutAdapterFavouritesBinding

class AdapterFavourites(
    private val CardClickListener: ClikcListnerAdapterInterface,
    private val activvty: DashBoardFragment,
) : BaseBindingAdapter<Favourite>() {

    interface ClikcListnerAdapterInterface {
        fun onClicklistner(position: Int, text: String)

    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return LayoutAdapterFavouritesBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as LayoutAdapterFavouritesBinding
        val data = items?.get(position)

        if (data!!.name.equals("DTH")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_dth)
        } else if (data.name.equals("Mobile")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_mobile)
        } else if (data.name.equals("Broadband")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_broadband)
        } else if (data.name.equals("Landline")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_landline)
        } else if (data.name.equals("Datacard")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.datacard)
        } else if (data.name.equals("Municipal Taxes")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.municipal_taxes)
        } else if (data.name.equals("Electricity")) {
            binding.ivImage.background =
                activvty.resources.getDrawable(R.drawable.ic_electricity_bill)
        } else if (data.name.equals("Broadband")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_broadband)
        } else if (data.name.equals("Piped Gas")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_gas)
        } else if (data.name.equals("Housing Society")) {
            binding.ivImage.background =
                activvty.resources.getDrawable(R.drawable.ic_housing_society)
        } else if (data.name.equals("Gas Cylinder")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_gas_bottle)
        } else if (data.name.equals("Education Fees")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_school_fees)
        } else if (data.name.equals("Water")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_waterdrop)
        } else if (data.name.equals("Hospital")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_hospital)
        } else if (data.name.equals("Credit Card")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_credit_card)
        } else if (data.name.equals("Cable TV")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_cable_tv)
        } else if (data.name.equals("Metro")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_metro)
        } else if (data.name.equals("Bus")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_bus)
        } else if (data.name.equals("Flight")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.plan)
        } else if (data.name.equals("Train")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_train)
        } else if (data.name.equals("Hotel")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_hotel)
        } else if (data.name.equals("Term life")) {
            binding.ivImage.background =
                activvty.resources.getDrawable(R.drawable.ic_life_insurance)
        } else if (data.name.equals("Car")) {
            binding.ivImage.background = activvty.resources.getDrawable(R.drawable.ic_car_insurence)
        }
        if (data.isSelected) {
            binding.ivSelected.background = activvty.resources.getDrawable(R.drawable.ic_selected)
        } else {
            binding.ivSelected.background = activvty.resources.getDrawable(R.drawable.ic_unselected)
        }
        binding.root.setOnClickListener {
            if (data.isSelected) {
                data.isSelected = false
                binding.ivSelected.background =
                    activvty.resources.getDrawable(R.drawable.ic_unselected)
                notifyDataSetChanged()
            } else {
                data.isSelected = true
                binding.ivSelected.background =
                    activvty.resources.getDrawable(R.drawable.ic_selected)
                notifyDataSetChanged()
            }
        }
        binding.tvName.text = data.name.toString()
    }


}