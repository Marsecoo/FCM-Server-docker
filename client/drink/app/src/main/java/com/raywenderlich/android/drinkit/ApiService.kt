package com.raywenderlich.android.drinkit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tw.gov.president.cks.fcm.data.FCMToken

interface ApiService {
    // 瀏覽紀錄
    @POST("registerToken")
    suspend fun registerToken(@Body request: FCMToken): Response<Void>
}