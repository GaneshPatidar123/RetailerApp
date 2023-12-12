package com.example.paypointretailer.Model.Response

import java.io.Serializable

data class GiftDetailsResponse(
    var Status: String?=null,
    var ErrorDes :String?=null,
    var Msg :String?=null,
) :Serializable
