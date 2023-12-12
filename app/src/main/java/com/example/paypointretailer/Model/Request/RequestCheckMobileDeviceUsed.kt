package com.example.paypointretailer.Model.Request

data class RequestCheckMobileDeviceUsed(
    val Cordova: String?,
    val IsVirtual: Boolean?,
    val Manufacturer: String?,
    val Model: String?,
    var Platform : String?,
    val Serial: String?,
    val Uuid: String?,
    var Version :String?,
    var key :Int?,
    var PushNotificationRegID : String?,
    var Mobile : String?,
)
