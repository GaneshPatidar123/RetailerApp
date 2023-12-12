package com.example.paypointretailer.View.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Extention.showSuccessMessage
import com.example.paypointretailer.MainActivity
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.RequestCheckMobileDeviceUsed
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.EncryptionUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.Utils.StaticData
import com.example.paypointretailer.ViewModel.GetLoginDataStateEvent
import com.example.paypointretailer.ViewModel.LoginViewModel
import com.example.paypointretailer.databinding.ActivityLoginBinding
import com.example.paypointretailer.databinding.ForgotPasswordLayoutBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID = 34
    private var lastLocation: Location? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var isforgotClick: Boolean = false

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val loginViewModel: LoginViewModel by viewModels()
    override fun setUpViews() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //  getLastLocation()


    }


    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    override fun setUpListeners() {
        binding.tvRegister.setOnClickListener {
            launch(RegisterActivity::class.java)
        }
        binding.ivHide.setOnClickListener {
            if (binding.edtPassword.getInputType() === InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                binding.edtPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
                binding.ivHide.setImageDrawable(resources.getDrawable(R.drawable.ic_show))
            } else {
                binding.ivHide.setImageDrawable(resources.getDrawable(R.drawable.ic_hide))
                binding.edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            }
            binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
        }
        binding.btnLogin.setOnClickListener {


            if (binding.edtMobileNUmber.text.isNullOrEmpty()) {
                binding.tvErrorMobileNumber.visibility = View.VISIBLE
            }/* else{
                 if(binding.edtMobileNUmber.text?.trim().toString().length != 10){
                     binding.tvErrorMobileNumber.visibility=View.VISIBLE
                     binding.tvErrorMobileNumber.text = getString(R.string.please_enter_valid_mobile_number)
                 }else{
                     binding.tvErrorMobileNumber.visibility=View.GONE
                 }
            }*/
            if (binding.edtPassword.text.isNullOrEmpty()) {
                binding.tvErrorPassword.visibility = View.VISIBLE
            } else {
                binding.tvErrorPassword.visibility = View.GONE
            }
            if (!binding.edtMobileNUmber.text?.trim()
                    .isNullOrEmpty() && !binding.edtPassword.text?.trim().isNullOrEmpty()
            ) {
                if (AppUtils.hasInternet(this)) {
                    val request: LoginRequest = LoginRequest(
                        mobile = binding.edtMobileNUmber.text.toString(),
                        password = EncryptionUtils.encrypt(binding.edtPassword.text.toString()),
                        device_token = null,
                        device_type = null,
                        loginfrom = "MobileApp-" + StaticData.currentVersion,
                        device_model = null,
                        uu_id = null,
                        latitude = latitude,
                        longitude = longitude,
                        platform = "Android-" + Build.VERSION.SDK_INT
                    )
                    loginViewModel.setLoginEvent(GetLoginDataStateEvent.LoginEvent, request)
                } else {
                    showErrorMessage(
                        this,
                        getString(R.string.please_check_internet_connection)
                    )
                }

            }
        }

        binding.tvForgot.setOnClickListener {
            showForgotDialog()
        }
    }

    private fun showForgotDialog() {
        val dialog = Dialog(this, R.style.RoundedCornersDialog)
        val mBottomSheetBinding = ForgotPasswordLayoutBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(mBottomSheetBinding.root)
        val window: Window? = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        mBottomSheetBinding.tvCamcel.setOnClickListener {
            dialog.dismiss()
        }
        mBottomSheetBinding.tvContinues.setOnClickListener {
            if (mBottomSheetBinding.edtMobileNUmber.text.toString().isNullOrEmpty()) {
                mBottomSheetBinding.tvErrorMesg.visibility = View.VISIBLE
                mBottomSheetBinding.tvErrorMesg.text =
                    getString(R.string.please_enter_mobile_number)
            } else if (mBottomSheetBinding.edtMobileNUmber.text!!.length < 10) {
                mBottomSheetBinding.tvErrorMesg.visibility = View.VISIBLE
                mBottomSheetBinding.tvErrorMesg.text =
                    getString(R.string.please_enter_valid_mobile_number)
            } else {
                mBottomSheetBinding.tvErrorMesg.visibility = View.GONE
                dialog.dismiss()
                isforgotClick = true;
                val request: RequestCheckMobileDeviceUsed =
                    RequestCheckMobileDeviceUsed(
                        Cordova = null,
                        IsVirtual = null,
                        Manufacturer = Build.MANUFACTURER,
                        Model = Build.MODEL,
                        Platform = "Android",
                        Serial = Build.SERIAL,
                        Uuid = AppUtils.getUUID(sharedPrefs),
                        Version = Build.VERSION.SDK_INT.toString(),
                        key = null,
                        PushNotificationRegID = null,
                        Mobile = mBottomSheetBinding.edtMobileNUmber.text.toString()
                    )
                loginViewModel.apiCallForgotPassword(
                    GetLoginDataStateEvent.CheckDeviceUsedEvent,
                    request
                )
            }

        }
        dialog.show()
    }

    override fun setUpObservers() {
        loginViewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data

                    if (userData != null) {
                        if (userData.Status == "true") {
                            pref.isUserName = binding.edtMobileNUmber.text.toString()
                            pref.passWord = binding.edtPassword.text.toString()
                            userData.BE!!.access_token = dataState.data.AT.access_token
                            userData.BE!!.expires_in = dataState.data.AT.expires_in
                            userData.BE!!.DevicePassword = binding.edtPassword.text.toString()
                            pref.saveData(userData.BE)
                            val request: RequestCheckMobileDeviceUsed =
                                RequestCheckMobileDeviceUsed(
                                    Cordova = null,
                                    IsVirtual = false,
                                    Manufacturer = Build.MANUFACTURER,
                                    Model = Build.MODEL,
                                    Platform = "Android",
                                    Serial = Build.SERIAL,
                                    Uuid = AppUtils.getUUID(sharedPrefs),
                                    Version = Build.VERSION.SDK_INT.toString(),
                                    key = userData.BE!!.BusinessId,
                                    PushNotificationRegID = "Yes",
                                    Mobile = null
                                )
                            loginViewModel.checkMobileDeViceUsed(
                                GetLoginDataStateEvent.CheckDeviceUsedEvent,
                                request
                            )
                            /* if (userData.BE?.EnableOTPValidation!!) {
                                 launch(VerifyOtpActivity::class.java)
                             } else {
                                 launchNewFirstActivity(MainActivity::class.java)
                             }*/
                            // showSuccessMessage(this, "" + pref.isUserName)
                        } else {
                            if (dataState.data.Msg.isNullOrEmpty()) {
                                showErrorMessage(this, "Please enter valid details:")
                            } else {
                                showErrorMessage(this, dataState.data.Msg.toString())
                            }
                        }
                    } else {
                        showErrorMessage(this, dataState.data.Msg.toString())
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

        loginViewModel.deviceCheckState.observe(this, Observer { dataState ->
            when (dataState) {

                is Resource.Success -> {
                    AppUtils.hideKeyboard(this@LoginActivity)
                    hideProgressDialog()
                    if (isforgotClick) {
                        isforgotClick = false;
                        showSuccessMessage(this, dataState.data.Msg!!)
                    } else {
                        val userData = pref.getData()
                        if (userData?.EnableOTPValidation!!) {
                            launch(VerifyOtpActivity::class.java)
                        } else {
                            launchNewFirstActivity(MainActivity::class.java)
                        }
                    }
                }

                is Resource.Error -> {
                    pref.clear()
                    hideProgressDialog()
                    AppUtils.hideKeyboard(this@LoginActivity)
                    //  notifyUser(dataState.message)
                }

                is Resource.Loading -> {
                    showProgressDialog()
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(5)
        mLocationRequest.setFastestInterval(0)
        mLocationRequest.setNumUpdates(1)

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            //   latitudeTextView.setText("Latitude: " + mLastLocation!!.latitude + "")
            // longitTextView.setText("Longitude: " + mLastLocation.longitude + "")
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}