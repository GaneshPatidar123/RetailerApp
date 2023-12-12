package com.example.paypointretailer

import android.content.SharedPreferences
import android.os.Build
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.View.Fragement.DashBoardFragment
import com.example.paypointretailer.View.Fragement.LoadMoneyFragment
import com.example.paypointretailer.View.Fragement.ReportFragment
import com.example.paypointretailer.View.Fragement.SupportFragment
import com.example.paypointretailer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){
    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun setUpViews() {
        setCurrFragment(DashBoardFragment(),"DashBoard")
    }

    override fun setUpListeners() {
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dashBoard -> setCurrFragment(DashBoardFragment(),"DashBoard")
                R.id.leadMoney -> setCurrFragment(LoadMoneyFragment(),"DashBoard")
                R.id.reports -> setCurrFragment(ReportFragment(),"DashBoard")
                R.id.help -> setCurrFragment(SupportFragment(),"DashBoard")
            }
            true
        }
    }

     fun setCurrFragment(fragment : Fragment,isfrom :String) {
         if(isfrom.equals("Limit")){
             binding.bottomNav.selectedItemId  = R.id.leadMoney
         }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer,fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        if( binding.bottomNav.selectedItemId != R.id.dashBoard){
            binding.bottomNav.selectedItemId  = R.id.dashBoard
        }else{
            super.onBackPressed()
        }
    }

    override fun setUpObservers() {
      /*  mainViewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val userData = dataState.data
                    pref.saveInitialData(userData)

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
        })*/


    }
}