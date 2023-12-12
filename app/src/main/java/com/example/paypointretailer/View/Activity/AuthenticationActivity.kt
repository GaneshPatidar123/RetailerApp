package com.example.paypointretailer.View.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.paypointretailer.databinding.ActivityAuthenticationBinding
import com.example.paypointretailer.databinding.LayoutLoginDialogBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity :
    BaseActivity<ActivityAuthenticationBinding>(R.layout.activity_authentication) {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID = 34
    private lateinit var bottomSheetDialog: Dialog
    private var latitude: String? = null
    private var longitude: String? = null

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val loginViewModel: LoginViewModel by viewModels()
    override fun setUpViews() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        val wordtoSpan: Spannable =
            SpannableString("Not You....? Sign In using another credentials")

        wordtoSpan.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.main_blue)),
            14,
            20,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        binding.ivHide.setOnClickListener {
            if (binding.edtPassword.getInputType() === InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                binding.edtPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
                binding.ivHide.setImageDrawable(resources.getDrawable(R.drawable.ic_down))
            } else {
                binding.ivHide.setImageDrawable(resources.getDrawable(R.drawable.ic_hide))
                binding.edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            }
            binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
        }

        binding.tvText.setText(wordtoSpan)
    }

    override fun setUpListeners() {
        setUpOnClickListner()
    }

    private fun setUpOnClickListner() {
        binding.tvUseFingure.setOnClickListener {
            showBottomSheetDialog()
        }
        binding.tvText.setOnClickListener {
            launch(LoginActivity::class.java)

        }
        binding.btnConfirm.setOnClickListener {
            if (binding.edtPassword.text.isNullOrEmpty()) {
                binding.tvErrorPassword.visibility = View.VISIBLE
            } else {
                binding.tvErrorPassword.visibility = View.GONE
                launch(VerifyOtpActivity::class.java)
            }
        }
        binding.btnConfirm.setOnClickListener {
            if (AppUtils.hasInternet(this)) {
                val request: LoginRequest = LoginRequest(
                    mobile = pref.getData()?.Mobile,
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

    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        val mBottomSheetBinding = LayoutLoginDialogBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setContentView(mBottomSheetBinding.root)
        mBottomSheetBinding.tvPatterm.setOnClickListener {
            bottomSheetDialog.dismiss()
            showBiometricPromptPIN()
        }
        mBottomSheetBinding.ivFingure.setOnClickListener {
            if (isBiometricSupported()) {
                showBiometricPrompt()
            } else {
                showMessage("Your device not supported Pin.")
            }
        }
        bottomSheetDialog.show()

    }

    private fun showBiometricPromptPIN() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Password Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setDeviceCredentialAllowed(true)
            //  .setNegativeButtonText("Pin")
            .build()

        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error
                    showMessage("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Handle authentication success
                    bottomSheetDialog.dismiss()

                    if (AppUtils.hasInternet(this@AuthenticationActivity)) {
                        val request: LoginRequest = LoginRequest(
                            mobile = pref.getData()?.Mobile,
                            password = EncryptionUtils.encrypt(pref.getData()?.Mobile),
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
                        showMessage("Authentication succeeded!")
                    } else {
                        showErrorMessage(
                            this@AuthenticationActivity,
                            getString(R.string.please_check_internet_connection)
                        )
                    }

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle authentication failure
                    showMessage("Authentication failed.")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    override fun setUpObservers() {
        loginViewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data

                    if (userData != null) {
                        if (userData.Status == "true") {
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
                    AppUtils.hideKeyboard(this@AuthenticationActivity)
                    hideProgressDialog()
                    val userData = pref.getData()
                    if (userData?.EnableOTPValidation!!) {
                        launch(VerifyOtpActivity::class.java)
                    } else {
                        launchNewFirstActivity(MainActivity::class.java)
                    }
                }

                is Resource.Error -> {
                    pref.clear()
                    hideProgressDialog()
                    AppUtils.hideKeyboard(this@AuthenticationActivity)
                    //  notifyUser(dataState.message)
                }

                is Resource.Loading -> {
                    showProgressDialog()
                }
            }
        })
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate =
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // The user can authenticate with biometrics, continue with the authentication process
                return true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Handle the error cases as needed in your app
                return false
            }

            else -> {
                // Biometric status unknown or another error occurred
                return false
            }
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error
                    showMessage("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Handle authentication success
                    bottomSheetDialog.dismiss()
                    showMessage("Authentication succeeded!")

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle authentication failure
                    showMessage("Authentication failed.")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation();
        }
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