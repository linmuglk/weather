package com.moarea.app.maoweather.activity;

import android.app.Activity;
import android.os.Bundle;

import com.moarea.app.maoweather.util.ActivityCollector;

/**
 * Created by Johnny on 2016/6/28.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
