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
    public static final String TABLE_USER_BHK = "user_bhk";

    private boolean isExist;
    private BhkSqliteOpenHelper dBhelper;
    private SQLiteDatabase db;

    public UserdbOperator(Context context) {
        dBhelper = new BhkSqliteOpenHelper(context);
    }

    public List<UserBean> queryUser(int mode, UserBean outbean) {
        List<UserBean> myList = new ArrayList<UserBean>();
        db = dBhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            switch (mode) {
                case 0: //query all table
                    cursor = db.query(TABLE_USER_BHK, null, null, null, null, null, null, null);
                    break;
                case 1://query the table according to email;
                    cursor = db.rawQuery("select * from user_bhk where email= ?", new String[]{outbean.getEmail()});
                    break;
                case 2://query the table according to username;
                    String[] temp = {outbean.getUsername()};
                    cursor = db.query(TABLE_USER_BHK, null, "username=?", temp, null, null, null, null);
                    break;
                case 3: //query the password according to username;
                    cursor = db.query(TABLE_USER_BHK, new String[]{"password"}, "username=?", new String[]{outbean.getUsername()}, null,
                            null, null, null);
                    break;
                default:
                    break;
            }

            if (cursor != null) {
                while (cursor.moveToNext()) {
//					int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    String username = cursor.getString(cursor.getColumnIndex("username"));
                    String password = cursor.getString(cursor.getColumnIndex("password"));
                    String pic_url = cursor.getString(cursor.getColumnIndex("pic_url"));
                    int status = cursor.getInt(cursor.getColumnIndex("status"));
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

    public ArrayList<String> getAllEmail() {
        db = dBhelper.getWritableDatabase();

        ArrayList<String> arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER_BHK, new String[]{"email"}, null, null, null, null, null);
            if (cursor != null) {
                int i = 0;
                while (cursor.moveToNext()) {

                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    arrayList.add(email);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            closeAll();
        }
        return arrayList;
    }

    public String qureyPassword(UserBean outBean) {
        String password = "";
        db = dBhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER_BHK, new String[]{"password"}, "username=?", new String[]{outBean.getUsername()}, null,
                    null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    password = cursor.getString(cursor.getColumnIndex("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            closeAll();
        }
        return password;
    }

    public String qureyPicture(UserBean outBean) {
        String pictureUrl = "";
        db = dBhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER_BHK, new String[]{"pic_url"}, "username=?", new String[]{outBean.getUsername()}, null,
                    null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    pictureUrl = cursor.getString(cursor.getColumnIndex("pic_url"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            closeAll();
        }
        return pictureUrl;
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
                    cursor = db.rawQuery("select * from user_bhk where email= ?", new String[]{outbean.getEmail()});

                    break;
                case 2://query the table according to username;
                    String[] temp = {outbean.getUsername()};
                    cursor = db.query(TABLE_USER_BHK, null, "username=?", temp, null,
                            null, null, null);
                    break;
                default:
                    break;

            }
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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

            db.insert(TABLE_USER_BHK, null, contentValues);

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
            if (!outBean.getUsername().equals("") && outBean.getUsername() != null) {
                contentValues.put("username", outBean.getUsername());
            }
            if (!outBean.getPassword().equals("") && outBean.getPassword() != null) {
                contentValues.put("password", outBean.getPassword());
            }
            String[] email = {outBean.getEmail()};
            db.update(TABLE_USER_BHK, contentValues, "email = ?", email);
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
                    db.delete(TABLE_USER_BHK, null, null);
                    db.rawQuery("delete from user_bhk", null);
                    break;
                case 1:
                    String[] str_email = {outBean.getEmail()};
                    db.delete(TABLE_USER_BHK, "id=?", str_email);
                    break;
                case 2:
                    String[] str_name = {outBean.getUsername()};
                    db.delete(TABLE_USER_BHK, "username=?", str_name);
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


