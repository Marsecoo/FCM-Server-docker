package com.raywenderlich.android.drinkit.api

import com.raywenderlich.android.drinkit.di.provideApiService
import com.raywenderlich.android.drinkit.di.provideOkHttpClient
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tw.gov.president.cks.fcm.data.FCMToken

interface FCMDefaultApiService {
    @POST("registerToken")
    suspend fun registerToken(@Body request: FCMToken): Response<Void>
}

object ProjectNetwork {
    val apiService by lazy {
        provideApiService( provideOkHttpClient())
    }
}