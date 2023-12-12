package com.example.paypointretailer.Model.Response.BillPayment

data class CusttomerDetails(
    var Status: Int?,
    var ErrorDesc : String?,
    var CustomerName : String?,
    var BillAmount : String?,
    var BillNo : String?,
    var BillDate : String?,
    var DueDate : String?,
    var SessionId : String?,

)
