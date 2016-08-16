package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.curry.bhk.bhk.R;

public class AddActivity extends BaseActivity {

    private TextView mDescriptionNumTV;
    private EditText mAddTitleET;
    private EditText mAddDescriptionET;

    private int MAX_LENGTH = 200;  //
    private int Rest_Length = MAX_LENGTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        viewInit();

        countDescriptionNum();
    }

    public void dataInit() {
    }

    public void viewInit() {
        mDescriptionNumTV = (TextView) findViewById(R.id.add_tv_description_num);
        mAddTitleET = (EditText) findViewById(R.id.add_et_title);
        mAddDescriptionET = (EditText) findViewById(R.id.add_et_description);
    }

    /*
 *  edittext计算剩余数字
 */
    private void countDescriptionNum() {

        mAddDescriptionET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Rest_Length = mAddDescriptionET.getText().length();
                if(Rest_Length>199){
                    mDescriptionNumTV.setTextColor(Color.RED);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                mDescriptionNumTV.setText(Rest_Length + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                mDescriptionNumTV.setText(Rest_Length + "/200");
//                selectionStart = mAddDescriptionET.getSelectionStart();
//                selectionEnd = mAddDescriptionET.getSelectionEnd();
//if (temp.length() > num) {
//s.delete(selectionStart - 1, selectionEnd);
//int tempSelection = selectionStart;
//mAddDescriptionET.setText(s);
//mAddDescriptionET.setSelection(tempSelection);//设置光标在最后
//}
            }
        });
    }

    public void addAcitvityOnclick(View view) {
        {
            switch (view.getId()) {
                case R.id.add_complete_btn:
                    toastSomething(AddActivity.this, "Add success!");
                    startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
                    finishActivity();
                    break;
                case R.id.add_back_img:
                    startActivity(new Intent().setClass(AddActivity.this, MainActivity.class));
                    finishActivity();
                    break;
                default:
                    break;
            }
        }
    }
}
