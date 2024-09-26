package tw.gov.president.cks.fcm.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tw.gov.president.cks.fcm.data.FCMToken

interface FCMDefaultApiService {
    @POST("registerToken")
    suspend fun registerToken(@Body request: FCMToken): Response<Void>
}

