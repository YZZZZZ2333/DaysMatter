package com.example.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table data(subj varchar(50), dayow int, sttime int, lstime int, clroom varchar(50), teac varchar(50))";
        db.execSQL(sql);
        sql = "insert into data values ('网络编程',2,3,3,'12教402','吴永胜')";
        db.execSQL(sql);
        sql = "insert into data values ('大学生职业发展与就业指导4',3,6,2,'11教405','胡海滨')";
        db.execSQL(sql);
        sql = "insert into data values ('Android移动开发',4,3,3,'4教东306','赵丽娜')";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}