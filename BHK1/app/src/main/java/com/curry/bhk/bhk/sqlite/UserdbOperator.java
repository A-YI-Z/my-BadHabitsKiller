package com.curry.bhk.bhk.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.curry.bhk.bhk.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Curry on 2016/8/11.
 */
public class UserdbOperator {

    private boolean isExist;
    private BhkSqliteOpenHelper dBhelper;
    private SQLiteDatabase db;

    public UserdbOperator(Context context) {
        dBhelper = new BhkSqliteOpenHelper(context);

    }

    public List<UserBean> queryUser(int mode, UserBean outbean) {
        List<UserBean> myList = new ArrayList<UserBean>();
//        dBhelper = new BhkSqliteOpenHelper(context);
        db = dBhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            switch (mode) {
                case 0: //query all table
                    cursor = db.query("user_bhk", null, null, null, null, null,
                            null, null);

                    break;
                case 1://query the table according to email;
                    cursor = db.rawQuery("select * from user_bhk where email="
                            + outbean.getEmail(), null);
                    break;
                case 2://query the table according to username;
                    String[] temp = {outbean.getUsername()};
                    cursor = db.query("user_bhk", null, "username=?", temp, null,
                            null, null, null);
                    break;
                default:
                    break;
            }

            if (cursor != null) {
                while (cursor.moveToNext()) {
//					int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String email = cursor.getString(cursor
                            .getColumnIndex("email"));
                    String username = cursor.getString(cursor
                            .getColumnIndex("username"));
                    String password = cursor.getString(cursor
                            .getColumnIndex("password"));
                    String pic_url = cursor.getString(cursor
                            .getColumnIndex("pic_url"));
                    int status = cursor.getInt(cursor
                            .getColumnIndex("status"));
                    UserBean userBean = new UserBean();
//					userBean.setId(id);
                    userBean.setEmail(email);
                    userBean.setUsername(username);
                    userBean.setPassword(password);
                    userBean.setPic_url(pic_url);
                    userBean.setStatus(status);
                    myList.add(userBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            closeAll();
        }
        return myList;
    }

    /**
     * judge data is exist or not
     *
     * @param mode
     * @param outbean
     * @return
     */
    public boolean isExist(int mode, UserBean outbean) {

//        dBhelper = new BhkSqliteOpenHelper(context);
        db = dBhelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            switch (mode) {
                case 1://query the table according to email;
                    cursor = db.rawQuery("select * from user_bhk where email="
                            + outbean.getEmail(), null);
                    break;
                case 2://query the table according to username;
                    String[] temp = {outbean.getUsername()};
                    cursor = db.query("user_bhk", null, "username=?", temp, null,
                            null, null, null);
                    break;
                default:
                    break;
            }
            if (cursor != null) {
                isExist = true;
            } else {
                isExist = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            closeAll();
        }

        return isExist;
    }

//    public String chooseHeadUrl(){
//
//    }

    /**
     * insert data into user_bhk.db
     *
     * @param outBean
     */
    public void insertUser(UserBean outBean) {
//        BhkSqliteOpenHelper dBhelper = new BhkSqliteOpenHelper(context);
        db = dBhelper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", outBean.getUsername());
            contentValues.put("password", outBean.getPassword());
            contentValues.put("email", outBean.getEmail());
            contentValues.put("pic_url", outBean.getPic_url());
            contentValues.put("status", outBean.getStatus());

            db.insert("user_bhk", null, contentValues);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }

    }

    public void updateUser(UserBean outBean) {
//        BhkSqliteOpenHelper dBhelper = new BhkSqliteOpenHelper(context);
        db = dBhelper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", outBean.getUsername());
            contentValues.put("password", outBean.getPassword());
            String[] email = {outBean.getEmail()};
            db.update("user_bhk", contentValues, "email = ?", email);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }


    public void deleteUser(int mode, UserBean outBean) {
//        BhkSqliteOpenHelper dBhelper = new BhkSqliteOpenHelper(context);
//        SQLiteDatabase db = dBhelper.getReadableDatabase();
        try {
            switch (mode) {
                case 0:
                    db.delete("user_bhk", null, null);
                    db.rawQuery("delete from user_bhk", null);
                    break;
                case 1:
                    String[] str_email = {outBean.getEmail()};
                    db.delete("user_bhk", "id=?", str_email);
                    break;
                case 2:
                    String[] str_name = {outBean.getUsername()};
                    db.delete("user_bhk", "username=?", str_name);
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }

    }

    private void closeAll() {
        db.close();
        dBhelper.close();
    }
}


