package com.example.paypointretailer.Model.Response.MobileRecharge

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class Result(
     var  PlanType :String?,
     var  TalkTime :String?,
     var  Description :String?,
     var  Validity :String?,
     var  Amount :String?,
):Parcelable
