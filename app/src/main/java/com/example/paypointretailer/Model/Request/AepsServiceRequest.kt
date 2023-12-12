package com.example.paypointretailer.Model.Request

data class AepsServiceRequest(
    var authorization: String?,
    var key: String?,
    var devicecode: String?,
    var icode: String?,
    var pcode: String?,
)
