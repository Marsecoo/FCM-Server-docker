package com.example.nanohttpd.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.nanohttpd.SimpleServer
import java.io.IOException

class MainService : Service() {
    private var mHttpServer:  SimpleServer? = null

    override fun onCreate() {
        super.onCreate()
        mHttpServer =  SimpleServer()
        Log.e("MainService", "The server Create")
        try {
            Log.e("MainService", "The server start.")
            // 因为程序模拟的是html放置在asset目录下，
            // 所以在这里存储一下AssetManager的指针。
//            mHttpServer?.asset_mgr = this.assets
            mHttpServer?.start()
            Log.e("MainService", "The server started.")
        } catch (ioe: IOException) {
            Log.e("MainService", "The server could not start.")
        }catch (e: Exception){
            e.printStackTrace()
            val service = Intent(this, MainService::class.java)
            startService(service)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mHttpServer?.stop()
        super.onDestroy()
    }

}