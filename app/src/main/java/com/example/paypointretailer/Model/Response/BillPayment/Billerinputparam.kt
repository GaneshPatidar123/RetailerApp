package com.example.paypointretailer.Model.Response.BillPayment

import java.io.Serializable

data class Billerinputparam(
    var paramName : String?,
    var dataType : String,
    var optional : Boolean,
    var minLength :Int?,
    var maxLength :Int?,
    var minValue : String?=null,
    var maxValue : String?=null,
    var visibility : Boolean,
    var regex : String?=null,
    var values : String?=null,
) :Serializable
