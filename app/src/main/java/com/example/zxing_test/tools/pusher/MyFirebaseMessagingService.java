package com.example.zxing_test.tools.pusher;

import androidx.annotation.NonNull;

import com.example.zxing_test.tools.StringUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        StringUtils.HaoLog("ggg= "+"onNewToken "+s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData() != null){
            StringUtils.HaoLog("ggg= "+"getData()"+remoteMessage.getData().get("body"));
        }
        if (remoteMessage.getNotification() != null) {
            StringUtils.HaoLog("ggg= "+"Message Notification Body: "+remoteMessage.getNotification().getBody());
        }

    }

}
