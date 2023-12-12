package com.example.paypointretailer.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.paypointretailer.Adapter.AdapterFavourites
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.ActivityAddFavouriteBinding

class AddFavouriteActivity : BaseActivity<ActivityAddFavouriteBinding>(R.layout.activity_add_favourite),AdapterFavourites.ClikcListnerAdapterInterface {
    private lateinit var adapterFavourites: AdapterFavourites
    override fun setUpViews() {

    //    adapterFavourites = AdapterFavourites(this,this)
      /*  val rechargeList = arrayOf("Mobile", "Broadband", "DTH", "Landline","Datacard", "Municipal Taxes", "Electricity", "Broadband","Piped Gas","Housing Society",
            "Gas Cylinder","Education Fees","Water","Hospital","Credit Card","Cable TV","Health", "Term life", "Car","Metro", "Bus", "Flight","Train","Hotel")
          binding.rvFavourites.adapter = adapterFavourites
        adapterFavourites.addItems(rechargeList.toList())
        adapterFavourites.notifyDataSetChanged()*/

    }

    override fun onClicklistner(position: Int, text: String) {

    }
}