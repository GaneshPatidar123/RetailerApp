package com.example.paypointretailer.View.Activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.paypointretailer.Adapter.PlanPagerAdapter
import com.example.paypointretailer.Api.APIInterfaceNew
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Response.MobileRecharge.GetOperatorResponse
import com.example.paypointretailer.Model.Response.MobileRecharge.PlanListResponse
import com.example.paypointretailer.Model.Response.MobileRecharge.Result
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.View.Fragement.PlanListFragment
import com.example.paypointretailer.databinding.ActivityBrowserPlanBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@AndroidEntryPoint
class BrowserPlanActivity :
    BaseActivity<ActivityBrowserPlanBinding>(R.layout.activity_browser_plan),
    PlanListFragment.OnDataReceivedListener {
    lateinit var getOperatorResponse: GetOperatorResponse
    lateinit var planListResponseData: PlanListResponse
    private lateinit var planPagerAdapter: PlanPagerAdapter
    var planTypeList: MutableList<Result>? = null
    var BASE_URL = "https://ppapi.paypointindia.co.in/pprestapi/api/plans/Mobile/"

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.browser_plan)
        getDataFromIntents(intent)


    }

    private fun getMyData() {
        if (AppUtils.hasInternet(this)) {

            showProgressDialog()
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(APIInterfaceNew::class.java)
            BASE_URL =
                BASE_URL + getOperatorResponse.opr_code!! + "/" + binding.selectArea.text.toString()
            val retrofitData = retrofitBuilder.getSend(BASE_URL)

            clearAllTabs()
            retrofitData!!.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    planListResponseData = GsonBuilder().create()
                        .fromJson(response.body(), PlanListResponse::class.java)
                    planTypeList = planListResponseData.result
                        ?.distinctBy { it.PlanType }!!.toMutableList()
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

        } else {
            showErrorMessage(
                this,
                getString(R.string.please_check_internet_connection)
            )
        }
    }

    private fun setUpViewPager() {
        planPagerAdapter = PlanPagerAdapter(
            supportFragmentManager, planTypeList!!.size,
            planListResponseData
        )
        binding.viewPager.setOffscreenPageLimit(0)
        binding.viewPager.setAdapter(planPagerAdapter)
    }
    private fun clearAllTabs() {
        binding.tabLayout.removeAllTabs()
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
            BASE_URL = ""
            BASE_URL = "https://ppapi.paypointindia.co.in/pprestapi/api/plans/Mobile/"
            clearAllTabs()
            getMyData()
        })

        binding.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.e("ViewPager ", " getCurrentItem() " + binding.viewPager.getCurrentItem())
                binding.viewPager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun getDataFromIntents(intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {
            getOperatorResponse = bundle.getSerializable("OperatorData") as GetOperatorResponse
            binding.selectArea.setText(getOperatorResponse.circle)
            var circleCollection = pref.getInitialData()?.CircleCollection
            for (item in circleCollection!!.indices) {
                if (circleCollection[item].ProductAliasID == getOperatorResponse.opr_code!!.toInt()) {
                    val stringList = circleCollection[item].Circle
                    val stateList = stringList?.split(",")!!.toTypedArray()
                    if (stateList.size != 0) {
                        val adapter =
                            ArrayAdapter(
                                this,
                                android.R.layout.simple_dropdown_item_1line,
                                stateList!!
                            )
                        binding.selectArea.threshold = 1
                        binding.selectArea.setAdapter(adapter)
                    }
                }
            }
            getMyData()
        }
    }

    override fun onDataReceived(data: Result) {
        Log.d("TAG", "onDataReceived: " + data)
        val resultIntent = Intent()
        resultIntent.putExtra("resultData", data)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}