package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.AepsServiceRequest
import com.example.paypointretailer.Model.Request.ChangePasswordRequest
import com.example.paypointretailer.Model.Response.MoneyTransfer.AepsServicesResponse
import com.example.paypointretailer.Model.Response.MoneyTransfer.PerformResponse
import com.example.paypointretailer.Utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class AepsRepository(
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,
) {
    suspend fun callServiceApplicationStatus(
        request: AepsServiceRequest,
    ): Flow<Resource<AepsServicesResponse>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(request)
                val requestBody =
                    getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<AepsServicesResponse>? = null
                apiResponse = apiService.callServiceApplicationApi(header, requestBody)

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
                            emit(Resource.Error(e.message!!))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                emit(Resource.Error(e.message!!))
            }

        }


    suspend fun callSelectPerformStatus(
        request: AepsServiceRequest,
        from :String,
    ): Flow<Resource<PerformResponse>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(request)
                val requestBody =
                    getRequestBodyPerfrom(from).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<PerformResponse>? = null
                apiResponse = apiService.callSelectPerformApi(header, requestBody)

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
                            emit(Resource.Error(e.message!!))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                emit(Resource.Error(e.message!!))
            }

        }

    private fun getHeader(
        accessToken: AepsServiceRequest
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer " + accessToken.authorization
        headerMap["devicecode"] = accessToken.devicecode.toString()
        headerMap["icode"] = accessToken.icode.toString()
        headerMap["key"] = accessToken.key.toString()
        headerMap["pcode"] = accessToken.pcode.toString()
        return headerMap
    }

    private fun getRequestBody(verifyEmailIdRequest: AepsServiceRequest): String {
        val body = JSONObject()
        /* body.put("BusinessId", verifyEmailIdRequest.BusinessId)
         body.put("DeviceCode", verifyEmailIdRequest.DeviceCode.toString())
         body.put("DevicePassword", verifyEmailIdRequest.DevicePassword)
         body.put("NewPassword", verifyEmailIdRequest.NewPassword.toString())*/
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }
    private fun getRequestBodyPerfrom(verifyEmailIdRequest: String): String {
        val body = JSONObject()
        body.put("Channel", verifyEmailIdRequest)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

}
