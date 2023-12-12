package com.example.paypointretailer.Model.Response.MainResponse

import java.io.Serializable

data class BalanceEntity(
    var CompanyName: String?,
    var Business: String?,
    var ClosingBalance: Double?,
    var LastTransactionDate: String?
):Serializable
