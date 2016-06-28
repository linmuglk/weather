package com.moarea.app.maoweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moarea.app.maoweather.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2016/6/28.
 */
public class MaoWeatherDB {

    public static final int VERSION = 1;
    public static final String DB_NAME = "mao_weather";

    private static MaoWeatherDB sMaoWeatherDB;

    private SQLiteDatabase mSQLiteDatabase;

    private MaoWeatherDB(Context context) {
        MaoWeatherOpenHelper maoWeatherOpenHelper = new MaoWeatherOpenHelper(context, DB_NAME, null, VERSION);
        mSQLiteDatabase = maoWeatherOpenHelper.getWritableDatabase();
    }

    public static MaoWeatherDB getInstance(Context context) {

        if (sMaoWeatherDB == null) {
            sMaoWeatherDB = new MaoWeatherDB(context);
        }
        return sMaoWeatherDB;
    }

    public void saveCity(City city) {
        if (city != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name_en", city.getCity_name_en());
            contentValues.put("city_name_ch", city.getCity_name_ch());
            contentValues.put("city_code", city.getCity_code());
            mSQLiteDatabase.insert("city", null, contentValues);
        }
    }

    public List<City> loadCities() {
        List<City> cities = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("city", null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCity_name_en(cursor.getString(cursor.getColumnIndex("city_name_en")));
            city.setCity_name_ch(cursor.getString(cursor.getColumnIndex("city_name_ch")));
            city.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
            cities.add(city);
        }
        if (cursor != null)
            cursor.close();
        return cities;
    }
}
