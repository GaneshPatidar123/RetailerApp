package com.example.paypointretailer.Adapter

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.paypointretailer.View.Fragement.CommisionFragment
import com.example.paypointretailer.View.Fragement.FirstFragment
import com.example.paypointretailer.View.Fragement.LegderFragment
import com.example.paypointretailer.View.Fragement.SecondFragment
import com.example.paypointretailer.View.Fragement.TransactionFragment


class  ViewPagerAdapterLegder(fragmentActivity: Fragment, private var totalCount: Int) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LegderFragment()
            1 -> CommisionFragment()
            2 -> TransactionFragment()
            else -> LegderFragment()
        }
    }

}