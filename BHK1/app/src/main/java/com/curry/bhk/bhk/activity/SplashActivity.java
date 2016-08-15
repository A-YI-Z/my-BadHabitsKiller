package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.os.Bundle;

import com.curry.bhk.bhk.R;

/**
 * Created by Curry on 2016/8/9.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Delay_goto().start();
    }

    /*
            sleep 5s go into mainactivity
         */
    class Delay_goto extends Thread {
        @Override
        public void run() {
            try {
                sleep(5000);
                startActivity(new Intent().setClass(SplashActivity.this, LoginActivity.class));
                //  overridePendingTransition(out,in)
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                SplashActivity.this.finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }
    }
}
