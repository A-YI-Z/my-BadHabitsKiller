package com.curry.bhk.bhk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.ImageItem;
import com.curry.bhk.bhk.utils.CheckBitmapDegree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Curry on 2016/8/24.
 */
public class DetailPictureAdapter extends BaseAdapter {

    private List<ImageItem> mDataList = new ArrayList<>();
    private Context mContext;

    public DetailPictureAdapter(Context context, List<ImageItem> outList) {
        this.mContext = context;
        this.mDataList = outList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_detail, null);
            mViewHolder.mPictureView = (ImageView) convertView.findViewById(R.id.detail_item_grid_image);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Bitmap bm = BitmapFactory.decodeFile(mDataList.get(position).sourcePath);
        bm = CheckBitmapDegree.rotateBitmapByDegree(bm, CheckBitmapDegree.getBitmapDegree(mDataList.get(position).sourcePath));
        mViewHolder.mPictureView.setImageBitmap(bm);

        return convertView;
    }

    static class ViewHolder {
        public ImageView mPictureView;
    }
}
