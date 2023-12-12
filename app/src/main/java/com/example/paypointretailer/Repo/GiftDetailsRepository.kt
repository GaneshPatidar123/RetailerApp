package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.RequestBuyGift
import com.example.paypointretailer.Model.Response.GiftDetailsData
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.Model.Response.OttSubscriptionList
import com.example.paypointretailer.Utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class GiftDetailsRepository  (
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,
) {
    suspend fun getGiftDetails(token: String,id :String): Flow<Resource<String>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs, token)

                val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
                val requestBody = id.toRequestBody(mediaType)
                /*val requestBody =
                    getRequestBody(id).toRequestBody(contentType.toMediaTypeOrNull())*/
                var apiResponse: Response<String>? = null
                apiResponse = apiService.getGiftDetails(header,requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            //  val jsonObject = JSONObject(Gson().toJson(response))
                            //val message = jsonObject.getString("message")
                            /*val jsonMain = jsonObject.getJSONObject("hourly")
                            val gson = Gson()
                            logD("CheckVerifyEmailIdResponseIns", "${jsonObject!!}")
                            val weatherData = gson.fromJson(jsonMain.toString(), SignUpResponse::class.java)*/
                            emit(Resource.Success(response))
                        } catch (e: Exception) {
                            logD(
                                "CheckVerifyEmailIdDataExceptionIn200",
                                e.localizedMessage ?: "Exception"
                            )
                            //  emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                // emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }
    suspend fun getOttList(request: String,id :String): Flow<Resource<GiftDetailsData>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs, request)
                val requestBody =
                    getRequestBody(id).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<GiftDetailsData>? = null
                apiResponse = apiService.getOttDetails(header,requestBody)
                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            //  val jsonObject = JSONObject(Gson().toJson(response))
                            //val message = jsonObject.getString("message")
                            /*val jsonMain = jsonObject.getJSONObject("hourly")
                            val gson = Gson()
                            logD("CheckVerifyEmailIdResponseIns", "${jsonObject!!}")
                            val weatherData = gson.fromJson(jsonMain.toString(), SignUpResponse::class.java)*/
                            emit(Resource.Success(response))
                        } catch (e: Exception) {
                            logD(
                                "CheckVerifyEmailIdDataExceptionIn200",
                                e.localizedMessage ?: "Exception"
                            )
                            //  emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                // emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    suspend fun callBuyNow(request: RequestBuyGift,isfrom:String): Flow<Resource<String>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeaderBuyNow(sharedPrefs, request)
                val requestBody =
                    getRequestBodyBuyNow(request,isfrom).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<String>? = null
                if(isfrom.equals("Gift")){
                    apiResponse = apiService.callBuyNow(header,requestBody)
                }else{
                    apiResponse = apiService.callOttBuyNow(header,requestBody)
                }

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            //  val jsonObject = JSONObject(Gson().toJson(response))
                            //val message = jsonObject.getString("message")
                            /*val jsonMain = jsonObject.getJSONObject("hourly")
                            val gson = Gson()
                            logD("CheckVerifyEmailIdResponseIns", "${jsonObject!!}")
                            val weatherData = gson.fromJson(jsonMain.toString(), SignUpResponse::class.java)*/
                            emit(Resource.Success(response))
                        } catch (e: Exception) {
                            logD(
                                "CheckVerifyEmailIdDataExceptionIn200",
                                e.localizedMessage ?: "Exception"
                            )
                            //  emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                // emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }
    private fun getHeader(
        sharedPrefs: SharedPreferences,
        request: String
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer "+request.toString()
        return headerMap
    }

    private fun getRequestBody(id: String): String {
        val body = JSONObject()
        body.put("sp_key", id)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

    private fun getHeaderBuyNow(
        sharedPrefs: SharedPreferences,
        request: RequestBuyGift
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer "+request.toekn.toString()
        headerMap["devicecode"] = request.devicecode.toString()
        headerMap["icode"] = request.icode.toString()
        headerMap["pcode"] = request.pcode.toString()
        headerMap["Key"] = request.key.toString()
        return headerMap
    }

    private fun getRequestBodyBuyNow(request: RequestBuyGift,isfrom: String): String {
        val body = JSONObject()
        if(isfrom.equals("Gift")){
            body.put("ValueOfVoucher", request.ValueOfVoucher)
            body.put("SenderEmailID", request.SenderEmailID)
            body.put("SenderMobileNo", request.SenderMobileNo)
            body.put("ReceiverEmailID", request.ReceiverEmailID)
            body.put("ReceiverMobileNo", request.ReceiverMobileNo)
            body.put("SKU", request.SKU)
            body.put("ProductID", request.ProductID)
        }else{
            body.put("Amount", request.ValueOfVoucher)
            body.put("SEmailID", request.SenderEmailID)
            body.put("SMobileNo", request.SenderMobileNo)
            body.put("REmailID", request.ReceiverEmailID)
            body.put("RMobileNo", request.ReceiverMobileNo)
            body.put("SKU", request.SKU)
            body.put("Key", request.ProductID)
        }
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

}