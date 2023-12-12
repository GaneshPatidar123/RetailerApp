package com.example.paypointretailer.View.Activity.addLimit

import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.ActivityCashDepositeBinding

class CashDepositeActivity : BaseActivity<ActivityCashDepositeBinding>(R.layout.activity_cash_deposite) {
    override fun setUpViews() {
         binding.toolBar.tvTitle.text = getString(R.string.cash_deposit)
    }

    override fun setUpListeners() {

        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}