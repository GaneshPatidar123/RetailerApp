package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Model.Response.VpaResponse
import com.example.paypointretailer.Repo.ProfileDetailsRepository
import com.example.paypointretailer.Repo.UpiPaymentRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpiPaymentViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val upiPaymentRepository: UpiPaymentRepository,
): ViewModel(){
    private val _dataState: MutableLiveData<Resource<VpaResponse>> = MutableLiveData()

    val dataState: LiveData<Resource<VpaResponse>>
        get() = _dataState

    fun getUpiList(getUpiPaymentStateEven: UpiPaymentStateEvent, request: GetBillCustDetailsRequest) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getUpiPaymentStateEven) {
                is UpiPaymentStateEvent.getVPA -> {
                    upiPaymentRepository.getVPAList(request)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }


}

sealed class UpiPaymentStateEvent {
    object getVPA: UpiPaymentStateEvent()
}