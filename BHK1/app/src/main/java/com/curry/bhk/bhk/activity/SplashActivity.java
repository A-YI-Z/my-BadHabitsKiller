package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.view.Wave;

/**
 * Created by Curry on 2016/8/9.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        loading();
        new Delay().start();

    }

    /*
            sleep 5s go into mainactivity
         */
    class Delay extends Thread {
        @Override
        public void run() {
            try {

                sleep(5000);
                startActivity(new Intent().setClass(SplashActivity.this, LoginActivity.class));
                //  overridePendingTransition(out,in)
                overridePendingTransition(R.anim.activity_anim_in_from_right, R.anim.activity_anim_out_to_left);
                SplashActivity.this.finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

    private Wave mWaveDrawable;

    private void loading() {
        TextView textView = (TextView) findViewById(R.id.text_splash);
        mWaveDrawable = new Wave();
        mWaveDrawable.setBounds(0, 0, 100, 100);
        //noinspection deprecation
        mWaveDrawable.setColor(Color.WHITE);
        textView.setCompoundDrawables(null, null, mWaveDrawable, null);
        mWaveDrawable.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWaveDrawable.stop();
    }
}
