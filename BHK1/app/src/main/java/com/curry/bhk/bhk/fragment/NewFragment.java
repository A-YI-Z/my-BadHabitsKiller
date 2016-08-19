package com.curry.bhk.bhk.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.activity.DetailActivity;
import com.curry.bhk.bhk.adapter.NewListitemAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.sqlite.EventdbOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Curry on 2016/8/12.
 */
public class NewFragment extends Fragment {

    private ListView mListView;
    private List<EventBean> mEventBeanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment, null);
        mListView = (ListView) view.findViewById(R.id.listView);
        dataInit();

        itemOnclick();
        return view;
    }

    private void dataInit() {
        EventdbOperator eventdbOperator = new EventdbOperator(getActivity());
        mEventBeanList = eventdbOperator.queryEvent(0, null);

        NewListitemAdapter newListitemAdapter = new NewListitemAdapter(getActivity(), mEventBeanList);
        mListView.setAdapter(newListitemAdapter);

        newListitemAdapter.notifyDataSetChanged();
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
    }
}
