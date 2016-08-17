package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.application.BadHabitsKillerApplication;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.curry.bhk.bhk.view.CircleImageView;

import java.util.List;

/**
 * Created by Curry on 2016/8/10.
 */
public class LoginActivity extends BaseActivity {

    private CircleImageView login_head_img_view;
    private EditText login_et_username;
    private EditText login_et_password;
    //    private CheckBox mLoginCheckBoxView;
    private android.widget.CheckBox mCheck;

    private String mloginusername = "";
    private String db_password = "";// sqlite
    private String input_password = "";// password now
    private String input_username = "";// username now
    private String input_email = "";// email now
    private String ago_username = "";

    //    private String mLoginHeadPicture = "";
    private boolean isRemeber;

    private List<UserBean> userbean_list;

    private UserdbOperator userdbOperator = new UserdbOperator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewInit();

        dataInit();

        /*
            judge is or not remember me
         */
        rememberMeCheckBox();

        usernameListener();
    }

    public void viewInit() {
        login_head_img_view = (CircleImageView) findViewById(R.id.login_img_head);
        login_et_username = (EditText) findViewById(R.id.login_et_username);
        login_et_password = (EditText) findViewById(R.id.login_et_password);
//        mLoginCheckBoxView = (CheckBox) findViewById(R.id.login_checkBox);
        mCheck = (android.widget.CheckBox) findViewById(R.id.login_checkBox);


    }

    private void dataInit() {
        /*
            get  username  from RegistActivity
         */
        SharedPreferences sharedPreferences = getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0);
        mloginusername = sharedPreferences.getString(PublicStatic.SHAREDPREFERENCES_USERNAME, "");
        login_et_username.setText(mloginusername);


         /*
        get head picture by username
         */
        matchHead();

    }

    private void usernameListener() {
        // about Soft keyboard
//        login_et_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_NEXT) {
//                    toastSomething(LoginActivity.this, "1234341123");
//                }
//                return false;
//            }
//        });

        ago_username = login_et_username.getText().toString();
        login_et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean isFocused) {

                if (!isFocused) {
//                    toastSomething(LoginActivity.this, "onFocusChange");
                    matchHead();
                }
            }
        });

        login_et_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                android:cursorVisible
                login_et_username.setCursorVisible(true);
            }
        });
    }

    /**
     * match true head pic according to username or email
     */
    private void matchHead() {
        mloginusername = login_et_username.getText().toString();
        if (!ago_username.equals(mloginusername)) {
            login_et_password.setText("");
        }
        UserBean userBean = new UserBean();
        userBean.setUsername(mloginusername);
        userbean_list = userdbOperator.queryUser( 2, userBean);
        if (!userbean_list.isEmpty()) {
            if (userbean_list.get(0).getPic_url().equals("default")) {
                login_head_img_view.setImageResource(R.drawable.defult_img);
            } else {
                Log.e(TAG, userbean_list.get(0).getPic_url());
                Bitmap bm = BitmapFactory.decodeFile(userbean_list.get(0).getPic_url());
                login_head_img_view.setImageBitmap(bm);
            }

            //get the password according to the username
            db_password = userbean_list.get(0).getPassword();
        }
    }

    /**
     * is or not remember password
     */
    private void rememberMeCheckBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0);
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    SharedPreferences.Editor editor = getSharedPreferences(
                            PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();
                    if (mCheck.isChecked()) {
                        Log.e(TAG, "isChecked");
                        isRemeber = true;
                    } else {
                        Log.e(TAG, "is not Checked");
                        isRemeber = false;
                    }
                    editor.putBoolean(PublicStatic.SHAREDPREFERENCES_CHECKBOX, isRemeber);
                    editor.commit();
                }
            }
        });
         /*
        remember userinfo is or not
         */
        if (sharedPreferences.getBoolean(PublicStatic.SHAREDPREFERENCES_CHECKBOX, false)) {
            Log.e(TAG, "here1");
            mCheck.setChecked(true);
            login_et_password.setText(db_password);
        } else {
            mCheck.setChecked(false);
        }
    }

    /**
     * click 'login'  btn
     */
    private void login() {
        input_password = login_et_password.getText().toString();
        input_username = login_et_username.getText().toString();

/*
    save the username which input
 */
        SharedPreferences.Editor edit = getSharedPreferences(
                PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();
        edit.putString(PublicStatic.SHAREDPREFERENCES_USERNAME, input_username);
        edit.commit();

        UserBean userbean = new UserBean();
        userbean.setUsername(input_username);

        if (input_username.equals("") || input_username == null) {
            toastSomething(LoginActivity.this, "Your username is null!");
        } else if (!userdbOperator.isExist(1, userbean) && !userdbOperator.isExist(2, userbean)) {
            toastSomething(LoginActivity.this, "The username or Email is not exist!");
        } else if (input_password.equals("") || input_password == null) {
            toastSomething(LoginActivity.this, "Please input your password!");
        } else if (input_password.equals(db_password)) {
            // static
            BadHabitsKillerApplication.mUsername = input_username;

            startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            toastSomething(LoginActivity.this, "Your password is wrong.");
        }
    }

    /**
     * this activity's all click event
     */
    public void clickInLoginActivity(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                login();
                break;
            case R.id.login_btn_register:
                startActivity(new Intent().setClass(LoginActivity.this, RegistActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
