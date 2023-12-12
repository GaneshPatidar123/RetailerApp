package com.example.paypointretailer.Model.Response.MobileRecharge

import java.io.Serializable

data class GetOperatorCode(
    var status: String?=null,
    var data :GetOperatorResponse?=null,
    var code :String?=null,
    var description :String?=null,
    var Msg :String?=null,
):Serializable
