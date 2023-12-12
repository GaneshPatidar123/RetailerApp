package com.example.paypointretailer.Utils

import android.content.Context
import android.content.SharedPreferences
import com.example.authdemo.models.response.auth.LoginResponse
import com.example.paypointretailer.Model.Response.MainResponse.InitializeAppData
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppSharedPref @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val USER_PREF = "userPreference"
        private const val IS_USER_NAME = "userNAme"
        private const val PASSWORD = "passowrd"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val USER_DETAILS = "userDetails"
        private const val USER_INITIAL_DATA = "iniTialData"
        private const val NOTIFICATION_STATUS = "notificationStatus"
        private const val DEVICE_TOKEN = "deviceToken"
    }

    private fun getSharedPreference(preferenceFile: String): SharedPreferences {
        return context.getSharedPreferences(preferenceFile, Context.MODE_PRIVATE)
    }

    private fun getSharedPreferenceEditor(
        preferenceFile: String
    ): SharedPreferences.Editor {
        return context.getSharedPreferences(preferenceFile, Context.MODE_PRIVATE).edit()
    }


    var isUserName: String?
        get() = getSharedPreference(USER_PREF).getString(IS_USER_NAME, " ")
        set(value) {
            getSharedPreferenceEditor(USER_PREF).putString(IS_USER_NAME, value).apply()
        }

    var passWord: String?
        get() = getSharedPreference(USER_PREF).getString(PASSWORD, " ")
        set(value) {
            getSharedPreferenceEditor(USER_PREF).putString(PASSWORD, value).apply()
        }


    var latitude: String?
        get() = getSharedPreference(USER_PREF).getString(LATITUDE, " ")
        set(value) {
            getSharedPreferenceEditor(USER_PREF).putString(LATITUDE, value).apply()
        }

    var longitude: String?
        get() = getSharedPreference(USER_PREF).getString(LONGITUDE, " ")
        set(value) {
            getSharedPreferenceEditor(USER_PREF).putString(LONGITUDE, value).apply()
        }


    fun saveData(data: LoginResponse?) {
        val jsonString = GsonBuilder().create().toJson(data)
        getSharedPreferenceEditor(USER_PREF).putString(USER_DETAILS, jsonString).apply()
    }
    fun getData(): LoginResponse? {
        val value = getSharedPreference(USER_PREF).getString(USER_DETAILS, " ")
        return GsonBuilder().create().fromJson(value, LoginResponse::class.java)
    }

    fun saveInitialData(data: InitializeAppData?) {
        val jsonString = GsonBuilder().create().toJson(data)
        getSharedPreferenceEditor(USER_PREF).putString(USER_INITIAL_DATA, jsonString).apply()
    }
    fun getInitialData(): InitializeAppData? {
        val value = getSharedPreference(USER_PREF).getString(USER_INITIAL_DATA, " ")
        return GsonBuilder().create().fromJson(value, InitializeAppData::class.java)
    }


    fun clear(){
        val editor: SharedPreferences.Editor = getSharedPreference(USER_PREF).edit()
        editor.clear()
        editor.apply()
    }

}