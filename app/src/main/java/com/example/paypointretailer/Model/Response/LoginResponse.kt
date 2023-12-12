package com.example.authdemo.models.response.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    var DeviceCode:String?=null,
    var DevicePassword: String?=null,
    var NewPassword: String? = null,
    var IdentificationCode: String? = null,
    var BusinessId: Int? = null,
    var Business: String? = null,
    var FirstName:String?=null,
    var LastName: String? = null,
    var EmailId: String? = null,
    var Mobile :String?=null,
    var ParentId: Int? = null,
    var BusinessIdentityID: String? = null,
    var PANNo:String?=null,
    var AadhaarNo: String? = null,
    var GSTNo:String?=null,
    var Category :String?=null,
    var Status:Int?=null,
    var EnableOTPValidation:Boolean?=null,
    var ENC_DevicePassword:String?=null,
    var ipayLatestVersion:String?=null,
    var MCCCode: String? = null,
    var AadharMobile: String? = null,
    var access_token : String?,
    var expires_in : String?
): Parcelable
