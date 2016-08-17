package com.curry.bhk.bhk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.bhk.bhk.R;

/**
 * Created by Curry on 2016/8/17.
 */
public class PendingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panding_fragment, null);
        return view;
    }
}
