package com.moarea.app.weather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moarea.app.weather.R;
import com.moarea.app.weather.model.City;
import com.moarea.app.weather.util.HttpCallback1;
import com.moarea.app.weather.util.HttpUtil;
import com.moarea.app.weather.util.Utility;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;


public class WeatherActivity extends Activity {


    public static final String WEATHER_KEY = "91c7b6ab430743a58c75facf9a68ac9d";

    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final int REQUEST_CODE = 1;

    private Button mChangeCityButton;
    private TextView mTextView_cityName;
    private Button mRefreshButton;
    private TextView mTextView_updateTime;
    private TextView mTextView_current_date;
    private TextView mTextView_weather_desp;
    private TextView mTextView_textView_temp1;
    private TextView mTextView_textView_temp2;

    private City mCity_current = new City();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdManager.getInstance(this).init("8c8f79aef6457ac0", "d71c14f920b0e968", false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maoweather);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        mChangeCityButton = (Button) findViewById(R.id.button_changeCity);
        mChangeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, CityChooseActivity.class);
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

        if (mSharedPreferences.getString("city_code", null) == null) {
            mCity_current.setCity_code("CN101010100");
            updateWeatherFromServer();
        } else {
            loadWeatherData(mSharedPreferences.getString("city_code", null), mSharedPreferences.getString("city_name_ch", null), mSharedPreferences.getString("update_time", null), mSharedPreferences.getString("data_now", null), mSharedPreferences.getString("txt_d", null), mSharedPreferences.getString("txt_n", null), mSharedPreferences.getString("tmp_min", null), mSharedPreferences.getString("tmp_max", null));
            updateWeatherFromServer();
        }

        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adLayout);
        linearLayout.addView(adView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadWeatherData(mSharedPreferences.getString("city_code", null), mSharedPreferences.getString("city_name_ch", null), mSharedPreferences.getString("update_time", null), mSharedPreferences.getString("data_now", null), mSharedPreferences.getString("txt_d", null), mSharedPreferences.getString("txt_n", null), mSharedPreferences.getString("tmp_min", null), mSharedPreferences.getString("tmp_max", null));
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
        String address = "https://api.heweather.com/x3/weather?cityid=" + mCity_current.getCity_code() + "&key=" + WeatherActivity.WEATHER_KEY;
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallback1() {

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


            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("同步数据中，请稍等");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
