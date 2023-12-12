package com.example.paypointretailer.View.Activity

import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.ActivityEditProfileBinding
import com.example.paypointretailer.databinding.ActivityProfileBinding

class EditProfileActivity  : BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile){
    override fun setUpViews() {
       binding.toolBar.tvTitle.text = getString(R.string.edit_profile)
    }

    override fun setUpListeners() {
      binding.toolBar.ivBack.setOnClickListener {
          onBackPressed()
      }
        binding.btnSave.setOnClickListener {
            onBackPressed()
        }
    }
}