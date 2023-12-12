package com.example.paypointretailer.Model.Response.BillPayment

import java.io.Serializable

data class GetServiceList(
    var ServiceTypeId : Int?=0,
    var ServiceType :String?=null,
    var DomainId : Int?=0,
    var Domain :String?=null,
    var ProductId : Int?=0,
    var Product :String?=null
):Serializable

