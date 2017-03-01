package com.ideahunters.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.analytics.HitBuilders;
import com.ideahunters.R;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.customwidgets.RoundedImageView.TouchImageView;
import com.ideahunters.interfaces.ChangeToogleButtonIconListener;
import com.ideahunters.interfaces.ToolbarTitleChangeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IdeaImageZoomFragment extends Fragment  {


    @BindView(R.id.imageview)
    TouchImageView image;
    private String branch_image;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ToolbarTitleChangeListener listener;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ChangeToogleButtonIconListener mlistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mlistener=(ChangeToogleButtonIconListener)context;
        } catch (ClassCastException castException) {
            castException.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imagezoom, container, false);
        ButterKnife.bind(this, view);
        IdeaHuntersApplication.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                .setLabel("IdeaImageZoom Screen")
                .build());
        setHasOptionsMenu(true);
        mlistener.showBackButton(true);
       // listener.setToolbarTitle(getString(R.string.app_name));

        Bundle bundle = getArguments();
        if (bundle != null) {
            // position = getArguments().getInt("position", 1234);

            branch_image = getArguments().getString("image");
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.banner).showImageForEmptyUri(R.drawable.banner).cacheOnDisc().cacheInMemory()
                  .build();
            imageLoader.displayImage(branch_image, image, displayImageOptions);

            // Picasso.with(getActivity()).load(branch_image).into(image);

        }

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
