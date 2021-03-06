package com.moarea.app.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moarea.app.weather.model.City;

import java.util.ArrayList;
import java.util.List;


public class WeatherDB {

    public static final int VERSION = 1;
    public static final String DB_NAME = "weather";

    private static WeatherDB sWeatherDB;

    private SQLiteDatabase mSQLiteDatabase;

    private WeatherDB(Context context) {
        WeatherOpenHelper WeatherOpenHelper = new WeatherOpenHelper(context, DB_NAME, null, VERSION);
        mSQLiteDatabase = WeatherOpenHelper.getWritableDatabase();
    }

    public static WeatherDB getInstance(Context context) {
        if (sWeatherDB == null) {
            sWeatherDB = new WeatherDB(context);
        }
        return sWeatherDB;
    }

    public void saveCity(City city) {
        if (city != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("CITY_NAME_EN", city.getCity_name_en());
            contentValues.put("CITY_NAME_CH", city.getCity_name_ch());
            contentValues.put("CITY_CODE", city.getCity_code());
            mSQLiteDatabase.insert("CITY", null, contentValues);
        }
    }


    public List<City> loadCities() {
        List<City> cities = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("CITY", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                city.setCity_name_en(cursor.getString(cursor.getColumnIndex("CITY_NAME_EN")));
                city.setCity_name_ch(cursor.getString(cursor.getColumnIndex("CITY_NAME_CH")));
                city.setCity_code(cursor.getString(cursor.getColumnIndex("CITY_CODE")));
                cities.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return cities;
    }

    public List<City> loadCitiesByName(String name) {

        List<City> cities = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("CITY", null, "CITY_NAME_CH like ?", new String[]{name + "%"}, null, null, "CITY_CODE");
        while (cursor.moveToNext()) {
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            city.setCity_name_en(cursor.getString(cursor.getColumnIndex("CITY_NAME_EN")));
            city.setCity_name_ch(cursor.getString(cursor.getColumnIndex("CITY_NAME_CH")));
            city.setCity_code(cursor.getString(cursor.getColumnIndex("CITY_CODE")));
            cities.add(city);
        }
        if (cursor != null)
            cursor.close();
        return cities;
    }

    public int checkDataState() {
        int data_state = -1;
        Cursor cursor = mSQLiteDatabase.query("data_state", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                data_state = cursor.getInt(cursor.getColumnIndex("STATE"));
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();

        return data_state;
    }

    public void updateDataState() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 1);
        mSQLiteDatabase.update("data_state", contentValues, null, null);
    }
}
