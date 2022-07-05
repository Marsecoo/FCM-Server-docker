package tw.gov.president.cks.fcm.manager

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tw.gov.president.cks.fcm.data.FCMToken
import tw.gov.president.cks.fcm.provider.provideApiService
import tw.gov.president.cks.fcm.provider.provideOkHttpClient

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