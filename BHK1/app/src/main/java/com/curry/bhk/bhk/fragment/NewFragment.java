package com.curry.bhk.bhk.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.bhk.bhk.R;
import com.gc.materialdesign.views.ButtonFloat;

/**
 * Created by Curry on 2016/8/12.
 */
public class NewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment, null);
        ButtonFloat buttonFloat = (ButtonFloat) view.findViewById(R.id.buttonFloat);

        return view;
    }

}
