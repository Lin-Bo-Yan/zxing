package com.example.zxing_test;

import androidx.activity.result.ActivityResult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;



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

    private void Loginback(MainAppCompatActivity activity,final String resultData){
        // resultData沒有設值，幾乎是不可能，因為解析json時就會被攔下了
        StringUtils.HaoLog("驗證= "+resultData);
        if(resultData == null){
            DialogUtils.showDialogMessage(activity,"QR已經過期，請重新登入");
            return;
        }
        //解析json
        ResultData result = new Gson().fromJson(resultData,ResultData.class);
        if ("af_token".equals(result.af_token) && "qrcode_info_url".equals(result.qrcode_info_url)) {
//            connection_server_get_httpReturn(activity,result);
        } else {
            DialogUtils.showDialogMessage(activity, "登入失敗，您的QRCode已失效");
        }

    }
}