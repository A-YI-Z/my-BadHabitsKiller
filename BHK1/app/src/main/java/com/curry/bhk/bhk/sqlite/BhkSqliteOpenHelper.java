package com.curry.bhk.bhk.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.curry.bhk.bhk.utils.PublicStatic;

/**
 * Created by Curry on 2016/8/10.
 */
public class BhkSqliteOpenHelper extends SQLiteOpenHelper {

    public BhkSqliteOpenHelper(Context context) {
        super(context, "user_bhk.db", null, PublicStatic.BHK_USER_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_user = "create table if not exists user_bhk"
                + "(email varchar primary key,"
                + "username varchar,"
                + "password varchar,"
                + "pic_url varchar,"
                + "status integer"
                + ")";
        sqLiteDatabase.execSQL(sql_user);

        String sql_event = "create table if not exists event_bhk"
                + "(id integer primary key autoincrement,"
                + "email varchar ,"
                + "author varchar,"
                + "title varchar,"
                + "photos_url varchar,"
                + "status integer,"
                + "time varchar,"
                + "description varchar"
                + ")";
        sqLiteDatabase.execSQL(sql_event);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
