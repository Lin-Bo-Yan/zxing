package com.example.zxing_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zxing_test.tools.PermissionUtils;
import com.example.zxing_test.tools.StringUtils;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ScanActvity extends AppCompatActivity {
    MainActivity.ScanCaptureType scanCaptureType = null;
    private final BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
//                StringUtils.HaoLog("joe "+" BarcodeCallback成功 = " + result.getText());
                handleDecode_switchJudge(result.getText());
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            StringUtils.HaoLog("joe "+" BarcodeCallback失敗 ");
        }
    };

//    private CaptureManager capture; // 這個是包好的東西，掃完會自動finish掉畫面
    private DecoratedBarcodeView dbv_custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_actvity);
        scanCaptureType = (MainActivity.ScanCaptureType) getIntent().getExtras().get("ScanCaptureType");


        String permission_camera = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this, permission_camera) != PackageManager.PERMISSION_GRANTED){
            PermissionUtils.requestPermission(this, permission_camera, "掃描QRcode功能需要相機權限");
        }

        DecoratedBarcodeView(savedInstanceState);
        ViewfinderView viewFinder = findViewById(com.google.zxing.client.android.R.id.zxing_viewfinder_view);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_frame);
        viewFinder.drawResultBitmap(bitmap);

        btn_choose_qrcode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbv_custom.resume();
//        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbv_custom.pause();
//        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);

        StringBuffer word = new StringBuffer();
        switch (permissions.length) {
            case 1:
                if (permissions[0].equals(Manifest.permission.CAMERA)) word.append("相機權限");
                else word.append("儲存權限");
                if (grantResults[0] == 0) word.append("已取得");
                else word.append("未取得");
                word.append("\n");
                if (permissions[0].equals(Manifest.permission.CAMERA)) word.append("儲存權限");
                else word.append("相機權限");
                word.append("已取得");

                break;
            case 2:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.CAMERA)) word.append("相機權限");
                    else word.append("儲存權限");
                    if (grantResults[i] == 0) word.append("已取得");
                    else word.append("未取得");
                    if (i < permissions.length - 1) word.append("\n");
                }
                break;
        }
        StringUtils.HaoLog("權限 = "+word);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return dbv_custom.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private void DecoratedBarcodeView(Bundle savedInstanceState){

        dbv_custom = findViewById(R.id.dbv_custom);
        dbv_custom.decodeContinuous(barcodeCallback);
        dbv_custom.setStatusText("");
//        capture = new CaptureManager(this, dbv_custom);
//        capture.initializeFromIntent(getIntent(), savedInstanceState);
//        capture.decode();
    }

    private void handleDecode_switchJudge(String result) {
        dbv_custom.pause();
        switch (scanCaptureType){
            case Json:
                boolean isJson = false;
                try {
                    isJson = new JSONObject(result) != null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    StringUtils.HaoLog("joe= "+"非本公司QR");
                    finish();
                }
                if (isJson) {

                    Intent resultIntent_switchJudge = new Intent();
                    resultIntent_switchJudge.putExtra("SCAN_QRCODE", result);
                    setResult(Activity.RESULT_OK, resultIntent_switchJudge); // 傳過去時，要告訴被傳的對象的返回值是成功的
                    finish();
                } else {
                    dbv_custom.resume();
                }
                break;
        }
    }

    private void btn_choose_qrcode(){
        LinearLayout btn_choose_qrcode = findViewById(R.id.btn_choose_qrcode);
        btn_choose_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 49);
                }
            }
        });
    }
}