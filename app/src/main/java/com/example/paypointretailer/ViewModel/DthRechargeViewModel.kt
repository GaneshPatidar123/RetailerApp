package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Repo.DthRechargeRepository
import com.example.paypointretailer.Repo.MainRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DthRechargeViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dthRechargeRepository: DthRechargeRepository,
): ViewModel(){

    private val _dataStateURL: MutableLiveData<Resource<String>> = MutableLiveData()

    val dataStateeURL: LiveData<Resource<String>>
        get() = _dataStateURL

    private val _dataStateRecharge: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val dataStateRecharge: LiveData<Resource<SignUpResponse>>
        get() = _dataStateRecharge

    fun callCheckServiceNoAPI(getMainDataStateEvent: GetDTHRechargedataStateEvent, request: VerifiedOtpRequest) {
        _dataStateURL.value = Resource.Loading
        viewModelScope.launch {
            when (getMainDataStateEvent) {
                is GetDTHRechargedataStateEvent.checkService -> {
                    dthRechargeRepository.callCheckServiceNo(request)
                        .collect { dataState ->
                            _dataStateURL.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
    fun dthRechargeNow(
        getMobileRechargeStateEvent: GetDTHRechargedataStateEvent,
        mobilreRechargeRequest: MobilreRechargeRequest,
    ) {
        _dataStateRecharge.value = Resource.Loading
        viewModelScope.launch {
            when (getMobileRechargeStateEvent) {
                is GetDTHRechargedataStateEvent.rechargeNow -> {
                    dthRechargeRepository.callRechargeNow(mobilreRechargeRequest)
                        .collect { dataState ->
                            _dataStateRecharge.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
}

sealed class GetDTHRechargedataStateEvent {
    object checkService : GetDTHRechargedataStateEvent()
    object rechargeNow : GetDTHRechargedataStateEvent()
}