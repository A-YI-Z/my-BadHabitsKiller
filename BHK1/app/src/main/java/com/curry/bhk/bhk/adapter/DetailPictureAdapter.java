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
        convertView = View.inflate(mContext, R.layout.item_publish, null);
        ImageView imageIv = (ImageView) convertView.findViewById(R.id.item_grid_image);

        Bitmap bm = BitmapFactory.decodeFile(mDataList.get(position).sourcePath);
        bm = CheckBitmapDegree.rotateBitmapByDegree(bm,CheckBitmapDegree.getBitmapDegree(mDataList.get(position).sourcePath));
        imageIv.setImageBitmap(bm);

//        final ImageItem item = mDataList.get(position);
//        ImageDisplayer.getInstance(mContext).displayBmp(imageIv, item.thumbnailPath, item.sourcePath);

        return convertView;
    }

}
