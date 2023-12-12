package com.example.paypointretailer.Model.Response.MainResponse

import java.io.Serializable

data class AepsBalanceEntity(
    var CurrentBalance: Double?,
    var UnClearBalance: Double?,
):Serializable
