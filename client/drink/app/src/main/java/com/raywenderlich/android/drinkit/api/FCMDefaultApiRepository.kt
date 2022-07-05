package com.raywenderlich.android.drinkit.api

import retrofit2.Response
import tw.gov.president.cks.fcm.data.FCMToken

class FCMDefaultApiRepository(private val api: FCMDefaultApiService) {
    companion object {
        const val MEDIA_TYPE_JSON = "application/json"
    }

    suspend fun registerToken(request: FCMToken): Response<Void> {
        return api.registerToken(request)
    }
}

