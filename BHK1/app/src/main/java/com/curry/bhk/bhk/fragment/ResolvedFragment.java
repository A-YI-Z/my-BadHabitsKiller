package com.curry.bhk.bhk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.bhk.bhk.R;

/**
 * Created by Curry on 2016/8/19.
 */
public class ResolvedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resolved_fragment, null);
        TextView tv = (TextView)getActivity().findViewById(R.id.title_bar_name);
        tv.setText("Resolved");
        return view;
    }
}
