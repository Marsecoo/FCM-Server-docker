# FCM Integration with Spring Boot To Send Push Notification From Server Side.
## [Pankaj Singh]

[![N|Solid](https://miro.medium.com/fit/c/176/176/2*tkv69bsX7l3iX7zJHWAzOw.jpeg)](https://medium.com/@singh.pankajmca/fcm-integration-with-spring-boot-to-send-push-notification-from-server-side-1091cfd2cacf)


# Spring Boot: 利用 FCM 發送 Push Notifications
## [Wayne]
[![N|Solid](https://scontent.ftpe2-1.fna.fbcdn.net/v/t1.6435-9/117061541_111446227334626_2138325368394388684_n.jpg?_nc_cat=100&ccb=1-5&_nc_sid=09cbfe&_nc_ohc=pRtmtHzycKsAX-5tNHd&_nc_ht=scontent.ftpe2-1.fna&oh=00_AT8XBlJt7F3Bf7yFplJ4SwpOBvrIwEX0j_Biy8fEcm_HbA&oe=6260D75D)](https://waynestalk.com/spring-boot-fcm-push-notifications/)


### ＥＮＶ
* springboot http-server (oracle 8)
* firebase-admin(6.8.1)

#### API Test
```
post -- http://url:8890/notification/topic
header:
[{"key":"Content-type","value":"application/json","description":""},
{"key":"Authorization","value":" u'r fcm Server key","description":""}]
body:
    {"title":"Hello", "message":"The message is jason test", "topic":"contactTopic"}
```
```
post -- http://url:8890/notification/token
header:
[{"key":"Content-type","value":"application/json","description":""},
{"key":"Authorization","value":" u'r fcm Server key","description":""}]
body:
    {"title":"Hello", "message":"The message is ...... ", , "token":"u'r device specific token"}
```
```
post -- http://url:8890/notification/data
header:
[{"key":"Content-type","value":"application/json","description":""},
{"key":"Authorization","value":" u'r fcm Server key","description":""}]
body:
    {"title":"Hello", "message":"Data message...... ", , "token":"u'r device specific token"}
```
   [Pankaj Singh]: <https://medium.com/@singh.pankajmca>

   [Wayne]: <https://waynestalk.com/about/>