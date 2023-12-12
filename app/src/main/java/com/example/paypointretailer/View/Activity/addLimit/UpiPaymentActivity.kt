package com.example.paypointretailer.View.Activity.addLimit

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.MainActivity
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Response.MobileRecharge.GetOperatorCode
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppConstant
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.View.Activity.WebViewActivity
import com.example.paypointretailer.ViewModel.DthRechargeViewModel
import com.example.paypointretailer.ViewModel.UpiPaymentStateEvent
import com.example.paypointretailer.ViewModel.UpiPaymentViewModel
import com.example.paypointretailer.databinding.ActivityUpiPaymentBinding
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpiPaymentActivity : BaseActivity<ActivityUpiPaymentBinding>(R.layout.activity_upi_payment) {

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: UpiPaymentViewModel by viewModels()
    private var StringData: MutableList<String>? = mutableListOf()
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.upi_payment)
        getUPIList()
    }

    private fun getUPIList() {
        var request: GetBillCustDetailsRequest = GetBillCustDetailsRequest(
            ProductAliasID = null,
            CANo = AppUtils.getUUID(sharedPrefs),
            Opt1 = pref.getData()?.access_token?.toString(),
            Opt3 = pref.getData()?.BusinessId?.toString(),
            devicecode = pref.getData()?.DeviceCode,
            icode = pref.getData()?.IdentificationCode,
            pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())

        )
        viewModel.getUpiList(UpiPaymentStateEvent.getVPA, request)
    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.selectArea.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.selectArea.showDropDown()
            false
        })

        binding.selectArea.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            setUp()
        })
        binding.btnAddNew.setOnClickListener {
            if (binding.edtVPA.visibility == View.VISIBLE && binding.edtVPA.text.isNullOrEmpty()) {
                showErrorMessage(this, "Please enter your upi Id")
            } else if (binding.edtAmount.text.isNullOrEmpty()) {
                showErrorMessage(this, "Please enter amount")
            } else {
                var url = ""
                if (binding.edtVPA.visibility == View.GONE) {
                    val originalText = binding.selectArea.text.toString()
                    val textWithoutSpaces = originalText.replace("\\s".toRegex(), "")
                    url = "https://mb2b.paypointindia.co.in/" + "Process/PostLoadWallet.aspx" +
                            "?" + pref.getData()?.BusinessId + "&" + pref.getData()?.DeviceCode + "&" + binding.edtAmount.text.toString() + "&" + textWithoutSpaces
                } else {
                    val originalText = binding.edtVPA.text.toString()
                    val textWithoutSpaces = originalText.replace("\\s".toRegex(), "")
                    url = "https://mb2b.paypointindia.co.in/" + "Process/PostLoadWallet.aspx" +
                            "?" + pref.getData()?.BusinessId + "&" + pref.getData()?.DeviceCode + "&" + binding.edtAmount.text.toString() + "&" + textWithoutSpaces
                }
                val bundle = Bundle()
                bundle.putString(AppConstant.IS_FROM, url)
                bundle.putString("isClick", "UPI")
                launch(
                    this,
                    WebViewActivity::class.java,
                    bundle
                )
            }


        }
    }

    private fun setUp() {
        if (binding.selectArea.text.toString().equals("Add another UPI")) {
            binding.edtVPA.visibility = View.VISIBLE
            binding.tvUpiID.visibility = View.VISIBLE
        } else {
            binding.edtVPA.visibility = View.GONE
            binding.tvUpiID.visibility = View.GONE
        }
    }

    override fun setUpObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val listdata = dataState.data.BusinessVPAMasterResEntity!!

                    for (item in listdata.indices) {
                        StringData?.add(listdata[item].PayerVA!!)
                    }

                    if (StringData?.size != 0) {

                        if (StringData?.size != 4 && StringData?.size != 0) {
                            StringData?.add("Add another UPI")
                        }

                        val adapter =
                            ArrayAdapter(
                                this,
                                android.R.layout.simple_dropdown_item_1line,
                                StringData!!
                            )
                        binding.selectArea.threshold = 1
                        binding.selectArea.setAdapter(adapter)
                    } else {
                        binding.edtVPA.visibility = View.VISIBLE
                        binding.selectArea.visibility = View.GONE
                        binding.tvUpiID.visibility = View.VISIBLE
                    }
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
}