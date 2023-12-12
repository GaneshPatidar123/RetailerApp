package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.GetRemitterDetailsRrquest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Request.VerifiedOtpRequest
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Model.Response.MoneyTransfer.BankListData
import com.example.paypointretailer.Utils.Resource
import com.example.paypointretailer.ViewModel.GetMoneyTransferStateEvent
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class MoneyTransferRepository (
    private val apiService: ApiEndpoints,
    private val sharedPrefs: SharedPreferences,
) {
    suspend fun getDetails(request: GetRemitterDetailsRrquest): Flow<Resource<String>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(sharedPrefs, request)
                val requestBody =
                    getRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<String>? = null
                apiResponse = apiService.getDetails(header, requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                           // val jsonObject = JSONObject(Gson().toJson(response))
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
        request: GetRemitterDetailsRrquest
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["authorization"] = "bearer "+request.Token.toString()
        headerMap["Content-Type"] = "application/json"
        headerMap["key"] = request.key.toString()
        return headerMap
    }

    private fun getRequestBody(verifyEmailIdRequest: GetRemitterDetailsRrquest): String {
        val body = JSONObject()
        body.put("MobileNo", verifyEmailIdRequest.MobileNo)
        body.put("ForcedChannelRefresh", verifyEmailIdRequest.ForcedChannelRefresh)
        body.put("PreferredMode", verifyEmailIdRequest.PreferredMode)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }


    suspend fun getBeneficialList(request: GetRemitterDetailsRrquest): Flow<Resource<String>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = geBeneficialtHeader(sharedPrefs, request)
                val requestBody =
                    getBeneficialRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<String>? = null
                apiResponse = apiService.getBeneficialList(header, requestBody)

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
                            emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                // emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    private fun geBeneficialtHeader(
        sharedPrefs: SharedPreferences,
        request: GetRemitterDetailsRrquest
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["authorization"] = "bearer "+request.Token.toString()
        headerMap["Content-Type"] = "application/json"
        headerMap["key"] = request.key.toString()
        return headerMap
    }

    private fun getBeneficialRequestBody(verifyEmailIdRequest: GetRemitterDetailsRrquest): String {
        val body = JSONObject()
        body.put("MobileNo", verifyEmailIdRequest.MobileNo)
      /*  body.put("ForcedChannelRefresh", verifyEmailIdRequest.ForcedChannelRefresh)
        body.put("PreferredMode", verifyEmailIdRequest.PreferredMode)*/
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }
    suspend fun getBankList(token: String,key :String): Flow<Resource<List<BankListData>>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = geBankListtHeader(sharedPrefs, token,key)
              /*  val requestBody =
                    getBeneficialRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())*/
                var apiResponse: Response<List<BankListData>>? = null
                apiResponse = apiService.getBankList(header)

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
                            emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                // emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

  suspend fun getServiceCharge(request: MobilreRechargeRequest): Flow<Resource<String>> =
      flow {
          try {
              val contentType = "application/json; charset=utf-8"  //text/plain
              val header = getChecktHeader(sharedPrefs,request)
                val requestBody =
                    getCheckServicelRequestBody(request).toRequestBody(contentType.toMediaTypeOrNull())
              var apiResponse: Response<String>? = null
              apiResponse = apiService.getCheckService(header,requestBody)

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
                          emit(Resource.Error("Verify Email Id Data Not Available"))
                      }
                  }
              }
          } catch (e: Exception) {
              logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
              // emit(Resource.Error("Verify Email Id Data Not Available"))
          }

      }

    private fun geBankListtHeader(
        sharedPrefs: SharedPreferences,
        token: String,
        key: String,
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["authorization"] = "bearer "+token.toString()
        headerMap["Content-Type"] = "application/json"
        headerMap["key"] = key.toString()
        return headerMap
    }

    private fun getChecktHeader(
        sharedPrefs: SharedPreferences,
         request: MobilreRechargeRequest
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["authorization"] = "bearer "+request.authorization
        headerMap["Content-Type"] = "application/json"
        headerMap["devicecode"] =request.devicecode.toString()
        headerMap["pcode"] =request.pcode.toString()
        headerMap["key"] = request.businessid.toString()
        return headerMap
    }

    private fun getCheckServicelRequestBody(verifyEmailIdRequest: MobilreRechargeRequest): String {
        val body = JSONObject()
        body.put("Amount", verifyEmailIdRequest.Amount)
        /*  body.put("ForcedChannelRefresh", verifyEmailIdRequest.ForcedChannelRefresh)
          body.put("PreferredMode", verifyEmailIdRequest.PreferredMode)*/
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }
}