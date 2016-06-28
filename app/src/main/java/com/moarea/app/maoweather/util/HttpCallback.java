package com.moarea.app.maoweather.util;

/**
 * Created by Johnny on 2016/6/28.
 */
public interface HttpCallback {

    void onFinish(String response);
    void onError(Exception e);
}
