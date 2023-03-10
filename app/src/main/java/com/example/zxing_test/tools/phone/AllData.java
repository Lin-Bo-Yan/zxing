package com.example.zxing_test.tools.phone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.zxing_test.tools.StringUtils;

public class AllData {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private static String MainServer = "https://laledev0.flowring.com/laleweb";

    public static void setMainServer(String mainServer) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString("MainServer", mainServer).apply();
        MainServer = mainServer;
        //它將MainServer變量設置為傳入的參數值，以便在應用程序其他部分中可以輕鬆訪問。
        //這樣做的目的可能是為了存儲應用程序使用的主要API服務器的位置，以便在應用程序需要與該服務器通信時可以輕鬆訪問它。
    }

    /*
    讀取 SharedPreferences 中儲存的 MainServer 字串值，如果沒有儲存過這個值，則回傳預設值 MainServer。
    */
    public static String setMainServer(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString("MainServer",MainServer);
    }

    public static String getMainServer() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString("MainServer", MainServer);

    }

//    public static void init(Context AppContext){
//        StringUtils.HaoLog("init= "+MainServer);
//        context = AppContext;
//    }
}
