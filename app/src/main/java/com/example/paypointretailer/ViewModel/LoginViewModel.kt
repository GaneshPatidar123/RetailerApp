package com.example.paypointretailer.ViewModel

import androidx.lifecycle.*
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.RequestCheckMobileDeviceUsed

import com.example.paypointretailer.Repo.LoginRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginRepository: LoginRepository,
): ViewModel() {
    //get login data
    private val _dataState: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val dataState: LiveData<Resource<SignUpResponse>>
        get() = _dataState

    private val _dataStateNew: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val deviceCheckState: LiveData<Resource<SignUpResponse>>
        get() = _dataStateNew

    fun setLoginEvent(getLoginDataStateEvent: GetLoginDataStateEvent, request: LoginRequest) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getLoginDataStateEvent) {
                is GetLoginDataStateEvent.LoginEvent -> {
                    loginRepository.callLoginUser(request)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

    fun checkMobileDeViceUsed(getLoginDataStateEvent: GetLoginDataStateEvent, request: RequestCheckMobileDeviceUsed) {
        _dataStateNew.value = Resource.Loading
        viewModelScope.launch {
            when (getLoginDataStateEvent) {
                is GetLoginDataStateEvent.CheckDeviceUsedEvent -> {
                    loginRepository.callCheckDevice(request)
                        .collect { dataState ->
                            _dataStateNew.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

    fun apiCallForgotPassword(getLoginDataStateEvent: GetLoginDataStateEvent, request: RequestCheckMobileDeviceUsed) {
        _dataStateNew.value = Resource.Loading
        viewModelScope.launch {
            when (getLoginDataStateEvent) {
                is GetLoginDataStateEvent.CheckDeviceUsedEvent -> {
                    loginRepository.apiCallForgotPassword(request)
                        .collect { dataState ->
                            _dataStateNew.value = dataState
                        }
                }
                else -> {}
            }
        }
    }


}

sealed class GetLoginDataStateEvent {
    object LoginEvent : GetLoginDataStateEvent()
    object CheckDeviceUsedEvent : GetLoginDataStateEvent()
}