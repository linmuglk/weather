package com.moarea.app.maoweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Johnny on 2016/6/28.
 */
public class MaoWeatherOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_CITY = "CREATE TABLE CITY(ID INTEGER PRIMARY KEY,CITY_NAME_EN TEXT,CITY_NAME_CH TEXT,CITY_CODE TEXT)";
    private static final String DATA_STATE = "CREATE TABLE DATA_STATE(STATE INTEGER PRIMARY KEY)";
    private static final String INSERT_DATA_STATE = "INSERT INTO DATA_STATE VALUES(0)";

    public MaoWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
        db.execSQL(DATA_STATE);
        db.execSQL(INSERT_DATA_STATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
