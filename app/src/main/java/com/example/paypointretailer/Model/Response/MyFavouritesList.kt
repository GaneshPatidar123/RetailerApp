package com.example.paypointretailer.Model.Response

import java.io.Serializable

data class MyFavouritesList(
    var RequestID :String?,
    var BusinessID :String?,
    var ProductIcon :String?,
    var ProductName :String?,
    var RedirectUrl :String?,
    var CreatedOn :String?,
): Serializable
