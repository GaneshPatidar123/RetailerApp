package com.example.paypointretailer.Model.Response.MobileRecharge

import java.io.Serializable

data class GetOperatorResponse(
    var opr_code: String?,
    var circle :String?,
):Serializable
