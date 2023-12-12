package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Repo.MobileRechargeRepository
import com.example.paypointretailer.Repo.OtpVerifyRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobileRechargeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mobileRechargeRepository: MobileRechargeRepository,
): ViewModel(){
    private val _dataState: MutableLiveData<Resource<String>> = MutableLiveData()

    val dataState: LiveData<Resource<String>>
        get() = _dataState

    private val _dataStateRecharge: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val dataStateRecharge: LiveData<Resource<SignUpResponse>>
        get() = _dataStateRecharge

    fun getOperator(
        getMobileRechargeStateEvent: GetMobileRechargeStateEvent,
        accessToken: String?,
        number: String,) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getMobileRechargeStateEvent) {
                is GetMobileRechargeStateEvent.getOperatorEvent -> {
                    mobileRechargeRepository.callGetOperator(accessToken!!,number)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

    fun rechargeNow(
        getMobileRechargeStateEvent: GetMobileRechargeStateEvent,
       mobilreRechargeRequest: MobilreRechargeRequest,
      ) {
        _dataStateRecharge.value = Resource.Loading
        viewModelScope.launch {
            when (getMobileRechargeStateEvent) {
                is GetMobileRechargeStateEvent.rechargeNow -> {
                    mobileRechargeRepository.callRechargeNow(mobilreRechargeRequest)
                        .collect { dataState ->
                            _dataStateRecharge.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
}

sealed class GetMobileRechargeStateEvent {
    object getOperatorEvent : GetMobileRechargeStateEvent()
    object rechargeNow : GetMobileRechargeStateEvent()
}