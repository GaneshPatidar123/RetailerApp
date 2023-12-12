package com.example.paypointretailer.View.Activity.addLimit

import com.example.paypointretailer.Adapter.AddLimitAdapter
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showSuccessMessage
import com.example.paypointretailer.Model.Favourite
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.ActivityAddLimitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddLimitActivity : BaseActivity<ActivityAddLimitBinding>(R.layout.activity_add_limit) ,AddLimitAdapter.OnClikcListnerAdapterInterface{
    private var addLimitList: MutableList<Favourite>? = mutableListOf()
    private lateinit var addLimitAdapter: AddLimitAdapter
    override fun setUpViews() {
        binding.toolBar.tvTitle.text =getString(R.string.add_limit)
        addStaticData()
        setUpRecycleView()
    }

    private fun setUpRecycleView() {
        addLimitAdapter = AddLimitAdapter(this, this)
        binding.rvLimit.adapter = addLimitAdapter
        addLimitAdapter.addItems(addLimitList!!.toList())
        addLimitAdapter.notifyDataSetChanged()
    }

    private fun addStaticData() {
        addLimitList?.clear()
        addLimitList?.add(Favourite(false, "Online","Transfer Funds by Online mode"))
        addLimitList?.add(Favourite(false, "UPI","Add Funds to My Limit via UPI"))
        addLimitList?.add(Favourite(false, "Offline Transaction","Create Request to Add Funds to My Limit via Offline Transaction (Cash/CDM (Cash Deposit Machine)/Cheque Deposit, NEFT/IMPS/RTGS, INF/BIL, INB (SBI TO SBI Transfer)"))
        addLimitList?.add(Favourite(false, "To My SBI KIOSK Limit","Add Funds to My SBI KIOSK Limit"))
        addLimitList?.add(Favourite(false, "From AEPS Ledger To O2O Ledger","Transfer funds from AEPS ledger to O2O ledger"))
        addLimitList?.add(Favourite(false, "From My AEPS Ledger To Bank Account","Transfer funds from my AEPS ledger to Bank Account"))
        addLimitList?.add(Favourite(false, "From My AEPS ledger To any Bank Account via digikhata","Transfer funds from my AEPS ledger to any Bank Account via digikhata"))
        addLimitList?.add(Favourite(false, "From My AEPS ledger to digikhata","Transfer funds from my AEPS ledger to digikhata"))
    }

    override fun onSelectPlan(position: Int, data: Favourite) {
      if(data.name.equals("Online")){
          showSuccessMessage(this, data.name!!)
      }else if(data.name.equals("UPI")){
          showSuccessMessage(this, data.name!!)
      }else if(data.name.equals("Offline Transaction")){
          showSuccessMessage(this, data.name!!)
      }else if(data.name.equals("To My SBI KIOSK Limit")){
          showSuccessMessage(this, data.name!!)
      }else if(data.name.equals("From AEPS Ledger To O2O Ledger")){
          showSuccessMessage(this, data.name!!)
      }else if(data.name.equals("From My AEPS Ledger To Bank Account")){
          showSuccessMessage(this, data.name!!)
      }else if(data.name.equals("From My AEPS ledger To any Bank Account via digikhata")){
          showSuccessMessage(this, data.name!!)
      }else if(data.name.equals("From My AEPS ledger to digikhata")){
          showSuccessMessage(this, data.name!!)
      }

    }
}