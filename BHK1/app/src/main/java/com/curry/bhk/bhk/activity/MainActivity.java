package com.curry.bhk.bhk.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.application.BadHabitsKillerApplication;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.fragment.NewFragment;
import com.curry.bhk.bhk.fragment.PendingFragment;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
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

        addFragment(0);
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

        mUserNameTV.setText(BadHabitsKillerApplication.mUsername);

        UserdbOperator userdbOperator = new UserdbOperator(MainActivity.this);
        UserBean userBean = new UserBean();
        userBean.setUsername(BadHabitsKillerApplication.mUsername);
        List<UserBean> userbean_list = userdbOperator.queryUser(2, userBean);
        if (!userbean_list.isEmpty()) {
            if (userbean_list.get(0).getPic_url().equals("default")) {
                mHeadImageView.setImageResource(R.drawable.defult_img);
            } else {
                Bitmap bm = BitmapFactory.decodeFile(userbean_list.get(0).getPic_url());
                mHeadImageView.setImageBitmap(bm);
            }
            BadHabitsKillerApplication.mEmail = userbean_list.get(0).getEmail();
        }
    }

    /**
     * Slide layout  ctrl
     */
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

    /**
     * click event  in this activity
     */
    public void menu_onClick(View view) {

        switch (view.getId()) {
            case R.id.title_bar_menu_btn:
                mDraglayout.open();
                break;
            case R.id.menu_new:
                addFragment(0);//New
                break;
            case R.id.menu_pending:
                addFragment(1);//Pending
                break;
            case R.id.menu_on_Hold:
                addFragment(2);//On Hold
                break;
            case R.id.menu_Resolved:
                addFragment(3);//Resolved
                break;
            case R.id.menu_Profiled:
                addFragment(4);//Profiled
                break;
            case R.id.menu_About:
                addFragment(5);//About
                break;
            case R.id.buttonFloat:
                this.startActivity(new Intent(MainActivity.this, AddActivity.class));
                finishActivity();
                break;
            default:
                break;
        }
    }

    /**
     * add  the fragments
     */
    public void addFragment(int numFragment) {
        mDraglayout.close();

        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
        switch (numFragment) {
            case 0:
                mTitlebarName.setText("New");

                NewFragment newFragment = new NewFragment();
                fragmentTransaction.replace(R.id.fragment, newFragment);
                break;
            case 1:
                mTitlebarName.setText("Pending");

                PendingFragment pendingFragment = new PendingFragment();
                fragmentTransaction.replace(R.id.fragment, pendingFragment);
                break;
            case 2:
                mTitlebarName.setText("On Hold");
                break;
            case 3:
                mTitlebarName.setText("Resolved");
                break;
            case 4:
                mTitlebarName.setText("Profiled");
                break;
            case 5:
                mTitlebarName.setText("About");
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }

}
