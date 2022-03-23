package com.google.firebase.quickstart.fcm.kotlin

import retrofit2.Response
import timber.log.Timber
import tw.gov.president.cks.fcm.data.FCMToken

class ApiRepository(private val apiService: ApiService) {
    companion object {
        const val MEDIA_TYPE_JSON = "application/json"
    }

    suspend fun registerToken(request: FCMToken): Response<Void> {
        return apiService.registerToken(request)
    }
}

