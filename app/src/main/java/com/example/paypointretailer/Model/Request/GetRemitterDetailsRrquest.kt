package com.example.paypointretailer.Model.Request

data class GetRemitterDetailsRrquest(
    var MobileNo :String?,
    var PreferredMode :String?,
    var ForcedChannelRefresh :String?,
    var Token :String?,
    var key :String?,
)
