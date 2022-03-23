package com.weng.demo.fcm.controller;

import com.weng.demo.fcm.FcmInitializer;
import com.weng.demo.fcm.model.PushNotificationRequest;
import com.weng.demo.fcm.model.PushNotificationResponse;
import com.weng.demo.fcm.service.PushNotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tw.gov.president.cks.fcm.data.FCMToken;

/**
 * @author Jason Weng
 * @date
 */
@RestController
public class PushNotificationController {
   private static final Logger logger = LoggerFactory.getLogger(FcmInitializer.class);
   private final HashMap<String,String> fakeTokenDB = new HashMap<String,String>();
   private PushNotificationService pushNotificationService;

   public PushNotificationController(PushNotificationService pushNotificationService) {
      this.pushNotificationService = pushNotificationService;
   }

   @PostMapping("/registerToken")
   public ResponseEntity  registerFCMToken(@RequestBody FCMToken request) {
      logger.info("Show  get FCMToken  DeviceId : "+request.getDeviceId()+"  value : "+request.getFcmToken());
      fakeTokenDB.put(request.getDeviceId(),request.getFcmToken());
      return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "device add"), HttpStatus.OK);
   }

   //FIXME below search token send to one or all devices
   @PostMapping("/notification/topic")
   public ResponseEntity sendNotification(@RequestBody PushNotificationRequest request) {
      pushNotificationService.sendPushNotificationWithoutData(request);
      return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
   }

   @PostMapping("/notification/token")
   public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
      pushNotificationService.sendPushNotificationToToken(request);
      return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
   }

   @PostMapping("/notification/data")
   public ResponseEntity sendDataNotification(@RequestBody PushNotificationRequest request) {
      pushNotificationService.sendPushNotification(request);
      return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
   }
}
