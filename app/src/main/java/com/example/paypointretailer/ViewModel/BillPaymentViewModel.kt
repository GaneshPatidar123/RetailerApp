package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.ChangePasswordRequest
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Response.BillPayment.CusttomerDetails
import com.example.paypointretailer.Model.Response.BillPayment.GetPayMode
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.Repo.BillPaymentRepository
import com.example.paypointretailer.Repo.ChangePasswordRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillPaymentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val billPaymentRepository: BillPaymentRepository,
) : ViewModel() {
    private val _dataState: MutableLiveData<Resource<List<GetServiceList>>> = MutableLiveData()

    val dataState: LiveData<Resource<List<GetServiceList>>>
        get() = _dataState

    private val _dataStatePayMode: MutableLiveData<Resource<GetPayMode>> = MutableLiveData()

    val dataStatePayMode: LiveData<Resource<GetPayMode>>
        get() = _dataStatePayMode


    private val _dataStateCustDetails: MutableLiveData<Resource<CusttomerDetails>> = MutableLiveData()

    val dataStateCustDetails: LiveData<Resource<CusttomerDetails>>
        get() = _dataStateCustDetails


    private val _dataStateRecharge: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val dataStateRecharge: LiveData<Resource<SignUpResponse>>
        get() = _dataStateRecharge
    fun getServiceList(
        getChangePasswordStateEvent: GetBillPaymentStateEvent,
        accessToken: String?,
        key: Int?,
        DomainId: Int?
    ) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getChangePasswordStateEvent) {
                is GetBillPaymentStateEvent.getListState -> {
                    billPaymentRepository.callServiceListResponse(key, accessToken,DomainId)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }

                else -> {}
            }
        }
    }
    fun getPayMode(
        getChangePasswordStateEvent: GetBillPaymentStateEvent,
        key: Int?
    ) {
        _dataStatePayMode.value = Resource.Loading
        viewModelScope.launch {
            when (getChangePasswordStateEvent) {
                is GetBillPaymentStateEvent.getPayMode -> {
                    billPaymentRepository.callPayModeResponse(key)
                        .collect { dataState ->
                            _dataStatePayMode.value = dataState
                        }
                }

                else -> {}
            }
        }
    }
    fun billPaymentNow(
        getBillPaymentStateEvent: GetBillPaymentStateEvent,
        mobilreRechargeRequest: MobilreRechargeRequest,
    ) {
        _dataStateRecharge.value = Resource.Loading
        viewModelScope.launch {
            when (getBillPaymentStateEvent) {
                is GetBillPaymentStateEvent.billPaymentNow -> {
                    billPaymentRepository.callRechargeNow(mobilreRechargeRequest)
                        .collect { dataState ->
                            _dataStateRecharge.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
    fun fetchCUstDetails(
        getChangePasswordStateEvent: GetBillPaymentStateEvent,
        getBillCustDetailsRequest: GetBillCustDetailsRequest?
    ) {
        _dataStateCustDetails.value = Resource.Loading
        viewModelScope.launch {
            when (getChangePasswordStateEvent) {
                is GetBillPaymentStateEvent.custDetails -> {
                    billPaymentRepository.callCustDetailsResponse(getBillCustDetailsRequest)
                        .collect { dataState ->
                            _dataStateCustDetails.value = dataState
                        }
                }

                else -> {}
            }
        }
    }
}

sealed class GetBillPaymentStateEvent {
    object getListState : GetBillPaymentStateEvent()
    object getPayMode : GetBillPaymentStateEvent()
    object custDetails : GetBillPaymentStateEvent()
    object billPaymentNow : GetBillPaymentStateEvent()
}