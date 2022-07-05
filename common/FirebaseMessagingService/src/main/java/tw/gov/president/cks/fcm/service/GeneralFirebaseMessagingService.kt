package tw.gov.president.cks.fcm.service

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tw.gov.president.cks.fcm.Constant
import tw.gov.president.cks.fcm.manager.FCMManager

class GeneralFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "GeneralFirebaseMessagingService"
    }
    private var broadcaster: LocalBroadcastManager? = null
    private val processLater = false

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        Log.e(TAG, "Refreshed token: $token")
        val info = Intent(Constant.FCM_REG)
        info.putExtra(Constant.FCM_TOKEN, token)
        localsendBroadcast(info)
        val deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        getSharedPreferences("_", MODE_PRIVATE).edit().putString(Constant.FCM_TOKEN, token).apply()
        FCMManager.registerToken(deviceId,token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, "onMessageReceived From : ${remoteMessage.from}")
        val info = Intent(Constant.FIREBASE_MESSAGING_REMOTE_MESSAGE)
        info.putExtra(Constant.REMOTE_MESSAGE_RAW, remoteMessage)
        localsendBroadcast(info)
    }

    private fun localsendBroadcast(info:Intent) {
        broadcaster?.sendBroadcast(info)
    }
}