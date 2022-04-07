package com.example.ktorwsissue

import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ktorwsissue.Constant.Companion.HTTP_PORT
import com.example.ktorwsissue.service.KtorService
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.ContentType
import io.ktor.http.cio.websocket.FrameType
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readBytes
import io.ktor.http.cio.websocket.send
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.net.NetworkInterface
import java.nio.charset.Charset
import java.util.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
//    private val coroutineContext = Dispatchers.IO

    private val logger = Logger.getLogger("KtorServer")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val i = Intent(this, KtorService::class.java)
        i.putExtra(HTTP_PORT, 7070)
        startService(i)
        findViewById<TextView>(R.id.serverStatusText).text = getString(R.string.serverStartedMessage)

        val localIpAddress = NetUtils.getIpAddressInLocalNetwork()
        if (localIpAddress != null) {
            findViewById<TextView>(R.id.ipAddressText).text =
                getString(R.string.localIpAddressMessage, localIpAddress)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}