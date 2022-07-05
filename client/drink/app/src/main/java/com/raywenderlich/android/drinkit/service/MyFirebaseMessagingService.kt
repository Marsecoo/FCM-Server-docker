/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.drinkit.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.raywenderlich.android.drinkit.api.FCMManager
import com.raywenderlich.android.drinkit.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tw.gov.president.cks.fcm.Constant
import tw.gov.president.cks.fcm.data.FCMToken

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "FirebaseMessagingService"
    }
    private var broadcaster: LocalBroadcastManager? = null
    private val processLater = false


    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        Log.e(TAG, "Refreshed token: $token")
        val info = Intent("FirebaseMessaging")
        info.putExtra(Constant.FCM_TOKEN, token)
        localsendBroadcast(info)
        val deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        getSharedPreferences("_", MODE_PRIVATE).edit().putString(Constant.FCM_TOKEN, token).apply()
        FCMManager.registerToken(deviceId,token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, "onMessageReceived From: ${remoteMessage.from}")
        val info = Intent(Constant.FIREBASE_MESSAGING_REMOTE_MESSAGE)
        info.putExtra(Constant.REMOTE_MESSAGE_RAW, remoteMessage)
        localsendBroadcast(info)

        if (/* Check if data needs to be processed by long running job */ processLater) {
            //scheduleJob()
            Log.d(TAG, "executing schedule job")
        } else {
            // Handle message within 10 seconds
            Log.e(TAG, "To handleNow(remoteMessage) ")
            handleNow(remoteMessage)
        }
    }

    @SuppressLint("LogNotTimber")
    private fun handleNow(remoteMessage: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())
        Log.e(TAG, "handleNow RemoteMessage  $remoteMessage")
        Log.e(TAG, "handleNow RemoteMessage  MessageType${remoteMessage.getMessageType()}")

        val notification = remoteMessage.notification
        notification?.let {
            Log.e(TAG, "handleNow RemoteMessage  Notification${it}")
        }
        val data = remoteMessage.data
        data?.let {
            Log.e(TAG, "handleNow RemoteMessage  data${it}")
        }

        handler.post {
            Toast.makeText(
                baseContext,
                getString(R.string.handle_notification_now),
                Toast.LENGTH_LONG
            ).show()

            remoteMessage.notification?.let {
                val intent = Intent("MyData")
                intent.putExtra("message", remoteMessage.data["text"])
                localsendBroadcast(intent)
            }
        }
    }

    private fun localsendBroadcast(info:Intent) {
        broadcaster?.sendBroadcast(info)
    }
}