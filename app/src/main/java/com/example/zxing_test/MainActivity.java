package com.example.zxing_test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeScannerView;

    private final BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                Log.d("joe", "BarcodeCallback成功 = " + result.getText());
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            Log.d("joe", "BarcodeCallback失敗");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeScannerView = findViewById(R.id.dbv_custom);
        barcodeScannerView.decodeContinuous(barcodeCallback);
        barcodeScannerView.setStatusText("");

//        ViewfinderView viewFinder = findViewById(com.google.zxing.client.android.R.id.zxing_viewfinder_view);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_frame);
//        viewFinder.drawResultBitmap(bitmap);
    }
}