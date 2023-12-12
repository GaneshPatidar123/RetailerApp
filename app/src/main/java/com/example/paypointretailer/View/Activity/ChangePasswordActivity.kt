package com.example.paypointretailer.View.Activity

import android.content.SharedPreferences
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Model.Request.ChangePasswordRequest
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.ViewModel.ChangePasswordViewModel
import com.example.paypointretailer.ViewModel.GetChangePasswordStateEvent
import com.example.paypointretailer.databinding.ActivityChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordActivity :
    BaseActivity<ActivityChangePasswordBinding>(R.layout.activity_change_password) {
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private val viewModel: ChangePasswordViewModel by viewModels()
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.change_password)
    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            if (binding.edtOldPassword.text.toString().isNullOrEmpty()) {
                binding.tvErrorOldPassword.visibility = View.VISIBLE
                binding.tvErrorOldPassword.text = getString(R.string.old_password_is_required)
            } else {
                if (!binding.edtOldPassword.text.toString()
                        .equals(pref.getData()?.DevicePassword)
                ) {
                    binding.tvErrorOldPassword.visibility = View.VISIBLE
                    binding.tvErrorOldPassword.text = getString(R.string.old_password_not_match)
                } else {
                    binding.tvErrorOldPassword.visibility = View.GONE
                }
            }

            if (binding.edtNewPassword.text.toString().isNullOrEmpty()) {
                binding.tvErrorNewPassword.visibility = View.VISIBLE
            } else {
                binding.tvErrorNewPassword.visibility = View.GONE
            }

            if (binding.edtConfirmPassword.text.toString().isNullOrEmpty()) {
                binding.tvErrorConfirmPassword.visibility = View.VISIBLE
                binding.tvErrorConfirmPassword.text = "Confirm password is required *"
            } else {
                if (binding.edtNewPassword.text.toString()
                        .equals(binding.edtConfirmPassword.text.toString())
                ) {
                    binding.tvErrorConfirmPassword.visibility = View.GONE
                } else {
                    binding.tvErrorConfirmPassword.visibility = View.VISIBLE
                    binding.tvErrorConfirmPassword.text =
                        getString(R.string.new_password_and_confirm_paasword_not_macth)
                }
            }

            if (!binding.edtOldPassword.text.toString()
                    .isNullOrBlank() && binding.edtOldPassword.text.toString()
                    .equals(pref.getData()?.DevicePassword) && !binding.edtConfirmPassword.text.toString()
                    .isNullOrEmpty()
                && !binding.edtNewPassword.text.toString()
                    .isNullOrBlank() && binding.edtNewPassword.text.toString()
                    .equals(binding.edtConfirmPassword.text.toString())
            ) {

                if (AppUtils.hasInternet(this)) {
                    val request: ChangePasswordRequest = ChangePasswordRequest(
                        BusinessId = pref.getData()!!.BusinessId.toString(),
                        DeviceCode = pref.getData()!!.DeviceCode.toString(),
                        DevicePassword = pref.getData()!!.DevicePassword,
                        NewPassword = binding.edtNewPassword.text.toString()

                    )
                    viewModel.changePassword(
                        GetChangePasswordStateEvent.changePasswordState,
                        request,
                        pref.getData()!!.access_token
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

    override fun setUpObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    if (dataState.data.Status.equals("Success")) {
                        pref.clear()
                        launchNewFirstActivity(LoginActivity::class.java)
                    } else {
                        showErrorMessage(this, dataState.data.Msg!!)
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