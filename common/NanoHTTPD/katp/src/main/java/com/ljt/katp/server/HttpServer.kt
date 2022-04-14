package com.ljt.katp.server

import com.google.gson.Gson
import dev.sysadmin.xerial.mchirico.SQLite
import dev.sysadmin.xerial.mchirico.getThroughReflection
import org.nanohttpd.protocols.http.IHTTPSession
import org.nanohttpd.protocols.http.NanoHTTPD
import org.nanohttpd.protocols.http.request.Method
import org.nanohttpd.protocols.http.response.Response
import org.nanohttpd.protocols.http.response.Response.newFixedLengthResponse
import tw.gov.president.cks.fcm.data.FCMToken
import java.util.*
import java.util.logging.Logger
import kotlin.reflect.full.memberProperties

class HttpServer(hostname: String?, port: Int) : NanoHTTPD(hostname, port) {
    private val TAG = "HttpServer"
    private val logger = Logger.getLogger("HttpServer")
    private var count = 0 //用于记录请求为第几次
    private val mGson: Gson = Gson() //用于记录请求为第几次
    private val table_name = FCMToken::class.simpleName!!.lowercase(Locale.getDefault())
    private val sp = SQLite.getSQLite("./${table_name}.db")
    private var isInitDB = false

    override fun serve(session: IHTTPSession?): Response {
        if (!isInitDB) {
            val colum_id = "_id"
            val dbcolumkeyset = linkedMapOf<String, String>()
            var token = FCMToken()
            token.javaClass.kotlin.memberProperties.forEach {
                val key = it.name
                val value =
                    it.returnType.toString().replace("kotlin.", "")
                        .replace("String", "TEXT NOT NULL")
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
            sp.initTable(table_name, sql.toString())
            isInitDB = false
        }
        return if (session != null) {
            processSessionInfo(session)
        } else {
            responseJsonString(503, "HTTPSession is null", "Success！")
        }
    }

    //Ref : https://blog.csdn.net/rookie_wei/article/details/73614493
    //https://www.796t.com/post/YWt2ZTA=.html
    //TODO : add load index.html from AssetManager or Res folder
    private fun processSessionInfo(session: IHTTPSession): Response {
        val method = session.method
        val uri = session.uri
        println("Show uri : $uri")
        val version_method = uri.split("/")
        println("Show version_method : $version_method")
        val headers = session.headers
        println("Show header : $headers ")
        val jsonbaodys: Map<String?, String?> = HashMap()
        session.parseBody(jsonbaodys)
        var jsonstring = jsonbaodys["postData"]
        if (method == Method.PUT) {
            jsonstring = jsonbaodys["content"]
        }
        val urlencoded_params = session.parameters
        println("Show urlencoded_params : $urlencoded_params ${urlencoded_params.size}")
        val contentType = headers["content-type"]
        println("Show contentType : $contentType")
        var api_version = 1
        var api_method = "registerToken"
        version_method?.let { api_version_method ->
            api_version_method.forEach { value ->
                api_version = api_version_method[1].toInt()
                api_method = api_version_method[2]
            }
        }
        val dbcolumkeyset = linkedMapOf<String, String>()
        var token = FCMToken()
        token.javaClass.kotlin.memberProperties.forEach {
            val key = it.name
            val value =
                it.returnType.toString().replace("kotlin.", "").replace("String", "TEXT NOT NULL")
            dbcolumkeyset.put(key, value)
        }
        val sql = StringBuilder()
        when (method) {
            Method.POST -> {
                when (api_method) {
                    "registerToken" -> {
                        var deviceId = ""
                        var fcmToken = ""
                        var token: FCMToken
                        if (contentType.equals("application/x-www-form-urlencoded")) {
                            urlencoded_params["deviceId"]?.let {
                                deviceId = it[0].toString()
                            }
                            urlencoded_params["fcmToken"]?.let {
                                fcmToken = it[0].toString()
                            }
                            token = FCMToken(deviceId, fcmToken)
                        } else {
                            token = mGson.fromJson(jsonstring, FCMToken::class.java)
                        }
                        println("Show get Token : $token")
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
                        return responseJsonString(200, "$token", "请求成功！")
                    }
                    "sumHundredTime" -> {
                        var b: Int = urlencoded_params["number"]?.get(0).toString().toInt()
                        for (i in 0..100) {
                            b += 1
                        }
                        return responseJsonString(200, b, "Success！")
                    }
                    "holleMan" -> {
                        return responseJsonString(
                            200,
                            "Hello ${urlencoded_params["name"]?.get(0)} !",
                            "Success！"
                        )
                    }
                }
                return responseJsonString(200, "say somthing to me $count, ok?", "请求成功！")
            }
            Method.GET -> {
                count++
                val param = session.parameters
                return responseJsonString(
                    200,
                    "${param["name"]?.get(0)}, say somthing to me $count, ok?",
                    "请求成功！"
                )
            }
            Method.HEAD -> {

            }
            Method.DELETE -> {

            }
            Method.OPTIONS -> {

            }
            Method.PUT -> {

            }
        }
        return responseJsonString(404, "", "Request not support!")
    }

    private fun <T : Any> responseJsonString(code: Int, data: T, msg: String): Response {
        val responser = Responser<T>(code, data, msg)
        return newFixedLengthResponse(mGson.toJson(responser))
    }
}