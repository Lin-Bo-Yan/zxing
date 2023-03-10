package com.example.zxing_test;

import androidx.activity.result.ActivityResult;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.zxing_test.model.HttpAfReturn;
import com.example.zxing_test.model.HttpReturn;
import com.example.zxing_test.model.eim.EimUserData;
import com.example.zxing_test.model.user.UserControlCenter;
import com.example.zxing_test.model.user.UserMin;
import com.example.zxing_test.tools.ActivityUtils;
import com.example.zxing_test.tools.CallbackUtils;
import com.example.zxing_test.tools.DialogUtils;
import com.example.zxing_test.tools.StringUtils;
import com.example.zxing_test.tools.cloud.api.CloudUtils;
import com.example.zxing_test.tools.phone.AllData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class EimLoginActivity extends MainAppCompatActivity {
    public enum ScanCaptureType{
        Json
    }
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityReturn = new CallbackUtils.ActivityReturn() {
                    @Override
                    public void Callback(ActivityResult activityReturn) {
                        if(activityReturn.getResultCode() == Activity.RESULT_OK){
                            String SCAN_QRCODE = activityReturn.getData().getStringExtra("SCAN_QRCODE");
                            if(SCAN_QRCODE != null){
                                //驗證
                                Loginback(EimLoginActivity.this,SCAN_QRCODE);
                            }
                        }
                    }
                };
                ActivityUtils.gotoQRcode(EimLoginActivity.this,ScanCaptureType.Json,resultLauncher);
            }
        });
    }

    //解析需要耗時所以需要開執行續
    private void Loginback(MainAppCompatActivity activity,final String resultData){

        new Thread(new Runnable() {
            @Override
            public void run() {
                // resultData沒有設值，幾乎是不可能，因為解析json時就會被攔下了
                StringUtils.HaoLog("驗證= "+resultData);
                if(resultData == null){
                    DialogUtils.showDialogMessage(activity,"QR已經過期，請重新登入");
                    return;
                }

                JSONObject result = null;
                try {
                    result = new JSONObject(resultData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (result != null && result.has("qrcode_info_url") && result.has("af_token")) {
                    connection_server_get_httpReturn(activity,result);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringUtils.HaoLog("連線= "+"登入失敗，您的QRCode已失效");
                            DialogUtils.showDialogMessage(activity, "登入失敗，您的QRCode已失效");
                        }
                    });
                }
            }
        }).start();
    }

    private void connection_server_get_httpReturn(MainAppCompatActivity activity, JSONObject result){

        String af_token = result.optString("af_token");
        String qrcode_info_url = result.optString("qrcode_info_url");
        // 打api用Authorization方式取得，所以我會寫一個接口，接class
        HttpAfReturn httpReturn = CloudUtils.iCloudUtils.getEimQRcode(activity, af_token, qrcode_info_url);
        if(httpReturn.success){
            String eimUserDataString = new Gson().toJson(httpReturn.data);
            EimUserData eimUserData = new Gson().fromJson(eimUserDataString, EimUserData.class);
//            {
//                UserMin userMin = eimUserData.getUserMin();
//                UserControlCenter.setLogin(userMin);
//                UserControlCenter.updateUserMinInfo(userMin);
//            }
            if(eimUserData.isLaleAppEim){
                String thirdPartyIdentifier = eimUserData.af_mem_id;
                String androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                HttpReturn httpReturn2 = CloudUtils.iCloudUtils.loginSimpleThirdParty(thirdPartyIdentifier,androidId);
                if(httpReturn2.status == 200){
                    String userMinString = new Gson().toJson(httpReturn2.data);
                    UserMin userMin = new Gson().fromJson(userMinString, UserMin.class);
                    StringUtils.HaoLog("httpReturn2.data=" + eimUserData);
                    userMin.eimUserData = eimUserData;
                    userMin.eimUserData.lale_token = userMin.token;
                    userMin.eimUserData.refresh_token = userMin.refreshToken;
                    UserControlCenter.setLogin(userMin);
                    UserControlCenter.updateUserMinInfo(userMin);
                    FirebasePusher_test_account(activity);
                }else {
                    StringUtils.HaoLog("登入 "+"失敗");
                }


            }else if ( eimUserData.isLaleAppWork == true) {
                FirebasePusher_AF_push_registration();
            }
        }else {
            StringUtils.HaoLog("登入"+" 失敗，您的QRCode已失效");
        }
    }

    public static void FirebasePusher_test_account(MainAppCompatActivity activity){
        StringUtils.HaoLog("登入 測試帳號");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AllData.context);
        String deviceToken = "f7_wBFToQGeDOz-fcoDVBH:APA91bHE6-b2QS1AE5St0gZkVz16ZXGqA3BswJFmj_0gsUpxkUv9tR-569XvJ0Gd29VyJc02RuRrPIzvOh8GTdf8T4NMIw0eW-a1CL_CUm1glc1bb64hxubEV1iWnF5mcy6wEiW8SCyv";
        HttpReturn pu;
        new Thread(new Runnable() {
            HttpReturn pu;
            @Override
            public void run() {
                try{
                    JSONObject UserIds = new JSONObject(pref.getString("UserIds", "{}"));
                    StringUtils.HaoLog("UserIds=" + UserIds.length());
                    if(UserIds.length() <= 1){
                        String userId = UserControlCenter.getUserMinInfo().userId;
                        String uuid = Settings.Secure.getString(activity.getContentResolver(),Settings.Secure.ANDROID_ID);
                        pu = CloudUtils.iCloudUtils.setPusher(userId,deviceToken,uuid);
                    }else {
                        String userId = UserControlCenter.getUserMinInfo().userId;
                        String uuid = Settings.Secure.getString(activity.getContentResolver(),Settings.Secure.ANDROID_ID);
                        pu = CloudUtils.iCloudUtils.UserPushFunction(userId,uuid);
                    }
                    StringUtils.HaoLog("setPusher= " + pu);
                }catch (JSONException e){e.printStackTrace();}

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            }
        }).start();
    }


    public static void FirebasePusher_AF_push_registration(){
        //CloudUtils.iCloudUtils.setAfPusher();
        StringUtils.HaoLog("登入 客戶帳號 ");
    }
}