package com.curry.bhk.bhk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.curry.bhk.bhk.R;

public class BaseActivity extends Activity {
    String TAG = "curry";
    boolean exit_flag = false;

    public static int eventItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() called with: " + "");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() called with: " + "");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() called with: " + "");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart() called with: " + "");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called with: " + "");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() called with: " + "");
        super.onDestroy();
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
            System.exit(0);
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
}
