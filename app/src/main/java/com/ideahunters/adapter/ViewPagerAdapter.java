package com.ideahunters.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ideahunters.view.fragment.DemiFragment;
import com.ideahunters.view.fragment.IdeaListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 24/2/17.
 */

public  class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);

    }
    @Override
    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        if (position==0){
//           fragment= new IdeaListFragment("0");
//        }
//        else {
//            fragment= new IdeaListFragment("1");
//        }
        return mFragmentList.get(position) ;
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void addFrag(Fragment fragment, String title) {

      //  Fragment fragment1=new IdeaListFragment(s);
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
