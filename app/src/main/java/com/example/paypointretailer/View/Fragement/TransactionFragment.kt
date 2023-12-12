package com.example.paypointretailer.View.Fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.example.paypointretailer.Base.BaseFragment
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.databinding.LayoutCommisionFragmentBinding
import com.example.paypointretailer.databinding.LayoutTransactionFragmentBinding

class TransactionFragment :
    BaseFragment<LayoutTransactionFragmentBinding>(R.layout.layout_transaction_fragment),
    BaseFragment.onDateSelect {
    private val CardClickListener: OnChangeTabListner? = null
    override fun setUpViews() {
        AppUtils.setActivtiy(requireActivity())
    }

    override fun setUpListeners() {
        binding.btnSubmit.setOnClickListener {
            CardClickListener?.CardClickListener()
        }
        binding.edtFromDate.setOnClickListener {
            showDatePicker(this, binding.edtFromDate)
        }
        binding.edtToDate.setOnClickListener {
            showDatePicker(this, binding.edtToDate)
        }
    }

    interface OnChangeTabListner {
        fun CardClickListener()
    }

    override fun date(date: String, editText: AppCompatEditText?) {
        editText?.setText(date)
    }

}