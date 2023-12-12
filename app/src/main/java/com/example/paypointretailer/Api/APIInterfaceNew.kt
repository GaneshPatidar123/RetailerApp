package com.example.paypointretailer.Api

import com.example.paypointretailer.Model.Response.MyFavouritesList
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url





interface APIInterfaceNew {
    @GET
    fun getSend(@Url url: String?): Call<String?>?


    @POST("LoadBusinessFavourites")
    suspend  fun getFavouritesList(
        @HeaderMap headerMap: HashMap<String, String>,
        @Body data: RequestBody
    ): Response<List<MyFavouritesList>>
}