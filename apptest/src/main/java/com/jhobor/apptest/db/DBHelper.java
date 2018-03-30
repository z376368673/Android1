package com.jhobor.apptest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/8.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_test";
    private static final int DB_VERSION = 1;
    public static final String TB_USER = "tb_user";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(">>","DBHelper.onCreate");
        String sql = "create table if not exists "+TB_USER+"(id integer primary key autoincrement, name char(12), img blob)";
        db.execSQL(sql);

        sql = "insert into "+TB_USER+"(name) values('aaa');";
        db.execSQL(sql);
        sql = "insert into "+TB_USER+"(name) values('bbb');";
        db.execSQL(sql);
        sql = "insert into "+TB_USER+"(name) values('ccc');";
        db.execSQL(sql);
        sql = "insert into "+TB_USER+"(name) values('ddd');";
        db.execSQL(sql);
        sql = "insert into "+TB_USER+"(name) values('eee');";
        db.execSQL(sql);
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
