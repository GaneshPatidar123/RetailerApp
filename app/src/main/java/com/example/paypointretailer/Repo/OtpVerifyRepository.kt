package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Utils.AppUtils
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.Utils.StaticData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class OtpVerifyRepository(
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,) {
    suspend fun callOtpVerified(request: VerifiedOtpRequest): Flow<Resource<SignUpResponse>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs,request)
                val requestBody =
                    getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<SignUpResponse>? = null
                apiResponse = apiService.verifiedOtp(header,requestBody)

                if (apiResponse.isSuccessful){
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200){
                        try {
                            val jsonObject = JSONObject(Gson().toJson(response))
                            //val message = jsonObject.getString("message")
                            /*val jsonMain = jsonObject.getJSONObject("hourly")
                            val gson = Gson()
                            logD("CheckVerifyEmailIdResponseIns", "${jsonObject!!}")
                            val weatherData = gson.fromJson(jsonMain.toString(), SignUpResponse::class.java)*/
                            emit(Resource.Success(response))
                        }
                        catch (e: Exception){
                            logD("CheckVerifyEmailIdDataExceptionIn200", e.localizedMessage ?: "Exception")
                            emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            }
            catch(e: Exception){
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    private fun getHeader(sharedPrefs: SharedPreferences, request: VerifiedOtpRequest): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer "+request.token.toString()
        headerMap["key"] = request.key.toString()
        return headerMap
    }

    private fun getRequestBody(verifyEmailIdRequest: VerifiedOtpRequest): String {
        val body = JSONObject()
        body.put("Mobile", verifyEmailIdRequest.Mobile)
        body.put("OTP", verifyEmailIdRequest.OTP)
        body.put("Serial", verifyEmailIdRequest.Serial)
        body.put("Uuid", verifyEmailIdRequest.Uuid)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")

        return body.toString().replace("\\n", "")
    }

}