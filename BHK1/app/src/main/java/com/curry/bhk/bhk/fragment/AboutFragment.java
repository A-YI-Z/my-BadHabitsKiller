package com.curry.bhk.bhk.fragment;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.bhk.bhk.R;

/**
 * Created by Curry on 2016/8/19.
 */
public class AboutFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, null);

        TextView mVersionTv = (TextView) view.findViewById(R.id.tv_about_version);

        int versionCode = 0;
        try {
            versionCode = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mVersionTv.setText(getResources().getString(R.string.version_code) + versionCode);
        return view;
    }
}
