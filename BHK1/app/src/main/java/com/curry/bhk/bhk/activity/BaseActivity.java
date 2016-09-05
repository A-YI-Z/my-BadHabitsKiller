package com.curry.bhk.bhk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.utils.ActivityCollector;

public class BaseActivity extends Activity {
    String TAG = "curry";
    boolean exit_flag = false;

    public static String mUsername = "";
    public static String mEmail = "";
    public static int eventItemId = 0;
    public static String mHeadUrl = "";
    public static String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, getClass().getSimpleName());

        ActivityCollector.addActivity(this);
    }


    public void finishActivity() {
        finish();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    @Override
    public void onBackPressed() {

        if (!exit_flag) {
            toastSomething(BaseActivity.this, "Please again to exit.");
            delayHandler.sendEmptyMessageDelayed(0, 2000);
            exit_flag = true;
        } else {
            ActivityCollector.finishAll();
        }
    }

    public Handler delayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            exit_flag = false;
        }
    };

    /**
     * toast some content
     */
    public void toastSomething(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    /**
     * hide the softkeyboard
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
