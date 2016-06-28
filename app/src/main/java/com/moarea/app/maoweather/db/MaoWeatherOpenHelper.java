package com.moarea.app.maoweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Johnny on 2016/6/28.
 */
public class MaoWeatherOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_CITY = "CREATE TABLE CITY(ID INTEGER PRIMARY KEY,CITY_NAME_EN TEXT,CITY_NAME_CH,CITY_CODE TEXT)";

    public MaoWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
