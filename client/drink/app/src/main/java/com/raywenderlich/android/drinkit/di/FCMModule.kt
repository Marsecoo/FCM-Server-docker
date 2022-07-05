package com.raywenderlich.android.drinkit.di

import android.content.IntentFilter
import tw.gov.president.cks.fcm.Constant

fun provideFCMIntentFilter(): IntentFilter {
    return IntentFilter().apply {
        addAction(Constant.FCM_TOKEN)
        addAction(Constant.FIREBASE_MESSAGING_REMOTE_MESSAGE)
    }
}