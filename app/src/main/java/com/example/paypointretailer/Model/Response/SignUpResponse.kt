package com.example.authdemo.models.response.auth

import com.example.paypointretailer.Model.Response.TokenData

data class SignUpResponse(
    var BE: LoginResponse?,
    var AT : TokenData,
    var BS : Any,
    var ErrDesc: String="",
    var Status: String?,
    var Msg :String?,
    var ErrorDes :String?,
    var description :String?
)
