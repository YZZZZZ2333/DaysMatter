package com.example.daysmatter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table data(rid int, title varchar(50), dsc varchar(200), typ int, star int, tim bigint)";
        db.execSQL(sql);
        sql = "create table share(rid int, uname varchar(50), title varchar(50), dsc varchar(200), typ int, tim bigint)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
