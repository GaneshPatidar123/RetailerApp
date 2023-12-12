package com.example.paypointretailer.View.Activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.ViewModel.BillPaymentViewModel
import com.example.paypointretailer.ViewModel.GetProfileDetailStateEvent
import com.example.paypointretailer.ViewModel.ProfileDetailsViewModel
import com.example.paypointretailer.databinding.ActivityEditProfileBinding
import com.example.paypointretailer.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile){
    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private val viewModel: ProfileDetailsViewModel by viewModels()
    override fun setUpViews() {
        binding.toolBar.tvTitle.text = getString(R.string.profile)
      //  viewModel.getProfileDetails(GetProfileDetailStateEvent.getProfile,)
    }

    override fun setUpListeners() {
        binding.toolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.edit.setOnClickListener {
            launch(EditProfileActivity::class.java)
        }
        binding.tvChangePassword.setOnClickListener {
            launch(ChangePasswordActivity::class.java)
        }
        binding.logout.setOnClickListener {
            pref.clear()
            launchNewFirstActivity(LoginActivity::class.java)
        }
    }
}