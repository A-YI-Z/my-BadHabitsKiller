package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.UserNameAdapter;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.curry.bhk.bhk.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.List;

/**
 * Created by Curry on 2016/8/10.
 */
public class LoginActivity extends BaseActivity {

    private CircleImageView login_head_img_view;
    private EditText login_et_username;
    private EditText login_et_password;
    private android.widget.CheckBox mCheck;
    private TextView mVersionName;
    private PopupWindow mPopView;

    private String mUsernameOrEmail = "";
    private String ago_username = "";
    private String db_password = "";// sqlite
    private String input_password = "";// password now
    private boolean isRemeber;

    private List<String> myUsersList;
    private List<UserBean> userbean_list;
    private UserNameAdapter userNameAdapter;
    private UserdbOperator userdbOperator;
    private UserBean userBean = new UserBean();
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewInit();

        dataInit();

        usernameListener();
        /*
            judge is or not remember me
         */
        rememberMeCheckBox();

    }

    public void viewInit() {
        login_head_img_view = (CircleImageView) findViewById(R.id.login_img_head);
        login_et_username = (EditText) findViewById(R.id.login_et_username);
        login_et_password = (EditText) findViewById(R.id.login_et_password);
        mCheck = (CheckBox) findViewById(R.id.login_checkBox);
        mVersionName = (TextView) findViewById(R.id.tv_login_versionName);

    }

    private void dataInit() {
        userdbOperator = new UserdbOperator(this);
        editor = getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();
        /*
            get  username  from RegistActivity
         */
        SharedPreferences sharedPreferences = getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0);
        boolean isEmailorUsername = sharedPreferences.getBoolean(PublicStatic.SHAREDPREFERENCES_EMAIL_OR_USERNAME, true);
        if (isEmailorUsername) {//true:Email
            mUsernameOrEmail = sharedPreferences.getString(PublicStatic.SHAREDPREFERENCES_EMAIL, "");
        } else {
            mUsernameOrEmail = sharedPreferences.getString(PublicStatic.SHAREDPREFERENCES_USERNAME, "");
        }
        login_et_username.setText(mUsernameOrEmail);

         /*
        get head picture by username
         */
        matchHead();

    }

    private void usernameListener() {
        ago_username = login_et_username.getText().toString();
//        login_et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean isFocused) {
//                if (!isFocused) {
//                    matchHead();
//                }
//            }
//        });

        login_et_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                android:cursorVisible
                login_et_username.setCursorVisible(true);
            }
        });

        login_et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!ago_username.equals(login_et_password.getText().toString())) {
                    login_et_password.setText("");
                    matchHead();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /**
     * match true head pic according to username or email
     */
    private void matchHead() {


        mUsernameOrEmail = login_et_username.getText().toString();
//        UserBean userBean = new UserBean();
        if (mUsernameOrEmail.contains("@")) {//judge is a email
            userBean.setEmail(mUsernameOrEmail);
            userbean_list = userdbOperator.queryUser(1, userBean);

//            editor.putString(PublicStatic.SHAREDPREFERENCES_EMAIL, mUsernameOrEmail);
//            editor.putBoolean(PublicStatic.SHAREDPREFERENCES_EMAIL_OR_USERNAME, true);
        } else {
            userBean.setUsername(mUsernameOrEmail);
            userbean_list = userdbOperator.queryUser(2, userBean);
//
//            editor.putString(PublicStatic.SHAREDPREFERENCES_USERNAME, mUsernameOrEmail);
//            editor.putBoolean(PublicStatic.SHAREDPREFERENCES_EMAIL_OR_USERNAME, false);
        }


        if (!userbean_list.isEmpty()) {
            String headUrl = userbean_list.get(0).getPic_url();
            if (headUrl.equals("default")) {
                login_head_img_view.setImageResource(R.drawable.defult_img);
            } else {
//                Log.e(TAG, headUrl);
//                Bitmap bm = BitmapFactory.decodeFile(headUrl);
//                bm = CheckBitmapDegree.rotateBitmapByDegree(bm, CheckBitmapDegree.getBitmapDegree(headUrl));
//                login_head_img_view.setImageBitmap(bm);

                String imageUrl = ImageDownloader.Scheme.FILE.wrap(headUrl);

                ImageLoader.getInstance().displayImage(imageUrl, login_head_img_view);
            }

            //get the password according to the username
            db_password = userbean_list.get(0).getPassword();
        } else {
            login_head_img_view.setImageResource(R.drawable.default_head);
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
//            Log.e(TAG, "here1");
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
        mUsernameOrEmail = login_et_username.getText().toString();


        editor.putString(PublicStatic.SHAREDPREFERENCES_USERNAME, mUsernameOrEmail);
        editor.putBoolean(PublicStatic.SHAREDPREFERENCES_EMAIL_OR_USERNAME, false);
        editor.commit();

        if (mUsernameOrEmail.equals("")) {
            toastSomething(LoginActivity.this, "Your username or email is null!");
        } else if (!userdbOperator.isExist(2, userBean) && !userdbOperator.isExist(1, userBean)) {
            toastSomething(LoginActivity.this, "The username or email is not exist!");
        } else if (input_password.equals("")) {
            toastSomething(LoginActivity.this, "Please input your password!");
        } else if (input_password.equals(db_password)) {

            // static
            BaseActivity.mUsername = userbean_list.get(0).getUsername();
            BaseActivity.mHeadUrl = userbean_list.get(0).getPic_url();
            BaseActivity.mEmail = userbean_list.get(0).getEmail();
            BaseActivity.mPassword = userbean_list.get(0).getPassword();

            startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
            finish();

            userBean.setStatus(1);
            userdbOperator.updateUser(userBean, 3);
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
//            case R.id.dropdown_button:
//                Log.e(TAG, "clickInLoginActivity: ");
//                showPopWindow();
//                break;
            default:
                break;
        }
    }

    private void showPopWindow() {
        UserdbOperator userdbOperator = new UserdbOperator(LoginActivity.this);
        myUsersList = userdbOperator.getUserNameByStatus(LoginActivity.this);
        Log.e(TAG, "showPopWindow: ~~~~~~~~~~~~~~~~~~~");
        if (!myUsersList.isEmpty()) {
            Log.e(TAG, "showPopWindow: ");
            userNameAdapter = new UserNameAdapter(this, myUsersList);
            ListView mUsersListView = new ListView(LoginActivity.this);
            mUsersListView.setBackgroundResource(R.drawable.et_unfouce);
            mUsersListView.setAdapter(userNameAdapter);

            mPopView = new PopupWindow(mUsersListView, login_et_username.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopView.setFocusable(true);
            mPopView.setOutsideTouchable(true);
            mPopView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            mPopView.showAsDropDown(login_et_username);

        }
        deleteOrChooseUser();

    }

    private void deleteOrChooseUser() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popwindow_item, null);
        final ImageButton mDeleteUserBtn = (ImageButton) view.findViewById(R.id.pop_delete);
        final TextView mUsersTv = (TextView) view.findViewById(R.id.pop_username);

        mUsersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsersName = mUsersTv.getText().toString();
                login_et_username.setText(mUsersName);
                mPopView.dismiss();
                matchHead();
            }
        });
        mDeleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBean.setEmail(mUsersTv.getText().toString());
                userBean.setStatus(0);
                userdbOperator.updateUser(userBean, 3);

                userNameAdapter.notifyDataSetChanged();
                if (myUsersList.isEmpty()) {
                    mPopView.dismiss();
                }
            }
        });
    }

}
