package com.example.paypointretailer.Api

import com.example.authdemo.models.response.auth.SignUpResponse
import com.example.paypointretailer.Model.Response.BillPayment.CusttomerDetails
import com.example.paypointretailer.Model.Response.BillPayment.GetPayMode
import com.example.paypointretailer.Model.Response.BillPayment.GetServiceList
import com.example.paypointretailer.Model.Response.GiftDetailsData
import com.example.paypointretailer.Model.Response.ListGiftVoucherResponse
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.example.paypointretailer.Model.Response.MoneyTransfer.AepsServicesResponse
import com.example.paypointretailer.Model.Response.MoneyTransfer.BankListData
import com.example.paypointretailer.Model.Response.MoneyTransfer.PerformResponse
import com.example.paypointretailer.Model.Response.OttSubscriptionList
import com.example.paypointretailer.Model.Response.VpaResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface ApiEndpoints {

    @POST("Validate/Retailer")
    suspend fun loginUser(
        @HeaderMap headerMap: HashMap<String, String>,
    ): Response<SignUpResponse>

    @POST("business/MobileDeviceUsed")
    suspend fun checkMobile(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<SignUpResponse>


    @POST("business/devicevalidate")
    suspend fun verifiedOtp(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<SignUpResponse>

    @POST("initialize/App")
    suspend fun getMainData(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<InitializeAppData>

    @POST("Validate/ChangePwd")
    suspend fun geChangePassword(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<SignUpResponse>

    @POST("Validate/ForgotPwd")
    suspend fun callForgotPassword(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<SignUpResponse>


    @POST("recharge/mnpCheck")
    suspend fun getOperator(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>

    @POST("recharge/CreateTransaction")
    suspend fun callRecharge(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<SignUpResponse>


    @POST("business/billingassignedproduct")
    suspend fun getBillServiceList(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<List<GetServiceList>>

    @POST("product/GetProductProcessTypeAndProductPaymode")
    suspend fun getPayMode(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<GetPayMode>

    @POST("billpayment/ConsumerDetails")
    suspend fun custDetails(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<CusttomerDetails>


    @POST("BillPayment/CreateTransaction")
    suspend fun callBillPayment(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<SignUpResponse>


    @GET("hotelbooking/ppinl")
    suspend fun getHotelUrl(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<SignUpResponse>

    @GET("flightbooking/v3")
    suspend fun getFlightUrl(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<SignUpResponse>

    @GET("railbooking")
    suspend fun getTrainBookUrl(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<SignUpResponse>

    @POST("recharge/dthInfo")
    suspend fun callCheckService(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>


    @POST("recharge/CreateDTHTransaction")
    suspend fun dthRechargeCall(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<SignUpResponse>


    @GET("Account/PersonalDetails")
    suspend fun getProfileDetails(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<InitializeAppData>

    @GET("Limit/BusinessVPAMaster")
    suspend fun getVPAList(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<VpaResponse>

    @GET("GiftVoucher/Brands")
    suspend fun getGiftVoucherList(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<List<ListGiftVoucherResponse>>

    @GET("ott/product")
    suspend fun getOttSubscription(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<List<OttSubscriptionList>>


    @GET("ott/jiosaavnsubscription")
    suspend fun getJeoSavanUrl(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<SignUpResponse>


    @POST("GiftVoucher/BrandDetails")
    suspend fun getGiftDetails(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>

     @POST("ott/productdetail")
    suspend fun getOttDetails(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<GiftDetailsData>

    @POST("GiftVoucher/Create")
    suspend fun callBuyNow(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>

    @POST("ott/buysubscription")
    suspend fun callOttBuyNow(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>

    @POST("Aeps/ServiceApplicationStatus")
    suspend fun callServiceApplicationApi(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<AepsServicesResponse>

    @POST("Aeps/aeps2fa")
    suspend fun callSelectPerformApi(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<PerformResponse>

    @POST("DMT/RemitterDetails")
    suspend fun getDetails(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>

    @POST("DMT/Beneficiaries")
    suspend fun getBeneficialList(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>

    @GET("DMT/Banklist")
    suspend fun getBankList(
        @HeaderMap headerMap: HashMap<String, String>
    ): Response<List<BankListData>>


    @POST("DMT/GetServiceCharge")
    suspend fun getCheckService(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<String>

    /* @GET("v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl")
     suspend fun getWeatherData(
         @Query("latitude") lat: Double,
         @Query("longitude") long: Double
     ): Response<WeatherDataApiResponse>


     @GET("updatedefualtcard/{card_id}")
     suspend fun setCardDefaultStatusData(
         @HeaderMap headerMap: HashMap<String, String>,
         @Path(value = "card_id") card_id: String
     ): Response<SetCardDefaultStatusResponse>

  >*/
}