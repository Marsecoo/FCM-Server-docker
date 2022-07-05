package com.raywenderlich.android.drinkit.di

import android.content.IntentFilter
import android.net.ConnectivityManager
import com.raywenderlich.android.drinkit.BuildConfig
import com.raywenderlich.android.drinkit.api.FCMDefaultApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.gov.president.cks.fcm.Constant
import java.util.concurrent.TimeUnit

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = when (BuildConfig.DEBUG) {
        true -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
    }
    return httpLoggingInterceptor
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(provideHttpLoggingInterceptor())
        .build()
}

fun provideApiService(okHttpClient: OkHttpClient): FCMDefaultApiService {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BuildConfig.API_HOST)
        .build()
        .create(FCMDefaultApiService::class.java)
}



