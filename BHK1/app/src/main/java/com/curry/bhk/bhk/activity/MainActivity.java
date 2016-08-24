package com.curry.bhk.bhk.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.fragment.AboutFragment;
import com.curry.bhk.bhk.fragment.NewFragment;
import com.curry.bhk.bhk.fragment.OnHoldFragment;
import com.curry.bhk.bhk.fragment.PendingFragment;
import com.curry.bhk.bhk.fragment.ProfiledFragment;
import com.curry.bhk.bhk.fragment.ResolvedFragment;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.CheckBitmapDegree;
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
    private ListView mMenuList;

    private int mSelectMenu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();

        mDraglayout.setDragListener(new myDrag());

        dataInit();
        Log.e(TAG, getIntent().getIntExtra("MENUID", 0) + "");
        addFragment(getIntent().getIntExtra("MENUID", 0));

        menuOnClick();
    }

    public void viewInit() {
        mHeadImageView = (CircleImageView) findViewById(R.id.menu_head_img);
        mUserNameTV = (TextView) findViewById(R.id.menu_username);
        mDraglayout = (DragLayout) findViewById(R.id.drag);
        mMenuRelativeLayout = (MyRelativeLayout) findViewById(R.id.myrl);
        mTitleBarBotton = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mTitlebarName = (TextView) findViewById(R.id.title_bar_name);
        mMenuList = (ListView) findViewById(R.id.list_menu);
    }

    public void dataInit() {
        String menuName[] = {"New", "Pending", "On Hold", "Resolved", "Profiled", "About"};
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(this, R.layout.menu_list_item, menuName);
        mMenuList.setAdapter(myArrayAdapter);
        myArrayAdapter.notifyDataSetChanged();

        mUserNameTV.setText(BaseActivity.mUsername);

        UserdbOperator userdbOperator = new UserdbOperator(MainActivity.this);
        UserBean userBean = new UserBean();
        userBean.setUsername(BaseActivity.mUsername);
        List<UserBean> userbean_list = userdbOperator.queryUser(2, userBean);
        if (!userbean_list.isEmpty()) {
            if (userbean_list.get(0).getPic_url().equals("default")) {
                mHeadImageView.setImageResource(R.drawable.defult_img);
            } else {
                Bitmap bm = BitmapFactory.decodeFile(userbean_list.get(0).getPic_url());
                bm = CheckBitmapDegree.rotateBitmapByDegree(bm,CheckBitmapDegree.getBitmapDegree(userbean_list.get(0).getPic_url()));
                mHeadImageView.setImageBitmap(bm);

            }
            BaseActivity.mEmail = userbean_list.get(0).getEmail();
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
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(500);
            alphaAnimation.setFillAfter(true);
            mTitleBarBotton.startAnimation(alphaAnimation);
        }

        @Override
        public void onDrag(float percent) {
            ViewHelper.setAlpha(mMenuRelativeLayout, 1 - percent * 0.2f);
        }
    }

    private void menuOnClick() {
        mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (mSelectMenu != i) {
//                    adapterView.getChildAt(mSelectMenu).setBackgroundColor(Color.TRANSPARENT);
//                }
//                adapterView.getChildAt(i).setBackgroundColor(Color.parseColor("#3762c9") );

                addFragment(i);
//                mSelectMenu = i;
            }
        });
    }

    /**
     * click event  in this activity
     */
    public void mainOnClick(View view) {

        switch (view.getId()) {
            case R.id.title_bar_menu_btn:
                mDraglayout.open();
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
//                mTitlebarName.setText("Pending");

                PendingFragment pendingFragment = new PendingFragment();
                fragmentTransaction.replace(R.id.fragment, pendingFragment);
                break;
            case 2:
                mTitlebarName.setText("On Hold");

                OnHoldFragment onHoldFragment = new OnHoldFragment();
                fragmentTransaction.replace(R.id.fragment, onHoldFragment);
                break;
            case 3:
                mTitlebarName.setText("Resolved");

                ResolvedFragment resolvedFragment = new ResolvedFragment();
                fragmentTransaction.replace(R.id.fragment, resolvedFragment);
                break;
            case 4:
                mTitlebarName.setText("Profiled");

                ProfiledFragment profiledFragment = new ProfiledFragment();
                fragmentTransaction.replace(R.id.fragment, profiledFragment);
                break;
            case 5:
                mTitlebarName.setText("About");

                AboutFragment aboutFragment = new AboutFragment();
                fragmentTransaction.replace(R.id.fragment, aboutFragment);
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }

}
