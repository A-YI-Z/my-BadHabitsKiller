package com.curry.bhk.bhk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curry.bhk.bhk.R;

import java.util.List;

/**
 * Created by Curry on 2016/8/31.
 */
public class MenuAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<String> myList;
    public static  int defItem;

    public MenuAdapter(Context context, List<String> outList) {
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

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = mLayoutInflater.inflate(R.layout.menu_list_item, null);
        TextView mMenuNameItem = (TextView) view.findViewById(R.id.menu_new);
        mMenuNameItem.setText(myList.get(position).toString());


        if (defItem == position) {
            view.setBackgroundResource(R.color.colorPrimaryDark);
        } else {
            view.setBackgroundResource(android.R.color.transparent);
        }
        return view;
    }

}
