package com.example.paypointretailer.View.Fragement

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.paypointretailer.Adapter.ViewPagerAdapterLegder
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.OnChangeTabListner
import com.example.paypointretailer.Utils.UpdateFirstFragmentEvent
import com.example.paypointretailer.databinding.LayoutReportFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ReportFragment : Fragment(){

    private lateinit var binding: LayoutReportFragmentBinding
    val fragmentB = LegderFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutReportFragmentBinding.inflate(layoutInflater)
        EventBus.getDefault().register(this)
        binding.btnSearch.setOnClickListener {
            binding.viewPager.currentItem++
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          setupData()
    }
    override fun onDestroyView() {
        super.onDestroyView()

        // Unregister from EventBus in onDestroyView
        EventBus.getDefault().unregister(this)
    }
    private fun setupData() {
        binding.toolBar.tvTitle.text = getString(R.string.business_Report)
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("Ledger").setIcon(R.drawable.ic_selected_ledger))
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("Comission Report").setIcon(R.drawable.ic_unselected_report))
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("Transaction Status").setIcon(R.drawable.ic_unselected_transation))

        val adapter = ViewPagerAdapterLegder(this, 3)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Ledger"
                    tab.icon = resources.getDrawable(R.drawable.ic_selected_ledger)
                }
                1-> {
                    tab.text = "Comission Report"
                    tab.icon = resources.getDrawable(R.drawable.ic_unselected_report)
                }
                2 -> {
                    tab.text = "Transaction Status"
                    tab.icon = resources.getDrawable(R.drawable.ic_unselected_transation)
                }
            }


        }.attach()


      /*  binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
               *//* for (i in 1 until 3) {
                    if (i == position) {
                      //  dotViews[i]?.setImageResource(R.drawable.ic_dot_selected)
                    } else {
                      //  dotViews[i]?.setImageResource(R.drawable.ic_dot_unselected)
                    }
                }*//*
            }
        })*/
    }
    @Subscribe
    fun onEvent(updateEvent: UpdateFirstFragmentEvent) {
        // Handle the event and update UI in FirstFragment
        val updatedData = updateEvent.message
        if(updatedData.equals("firstFragement") || updatedData.equals("secondFragment")) {
            binding.viewPager.currentItem++
        }
    }




}