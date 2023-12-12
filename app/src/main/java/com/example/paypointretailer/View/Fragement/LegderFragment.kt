package com.example.paypointretailer.View.Fragement

import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.example.paypointretailer.Base.BaseFragment
import com.example.paypointretailer.Model.Response.MobileRecharge.PlanListResponse
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils.Companion.setActivtiy
import com.example.paypointretailer.Utils.UpdateFirstFragmentEvent
import com.example.paypointretailer.databinding.LayoutLedgerFragmentBinding
import org.greenrobot.eventbus.EventBus

class LegderFragment : BaseFragment<LayoutLedgerFragmentBinding>(R.layout.layout_ledger_fragment),
    BaseFragment.onDateSelect {

    override fun setUpViews() {
        setActivtiy(requireActivity())
    }

    override fun setUpListeners() {
        binding.btnSubmit.setOnClickListener {
            EventBus.getDefault().post(UpdateFirstFragmentEvent("firstFragement"))
        }
        binding.edtFromDate.setOnClickListener {
            showDatePicker(this, binding.edtFromDate)
        }
        binding.edtToDate.setOnClickListener {
            showDatePicker(this, binding.edtToDate)
        }
    }


    override fun date(date: String, editText: AppCompatEditText?) {
        editText?.setText(date)
    }


}
