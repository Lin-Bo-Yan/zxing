package com.example.zxing_test.tools;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.example.zxing_test.MainActivity;
import com.example.zxing_test.ScanActvity;
import com.google.zxing.integration.android.IntentIntegrator;

public class ActivityUtils {


    static public void gotoQRcode(Activity activity, MainActivity.ScanCaptureType type, ActivityResultLauncher ActivityResult){
        Intent intent = new Intent(activity, ScanActvity.class);
        intent.putExtra("ScanCaptureType", type);
        ActivityResult.launch(intent);

        //        new IntentIntegrator(activity).setCaptureActivity(ScanActvity.class).initiateScan();
    }
}
