package com.moarea.app.maoweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.moarea.app.maoweather.R;
import com.moarea.app.maoweather.db.MaoWeatherDB;

/**
 * Created by Johnny on 2016/6/28.
 */
public class MaoWeatherActivity extends BaseActivity {

    public static final String WEATHER_KEY = "29b54988329f4c179259bc31150dd9e3";

    private Button mChangeCityButton;
    private MaoWeatherDB mMaoWeatherDB;
    private ProgressDialog mProgressDialog;

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_maoweather);

        mMaoWeatherDB = MaoWeatherDB.getInstance(this);

        mChangeCityButton = (Button) findViewById(R.id.button_changeCity);
        mChangeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaoWeatherActivity.this, CityChooseActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }
}
