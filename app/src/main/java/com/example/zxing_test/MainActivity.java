package com.example.zxing_test;

import androidx.activity.result.ActivityResult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends MainAppCompatActivity {

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
                            StringUtils.HaoLog("joe= "+"SCAN_QRCODE "+SCAN_QRCODE);
                        }
                    }
                };
                ActivityUtils.gotoQRcode(MainActivity.this,ScanCaptureType.Json,resultLauncher);
            }
        });
    }

    public enum ScanCaptureType{
        Json
    }
}