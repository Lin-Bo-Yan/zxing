package com.example.zxing_test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.zxing_test.tools.CallbackUtils;
import com.example.zxing_test.tools.StringUtils;

public class MainAppCompatActivity extends AppCompatActivity {

    public CallbackUtils.ActivityReturn activityReturn;
    public ActivityResultLauncher resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<androidx.activity.result.ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(activityReturn != null){
                        activityReturn.Callback(result);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}