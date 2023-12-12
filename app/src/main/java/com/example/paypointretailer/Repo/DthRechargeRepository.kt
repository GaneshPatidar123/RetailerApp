package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class DthRechargeRepository (
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,
) {
    suspend fun callCheckServiceNo(request: VerifiedOtpRequest): Flow<Resource<String>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs, request)
                val requestBody =
                    getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<String>? = null
                apiResponse = apiService.callCheckService(header, requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                         //   val jsonObject = JSONObject(Gson().toJson(response))
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
        request: VerifiedOtpRequest
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer "+request.token.toString()
        return headerMap
    }

    private fun getRequestBody(verifyEmailIdRequest: VerifiedOtpRequest): String {
        val body = JSONObject()
        body.put("ServiceNo", verifyEmailIdRequest.Mobile)
        body.put("Opt1", verifyEmailIdRequest.key.toString())
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }

    suspend fun callRechargeNow(request: MobilreRechargeRequest): Flow<Resource<SignUpResponse>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeaderRecharge(sharedPrefs,request)
                val requestBody =
                    getRequestBodyRecharge(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<SignUpResponse>? = null
                apiResponse = apiService.dthRechargeCall(header,requestBody)

                if (apiResponse.isSuccessful){
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200){
                        try {
                            emit(Resource.Success(apiResponse.body()!!))
                        }
                        catch (e: Exception){
                            logD("CheckVerifyEmailIdDataExceptionIn200", e.localizedMessage ?: "Exception")
                            //   emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            }
            catch(e: Exception){
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                //   emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    private fun getRequestBodyRecharge(request: MobilreRechargeRequest): String {
        val body = JSONObject()
        body.put("ServiceAcNo", request.ServiceAcNo)
        body.put("Amount", request.Amount)
        body.put("ProductID", request.ProductID)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }


    private fun getHeaderRecharge(sharedPrefs: SharedPreferences, request: MobilreRechargeRequest): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer "+request.authorization
        headerMap["businessid"] = request.businessid.toString()
        headerMap["devicecode"] =  request.devicecode.toString()
        headerMap["icode"] = request.icode.toString()
        headerMap["pcode"] = request.pcode.toString()
        return headerMap
    }
}