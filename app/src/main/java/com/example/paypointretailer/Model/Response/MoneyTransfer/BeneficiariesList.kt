package com.example.paypointretailer.Model.Response.MoneyTransfer

data class BeneficiariesList(
    var BeneId :Int?,
    var BeneficiaryMobile :String?,
    var BeneficiaryName :String?,
    var IFSCcode :String?,
    var AccountNo :String?,
    var Bankid :String?,
    var Bank :String?,
    var IsAcValidate :Int?,
    var Status : Int?,
    var ChannelStatus :Int?,
    var is_otp_required : Int?,
    var IMPS : Int?,
    var NEFT :Int?,
    var isSelect : Boolean?=false,
)
