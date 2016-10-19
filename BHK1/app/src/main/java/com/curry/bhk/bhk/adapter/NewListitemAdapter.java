package com.curry.bhk.bhk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.EventBean;

import java.util.List;

/**
 * Created by Curry on 2016/8/9.
 */
public class NewListitemAdapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    List<EventBean> myList;

    public NewListitemAdapter(Context context, List<EventBean> outList) {
        mLayoutInflater = LayoutInflater.from(context);
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
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.list_item, null);
            mViewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            mViewHolder.tv_author = (TextView) view.findViewById(R.id.tv_author);
            mViewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            mViewHolder.tv_description = (TextView) view.findViewById(R.id.tv_description);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        mViewHolder.tv_title.setText(myList.get(i).getTitle());
        mViewHolder.tv_author.setText(myList.get(i).getAuthor());
        mViewHolder.tv_time.setText(myList.get(i).getTime());
        mViewHolder.tv_description.setText(myList.get(i).getDescription());
        return view;
    }

    class ViewHolder {
        private TextView tv_title;
        private TextView tv_author;
        private TextView tv_time;
        private TextView tv_description;
    }
}
