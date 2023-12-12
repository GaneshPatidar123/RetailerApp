package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.AepsServiceRequest
import com.example.paypointretailer.Model.Request.ChangePasswordRequest
import com.example.paypointretailer.Model.Response.MoneyTransfer.AepsServicesResponse
import com.example.paypointretailer.Model.Response.MoneyTransfer.PerformResponse
import com.example.paypointretailer.Repo.AepsRepository
import com.example.paypointretailer.Repo.ChangePasswordRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AepsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val aepsRepository: AepsRepository,
): ViewModel(){
    private val _dataState: MutableLiveData<Resource<AepsServicesResponse>> = MutableLiveData()

    val dataState: LiveData<Resource<AepsServicesResponse>>
        get() = _dataState

    private val _PerformdataState: MutableLiveData<Resource<PerformResponse>> = MutableLiveData()

    val perfromDataState: LiveData<Resource<PerformResponse>>
        get() = _PerformdataState

    fun callServiceApplicationStatus(
        getAEPSStateEvent: GetAEPSStateEvent,
        request: AepsServiceRequest,
    ) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getAEPSStateEvent) {
                is GetAEPSStateEvent.AEPSServices -> {
                    aepsRepository.callServiceApplicationStatus(request)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
    fun callSelectPerformStatus(
        getAEPSStateEvent: GetAEPSStateEvent,
        request: AepsServiceRequest,
        channel :String,
    ) {
        _PerformdataState.value = Resource.Loading
        viewModelScope.launch {
            when (getAEPSStateEvent) {
                is GetAEPSStateEvent.callSelectPerfrom -> {
                    aepsRepository.callSelectPerformStatus(request,channel)
                        .collect { dataState ->
                            _PerformdataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
}

sealed class GetAEPSStateEvent {
    object AEPSServices : GetAEPSStateEvent()
    object callSelectPerfrom : GetAEPSStateEvent()
}
