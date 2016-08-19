package com.curry.bhk.bhk.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.bean.EventBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Curry on 2016/8/17.
 */
public class EventdbOperator {

    private BhkSqliteOpenHelper dBhelper;
    private SQLiteDatabase db;

    public EventdbOperator(Context context) {
        dBhelper = new BhkSqliteOpenHelper(context);
    }

    public List<EventBean> queryEvent(int mode, EventBean outbean) {
        List<EventBean> myList = new ArrayList<EventBean>();

        db = dBhelper.getWritableDatabase();

        Cursor cursor = null;
        try {
            switch (mode) {
                case 0: //query all table
                    cursor = db.query("event_bhk", null, null, null, null, null, null, null);
                    break;
                case 1://query the table according to email;
                    cursor = db.rawQuery("select * from event_bhk where email=" + outbean.getEmail(), null);
                    break;
                case 2://query the table according to author;
                    String[] temp = {outbean.getAuthor()};
                    cursor = db.query("event_bhk", null, "author=?", temp, null, null, null, null);
                    break;
                case 3://query the table according to ID;
                    cursor = db.rawQuery("select * from event_bhk where id=" + outbean.getId(), null);
                    break;
                case 4://query the table according to resolvedby;
                    cursor = db.rawQuery("select * from event_bhk where resolvedby =" + outbean.getResolvedby(), null);
                    break;
                default:
                    break;
            }

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String photos_url = cursor.getString(cursor.getColumnIndex("photos_url"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String resolvedby = cursor.getString(cursor.getColumnIndex("resolvedby"));
                    int status = cursor.getInt(cursor.getColumnIndex("status"));

                    EventBean eventBean = new EventBean();
                    eventBean.setId(id);
                    eventBean.setEmail(email);
                    eventBean.setResolvedby(resolvedby);
                    eventBean.setTime(time);
                    eventBean.setTitle(title);
                    eventBean.setPhotos_url(photos_url);
                    eventBean.setAuthor(author);
                    eventBean.setDescription(description);
                    eventBean.setStatus(status);
                    myList.add(eventBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            close();
        }
        return myList;
    }

    /**
     * insert data into event_bhk.db
     *
     * @param outBean
     */
    public void insertEvent(EventBean outBean) {

        db = dBhelper.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("email", outBean.getEmail());
            contentValues.put("status", outBean.getStatus());
            contentValues.put("author", outBean.getAuthor());
            contentValues.put("description", outBean.getDescription());
            contentValues.put("photos_url", outBean.getPhotos_url());
            contentValues.put("time", outBean.getTime());
            contentValues.put("title", outBean.getTitle());
            contentValues.put("resolvedby", outBean.getResolvedby());

            db.insert("event_bhk", null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    public void updateEvent(EventBean outBean) {
        db = dBhelper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("resolvedby", BaseActivity.mUsername);
            String[] id = {outBean.getId() + ""};
            db.update("user_bhk", contentValues, "id = ?", id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        db.close();
        dBhelper.close();
    }
}
