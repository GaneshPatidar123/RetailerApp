package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class MainRepository(
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,
) {
    suspend fun callMainResponse(request: VerifiedOtpRequest): Flow<Resource<InitializeAppData>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs, request)
                val requestBody =
                    getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<InitializeAppData>? = null
                apiResponse = apiService.getMainData(header, requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            val jsonObject = JSONObject(Gson().toJson(response))
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
                            emit(Resource.Error("Verify Email Id Data Not Available"))
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
        request: VerifiedOtpRequest
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        /*  headerMap["authorization"] = "bearer "+request.token.toString()
           headerMap["key"] = request.key.toString()*/
        return headerMap
    }

    private fun getRequestBody(verifyEmailIdRequest: VerifiedOtpRequest): String {
        val body = JSONObject()
        body.put("IdentificationCode", verifyEmailIdRequest.Mobile)
        body.put("BusinessId", verifyEmailIdRequest.key.toString())
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

    suspend fun callgetHotelUrlResponse(
        request: GetBillCustDetailsRequest,
        isfrom: String
    ): Flow<Resource<SignUpResponse>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHotelUrlHeader(sharedPrefs, request)
                val requestBody =
                    getHotelUrlRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<SignUpResponse>? = null
                if (isfrom.equals("Hotel")) {
                    apiResponse = apiService.getHotelUrl(header)
                } else if (isfrom.equals("Flight")) {
                    apiResponse = apiService.getFlightUrl(header)
                } else if(isfrom.equals("Jio Savan")){
                    apiResponse = apiService.getJeoSavanUrl(header)
                } else {
                    apiResponse = apiService.getTrainBookUrl(header)
                }

                if (apiResponse!!.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            val jsonObject = JSONObject(Gson().toJson(response))
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
                            emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                // emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    private fun getHotelUrlHeader(
        sharedPrefs: SharedPreferences,
        request: GetBillCustDetailsRequest
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer " + request.Opt1.toString()
        headerMap["key"] = request.Opt3.toString()
        headerMap["devicecode"] = request.devicecode.toString()
        headerMap["icode"] = request.icode.toString()
        headerMap["pcode"] = request.pcode.toString()
        headerMap["uuid"] = request.CANo.toString()
        return headerMap
    }

    private fun getHotelUrlRequestBody(verifyEmailIdRequest: GetBillCustDetailsRequest): String {
        val body = JSONObject()
        /*  body.put("IdentificationCode", verifyEmailIdRequest.Mobile)
          body.put("BusinessId", verifyEmailIdRequest.key.toString())*/
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

}