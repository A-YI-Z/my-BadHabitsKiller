package com.curry.bhk.bhk.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.ImageItem;
import com.curry.bhk.bhk.utils.ImageDisplayer;
import com.curry.bhk.bhk.utils.PublicStatic;

import java.util.ArrayList;
import java.util.List;

public class ImageZoomActivity extends BaseActivity {

    private ViewPager mViewPager;
    private Button mPhotoDel;
    private RelativeLayout photo_relativeLayout;

    private MyPageAdapter mPageAdapter;
    private int mCurrentPosition;
    private List<ImageItem> mZoomDataList = new ArrayList<>();

    private boolean mFromDetail = true;//if is from detailActivity

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_zoom);

        photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        photo_relativeLayout.setBackgroundColor(0x70000000);
        mPhotoDel = (Button) findViewById(R.id.photo_bt_del);
        Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);

        dataInit();

        photo_bt_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        mPhotoDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mZoomDataList.size() == 1) {
                    removeImgs();
                    finish();
                } else {
                    removeImg(mCurrentPosition);
                    mViewPager.removeAllViews();
                    mPageAdapter.removeView(mCurrentPosition);
                    mPageAdapter.notifyDataSetChanged();
                }
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(pageChangeListener);

        mPageAdapter = new MyPageAdapter(mZoomDataList);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
    }

    private void dataInit() {
        mCurrentPosition = getIntent().getIntExtra(PublicStatic.EXTRA_CURRENT_IMG_POSITION, 0);

        mFromDetail = getIntent().getBooleanExtra("FROM_DETAIL_ACTIVITY", false);
        if (mFromDetail) {
            mZoomDataList = (ArrayList) getIntent().getSerializableExtra(PublicStatic.EXTRA_IMAGE_LIST);
            mPhotoDel.setVisibility(View.GONE);
        } else {
            mZoomDataList = AddActivity.mDataList;
            mPhotoDel.setVisibility(View.VISIBLE);
        }

    }

    private void removeImgs() {
        mZoomDataList.clear();
    }

    private void removeImg(int location) {
        if (location + 1 <= mZoomDataList.size()) {
            mZoomDataList.remove(location);
        }
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            mCurrentPosition = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    class MyPageAdapter extends PagerAdapter {
        private List<ImageItem> dataList = new ArrayList<ImageItem>();
        private ArrayList<ImageView> mViews = new ArrayList<ImageView>();

        public MyPageAdapter(List<ImageItem> dataList) {
            this.dataList = dataList;
            int size = dataList.size();
            for (int i = 0; i != size; i++) {
                ImageView iv = new ImageView(ImageZoomActivity.this);
                ImageDisplayer.getInstance(ImageZoomActivity.this).displayBmp(
                        iv, null, dataList.get(i).sourcePath, false);
                iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                mViews.add(iv);
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public Object instantiateItem(View arg0, int arg1) {
            ImageView iv = mViews.get(arg1);
            ((ViewPager) arg0).addView(iv);
            return iv;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            if (mViews.size() >= arg1 + 1) {
                ((ViewPager) arg0).removeView(mViews.get(arg1));
            }
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        public void removeView(int position) {
            if (position + 1 <= mViews.size()) {
                mViews.remove(position);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }
}