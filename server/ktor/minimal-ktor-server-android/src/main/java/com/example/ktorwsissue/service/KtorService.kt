package com.example.ktorwsissue.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.ktorwsissue.Constant
import com.example.ktorwsissue.server.ServerFactory
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.util.logging.Logger
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level

class KtorService : Service() {
    private val logger = Logger.getLogger("KtorService")
    private val coroutineContext = Dispatchers.IO
    private var HTTP_PORT = 5555
    private val server : ApplicationEngine by lazy {
        ServerFactory.getServer(HTTP_PORT)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e("KtorService", "Show onBind Intent : $intent ")
        logger.info("Show onBind Intent : $intent ")
        intent?.let {
            Log.e("KtorService", "Show onBind Intent extra  : ${it.extras} ")
            it.extras?.let { bundle ->
                val http_prot = bundle.getInt(Constant.HTTP_PORT)
                Log.e("KtorService", "Show onBind Intent extra port : $http_prot ")
            }
        }
        //        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return null
    }

    override fun onCreate() {
        Log.e("KtorService", "onCreate")
        logger.info("onCreate")
        super.onCreate()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        Log.e("KtorService", "Show onStart Intent : $intent ")
        intent?.let {
            Log.e("KtorService", "Show onStart Intent extra  : ${it.extras} ")
            it.extras?.let { bundle ->
                HTTP_PORT = bundle.getInt(Constant.HTTP_PORT)
                Log.e("KtorService", "Show onStart Intent extra port : $HTTP_PORT ")
            }
        }
        super.onStart(intent, startId)
        CoroutineScope(coroutineContext).launch {
            logger.info("Starting server...")
            server.start(wait = true)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("KtorService", "Show onStartCommand Intent : $intent ")
        logger.info("Show onStartCommand Intent : $intent ")
        intent?.let {
            Log.e("KtorService", "Show onStartCommand Intent extra  : ${it.extras} ")
            it.extras?.let { bundle ->
                HTTP_PORT = bundle.getInt(Constant.HTTP_PORT)
                Log.e("KtorService", "Show onStartCommand Intent extra port : $HTTP_PORT ")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.e("KtorService", "Show onDestroy ")
        logger.info("Stopping server")
        server.stop(1_000, 2_000)
        super.onDestroy()
    }
}