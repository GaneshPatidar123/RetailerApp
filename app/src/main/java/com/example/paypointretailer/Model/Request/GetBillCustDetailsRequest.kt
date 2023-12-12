package com.example.paypointretailer.Model.Request

data class GetBillCustDetailsRequest(
    var ProductAliasID :String?,
    var CANo : String?,
    var Opt1 :String?,
    var Opt3 : String?,
    var devicecode : String?,
    var icode :String?,
    var pcode : String?

)
