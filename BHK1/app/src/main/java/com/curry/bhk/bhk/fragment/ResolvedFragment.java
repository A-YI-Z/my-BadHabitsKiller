package com.curry.bhk.bhk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.adapter.NewListitemAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.sqlite.EventdbOperator;

import java.util.List;

/**
 * Created by Curry on 2016/8/19.
 */
public class ResolvedFragment extends Fragment {
    private List<EventBean> mEventBeanList;
    private ListView mListView;
    private TextView mNullTextView;
    private EventdbOperator eventdbOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resolved_fragment, null);
        mListView = (ListView) view.findViewById(R.id.reslovedListView);
        mNullTextView = (TextView) view.findViewById(R.id.resolved_null_textview);
        dataInit();
        return view;
    }

    private void dataInit() {
        TextView tv = (TextView) getActivity().findViewById(R.id.title_bar_name);
        tv.setText("Resolved");

        EventBean eventBean = new EventBean();
        eventBean.setStatus(3);
        eventBean.setResolvedby(BaseActivity.mUsername);

        eventdbOperator = new EventdbOperator(getActivity());
        mEventBeanList = eventdbOperator.queryEvent(4, eventBean);
        if (mEventBeanList.size() == 0) {
            mNullTextView.setVisibility(View.VISIBLE);
        }
        NewListitemAdapter newListitemAdapter = new NewListitemAdapter(getActivity(), mEventBeanList);
        mListView.setAdapter(newListitemAdapter);

        newListitemAdapter.notifyDataSetChanged();
    }
}
