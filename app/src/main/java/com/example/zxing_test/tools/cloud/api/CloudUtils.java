package com.example.zxing_test.tools.cloud.api;

import android.content.Context;

import com.example.zxing_test.WebActivity;
import com.example.zxing_test.model.HttpAfReturn;
import com.example.zxing_test.tools.StringUtils;
import com.example.zxing_test.tools.phone.AllData;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloudUtils implements ICloudUtils{
    public static CloudUtils iCloudUtils = new CloudUtils();

    @Override
    public HttpAfReturn getEimQRcode(Context context, String af_token, String qrcode_info_url) {
        Request.Builder request =new Request.Builder()
                .url(qrcode_info_url)
                .addHeader("Authorization","Bearer "+af_token);
        return getJhttpAfReturn(request);
    }

    @Override
    public HttpAfReturn setAfPusher(String WFCI_URL, String memId, String userId, String FCM_token, String uuid) {
        if (WFCI_URL == null || WFCI_URL.isEmpty())
            return new HttpAfReturn();
        MediaType mediaType = MediaType.parse("application/json");
        Map<String, Object> map = new HashMap();
        map.put("memId", memId);
        map.put("apId", "AF");
        map.put("uuid", uuid);
        map.put("token", FCM_token);
        map.put("userId", userId);
        map.put("deviceName", "Android");
        map.put("devicePlatform", "Android");
        map.put("deviceVersion", WebActivity.getVersionName(AllData.context)); // 獲取軟件版本號，對應AndroidManifest.xml下android:versionName
        map.put("deviceModel", "Android");
        map.put("allowed", true);
        StringUtils.HaoLog("setAfPusher json=" + new JSONObject(map).toString());
        StringUtils.HaoLog("setAfPusher URL=" + WFCI_URL + "/api/app-pusher");
        StringUtils.HaoLog("setAfPusher method=POST");
        RequestBody body = RequestBody.create(mediaType, new JSONObject(map).toString());
        Request.Builder request = new Request.Builder()
                .url(WFCI_URL + "/api/app-pusher")
                .method("POST", body);
        return getJhttpAfReturn(request);
    }

    public HttpAfReturn getJhttpAfReturn(Request.Builder request) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        try {
            Response response = client.newCall(request.build()).execute();
            String body = response.body().string();
            HttpAfReturn httpReturn = new Gson().fromJson(body, HttpAfReturn.class);
            StringUtils.HaoLog("body=" + body);
            StringUtils.HaoLog("getJhttpAfReturn");
            StringUtils.HaoLog(response.header("url") + " " + httpReturn.success + " " + httpReturn.data);
            StringUtils.HaoLog("getJhttpAfReturn end");
            return httpReturn;
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return new HttpAfReturn();
    }
}
