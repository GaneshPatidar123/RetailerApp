package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.LoginRequest
import com.example.paypointretailer.Model.Request.RequestCheckMobileDeviceUsed
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


class LoginRepository(
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,
) {
    suspend fun callLoginUser(request: LoginRequest): Flow<Resource<SignUpResponse>> =

    flow {
        try {
            val contentType = "application/json; charset=utf-8"  //text/plain
            val header = getHeader(sharedPrefs,request)
            val requestBody =
                getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
            var apiResponse: Response<SignUpResponse>? = null
            apiResponse = apiService.loginUser(header)

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

    private fun getHeader(sharedPrefs: SharedPreferences, request: LoginRequest): HashMap<String, String> {

        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["latitude"] = request.latitude.toString()
        headerMap["loginfrom"] = request.loginfrom.toString()
        headerMap["longitude"] = request.longitude.toString()
        headerMap["password"] =  request.password.toString()
        headerMap["platform"] = request.platform.toString()
        headerMap["username"] = request.mobile.toString()
        headerMap["uuid"] = AppUtils.getUUID(sharedPrefs)
        headerMap["ver"] = StaticData.currentVersion
        return headerMap
    }

    private fun getRequestBody(verifyEmailIdRequest: LoginRequest): String {
        val body = JSONObject()
        body.put("mobile", verifyEmailIdRequest.mobile)
        body.put("password", verifyEmailIdRequest.password)
        body.put("device_token", verifyEmailIdRequest.device_token)
        body.put("device_type", verifyEmailIdRequest.device_type)
        body.put("device_model", verifyEmailIdRequest.device_model)
        body.put("uu_id", verifyEmailIdRequest.uu_id)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")

        return body.toString().replace("\\n", "")
    }

    suspend fun callCheckDevice(request: RequestCheckMobileDeviceUsed): Flow<Resource<SignUpResponse>> =

        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeaderCheck(sharedPrefs,request)
                val requestBody =
                    getRequestBodyCheck(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<SignUpResponse>? = null
                apiResponse = apiService.checkMobile(header,requestBody)

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
    private fun getHeaderCheck(sharedPrefs: SharedPreferences, request: RequestCheckMobileDeviceUsed): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["key"] = request.key.toString()
        return headerMap
    }

    private fun getRequestBodyCheck(verifyEmailIdRequest: RequestCheckMobileDeviceUsed): String {
        val body = JSONObject()
        body.put("IsVirtual", false)
        body.put("Manufacturer", verifyEmailIdRequest.Manufacturer)
        body.put("Model", verifyEmailIdRequest.Model)
        body.put("Platform", verifyEmailIdRequest.Platform)
        body.put("Serial", verifyEmailIdRequest.Serial)
        body.put("Uuid", verifyEmailIdRequest.Uuid)
        body.put("Version", verifyEmailIdRequest.Version)
        body.put("PushNotificationRegID", verifyEmailIdRequest.PushNotificationRegID)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")

        return body.toString().replace("\\n", "")
    }

    suspend fun apiCallForgotPassword(request: RequestCheckMobileDeviceUsed): Flow<Resource<SignUpResponse>> =

        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeaderForgotPassword(sharedPrefs,request)
                val requestBody =
                    getRequestBodyForgotPassword(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<SignUpResponse>? = null
                apiResponse = apiService.callForgotPassword(header,requestBody)

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

    private fun getHeaderForgotPassword(sharedPrefs: SharedPreferences, request: RequestCheckMobileDeviceUsed): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
    //    headerMap["key"] = request.key.toString()
        return headerMap
    }

    private fun getRequestBodyForgotPassword(verifyEmailIdRequest: RequestCheckMobileDeviceUsed): String {
        val body = JSONObject()
        body.put("Mobile", verifyEmailIdRequest.Mobile)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")

        return body.toString().replace("\\n", "")
    }

}