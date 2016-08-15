package com.curry.bhk.bhk.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.fragment.NewFragment;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.curry.bhk.bhk.view.CircleImageView;
import com.curry.bhk.bhk.view.SlideMenu;

import java.util.List;

/**
 * Created by Curry on 2016/8/9.
 */
public class MainActivity extends BaseActivity {
    private SlideMenu mSlideMenu;
    private CircleImageView mHeadImageView;
    private TextView mUserNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();
        dataInit();
        addFragment();
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

    public void viewInit() {
        mSlideMenu = (SlideMenu) findViewById(R.id.slide_menu);
        mHeadImageView = (CircleImageView) findViewById(R.id.menu_head_img);
        mUserNameTV = (TextView) findViewById(R.id.menu_username);

    }

    public void menu_onClick(View view) {

        switch (view.getId()) {
            case R.id.title_bar_menu_btn:
                ctrlSlide();
                break;
            case R.id.menu_new:
                ctrlSlide();
                addFragment();
                break;
            case R.id.menu_pending:
                ctrlSlide();
                break;
            case R.id.menu_on_Hold:
                ctrlSlide();
                break;
            case R.id.menu_Resolved:
                ctrlSlide();
                break;
            case R.id.menu_Profiled:
                ctrlSlide();
                break;
            case R.id.menu_About:
                ctrlSlide();
                break;
            case R.id.buttonFloat:
                Toast.makeText(MainActivity.this, "!!!!!!!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void addFragment() {
        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
        NewFragment newFragment = new NewFragment();
        fragmentTransaction.replace(R.id.fragment, newFragment);
        fragmentTransaction.commit();
    }

    /**
     * ctrl slidemenu open or close
     */
    public void ctrlSlide() {
        if (mSlideMenu.isMainScreenShowing()) {
            mSlideMenu.openMenu();
        } else {
            mSlideMenu.closeMenu();
        }
    }
}
