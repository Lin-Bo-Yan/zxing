package com.example.zxing_test.tools.cloud.api;

import android.content.Context;

import androidx.annotation.WorkerThread;

import com.example.zxing_test.model.HttpAfReturn;
import com.example.zxing_test.model.HttpReturn;

public interface ICloudUtils {

    /**
     * 取得EIM_QRcode登入資料 ，調用 HttpAfReturn用於返回 getEimQRcode登入訊息，將結果封裝在HttpAfReturn類中
     */
    @WorkerThread
    HttpAfReturn getEimQRcode(Context context, String af_token, String qrcode_info_url);

    /**
     * 登入後啟動推播
     */
    @WorkerThread
    HttpAfReturn setAfPusher(String WFCI_URL, String memId, String userId,String FCM_token, String uuid);

    /**
     * 接收到第三方登入token 發起登入
     */
    @WorkerThread
     HttpReturn loginSimpleThirdParty(String thirdPartyIdentifier, String deviceId);

    /**
     * 登入後啟動推播
     */
    @WorkerThread
     HttpReturn setPusher(String userId, String FCM_token, String uuid);

    /**
     * 切換帳號時切換推播
     */
    @WorkerThread
    HttpReturn UserPushFunction(String userId, String uuid);
}
