package com.curry.bhk.bhk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.bean.NewBean;

import java.util.List;

/**
 * Created by Curry on 2016/8/9.
 */
public class NewListitemAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<NewBean> myList;

    public NewListitemAdapter(Context context, List<NewBean> outList) {
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
        view = layoutInflater.inflate(R.layout.list_item, null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_author = (TextView) view.findViewById(R.id.tv_author);
        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);

        myList.get(i).setAuthor(tv_name.getText().toString());
        myList.get(i).setAuthor(tv_author.getText().toString());
        myList.get(i).setAuthor(tv_date.getText().toString());
        return view;
    }
}
