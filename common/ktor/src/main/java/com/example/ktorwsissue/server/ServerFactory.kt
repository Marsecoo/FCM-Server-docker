package com.example.ktorwsissue.server


import dev.sysadmin.xerial.mchirico.SQLite
import dev.sysadmin.xerial.mchirico.getThroughReflection
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
import java.util.*
import java.util.logging.Logger
import kotlin.reflect.full.memberProperties

object ServerFactory {
    private val logger = Logger.getLogger("KtorServer")
    fun getServer(port: Int): ApplicationEngine {
        return embeddedServer(Netty, port, watchPaths = emptyList()) {
            val table_name = FCMToken::class.simpleName!!.lowercase(Locale.getDefault())
            val sp = SQLite.getSQLite("./${table_name}.db")
            var isInitDB = false
            val dbcolumkeyset = linkedMapOf<String, String>()
            var token = FCMToken()
            if (!isInitDB){
                val colum_id = "_id"
                token.javaClass.kotlin.memberProperties.forEach {
                    val key = it.name
                    val value =
                        it.returnType.toString().replace("kotlin.", "").replace("String", "TEXT NOT NULL")
                    dbcolumkeyset.put(key, value)
                }
                val sql = StringBuilder()
                sql.append("CREATE TABLE")
                sql.append(" $table_name ")
                sql.append('(')
                sql.append("$colum_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                var size =
                    if (dbcolumkeyset != null && dbcolumkeyset.isNotEmpty()) dbcolumkeyset.size else 0
                if (size > 0) {
                    for (colName in dbcolumkeyset.keys) {
                        val index = dbcolumkeyset.keys.indexOf(colName)
                        sql.append(if (index > 0) "," else "")
                        sql.append(colName)
                        sql.append(" ")
                        sql.append(dbcolumkeyset.get(colName))
                    }
                    sql.append(",timeEnter DATE")
                    sql.append(')')
                }
                logger.info("Show class gen sql command   ${sql.toString()}")
                sp.initTable(table_name,sql.toString())
                isInitDB=false
            }
            val sql = StringBuilder()
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
                        token = call.receive<FCMToken>()
                        logger.info("Show get Post request token $token")
                        sql.clear()
                        sql.append("INSERT")
                        sql.append("")
                        sql.append(" INTO ")
                        sql.append(" $table_name ")
                        sql.append('(')
                        for (colName in dbcolumkeyset.keys) {
                            val index = dbcolumkeyset.keys.indexOf(colName)
                            sql.append(if (index > 0) "," else "")
                            sql.append(colName)
                        }
                        sql.append(')')
                        sql.append(" values ")
                        sql.append('(')
                        for (colName in dbcolumkeyset.keys) {
                            val index = dbcolumkeyset.keys.indexOf(colName)
                            sql.append(if (index > 0) "," else "")
                            val value =
                                if (index == 0) token.getThroughReflection<String>(colName) else token.getThroughReflection<String>(
                                    colName
                                )
                            sql.append("'" + value + "'")
                        }
                        sql.append(')')
                        sp.execSQL(sql.toString())
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