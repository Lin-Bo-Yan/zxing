package com.example.zxing_test.model.user;

import com.example.zxing_test.model.eim.EimUserData;

public class UserMin {
    public EimUserData eimUserData;

    public String userId;                // 使用者的 ID
    public String displayName;           // 顯示名稱
    public String avatarUrl;             // 頭像檔案 URL
    public String avatarThumbnailUrl;    //頭像檔案縮圖 URL
    public String token;
    public String refreshToken;
    public long expiration;//token到期時間
    public long refreshExpiration;      // 刷新號令有效秒數
    public ExternalServerSetting externalServerSetting; // 外部 SERVER 資料

    public String _type;              // 判斷是不是官方帳號 boolean isOfficialAccount

    public ExternalServerSetting getExternalServerSetting() {
        return externalServerSetting;
    }
    public UserMin() {
        userId = "";
        displayName = "";
        token = "";
        avatarUrl = "";
        _type = "text";
    }
}
