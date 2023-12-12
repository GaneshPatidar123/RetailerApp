package com.example.paypointretailer.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypointretailer.Model.Request.RequestBuyGift
import com.example.paypointretailer.Model.Response.GiftDetailsData
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.Model.Response.OttSubscriptionList
import com.example.paypointretailer.Repo.GiftDetailsRepository
import com.example.paypointretailer.Repo.GiftVoucherRepository
import com.example.paypointretailer.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyGiftViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val giftVoucherRepository: GiftDetailsRepository,
): ViewModel() {
    //get login data
    private val _dataStateOtt: MutableLiveData<Resource<GiftDetailsData>> = MutableLiveData()

    val dataStateOTT: LiveData<Resource<GiftDetailsData>>
        get() = _dataStateOtt

    private val _dataState: MutableLiveData<Resource<String>> = MutableLiveData()

    val dataState: LiveData<Resource<String>>
        get() = _dataState

    private val _dataStateBuyNow: MutableLiveData<Resource<String>> = MutableLiveData()

    val dataStateBuyNow: LiveData<Resource<String>>
        get() = _dataStateBuyNow

    fun getGiftDetails(giftVoucherStateEvent: BuyGiftDetailsStateEvent, token: String,id :String) {
        _dataState.value = Resource.Loading
        viewModelScope.launch {
            when (giftVoucherStateEvent) {
                is BuyGiftDetailsStateEvent.getGIftDetails -> {
                    giftVoucherRepository.getGiftDetails(token,id)
                        .collect { dataState ->
                            _dataState.value = dataState
                        }
                }
                else -> {}
            }
        }
    }

    fun getOttDetails(giftVoucherStateEvent: BuyGiftDetailsStateEvent, token: String,id :String) {
        _dataStateOtt.value = Resource.Loading
        viewModelScope.launch {
            when (giftVoucherStateEvent) {
                is BuyGiftDetailsStateEvent.getGIftDetails -> {
                    giftVoucherRepository.getOttList(token,id)
                        .collect { dataState ->
                            _dataStateOtt.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
    fun callBuyNow(giftVoucherStateEvent: BuyGiftDetailsStateEvent, request : RequestBuyGift,isfrom:String) {
        _dataStateBuyNow.value = Resource.Loading
        viewModelScope.launch {
            when (giftVoucherStateEvent) {
                is BuyGiftDetailsStateEvent.BuyNow -> {
                    giftVoucherRepository.callBuyNow(request,isfrom)
                        .collect { dataState ->
                            _dataStateBuyNow.value = dataState
                        }
                }
                else -> {}
            }
        }
    }
}

sealed class BuyGiftDetailsStateEvent {
    object getGIftDetails : BuyGiftDetailsStateEvent()
    object BuyNow : BuyGiftDetailsStateEvent()
}
