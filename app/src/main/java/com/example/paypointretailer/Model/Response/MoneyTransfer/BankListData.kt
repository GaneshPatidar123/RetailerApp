package com.example.paypointretailer.Model.Response.MoneyTransfer

import java.io.Serializable

data class BankListData(
    var BankId :String?,
    var BankName :String?,
    var BankCode :String?,
    var DefaultIFSCCode :String?,
    var IMPS :String?,
    var NEFT :String?,
    var Status :String?,
) :Serializable
