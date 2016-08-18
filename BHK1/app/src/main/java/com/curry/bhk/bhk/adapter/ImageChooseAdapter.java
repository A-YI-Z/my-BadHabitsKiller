package com.curry.bhk.bhk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.curry.bhk.bhk.bean.ImageItem;

import java.util.List;

/**
 * Created by Curry on 2016/8/18.
 */
public class ImageChooseAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<ImageItem> myList;

    public ImageChooseAdapter(Context context, List<ImageItem> outList) {
        layoutInflater = LayoutInflater.from(context);
        myList = outList;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mViewHolder;

        return view;
    }

    static class ViewHolder {

    }
}
