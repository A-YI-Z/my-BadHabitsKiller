package com.curry.bhk.bhk.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.curry.bhk.bhk.fragment.AboutFragment;
import com.curry.bhk.bhk.fragment.NewFragment;
import com.curry.bhk.bhk.fragment.OnHoldFragment;
import com.curry.bhk.bhk.fragment.PendingFragment;
import com.curry.bhk.bhk.fragment.ProfiledFragment;
import com.curry.bhk.bhk.fragment.ResolvedFragment;
import com.curry.bhk.bhk.utils.CheckBitmapDegree;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.curry.bhk.bhk.view.CircleImageView;
import com.curry.bhk.bhk.view.DragLayout;
import com.curry.bhk.bhk.view.MyRelativeLayout;
import com.nineoldandroids.view.ViewHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();

        mDraglayout.setDragListener(new MyDrag());

        dataInit();
        Log.e(TAG, getIntent().getIntExtra("MENUID", 0) + "");
        addFragment(getIntent().getIntExtra("MENUID", 0));

        menuOnClick();


        IntentFilter filter = new IntentFilter();
        filter.addAction("CHANGE");
        registerReceiver(receiver, filter);
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
        String menuName[] = {"New", "Pending", "On Hold", "Resolved", "Profiled", "About", "Sign Out"};
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(this, R.layout.menu_list_item, menuName);
        mMenuList.setAdapter(myArrayAdapter);
        myArrayAdapter.notifyDataSetChanged();

        mUserNameTV.setText(BaseActivity.mUsername);

//        UserdbOperator userdbOperator = new UserdbOperator(MainActivity.this);
//        UserBean userBean = new UserBean();
//        userBean.setUsername(BaseActivity.mUsername);
//        List<UserBean> userbean_list = userdbOperator.queryUser(2, userBean);
//        if (!userbean_list.isEmpty()) {
//            if (BaseActivity.mHeadUrl.equals("default")) {
//                mHeadImageView.setImageResource(R.drawable.defult_img);
//            } else {
//                Bitmap bm = BitmapFactory.decodeFile(BaseActivity.mHeadUrl);
//                bm = CheckBitmapDegree.rotateBitmapByDegree(bm, CheckBitmapDegree.getBitmapDegree(BaseActivity.mHeadUrl));
//                mHeadImageView.setImageBitmap(bm);
//            }
//        }

        if (!BaseActivity.mHeadUrl.equals("")) {
            if (BaseActivity.mHeadUrl.equals("default")) {
                mHeadImageView.setImageResource(R.drawable.defult_img);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(BaseActivity.mHeadUrl);
                bitmap = CheckBitmapDegree.rotateBitmapByDegree(bitmap, CheckBitmapDegree.getBitmapDegree(BaseActivity.mHeadUrl));
                mHeadImageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * Slide layout  ctrl
     */
    public class MyDrag implements DragLayout.DragListener {

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // login out
                if (position == 6) {

                    SharedPreferences.Editor edit = getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();
                    edit.putBoolean(PublicStatic.SHAREDPREFERENCES_CHECKBOX, false);
                    edit.commit();

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finishActivity();
                } else {
                    addFragment(position);
                }
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
//                mTitlebarName.setText("On Hold");

                OnHoldFragment onHoldFragment = new OnHoldFragment();
                fragmentTransaction.replace(R.id.fragment, onHoldFragment);
                break;
            case 3:
//                mTitlebarName.setText("Resolved");

                ResolvedFragment resolvedFragment = new ResolvedFragment();
                fragmentTransaction.replace(R.id.fragment, resolvedFragment);
                break;
            case 4:
                mTitlebarName.setText("Profiled");
//
                ProfiledFragment profiledFragment = new ProfiledFragment();
                fragmentTransaction.replace(R.id.fragment, profiledFragment);
//                startActivity(new Intent(MainActivity.this, ProfiledActivity.class));
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

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CHANGE")) {
               dataInit();
            }
        }
    };

}
