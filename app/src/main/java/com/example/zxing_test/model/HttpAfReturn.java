package com.example.zxing_test.model;

/*
一些常用的 HTTP 請求返回信息字段
*/
public class HttpAfReturn {
    public boolean success= false;
    public String message;
    public String errorMessage = "連線失敗";
    public Object data = null;
    public int total;
    public int code = 500;
    public String stackTrace;
}
