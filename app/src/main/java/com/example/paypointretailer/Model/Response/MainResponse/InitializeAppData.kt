package com.example.paypointretailer.Model.Response.MainResponse

import com.example.authdemo.models.response.auth.LoginResponse
import java.io.Serializable


data class InitializeAppData(
    var BusinessEntity: LoginResponse?,
    var AepsBalanceEntity : AepsBalanceEntity,
    var BalanceEntity : BalanceEntity,
    var SalesEntity : SalesEntity,
    var ProductEntity: MutableList<ProductEntity>,
    var BusinessNotification: BusinessNotification?,
    var NotificationList :MutableList<NotificationList>,
    var CircleCollection :MutableList<CircleCollection>,
):Serializable
