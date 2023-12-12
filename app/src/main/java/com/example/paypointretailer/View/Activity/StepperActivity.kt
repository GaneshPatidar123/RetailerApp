package com.example.paypointretailer.View.Activity


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.OpenableColumns
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.MainActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.ActivityStepperBinding
import com.google.android.material.snackbar.Snackbar


class StepperActivity : BaseActivity<ActivityStepperBinding>(R.layout.activity_stepper) {
    private var position = 1
    var accountType = arrayOf(
        "Saving Account", "Current  Account"
    )
    private var storagePermision =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                selectFile()
            } else {
                Snackbar.make(
                    binding.root,
                    "Please grant Read Media permission from App Settings",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    @SuppressLint("Range")
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                var fileName =""
                if (result.data?.data!!.getScheme().equals("content")) {
                    val cursor = contentResolver.query(result.data?.data!!, null, null, null, null)
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            fileName =
                                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        }
                    } finally {
                        cursor!!.close()
                    }
                    binding.tvNameFile.text = fileName
                }
            }
        }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        resultLauncher.launch(intent)
    }

    override fun setUpViews() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, accountType)
        binding.edtSelectAccountType.threshold = 1
        binding.edtSelectAccountType.setAdapter(adapter)
        binding.firstStepperImage.setBackgroundResource(R.drawable.stepone)

    }

    override fun setUpListeners() {
        binding.btnProcess.setOnClickListener {
            when (position) {
                1 -> {
                    checkValidationFisrtStep()
                }
                2 -> {
                    checkValidationSecondStep()
                }
                3 -> {
                    binding.llThree.visibility = View.GONE
                    binding.llFour.visibility = View.VISIBLE
                    position = 4
                    binding.firstStepperImage.background = resources.getDrawable(R.drawable.stepdone)
                    binding.secondStepperImage.background = resources.getDrawable(R.drawable.selected_rounded_shape)
                    binding.secondStepperImage.setTextColor(resources.getColor(R.color.white))
                    binding.secondStepperImage.text = position.toString()
                    binding.btnProcess.text =  getString(R.string.submit)
                    binding.tvSecondText.text = getString(R.string.retailer_agreement)
                    binding.tvFistText.text = getString(R.string.verify_Pan)
                }
                4 -> {
                    binding.llFour.visibility = View.GONE
                    binding.llFive.visibility = View.VISIBLE
                    position = 5
                    binding.firstStepperImage.background = resources.getDrawable(R.drawable.stepdone)
                    binding.secondStepperImage.background = resources.getDrawable(R.drawable.selected_rounded_shape)
                    binding.secondStepperImage.setTextColor(resources.getColor(R.color.white))
                    binding.secondStepperImage.text = position.toString()
                    binding.btnProcess.text =  getString(R.string.submit)
                    binding.tvSecondText.text = getString(R.string.bank_Detail)
                    binding.tvFistText.text = getString(R.string.retailer_agreement)
                }
                5 -> {
                   // checkValidationFiveStep()
                    binding.llFive.visibility = View.GONE
                    binding.llSix.visibility = View.VISIBLE
                    position = 6
                    binding.lastStepperImageNew.visibility=View.VISIBLE
                    binding.secondStepperImage.visibility=View.GONE
                    binding.lastStepperImage.background = resources.getDrawable(R.drawable.selected_rounded_shape)
                    binding.lastStepperImage.setTextColor(resources.getColor(R.color.white))
                    binding.lastStepperImage.text = position.toString()
                    binding.tvFistText.setTextColor(resources.getColor(R.color.text_color))
                    binding.tvSecondText.setTextColor(resources.getColor(R.color.text_color))
                    binding.tvLastText.setTextColor(resources.getColor(R.color.step_text_color))
                    binding.btnProcess.text =  getString(R.string.proceed)

                }else -> {
                  //  position = 0
                   launchNewFirstActivity(MainActivity::class.java)
                }
            }
        }
        binding.tvBrowser.setOnClickListener {
            selectFile()
           /* if (ContextCompat.checkSelfPermission(
                    this@StepperActivity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                storagePermision.launch(Manifest.permission.CAMERA)
            } else {
                selectfile()
            }*/
        }
        binding.edtSelectAccountType.setOnTouchListener(OnTouchListener { v, event ->
            binding.edtSelectAccountType.showDropDown()
            false
        })
       binding.tvSendOtp.setOnClickListener {
           if(binding.edtAadhar.text.isNullOrEmpty()){
               binding.errorAadharNumber.visibility =View.VISIBLE
           }else{
               binding.errorAadharNumber.visibility =View.GONE
           }

           if(binding.edtAadhar!!.text?.length!! < 12 ){
               binding.errorAadharNumber.visibility =View.VISIBLE
               binding.errorAadharNumber.text = "Please enter 12 digit Aadhar number"
           }else{
               binding.errorAadharNumber.visibility =View.GONE
           }

           if(!binding.edtAadhar.text.isNullOrEmpty() && binding.edtAadhar!!.text?.length == 12 ){

           }
       }

    }

    private fun checkValidationFiveStep() {
        if(binding.edtSelect.text.isNullOrEmpty()){
            binding.errorSelectBank.visibility =View.VISIBLE
        }else{
            binding.errorSelectBank.visibility =View.GONE
        }

        if(binding.edtEnterOtp.text.isNullOrEmpty()){
            binding.errorIFSECode.visibility =View.VISIBLE
        }else{
            binding.errorIFSECode.visibility =View.GONE
        }
        if(binding.edtSelectAccountType.text.isNullOrEmpty()){
            binding.errorSelectAccontType.visibility =View.VISIBLE
        }else{
            binding.errorSelectAccontType.visibility =View.GONE
        }

        if(binding.EdtAccountNumber.text.isNullOrEmpty()){
            binding.errorAccountNumber.visibility =View.VISIBLE
        }else{
            binding.errorAccountNumber.visibility =View.GONE
        }

        if(!binding.edtSelect.text.isNullOrEmpty() && !binding.edtEnterOtp.text.isNullOrEmpty() &&
            binding.edtSelectAccountType.text.isNullOrEmpty() && !binding.EdtAccountNumber.text.isNullOrEmpty()) {

            binding.llFive.visibility = View.GONE
            binding.llSix.visibility = View.VISIBLE
            position = 6
            binding.lastStepperImageNew.visibility=View.VISIBLE
            binding.secondStepperImage.visibility=View.GONE
            binding.lastStepperImage.background = resources.getDrawable(R.drawable.selected_rounded_shape)
            binding.lastStepperImage.setTextColor(resources.getColor(R.color.white))
            binding.lastStepperImage.text = position.toString()
            binding.tvFistText.setTextColor(resources.getColor(R.color.text_color))
            binding.tvSecondText.setTextColor(resources.getColor(R.color.text_color))
            binding.tvLastText.setTextColor(resources.getColor(R.color.step_text_color))
            binding.btnProcess.text =  getString(R.string.proceed)
        }
    }

    private fun checkValidationSecondStep() {
        if(binding.edtAadhar.text.isNullOrEmpty()){
            binding.errorAadharNumber.visibility =View.VISIBLE
        }else{
            binding.errorAadharNumber.visibility =View.GONE
        }

        if(binding.edtEnterOtp.text.isNullOrEmpty()){
            binding.errorAadharOtp.visibility =View.VISIBLE
        }else{
            binding.errorAadharOtp.visibility =View.GONE
        }

        if(!binding.edtAadhar.text.isNullOrEmpty() && binding.edtAadhar!!.text?.length == 12 && !binding.edtEnterOtp.text.isNullOrEmpty()){
            binding.llTwo.visibility = View.GONE
            binding.llThree.visibility = View.VISIBLE
            position = 3
            binding.firstStepperImage.background = resources.getDrawable(R.drawable.stepdone)
            binding.secondStepperImage.background = resources.getDrawable(R.drawable.selected_rounded_shape)
            binding.secondStepperImage.setTextColor(resources.getColor(R.color.white))
            binding.secondStepperImage.text = position.toString()
            binding.tvSecondText.text = getString(R.string.verify_Pan)
            binding.tvFistText.text = getString(R.string.business_Detail)
            binding.tvFistText.text = getString(R.string.verify_Aadhaar)
        }
    }

    private fun checkValidationFisrtStep() {
          if(binding.edtBusinessName.text.isNullOrEmpty()){
              binding.errorBusinessName.visibility =View.VISIBLE
          }else{
              binding.errorBusinessName.visibility =View.GONE
          }

        if(binding.edtTypeOfBusiness.text.isNullOrEmpty()){
            binding.errorTypeOfBusiness.visibility =View.VISIBLE
        }else{
            binding.errorTypeOfBusiness.visibility =View.GONE
        }

        if(binding.edtAddress1.text.isNullOrEmpty()){
            binding.errorAddressOne.visibility =View.VISIBLE
        }else{
            binding.errorAddressOne.visibility =View.GONE
        }

        if(binding.edtPincode.text.isNullOrEmpty()){
            binding.errorPincode.visibility =View.VISIBLE
        }else{
            binding.errorPincode.visibility =View.GONE
        }

        if(binding.edtCity.text.isNullOrEmpty()){
            binding.errorCity.visibility =View.VISIBLE
        }else{
            binding.errorCity.visibility =View.GONE
        }
        if(binding.edtState.text.isNullOrEmpty()){
            binding.errorState.visibility =View.VISIBLE
        }else{
            binding.errorState.visibility =View.GONE
        }

        if(!binding.edtBusinessName.text.isNullOrEmpty() && !binding.edtTypeOfBusiness.text.isNullOrEmpty()  && !binding.edtAddress1.text.isNullOrEmpty()
             && !binding.edtPincode.text.isNullOrEmpty() && !binding.edtCity.text.isNullOrEmpty() && !binding.edtState.text.isNullOrEmpty()){
            binding.llOne.visibility = View.GONE
            binding.llTwo.visibility = View.VISIBLE
            position = 2
            binding.firstStepperImage.background = resources.getDrawable(R.drawable.stepdone)
            binding.secondStepperImage.background = getResources().getDrawable(R.drawable.selected_rounded_shape)
            binding.secondStepperImage.setTextColor(resources.getColor(R.color.white))
            binding.secondStepperImage.text = position.toString()
            binding.tvFistText.setTextColor(resources.getColor(R.color.text_color))
            binding.tvSecondText.setTextColor(resources.getColor(R.color.step_text_color))
            binding.btnProcess.text =  getString(R.string.submit)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBackPressed() {
        super.onBackPressed()
      /*  when (position) {
            0 -> {
                super.onBackPressed()
            }
            1 -> {
                binding.llOne.visibility = View.VISIBLE
                binding.llTwo.visibility = View.GONE
                position = 0
                binding.stepView.done(false)
                binding.stepView.go(position, true)
                binding.btnProcess.text = "Submit"
            }
            2 -> {
                binding.llTwo.visibility = View.VISIBLE
                binding.llThree.visibility = View.GONE
                position = 1
                binding.stepView.done(false)
                binding.stepView.go(position, true)
                binding.btnProcess.text = "Submit"
            }
            3 -> {
                binding.llFour.visibility = View.GONE
                binding.llThree.visibility = View.VISIBLE
                position = 2
                binding.stepView.done(false)
                binding.stepView.go(position, true)
                binding.btnProcess.text = "Submit"
            }
            else -> {
                binding.llFour.visibility = View.VISIBLE
                binding.llFive.visibility = View.GONE
                position = 3
                binding.stepView.done(false)
                binding.stepView.go(position, true)
                binding.btnProcess.text = "Submit"
            }
        }*/
    }
}