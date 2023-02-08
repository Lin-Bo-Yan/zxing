package com.example.zxing_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.List;

public class ScanActvity extends AppCompatActivity {
    private final BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                StringUtils.HaoLog("joe "+" BarcodeCallback成功 = " + result.getText());
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            StringUtils.HaoLog("joe "+" BarcodeCallback失敗 ");
        }
    };

    private CaptureManager capture;
    private DecoratedBarcodeView dbv_custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_actvity);

        DecoratedBarcodeView(savedInstanceState);
        ViewfinderView viewFinder = findViewById(com.google.zxing.client.android.R.id.zxing_viewfinder_view);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_frame);
        viewFinder.drawResultBitmap(bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return dbv_custom.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    private void DecoratedBarcodeView(Bundle savedInstanceState){
        dbv_custom = findViewById(R.id.dbv_custom);
        dbv_custom.decodeContinuous(barcodeCallback);
        dbv_custom.setStatusText("");
        capture = new CaptureManager(this, dbv_custom);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }
}