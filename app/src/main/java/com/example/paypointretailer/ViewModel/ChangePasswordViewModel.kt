package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.ChangePasswordRequest
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Repo.ChangePasswordRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val changePasswordRepository: ChangePasswordRepository,
): ViewModel(){
    private val _dataState: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()

    val dataState: LiveData<Resource<SignUpResponse>>
        get() = _dataState

    fun changePassword(
        getChangePasswordStateEvent: GetChangePasswordStateEvent,
        request: ChangePasswordRequest,
        accessToken: String?
    ) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (getChangePasswordStateEvent) {
                is GetChangePasswordStateEvent.changePasswordState -> {
                    changePasswordRepository.callChangePassResponse(request,accessToken)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

}

sealed class GetChangePasswordStateEvent {
    object changePasswordState : GetChangePasswordStateEvent()
}
