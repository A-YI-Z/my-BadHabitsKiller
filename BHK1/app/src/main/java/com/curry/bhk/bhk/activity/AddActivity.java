package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.ImageChooseAdapter;
import com.curry.bhk.bhk.application.BadHabitsKillerApplication;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.bean.ImageItem;
import com.curry.bhk.bhk.sqlite.EventdbOperator;
import com.gc.materialdesign.views.ButtonRectangle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddActivity extends BaseActivity {
    public static int id = 0;

    private TextView mDescriptionNumTV;
    private EditText mAddTitleET;
    private EditText mAddDescriptionET;
    private ImageView mImgChoose;
    private ImageView mAddBack;
    private ButtonRectangle mAddComplete;
    private GridView mPhotoGridView;

    private int mRestLength = 0;
    private String mTitleStr = "";
    private String mDescriptionStr = "";

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

        mPhotoGridView = (GridView)findViewById(R.id.gridview);
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
        mImgChoose.setOnClickListener(new OnclickEvent());
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

    private void getPhotoOnItemClick(){
        List<ImageItem> mPhotoList = new ArrayList<>();
        mImageChooseAdapter = new ImageChooseAdapter(AddActivity.this,mPhotoList);
        mPhotoGridView.setAdapter(mImageChooseAdapter);
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

            toastSomething(AddActivity.this, "Add success!");
            startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
            finishActivity();
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
        eventBean.setAuthor(BadHabitsKillerApplication.mUsername);
        eventBean.setPhotos_url("");
        eventBean.setTitle(mTitleStr);
        eventBean.setEmail(BadHabitsKillerApplication.mEmail);
        eventBean.setStatus(0);
        eventBean.setTime(data);

        eventdbOperator.insertEvent(eventBean);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
        finishActivity();
    }
}
