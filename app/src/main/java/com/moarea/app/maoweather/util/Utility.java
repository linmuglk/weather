package com.moarea.app.maoweather.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.moarea.app.maoweather.db.MaoWeatherDB;
import com.moarea.app.maoweather.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johnny on 2016/6/28.
 */
public class Utility {

    //处理从服务器获取的数据
    public synchronized static boolean handleCityResponse(MaoWeatherDB maoWeatherDB, String response) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("city_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject city_info = jsonArray.getJSONObject(i);
                    City city = new City();
                    String city_name_ch = city_info.getString("city");
                    String city_name_en = "";
                    String city_code = city_info.getString("id");
                    city.setCity_code(city_code);
                    city.setCity_name_en(city_name_en);
                    city.setCity_name_ch(city_name_ch);
                    maoWeatherDB.saveCity(city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    //处理从服务器返回的天气信息
    public synchronized static boolean handleWeatherResponse(SharedPreferences.Editor editor, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("HeWeather data service 3.0");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject weather_info_all = jsonArray.getJSONObject(0);

                    JSONObject weather_info_basic = weather_info_all.getJSONObject("basic");

                    editor.putString("city_name_ch", weather_info_basic.getString("city"));
                    editor.putString("city_code", weather_info_basic.getString("id"));

                    JSONObject weather_info_basic_update = weather_info_basic.getJSONObject("update");
                    editor.putString("update_time", weather_info_basic_update.getString("loc"));

                    JSONArray weather_info_daily_forecast = weather_info_all.getJSONArray("daily_forecast");

                    JSONObject weather_info_now_forecast = weather_info_daily_forecast.getJSONObject(0);

                    editor.putString("data_now", weather_info_now_forecast.getString("date"));//当前日期

                    JSONObject weather_info_now_forecast_tmp = weather_info_now_forecast.getJSONObject("tmp");

                    editor.putString("tmp_min", weather_info_now_forecast_tmp.getString("min"));
                    editor.putString("tmp_max", weather_info_now_forecast_tmp.getString("max"));

                    JSONObject weather_info_now_forecast_cond = weather_info_now_forecast.getJSONObject("cond");

                    editor.putString("txt_d", weather_info_now_forecast_cond.getString("txt_d"));//天气情况前
                    editor.putString("txt_n", weather_info_now_forecast_cond.getString("txt_n"));//天气情况后
                }
                editor.commit();

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
