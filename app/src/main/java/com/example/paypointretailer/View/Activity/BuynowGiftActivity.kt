package com.example.paypointretailer.View.Activity

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Adapter.AdapterBillPayment
import com.example.paypointretailer.Adapter.AdapterTermAndCondition
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Extention.showSuccessMessage
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.RequestBuyGift
import com.example.paypointretailer.Model.Request.RequestCheckMobileDeviceUsed
import com.example.paypointretailer.Model.Response.GiftDetailsData
import com.example.paypointretailer.Model.Response.GiftDetailsResponse
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppConstant
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.CustomSizeBottomSheetDialogFragment
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.Utils.StaticData
import com.example.paypointretailer.Utils.Validation
import com.example.paypointretailer.ViewModel.BuyGiftDetailsStateEvent
import com.example.paypointretailer.ViewModel.BuyGiftViewModel
import com.example.paypointretailer.ViewModel.GetLoginDataStateEvent
import com.example.paypointretailer.databinding.ActivityBuynowGiftBinding
import com.example.paypointretailer.databinding.ErrorDialogLayoutBinding
import com.example.paypointretailer.databinding.ForgotPasswordLayoutBinding
import com.example.paypointretailer.databinding.LayoutViewMoreBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BuynowGiftActivity : BaseActivity<ActivityBuynowGiftBinding>(R.layout.activity_buynow_gift) {
    var isFrom = ""

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val buyGiftViewModel: BuyGiftViewModel by viewModels()
    private var billMode: MutableList<String>? = mutableListOf()
    private var detailsDatas = GiftDetailsData()
    private lateinit var bottomSheetDialog: Dialog
    private lateinit var adapter: AdapterTermAndCondition
    override fun setUpViews() {
        binding.btnBuy.alpha = 0.5f
        binding.btnBuy.isClickable = false
        getBndleData(intent)
    }

    private fun getBndleData(intent: Intent?) {
        var bundle = intent?.extras
        if (bundle != null) {
            isFrom = bundle.getString(AppConstant.IS_FROM)!!
            var data = bundle.getSerializable(AppConstant.DATA) as ListGiftVoucherResponse
            binding.toolBar.tvTitle.text = data.Name

            val imageUrl = "https://paypointindia.co.in/images/gv/" + data.Key + ".png"
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ivImage)
            if (AppUtils.hasInternet(this)) {
                if (isFrom.equals("Gift")) {
                    buyGiftViewModel.getGiftDetails(
                        BuyGiftDetailsStateEvent.getGIftDetails,
                        pref.getData()?.access_token!!, data.Key!!
                    )
                } else {
                    buyGiftViewModel.getOttDetails(
                        BuyGiftDetailsStateEvent.getGIftDetails,
                        pref.getData()?.access_token!!, data.Key!!
                    )
                }
            } else {
                showErrorMessage(
                    this,
                    getString(R.string.please_check_internet_connection)
                )
            }
        }
    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.edtSelectAmount.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.edtSelectAmount.showDropDown()
            false
        })
        binding.tvMore.setOnClickListener {
            showBottomShetDialog("More")
        }
        binding.tvTerm.setOnClickListener {
            showBottomShetDialog("Term")
        }
        binding.btnBuy.setOnClickListener {
            setupVisiblity()
        }
        binding.edtSelectAmount.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            setupVisiblity()
        })
        binding.edtAmount.doOnTextChanged { text, start, before, count ->
            setupVisiblity()
        }
        binding.edtSenderMobile.doOnTextChanged { text, start, before, count ->
            setupVisiblity()
        }
        binding.edtSenderEmail.doOnTextChanged { text, start, before, count ->
            setupVisiblity()
        }
        binding.edtReceiveMobile.doOnTextChanged { text, start, before, count ->
            setupVisiblity()
        }
        binding.edtReceiveEmail.doOnTextChanged { text, start, before, count ->
            setupVisiblity()
        }
        binding.btnBuy.setOnClickListener {
            if (binding.llChoose.visibility == View.VISIBLE && binding.edtSelectAmount.text.toString()
                    .equals(
                        "Please select Amount"
                    )
            ) {
                showErrorMessage(this, "Please select Amount")
            } else if (binding.llEnterAmount.visibility == View.VISIBLE && binding.edtAmount.text.isNullOrEmpty()) {
                showErrorMessage(this, "Please Enter Amount")
            } else {
                if (!binding.edtSenderMobile.text.isNullOrEmpty()) {
                    var ValueOfVoucher = ""
                    if (binding.llChoose.visibility == View.VISIBLE) {
                        ValueOfVoucher = binding.edtSelectAmount.text.toString()
                    } else {
                        ValueOfVoucher = binding.edtAmount.text.toString()
                    }

                    if (AppUtils.hasInternet(this)) {
                        val request: RequestBuyGift = RequestBuyGift(
                            SenderEmailID = binding.edtSenderEmail.text.toString(),
                            ReceiverEmailID = binding.edtReceiveEmail.text.toString(),
                            SenderMobileNo = binding.edtSenderMobile.text.toString(),
                            ReceiverMobileNo = binding.edtReceiveMobile.text.toString(),
                            ValueOfVoucher = ValueOfVoucher,
                            ProductID = detailsDatas.id!!.toInt(),
                            SKU = detailsDatas.sku,
                            toekn = pref.getData()?.access_token!!,
                            devicecode = pref.getData()?.DeviceCode,
                            icode = pref.getData()?.IdentificationCode,
                            pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString()),
                            key = pref.getData()?.BusinessId!!,
                        )
                        buyGiftViewModel.callBuyNow(
                            BuyGiftDetailsStateEvent.BuyNow,
                            request,
                            isFrom
                        )
                    } else {
                        showErrorMessage(
                            this,
                            getString(R.string.please_check_internet_connection)
                        )
                    }
                }
            }
        }

    }

    private fun setupVisiblity() {
        if (/*binding.llEnterAmount.visibility == View.VISIBLE && !binding.edtAmount.text.isNullOrEmpty() &&*/
            !binding.edtSenderMobile.text.isNullOrEmpty() && binding.edtSenderMobile.text!!.length == 10 &&
            !binding.edtSenderEmail.text.isNullOrEmpty() && Validation.checkEmail(binding.edtSenderEmail.text.toString()) &&
            !binding.edtReceiveMobile.text.isNullOrEmpty() && binding.edtReceiveMobile.text!!.length == 10 &&
            !binding.edtReceiveEmail.text.isNullOrEmpty() && Validation.checkEmail(
                binding.edtReceiveEmail.text.toString()
            )
        ) {
            binding.btnBuy.alpha = 1f
            binding.btnBuy.isClickable = true
        } else {
            binding.btnBuy.alpha = 0.5f
            binding.btnBuy.isClickable = false
        }
    }

    private fun showBottomShetDialog(isfrom: String) {
        bottomSheetDialog = BottomSheetDialog(this)
        val mBottomSheetBinding = LayoutViewMoreBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(mBottomSheetBinding.root)

        mBottomSheetBinding.ivClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        if (isfrom.equals("More")) {
            mBottomSheetBinding.tvName.text = detailsDatas.name
            mBottomSheetBinding.tvData.text = detailsDatas.description
            mBottomSheetBinding.tvData.visibility = View.VISIBLE
            mBottomSheetBinding.rvList.visibility = View.GONE
        } else {
            mBottomSheetBinding.tvName.text = "Terms and Conditions"
            val fruitList: List<String> = detailsDatas.tnc_mail!!.split("<br/>")
            mBottomSheetBinding.tvData.visibility = View.GONE
            mBottomSheetBinding.rvList.visibility = View.VISIBLE

            adapter = AdapterTermAndCondition(this)
            mBottomSheetBinding.rvList.adapter = adapter
            adapter.addItems(fruitList.toList())
            adapter.notifyDataSetChanged()

        }
        bottomSheetDialog.show()
    }


    override fun setUpObservers() {
        buyGiftViewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    var response =
                        GsonBuilder().create()
                            .fromJson(dataState.data, GiftDetailsResponse::class.java)
                    if (response.Status.equals("Success")) {
                        var detailsData = GsonBuilder().create()
                            .fromJson(response.Msg, GiftDetailsData::class.java)
                        detailsDatas = detailsData
                        if (detailsData.custom_denominations.isNullOrEmpty()) {
                            binding.llEnterAmount.visibility = View.VISIBLE
                            binding.llChoose.visibility = View.GONE
                        } else {
                            binding.llEnterAmount.visibility = View.GONE
                            binding.llChoose.visibility = View.VISIBLE
                            billMode?.clear()
                            val fruitList: List<String> =
                                detailsData.custom_denominations!!.split(",")
                            billMode!!.addAll(fruitList)
                            if (billMode!!.size != 0 && billMode != null) {
                                val adapter =
                                    ArrayAdapter(
                                        this,
                                        android.R.layout.simple_dropdown_item_1line,
                                        billMode!!
                                    )
                                binding.edtSelectAmount.threshold = 1
                                binding.edtSelectAmount.setAdapter(adapter)
                            }
                        }
                        binding.tvMinMax.text =
                            "Min ₹ ${detailsData.min_custom_price} - Max ₹ ${detailsData.max_custom_price}"
                    } else {
                        showErrorDialog(response.ErrorDes)

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

        buyGiftViewModel.dataStateOTT.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    detailsDatas = dataState.data
                    if (dataState.data.custom_denominations.isNullOrEmpty()) {
                        binding.llEnterAmount.visibility = View.VISIBLE
                        binding.llChoose.visibility = View.GONE
                    } else {
                        binding.llEnterAmount.visibility = View.GONE
                        binding.llChoose.visibility = View.VISIBLE
                        billMode?.clear()
                        val fruitList: List<String> =
                            dataState.data.custom_denominations!!.split(",")
                        billMode!!.addAll(fruitList)
                        if (billMode!!.size != 0 && billMode != null) {
                            val adapter =
                                ArrayAdapter(
                                    this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    billMode!!
                                )
                            binding.edtSelectAmount.threshold = 1
                            binding.edtSelectAmount.setAdapter(adapter)
                        }
                    }
                    binding.tvMinMax.text =
                        "Min ₹ ${dataState.data.min_custom_price} - Max ₹ ${dataState.data.max_custom_price}"

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
        buyGiftViewModel.dataStateBuyNow.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    var response =
                        GsonBuilder().create()
                            .fromJson(dataState.data, SignUpResponse::class.java)
                    if (response.Status.equals("Success")) {
                        showSuccessMessage(this, "subscription Buy successfully")
                        finish()
                    } else {
                        showErrorDialog(response.ErrorDes)
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
    }

    private fun showErrorDialog(errorDes: String?) {
        val dialog = Dialog(this, R.style.RoundedCornersDialog)
        val mBottomSheetBinding = ErrorDialogLayoutBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(mBottomSheetBinding.root)
        val window: Window? = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        mBottomSheetBinding.tvTitle.text = "Gift Voucher"
        mBottomSheetBinding.tvInfo.text = errorDes
        mBottomSheetBinding.tvOk.setOnClickListener {
            dialog.dismiss()
            onBackPressed()
        }
        dialog.show()
    }
}