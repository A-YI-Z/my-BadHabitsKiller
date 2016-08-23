package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.ImageBucketAdapter;
import com.curry.bhk.bhk.bean.ImageBucket;
import com.curry.bhk.bhk.utils.ImageFetcher;
import com.curry.bhk.bhk.utils.PublicStatic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * choose photo album
 */

public class ImageBucketChooseActivity extends BaseActivity {
    private ImageFetcher mHelper;
    private List<ImageBucket> mDataList = new ArrayList<>();
    private ListView mListView;
    private ImageBucketAdapter mAdapter;
    private int mAvailableSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_bucket_choose);

        mHelper = ImageFetcher.getInstance(getApplicationContext());
        dataInit();
        viewInit();
    }

    private void dataInit() {
        mDataList = mHelper.getImagesBucketList(false);
        mAvailableSize = getIntent().getIntExtra(
                PublicStatic.EXTRA_CAN_ADD_IMAGE_SIZE,
                PublicStatic.MAX_IMAGE_SIZE);
    }

    private void viewInit() {
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new ImageBucketAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
        TextView titleTv = (TextView) findViewById(R.id.bar_tv_title);
        titleTv.setText("Photo album");
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                selectOne(position);

                Intent intent = new Intent(ImageBucketChooseActivity.this,
                        ImageChooseActivity.class);
                intent.putExtra(PublicStatic.EXTRA_IMAGE_LIST,
                        (Serializable) mDataList.get(position).mImageList);
                intent.putExtra(PublicStatic.EXTRA_BUCKET_NAME,
                        mDataList.get(position).mBucketName);
                intent.putExtra(PublicStatic.EXTRA_CAN_ADD_IMAGE_SIZE,
                        mAvailableSize);

                startActivity(intent);

                finishActivity();
            }
        });

        ImageView cancelTv = (ImageView) findViewById(R.id.back_img);
        cancelTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageBucketChooseActivity.this, AddActivity.class));
                finishActivity();
            }
        });
    }

    private void selectOne(int position) {
        int size = mDataList.size();
        for (int i = 0; i != size; i++) {
            if (i == position) {
                mDataList.get(i).mSelected = true;
            } else {
                mDataList.get(i).mSelected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ImageBucketChooseActivity.this, AddActivity.class));
        finishActivity();
    }
}
