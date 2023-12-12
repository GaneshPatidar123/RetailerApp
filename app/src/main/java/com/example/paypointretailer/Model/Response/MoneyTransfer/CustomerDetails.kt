package com.example.paypointretailer.Model.Response.MoneyTransfer

data class CustomerDetails(
    var remitterName :String?,
    var remitterMobile :String?,
    var ActChannel :String?,
    var isVerified :Int?,
    var message :String?,
    var status :String?,
    var statusCode :String?,
    var Address :String?,
    var available_limit :Long?,
    var Month_limit :Long?,
    var Mode :Int?,
    var Pipe :MutableList<PipeData>?,
)
