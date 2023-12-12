package com.example.paypointretailer.Repo

import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Extention.logD
import com.example.paypointretailer.Extention.logE
import com.example.paypointretailer.Model.Request.ChangePasswordRequest
import com.example.paypointretailer.Model.Request.GetBillCustDetailsRequest
import com.example.paypointretailer.Model.Request.MobilreRechargeRequest
import com.example.paypointretailer.Model.Response.BillPayment.CusttomerDetails
import com.example.paypointretailer.Model.Response.BillPayment.GetPayMode
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.Utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class BillPaymentRepository (
    private val apiService: ApiEndpoints,
private val sharedPrefs: SharedPreferences,
) {
    suspend fun callServiceListResponse(
        key: Int?,
        accessToken: String?,
        DomainId: Int?
    ): Flow<Resource<List<GetServiceList>>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeader(accessToken,key)
                val requestBody =
                    getRequestBody(DomainId!!).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<List<GetServiceList>>? = null
                apiResponse = apiService.getBillServiceList(header, requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            //val jsonObject = JSONObject(Gson().toJson(response))
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
                         //   emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
           //     emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    private fun getHeader(
        accessToken: String?,
        key: Int?
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["authorization"] = "bearer " + accessToken
        headerMap["key"] =  key.toString()
        return headerMap
    }

    private fun getRequestBody(DomainId: Int): String {
        val body = JSONObject()
        body.put("DomainId",DomainId)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }


    suspend fun callPayModeResponse(
        key: Int?,
    ): Flow<Resource<GetPayMode>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeaderPayMode(key)
                val requestBody =
                    getRequestBodyPayMode(key!!).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<GetPayMode>? = null
                apiResponse = apiService.getPayMode(header, requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            //val jsonObject = JSONObject(Gson().toJson(response))
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
                            //   emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                //     emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    private fun getHeaderPayMode(
        key: Int?
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        return headerMap
    }

    private fun getRequestBodyPayMode(ProductID: Int): String {
        val body = JSONObject()
        body.put("ProductID",ProductID)
        logE("CheckVerifyEmailIdRequest", "getRequestBody: ${body.toString()}")
        return body.toString().replace("\\n", "")
    }




    suspend fun callCustDetailsResponse(
        request: GetBillCustDetailsRequest?,
    ): Flow<Resource<CusttomerDetails>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"  //text/plain
                val header = getHeaderCustDetail(request)
                val requestBody =
                    getRequestBodyCustDetail(request!!).toRequestBody(contentType.toMediaTypeOrNull())
                var apiResponse: Response<CusttomerDetails>? = null
                apiResponse = apiService.custDetails(header, requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    //val status = response.status

                    logD("CheckVerifyEmailIdResponse", "${response!!}")

                    if (apiResponse.code() == 200) {
                        try {
                            //val jsonObject = JSONObject(Gson().toJson(response))
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
                            //   emit(Resource.Error("Verify Email Id Data Not Available"))
                        }
                    }
                }
            } catch (e: Exception) {
                logD("CheckVerifyEmailIdDataExceptionInCatch", e.localizedMessage ?: "Exception")
                //     emit(Resource.Error("Verify Email Id Data Not Available"))
            }

        }

    private fun getHeaderCustDetail(
        request: GetBillCustDetailsRequest?
    ): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["devicecode"] = request?.devicecode.toString()
        headerMap["icode"] = request?.icode.toString()
        headerMap["pcode"] = request?.pcode.toString()
        return headerMap
    }

    private fun getRequestBodyCustDetail(request: GetBillCustDetailsRequest): String {
        val body = JSONObject()
        body.put("ProductAliasID",request.ProductAliasID)
        body.put("CANo",request.CANo)
        body.put("Opt1",request.Opt1)
        body.put("Opt3",request.Opt3)
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
                apiResponse = apiService.callBillPayment(header,requestBody)

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
        body.put("BillType", request.BillType)
        body.put("SessionID", request.SessionID)
        body.put("PayMode", 1)
        body.put("CustomerMobileNo", request.CustomerMobileNo)
        body.put("Opt1", "")
        body.put("Opt2", "")
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