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
import com.example.paypointretailer.Repo.ProfileDetailsRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileDetailsRepository: ProfileDetailsRepository,
): ViewModel(){
    private val _dataState: MutableLiveData<Resource<InitializeAppData>> = MutableLiveData()

    val dataState: LiveData<Resource<InitializeAppData>>
        get() = _dataState

    fun getProfileDetails(getProfileDetailStateEvent: GetProfileDetailStateEvent, request: VerifiedOtpRequest) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getProfileDetailStateEvent) {
                is GetProfileDetailStateEvent.getProfile -> {
                    profileDetailsRepository.getProfileDetails(request)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }


}

sealed class GetProfileDetailStateEvent {
    object getProfile : GetProfileDetailStateEvent()
}