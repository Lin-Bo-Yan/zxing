package com.example.zxing_test.tools;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.example.zxing_test.EimLoginActivity;
import com.example.zxing_test.ScanActvity;

public class ActivityUtils {


    static public void gotoQRcode(Activity activity, EimLoginActivity.ScanCaptureType type, ActivityResultLauncher ActivityResult){
        Intent intent = new Intent(activity, ScanActvity.class);
        intent.putExtra("ScanCaptureType", type);
        ActivityResult.launch(intent);

        //        new IntentIntegrator(activity).setCaptureActivity(ScanActvity.class).initiateScan();
    }
}
