package com.example.ktorwsissue.server


import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*

import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import org.slf4j.event.Level
import tw.gov.president.cks.fcm.data.FCMToken
import java.nio.charset.Charset
import java.util.logging.Logger

object ServerFactory {
    private val logger = Logger.getLogger("KtorServer")
    fun getServer(port: Int): ApplicationEngine {
        return embeddedServer(Netty, port, watchPaths = emptyList()) {
            install(WebSockets)
            install(DefaultHeaders) {
                header("X-Developer", "Baeldung")
            }
            install(CallLogging) {
                level = Level.DEBUG
//                filter { call -> call.request.path().startsWith("/author") }
//                filter { call -> call.request.path().startsWith("/registerToken") }
            }
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }
            install(CORS) {
                method(HttpMethod.Get)
                method(HttpMethod.Post)
                method(HttpMethod.Delete)
                anyHost()
            }
            install(Compression) {
                gzip()
            }
            routing {
                get("/") {
                    logger.info("Show get Get request  ${call.request}")
                    call.respondText("All good here in MODEL", ContentType.Text.Plain)
                }
                route("/todo") {
                    delete("/{id}") {
                        call.respond(HttpStatusCode.OK)
                    }
                }
                route("/author") {
                    get() {
                        call.respond("fyytytrytry")
                    }
                    post("/{id}") {
                        call.respond("rewrewqrqw Added")
                    }
                }

                route("/2") {
                    post("/registerToken") {
                        var token = call.receive<FCMToken>()
                        logger.info("Show get Post request token $token")
                        call.respond("${token.deviceId} Added")
                    }
                }
                webSocket("/ws") {
                    logger.info("Sending message to client...")
                    send("foo")
                    val receivedMessage = incoming.receive()
                    val messageFormatted = if (receivedMessage.frameType == FrameType.TEXT) {
                        receivedMessage.readBytes().toString(Charset.defaultCharset())
                    } else {
                        "<non-text frame>"
                    }
                    logger.info("Got message from client: $messageFormatted")
                    logger.info("Closing connection...")
                    close()
                }
            }

        }
    }
}