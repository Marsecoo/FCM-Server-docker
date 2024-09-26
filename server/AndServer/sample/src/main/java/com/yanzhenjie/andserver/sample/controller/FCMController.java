/*
 * Copyright 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.andserver.sample.controller;

import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestBody;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.session.Session;
//import com.yanzhenjie.andserver.sample.model.FCMToken;
import com.yanzhenjie.andserver.sample.util.Logger;
import com.yanzhenjie.andserver.util.MediaType;

import tw.gov.president.cks.fcm.data.FCMToken;

/**
 * Ref : https://blog.csdn.net/qq_38001118/article/details/117993282?utm_medium=distribute.pc_feed_404.none-task-blog-2~default~BlogCommendFromBaidu~Rate-9.pc_404_mixedpudn&depth_1-utm_source=distribute.pc_feed_404.none-task-blog-2~default~BlogCommendFromBaidu~Rate-9.pc_404_mixedpud
 *
 */
@RestController
@RequestMapping(path = "/registerToken")
class FCMController {
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String registerToken(@RequestBody FCMToken token, HttpRequest request, HttpResponse response ) {
        Session session = request.getValidSession();
        //FIXME FCMToken !!  need use alibaba:fastjson?
        Logger.e("Show FCMToken : "+ token);
        Logger.e("Show HttpRequest : "+ request.getBody());
        Logger.e("Show session : "+ session);

//        session.setAttribute(LoginInterceptor.LOGIN_ATTRIBUTE, true);
//        if (account.equals("123") && password.equals("123")) {
//            Cookie cookie = new Cookie("account", account + "=" + password);
//            response.addCookie(cookie);
//            return "Login successful.";
//        }else{
//            String body = JsonUtils.failedJson(response.getStatus(), "Login  fail");
//            response.setBody(new JsonBody(body));
//
//        }
        return "fail";
    }
}