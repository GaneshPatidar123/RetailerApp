package com.example.paypointretailer.View.Activity.BillPayment

import android.R.attr.password
import android.content.Intent
import android.content.SharedPreferences
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Response.BillPayment.Billerinputparam
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.ViewModel.BillPaymentViewModel
import com.example.paypointretailer.ViewModel.GetBillPaymentStateEvent
import com.example.paypointretailer.databinding.ActivityBillPaymentBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BillPaymentActivity :
    BaseActivity<ActivityBillPaymentBinding>(R.layout.activity_bill_payment) {
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: BillPaymentViewModel by viewModels()
    private var maxKNumber: Int = 0
    private var productId: Int = 0
    private var billMode: MutableList<String>? = mutableListOf()
    var sessionId = ""
    var DomainId = 0
    override fun setUpViews() {
        getDataFromIntents(intent)
    }

    private fun getDataFromIntents(intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getSerializable("Data") != null) {
                var data = bundle.getSerializable("Data") as GetServiceList
                binding.tvServiceName.text = data.Product
                productId = data.ProductId!!
                DomainId = bundle.getInt("DomainId")
            }
            if (bundle.getInt("productID") != null) {
                if (bundle.getInt("productID") != 0) {
                    productId = bundle.getInt("productID")
                    binding.tvServiceName.text = "LIC "
                }
            }
            binding.toolBar.tvTitle.text = bundle.getString("toolBar")

            getPayMode(productId)
        }
    }

    private fun getPayMode(productId: Int?) {
        if (AppUtils.hasInternet(this)) {
            viewModel.getPayMode(GetBillPaymentStateEvent.getPayMode, productId)
        } else {
            showErrorMessage(
                this,
                getString(R.string.please_check_internet_connection)
            )
        }


    }

    override fun setUpListeners() {
        binding.tvChange.setOnClickListener {
            onBackPressed()
        }
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtMobileNUmber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length < 10) {
                    binding.tvErrorNumber.visibility = View.VISIBLE
                } else {
                    binding.tvErrorNumber.visibility = View.GONE
                }
                setUpVisibiliy()
            }
        })

        binding.edtKnumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             /*   if (s!!.length < maxKNumber) {
                    binding.tvErrorKNumber.visibility = View.VISIBLE
                } else {
                    binding.tvErrorKNumber.visibility = View.GONE
                }*/
                setUpVisibiliy()
            }
        })

        binding.edtCustName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length != 0) {
                    binding.btnPayBill.alpha = 1f
                    binding.btnPayBill.isClickable = true
                } else {
                    binding.btnPayBill.alpha = 0.5f
                    binding.btnPayBill.isClickable = false
                }

            }
        })
        binding.tvFetch.setOnClickListener {
            if (AppUtils.hasInternet(this)) {
                var CANo= binding.edtKnumber.text.toString()
                 if(!binding.edtoption1.text.isNullOrEmpty()){
                     CANo = CANo +"~"+binding.edtoption1.text.toString()
                 }

                var request: GetBillCustDetailsRequest = GetBillCustDetailsRequest(
                    ProductAliasID = productId.toString(),
                    CANo = CANo,
                    Opt1 = null,
                    Opt3 = binding.edtMobileNUmber.text.toString(),
                    devicecode = pref.getData()?.DeviceCode,
                    icode = pref.getData()?.IdentificationCode,
                    pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())
                )
                viewModel.fetchCUstDetails(GetBillPaymentStateEvent.custDetails, request)
            } else {
                showErrorMessage(
                    this,
                    getString(R.string.please_check_internet_connection)
                )
            }

        }
        binding.edtChoosePayMode.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.edtChoosePayMode.showDropDown()
            false
        })

        binding.btnPayBill.setOnClickListener {
            AppUtils.hideKeyboard(this@BillPaymentActivity)
            if (binding.tvErrorKNumber.visibility == View.GONE && binding.tvErrorNumber.visibility == View.GONE &&
                !binding.edtKnumber.text.isNullOrEmpty() && !binding.edtMobileNUmber.text.isNullOrEmpty() &&
                !binding.edtCustName.text.isNullOrEmpty() && !binding.edtAmount.text.isNullOrEmpty()
            ) {
                if (binding.edtChoosePayMode.text.toString().equals("Cash Pay")) {
                    var opt1: String? = null
                    var opt2: String? = null
                    var billType: String? = null
                    if (binding.edtoption1.visibility == View.VISIBLE) {
                        opt1 = binding.edtoption1.text.toString()
                    } else {
                        opt1 = null
                    }
                    if (binding.edtoption2.visibility == View.VISIBLE) {
                        opt2 = binding.edtoption2.text.toString()
                    } else {
                        opt2 = null
                    }
                    if (productId == 116) {
                        billType = "S"
                    } else {
                        billType = "0"
                    }
                    if (AppUtils.hasInternet(this)) {

                        if (binding.edtChoosePayMode.text.toString().equals("Cheque")) {
                            var request: MobilreRechargeRequest = MobilreRechargeRequest(
                                ServiceAcNo = binding.edtKnumber.text.toString(),
                                Amount = binding.edtAmount.text.toString(),
                                ProductID = productId,
                                authorization = pref.getData()?.access_token,
                                businessid = pref.getData()?.BusinessId.toString(),
                                devicecode = pref.getData()?.DeviceCode,
                                icode = pref.getData()?.IdentificationCode,
                                pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString()),
                                SessionID = sessionId,
                                CustomerMobileNo = binding.edtMobileNUmber.text.toString(),
                                Opt1 = opt1,
                                Opt2 = opt2,
                                BillType = billType,
                                PayMode = "2",
                            )
                            viewModel.billPaymentNow(
                                GetBillPaymentStateEvent.billPaymentNow,
                                request
                            )
                        } else {
                            var request: MobilreRechargeRequest = MobilreRechargeRequest(
                                ServiceAcNo = binding.edtKnumber.text.toString(),
                                Amount = binding.edtAmount.text.toString(),
                                ProductID = productId,
                                authorization = pref.getData()?.access_token,
                                businessid = pref.getData()?.BusinessId.toString(),
                                devicecode = pref.getData()?.DeviceCode,
                                icode = pref.getData()?.IdentificationCode,
                                pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString()),
                                SessionID = sessionId,
                                CustomerMobileNo = binding.edtMobileNUmber.text.toString(),
                                Opt1 = opt1,
                                Opt2 = opt2,
                                BillType = billType,
                                PayMode = "1",
                            )
                            viewModel.billPaymentNow(
                                GetBillPaymentStateEvent.billPaymentNow,
                                request
                            )
                        }

                    } else {
                        showErrorMessage(
                            this,
                            getString(R.string.please_check_internet_connection)
                        )
                    }
                } else {
                    AppUtils.showErrorSnackBar(this, "Please select Payment mode")
                }
            } else {
                if (!binding.edtMobileNUmber.text.isNullOrEmpty() && !binding.edtMobileNUmber.text.isNullOrEmpty()) {
                    AppUtils.showErrorSnackBar(this, "Please enter customer Name")
                }
            }
        }
    }

    private fun setUpVisibiliy() {
        if ( binding.tvErrorNumber.visibility == View.GONE && !binding.edtKnumber.text.isNullOrEmpty() && !binding.edtMobileNUmber.text.isNullOrEmpty()) {
            binding.tvFetch.alpha = 1f
            binding.tvFetch.isClickable = true
        } else {
            binding.tvFetch.alpha = 0.5f
            binding.tvFetch.isClickable = false
            binding.btnPayBill.alpha = 0.5f
            binding.btnPayBill.isClickable = false
        }
    }

    override fun setUpObservers() {
        viewModel.dataStatePayMode.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data

                        binding.tvKNumber.setText(userData.CALabel.toString())
                    if (userData.CALabelDataType == "NUMERIC") {
                        binding.edtKnumber.setInputType(InputType.TYPE_CLASS_NUMBER)
                    } else {
                        binding.edtKnumber.setInputType(InputType.TYPE_CLASS_TEXT)
                    }
                    if (userData.OPT1DataType == "NUMERIC" && !userData.OPT1DataType.isNullOrEmpty()) {
                        binding.edtoption1.setInputType(InputType.TYPE_CLASS_NUMBER)
                    } else {
                        binding.edtoption1.setInputType(InputType.TYPE_CLASS_TEXT)
                    }


                    if (userData.AggregatorId == 58 || userData.AggregatorId == 70) {
                        val jsonArray: JsonArray =
                            Gson().fromJson(userData.Billerinputparam, JsonArray::class.java)
                        if (jsonArray.size() > 0) {
                            var size = jsonArray.size()
                            if (size == 2) {
                                binding.tvoption1.visibility = View.VISIBLE
                                binding.edtoption1.visibility = View.VISIBLE
                                val firstElement: JsonElement = jsonArray[0]
                                val data: Billerinputparam =
                                    Gson().fromJson(firstElement, Billerinputparam::class.java)
                                val inputFilters =
                                    arrayOf<InputFilter>(InputFilter.LengthFilter(data.maxLength!!))
                                binding.edtKnumber.filters = inputFilters
                                binding.tvKNumber.text = data.paramName
                                maxKNumber = data.maxLength!!


                                val secondElement: JsonElement = jsonArray[1]
                                val seconddata: Billerinputparam =
                                    Gson().fromJson(secondElement, Billerinputparam::class.java)
                                val secondinputFilters =
                                    arrayOf<InputFilter>(InputFilter.LengthFilter(seconddata.maxLength!!))
                                binding.edtoption1.filters = secondinputFilters
                                binding.tvoption1.text = seconddata.paramName
                            } else if (size == 3) {

                                val firstElement: JsonElement = jsonArray[0]
                                val data: Billerinputparam =
                                    Gson().fromJson(firstElement, Billerinputparam::class.java)
                                val inputFilters =
                                    arrayOf<InputFilter>(InputFilter.LengthFilter(data.maxLength!!))
                                binding.edtKnumber.filters = inputFilters
                                binding.tvKNumber.text = data.paramName
                                maxKNumber = data.maxLength!!

                                binding.tvoption1.visibility = View.VISIBLE
                                binding.edtoption1.visibility = View.VISIBLE
                                val secondElement: JsonElement = jsonArray[1]
                                val seconddata: Billerinputparam =
                                    Gson().fromJson(secondElement, Billerinputparam::class.java)
                                val secondinputFilters =
                                    arrayOf<InputFilter>(InputFilter.LengthFilter(seconddata.maxLength!!))
                                binding.edtoption1.filters = secondinputFilters
                                binding.tvoption1.text = seconddata.paramName

                                binding.tvoption2.visibility = View.VISIBLE
                                binding.edtoption2.visibility = View.VISIBLE
                                val firstElementnew: JsonElement = jsonArray[2]
                                val datanew: Billerinputparam =
                                    Gson().fromJson(firstElementnew, Billerinputparam::class.java)
                                val inputFiltersnew =
                                    arrayOf<InputFilter>(InputFilter.LengthFilter(datanew.maxLength!!))
                                binding.edtoption2.filters = inputFiltersnew
                                binding.tvoption2.text = datanew.paramName

                            } else {
                                val firstElement: JsonElement = jsonArray[0]
                                val data: Billerinputparam =
                                    Gson().fromJson(firstElement, Billerinputparam::class.java)
                                val inputFilters =
                                    arrayOf<InputFilter>(InputFilter.LengthFilter(data.maxLength!!))
                                binding.edtKnumber.filters = inputFilters
                                //      binding.tvKNumber.text = data.paramName
                                maxKNumber = data.maxLength!!

                            }

                        } else {

                            println("JSON array is empty")
                        }
                    } else {
                        var type = userData.CALabelDataType
                        binding.tvKNumber.setText(userData.CALabel.toString())
                        if (userData.CALabelRegex !== null && !userData.CALabelRegex.equals("")) {
                            if (type == "NUMERIC") {
                                binding.edtKnumber.setInputType(InputType.TYPE_CLASS_NUMBER)
                            } else {
                                binding.edtKnumber.setInputType(InputType.TYPE_CLASS_TEXT)
                            }
                        }

                        if ((userData.OPT1 != null && userData.OPT1 !== null && !userData.OPT1.equals(
                                ""
                            )) || (userData.OPT1Text != null && userData.OPT1Text !== null && !userData.OPT1Text.equals(
                                ""
                            ))
                        ) {
                            if (productId == 227) {
                                binding.tvoption1.visibility = View.VISIBLE
                                binding.edtoption1.visibility = View.VISIBLE
                                binding.tvoption1.text = userData.CALabelText
                            } else {
                                binding.tvoption1.visibility = View.VISIBLE
                                binding.edtoption1.visibility = View.VISIBLE
                                binding.tvoption1.text = userData.OPT1Text
                                if (userData.OPT1Regex !== null && !userData.OPT1Regex.equals("")) {
                                    /* this.frmPayBill.get('txtOtp1').clearValidators();
                                     this.frmPayBill.get('txtOtp1').setValidators(
                                         Validators.compose(
                                             [Validators.required, Validators.pattern(fcOpt.OPT1Regex)]
                                         )
                                     );*/
                                }
                            }
                            if (DomainId == 25 && (productId == 289 || productId == 301)) {
                                binding.tvoption1.text = userData.OPT1
                                binding.tvKNumber.text = userData.CALabel
                                binding.tvoption1.visibility = View.VISIBLE
                                binding.edtoption1.visibility = View.VISIBLE
                            } else {
                                binding.tvoption1.text = userData.OPT1
                                binding.tvKNumber.text = userData.CALabel
                                binding.tvoption1.visibility = View.VISIBLE
                                binding.edtoption1.visibility = View.VISIBLE
                            }
                        } else {
                            binding.tvoption1.visibility = View.GONE
                            binding.edtoption1.visibility = View.GONE
                        }

                        /* if ((userData.OPT2 != null && userData.OPT2 !== null && !userData.OPT2 .equals(""))){
                             binding.tvoption2.visibility = View.VISIBLE
                             binding.edtoption2.visibility = View.VISIBLE
                         }else{
                             binding.tvoption2.visibility = View.GONE
                             binding.edtoption2.visibility = View.GONE
                         }*/
                    }

                    var PaymodeCollection = userData.PaymodeCollection
                    for (item in PaymodeCollection!!.indices) {
                        billMode?.add(PaymodeCollection[item].Paymode)
                    }
                    binding.tvChoosePayMode.alpha = 0.4f
                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, billMode!!)
                    binding.edtChoosePayMode.threshold = 1
                    binding.edtChoosePayMode.setAdapter(adapter)

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

        viewModel.dataStateCustDetails.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()

                    if (dataState.data.Status != 3) {
                        val userData = dataState.data
                        if (dataState.data.SessionId != null) {
                            sessionId = dataState.data.SessionId!!
                        } else {
                            sessionId = "0"
                        }
                        binding.llCustDetails.visibility = View.VISIBLE
                        binding.edtCustName.setText(userData.CustomerName.toString())
                        binding.edtAmount.setText(userData.BillAmount.toString())
                    } else {
                        showErrorMessage(this, dataState.data.ErrorDesc!!)
                        binding.llCustDetails.visibility = View.GONE
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
                        showErrorMessage(this, "Bill payment successfully submitted")
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
}