package com.curry.bhk.bhk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.gc.materialdesign.views.ButtonRectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private ImageView regist_head_img;
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
        regist_head_img = (ImageView) findViewById(R.id.regist_img_choose);
        regist_complete = (ButtonRectangle) findViewById(R.id.regist_btn_complete);


//        regist_complete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                et_input_set();
//                if (!input_isnot_legal) {
//                    completeRegist();
//                }
//            }
//        });


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

        input_isnot_legal = true;
        if (username.equals("") || email.equals("") || password.equals("") || confirm_password.equals("")) {
            toastSomething(RegistActivity.this, "Please be sure to complete.");
        } else if (!email.equals("") && !isEmail(email)) {
            toastSomething(RegistActivity.this, "Is not a true email.");
        } else if (!username.matches("[a-zA-Z]+")) {// judge strings is  all English letters
            toastSomething(RegistActivity.this, "Nickname must English letters.");
        } else if (password.length() < 8) {
            toastSomething(RegistActivity.this, "The password is too short");
        } else if (!confirm_password.equals(password)) {
            toastSomething(RegistActivity.this, "The two passwords don't match,please input again.");
        } else if (mHeadImageUrl.equals("")) {
            toastSomething(RegistActivity.this,"Please choose a picture for your head portrait.");
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
//        else{
        UserBean userBean = new UserBean();
        userBean.setUsername(username);
        userBean.setEmail(email);
        userBean.setPassword(password);
        userBean.setStatus(1);
        userBean.setPic_url(mHeadImageUrl);

        UserdbOperator userdbOperator = new UserdbOperator();

//            List<UserBean> userbean_list = userdbOperator.queryUser(RegistActivity.this, 0, null);
//            if (userbean_list.size() != 0) {
//                for (int i = 0; i < userbean_list.size(); i++) {
//                    if (!email.equals(userbean_list.get(i).getEmail())) {
        userdbOperator.insertUser(RegistActivity.this, userBean);

        SharedPreferences.Editor edit = getSharedPreferences(
                PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();
        edit.putString(PublicStatic.SHAREDPREFERENCES_USERNAME, username);
        edit.commit();

        Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
        //                 intent.putExtra("USERNAME", username);
        startActivity(intent);
        toastSomething(RegistActivity.this,"Regist  success!");
        finish();
//        }
//        else {
//                        Toast.makeText(RegistActivity.this, "The email address is exits!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
    }

    /**
     * choose head image
     */
    private void choose_head_img() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
        builder.setTitle("Choose head-image");
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
//                                regist_head_img.setImageResource(R.raw.defult_img);
//                                mHeadImageUrl = "android:resource://"+ getPackageName() + "/" + R.raw.defult_img;
//                                // System.out.println(PublicStatic.head_path);
                                toastSomething(RegistActivity.this,"Default picture set success!");
                                break;
                            case 1:
                                Intent intent1 = new Intent(Intent.ACTION_PICK,
                                        null);
                                intent1.setDataAndType(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        "image/*");
                                startActivityForResult(intent1, 1);
//                                    Toast.makeText(RegistActivity.this, "Photo album",
//                                            Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Intent intent2 = new Intent(
                                        "android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(intent2, 1);
//                                    Toast.makeText(RegistActivity.this, "take a photo",
//                                            Toast.LENGTH_SHORT).show();
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
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri myuri = data.getData();
                ContentResolver resolver = getContentResolver();
                Cursor mycursor = resolver.query(myuri, null, null, null, null);
                if (mycursor != null) {
                    mycursor.moveToNext();
                    mHeadImageUrl = mycursor.getString(mycursor
                            .getColumnIndex("_data"));
                    Bitmap bm = BitmapFactory.decodeFile(mHeadImageUrl);
                    regist_head_img.setImageBitmap(bm);
                }
                mycursor.close();
            } else {
                toastSomething(RegistActivity.this,"Don't choose any picture.");
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap mybitmap = (Bitmap) bundle.get("data");
                    regist_head_img.setImageBitmap(mybitmap);
                    mHeadImageUrl = saveFile(mybitmap);
                }
            } else {
                toastSomething(RegistActivity.this,"Taking a photo is defeated.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * save picture
     */
    public String saveFile(Bitmap bmp) {
//        String sdStatue = Environment.getExternalStorageState();
//        if (!sdStatue.endsWith(Environment.MEDIA_MOUNTED)) {
//            System.out.println("can't use SD card");
//            return null;
//        }
        String sd_root_path = android.os.Environment.getExternalStorageDirectory().getPath();
        FileOutputStream b = null;
        File dirFile = new File(sd_root_path + "/myImage");
        if (!dirFile.isDirectory()) {
            dirFile.mkdir();
        }
//        SimpleDateFormat sDattFormat = new SimpleDateFormat("yyyMMddhhmmss");
//        String data = sDattFormat.format(new Date());
//        String pictureName = sd_root_path + "/myImage/" + data + ".jpg";
        String pictureName = sd_root_path + "/myImage/" + email + ".jpg";
        try {
            b = new FileOutputStream(pictureName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sd_root_path;
    }
}
