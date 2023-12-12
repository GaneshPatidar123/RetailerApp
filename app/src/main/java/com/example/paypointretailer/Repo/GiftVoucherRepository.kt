package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Model.Response.OttSubscriptionList
import com.example.paypointretailer.Utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class GiftVoucherRepository (
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,
) {
    suspend fun getGiftList(request: String,isfrom :String): Flow<Resource<List<ListGiftVoucherResponse>>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs, request)
                val requestBody =
                    getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<List<ListGiftVoucherResponse>>? = null
                 apiResponse = apiService.getGiftVoucherList(header)

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
    suspend fun getOttList(request: String,isfrom :String): Flow<Resource<List<OttSubscriptionList>>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs, request)
                val requestBody =
                    getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<List<OttSubscriptionList>>? = null
                    apiResponse = apiService.getOttSubscription(header)
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

    private fun getRequestBody(verifyEmailIdRequest: String): String {
        val body = JSONObject()
       /* body.put("IdentificationCode", verifyEmailIdRequest.Mobile)
        body.put("BusinessId", verifyEmailIdRequest.key.toString())*/
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

}