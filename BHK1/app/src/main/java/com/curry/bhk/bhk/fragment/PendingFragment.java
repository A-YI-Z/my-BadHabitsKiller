package com.curry.bhk.bhk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.adapter.NewListitemAdapter;
import com.curry.bhk.bhk.bean.EventBean;
import com.curry.bhk.bhk.sqlite.EventdbOperator;

import java.util.List;

/**
 * Created by Curry on 2016/8/17.
 */
public class PendingFragment extends Fragment {
    private List<EventBean> mEventBeanList;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panding_fragment, null);
        mListView = (ListView) view.findViewById(R.id.pendingListView);

        dataInit();
        return view;
    }

    public void dataInit() {
        EventBean eventBean = new EventBean();
//        eventBean.setResolvedby(BaseActivity.mUsername);
        eventBean.setId(BaseActivity.eventItemId);

        EventdbOperator eventdbOperator = new EventdbOperator(getActivity());
        mEventBeanList = eventdbOperator.queryEvent(3, eventBean);

        NewListitemAdapter newListitemAdapter = new NewListitemAdapter(getActivity(), mEventBeanList);
        mListView.setAdapter(newListitemAdapter);

        newListitemAdapter.notifyDataSetChanged();
    }
}
