package com.curry.bhk.bhk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.ImageChooseAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.bean.ImageItem;
import com.curry.bhk.bhk.sqlite.EventdbOperator;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.gc.materialdesign.views.ButtonRectangle;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddActivity extends BaseActivity {
    public static int id = 0;

    private TextView mDescriptionNumTV;
    private EditText mAddTitleET;
    private EditText mAddDescriptionET;
    private ImageView mAddBack;
    private ButtonRectangle mAddComplete;
    private GridView mPhotoGridView;

    private int mRestLength = 0;
    private String mTitleStr = "";
    private String mDescriptionStr = "";
    private StringBuffer mImageUrl = new StringBuffer();

    public static List<ImageItem> mDataList = new ArrayList<>();
    private ImageChooseAdapter mImageChooseAdapter;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int EMAIL_BACK = 2;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dataInit();

        viewInit();

        addAcitvityOnclick();

        countDescriptionNum();
    }

//    protected void onPause() {
//        super.onPause();
//        saveTempToPref();
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        saveTempToPref();
//    }

//    private void saveTempToPref() {
//
////        SharedPreferences sp = getSharedPreferences(
////                PublicStatic.APPLICATION_NAME, MODE_PRIVATE);
////        String prefStr = JSON.toJSONString(mDataList);
////        sp.edit().putString(PublicStatic.PREF_TEMP_IMAGES, prefStr).commit();
//
//        mAddDescriptionET.setText(mDescriptionStr);
//        mAddTitleET.setText(mTitleStr);
//    }


    public void dataInit() {
//        SharedPreferences sp = getSharedPreferences(
//                PublicStatic.APPLICATION_NAME, MODE_PRIVATE);
//        String prefStr = sp.getString(PublicStatic.PREF_TEMP_IMAGES, null);
//        if (!TextUtils.isEmpty(prefStr)) {
//            List<ImageItem> tempImages = JSON.parseArray(prefStr,
//                    ImageItem.class);
//            mDataList = tempImages;
//        }
//        List<ImageItem> incomingDataList = (List<ImageItem>) getIntent().getSerializableExtra(PublicStatic.EXTRA_IMAGE_LIST);
//        if (incomingDataList != null) {
//            Log.e(TAG, "!!!!!!");
//            mDataList.addAll(incomingDataList);
//        }
    }

    private void viewInit() {
        mDescriptionNumTV = (TextView) findViewById(R.id.add_tv_description_num);
        mAddTitleET = (EditText) findViewById(R.id.add_et_title);
        mAddDescriptionET = (EditText) findViewById(R.id.add_et_description);

        mAddComplete = (ButtonRectangle) findViewById(R.id.add_complete_btn);
        mAddBack = (ImageView) findViewById(R.id.back_img);


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
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDescriptionNumTV.setText(mRestLength + "/200");
            }
        });
    }

    public void addAcitvityOnclick() {
        mAddBack.setOnClickListener(new OnclickEvent());
        mAddComplete.setOnClickListener(new OnclickEvent());

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
                default:
                    break;
            }
        }
    }

    private void getPhotoOnItemClick() {

//        mPhotoGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mImageChooseAdapter = new ImageChooseAdapter(AddActivity.this, mDataList);
        mPhotoGridView.setAdapter(mImageChooseAdapter);
        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == getDataSize()) {
                    choosePhoto();
                } else {
                    Intent intent = new Intent(AddActivity.this, ImageZoomActivity.class);
                    intent.putExtra(PublicStatic.EXTRA_IMAGE_LIST, (Serializable) mDataList);
                    intent.putExtra(PublicStatic.EXTRA_CURRENT_IMG_POSITION, i);
                    startActivity(intent);
                }
            }
        });
    }

    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
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

        if (!mDataList.isEmpty()) {
            int size = mDataList.size();
            for (int i = 0; i < size; i++) {
                mImageUrl.append(mDataList.get(i).sourcePath + "#");
            }
            mImageUrl.substring(0, mImageUrl.length() - 1);
        }


        eventBean.setDescription(mDescriptionStr);
        eventBean.setAuthor(BaseActivity.mUsername);
        eventBean.setPhotos_url(mImageUrl.toString());
        eventBean.setTitle(mTitleStr);
        eventBean.setEmail(BaseActivity.mEmail);
        eventBean.setStatus(0);
        eventBean.setTime(data);
        eventBean.setResolvedby("");

        eventdbOperator.insertEvent(eventBean);
    }


    /**
     * choose photo
     */
    private void choosePhoto() {
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
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            takeNewPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                    dialog.dismiss();
                }
            });

            useOldPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddActivity.this, ImageBucketChooseActivity.class);
                    intent.putExtra(PublicStatic.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize());
//                    startActivity(intent);
                    startActivityForResult(intent, CHOOSE_PICTURE);
//                    finishActivity();

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

    private int getAvailableSize() {
        int availSize = PublicStatic.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    /**
     * take a photo and return this activity
     */
    public void takePhoto() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory().getPath() + "/BHK/", BaseActivity.mUsername + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);

//        Intent intentCapture = new Intent("android.media.action.IMAGE_CAPTURE");
//        startActivityForResult(intentCapture, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (mDataList.size() < PublicStatic.MAX_IMAGE_SIZE
                        && resultCode == -1 && !TextUtils.isEmpty(path)) {
                    ImageItem item = new ImageItem();
                    item.sourcePath = path;
                    mDataList.add(item);
                }
                break;
            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    List<ImageItem> incomingDataList = (ArrayList<ImageItem>) bundle.getSerializable(PublicStatic.EXTRA_IMAGE_LIST);
                    if (incomingDataList != null) {
                        Log.e(TAG, "!!!!!!");
                        mDataList.addAll(incomingDataList);
                    }
                    Log.e(TAG, "back");
                    getPhotoOnItemClick();
                }

                break;
            case EMAIL_BACK:
                startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
                finishActivity();
                break;
            default:
                break;
        }

    }

    /**
     * send email  or not
     */
    public void popUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do you want to mass E-mail?");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toastSomething(AddActivity.this, "Add success!");
                        dialog.dismiss();
                        startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
                        finishActivity();
                        //add data
                        addDataIntoSql();
                    }
                });
        builder.setPositiveButton("Sure",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSystemEmail();
                        dialog.dismiss();
                        //add data
                        addDataIntoSql();
                    }
                });
        builder.create().show();
    }

    /**
     * go to system email
     */
    public void goToSystemEmail() {

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//        String[] tos = {BaseActivity.mEmail};
        ArrayList<String> arrayList = new UserdbOperator(AddActivity.this).getAllEmail();
        String[] allEmail = arrayList.toArray(new String[arrayList.size()]);

        intent.putExtra(Intent.EXTRA_EMAIL, allEmail);
//        intent.putExtra(Intent.EXTRA_CC, ccs);
        intent.putExtra(Intent.EXTRA_TEXT, mDescriptionStr);
        intent.putExtra(Intent.EXTRA_SUBJECT, mTitleStr);


        if (mDataList != null) {
            int size = mDataList.size();
            ArrayList imageUris = new ArrayList();
            for (int i = 0; i < size; i++) {
                File file = new File(mDataList.get(i).sourcePath);
                imageUris.add(Uri.fromFile(file));
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        }

        intent.setType("image/*");
        intent.setType("message/rfc882");
        Intent.createChooser(intent, "Choose Email Client.");
        startActivityForResult(intent, EMAIL_BACK);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
        finishActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        saveTempToPref();

        mImageChooseAdapter.notifyDataSetChanged();//when delete picture  , should notify data.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDataList = null;
    }
}
