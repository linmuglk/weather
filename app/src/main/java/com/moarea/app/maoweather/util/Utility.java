package com.moarea.app.maoweather.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.moarea.app.maoweather.db.MaoWeatherDB;
import com.moarea.app.maoweather.model.City;

import org.json.JSONArray;
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
}
