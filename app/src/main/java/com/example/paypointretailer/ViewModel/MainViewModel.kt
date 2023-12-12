package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Repo.MainRepository
import com.example.paypointretailer.Repo.OtpVerifyRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository,
): ViewModel(){
    private val _dataState: MutableLiveData<Resource<InitializeAppData>> = MutableLiveData()

    val dataState: LiveData<Resource<InitializeAppData>>
        get() = _dataState

    private val _dataStateURL: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val dataStateeURL: LiveData<Resource<SignUpResponse>>
        get() = _dataStateURL

    fun getMainData(getMainDataStateEvent: GetMainDataStateEvent, request: VerifiedOtpRequest) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getMainDataStateEvent) {
                is GetMainDataStateEvent.mainData -> {
                    mainRepository.callMainResponse(request)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }


    fun getHotelURl(getMainDataStateEvent: GetMainDataStateEvent, request: GetBillCustDetailsRequest,isfrom:String) {
        _dataStateURL.value = Resource.Loading
        viewModelScope.launch {
            when (getMainDataStateEvent) {
                is GetMainDataStateEvent.getHotelURl -> {
                    mainRepository.callgetHotelUrlResponse(request,isfrom)
                        .collect { dataState ->
                            _dataStateURL.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

    fun getJeoSavanUrl(getMainDataStateEvent: GetMainDataStateEvent, request: GetBillCustDetailsRequest,isfrom:String) {
        _dataStateURL.value = Resource.Loading
        viewModelScope.launch {
            when (getMainDataStateEvent) {
                is GetMainDataStateEvent.getHotelURl -> {
                    mainRepository.callgetHotelUrlResponse(request,isfrom)
                        .collect { dataState ->
                            _dataStateURL.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

}

sealed class GetMainDataStateEvent {
    object mainData : GetMainDataStateEvent()
    object getHotelURl : GetMainDataStateEvent()
}
