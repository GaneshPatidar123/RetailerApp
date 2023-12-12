package com.example.paypointretailer.Di

import android.content.SharedPreferences
import com.example.paypointretailer.Api.ApiEndpoints
import com.example.paypointretailer.Repo.AepsRepository
import com.example.paypointretailer.Repo.BillPaymentRepository
import com.example.paypointretailer.Repo.ChangePasswordRepository
import com.example.paypointretailer.Repo.DthRechargeRepository
import com.example.paypointretailer.Repo.GiftDetailsRepository
import com.example.paypointretailer.Repo.GiftVoucherRepository
import com.example.paypointretailer.Repo.LoginRepository
import com.example.paypointretailer.Repo.MainRepository
import com.example.paypointretailer.Repo.MobileRechargeRepository
import com.example.paypointretailer.Repo.MoneyTransferRepository
import com.example.paypointretailer.Repo.OtpVerifyRepository
import com.example.paypointretailer.Repo.ProfileDetailsRepository
import com.example.paypointretailer.Repo.UpiPaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideLoginDataRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        LoginRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideOtpVerifyDataRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        OtpVerifyRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideMainRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        MainRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideChangePasswordRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        ChangePasswordRepository(apiService, sharedPrefs)


    @Singleton
    @Provides
    fun provideMobileRechargeRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        MobileRechargeRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideMobileBillPaymentRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        BillPaymentRepository(apiService, sharedPrefs)


    @Singleton
    @Provides
    fun provideDthRechargeRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        DthRechargeRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideProfileDetailsRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        ProfileDetailsRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideUpiPaymentRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        UpiPaymentRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideUGiftVoucherRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        GiftVoucherRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideGiftDetailsRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        GiftDetailsRepository(apiService, sharedPrefs)


    @Singleton
    @Provides
    fun provideAepsRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        AepsRepository(apiService, sharedPrefs)

    @Singleton
    @Provides
    fun provideMoneyTransferRepository(apiService: ApiEndpoints, sharedPrefs: SharedPreferences) =
        MoneyTransferRepository(apiService, sharedPrefs)
}

