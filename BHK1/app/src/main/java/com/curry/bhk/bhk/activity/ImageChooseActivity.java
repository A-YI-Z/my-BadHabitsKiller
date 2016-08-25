package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.ImageGridAdapter;
import com.curry.bhk.bhk.bean.ImageItem;
import com.curry.bhk.bhk.utils.PublicStatic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * choose photo
 */
public class ImageChooseActivity extends BaseActivity {
    private List<ImageItem> mChooseDataList = new ArrayList<>();
    private String mBucketName;
    private int mAvailableSize;
    private GridView mGridView;
    private TextView mBucketNameTv;
    private ImageView mBackIv;
    private ImageGridAdapter mAdapter;
    private Button mFinishBtn;
    private HashMap<String, ImageItem> mSelectedImgs = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_choose);

        mChooseDataList = (List<ImageItem>) getIntent().getSerializableExtra(PublicStatic.EXTRA_IMAGE_LIST);

        if (TextUtils.isEmpty(mBucketName)) {
            mBucketName = "Please choose";
        }
        mAvailableSize = getIntent().getIntExtra(
                PublicStatic.EXTRA_CAN_ADD_IMAGE_SIZE,
                PublicStatic.MAX_IMAGE_SIZE);

        initView();

        initListener();

    }

    private void initView() {
        mBucketNameTv = (TextView) findViewById(R.id.bar_tv_title);
        mBucketNameTv.setText(mBucketName);

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mChooseDataList);
        mGridView.setAdapter(mAdapter);
        mFinishBtn = (Button) findViewById(R.id.finish_btn);

        mBackIv = (ImageView) findViewById(R.id.back_img);

        mFinishBtn.setText("Finish" + "(" + mSelectedImgs.size() + "/" + mAvailableSize + ")");
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mFinishBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
//                Intent intent = new Intent(ImageChooseActivity.this, AddActivity.class);
//                intent.putExtra(PublicStatic.EXTRA_IMAGE_LIST, (Serializable) new ArrayList<>(mSelectedImgs.values()));
//                startActivity(intent);
//                finishActivity();


                Intent intent = new Intent(ImageChooseActivity.this, ImageBucketChooseActivity.class);
                intent.putExtra(PublicStatic.EXTRA_IMAGE_LIST, (Serializable) new ArrayList<>(mSelectedImgs.values()));

                setResult(RESULT_OK, intent);

                finishActivity();
            }
        });

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ImageItem item = mChooseDataList.get(position);
                if (item.isSelected) {
                    item.isSelected = false;
                    mSelectedImgs.remove(item.imageId);
                } else {
                    if (mSelectedImgs.size() >= mAvailableSize) {
                        toastSomething(ImageChooseActivity.this, "You can only choose four pictures.");
                        return;
                    }
                    item.isSelected = true;
                    mSelectedImgs.put(item.imageId, item);
                }

                mFinishBtn.setText("Finish" + "(" + mSelectedImgs.size() + "/" + mAvailableSize + ")");
                mAdapter.notifyDataSetChanged();
            }

        });

        mBackIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageChooseActivity.this, ImageBucketChooseActivity.class));
                finishActivity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ImageChooseActivity.this, ImageBucketChooseActivity.class));
        finishActivity();
    }
}