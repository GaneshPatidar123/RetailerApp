package com.example.paypointretailer.View.Activity.BillPayment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Adapter.AdapterGetBillService
import com.example.paypointretailer.Adapter.AddLimitAdapter
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Favourite
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppConstant
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.AppUtils.Companion.activity
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.View.Activity.LoginActivity
import com.example.paypointretailer.ViewModel.BillPaymentViewModel
import com.example.paypointretailer.ViewModel.ChangePasswordViewModel
import com.example.paypointretailer.ViewModel.GetBillPaymentStateEvent
import com.example.paypointretailer.ViewModel.GetChangePasswordStateEvent
import com.example.paypointretailer.databinding.ActivityElectricityBillPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@AndroidEntryPoint
class ElectricityBillPaymentActivity :
    BaseActivity<ActivityElectricityBillPaymentBinding>(R.layout.activity_electricity_bill_payment),
    AdapterGetBillService.OnClikcListnerAdapterInterface {
    private lateinit var adapterGetBillService: AdapterGetBillService
    private var getServiceList: List<GetServiceList>? = mutableListOf()

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: BillPaymentViewModel by viewModels()
    private var isFrom = ""
    private var DomainId = 0
    override fun setUpViews() {

        getDataFromIntents(intent)
        setUpRecycleView()
        getServiceList()
    }

    private fun getDataFromIntents(intent: Intent?) {
        val bundle = intent?.extras
        if (bundle != null) {
            isFrom = bundle.getString(AppConstant.IS_FROM)!!
        }
    }

    private fun getServiceList() {
        when (isFrom) {
            "Electricity" -> {
                DomainId = 2
                binding.toolBar.tvTitle.text = getString(R.string.electricity_bill_payment)
            }

            "Municipal Taxes" -> {
                DomainId = 30
                binding.toolBar.tvTitle.text = "Municipal Taxes Payment"
            }

            "Broadband" -> {
                DomainId = 20
                binding.toolBar.tvTitle.text = "ISP bill Payment"
            }

            "Housing Society" -> {
                DomainId = 31
                binding.toolBar.tvTitle.text = "Housing Society Payment"
            }

            "Water" -> {
                DomainId = 21
                binding.toolBar.tvTitle.text = "Water bill Payment"
            }

            "Hospital" -> {
                DomainId = 34
                binding.toolBar.tvTitle.text = "Hospital  Payment"
            }

            "Piped Gas" -> {
                DomainId = 11
                binding.toolBar.tvTitle.text = "Gas bill Payment"
            }

            "Gas Cylinder" -> {
                DomainId = 25
                binding.toolBar.tvTitle.text = "Book a gas"
            }

            "Education Fees" -> {
                DomainId = 29
                binding.toolBar.tvTitle.text = "Education Fees"
            }

            "Municipal Service" -> {
                DomainId = 33
                binding.toolBar.tvTitle.text = "Municipal Service"
            }

            "Landline" -> {
                DomainId = 12
                binding.toolBar.tvTitle.text = "Landline bill Payment"
            }

            "Cable Tv" -> {
                DomainId = 27
                binding.toolBar.tvTitle.text = "Cable - Tv  bill Payment"
            }
            "Health" -> {
                DomainId = 28
                binding.toolBar.tvTitle.text = "Health insurance payment "
            }
            // Add more cases if needed
            else -> {
                // Handle the default case if none of the above conditions match
            }
        }
        if (AppUtils.hasInternet(this)) {
            viewModel.getServiceList(
                GetBillPaymentStateEvent.getListState,
                pref.getData()!!.access_token,
                pref.getData()?.BusinessId,
                DomainId
            )
        } else {
            showErrorMessage(
                this,
                getString(R.string.please_check_internet_connection)
            )
        }

    }

    override fun setUpListeners() {
        binding.ivClose.setOnClickListener {
            binding.ivClose.visibility = View.GONE
            /* adapterFavourites.clear()
             adapterFavourites.addItems(list!!)
             adapterFavourites.notifyDataSetChanged()*/
            binding.search.text?.clear()
            binding.search.clearFocus()
            // AppUtils.hideKeyboard(requireActivity())
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.search.windowToken, 0)
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s)
                if (s!!.length != 0) {
                    binding.ivClose.visibility = View.VISIBLE
                } else {
                    binding.ivClose.visibility = View.GONE
                }
            }
        })
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun filter(text: CharSequence?) {
        val filteredlist: ArrayList<GetServiceList> = ArrayList()

        // running a for loop to compare elements.
        for (item in getServiceList!!) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.Product!!.toLowerCase()
                    .contains(text.toString().toLowerCase()) || item.Product!!.toLowerCase()
                    .contains(text.toString().toUpperCase())
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        adapterGetBillService.clear()
        if (filteredlist.size != 0) {
            adapterGetBillService.addItems(filteredlist)
            adapterGetBillService.notifyDataSetChanged()
        } else {
            adapterGetBillService.addItems(getServiceList!!)
            adapterGetBillService.notifyDataSetChanged()
        }
    }

    override fun setUpObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    getServiceList = userData.toImmutableList()
                    adapterGetBillService.addItems(getServiceList!!)
                    adapterGetBillService.notifyDataSetChanged()
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

    private fun setUpRecycleView() {
        adapterGetBillService = AdapterGetBillService(this, this)
        binding.rvList.adapter = adapterGetBillService
        adapterGetBillService.notifyDataSetChanged()
    }

    override fun onSelectPlan(position: Int, data: GetServiceList) {
        var bundle = Bundle()
        bundle.putSerializable("Data", data)
        bundle.putInt("DomainId", DomainId)
        bundle.putString("toolBar", binding.toolBar.tvTitle.text.toString())
        launch(this, BillPaymentActivity::class.java, bundle)
    }
}