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
public class UserNameAdapter extends BaseAdapter {
    ViewHolder holder;
    private List<String> mDataList;
    private LayoutInflater mLayoutInflater;

    public UserNameAdapter(Context context, List<String> outList) {
        this.mDataList = outList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.popwindow_item, null);
            holder.mUsersTv = (TextView) convertView.findViewById(R.id.pop_username);
//            holder. mDeleteUserBtn = (ImageButton) convertView.findViewById(R.id.pop_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mUsersTv.setText(mDataList.get(position));


        return convertView;
    }

    class ViewHolder {
        private TextView mUsersTv;
//        private ImageButton mDeleteUserBtn;
    }

//    private void deleteOrChooseUser() {
//        holder.mUsersTv.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String mUsersName = holder.mUsersTv.getText().toString();
//                login_et_username.setText(mUsersName);
//                mPopView.dismiss();
//                matchHead();
//            }
//        });
//        holder.mDeleteUserBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserBean userBean = new UserBean();
//                UserdbOperator userdbOperator = new UserdbOperator();
//                userBean.setEmail(mUsersTv.getText().toString());
//                userBean.setStatus(0);
//                userdbOperator.updateUser(userBean, 3);
//
//                userNameAdapter.notifyDataSetChanged();
//                if (myUsersList.isEmpty()) {
//                    mPopView.dismiss();
//                }
//            }
//        });
//    }
}
