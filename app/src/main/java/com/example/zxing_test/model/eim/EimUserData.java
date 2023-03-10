package com.example.zxing_test.model.eim;

import com.example.zxing_test.model.user.ExternalServerSetting;
import com.example.zxing_test.model.user.UserMin;
import com.example.zxing_test.tools.phone.AllData;

    // 測試帳號
public class EimUserData {
    public boolean isLaleAppEim = false;
    public boolean isLaleAppWork = false;
    public String af_url = null;
    public String af_mem_id = null;
    public String af_login_id = null;
    public String af_token = null;
    public String af_wfci_service_url = null;

    public String lale_third_party_identifier = null;
    public String lale_server = null;
    public String lale_token = null;
    public String refresh_token = null;
    public String lale_user_id = null;
    public LaleExternalServerInfo lale_external_server_info = null;
    public boolean isLaleCall = false;
    public String call_service_url = "";

    public UserMin getUserMin(){
        UserMin userMin = new UserMin();
        userMin.userId = lale_user_id;

        if (!isLaleAppEim){
            userMin.userId = af_mem_id;
        }
        userMin.token = lale_token;
        userMin.refreshToken = refresh_token;

        AllData.setMainServer(lale_server);
        if (lale_external_server_info != null) {
            userMin.externalServerSetting = new ExternalServerSetting();
            userMin.externalServerSetting.jitsiServerUrl = lale_external_server_info.jitsiServerUrl;
            userMin.externalServerSetting.messageServerUrl = lale_external_server_info.messageServerUrl;
            userMin.externalServerSetting.mqttUrl = lale_external_server_info.mqttUrl;
        }
        userMin.eimUserData = this;
        return userMin;
    }
}
