package com.example.zxing_test.model.user;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.zxing_test.tools.StringUtils;
import com.example.zxing_test.tools.phone.AllData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserControlCenter {
    static UserMin userMin = null;



    public static void setLogin(UserMin userMin){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AllData.context);
        pref.edit().putString(userMin.userId+"KEY_JWT",userMin.token).apply(); // f11KEY_JWT
        pref.edit().putString("nowUserId",userMin.userId).apply();
        try {
            JSONObject UserIds = new JSONObject(pref.getString("UserIds", "{}"));
            UserIds.put(userMin.userId, new Gson().toJson(userMin));
            pref.edit().putString("UserIds", UserIds.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserControlCenter.userMin = userMin;
    }

    public static void updateUserMinInfo(UserMin newUserMin) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AllData.context);
        pref.edit().putString(userMin.userId + "KEY_JWT", userMin.token).apply();
        pref.edit().putString("nowUserId", userMin.userId).apply();
        try {
            JSONObject UserIds = new JSONObject(pref.getString("UserIds", "{}"));
            UserIds.put(userMin.userId, new Gson().toJson(userMin));
            pref.edit().putString("UserIds", UserIds.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userMin = newUserMin;

    }

    public static UserMin getUserMinInfo(){
        if(userMin != null){
            return userMin;
        }
        userMin = getUserMinInfoByPhone();
        return userMin;
    }

    static UserMin getUserMinInfoByPhone(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AllData.context);
        String id = pref.getString("nowUserId","");
        if(id.isEmpty()){return null;}
        else {
            try{
                JSONObject UserIds = new JSONObject(pref.getString("UserIds",""));
                Gson gson = new Gson();
                return gson.fromJson(UserIds.getString(id),UserMin.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
