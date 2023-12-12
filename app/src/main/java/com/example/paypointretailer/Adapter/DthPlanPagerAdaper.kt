package com.example.paypointretailer.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.paypointretailer.Model.Response.MobileRecharge.PlanListResponse
import com.example.paypointretailer.View.Fragement.DthPlanListFragment
import com.example.paypointretailer.View.Fragement.PlanListFragment

class DthPlanPagerAdaper(
    fm: FragmentManager?,
    var mNumOfTabs: Int,
    data: PlanListResponse
) :
    FragmentStatePagerAdapter(fm!!) {
    var data: PlanListResponse

    init {
        this.data = data
    }

    override fun getItem(position: Int): Fragment {
        return DthPlanListFragment.newInstance(data, position)!!
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}