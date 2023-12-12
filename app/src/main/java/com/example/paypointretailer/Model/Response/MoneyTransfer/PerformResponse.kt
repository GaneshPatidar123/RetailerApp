package com.example.paypointretailer.Model.Response.MoneyTransfer

import java.io.Serializable

data class PerformResponse(
    var serviceRequestTypeField :String?,
    var msgDataField :String?,
    var additionalChargeField :String?,
    var responseStatusField :String?,
    var responseDescriptionField :String?,
    var sessionIDField :String?,
    var dateTimeField :String?,
) :Serializable
