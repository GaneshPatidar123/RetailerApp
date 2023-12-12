package com.example.paypointretailer.Model.Response.MoneyTransfer

data class BeneficiariesData(
    var RemitterMobile : String?,
    var beneficiaries :MutableList<BeneficiariesList>?,
    var ActChannel :String?,
    var message :String?,
    var status :String?,
    var statusCode:String?,
    var Count :Int?,
    var is_otp_required : Int?,
)
