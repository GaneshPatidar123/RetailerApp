package com.example.paypointretailer.Model.Request

data class MobilreRechargeRequest(
    var ServiceAcNo: String?,
    var Amount: String?,
    var ProductID: Int?,
    var authorization: String?,
    var businessid: String?,
    var devicecode: String?,
    var icode: String?,
    var pcode: String?,
    var SessionID: String? = null,
    var CustomerMobileNo: String? = null,
    var BillType: String? = null,
    var Opt1: String? = null,
    var Opt2: String? = null,
    var ChequeDate: String? = null,
    var ChequeNo: String? = null,
    var MICRCode: String? = null,
    var PayMode: String? = null,
)
