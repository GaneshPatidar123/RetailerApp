package com.example.paypointretailer.View.Activity.MoneyTransfer

import android.app.Dialog
import android.content.SharedPreferences
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.example.paypointretailer.Adapter.beneficiariesAdapter
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Request.GetRemitterDetailsRrquest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Response.GiftDetailsData
import com.example.paypointretailer.Model.Response.MoneyTransfer.BankListData
import com.example.paypointretailer.Model.Response.MoneyTransfer.BeneficiariesData
import com.example.paypointretailer.Model.Response.MoneyTransfer.CustomerDetails
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.ViewModel.GetMobileRechargeStateEvent
import com.example.paypointretailer.ViewModel.GetMoneyTransferStateEvent
import com.example.paypointretailer.ViewModel.MoneyTransferViewModel
import com.example.paypointretailer.databinding.ActivityMoneyTransferBinding
import com.example.paypointretailer.databinding.DialogAddNewBenificiallyBinding
import com.example.paypointretailer.databinding.ErrorDialogLayoutBinding
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoneyTransferActivity :
    BaseActivity<ActivityMoneyTransferBinding>(R.layout.activity_money_transfer),
    beneficiariesAdapter.ClikcListnerAdapterInterface {
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: MoneyTransferViewModel by viewModels()
    private lateinit var beneficiariesAdapter: beneficiariesAdapter
    private var PreferredMode = "IMPS"
    var bankListData: MutableList<BankListData>? = mutableListOf()
    var bankStringList: MutableList<String>? = mutableListOf()
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.bc_money_transfer)
        binding.radioGroup.check(R.id.rvIMPS)
        setUpRecycleView()
        getBankList()
    }

    private fun getBankList() {
        viewModel.getBankList(
            GetMoneyTransferStateEvent.getBankList,
            pref.getData()?.access_token,
            pref.getData()?.BusinessId
        )
    }

    private fun setUpRecycleView() {
        beneficiariesAdapter = beneficiariesAdapter(this, this)
        binding.rvList.adapter = beneficiariesAdapter

    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvAdd.setOnClickListener {
            showDialogs()

        }
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rvIMPS -> {
                    PreferredMode = "IMPS"

                    callcustDetailAPI(PreferredMode)
                }

                R.id.rvNEFT -> {
                    PreferredMode = "NEFT"
                    callcustDetailAPI(PreferredMode)
                }
            }
        }


        binding.tvCheck.setOnClickListener {
            AppUtils.hideKeyboard(this@MoneyTransferActivity)
            if (binding.edtNumber.text.isNullOrEmpty()) {
                showErrorMessage(this, "Please enter mobile number")
            } else if (binding.edtNumber.text!!.length < 10) {
                showErrorMessage(this, "Please enter valid mobile number")
            } else {
                callcustDetailAPI(PreferredMode)

            }
        }
        binding.tvCheckServices.setOnClickListener {
            if (binding.edtAmount.text.isNullOrEmpty()) {
                AppUtils.showErrorSnackBar(this, "Please enter amount")
            } else {
                var request: MobilreRechargeRequest = MobilreRechargeRequest(
                    ServiceAcNo = null,
                    Amount = binding.edtAmount.text.toString(),
                    ProductID = null,
                    authorization = pref.getData()?.access_token,
                    businessid = pref.getData()?.BusinessId.toString(),
                    devicecode = pref.getData()?.DeviceCode,
                    icode = pref.getData()?.IdentificationCode,
                    pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())
                )
                viewModel.getServiceCharge(GetMoneyTransferStateEvent.GetServiceCharge, request)
            }
        }
    }

    private fun callcustDetailAPI(preferredMode: String) {
        if (!binding.edtNumber.text.isNullOrEmpty() && binding.edtNumber.text!!.length == 10) {
            if (AppUtils.hasInternet(this)) {
                val request: GetRemitterDetailsRrquest = GetRemitterDetailsRrquest(
                    MobileNo = binding.edtNumber.text.toString(),
                    PreferredMode = preferredMode,
                    ForcedChannelRefresh = "2",
                    Token = pref.getData()?.access_token.toString(),
                    key = pref.getData()?.BusinessId.toString()
                )
                viewModel.getDetails(GetMoneyTransferStateEvent.getDetails, request)

            } else {
                showErrorMessage(
                    this,
                    getString(R.string.please_check_internet_connection)
                )
            }
        }

    }

    private fun showDialogs() {
        val dialog = Dialog(this, R.style.RoundedCornersDialog)
        val mBottomSheetBinding =
            DialogAddNewBenificiallyBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(mBottomSheetBinding.root)
        val window: Window? = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        if (bankStringList?.size != 0 && bankStringList != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bankStringList!!)
            mBottomSheetBinding.edtSelectBank.threshold = 1
            mBottomSheetBinding.edtSelectBank.setAdapter(adapter)
        }

        mBottomSheetBinding.edtSelectBank.setOnTouchListener(View.OnTouchListener { v, event ->
            mBottomSheetBinding.edtSelectBank.showDropDown()
            false
        })

        mBottomSheetBinding.edtSelectBank.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            for (items in bankListData!!.indices)
                if (bankListData!![items].BankName.equals(mBottomSheetBinding.edtSelectBank.text.toString())) {
                    if (!bankListData!![items].DefaultIFSCCode.isNullOrEmpty()) {
                        mBottomSheetBinding.edtIfscCode.setText(bankListData!![items].DefaultIFSCCode.toString())
                    } else {
                        mBottomSheetBinding.edtIfscCode.text?.clear()
                    }

                }
        })
        mBottomSheetBinding.tvCamcel.setOnClickListener {
            dialog.dismiss()
        }
        mBottomSheetBinding.tvadd.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun setUpObservers() {
        viewModel.dataStateDetails.observe(this, Observer { dataState ->
            AppUtils.hideKeyboard(this@MoneyTransferActivity)
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    var customerDetails =
                        GsonBuilder().create().fromJson(userData, CustomerDetails::class.java)
                    setUpResponse(customerDetails)
                    if (customerDetails.statusCode.equals("TXN") || customerDetails.statusCode.equals(
                            "txn"
                        )
                    ) {
                        val request: GetRemitterDetailsRrquest = GetRemitterDetailsRrquest(
                            MobileNo = binding.edtNumber.text.toString(),
                            PreferredMode = null,
                            ForcedChannelRefresh = null,
                            Token = pref.getData()?.access_token.toString(),
                            key = pref.getData()?.BusinessId.toString()
                        )
                        viewModel.getBeneficialList(
                            GetMoneyTransferStateEvent.getBeneficial,
                            request
                        )
                    }
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

        viewModel.dataStateBeneficial.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val data = dataState.data
                    var beneficiariesData =
                        GsonBuilder().create().fromJson(data, BeneficiariesData::class.java)

                    if (beneficiariesData.status.equals("true")) {
                        if (beneficiariesData.beneficiaries?.size != 0) {
                            binding.llStepTwo.visibility = View.VISIBLE
                            binding.llStepThree.visibility = View.VISIBLE
                            beneficiariesAdapter.clear()
                            beneficiariesAdapter.addItems(beneficiariesData.beneficiaries!!)
                            beneficiariesAdapter.notifyDataSetChanged()
                        }

                    }


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

        viewModel.dataStateBankList.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    bankListData = dataState.data.toMutableList()
                    for (item in bankListData!!.indices) {
                        bankStringList?.add(bankListData!![item].BankName!!)
                    }
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

        viewModel.dataStateCheckService.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    var detailsData = GsonBuilder().create()
                        .fromJson(dataState.data, BeneficiariesData::class.java)

                    showErrorDialog(detailsData.message)
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
    }

    private fun showErrorDialog(message: String?) {
        val dialog = Dialog(this, R.style.RoundedCornersDialog)
        val mBottomSheetBinding = ErrorDialogLayoutBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(mBottomSheetBinding.root)
        val window: Window? = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        mBottomSheetBinding.tvTitle.text = "Service Availbale"
        mBottomSheetBinding.tvInfo.text = message
        mBottomSheetBinding.tvOk.setOnClickListener {
            dialog.dismiss()
            onBackPressed()
        }
        dialog.show()
    }

    private fun setUpResponse(customerDetails: CustomerDetails) {
        binding.llDetails.visibility = View.VISIBLE
        binding.tvNameAns.text = customerDetails.remitterName
        binding.tvAddressAns.text = customerDetails.Address
        binding.tvActiveChannelAns.text = customerDetails.ActChannel
        binding.tvConsumeUnitAns.text = "₹" + customerDetails.available_limit.toString()
        binding.tvRemainingUnitAns.text = "₹" + customerDetails.Month_limit.toString()
        if (customerDetails.isVerified == 1) {
            binding.tvMobileOtpStatus.text = "Verfified"
            binding.tvMobileOtpStatus.setTextColor(getColor(R.color.green))
        } else {
            binding.tvMobileOtpStatus.text = "Not Verfified"
            binding.tvMobileOtpStatus.setTextColor(getColor(R.color.red))
        }
    }

    override fun onClickDatalistner(position: Int, text: String) {
        //
    }

}