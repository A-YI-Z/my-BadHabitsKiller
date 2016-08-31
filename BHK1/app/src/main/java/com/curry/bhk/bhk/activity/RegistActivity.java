package com.curry.bhk.bhk.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.curry.bhk.bhk.utils.SavePicture;
import com.curry.bhk.bhk.view.CircularImageView;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class RegistActivity extends BaseActivity {
    private EditText regist_et_nickname;
    private EditText regist_et_email;
    private EditText regist_et_password;
    private EditText regist_et_confirm_password;
    private CircularImageView regist_head_img;
    private ButtonRectangle regist_complete;

    private String email;
    private String username;
    private String password;
    private String confirm_password;
    private String mHeadImageUrl = "";

    boolean input_isnot_legal = false;//if input is legal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        viewInit();
    }

    /**
     *
     */
    public void viewInit() {
        regist_et_nickname = (EditText) findViewById(R.id.regist_et_nickname);
        regist_et_email = (EditText) findViewById(R.id.regist_et_email);
        regist_et_password = (EditText) findViewById(R.id.regist_et_password);
        regist_et_confirm_password = (EditText) findViewById(R.id.regist_et_confirm_password);
        regist_head_img = (CircularImageView) findViewById(R.id.regist_img_choose);

        regist_complete = (ButtonRectangle) findViewById(R.id.regist_btn_complete);
    }

    /**
     * @param view
     */
    public void clickInRegistActivity(View view) {

        switch (view.getId()) {
            case R.id.regist_img_back://back
                startActivity(new Intent().setClass(RegistActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.regist_btn_complete://complete
                et_input_set();
                if (!input_isnot_legal) {
                    completeRegist();
                }
                break;
            case R.id.regist_img_choose://choose head image
                choose_head_img();
                break;
            default:
                break;
        }
    }

    /**
     *
     */
    private void et_input_set() {
        username = regist_et_nickname.getText().toString();
        email = regist_et_email.getText().toString();
        password = regist_et_password.getText().toString();
        confirm_password = regist_et_confirm_password.getText().toString();

        UserdbOperator userdbOperator = new UserdbOperator(RegistActivity.this);
        UserBean userBean = new UserBean();
        userBean.setEmail(email);
        userBean.setUsername(username);

        //password is num and English letters and length more than 8 while less than 16.
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";

        input_isnot_legal = true;
        if (username.equals("") || email.equals("") || password.equals("") || confirm_password.equals("")) {
            toastSomething(RegistActivity.this, "Please fill in.");
        } else if (!isEmail(email)) {
            toastSomething(RegistActivity.this, "Is not a true email address.");
        } else if (userdbOperator.isExist(1, userBean)) {
            toastSomething(RegistActivity.this, "The email is exists .");
        } else if (userdbOperator.isExist(2, userBean)){
            toastSomething(RegistActivity.this, "Nickname is exist.");
        }else if (username.length() > 10) {// judge strings is  all English letters
            toastSomething(RegistActivity.this, "Nickname is too long.");
        } else if (!password.matches(regex)) {
            toastSomething(RegistActivity.this, "The password is wrong.");
        } else if (!confirm_password.equals(password)) {
            toastSomething(RegistActivity.this, "The two passwords don't match,please input again.");
        } else if (mHeadImageUrl.equals("")) {
            toastSomething(RegistActivity.this, "Please choose a picture for your head portrait.");
        } else {
            input_isnot_legal = false;
        }
    }

    /**
     * judge email is true or not
     *
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * click "complete"
     */
    private void completeRegist() {
        UserBean userBean = new UserBean();
        userBean.setUsername(username);
        userBean.setEmail(email);
        userBean.setPassword(password);
        userBean.setStatus(0);
        userBean.setPic_url(mHeadImageUrl);

        UserdbOperator userdbOperator = new UserdbOperator(this);
        userdbOperator.insertUser(userBean);

        SharedPreferences.Editor edit = getSharedPreferences(
                PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();
        edit.putString(PublicStatic.SHAREDPREFERENCES_USERNAME, username);
        edit.putString(PublicStatic.SHAREDPREFERENCES_EMAIL, email);
        edit.putBoolean(PublicStatic.SHAREDPREFERENCES_EMAIL_OR_USERNAME, true);
        edit.putBoolean(PublicStatic.SHAREDPREFERENCES_CHECKBOX, false);
        edit.commit();

        Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
//        intent.putExtra("EMAIL", email);

        startActivity(intent);
        toastSomething(RegistActivity.this, "Regist  success!");
        finish();
    }

    /**
     * choose head image
     */
    private void choose_head_img() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
        builder.setTitle("Choose a picture .");
        String items[] = {"Default picture", "Photo album", "Take a photo"};
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://default picture
//                                InputStream inputStream = getResources().openRawResource(R.raw.defult_img);
                                regist_head_img.setImageResource(R.drawable.defult_img);
                                mHeadImageUrl = "default";
                                break;
                            case 1:
                                Intent intentPick = new Intent(Intent.ACTION_PICK, null);
                                intentPick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intentPick, 0);

                                break;
                            case 2:
                                Intent intentCapture = new Intent("android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(intentCapture, 1);

                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mHeadImageUrl = new SavePicture(RegistActivity.this).pictureResult(requestCode, resultCode, data, regist_head_img);
        Log.e(TAG, mHeadImageUrl);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent().setClass(RegistActivity.this, LoginActivity.class));
        finishActivity();
    }
}
