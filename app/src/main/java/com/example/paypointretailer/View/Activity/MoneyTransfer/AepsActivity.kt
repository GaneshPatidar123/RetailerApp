package com.example.paypointretailer.View.Activity.MoneyTransfer

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Request.AepsServiceRequest
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.ViewModel.AepsViewModel
import com.example.paypointretailer.ViewModel.GetAEPSStateEvent
import com.example.paypointretailer.databinding.ActivityAepsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AepsActivity : BaseActivity<ActivityAepsBinding>(R.layout.activity_aeps) {
    private var operatorList: MutableList<String>? = mutableListOf()
    var packageNames: MutableList<String> = mutableListOf()
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: AepsViewModel by viewModels()
    override fun setUpViews() {
        getAllPackageName()
        binding.toolBar.tvTitle.text = getString(R.string.aadhar_micro_atm)
        operatorList?.add("Channel 3")

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, operatorList!!)
        binding.edtChoose.threshold = 1
        binding.edtChoose.setAdapter(adapter)
        var request: AepsServiceRequest = AepsServiceRequest(
            devicecode = pref.getData()?.DeviceCode,
            icode = pref.getData()?.IdentificationCode,
            pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString()),
            key = pref.getData()?.BusinessId!!.toString(),
            authorization = pref.getData()?.access_token
        )
        viewModel.callServiceApplicationStatus(GetAEPSStateEvent.AEPSServices, request)
    }

    private fun getAllPackageName() {
        val pm: PackageManager = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            pm.queryIntentActivities(mainIntent, 0)
        }
        for (resolveInfo in resolvedInfos) {
            val packageName = resolveInfo.activityInfo.packageName
            when (packageName) {
                "com.mantra.rdservice" -> {
                    packageNames.add("Mantra RD Service")
                }
                "com.evolute.rdservice" -> {
                    packageNames.add("Evolute RD Service")
                }
                "com.secugen.rdservice" -> {
                    packageNames.add("SecuGen RD Service")
                }
                "com.mantra.clientmanagement" -> {
                    packageNames.add("Mantra Management Client")
                }
                "com.iritech.rdservice" -> {
                    packageNames.add("Iris RD Service")
                }
                "co.aratek.asix_gms.rdservice" -> {
                    packageNames.add("Aratek A600 RD Service")
                }
                "com.scl.rdservice" -> {
                    packageNames.add("Morpho SCL RDService")
                }
                "in.bioenable.rdservice" -> {
                    packageNames.add("BioEnable Nitgen RDService")
                }
                "com.precision.pb510.rdservice" -> {
                    packageNames.add("PB510 RDService")
                }
                "com.acpl.registersdk" -> {
                    packageNames.add("ACPL FM220 Registered Device")
                }
                "com.tatvik.bio.tmf20" -> {
                    packageNames.add("Tatvik TMF20 RDService")
                }
                "com.mantra.mis100v2.rdservice" -> {
                    packageNames.add("MIS100V2 RDService")
                }
               /* "" -> {
                    packageNames.add("Anonymous RD Service")
                }*/
            }

        }
        Log.d("ganesh", "getAllPackageName: "+packageNames)
    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.edtChoose.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.edtChoose.showDropDown()
            false
        })

        binding.edtChoose.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            setUpData()
        })
    }

    private fun setUpData() {

        binding.toolBar.tvTitle.text = "Self Registration"
        binding.edtChoose.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE

        var request: AepsServiceRequest = AepsServiceRequest(
            devicecode = pref.getData()?.DeviceCode,
            icode = pref.getData()?.IdentificationCode,
            pcode = EncryptionUtils.encrypt(pref.getData()?.DevicePassword.toString()),
            key = pref.getData()?.BusinessId!!.toString(),
            authorization = pref.getData()?.access_token
        )
        viewModel.callSelectPerformStatus(GetAEPSStateEvent.callSelectPerfrom, request, "CHANNEL5")
    }

    override fun setUpObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    Log.d("TAG", "setUpObservers: " + "Success")

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

        viewModel.perfromDataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    Log.d("TAG", "setUpObservers: " + "Success")
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