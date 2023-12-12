package com.example.paypointretailer.View.Activity

import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.showErrorMessage
import com.example.paypointretailer.Extention.showSuccessMessage
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.ActivityRegisterBinding
import com.example.paypointretailer.databinding.LayoutOtpDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {
    override fun setUpViews() {
        val wordToSpan: Spannable =
            SpannableString("Already an user ?  Sign In")

        wordToSpan.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.main_blue)),
            19,
            26,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvSignIn.text = wordToSpan
    }

    override fun setUpListeners() {
       binding.btnNext.setOnClickListener {
           checkValidation()
       }
        binding.tvSignIn.setOnClickListener {
            launch(LoginActivity::class.java)
            finish()
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
    }

    private fun checkValidation() {
        if(binding.edtFirstName.text.isNullOrEmpty()){
            binding.errorFirstName.visibility=View.VISIBLE
        }else{
            binding.errorFirstName.visibility=View.GONE
        }
        if(binding.edtLastName.text.isNullOrEmpty()){
            binding.errorLastName.visibility=View.VISIBLE
        }else{
            binding.errorLastName.visibility=View.GONE
        }
        if(binding.edtMobileNUmber.text.isNullOrEmpty()){
            binding.errorMobileNumber.visibility=View.VISIBLE
        } else{
            if(binding.edtMobileNUmber.text?.trim().toString().length != 10){
                binding.errorMobileNumber.visibility=View.VISIBLE
                binding.errorMobileNumber.text = getString(R.string.please_enter_valid_mobile_number)
            }else{
                binding.errorMobileNumber.visibility=View.GONE
            }
        }
        if(binding.edtPassword.text.isNullOrEmpty()){
            binding.errorPassword.visibility=View.VISIBLE
        }else{
            binding.errorPassword.visibility=View.GONE
        }

        checkForRedirection()

    }

    private fun checkForRedirection() {
        if(!binding.edtFirstName.text?.trim().isNullOrEmpty() && !binding.edtLastName.text?.trim().isNullOrEmpty()
            && !binding.edtMobileNUmber.text?.trim().isNullOrEmpty() && !binding.edtPassword.text?.trim().isNullOrEmpty()
            && binding.edtMobileNUmber.text?.trim().toString().length == 10 ){
            if(binding.ivChecked.isChecked){
                showBottomSheetDialog()
            }else{
                showErrorMessage(this,"Please check Privacy & Policy")
            }

        }

    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        val mBottomSheetBinding = LayoutOtpDialogBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setContentView(mBottomSheetBinding.root)
        bottomSheetDialog.setCancelable(true)
        mBottomSheetBinding.btnNext.setOnClickListener {
            if(mBottomSheetBinding.pinView.otp?.length==0){
                showErrorMessage(this,"Please enter OTP")
            } else if(mBottomSheetBinding.pinView.otp?.length!! < 6){
                showErrorMessage(this,"Please enter 6 digit OTP ")
            } else if(!mBottomSheetBinding.ivChecked.isChecked){
                showErrorMessage(this,"Please checked important information")
            } else {
                bottomSheetDialog.dismiss()
                launch(StepperActivity::class.java)
                finish()
            }
        }

        bottomSheetDialog.show()
    }

}