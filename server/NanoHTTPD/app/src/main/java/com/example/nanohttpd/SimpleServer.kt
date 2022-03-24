package com.example.nanohttpd

import android.content.res.AssetManager
import android.util.Log
import org.nanohttpd.protocols.http.NanoHTTPD
import com.example.nanohttpd.SimpleServer
import com.google.gson.Gson
import org.nanohttpd.protocols.http.NanoHTTPD.IHTTPSession
import tw.gov.president.cks.fcm.data.FCMToken
import java.io.IOException
import java.util.HashMap

class SimpleServer : NanoHTTPD {
    @JvmField
    var asset_mgr: AssetManager? = null
    companion object {
        private const val TAG = "SimpleServer"
        private val gson = Gson()
    }

    constructor() : super(8890) {
        Log.e(TAG, "Show SimpleServer run  ")
    }

    constructor(hostname: String, port: Int) : super(hostname, port) {
        Log.e(TAG, "Show hostname : $hostname")
    }

    //    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parameters, Map<String, String> files) {
    //        int len = 0;
    //        byte[] buffer = null;
    //        Log.e(TAG, "Show header remote-addr : "+header.get("remote-addr"));
    //        String file_name = uri.substring(1);
    //        if (file_name.equalsIgnoreCase("")) {
    //            file_name = "index.html";
    //        }
    //        try {
    //            InputStream in = asset_mgr.open(file_name, AssetManager.ACCESS_BUFFER);
    //            buffer = new byte[1024 * 1024];
    //
    //            int temp = 0;
    //            while ((temp = in.read()) != -1) {
    //                buffer[len] = (byte) temp;
    //                len++;
    //            }
    //            in.close();
    //        } catch (IOException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //        return new NanoHTTPD.Response(new String(buffer, 0, len));
    //    }
    //Ref : https://blog.csdn.net/rookie_wei/article/details/73614493
    //https://www.796t.com/post/YWt2ZTA=.html
    override fun serve(session: IHTTPSession): Response {
        val method = session.method
        Log.e(TAG, "Show get http method  : $method")
        var queryParams = ""
        var parameters: Map<String?, String?>? = HashMap()
        var headers: Map<String?, String> = HashMap()
        var uri = ""
        try {
            session.parseBody(HashMap())
            parameters = session.parms
            headers = session.headers
            queryParams = session.queryParameterString
            uri = session.uri
            Log.e(TAG, "Show header remote-addr : " + headers["remote-addr"])
            Log.e(TAG, "Show method  queryParams : $queryParams")
            when (method) {
                Method.HEAD -> Log.e(TAG, "HEAD method  : $method")
                Method.DELETE -> Log.e(TAG, "DELETE method  : $method")
                Method.OPTIONS -> Log.e(TAG, "OPTIONS method  : $method")
                Method.PUT -> Log.e(TAG, "PUT method  : $method")
                Method.POST -> {
                    Log.e(TAG, "POST method  queryParams : $queryParams")
                    val token = gson.fromJson(queryParams, FCMToken::class.java)
                    Log.e(TAG, "POST method  queryParams get token  : $token")
                }
                Method.GET -> {
                    Log.e(TAG, "Get method  queryParams : $queryParams")
                    var len = 0
                    var buffer: ByteArray? = null
                    var file_name = uri.substring(1)
                    if (file_name.equals("", ignoreCase = true)) {
                        file_name = "index.html"
                    }
                    try {
                        val `in` = asset_mgr!!.open(file_name, AssetManager.ACCESS_BUFFER)
                        buffer = ByteArray(1024 * 1024)
                        var temp = 0
                        while (`in`.read().also { temp = it } != -1) {
                            buffer[len] = temp.toByte()
                            len++
                        }
                        `in`.close()
                    } catch (e: IOException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }
                else -> {
                    Log.e(TAG, "Get method  queryParams : $queryParams")
                    var len = 0
                    var buffer: ByteArray? = null
                    var file_name = uri.substring(1)
                    if (file_name.equals("", ignoreCase = true)) {
                        file_name = "index.html"
                    }
                    try {
                        val `in` = asset_mgr!!.open(file_name, AssetManager.ACCESS_BUFFER)
                        buffer = ByteArray(1024 * 1024)
                        var temp = 0
                        while (`in`.read().also { temp = it } != -1) {
                            buffer[len] = temp.toByte()
                            len++
                        }
                        `in`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ResponseException) {
            e.printStackTrace()
        }
        return super.serve(session)
    }
}