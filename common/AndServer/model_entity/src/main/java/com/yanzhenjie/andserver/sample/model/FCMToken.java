//package com.yanzhenjie.andserver.sample.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.alibaba.fastjson.annotation.JSONField;
//
///**
// * Temp class  want use kotlin data class
// */
//public class FCMToken implements Parcelable {
//
//    @JSONField(name = "deviceId")
//    private String mUserId;
//    @JSONField(name = "fcmToken")
//    private String mUserName;
//
//    public FCMToken() {
//    }
//
//    protected FCMToken(Parcel in) {
//        mUserId = in.readString();
//        mUserName = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(mUserId);
//        dest.writeString(mUserName);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<FCMToken> CREATOR = new Creator<FCMToken>() {
//        @Override
//        public FCMToken createFromParcel(Parcel in) {
//            return new FCMToken(in);
//        }
//
//        @Override
//        public FCMToken[] newArray(int size) {
//            return new FCMToken[size];
//        }
//    };
//
//    public String getUserId() {
//        return mUserId;
//    }
//
//    public void setUserId(String userId) {
//        mUserId = userId;
//    }
//
//    public String getUserName() {
//        return mUserName;
//    }
//
//    public void setUserName(String userName) {
//        this.mUserName = userName;
//    }
//}