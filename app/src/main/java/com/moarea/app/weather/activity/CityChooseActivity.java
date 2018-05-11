package com.moarea.app.weather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.moarea.app.weather.R;
import com.moarea.app.weather.db.WeatherDB;
import com.moarea.app.weather.model.City;
import com.moarea.app.weather.util.HttpCallback1;
import com.moarea.app.weather.util.HttpUtil;
import com.moarea.app.weather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class CityChooseActivity extends Activity {

    private WeatherDB mWeatherDB;
    private ProgressDialog mProgressDialog;
    private EditText editText;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private List<String> cityNames = new ArrayList<>();
    private City mCity_selected;
    private List<City> mCities;

    private static final int NONE_DATA = 0;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_citychoose);

        mWeatherDB = WeatherDB.getInstance(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();


        if (mWeatherDB.checkDataState() == NONE_DATA) {
            queryCitiesFromServer();
        }

        mCities = queryCitiesFromLocal("");
        editText = (EditText) findViewById(R.id.edit_city);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mCities = queryCitiesFromLocal(s.toString());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityNames);
        mListView = (ListView) findViewById(R.id.list_view_cities);
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCity_selected = mCities.get(position);
                queryWeatherFromServer();
            }
        });
    }


    private void queryCitiesFromServer() {
        String address = " https://api.heweather.com/x3/citylist?search=allchina&key=" + WeatherActivity.WEATHER_KEY;
        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallback1() {
            @Override
            public void onFinish(String response) {
                if (Utility.handleCityResponse(mWeatherDB, response)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            mWeatherDB.updateDataState();
                        }
                    });
                }
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(CityChooseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private List<City> queryCitiesFromLocal(String name) {
        List<City> cities = mWeatherDB.loadCitiesByName(name);
        cityNames.clear();
        for (City city : cities) {
            cityNames.add(city.getCity_name_ch());
        }
        return cities;
    }


    private void queryWeatherFromServer() {

        String address = "https://api.heweather.com/x3/weather?cityid=" + mCity_selected.getCity_code() + "&key=" + WeatherActivity.WEATHER_KEY;
        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallback1() {
            @Override
            public void onFinish(String response) {
                if (Utility.handleWeatherResponse(mEditor, response)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(CityChooseActivity.this, "数据同步失败", Toast.LENGTH_SHORT).show();
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
