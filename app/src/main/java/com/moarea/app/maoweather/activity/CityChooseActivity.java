package com.moarea.app.maoweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.moarea.app.maoweather.R;
import com.moarea.app.maoweather.db.MaoWeatherDB;
import com.moarea.app.maoweather.model.City;
import com.moarea.app.maoweather.util.HttpCallback;
import com.moarea.app.maoweather.util.HttpUtil;
import com.moarea.app.maoweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2016/6/28.
 */
public class CityChooseActivity extends BaseActivity {

    private MaoWeatherDB mMaoWeatherDB;
    private ProgressDialog mProgressDialog;
    private EditText editText;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private List<String> cityNames = new ArrayList<>();
    private City mCity_selected;
    private List<City> mCities;

    private static final int NONE_DATE = 0;
    private static final int HAS_DATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_citychoose);

        mMaoWeatherDB = MaoWeatherDB.getInstance(this);

        if(mMaoWeatherDB.checkDataState() == NONE_DATE){
            queryCitiesFromServer();
        }

        editText = (EditText) findViewById(R.id.edit_city);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mCities =  queryCitiesFromLocal(s.toString());
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
            }
        });

    }

    //从服务器取出所有的城市信息
    private void queryCitiesFromServer() {
        String address = " https://api.heweather.com/x3/citylist?search=allchina&key=" + MaoWeatherActivity.WEATHER_KEY;
        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallback() {
            @Override
            public void onFinish(String response) {
                if (Utility.handleCityResponse(mMaoWeatherDB, response)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            mMaoWeatherDB.updateDataState();
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
        List<City> cities = mMaoWeatherDB.loadCitiesByName(name);
        cityNames.clear();
        for (City city : cities) {
            cityNames.add(city.getCity_name_ch());
        }

        return cities;
    }

    //显示对话框
    private void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载数据...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    //关闭对话框
    private void closeProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

}
