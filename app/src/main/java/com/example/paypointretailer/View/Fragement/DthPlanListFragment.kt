package com.example.paypointretailer.View.Fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.paypointretailer.Adapter.PlanListAdapter
import com.example.paypointretailer.Model.Response.MobileRecharge.PlanListResponse
import com.example.paypointretailer.Model.Response.MobileRecharge.Result
import com.example.paypointretailer.databinding.LayoutDthRechargeBinding
import com.example.paypointretailer.databinding.LayoutPlanlistBinding

class DthPlanListFragment :  Fragment() , PlanListAdapter.ClikcListnerAdapterInterface{
    private lateinit var binding: LayoutDthRechargeBinding
    private lateinit var adapter: PlanListAdapter
    val planTypeListMain: MutableList<Result> = mutableListOf()
    var position = 0

    var callback: OnDataReceivedListener? = null

    interface OnDataReceivedListener {
        fun onDataReceived(data: Result)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //  callback = requireActivity() as OnDataReceivedListener?;
        binding = LayoutDthRechargeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         if (getArguments() != null) {
            var allProducts = requireArguments().getSerializable("PLANLISTDATADTTH") as PlanListResponse
            this.position = requireArguments().getInt("KEY_POSITIONDTH");

            callback = activity as? OnDataReceivedListener

            var planTypeList = allProducts.result
                ?.distinctBy { it.PlanType }!!.toMutableList()
            planTypeListMain.clear()
            for(items in allProducts.result!!.indices){
                if (planTypeList[this.position].PlanType.equals(allProducts.result!![items].PlanType)){
                    val result = Result(allProducts.result!![items].PlanType,allProducts.result!![items].TalkTime,allProducts.result!![items].Description,allProducts.result!![items].Validity,allProducts.result!![items].Amount)
                    planTypeListMain.add(result)
                }
            }

            if(planTypeListMain!=null){
                adapter = PlanListAdapter(this, this)
                binding.rvList.adapter = adapter
                adapter.addItems(planTypeListMain.toList())
                adapter.notifyDataSetChanged()
            }

        }
    }

    companion object {
        fun newInstance(products: PlanListResponse, position: Int): DthPlanListFragment? {
            val fragment = DthPlanListFragment()
            val args = Bundle()
            args.putSerializable("PLANLISTDATADTTH", products)
            args.putInt("KEY_POSITIONDTH", position)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onSelectPlan(position: Int, result: Result) {
        callback?.onDataReceived(result)
    }
}

   /* override fun onSelectPlan(position: Int, result: Result) {
        callback?.onDataReceived(result)
        //  (activity as BrowserPlanActivity).finish()
    }
}*/