package com.example.paypointretailer.View.Activity.Recharge

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Response.MainResponse.ProductEntity
import com.example.paypointretailer.Model.Response.MobileRecharge.GetOperatorCode
import com.example.paypointretailer.Model.Response.MobileRecharge.Result
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.View.Activity.BrowserPlanActivity
import com.example.paypointretailer.ViewModel.GetMobileRechargeStateEvent
import com.example.paypointretailer.ViewModel.MobileRechargeViewModel
import com.example.paypointretailer.databinding.ActivityMobileRechargeBinding

import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MobileRechargeActivity :
    BaseActivity<ActivityMobileRechargeBinding>(R.layout.activity_mobile_recharge) {

    private var operatorList: MutableList<String>? = mutableListOf()
    private var operatorLists: List<ProductEntity>? = mutableListOf()
    lateinit var getOperatorCode: GetOperatorCode
    val requestCode = 1

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: MobileRechargeViewModel by viewModels()
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.mobile_Recharge)
        if (pref.getInitialData()?.ProductEntity != null) {
            val list = pref.getInitialData()?.ProductEntity
            operatorLists = list?.filter { it -> it.DomainId == 3 }
        }
        for (item in operatorLists!!.indices) {
            operatorList?.add(operatorLists!![item].Product!!)
        }
        // operatorList?.addAll(operatorLists as MutableList<ProductEntity>)
        binding.tvBrowser.alpha = 0.4f
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, operatorList!!)
        binding.edtChoose.threshold = 1
        binding.edtChoose.setAdapter(adapter)

    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtChoose.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.edtChoose.showDropDown()
            false
        })

        /*binding.edtChoose.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position)
            val studentInfo: ProductEntity = item as ProductEntity
            binding.edtChoose.setText(studentInfo.Product)
            binding.edtChoose.showDropDown()
        })*/



        binding.edtMobileNUmber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 10) {
                    viewModel.getOperator(
                        GetMobileRechargeStateEvent.getOperatorEvent,
                        pref.getData()?.access_token,
                        s.toString()
                    )

                } else {
                    binding.tvBrowser.alpha = 0.4f
                }
            }
        })
        binding.tvBrowser.setOnClickListener {
            if (binding.edtMobileNUmber.text?.length == 10) {
                var bundle = Bundle()

                //  launch(this@MobileRechargeActivity,BrowserPlanActivity::class.java,bundle)

                val intent = Intent(this, BrowserPlanActivity::class.java)
                bundle.putSerializable("OperatorData", getOperatorCode.data)
                intent.putExtras(bundle);
                startActivityForResult(intent, requestCode)
            }
        }

        binding.btnRecharge.setOnClickListener {
            AppUtils.hideKeyboard(this)
            if (binding.edtMobileNUmber.text.isNullOrEmpty()) {
                AppUtils.showErrorSnackBar(this, "Please enter mobile number")
            } else if (binding.edtMobileNUmber.text!!.length < 10) {
                AppUtils.showErrorSnackBar(this, "Please enter 10 digit mobile number")
            } else if (binding.edtChoose.text!!.equals("select Operator")) {
                AppUtils.showErrorSnackBar(this, "Please select your operator")
            } else if (binding.edtAmount.text.isNullOrEmpty()) {
                AppUtils.showErrorSnackBar(this, "Please enter plan amount")
            } else {
                // pcode = incrept Password
                if (AppUtils.hasInternet(this)) {
                    var request: MobilreRechargeRequest = MobilreRechargeRequest(
                        ServiceAcNo = binding.edtMobileNUmber.text.toString(),
                        Amount = binding.edtAmount.text.toString(),
                        ProductID = getOperatorCode.data?.opr_code!!.toInt(),
                        authorization = pref.getData()?.access_token,
                        businessid = pref.getData()?.BusinessId.toString(),
                        devicecode = pref.getData()?.DeviceCode,
                        icode = pref.getData()?.IdentificationCode,
                        pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())
                    )
                    viewModel.rechargeNow(GetMobileRechargeStateEvent.rechargeNow, request)
                } else {
                    showErrorMessage(
                        this,
                        getString(R.string.please_check_internet_connection)
                    )
                }

            }
        }

    }


    override fun setUpObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    getOperatorCode =
                        GsonBuilder().create().fromJson(dataState.data, GetOperatorCode::class.java)
                    if (getOperatorCode.status == "success") {
                        binding.tvBrowser.alpha = 1f
                        var oprCode = getOperatorCode.data?.opr_code
                        for (item in operatorLists!!.indices) {
                            if (operatorLists!![item].ProductId == oprCode?.toInt()) {
                                binding.edtChoose.setText(operatorLists!![item].Product!!)
                            }
                        }
                    } else {
                        showErrorMessage(this, getOperatorCode.description!!)
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

        viewModel.dataStateRecharge.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    if (dataState.data.Status == "success") {
                        //   showErrorMessage(this, dataState.data.ErrorDes!!)
                    } else {
                        showErrorMessage(this, dataState.data.ErrorDes!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val resultData = data?.getParcelableExtra<Result>("resultData")
            binding.llTalkTime.visibility = View.VISIBLE
            binding.edtAmount.setText(resultData?.Amount.toString())
            binding.tvValidityAns.text = resultData?.Validity

            val wordToSpan: Spannable =
                SpannableString("Description : ${resultData?.Description}")

            wordToSpan.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)),
                12,
                wordToSpan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.tvDescription.text = wordToSpan
            Log.d("TAG", "onActivityResult: " + resultData)
        }
    }
    /*   override fun onDataReceived(data: Result) {
           binding.tvAmount.text = data.Amount
       }
   */
}