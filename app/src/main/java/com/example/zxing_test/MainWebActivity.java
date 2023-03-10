package com.example.zxing_test;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.zxing_test.model.user.UserControlCenter;
import com.example.zxing_test.model.user.UserMin;
import com.example.zxing_test.tools.PermissionUtils;
import com.example.zxing_test.tools.StringUtils;
import com.example.zxing_test.tools.phone.AllData;

import java.io.File;

public class MainWebActivity extends AppCompatActivity {
    MyWebView webView;
    @Override
    protected void onResume() {
        super.onResume();
        UserMin userMin = UserControlCenter.getUserMinInfo();
        StringUtils.HaoLog("onResume= " + userMin.displayName);
        if(userMin != null && !userMin.userId.isEmpty()){
            checkPermission();
        }else {goLogin();}
        checkHasWebView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_web);
        AllData.context = getApplicationContext();
        if (AllData.context == null){AllData.context = getApplicationContext();}
        initFireBaseMsgBroadcastReceiver();
    }

    private void goLogin(){
        if(webView != null){
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
        Intent intent = new Intent(getApplicationContext(),EimLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void checkPermission() {

    }

    private void initFireBaseMsgBroadcastReceiver() {

    }

    private void checkHasWebView(){
        if(webView == null){
            webView = new MyWebView(getApplicationContext());
            setWebView(webView, getMainWebUrl());
            backtoActivity();
        }
    }

    private String getMainWebUrl(){
        UserMin userMin = UserControlCenter.getUserMinInfo();
        if(userMin != null && userMin.eimUserData != null && userMin.eimUserData.af_url != null){
            return UserControlCenter.getUserMinInfo().eimUserData.af_url + "/eimApp/index.html#/";
        }
        return "";
    }

    void cleanWebviewCache() {
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + "/webcache");
        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir.getPath());
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir.getPath());
        }


    }

    @SuppressLint("JavascriptInterface")
    private void setWebView(WebView webView, String url){
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );
        webView.setVisibility(View.INVISIBLE);
//        webView.setDownloadListener(mWebDownloadListener);
        cleanWebviewCache();
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setTextZoom(100);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        //新增瀏覽器客戶端
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                findViewById(R.id.logo).setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                StringUtils.HaoLog("還活著 onPageFinished= " + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if(getMainWebUrl().equals(request.getUrl().toString())&&errorResponse.getStatusCode()>=500&&errorResponse.getStatusCode()<600)
                {
                    //Logout();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });
        WebChromeClient mWebChromeClient = new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                String[]  PermissionRequest = request.getResources();
                for (int i = 0; i < PermissionRequest.length; i++) {
                    StringUtils.HaoLog("onPermissionRequest:"+PermissionRequest[i]);
                    if(PermissionRequest[i].equals("android.webkit.resource.AUDIO_CAPTURE"))
                    {
                        if(   PermissionUtils.checkPermission(MainWebActivity.this,"android.permission.RECORD_AUDIO"))
                        {
                        }else
                        {
                            runOnUiThread(()->{
                                requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 466);
                            });

                        }
                    }
                }
                request.grant(request.getResources());
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());

                newWebView.setWebViewClient(new WebViewClient());
                newWebView.setWebChromeClient(this);
                setWebView(newWebView, url);
                backtoActivity();
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            // Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {

            }
        };

        webView.addJavascriptInterface(this, "laleIm");
        webView.addJavascriptInterface(this, "LaleTon");
        webView.addJavascriptInterface(this, "FlowringLale");
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(mWebChromeClient);
        webView.loadUrl("https://translate.google.com/?hl=zh-TW");
    }

    void backtoActivity() {
        if (webView != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            if (webView.getParent() != null)
                ((ViewGroup) webView.getParent()).removeView(webView);
            ((ViewGroup) findViewById(R.id.all)).addView(webView, params);
        }
    }

}