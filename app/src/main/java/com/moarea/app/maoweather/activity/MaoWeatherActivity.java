package com.moarea.app.maoweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.moarea.app.maoweather.R;
import com.moarea.app.maoweather.db.MaoWeatherDB;

import java.util.List;

/**
 * Created by Johnny on 2016/6/28.
 */
public class MaoWeatherActivity extends BaseActivity {

    private MaoWeatherDB mMaoWeatherDB;
    private static final String WEATHER_KEY = "29b54988329f4c179259bc31150dd9e3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_maoweather);


    }
}
