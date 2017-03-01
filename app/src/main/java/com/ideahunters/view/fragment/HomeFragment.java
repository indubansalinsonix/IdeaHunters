package com.ideahunters.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ideahunters.R;
import com.ideahunters.adapter.ViewPagerAdapter;
import com.ideahunters.interfaces.ChangeToogleButtonIconListener;
import com.ideahunters.interfaces.OnBackPressedListener;
import com.ideahunters.interfaces.ToolbarTitleChangeListener;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 24/2/17.
 */

public class HomeFragment extends Fragment implements Constants,OnBackPressedListener{
Context context;
ChangeToogleButtonIconListener listener;
    private View view;
    private ToolbarTitleChangeListener mlistener;
    private ViewPagerAdapter adapter;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabanim_tabs)
    TabLayout tabLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
               ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mlistener.setToolbarTitle(getString(R.string.app_name));
        listener.showBackButton(false);

       setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.d("tab2", String.valueOf(tab.getPosition()));
                Singleton.getInstance().selectTab=tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                       //Singleton.getInstance().selectTab=0;
//                        Singleton.getInstance().myList=false;
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new IdeaListFragment("0")).commit();

                        // Toast.makeText(MainActivity.this,"one",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                    //    Singleton.getInstance().selectTab=1;
//                        Singleton.getInstance().myList=false;
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new IdeaListFragment("0")).commit();

                        //         adapter.notifyDataSetChanged();
                       // Toast.makeText(MainActivity.this,"Two",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                       // Singleton.getInstance().selectTab=2;
//                        Singleton.getInstance().myList=false;
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new IdeaListFragment("0")).commit();

                        //  Toast.makeText(MainActivity.this,"Three",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                     //   Singleton.getInstance().selectTab=3;
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("tab23", String.valueOf(tab.getPosition()));
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("tab24", String.valueOf(tab.getPosition()));
            }
        });

        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        listener= (ChangeToogleButtonIconListener) context;
        mlistener = (ToolbarTitleChangeListener) context;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new IdeaListFragment(0),getResources().getString(R.string.allIdeas));
        adapter.addFrag(new IdeaListFragment(1), getResources().getString(R.string.my_ideas));
       adapter.addFrag(new IdeaListFragment(2), getResources().getString(R.string.mostLiked));
       adapter.addFrag(new IdeaListFragment(3), getResources().getString(R.string.likeByMe));
       // adapter.addFrag(new IdeaListFragment(), getResources().getString(R.string.mostLiked));
        viewPager.setAdapter(adapter);
        Log.d("Singleton",""+Singleton.getInstance().selectTab);
        viewPager.setCurrentItem(Singleton.getInstance().selectTab);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab)
    public void onClick() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IdeaSubmitFragment()).addToBackStack("submit").commit();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main2, menu);
    }

    @Override
    public void onBackPressed() {
        viewPager.setCurrentItem(0);
        Singleton.getInstance().selectTab=0;
    }
}
