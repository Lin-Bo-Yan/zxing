package com.example.zxing_test;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.google.zxing.integration.android.IntentIntegrator;

public class ActivityUtils {


    static public void gotoQRcode(Activity activity){
        new IntentIntegrator(activity).setCaptureActivity(ScanActvity.class).initiateScan();
//        Intent intent = new Intent(activity, ScanActvity.class);
//        intent.putExtra("ScanCaptureType", type);
//        RcodeResult.launch(intent);
    }
}
