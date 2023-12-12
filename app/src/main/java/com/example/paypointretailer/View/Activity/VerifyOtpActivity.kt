package com.example.paypointretailer.View.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.MainActivity
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.RequestCheckMobileDeviceUsed
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.Utils.StaticData
import com.example.paypointretailer.ViewModel.GetLoginDataStateEvent
import com.example.paypointretailer.ViewModel.GetOtpVerifiedDataStateEvent
import com.example.paypointretailer.ViewModel.LoginViewModel
import com.example.paypointretailer.ViewModel.OtpVerfiedViewModel
import com.example.paypointretailer.databinding.ActivityVerifyOtpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import javax.inject.Inject

@AndroidEntryPoint
class VerifyOtpActivity : BaseActivity<ActivityVerifyOtpBinding>(R.layout.activity_verify_otp) {
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: OtpVerfiedViewModel by viewModels()
    override fun setUpViews() {
       var userData = pref.getData()
        binding.tvName.text = userData?.FirstName+ " "+userData?.LastName+"!"
    }

    override fun setUpListeners() {

        binding.tvRegister.setOnClickListener {
            launch(RegisterActivity::class.java)
        }
        binding.btnLogin.setOnClickListener {
            if (binding.edtOtp.text.isNullOrEmpty() ) {
                binding.errorOtp.visibility = View.VISIBLE
            } else if(binding.edtOtp.text!!.length < 6){
                binding.errorOtp.text ="Please Enter 6 digit OTP"
                binding.errorOtp.visibility = View.VISIBLE
            } else {
                binding.errorOtp.visibility = View.GONE
                if (AppUtils.hasInternet(this)) {
                    val request: VerifiedOtpRequest = VerifiedOtpRequest(
                        Mobile = pref.getData()?.Mobile.toString(),
                        OTP = binding.edtOtp.text!!.toString(),
                        Serial = Build.SERIAL,
                        Uuid = AppUtils.getUUID(sharedPrefs),
                        key = pref.getData()?.BusinessId,
                        token = pref.getData()?.access_token
                    )
                    viewModel.setOtpVerifiedEvent(GetOtpVerifiedDataStateEvent.otpVerifiedEvent,request)
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
                    val userData = dataState.data
                    if(userData.Status=="success"){
                        launchNewFirstActivity(MainActivity::class.java)
                    }else{
                        showErrorMessage(this,dataState.data.Msg!!)
                    }
                }

                is Resource.Error -> {
                      hideProgressDialog()
                      showErrorMessage(this,dataState.message)
                    //  notifyUser(dataState.message)
                }

                is Resource.Loading -> {
                     showProgressDialog()
                }
            }
        })


    }
}