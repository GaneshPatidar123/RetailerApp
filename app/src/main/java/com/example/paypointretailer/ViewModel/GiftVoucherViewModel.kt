package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.RequestCheckMobileDeviceUsed
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.Model.Response.OttSubscriptionList
import com.example.paypointretailer.Repo.GiftVoucherRepository
import com.example.paypointretailer.Repo.LoginRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiftVoucherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val giftVoucherRepository: GiftVoucherRepository,
): ViewModel() {
    //get login data
    private val _dataState: MutableLiveData<Resource<List<ListGiftVoucherResponse>>> = MutableLiveData()

    val dataState: LiveData<Resource<List<ListGiftVoucherResponse>>>
        get() = _dataState

    private val _dataStateOtt: MutableLiveData<Resource<List<OttSubscriptionList>>> = MutableLiveData()

    val dataStateOtt: LiveData<Resource<List<OttSubscriptionList>>>
        get() = _dataStateOtt


    fun getGiftVoucherList(giftVoucherStateEvent: GiftVoucherStateEvent, request: String,isfrom :String) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (giftVoucherStateEvent) {
                is GiftVoucherStateEvent.getGiftVocuher -> {
                    giftVoucherRepository.getGiftList(request,isfrom)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
    fun getOttSubscrtpionList(giftVoucherStateEvent: GiftVoucherStateEvent, request: String,isfrom :String) {
        _dataStateOtt.value = Resource.Loading
        viewModelScope.launch {
            when (giftVoucherStateEvent) {
                is GiftVoucherStateEvent.getGiftVocuher -> {
                    giftVoucherRepository.getOttList(request,isfrom)
                        .collect { dataState ->
                            _dataStateOtt.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
}

sealed class GiftVoucherStateEvent {
    object getGiftVocuher : GiftVoucherStateEvent()
}

