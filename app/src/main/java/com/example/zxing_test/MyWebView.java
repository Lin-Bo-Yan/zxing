package com.example.zxing_test;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.example.zxing_test.tools.StringUtils;

public class MyWebView extends WebView {

    public MyWebView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        //作為webview保活使用,判斷該 View 的可見性是否為 View.GONE
        StringUtils.HaoLog("onWindowVisibilityChanged= "+(visibility != View.GONE));
        if((visibility != View.GONE)){super.onWindowVisibilityChanged(visibility);}
    }
}
