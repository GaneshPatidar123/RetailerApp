package com.example.paypointretailer.View.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.paypointretailer.Adapter.AdapterGiftVoucher
import com.example.paypointretailer.Adapter.AdapterOnlineShopping
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.MainActivity
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppConstant
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.ViewModel.GiftVoucherStateEvent
import com.example.paypointretailer.ViewModel.GiftVoucherViewModel
import com.example.paypointretailer.ViewModel.OtpVerfiedViewModel
import com.example.paypointretailer.databinding.ActivityGiftVoucherBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GiftVoucherActivity :
    BaseActivity<ActivityGiftVoucherBinding>(R.layout.activity_gift_voucher) , AdapterGiftVoucher.OnClikcListnerAdapterInterface {
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val giftVoucherViewModel: GiftVoucherViewModel by viewModels()
    private lateinit var adapterGiftVoucher : AdapterGiftVoucher
    private var isFrom =""
    private var listData: MutableList<ListGiftVoucherResponse>? = mutableListOf()
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.gift_voucher)
        getBndleData(intent)
        setUpRecycleView()
        getGiftVouchcerList()
    }

    private fun getBndleData(intent: Intent?) {
        var bundle = intent?.extras
        if(bundle!=null){
            isFrom = bundle.getString(AppConstant.IS_FROM)!!
        }
    }

    private fun setUpRecycleView() {
        adapterGiftVoucher = AdapterGiftVoucher(this, this,isFrom)
        binding.rvGiftList.adapter = adapterGiftVoucher
        adapterGiftVoucher.notifyDataSetChanged()
    }

    private fun getGiftVouchcerList() {
        if(isFrom.equals("Gift")){
            giftVoucherViewModel.getGiftVoucherList(
                GiftVoucherStateEvent.getGiftVocuher,
                pref.getData()?.access_token.toString(),isFrom
            )
        }else{
            giftVoucherViewModel.getOttSubscrtpionList(
                GiftVoucherStateEvent.getGiftVocuher,
                pref.getData()?.access_token.toString(),isFrom
            )
        }

    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setUpObservers() {
        giftVoucherViewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    adapterGiftVoucher.addItems(userData)
                    adapterGiftVoucher.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    hideProgressDialog()
                    showErrorMessage(this, dataState.message)
                    //  notifyUser(dataState.message)
                }

                is Resource.Loading -> {
                    showProgressDialog()
                }
            }
        })
        giftVoucherViewModel.dataStateOtt.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                      for(item in userData.indices){
                          var ListGiftVoucherResponse = ListGiftVoucherResponse(userData[item].name,userData[item].id,"")
                          listData?.add(ListGiftVoucherResponse)
                      }
                    adapterGiftVoucher.addItems(listData!!)
                    adapterGiftVoucher.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    hideProgressDialog()
                    showErrorMessage(this, dataState.message)
                }

                is Resource.Loading -> {
                    showProgressDialog()
                }
            }
        })

    }

    override fun onSelectPlan(position: Int, data: ListGiftVoucherResponse) {
        var bundle = Bundle()
        bundle.putSerializable(AppConstant.DATA,data)
        bundle.putString(AppConstant.IS_FROM,isFrom)
        launch(this@GiftVoucherActivity,BuynowGiftActivity::class.java,bundle)
    }
}