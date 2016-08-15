package com.curry.bhk.bhk.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.curry.bhk.bhk.utils.PublicStatic;

/**
 * Created by Curry on 2016/8/10.
 */
public class UserSqliteOpenHelper extends SQLiteOpenHelper {

    public UserSqliteOpenHelper(Context context) {
        super(context, "user_bhk.db", null, PublicStatic.BHK_USER_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists user_bhk"
                + "(email varchar primary key,"
                + "username varchar," + "password varchar," + "pic_url varchar,"
                + "status integer)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
