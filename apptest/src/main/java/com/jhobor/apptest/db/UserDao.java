package com.jhobor.apptest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jhobor.apptest.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/4/8.
 */

public class UserDao {
    private DBHelper dbHelper;

    public UserDao(Context context){
        dbHelper = new DBHelper(context);
    }

    public List<User> loadAll(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "select * from "+DBHelper.TB_USER;
        Cursor cursor = database.rawQuery(sql, null);
        List<User> userList = new ArrayList<>();
        while (cursor.moveToNext()){
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            byte[] img = cursor.getBlob(2);
            userList.add(new User(id,name,img));
        }
        cursor.close();
        database.close();
        return userList;
    }

    public void uploadImg(int id,byte[] img){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("img",img);
        database.update(DBHelper.TB_USER,cv,"id=?",new String[]{String.valueOf(id)});
        database.close();
    }

    public byte[] downloadImg(int id){
        byte[] img = null;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select img from "+DBHelper.TB_USER+" where id = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(id)});
        if (cursor.moveToNext()){
            img = cursor.getBlob(0);
        }
        cursor.close();
        database.close();
        return img;
    }
}
