package com.curry.bhk.bhk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.ImageChooseAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.sqlite.EventdbOperator;
import com.curry.bhk.bhk.utils.SavePicture;
import com.gc.materialdesign.views.ButtonRectangle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends BaseActivity {
    public static int id = 0;

    private TextView mDescriptionNumTV;
    private EditText mAddTitleET;
    private EditText mAddDescriptionET;
    private ImageView mOnePhoto;
    private ImageView mAddBack;
    private ButtonRectangle mAddComplete;
    private GridView mPhotoGridView;

    private int mRestLength = 0;
    private String mTitleStr = "";
    private String mDescriptionStr = "";
    private String mImageUrl = "null";


    private ImageChooseAdapter mImageChooseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        viewInit();

        addAcitvityOnclick();

        countDescriptionNum();
    }

//    public void dataInit() {
//    }

    private void viewInit() {
        mDescriptionNumTV = (TextView) findViewById(R.id.add_tv_description_num);
        mAddTitleET = (EditText) findViewById(R.id.add_et_title);
        mAddDescriptionET = (EditText) findViewById(R.id.add_et_description);

        mAddComplete = (ButtonRectangle) findViewById(R.id.add_complete_btn);
        mAddBack = (ImageView) findViewById(R.id.back_img);
        mOnePhoto = (ImageView) findViewById(R.id.image_photo);

        mPhotoGridView = (GridView) findViewById(R.id.gridview);
    }

    /**
     * count  number of  the words.
     */
    private void countDescriptionNum() {

        mAddDescriptionET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRestLength = mAddDescriptionET.getText().length();
                if (mRestLength > 199) {
                    mDescriptionNumTV.setTextColor(Color.RED);
                } else {
                    mDescriptionNumTV.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
//                mDescriptionNumTV.setText(Rest_Length + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDescriptionNumTV.setText(mRestLength + "/200");
            }
        });
    }

    public void addAcitvityOnclick() {
        mAddBack.setOnClickListener(new OnclickEvent());
//        mImgChoose.setOnClickListener(new OnclickEvent());
        mAddComplete.setOnClickListener(new OnclickEvent());

        mOnePhoto.setOnClickListener(new OnclickEvent());

        getPhotoOnItemClick();
    }

    private class OnclickEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_complete_btn:
                    addComplete();
                    break;
                case R.id.back_img:
                    startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
                    finishActivity();
                    break;
                case R.id.image_photo:
                    chooseOnePhoto();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * ?????????????waiting demo finish?????????????
     */
    private void getPhotoOnItemClick() {

//        List<ImageItem> mPhotoList = new ArrayList<>();
//        mImageChooseAdapter = new ImageChooseAdapter(AddActivity.this,mPhotoList);
//        mPhotoGridView.setAdapter(mImageChooseAdapter);
//
//        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
    }


    /**
     * click the add btn ,execute this  method
     */
    private void addComplete() {
        mTitleStr = mAddTitleET.getText().toString();
        mDescriptionStr = mAddDescriptionET.getText().toString();

        if (mTitleStr.equals("")) {
            toastSomething(AddActivity.this, "The title can't be null.");
        } else if (mDescriptionStr.equals("")) {
            toastSomething(AddActivity.this, "The description can't be null.");
        } else {
            //add data
            addDataIntoSql();

            popUpDialog();


        }
    }

    /**
     * add data into sql
     */
    private void addDataIntoSql() {
        SimpleDateFormat simpleDateFormatFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = simpleDateFormatFormat.format(new Date());

        EventBean eventBean = new EventBean();
        EventdbOperator eventdbOperator = new EventdbOperator(AddActivity.this);

//        int id = eventdbOperator.queryEvent(0,null).size();
//        eventBean.setId(id++);

        eventBean.setDescription(mDescriptionStr);
        eventBean.setAuthor(BaseActivity.mUsername);
        eventBean.setPhotos_url(mImageUrl);
        eventBean.setTitle(mTitleStr);
        eventBean.setEmail(BaseActivity.mEmail);
        eventBean.setStatus(0);
        eventBean.setTime(data);
        eventBean.setResolvedby("");


        eventdbOperator.insertEvent(eventBean);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
        finishActivity();
    }

    /**
     * choose photo
     */
    private void chooseOnePhoto() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View viewContent = LayoutInflater.from(this).inflate(R.layout.alert_choose_photo, null);
            TextView takeNewPhotos = (TextView) viewContent.findViewById(R.id.dialog_content_take_new_photo_txt);
            TextView useOldPhotos = (TextView) viewContent.findViewById(R.id.dialog_content_use_old_photo_txt);
            TextView cancel = (TextView) viewContent.findViewById(R.id.dialog_content_cancel_txt);
            TextPaint tp = cancel.getPaint();
            tp.setFakeBoldText(true);

            builder.setView(viewContent);
            final AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.popup_window_style);
            dialog.show();

            takeNewPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentNew = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intentNew, 1);
                    overridePendingTransition(R.anim.activity_anim_in_from_right, R.anim.activity_anim_out_to_left);
                    dialog.dismiss();
                }
            });

            useOldPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentOld = new Intent(Intent.ACTION_PICK, null);
                    intentOld.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intentOld, 1);
                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
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
                    mImageUrl = mycursor.getString(mycursor
                            .getColumnIndex("_data"));
                    Bitmap bitmap = BitmapFactory.decodeFile(mImageUrl);
                    bitmap = RegistActivity.rotateBitmapByDegree(bitmap, RegistActivity.getBitmapDegree(mImageUrl));
                    mOnePhoto.setImageBitmap(bitmap);
                }
                mycursor.close();
            } else {
                toastSomething(AddActivity.this, "Don't choose any picture.");
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    bitmap = RegistActivity.rotateBitmapByDegree(bitmap, RegistActivity.getBitmapDegree(new SavePicture(this).saveFile(bitmap)));
                    mOnePhoto.setImageBitmap(bitmap);
                    mImageUrl = new SavePicture(this).saveFile(bitmap);
                }
            } else {
                toastSomething(AddActivity.this, "Taking a photo is defeated.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * send email  or not
     */
    public void popUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do you want to mass E-mail?");
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toastSomething(AddActivity.this, "Add success!");
                        dialog.dismiss();
                        startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
                        finishActivity();

                    }
                });
        builder.setPositiveButton("Sure",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSystemEmail();
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    /**
     * go to system email
     */
    public void goToSystemEmail() {

        // Here should use 'mailto:' , otherwise can't match mail application
        Uri uri = Uri.parse("mailto:" + BaseActivity.mEmail);
        String[] email = {"123@qq.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email); // Cc people
        intent.putExtra(Intent.EXTRA_SUBJECT, mTitleStr); // The theme
        intent.putExtra(Intent.EXTRA_TEXT, mDescriptionStr); // The body
        startActivity(Intent.createChooser(intent, "Please choose a E-mail application."));
        finishActivity();
    }
}
