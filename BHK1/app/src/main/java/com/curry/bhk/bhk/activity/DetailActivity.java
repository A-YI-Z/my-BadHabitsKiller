package com.curry.bhk.bhk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.adapter.DetailPictureAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.bean.ImageItem;
import com.curry.bhk.bhk.sqlite.EventdbOperator;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.gc.materialdesign.views.ButtonRectangle;

import java.io.Serializable;
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
    private RelativeLayout mPictureBackground;
    private TextView mBarTitle;

    private EventdbOperator eventdbOperator;
    private EventBean eventBean = new EventBean();

    private List<ImageItem> mDetailPhotoList = new ArrayList<>();
    private DetailPictureAdapter mDetailPictureAdapter;

    private int status = 0;

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
        mPictureBackground = (RelativeLayout) findViewById(R.id.detail_picture_bg);
        mDetailAuthor = (TextView) findViewById(R.id.detail_author);
        mDetailDescription = (TextView) findViewById(R.id.detail_description);
        mDetailTime = (TextView) findViewById(R.id.detail_time);
        mDetailTitle = (TextView) findViewById(R.id.detail_title);
        mBarTitle = (TextView) findViewById(R.id.bar_tv_title);

        mDetailPhoto = (GridView) findViewById(R.id.detail_photo_gridview);
    }

    private void dataInit() {
        mBarTitle.setText("Detail");
        eventdbOperator = new EventdbOperator(DetailActivity.this);

        eventBean.setId(BaseActivity.eventItemId);
        List<EventBean> mDetailList = eventdbOperator.queryEvent(3, eventBean);

        mDetailAuthor.setText(mDetailList.get(0).getAuthor());
        mDetailDescription.setText(mDetailList.get(0).getDescription());
        mDetailTime.setText(mDetailList.get(0).getTime());
        mDetailTitle.setText(mDetailList.get(0).getTitle());
        status = mDetailList.get(0).getStatus();

        String mPictureUrl = mDetailList.get(0).getPhotos_url();
        if (mPictureUrl != null && !mPictureUrl.equals("")) {
            String photoUrl[] = mPictureUrl.split("#");
            int length = photoUrl.length;
            for (int i = 0; i < length; i++) {
                ImageItem item = new ImageItem();
                item.sourcePath = photoUrl[i];
                mDetailPhotoList.add(item);
            }

            mDetailPictureAdapter = new DetailPictureAdapter(DetailActivity.this, mDetailPhotoList);
            mDetailPhoto.setAdapter(mDetailPictureAdapter);
        } else {
            mPictureBackground.setBackgroundResource(R.drawable.nophoto);
        }


    }

    public void detailAcitvityOnclick() {
        mDetailBack.setOnClickListener(new OnclickEvent());
        if (status != 0) {
//            mDetailResolveBtn.setAlpha(0.5f);
            mDetailResolveBtn.setVisibility(View.GONE);
        } else {
            mDetailResolveBtn.setOnClickListener(new OnclickEvent());
        }
        mDetailPhoto.setOnItemClickListener(new GridViewOnItemClick());
    }

    private class GridViewOnItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(DetailActivity.this, ImageZoomActivity.class);
            intent.putExtra(PublicStatic.EXTRA_IMAGE_LIST, (Serializable) mDetailPhotoList);
            intent.putExtra(PublicStatic.EXTRA_CURRENT_IMG_POSITION, position);
            intent.putExtra("FROM_DETAIL_ACTIVITY", true);
            startActivity(intent);
        }
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
                    eventBean.setResolvedby(BaseActivity.mEmail);
                    eventBean.setStatus(1);
                    eventdbOperator.updateEvent(eventBean, 0);

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
