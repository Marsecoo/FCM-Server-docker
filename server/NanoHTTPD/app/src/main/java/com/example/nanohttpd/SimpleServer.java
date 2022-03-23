package com.example.nanohttpd;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;

import tw.gov.president.cks.fcm.data.FCMToken;

public class SimpleServer extends NanoHTTPD {
    private final static String TAG = "SimpleServer";
    private final static Gson gson = new Gson();
    AssetManager asset_mgr;

    public SimpleServer() {
        super(8890);
        Log.e(TAG, "Show SimpleServer run  " );
    }

    public SimpleServer(String hostname, int port) {
        super(hostname, port);
        Log.e(TAG, "Show hostname : " + hostname);
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
    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        Log.e(TAG, "Show get http method  : " + method);
        String queryParams = "";
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        String uri  = "";
        try {
            session.parseBody(new HashMap<String, String>());
            parameters = session.getParms();
            headers = session.getHeaders();
            queryParams = session.getQueryParameterString();
            uri = session.getUri();
            Log.e(TAG, "Show header remote-addr : " + headers.get("remote-addr"));
            Log.e(TAG, "Show method  queryParams : " + queryParams);
            switch (method) {
                case HEAD:
                    Log.e(TAG, "HEAD method  : " + method);
                    break;
                case DELETE:
                    Log.e(TAG, "DELETE method  : " + method);
                    break;
                case OPTIONS:
                    Log.e(TAG, "OPTIONS method  : " + method);
                    break;
                case PUT:
                    Log.e(TAG, "PUT method  : " + method);
                    break;
                case POST:
                    Log.e(TAG, "POST method  queryParams : " + queryParams);
                    FCMToken token = gson.fromJson(queryParams, FCMToken.class);
                    Log.e(TAG, "POST method  queryParams get token  : " + token);
                    break;
                case GET:
                default:
                    Log.e(TAG, "Get method  queryParams : " + queryParams);
                    int len = 0;
                    byte[] buffer = null;
                    String file_name = uri.substring(1);
                    if (file_name.equalsIgnoreCase("")) {
                        file_name = "index.html";
                    }
                    try {
                        InputStream in = asset_mgr.open(file_name, AssetManager.ACCESS_BUFFER);
                        buffer = new byte[1024 * 1024];
                        int temp = 0;
                        while ((temp = in.read()) != -1) {
                            buffer[len] = (byte) temp;
                            len++;
                        }
                        in.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return super.serve(session);
    }
}