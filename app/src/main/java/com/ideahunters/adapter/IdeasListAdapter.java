package com.ideahunters.adapter;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ideahunters.R;
import com.ideahunters.customwidgets.RoundedImageView.RoundedImageView;
import com.ideahunters.model.IdeaslistData;
import com.ideahunters.utils.Singleton;
import com.ideahunters.view.fragment.IdeaDetailFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 30/1/17.
 */
public class IdeasListAdapter extends RecyclerView.Adapter<IdeasListAdapter.ViewHolder> {

    @BindView(R.id.idea_image)
    RoundedImageView profileImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.desc)
    TextView desc;
    private FragmentActivity mContext;
    private ArrayList<IdeaslistData> idealist;
    private ArrayList<IdeaslistData> filteridealist;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;


    public IdeasListAdapter(FragmentActivity context, ArrayList<IdeaslistData> list) {
        idealist = list;
        filteridealist=list;
        mContext = context;

    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       Log.e("size", "hoiiiii");
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_idea_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final IdeaslistData model = idealist.get(position);

       holder.bind(model);



    }


    @Override
    public int getItemCount() {
        return idealist.size();
    }

    public void setFilter(ArrayList<IdeaslistData> idealist1) {
        Log.e("listdata", String.valueOf(idealist1));
       idealist = new ArrayList<>();
        idealist.addAll(idealist1);
        notifyDataSetChanged();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.idea_image)
        RoundedImageView profileImg;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bind(final IdeaslistData ideaModel) {
            if (!TextUtils.isEmpty(ideaModel.getImage())) {
                imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
                displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.idea).showImageForEmptyUri(R.drawable.idea).cacheOnDisc().cacheInMemory()
                        .postProcessor(new BitmapProcessor() {
                            @Override
                            public Bitmap process(Bitmap bmp) {
                                return Bitmap.createScaledBitmap(bmp, 60, 60, false);
                            }
                        }).build();
                imageLoader.displayImage(ideaModel.getImage(),profileImg,displayImageOptions);
                Log.e("idea_adaptrer_name",ideaModel.getIdeaTitle());

            }

           title.setText(ideaModel.getIdeaTitle());
           desc.setText(ideaModel.getExplainIdea());
           mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("idea_adaptrer_name",ideaModel.getIdeaTitle());
                    Log.e("idea_adaptrer_image",ideaModel.getImage());
                    Fragment fragment=new IdeaDetailFragment();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("details", ideaModel);
                    bundle.putString("sug_id",ideaModel.getId());
                    fragment.setArguments(bundle);
                    mContext.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                }
            });
          //  Singleton.getInstance().setAnimation(mContext,mainLayout,R.anim.item_animation);


        }


    }

}

