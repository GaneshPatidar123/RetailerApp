package com.example.paypointretailer.View.Fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.paypointretailer.MainActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.View.Activity.addLimit.CashDepositeActivity
import com.example.paypointretailer.View.Activity.addLimit.OnlinePaymentActivity
import com.example.paypointretailer.View.Activity.addLimit.UpiPaymentActivity
import com.example.paypointretailer.databinding.LayoutLoadMoneyBinding


class LoadMoneyFragment : Fragment() {

    private lateinit var binding: LayoutLoadMoneyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutLoadMoneyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupOnCLick()

    }

    private fun setupOnCLick() {
        binding.cardOnlinePayment.setOnClickListener {
            (activity as MainActivity).launch(OnlinePaymentActivity::class.java)
        }
        binding.cardUPI.setOnClickListener {
            (activity as MainActivity).launch(UpiPaymentActivity::class.java)
        }
        binding.cardCash.setOnClickListener {
            (activity as MainActivity).launch(CashDepositeActivity::class.java)
        }

    }

    private fun setupData() {
        binding.toolBar.ivNotification.visibility = View.VISIBLE
        binding.toolBar.ivHome.visibility = View.VISIBLE
        binding.toolBar.tvTitle.text = getString(R.string.load_money)
        binding.toolBar.ivBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu))
    }
}