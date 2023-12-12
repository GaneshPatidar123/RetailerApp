package com.example.paypointretailer.View.Activity.Recharge

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Adapter.DthPlanPagerAdaper
import com.example.paypointretailer.Api.APIInterfaceNew
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Request.RequestCheckMobileDeviceUsed
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MainResponse.ProductEntity
import com.example.paypointretailer.Model.Response.MobileRecharge.GetOperatorCode
import com.example.paypointretailer.Model.Response.MobileRecharge.PlanListResponse
import com.example.paypointretailer.Model.Response.MobileRecharge.Result
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.View.Fragement.DthPlanListFragment
import com.example.paypointretailer.View.Fragement.PlanListFragment
import com.example.paypointretailer.ViewModel.DthRechargeViewModel
import com.example.paypointretailer.ViewModel.GetDTHRechargedataStateEvent
import com.example.paypointretailer.ViewModel.GetMainDataStateEvent
import com.example.paypointretailer.ViewModel.LoginViewModel
import com.example.paypointretailer.ViewModel.MainViewModel
import com.example.paypointretailer.databinding.ActivityDthRechargeBinding
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class DthRechargeActivity :
    BaseActivity<ActivityDthRechargeBinding>(R.layout.activity_dth_recharge),
    DthPlanListFragment.OnDataReceivedListener {
    private lateinit var planPagerAdapter: DthPlanPagerAdaper
    lateinit var planListResponseData: PlanListResponse
    var BASE_URL = "https://ppapi.paypointindia.co.in/pprestapi/api/plans/DTH/"
    var planTypeList: MutableList<Result>? = null
    var dthListData: MutableList<String>? = mutableListOf()
    private var operatorLists: List<ProductEntity>? = null

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: DthRechargeViewModel by viewModels()
    var ProductId = 0
    override fun setUpViews() {
        dthListData?.clear()
        binding.toolBar.tvTitle.text = getString(R.string.dth_recharge)
        if (pref.getInitialData()?.ProductEntity != null) {
            var DTHLis = pref.getInitialData()?.ProductEntity
            operatorLists = DTHLis?.filter { it -> it.DomainId == 4 }
            for (item in operatorLists!!.indices) {
                dthListData?.add(operatorLists!![item].Product!!)
            }
        }
        Log.d("TAG", "setUpViews: " + dthListData?.size)
        if (dthListData?.size != 0 && dthListData != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dthListData!!)
            binding.edtSelectOperatore.threshold = 1
            binding.edtSelectOperatore.setAdapter(adapter)
        }
        // operatorList?.addAll(operatorLists as MutableList<ProductEntity>)
        //binding.tvBrowser.alpha = 0.4f

        //   getMyData()
    }

    private fun getMyData() {

        showProgressDialog()
        val retrofitBuilder =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
                .build().create(APIInterfaceNew::class.java)
        for (item in operatorLists!!.indices) {
            if (operatorLists!![item].Product.equals(binding.edtSelectOperatore.text.toString())) {
                ProductId = operatorLists!![item].ProductId!!
            }
            // dthListData?.add(operatorLists!![item].Product!!)
        }
        clearAllTabs()
        BASE_URL = BASE_URL + "$ProductId" + "/" + "All%20India"
        val retrofitData = retrofitBuilder.getSend(BASE_URL)

        retrofitData!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                hideProgressDialog()
                planListResponseData =
                    GsonBuilder().create().fromJson(response.body(), PlanListResponse::class.java)
                planTypeList?.clear()
                planTypeList =
                    planListResponseData.result?.distinctBy { it.PlanType }!!.toMutableList()

                for (item in planTypeList!!.indices) {
                    binding.tabLayout.addTab(
                        binding.tabLayout.newTab().setText(planTypeList!![item].PlanType)
                    )
                }
                hideProgressDialog()
                setUpViewPager()
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.d("TAG", "error" + t.message)
                hideProgressDialog()
            }
        })
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.e("ViewPager ", " getCurrentItem() " + binding.viewPager.getCurrentItem())
                binding.viewPager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun clearAllTabs() {
        binding.tabLayout.removeAllTabs()
    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.edtSelectOperatore.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.edtSelectOperatore.showDropDown()
            false
        })
        binding.llCommission.setOnClickListener {
            binding.tvPlan.setTextColor(getColor(R.color.light_grey))
            binding.viewPlan.setBackgroundColor(getColor(R.color.white))
            binding.tvCommossion.setTextColor(getColor(R.color.main_blue))
            binding.viewCommission.setBackgroundColor(getColor(R.color.main_blue))
            binding.llPlanData.visibility = View.GONE
            binding.llCommissionData.visibility = View.VISIBLE
        }
        binding.llPLan.setOnClickListener {
            binding.tvPlan.setTextColor(getColor(R.color.main_blue))
            binding.viewPlan.setBackgroundColor(getColor(R.color.main_blue))
            binding.tvCommossion.setTextColor(getColor(R.color.light_grey))
            binding.viewCommission.setBackgroundColor(getColor(R.color.white))
            binding.llPlanData.visibility = View.VISIBLE
            binding.llCommissionData.visibility = View.GONE
            //  setUpViewPager()
        }

        binding.edtSelectOperatore.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            BASE_URL = ""
            BASE_URL = "https://ppapi.paypointindia.co.in/pprestapi/api/plans/DTH/"
            binding.tvPlan.setTextColor(getColor(R.color.main_blue))
            binding.viewPlan.setBackgroundColor(getColor(R.color.main_blue))
            binding.tvCommossion.setTextColor(getColor(R.color.light_grey))
            binding.viewCommission.setBackgroundColor(getColor(R.color.white))
            binding.llPlanData.visibility = View.VISIBLE
            binding.llCommissionData.visibility = View.GONE
            binding.edtAmount.text?.clear()
            clearAllTabs()
            if (AppUtils.hasInternet(this)) {
                getMyData()
            } else {
                showErrorMessage(
                    this, getString(R.string.please_check_internet_connection)
                )
            }

        })

        binding.tvCheck.setOnClickListener {
            AppUtils.hideKeyboard(this@DthRechargeActivity)
            if (ProductId == 0) {
                showErrorMessage(this, "Please select operator  ")
            } else if (binding.edtCustTD.text.isNullOrEmpty()) {
                showErrorMessage(this, "Please enter Customer ID ")
            } else {
                if (AppUtils.hasInternet(this)) {
                    val request: VerifiedOtpRequest = VerifiedOtpRequest(
                        Mobile = binding.edtCustTD.text.toString(),
                        OTP = null,
                        Serial = Build.SERIAL,
                        Uuid = AppUtils.getUUID(sharedPrefs),
                        key = ProductId,
                        token = pref.getData()?.access_token
                    )
                    viewModel.callCheckServiceNoAPI(
                        GetDTHRechargedataStateEvent.checkService, request
                    )
                } else {
                    showErrorMessage(
                        this, getString(R.string.please_check_internet_connection)
                    )
                }
            }
        }

        binding.btnPayNow.setOnClickListener {
            if (ProductId == 0) {
                showErrorMessage(this, "Please select operator ")
            } else if (binding.edtCustTD.text.isNullOrEmpty()) {
                showErrorMessage(this, "Please enter Customer ID ")
            } else if (binding.edtAmount.text.isNullOrEmpty()) {
                showErrorMessage(this, "Please enter your plan amount")
            } else {
                if (AppUtils.hasInternet(this)) {
                    var request: MobilreRechargeRequest = MobilreRechargeRequest(
                        ServiceAcNo = binding.edtCustTD.text.toString(),
                        Amount = binding.edtAmount.text.toString(),
                        ProductID = ProductId,
                        authorization = pref.getData()?.access_token,
                        businessid = pref.getData()?.BusinessId.toString(),
                        devicecode = pref.getData()?.DeviceCode,
                        icode = pref.getData()?.IdentificationCode,
                        pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString()),
                        SessionID = null,
                        CustomerMobileNo = null
                    )
                    viewModel.dthRechargeNow(GetDTHRechargedataStateEvent.rechargeNow, request)
                } else {
                    showErrorMessage(
                        this, getString(R.string.please_check_internet_connection)
                    )
                }
            }
        }

    }

    override fun setUpObservers() {
        viewModel.dataStateeURL.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    var getOperatorCode =
                        GsonBuilder().create().fromJson(dataState.data, GetOperatorCode::class.java)
                    if (getOperatorCode.status.equals("failure")) {
                        showErrorMessage(this, getOperatorCode.description!!)
                    } else {
                        showErrorMessage(this, "Your service no is valid")
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
                    //  notifyUser(dataState.message)
                }

                is Resource.Loading -> {
                    showProgressDialog()
                }
            }
        })

    }

    private fun setUpViewPager() {
        planPagerAdapter = DthPlanPagerAdaper(
            supportFragmentManager, planTypeList!!.size, planListResponseData
        )
        binding.viewPager.setOffscreenPageLimit(0)
        binding.viewPager.setAdapter(planPagerAdapter)
    }

    override fun onDataReceived(data: Result) {
        binding.edtAmount.setText(data.Amount.toString())
    }
}