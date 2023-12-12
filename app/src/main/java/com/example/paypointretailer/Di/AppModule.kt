package com.example.paypointretailer.Di

import com.example.paypointretailer.Api.ApiEndpoints
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.aabhasjindal.otptextview.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   /* @Provides
    @Singleton
    fun provideBaseUrl() = "https://api.open-meteo.com/"*/

    /*@Provides
    @Singleton
    fun provideConnectionTimeout() = NETWORK_TIMEOUT*/

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = if (BuildConfig.DEBUG) {
        /*val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }*/

        OkHttpClient
            .Builder()
            .connectTimeout(20, TimeUnit.SECONDS) // Set your desired connection timeout
            .readTimeout(20, TimeUnit.SECONDS)    // Set your desired read timeout
            .writeTimeout(20, TimeUnit.SECONDS)
            /*.addInterceptor(requestInterceptor)*/
            .addInterceptor(httpLoggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .connectTimeout(20, TimeUnit.SECONDS) // Set your desired connection timeout
            .readTimeout(20, TimeUnit.SECONDS)    // Set your desired read timeout
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
       /* var base_url: String = ""
        base_url = if (BuildConfig.DEBUG){
            "https://api.open-meteo.com/"
        } else{
            "https://api.open-meteo.com/"
        }*/

        var base_url: String = if (BuildConfig.FLAVOR.equals("dev", true)) {
             "https://mb2bstage.paypointindia.co.in/api/"
            } else if (BuildConfig.FLAVOR.equals("qa", true)) {
            "https://mb2bstage.paypointindia.co.in/api/"
            } else if (BuildConfig.FLAVOR.equals("prod", true)) {
                "https://mb2b.paypointindia.co.in/api"
            } else if (BuildConfig.FLAVOR.equals("stg", true)) {
                "https://mb2bstage.paypointindia.co.in/api/"
            } else {
                "https://mb2bstage.paypointindia.co.in/api/"
            }

        return Retrofit.Builder()
            .baseUrl(base_url/*BASE_URL*/)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiEndpoints = retrofit.create(ApiEndpoints::class.java)
}

