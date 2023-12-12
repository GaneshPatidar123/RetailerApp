package com.example.paypointretailer.View.Fragement


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.paypointretailer.Adapter.AdapterBillPayment
import com.example.paypointretailer.Adapter.AdapterBooking
import com.example.paypointretailer.Adapter.AdapterFavourites
import com.example.paypointretailer.Adapter.AdapterMoneyTransfer
import com.example.paypointretailer.Adapter.AdapterOnlineShopping
import com.example.paypointretailer.Adapter.AdapterRecharge
import com.example.paypointretailer.Api.APIInterfaceNew
import com.example.paypointretailer.Base.BaseFragment
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Extention.showSuccessMessage
import com.example.paypointretailer.MainActivity
import com.example.paypointretailer.Model.Favourite
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MyFavouritesList
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppConstant
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.View.Activity.BillPayment.BillPaymentActivity
import com.example.paypointretailer.View.Activity.BillPayment.ElectricityBillPaymentActivity
import com.example.paypointretailer.View.Activity.GiftVoucherActivity
import com.example.paypointretailer.View.Activity.MoneyTransfer.AepsActivity
import com.example.paypointretailer.View.Activity.MoneyTransfer.AepsServicesActivity
import com.example.paypointretailer.View.Activity.MoneyTransfer.MoneyTransferActivity
import com.example.paypointretailer.View.Activity.ProfileActivity
import com.example.paypointretailer.View.Activity.Recharge.DthRechargeActivity
import com.example.paypointretailer.View.Activity.Recharge.MobileRechargeActivity
import com.example.paypointretailer.View.Activity.WebViewActivity
import com.example.paypointretailer.ViewModel.GetMainDataStateEvent
import com.example.paypointretailer.ViewModel.MainViewModel
import com.example.paypointretailer.databinding.LayoutDashboardBinding
import com.example.paypointretailer.databinding.LayoutFavouriteDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@AndroidEntryPoint
class DashBoardFragment : BaseFragment<LayoutDashboardBinding>(R.layout.layout_dashboard),
    AdapterRecharge.ClikcListnerAdapterInterface,
    AdapterFavourites.ClikcListnerAdapterInterface,
    AdapterOnlineShopping.ClikcListnerAdapterInterface,
    AdapterMoneyTransfer.ClikcListnerAdapterInterface,
    AdapterBillPayment.ClikcListnerAdapterInterface, AdapterBooking.ClikcListnerAdapterInterface {

    private lateinit var adapterRecharge: AdapterRecharge
    private lateinit var adapterBillPayment: AdapterBillPayment
    private lateinit var adapterInsurence: AdapterRecharge
    private lateinit var adapterBooking: AdapterBooking
    private lateinit var adapterFavourites: AdapterFavourites
    private lateinit var adapterMoneyTransfer: AdapterMoneyTransfer
    private lateinit var adapterOnlineShopping: AdapterOnlineShopping
    private var list: MutableList<Favourite>? = mutableListOf()

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private var mainViewModel: MainViewModel? = null
    var isClick = ""
    var BASE_URL_FAVOURITES = "https://v2022.paypointindia.co.in/Common/"
    override fun setUpViews() {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        callDashBoardApi()

    }

    private fun getMyFavouriteList() {

        showProgressDialog()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_FAVOURITES) // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(APIInterfaceNew::class.java)
        val contentType = "application/json; charset=utf-8"
        val requestBody =
            getRequestBody().toRequestBody(contentType.toMediaTypeOrNull())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                var apiResponse: Response<List<MyFavouritesList>>? = null
                apiResponse = apiService.getFavouritesList(getHeaderCustDetail(), requestBody)
                if (apiResponse.isSuccessful) {
                    hideProgressDialog()
                    val body = apiResponse.body()
                    // Handle the successful response
                    println(apiResponse.raw().body?.string())
                    val result = apiResponse.raw().body?.string()
                    Log.d("GANESH", "success: " + result)
                    // Process the result
                } else {
                    hideProgressDialog()
                    // Handle the error response
                    val errorBody = apiResponse.errorBody()
                    // Process the error body
                    Log.d("GANESH", "error: " + apiResponse.errorBody())
                }
            }

        } catch (e: Exception) {
            // Handle the exception
            hideProgressDialog()
            e.printStackTrace()
            Log.d("GANESH", "try Catch: " + e.toString())
        }


    }

    private fun callDashBoardApi() {
        if (AppUtils.hasInternet(requireActivity())) {
            val request: VerifiedOtpRequest = VerifiedOtpRequest(
                Mobile = pref.getData()?.IdentificationCode.toString(),
                OTP = null,
                Serial = Build.SERIAL,
                Uuid = AppUtils.getUUID(sharedPrefs),
                key = pref.getData()?.BusinessId,
                token = pref.getData()?.access_token
            )
            mainViewModel?.getMainData(GetMainDataStateEvent.mainData, request)
        } else {
            showErrorMessage(
                requireActivity(),
                getString(R.string.please_check_internet_connection)
            )
        }
    }

    override fun setUpListeners() {
        binding.toolBar.ivUser.setOnClickListener {
            (activity as MainActivity).launch(ProfileActivity::class.java)
        }
        binding.llAddMore.setOnClickListener {
            showFavouritesDialog()
            // (activity as MainActivity).launch(AddFavouriteActivity::class.java)
        }
        binding.ivRefresh.setOnClickListener {
            callDashBoardApi()
        }
        binding.btnAddlimit.setOnClickListener {
            // (activity as MainActivity).launch(AddLimitActivity::class.java)
            (activity as MainActivity).setCurrFragment(LoadMoneyFragment(), "Limit")
        }
    }

    override fun setUpObservers() {
        mainViewModel?.dataState!!.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    //  getMyFavouriteList()
                    val userData = dataState.data

                    pref.saveInitialData(userData)
                    binding.tvWalletAmount.text =
                        "₹" + userData.BalanceEntity.ClosingBalance.toString()
                    val solution: Double =
                        String.format("%.2f", userData.AepsBalanceEntity.CurrentBalance).toDouble()
                    binding.tvAEPSWalletAmount.text = "₹" + solution.toString()

                }

                is Resource.Error -> {
                    hideProgressDialog()
                    showErrorMessage(requireActivity(), dataState.message)
                    //  notifyUser(dataState.message)
                }

                is Resource.Loading -> {
                    showProgressDialog()
                }
            }
        })

        mainViewModel?.dataStateeURL!!.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    if (isClick.equals("Flight")) {
                        isClick = ""
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(userData.Msg))
                        startActivity(browserIntent)

                    } else {
                        if (!isClick.equals("")) {
                            if (userData.Status.equals("success")) {
                                var bundle = Bundle()
                                bundle.putString(AppConstant.IS_FROM, userData.Msg)
                                bundle.putString("isClick", isClick)
                                (activity as MainActivity).launch(
                                    requireActivity(),
                                    WebViewActivity::class.java,
                                    bundle
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressDialog()
                    showErrorMessage(requireActivity(), dataState.message)
                    //  notifyUser(dataState.message)
                }

                is Resource.Loading -> {
                    showProgressDialog()
                }
            }
        })
    }

    private fun getHeaderCustDetail(
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"

        return headerMap
    }

    private fun getRequestBody(): String {
        val body = JSONObject()
        body.put("isrefresh", 1)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

    private fun showFavouritesDialog() {
        val bottomSheetDialog =
            BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogThemeNew)
        val mBottomSheetBinding = LayoutFavouriteDialogBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setContentView(mBottomSheetBinding.root)
        bottomSheetDialog.setCancelable(true)
        adapterFavourites = AdapterFavourites(this, this)
        addStaticData()
        mBottomSheetBinding.rvFavourites.adapter = adapterFavourites
        adapterFavourites.addItems(list!!)
        adapterFavourites.notifyDataSetChanged()
        mBottomSheetBinding.btnSave.setOnClickListener {
            var selecetedlist: MutableList<Favourite>? = mutableListOf()
            for (i in adapterFavourites.getListItems()!!.indices) {
                if (adapterFavourites.getListItems()!![i].isSelected) {
                    selecetedlist?.add(adapterFavourites.getListItems()!![i])
                }
            }
            Log.d("GANESH", "selecetedlist: " + selecetedlist?.size)
            bottomSheetDialog.dismiss()
        }

        mBottomSheetBinding.ivClose.setOnClickListener {
            mBottomSheetBinding.ivClose.visibility = View.GONE
            adapterFavourites.clear()
            adapterFavourites.addItems(list!!)
            adapterFavourites.notifyDataSetChanged()
            mBottomSheetBinding.search.text?.clear()
            mBottomSheetBinding.search.clearFocus()
            // AppUtils.hideKeyboard(requireActivity())
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mBottomSheetBinding.search.windowToken, 0)
        }
        mBottomSheetBinding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s)
                if (s!!.length != 0) {
                    mBottomSheetBinding.ivClose.visibility = View.VISIBLE
                } else {
                    mBottomSheetBinding.ivClose.visibility = View.GONE
                }
            }
        })
        bottomSheetDialog.show()
    }

    private fun filter(text: CharSequence?) {
        val filteredlist: ArrayList<Favourite> = ArrayList()

        // running a for loop to compare elements.
        for (item in list!!) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name!!.toLowerCase()
                    .contains(text.toString().toLowerCase()) || item.name!!.toLowerCase()
                    .contains(text.toString().toUpperCase())
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        adapterFavourites.clear()
        if (filteredlist.size != 0) {
            adapterFavourites.addItems(filteredlist)
            adapterFavourites.notifyDataSetChanged()
        } else {
            adapterFavourites.addItems(list!!)
            adapterFavourites.notifyDataSetChanged()
        }
    }

    private fun addStaticData() {
        list?.clear()
        list?.add(Favourite(false, "Mobile"))
        list?.add(Favourite(false, "Broadband"))
        list?.add(Favourite(false, "DTH"))
        list?.add(Favourite(false, "Landline"))
        list?.add(Favourite(false, "Datacard"))
        list?.add(Favourite(false, "Municipal Taxes"))
        list?.add(Favourite(false, "Electricity"))
        list?.add(Favourite(false, "Broadband"))
        list?.add(Favourite(false, "Piped Gas"))
        list?.add(Favourite(false, "Housing Society"))
        list?.add(Favourite(false, "Gas Cylinder"))
        list?.add(Favourite(false, "Education Fees"))
        list?.add(Favourite(false, "Water"))
        list?.add(Favourite(false, "Hospital"))
        list?.add(Favourite(false, "Credit Card"))
        list?.add(Favourite(false, "Cable TV"))
        list?.add(Favourite(false, "Term life"))
        list?.add(Favourite(false, "Car"))
        list?.add(Favourite(false, "Metro"))
        list?.add(Favourite(false, "Bus"))
        list?.add(Favourite(false, "Flight"))
        list?.add(Favourite(false, "Train"))
        list?.add(Favourite(false, "Hotel"))

        /*  val rechargeList = arrayOf("Mobile", "Broadband", "DTH", "Landline","Datacard", "Municipal Taxes", "Electricity", "Broadband","Piped Gas","Housing Society",
              "Gas Cylinder","Education Fees","Water","Hospital","Credit Card","Cable TV","Health", "Term life", "Car","Metro", "Bus", "Flight","Train","Hotel")*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }

    private fun setupData() {
        val rechargeList = arrayOf("Mobile", "DTH")
        adapterRecharge = AdapterRecharge(this, this, "richange")
        binding.rvRecharge.adapter = adapterRecharge
        adapterRecharge.addItems(rechargeList.toList())
        adapterRecharge.notifyDataSetChanged()

        val billPaymentList = arrayOf(
            "Telecom",
            "Municipal Taxes",
            "Electricity",
            "Broadband",
            "Piped Gas",
            "Housing Society",
            "Gas Cylinder",
            "Education Fees",
            "Water",
            "Hospital",
            "Credit Card",
            "Cable TV",
            "Landline",
            "LIC",
            "Municipal Service"
        )
        adapterBillPayment = AdapterBillPayment(this, this)
        binding.rvBillPayment.adapter = adapterBillPayment
        adapterBillPayment.addItems(billPaymentList.toList())
        adapterBillPayment.notifyDataSetChanged()

        val InsurenceList = arrayOf("Health", "Term life", "Car")
        adapterInsurence = AdapterRecharge(this, this, "Insurence")
        binding.rvInsurance.adapter = adapterInsurence
        adapterInsurence.addItems(InsurenceList.toList())
        adapterInsurence.notifyDataSetChanged()


        val bookingList = arrayOf("Train", "Flight", "Bus", "Hotel")
        adapterBooking = AdapterBooking(this, this)
        binding.rvBooking.adapter = adapterBooking
        adapterBooking.addItems(bookingList.toList())
        adapterBooking.notifyDataSetChanged()

        val onlineShopping = arrayOf("Gift Voucher", "OTT Subscription", "Jio Savan")
        adapterOnlineShopping = AdapterOnlineShopping(this, this)
        binding.rvOnLine.adapter = adapterOnlineShopping
        adapterOnlineShopping.addItems(onlineShopping.toList())
        adapterOnlineShopping.notifyDataSetChanged()


        val moneyTransfer = arrayOf("AEPS", "Aadhar Pay", "Money Transfer")
        adapterMoneyTransfer = AdapterMoneyTransfer(this, this)
        binding.rvMoney.adapter = adapterMoneyTransfer
        adapterMoneyTransfer.addItems(moneyTransfer.toList())
        adapterMoneyTransfer.notifyDataSetChanged()

    }

    override fun onClicklistner(position: Int, value: String) {
        showSuccessMessage(requireActivity(), "click on $value")
    }

    override fun onBillPaymentlistner(position: Int, text: String) {
        val billPaymentList = arrayOf(
            "Mobile", "Municipal Taxes", "Electricity", "Broadband", "Piped Gas", "Housing Society",
            "Gas Cylinder", "Education Fees", "Water", "Hospital", "Credit Card", "Cable TV"
        )
        var bundle = Bundle()
        if (text.equals("Electricity")) {
            bundle.putString(AppConstant.IS_FROM, "Electricity")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Municipal Taxes")) {
            bundle.putString(AppConstant.IS_FROM, "Municipal Taxes")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Broadband")) {
            bundle.putString(AppConstant.IS_FROM, "Broadband")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Housing Society")) {
            bundle.putString(AppConstant.IS_FROM, "Housing Society")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Water")) {
            bundle.putString(AppConstant.IS_FROM, "Water")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Hospital")) {
            bundle.putString(AppConstant.IS_FROM, "Hospital")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Piped Gas")) {
            bundle.putString(AppConstant.IS_FROM, "Piped Gas")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Gas Cylinder")) {
            bundle.putString(AppConstant.IS_FROM, "Gas Cylinder")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Education Fees")) {
            bundle.putString(AppConstant.IS_FROM, "Education Fees")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Municipal Service")) {
            bundle.putString(AppConstant.IS_FROM, "Municipal Service")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("LIC")) {
            var bundle = Bundle()
            bundle.putSerializable("productID", 1364)
            bundle.putString("toolBar", "LIC Premimum Payment")
            (activity as MainActivity).launch(
                requireActivity(),
                BillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Landline")) {
            var bundle = Bundle()
            bundle.putString(AppConstant.IS_FROM, "Landline")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else if (text.equals("Cable TV")) {
            var bundle = Bundle()
            bundle.putString(AppConstant.IS_FROM, "Cable Tv")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        }

    }

    override fun onBookinglistner(position: Int, text: String) {
        if (text.equals("Hotel")) {
            isClick = "Hotel"
            var request: GetBillCustDetailsRequest = GetBillCustDetailsRequest(
                ProductAliasID = null,
                CANo = "",
                Opt1 = pref.getData()?.access_token?.toString(),
                Opt3 = pref.getData()?.BusinessId?.toString(),
                devicecode = pref.getData()?.DeviceCode,
                icode = pref.getData()?.IdentificationCode,
                pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())

            )
            mainViewModel?.getHotelURl(GetMainDataStateEvent.getHotelURl, request, "Hotel")
        } else if (text.equals("Flight")) {
            isClick = "Flight"
            var request: GetBillCustDetailsRequest = GetBillCustDetailsRequest(
                ProductAliasID = null,
                CANo = "",
                Opt1 = pref.getData()?.access_token?.toString(),
                Opt3 = pref.getData()?.BusinessId?.toString(),
                devicecode = pref.getData()?.DeviceCode,
                icode = pref.getData()?.IdentificationCode,
                pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())

            )
            mainViewModel?.getHotelURl(GetMainDataStateEvent.getHotelURl, request, "Flight")
        } else if (text.equals("Train")) {
            isClick = "Train"
            var request: GetBillCustDetailsRequest = GetBillCustDetailsRequest(
                ProductAliasID = null,
                CANo = AppUtils.getUUID(sharedPrefs),
                Opt1 = pref.getData()?.access_token?.toString(),
                Opt3 = pref.getData()?.BusinessId?.toString(),
                devicecode = pref.getData()?.DeviceCode,
                icode = pref.getData()?.IdentificationCode,
                pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())

            )
            mainViewModel?.getHotelURl(GetMainDataStateEvent.getHotelURl, request, "Train")
        } else {
            showSuccessMessage(requireActivity(), "coming  soon $text")
        }

    }

    override fun onClickRechargelistner(position: Int, text: String) {
        if (text.equals("Mobile")) {
            (activity as MainActivity).launch(MobileRechargeActivity::class.java)
        } else if (text.equals("DTH")) {
            (activity as MainActivity).launch(DthRechargeActivity::class.java)
        } else if (text.equals("Health")) {
            var bundle = Bundle()
            bundle.putString(AppConstant.IS_FROM, "Health")
            (activity as MainActivity).launch(
                requireActivity(),
                ElectricityBillPaymentActivity::class.java,
                bundle
            )
        } else {
            showSuccessMessage(requireActivity(), "coming  soon $text")
        }

    }

    override fun onClickShoppinglistner(position: Int, text: String) {
        if (text.equals("Gift Voucher")) {
            var bundle = Bundle()
            bundle.putString(AppConstant.IS_FROM, "Gift")
            (activity as MainActivity).launch(
                requireActivity(),
                GiftVoucherActivity::class.java,
                bundle
            )
        } else if (text.equals("OTT Subscription")) {
            var bundle = Bundle()
            bundle.putString(AppConstant.IS_FROM, "Subscription")
            (activity as MainActivity).launch(requireActivity(), GiftVoucherActivity::class.java)
        } else {
            isClick = "Jio Savan"
            var request: GetBillCustDetailsRequest = GetBillCustDetailsRequest(
                ProductAliasID = null,
                CANo = "",
                Opt1 = pref.getData()?.access_token?.toString(),
                Opt3 = pref.getData()?.BusinessId?.toString(),
                devicecode = pref.getData()?.DeviceCode,
                icode = pref.getData()?.IdentificationCode,
                pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString())

            )
            mainViewModel?.getJeoSavanUrl(GetMainDataStateEvent.getHotelURl, request, "Jio Savan")
            // showSuccessMessage(requireActivity(), "coming  soon $text")
        }
    }

    override fun onClickMoneyTransfer(position: Int, text: String) {
        if (text.equals("AEPS")) {
            (activity as MainActivity).launch(AepsActivity::class.java)
        } else if (text.equals("Aadhar Pay")) {
            (activity as MainActivity).launch(AepsServicesActivity::class.java)
        }else if(text.equals("Money Transfer")){
            (activity as MainActivity).launch(MoneyTransferActivity::class.java)
        }
    }

}