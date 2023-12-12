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
import com.example.paypointretailer.Utils.UpdateFirstFragmentEvent
import com.example.paypointretailer.databinding.LayoutCommisionFragmentBinding
import com.example.paypointretailer.databinding.LayoutLedgerFragmentBinding
import org.greenrobot.eventbus.EventBus

class CommisionFragment :
    BaseFragment<LayoutCommisionFragmentBinding>(R.layout.layout_commision_fragment),
    BaseFragment.onDateSelect {

    override fun setUpViews() {
        AppUtils.setActivtiy(requireActivity())
    }

    override fun setUpListeners() {
        binding.edtFromDate.setOnClickListener {
            showDatePicker(this, binding.edtFromDate)
        }
        binding.edtToDate.setOnClickListener {
            showDatePicker(this, binding.edtToDate)
        }
        binding.btnSubmit.setOnClickListener {
            EventBus.getDefault().post(UpdateFirstFragmentEvent("secondFragment"))
        }
    }

    override fun setUpObservers() {

    }

    override fun date(date: String, editText: AppCompatEditText?) {
        editText?.setText(date)
    }

}