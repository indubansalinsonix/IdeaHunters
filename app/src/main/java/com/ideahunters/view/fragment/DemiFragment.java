package com.ideahunters.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ideahunters.R;

/**
 * Created by root on 27/2/17.
 */

public class DemiFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View  view = inflater.inflate(R.layout.activity_login, container, false);
    return view;
    }
}
