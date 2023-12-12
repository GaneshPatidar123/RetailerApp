package com.example.paypointretailer.Model.Response.MoneyTransfer

import com.example.authdemo.models.response.auth.LoginResponse
import java.io.Serializable

data class AepsServicesResponse(
    var ICICI: String?,
    var PayTm: String?,
    var ICICINEW: String?,
    var NSDL: String?,
    var CHANNEL5: String?,
    var PreferChannel: String?,
    var Config: Config?,
) : Serializable
