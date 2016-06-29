package com.moarea.app.maoweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.moarea.app.maoweather.R;
import com.moarea.app.maoweather.db.MaoWeatherDB;
import com.moarea.app.maoweather.model.City;
import com.moarea.app.maoweather.util.HttpCallback;
import com.moarea.app.maoweather.util.HttpUtil;
import com.moarea.app.maoweather.util.Utility;

/**
 * Created by Johnny on 2016/6/28.
 */
public class MaoWeatherActivity extends BaseActivity {

    public static final String WEATHER_KEY = "29b54988329f4c179259bc31150dd9e3";

    private Button mChangeCityButton;
    private MaoWeatherDB mMaoWeatherDB;
    private ProgressDialog mProgressDialog;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    public static final int REQUEST_CODE = 1;

    private TextView mTextView_cityName;
    private TextView mTextView_updateTime;
    private TextView mTextView_current_date;
    private TextView mTextView_weather_desp;
    private TextView mTextView_textView_temp1;
    private TextView mTextView_textView_temp2;

    private City mCity_current = new City();
    private Button mRefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maoweather);
        mMaoWeatherDB = MaoWeatherDB.getInstance(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
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

        mRefreshButton = (Button) findViewById(R.id.button_refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeatherFromServer();
            }
        });
        loadWeatherData(mSharedPreferences.getString("city_code", null), mSharedPreferences.getString("city_name_ch", null), mSharedPreferences.getString("update_time", null), mSharedPreferences.getString("data_now", null), mSharedPreferences.getString("txt_d", null), mSharedPreferences.getString("txt_n", null), mSharedPreferences.getString("tmp_min", null), mSharedPreferences.getString("tmp_max", null));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadWeatherData(data.getStringExtra("city_code"), data.getStringExtra("city_name_ch"), data.getStringExtra("update_time"), data.getStringExtra("data_now"), data.getStringExtra("txt_d"), data.getStringExtra("txt_n"), data.getStringExtra("tmp_min"), data.getStringExtra("tmp_max"));
            }
        }
    }

    private void loadWeatherData(String city_code, String city_name, String update_time, String current_data, String txt_d, String txt_n, String tmp_min, String tmp_max) {

        mTextView_cityName.setText(city_name);
        mTextView_updateTime.setText(update_time);
        mTextView_current_date.setText(current_data);

        if (txt_d.equals(txt_n)) {
            mTextView_weather_desp.setText(txt_d);
        } else {
            mTextView_weather_desp.setText(txt_d + "转" + txt_n);
        }
        mTextView_textView_temp1.setText(tmp_min + "℃");
        mTextView_textView_temp2.setText(tmp_max + "℃");

        mCity_current.setCity_name_ch(city_name);
        mCity_current.setCity_code(city_code);

    }

    private void updateWeatherFromServer() {

        String address = "https://api.heweather.com/x3/weather?cityid=" + mCity_current.getCity_code() + "&key=" + MaoWeatherActivity.WEATHER_KEY;
        showProgressDialog();


        HttpUtil.sendHttpRequest(address, new HttpCallback() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utility.handleWeatherResponse(mEditor, response)) {
                            loadWeatherData(mSharedPreferences.getString("city_code", null), mSharedPreferences.getString("city_name_ch", null), mSharedPreferences.getString("update_time", null), mSharedPreferences.getString("data_now", null), mSharedPreferences.getString("txt_d", null), mSharedPreferences.getString("txt_n", null), mSharedPreferences.getString("tmp_min", null), mSharedPreferences.getString("tmp_max", null));
                            closeProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(MaoWeatherActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("正在同步数据...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {

        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
