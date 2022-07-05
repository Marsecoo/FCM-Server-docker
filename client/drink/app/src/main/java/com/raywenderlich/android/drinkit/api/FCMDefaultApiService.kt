package com.raywenderlich.android.drinkit.api

import com.raywenderlich.android.drinkit.di.provideApiService
import com.raywenderlich.android.drinkit.di.provideOkHttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tw.gov.president.cks.fcm.data.FCMToken

interface FCMDefaultApiService {
    @POST("registerToken")
    suspend fun registerToken(@Body request: FCMToken): Response<Void>
}

object FCMManager {
    val apiService by lazy {
        provideApiService( provideOkHttpClient())
    }

    fun registerToken(deviceId:String,token:String) {
        GlobalScope.launch {
           apiService.registerToken(FCMToken(deviceId, token ));
        }
    }
}