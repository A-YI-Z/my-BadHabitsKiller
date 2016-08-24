package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.ImageChooseAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.bean.ImageItem;
import com.curry.bhk.bhk.sqlite.EventdbOperator;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {

    private ImageView mDetailBack;
    private GridView mDetailPhoto;
    private ButtonRectangle mDetailResolveBtn;
    private TextView mDetailTitle;
    private TextView mDetailAuthor;
    private TextView mDetailTime;
    private TextView mDetailDescription;
    private TextView mTitleBarText;

    private EventdbOperator eventdbOperator;
    private EventBean eventBean = new EventBean();

    private List<ImageItem> mDetailPhotoList = new ArrayList<>();
    private ImageChooseAdapter mImageChooseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewInit();

        dataInit();

        detailAcitvityOnclick();
    }

    private void viewInit() {
        mDetailBack = (ImageView) findViewById(R.id.back_img);
        mDetailResolveBtn = (ButtonRectangle) findViewById(R.id.detail_pending_btn);

        mDetailAuthor = (TextView) findViewById(R.id.detail_author);
        mDetailDescription = (TextView) findViewById(R.id.detail_description);
        mDetailTime = (TextView) findViewById(R.id.detail_time);
        mDetailTitle = (TextView) findViewById(R.id.detail_title);
        mTitleBarText = (TextView) findViewById(R.id.bar_tv_title);

        mDetailPhoto = (GridView) findViewById(R.id.detail_photo_gridview);
    }

    private void dataInit() {
        mTitleBarText.setText("Detail");

        eventdbOperator = new EventdbOperator(DetailActivity.this);
        ;

        eventBean.setId(BaseActivity.eventItemId);
        List<EventBean> mDetailList = eventdbOperator.queryEvent(3, eventBean);

        mDetailAuthor.setText(mDetailList.get(0).getAuthor());
        mDetailDescription.setText(mDetailList.get(0).getDescription());
        mDetailTime.setText(mDetailList.get(0).getTime());
        mDetailTitle.setText(mDetailList.get(0).getTitle());

        String photoUrl[] = mDetailList.get(0).getPhotos_url().split("#");
        int length = photoUrl.length;
        for (int i = 0; i < length; i++) {
            ImageItem item = new ImageItem();
            item.sourcePath = photoUrl[i];
            Log.e(TAG, item.sourcePath);
            mDetailPhotoList.add(item);
        }

        mImageChooseAdapter = new ImageChooseAdapter(DetailActivity.this, mDetailPhotoList);
        mDetailPhoto.setAdapter(mImageChooseAdapter);

//        if (strPhotoUrl.equals("null")) {
//            mDetailPhoto.setImageResource(R.drawable.nophoto);
//        } else {
//            Bitmap bitmap = BitmapFactory.decodeFile(strPhotoUrl);
//            bitmap = CheckBitmapDegree.rotateBitmapByDegree(bitmap, CheckBitmapDegree.getBitmapDegree(strPhotoUrl));
//            mDetailPhoto.setImageBitmap(bitmap);
//        }
    }

    public void detailAcitvityOnclick() {
        mDetailBack.setOnClickListener(new OnclickEvent());
        mDetailResolveBtn.setOnClickListener(new OnclickEvent());
    }

    private class OnclickEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_img:
                    startActivity(new Intent().setClass(DetailActivity.this, MainActivity.class));
                    finishActivity();
                    break;
                case R.id.detail_pending_btn:
                    /*
                        insert the resolvedby data
                     */
                    eventBean.setResolvedby(BaseActivity.mUsername);
                    eventdbOperator.updateEvent(eventBean,0);

                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    intent.putExtra("MENUID", 1);
                    startActivity(intent);
                    finishActivity();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent().setClass(DetailActivity.this, MainActivity.class));
        finishActivity();
    }
}
