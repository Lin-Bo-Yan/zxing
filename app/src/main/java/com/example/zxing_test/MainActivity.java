package com.example.zxing_test;

import androidx.activity.result.ActivityResult;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.zxing_test.model.HttpAfReturn;
import com.example.zxing_test.model.eim.EimUserData;
import com.example.zxing_test.tools.ActivityUtils;
import com.example.zxing_test.tools.CallbackUtils;
import com.example.zxing_test.tools.DialogUtils;
import com.example.zxing_test.tools.StringUtils;
import com.example.zxing_test.tools.cloud.api.CloudUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends MainAppCompatActivity {
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
                                Loginback(MainActivity.this,SCAN_QRCODE);
                            }
                        }
                    }
                };
                ActivityUtils.gotoQRcode(MainActivity.this,ScanCaptureType.Json,resultLauncher);
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

            if(eimUserData.isLaleAppEim){
                StringUtils.HaoLog("登入 "+"成功");



            }else if ( eimUserData.isLaleAppWork == true) {
                    /*
                    這段程式碼用於註冊FCM推播服務方式，使用getInstance()方法取得Firebase 實例 ID成功後會獲得註冊token
                    接著呼叫setAfPusher方法，向後端伺服器註冊 AF 推播服務
                    */
            }
        }else {
            StringUtils.HaoLog("登入"+" 失敗，您的QRCode已失效");
        }
    }


}