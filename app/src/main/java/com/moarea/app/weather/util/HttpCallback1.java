package com.moarea.app.weather.util;


public interface HttpCallback1 {

    void onFinish(String response);
    void onError(Exception e);
}
