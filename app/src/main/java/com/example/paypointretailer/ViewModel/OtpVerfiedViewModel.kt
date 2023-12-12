package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Repo.LoginRepository
import com.example.paypointretailer.Repo.OtpVerifyRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVerfiedViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val otpVerifyRepository: OtpVerifyRepository,
): ViewModel(){
    private val _dataState: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val dataState: LiveData<Resource<SignUpResponse>>
        get() = _dataState

    fun setOtpVerifiedEvent(getOtpVerifiedDataStateEvent: GetOtpVerifiedDataStateEvent, request: VerifiedOtpRequest) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getOtpVerifiedDataStateEvent) {
                is GetOtpVerifiedDataStateEvent.otpVerifiedEvent -> {
                    otpVerifyRepository.callOtpVerified(request)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

}

sealed class GetOtpVerifiedDataStateEvent {
    object otpVerifiedEvent : GetOtpVerifiedDataStateEvent()
}
