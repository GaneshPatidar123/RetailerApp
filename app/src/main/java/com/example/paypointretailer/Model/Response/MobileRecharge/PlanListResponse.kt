package com.example.paypointretailer.Model.Response.MobileRecharge

import java.io.Serializable

data class PlanListResponse(
    var result : MutableList<Result>?,
) :Serializable
