package com.example.paypointretailer.Model.Request

data class RequestBuyGift(
    var SenderEmailID :String?,
    var ReceiverEmailID : String?,
    var SenderMobileNo : String?,
    var ReceiverMobileNo : String?,
    var ValueOfVoucher : String?,
    var ProductID : Int,
    var SKU : String?,
    var devicecode: String?,
    var icode: String?,
    var pcode: String?,
    var key : Int,
    var toekn : String,
)
