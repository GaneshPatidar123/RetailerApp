package com.example.paypointretailer.Model.Request

data class LoginRequest(
    val mobile: String?,
    val password: String?,
    val device_token: String?,
    val device_type: String?,
    var loginfrom : String?,
    val device_model: String?,
    val uu_id: String?,
    var latitude :String?,
    var longitude : String?,
    var platform :String?,
)

