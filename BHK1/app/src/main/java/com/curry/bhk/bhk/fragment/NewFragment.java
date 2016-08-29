package com.curry.bhk.bhk.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.activity.DetailActivity;
import com.curry.bhk.bhk.adapter.NewListitemAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.sqlite.EventdbOperator;
import com.curry.bhk.bhk.sqlite.UserdbOperator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Curry on 2016/8/12.
 */
public class NewFragment extends Fragment {

    private ListView mListView;
    private TextView mNullTextView;
    private List<EventBean> mEventBeanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment, null);
        mNullTextView = (TextView) view.findViewById(R.id.new_null_textview);
        mListView = (ListView) view.findViewById(R.id.listView);
        dataInit();

        itemOnclick();
        return view;
    }

    private void dataInit() {

        EventdbOperator eventdbOperator = new EventdbOperator(getActivity());
        mEventBeanList = eventdbOperator.queryEvent(0, null);
        if (mEventBeanList.size() == 0) {
            mNullTextView.setVisibility(View.VISIBLE);
        } else {
            NewListitemAdapter newListitemAdapter = new NewListitemAdapter(getActivity(), mEventBeanList);
            mListView.setAdapter(newListitemAdapter);

            newListitemAdapter.notifyDataSetChanged();
        }
    }

    private void itemOnclick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BaseActivity.eventItemId = mEventBeanList.get(i).getId();
//                Log.e("Curry", mItemId + "");

                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra("ID", mItemId);
                startActivity(intent);

                getActivity().finish();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                popUpDialog();
                return true;
            }
        });
    }

    private void popUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Do you want to mass E-mail?");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendEmail();
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    private void sendEmail() {
        EventdbOperator eventdbOperator = new EventdbOperator(getActivity());
        EventBean eventBean = new EventBean();
        eventBean.setId(BaseActivity.eventItemId);
        List<EventBean> mDetailList = eventdbOperator.queryEvent(3, eventBean);
        EventBean getEventBean = mDetailList.get(0);


        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//        String[] tos = {BaseActivity.mEmail};
        ArrayList<String> arrayList = new UserdbOperator(getActivity()).getAllEmail();
        String[] allEmail = arrayList.toArray(new String[arrayList.size()]);

        intent.putExtra(Intent.EXTRA_EMAIL, allEmail);
//        intent.putExtra(Intent.EXTRA_CC, ccs);
        intent.putExtra(Intent.EXTRA_TEXT, getEventBean.getDescription());
        intent.putExtra(Intent.EXTRA_SUBJECT, getEventBean.getDescription());

        ArrayList imageUris = new ArrayList();

        if (getEventBean.getPhotos_url() != null) {
            String photoUrl[] = getEventBean.getPhotos_url().split("#");
            int length = photoUrl.length;
            for (int i = 0; i < length; i++) {
                File file = new File(photoUrl[i]);
                imageUris.add(Uri.fromFile(file));
            }

            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        }

        intent.setType("image/*");
        intent.setType("message/rfc882");
        Intent.createChooser(intent, "Choose Email Client.");
        startActivity(intent);
    }

}
