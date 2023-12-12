package com.example.paypointretailer.View.Activity.addLimit

import android.view.View
import android.widget.ArrayAdapter
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.ActivityOnlinePaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnlinePaymentActivity : BaseActivity<ActivityOnlinePaymentBinding>(R.layout.activity_online_payment){
    var accountType = arrayOf(
        "Saving Account", "Current  Account"
    )
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.online_payment)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, accountType)
        binding.edtSelectAccountType.threshold = 1
        binding.edtSelectAccountType.setAdapter(adapter)
    }

    override fun setUpListeners() {
        binding.edtSelectAccountType.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.edtSelectAccountType.showDropDown()
            false
        })
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}