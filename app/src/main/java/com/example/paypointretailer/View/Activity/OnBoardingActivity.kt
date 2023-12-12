package com.example.paypointretailer.View.Activity

import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.paypointretailer.Adapter.ViewPagerAdapter
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.Extention.dpToPx
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppLifecycleObserver
import com.example.paypointretailer.Utils.ApplicationClass
import com.example.paypointretailer.databinding.ActivityOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    private lateinit var appLifecycleObserver: AppLifecycleObserver
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    override fun setUpViews() {
        appLifecycleObserver =(application as ApplicationClass).appLifecycleObserver
        setupViewPager()
    }
    override fun setUpListeners() {
        binding.btnNext.setOnClickListener {
            if(binding.viewPager.currentItem==1){
                launch((LoginActivity::class.java))
                finish()
            }else {
                binding.viewPager.currentItem++
                if( binding.viewPager.currentItem == 0){
                    binding.tvSkip.visibility=View.VISIBLE
                    binding.btnRegister.visibility=View.GONE
                    binding.btnNext.text = resources.getText(R.string.next)
                    changeWidthTwo()
                }else{
                    binding.tvSkip.visibility=View.GONE
                    binding.btnRegister.visibility=View.VISIBLE
                    binding.btnNext.text = resources.getText(R.string.login)
                    changeWidthOne()
                }

            }
        }
        binding.btnRegister.setOnClickListener {
            launch(RegisterActivity::class.java)
        }
        binding.tvSkip.setOnClickListener {
            launch(LoginActivity::class.java)
            finish()
        }
    }


    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)

        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
               if(position==0 ){
                   binding.btnRegister.visibility=View.GONE
                   binding.tvSkip.visibility=View.VISIBLE
                   binding.btnNext.text = resources.getText(R.string.next)
                   changeWidthTwo()
               }else{
                   binding.tvSkip.visibility=View.GONE
                   binding.btnRegister.visibility=View.VISIBLE
                   binding.btnNext.text = resources.getText(R.string.login)
                   changeWidthOne()
               }
            }
        })

    }

    private fun changeWidthOne() {
        val params: ViewGroup.LayoutParams =  binding.tvOne.getLayoutParams()
        params.height =  dpToPx(6)
        params.width = dpToPx(6)
        binding.tvOne.setLayoutParams(params)
        val paramstwo: ViewGroup.LayoutParams =  binding.tvTwo.getLayoutParams()
        paramstwo.height =  dpToPx(6)
        paramstwo.width = dpToPx(16)
        binding.tvTwo.setLayoutParams(paramstwo)
        binding.tvOne.background = resources.getDrawable(R.drawable.indicator_unselected_background)
        binding.tvTwo.background = resources.getDrawable(R.drawable.indicator_selected_background)
    }

    private fun changeWidthTwo() {
        val params: ViewGroup.LayoutParams =  binding.tvOne.getLayoutParams()
        params.height = dpToPx(6)
        params.width =dpToPx(16)
        binding.tvOne.setLayoutParams(params)
        val paramstwo: ViewGroup.LayoutParams =  binding.tvTwo.getLayoutParams()
        paramstwo.height = dpToPx(6)
        paramstwo.width = dpToPx(6)
        binding.tvTwo.setLayoutParams(paramstwo)
        binding.tvTwo.background = resources.getDrawable(R.drawable.indicator_unselected_background)
        binding.tvOne.background = resources.getDrawable(R.drawable.indicator_selected_background)
    }

    override fun onResume() {
        super.onResume()
       /* if (!appLifecycleObserver.isAppInForeground()) {
            // App has come to the foreground from the background
            if(pref.getData()!=null) {
                launch(AuthenticationActivity::class.java)
            } else{
                super.onResume()
            }

        }*/
    }
}