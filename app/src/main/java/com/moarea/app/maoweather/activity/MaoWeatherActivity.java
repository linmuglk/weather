package com.moarea.app.maoweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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

    private TextView mTextView_cityName;
    private TextView mTextView_updateTime;
    private TextView mTextView_current_date;
    private TextView mTextView_weather_desp;
    private TextView mTextView_textView_temp1;
    private TextView mTextView_textView_temp2;


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

        mTextView_cityName = (TextView) findViewById(R.id.textView_city_name);
        mTextView_updateTime = (TextView) findViewById(R.id.textView_publishTime);
        mTextView_current_date = (TextView) findViewById(R.id.textView_current_date);
        mTextView_weather_desp = (TextView) findViewById(R.id.textView_weather_desp);
        mTextView_textView_temp1 = (TextView) findViewById(R.id.textView_temp1);
        mTextView_textView_temp2 = (TextView) findViewById(R.id.textView_temp2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                mTextView_cityName.setText(data.getStringExtra("city_name_ch"));
                mTextView_updateTime.setText(data.getStringExtra("update_time"));
                mTextView_current_date.setText(data.getStringExtra("data_now"));

                String txt_d = data.getStringExtra("txt_d");
                String txt_n = data.getStringExtra("txt_n");

                if (txt_d.equals(txt_n)) {
                    mTextView_weather_desp.setText(data.getStringExtra("txt_d"));
                } else {
                    mTextView_weather_desp.setText(data.getStringExtra("txt_d") + "转" + data.getStringExtra("txt_n"));
                }
                mTextView_textView_temp1.setText(data.getStringExtra("tmp_min") + "℃");
                mTextView_textView_temp2.setText(data.getStringExtra("tmp_max") + "℃");
            }
        }
    }
}
