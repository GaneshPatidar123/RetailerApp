package com.example.paypointretailer.Model.Response

import java.io.Serializable

data class GiftDetailsData(
    var id : String?=null,
    var name :String?=null,
    var description :String?=null,
    var short_description : String?=null,
    var sku :String?=null,
    var price_type : String?=null,
    var min_custom_price :String?=null,
    var max_custom_price :String?=null,
    var custom_denominations :String?=null,
    var tnc_mobile :String?=null,
    var tnc_web :String?=null,
    var tnc_mail :String?=null,

):Serializable
