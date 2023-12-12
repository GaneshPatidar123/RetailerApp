package com.example.paypointretailer.Model.Response

data class VpaResponse(
    var ActiveUserDays:Int?,
    var BusinessVPAMasterResEntity : MutableList<VpaListData>?,
)
