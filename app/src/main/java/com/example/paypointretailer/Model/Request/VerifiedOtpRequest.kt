package com.example.paypointretailer.Model.Request

data class VerifiedOtpRequest(
    val Mobile: String?,
    val OTP: String?,
    val Serial: String?,
    val Uuid: String?,
    var key :Int?,
    var token: String?,
)
