package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypointretailer.Model.Request.GetRemitterDetailsRrquest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.Model.Response.MoneyTransfer.BankListData
import com.example.paypointretailer.Repo.MoneyTransferRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoneyTransferViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val moneyTransferRepository: MoneyTransferRepository,
) : ViewModel() {
    private val _dataStateBeneficial: MutableLiveData<Resource<String>> = MutableLiveData()

    val dataStateBeneficial: LiveData<Resource<String>>
        get() = _dataStateBeneficial

    private val _dataStateDetails: MutableLiveData<Resource<String>> = MutableLiveData()

    val dataStateDetails: LiveData<Resource<String>>
        get() = _dataStateDetails

    private val _dataStateBankList: MutableLiveData<Resource<List<BankListData>>> = MutableLiveData()

    val dataStateBankList: LiveData<Resource<List<BankListData>>>
        get() = _dataStateBankList


    private val _dataStateCheckServices: MutableLiveData<Resource<String>> = MutableLiveData()

    val dataStateCheckService: LiveData<Resource<String>>
        get() = _dataStateCheckServices

    fun getDetails(
        getMoneyTransferStateEvent: GetMoneyTransferStateEvent,
        request: GetRemitterDetailsRrquest,
        ) {
        _dataStateDetails.value = Resource.Loading
        viewModelScope.launch {
            when (getMoneyTransferStateEvent) {
                is GetMoneyTransferStateEvent.getDetails -> {
                    moneyTransferRepository.getDetails(request)
                        .collect { dataState ->
                            _dataStateDetails.value = dataState
                        }
                }

                else -> {}
            }
        }
    }
    fun getBeneficialList(
        getMoneyTransferStateEvent: GetMoneyTransferStateEvent,
        request: GetRemitterDetailsRrquest,
    ) {
        _dataStateBeneficial.value = Resource.Loading
        viewModelScope.launch {
            when (getMoneyTransferStateEvent) {
                is GetMoneyTransferStateEvent.getBeneficial -> {
                    moneyTransferRepository.getBeneficialList(request)
                        .collect { dataState ->
                            _dataStateBeneficial.value = dataState
                        }
                }

                else -> {}
            }
        }
    }

    fun getBankList(getMoneyTransferStateEvent: GetMoneyTransferStateEvent.getBankList, accessToken: String?, businessId: Int?) {
        _dataStateBankList.value = Resource.Loading
        viewModelScope.launch {
            when (getMoneyTransferStateEvent) {
                is GetMoneyTransferStateEvent.getBankList -> {
                    moneyTransferRepository.getBankList(accessToken!!, businessId.toString())
                        .collect { dataState ->
                            _dataStateBankList.value = dataState
                        }
                }

                else -> {}
            }
        }
    }

    fun getServiceCharge(getMoneyTransferStateEvent: GetMoneyTransferStateEvent,request : MobilreRechargeRequest) {
        _dataStateCheckServices.value = Resource.Loading
        viewModelScope.launch {
            when (getMoneyTransferStateEvent) {
                is GetMoneyTransferStateEvent.getBankList -> {
                    moneyTransferRepository.getServiceCharge(request)
                        .collect { dataState ->
                            _dataStateCheckServices.value = dataState
                        }
                }

                else -> {}
            }
        }
    }

}

sealed class GetMoneyTransferStateEvent {
    object getDetails : GetMoneyTransferStateEvent()
    object getBeneficial : GetMoneyTransferStateEvent()
    object getBankList : GetMoneyTransferStateEvent()
    object GetServiceCharge : GetMoneyTransferStateEvent()
}
