package com.curry.bhk.bhk.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.fragment.NewFragment;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.curry.bhk.bhk.view.CircleImageView;
import com.curry.bhk.bhk.view.DragLayout;
import com.curry.bhk.bhk.view.MyRelativeLayout;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

/**
 * Created by Curry on 2016/8/9.
 */
public class MainActivity extends BaseActivity {
    private DragLayout mDraglayout;
    private MyRelativeLayout mMenuRelativeLayout;
    private ImageView mTitleBarBotton;
    private CircleImageView mHeadImageView;
    private TextView mUserNameTV;
    private TextView mTitlebarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();

        mDraglayout.setDragListener(new myDrag());

        dataInit();

        addFragment("New");
    }

    public void viewInit() {
        mHeadImageView = (CircleImageView) findViewById(R.id.menu_head_img);
        mUserNameTV = (TextView) findViewById(R.id.menu_username);
        mDraglayout = (DragLayout) findViewById(R.id.drag);
        mMenuRelativeLayout = (MyRelativeLayout) findViewById(R.id.myrl);
        mTitleBarBotton = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mTitlebarName = (TextView) findViewById(R.id.title_bar_name);
    }

    public void dataInit() {
        SharedPreferences sharedPreferences = getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0);
        String mUserName = sharedPreferences.getString(PublicStatic.SHAREDPREFERENCES_USERNAME, "");
        mUserNameTV.setText(mUserName);

        UserdbOperator userdbOperator = new UserdbOperator();
        UserBean userBean = new UserBean();
        userBean.setUsername(mUserName);
        List<UserBean> userbean_list = userdbOperator.queryUser(MainActivity.this, 2, userBean);
        if (!userbean_list.isEmpty()) {
            if (userbean_list.get(0).getPic_url().equals("default")) {
                mHeadImageView.setImageResource(R.drawable.defult_img);
            } else {
                Bitmap bm = BitmapFactory.decodeFile(userbean_list.get(0).getPic_url());
                mHeadImageView.setImageBitmap(bm);
            }
        }
    }

    public class myDrag implements DragLayout.DragListener {

        @Override
        public void onOpen() {
            // TODO Auto-generated method stub
            // RotateAnimation rotate = new RotateAnimation(0, 360);
            // rotate.setDuration(700);
            // rotate.setFillAfter(true);
            // ima_cehua.startAnimation(rotate);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(500);
            alphaAnimation.setFillAfter(true);
            mTitleBarBotton.startAnimation(alphaAnimation);
//            ViewHelper.setAlpha(mTitleBarBotton, 0);
        }

        @Override
        public void onClose() {
            // tv.startAnimation(AnimationUtils.loadAnimation(Cehua.this, R.anim.shake));
//            ViewHelper.setAlpha(mTitleBarBotton, 1);
            mTitleBarBotton.startAnimation(AnimationUtils.loadAnimation(
                    MainActivity.this, R.anim.shake));
        }

        @Override
        public void onDrag(float percent) {
            ViewHelper.setAlpha(mMenuRelativeLayout, 1 - percent * 0.2f);
        }
    }

    public void menu_onClick(View view) {

        switch (view.getId()) {
            case R.id.title_bar_menu_btn:
                mDraglayout.open();
                break;
            case R.id.menu_new:
                addFragment("New");
                break;
            case R.id.menu_pending:
                addFragment("Pending");
                break;
            case R.id.menu_on_Hold:
                addFragment("On Hold");
                break;
            case R.id.menu_Resolved:
                addFragment("Resolved");
                break;
            case R.id.menu_Profiled:
                addFragment("Profiled");
                break;
            case R.id.menu_About:
                addFragment("About");
                break;
            case R.id.buttonFloat:
                this.startActivity(new Intent(MainActivity.this, AddActivity.class));
                finishActivity();
                break;
            default:
                break;
        }
    }

    public void addFragment(String menuName) {
        mDraglayout.close();
        mTitlebarName.setText(menuName);

        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
        NewFragment newFragment = new NewFragment();
        fragmentTransaction.replace(R.id.fragment, newFragment);
        fragmentTransaction.commit();
    }

}
