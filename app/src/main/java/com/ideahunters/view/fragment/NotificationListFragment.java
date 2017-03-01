package com.ideahunters.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ideahunters.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 1/3/17.
 */

public class NotificationListFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private View view;
    private ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        this.viewGroup = container;
        ButterKnife.bind(this, view);


        return view;

    }
}
