package com.ideahunters.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.ideahunters.R;
import com.ideahunters.adapter.CommentListAdapter1;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.customwidgets.FullLengthListView.FullLengthListView;
import com.ideahunters.customwidgets.sweetalertdialog.SweetAlertDialog;
import com.ideahunters.interfaces.ChangeToogleButtonIconListener;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.interfaces.OnBackPressedListener;
import com.ideahunters.interfaces.ToolbarTitleChangeListener;
import com.ideahunters.model.Comment;
import com.ideahunters.model.IdeaslistData;
import com.ideahunters.presenter.CommentListPresenter;
import com.ideahunters.presenter.IdeaDeletePresenter;
import com.ideahunters.presenter.IdeaDetailPresenter;
import com.ideahunters.presenter.PostCommentPresenter;
import com.ideahunters.presenter.PostLikesPresenter;
import com.ideahunters.presenter.ReportIdeaAbusePresenterImpl;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 6/2/17.
 */

public class IdeaDetailFragment extends Fragment implements CommentListPresenter.CommentListPresenterListener, PostCommentPresenter.PostCommentPresenterListener, PostLikesPresenter.PostLikesPresenterListener, Constants, IdeaDeletePresenter.IdeaDeletePresenterListener, OnBackPressedListener, ReportIdeaAbusePresenterImpl.ReportIdeaAbusePresenterListener, ConnectivityReceiveListener {

    @BindView(R.id.blur_image)
    ImageView blurImage;
    @BindView(R.id.idea_image)
    ImageView ideaImage;
    @BindView(R.id.image_layout)
    RelativeLayout imageLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.sub_category)
    TextView subCategory;

    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.company_name_layout)
    LinearLayout companyNameLayout;
    @BindView(R.id.results)
    TextView results;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.comment_list)
    FullLengthListView commentList;
    @BindView(R.id.nocomment)
    TextView nocomment;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.view_lay)
    View viewLay;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.btn_comment)
    Button btnComment;
    @BindView(R.id.lay1)
    LinearLayout lay1;
    @BindView(R.id.like_count)
    TextView likeCount;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.sub_cat_layout)
    LinearLayout subCatLayout;
    @BindView(R.id.comment_loader)
    ProgressBar progressBar;
    @BindView(R.id.reportloader)
    ProgressBar reportloader;
    @BindView(R.id.report_idea)
    TextView reportIdea;
    @BindView(R.id.report_idea_layout)
    LinearLayout reportIdeaLayout;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    private int position;
    private static final float BLUR_RADIUS = 25f;
    private CommentListAdapter1 ideaAdapter;
    private CommentListPresenter commentListPresenter;
    private String sug_id;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    Bitmap bitmap, blurredBitmap;
    private Handler uiHandler;
    private ToolbarTitleChangeListener listener;
    private IdeaDetailPresenter ideaDetailsPresenter;
    static String ideaImageData;
    private Handler mHandler;
    private View view;
    private IdeaslistData ideaslistData;
    IdeaDeletePresenter ideaDeletePresenter;
    RenderScript renderScript;
ViewGroup viewGroup;
    boolean strike_through=false;
    private ChangeToogleButtonIconListener mlistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mlistener = (ChangeToogleButtonIconListener) context;
        } catch (ClassCastException castException) {
            castException.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IdeaHuntersApplication.getInstance().setConnectivityListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.idea_description, container, false);
        ButterKnife.bind(this, view);
        this.viewGroup=container;
        setHasOptionsMenu(true);
        if (!IdeaHuntersApplication.connection)
            IdeaHuntersApplication.getInstance().checkConnection(getActivity());
      //  setHasOptionsMenu(true);
        IdeaHuntersApplication.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                .setLabel("IdeaDetail Screen")
                .build());
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.banner).showImageForEmptyUri(R.drawable.banner).cacheOnDisc().cacheInMemory().build();

        getIntentData();
        mlistener.showBackButton(true);

        loader.setVisibility(View.VISIBLE);
        commentListPresenter = new CommentListPresenter(this, getActivity());
        commentListPresenter.getCommentsList(sug_id);
        if (!IdeaHuntersApplication.connection) {
            Singleton.getInstance().showSnackAlert(container, getString(R.string.dialog_message_no_internet));
        }

        return view;
    }


    private void getIntentData() {
        sug_id = getArguments().getString("sug_id");
        ideaslistData = (IdeaslistData) getArguments().getSerializable("details");
        setData();
    }

    private void setData() {
        Log.e("name", ideaslistData.getUserId());
        name.setText(ideaslistData.getUserId());
        Log.e("image", ideaslistData.getImage());
        listener = (ToolbarTitleChangeListener) getActivity();
        listener.setToolbarTitle(ideaslistData.getIdeaTitle());


        category.setText(ideaslistData.getCatId());
        if (!TextUtils.isEmpty(ideaslistData.getSubcatId())) {
            subCatLayout.setVisibility(View.VISIBLE);
            subCategory.setText(ideaslistData.getSubcatId());
        } else {
            subCatLayout.setVisibility(View.GONE);
        }

        if (TextUtils.equals(ideaslistData.getReport(), "1")) {
            reportIdea.setEnabled(false);
            reportIdea.setClickable(false);
           reportIdea.setPaintFlags(reportIdea.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        Log.e("iddd", ideaslistData.getUserId());
        if (ideaslistData.getPostedBy().equals(Singleton.getInstance().getValue(getActivity(), USER_ID))) {
            reportIdeaLayout.setVisibility(View.GONE);
        }
        description.setText(ideaslistData.getExplainIdea());
        results.setText(ideaslistData.getKeyResult());
        Log.e("like_count", ideaslistData.getLikes());
        companyName.setText(ideaslistData.getCompanyId());
        String formatted_date=Singleton.getInstance().parseDateToddMMyyyy(getActivity(),ideaslistData.getSubmittedDate());
        date.setText(formatted_date);
        likeCount.setText(ideaslistData.getLikes());
        Singleton.getInstance().mycount = ideaslistData.getLikesCount();
        if (Singleton.getInstance().mycount.equals("1")) {
            likeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
        } else {
            likeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp, 0, 0, 0);
        }
        if (!TextUtils.isEmpty(ideaslistData.getImage())) {
            Log.e("image", "exist");

            imageLoader.displayImage(ideaslistData.getImage(), ideaImage, displayImageOptions);
            imageLoader.loadImage(ideaslistData.getImage(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    // Do whatever you want with Bitmap
                    Bitmap blurred = blurRenderScript(loadedImage, 25);//second parametre is radius
                    blurImage.setImageBitmap(blurred);
                }
            });

        } else {
            Log.e("image", "not exist");

            ideaImage.setImageResource(R.drawable.banner);
            // blurImage.setImageResource(R.drawable.banner);
        /*    Drawable drawable1=getResources().getDrawable(R.drawable.banner);
            BitmapDrawable drawable = (BitmapDrawable) drawable1;
            Bitmap bitmap = drawable.getBitmap();
            Bitmap blurred = blurRenderScript(bitmap, 25);*///second parametre is radius
            blurImage.setImageResource(R.drawable.banner);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().supportInvalidateOptionsMenu();
        if (ideaslistData.getPostedBy().equals(Singleton.getInstance().getValue(getActivity(), USER_ID))) {
            setHasOptionsMenu(true);
            inflater.inflate(R.menu.menu_detail, menu);
        } else {
            setHasOptionsMenu(true);
            inflater.inflate(R.menu.main2, menu);
            MenuItem item = menu.findItem(R.id.action_search);
            item.setVisible(false);

        }



    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

//        menu.findItem(R.id.action_search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit:
                if (!IdeaHuntersApplication.connection) {
                    Singleton.getInstance().showSnackAlert(scrollview, getString(R.string.dialog_message_no_internet));
                    return false;
                }
                Fragment fragment = new IdeaSubmitFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("details", ideaslistData);
                bundle.putString("sug_id", sug_id);
                //  bundle.putString("sug_id",ideaModel.getId());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("submit").commit();

                return true;
            case R.id.action_delete:
                if (!IdeaHuntersApplication.connection) {
                    Singleton.getInstance().showSnackAlert(scrollview, getString(R.string.dialog_message_no_internet));
                    return false;
                } else {

                    ideaDeletePresenter = new IdeaDeletePresenter(this, getActivity());
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning!")
                            .setContentText("You want to Delete the Idea?")
                            .setCancelText("Cancel!")
                            .setConfirmText("Yes!")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    // reuse previous dialog instance, keep widget user state, reset them if you need
                                    sDialog.dismissWithAnimation();

                                    // or you can new a SweetAlertDialog to show
                               /* sDialog.dismiss();
                                new SweetAlertDialog(SampleActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Cancelled!")
                                        .setContentText("Your imaginary file is safe :)")
                                        .setConfirmText("OK")
                                        .show();*/
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    ideaDeletePresenter.deleteIdea(sug_id);


                                }
                            })
                            .show();

                    return true;
                }
            default:
                return false;
        }

    }

    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        try {
            renderScript = RenderScript.create(getActivity());


        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    @Override
    public void setCommentList(ArrayList<Comment> commentList1) {
        if (commentList1.size() > 0) {
            commentList.setVisibility(View.VISIBLE);
            nocomment.setVisibility(View.GONE);
            ideaAdapter = new CommentListAdapter1(getActivity(), commentList1,sug_id);
            commentList.setAdapter(ideaAdapter);
            ideaAdapter.notifyDataSetChanged();
        } else {
//                        mybuy.setVisibility(View.GONE);
            nocomment.setVisibility(View.VISIBLE);
            commentList.setVisibility(View.GONE);
        }
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onFailureResponse() {
        if (loader != null)
            loader.setVisibility(View.GONE);

    }

    @Override
    public void onSuccessfulPost() {
        comment.setText("");
//        comment.requestFocusFromTouch();
//        commentList.requestFocus();
        loader.setVisibility(View.VISIBLE);
        commentListPresenter = new CommentListPresenter(this, getActivity());
        commentListPresenter.getCommentsList(sug_id);


    }

    @Override
    public void showBlankCommentError() {
        Toast.makeText(getActivity(), "Enter Comment", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccessfulLike(String likecount, String message, String likebyme) {
      Singleton.getInstance().dismissDialog();
        Singleton.getInstance().mycount = likebyme;
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        likeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
        likeCount.setText(likecount);
    }

    @Override
    public void onUnSuccessfulLike(String message) {
        Singleton.getInstance().dismissDialog();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.idea_image, R.id.btn_comment, R.id.like_count, R.id.report_idea})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idea_image:
                Fragment fragment = new IdeaImageZoomFragment();
                Bundle bundle = new Bundle();
                bundle.putString("image", ideaslistData.getImage());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("zoom").commit();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_comment:
                if (!IdeaHuntersApplication.connection) {
                    Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_no_internet));
                }
                else {
                    PostCommentPresenter postCommentPresenter = new PostCommentPresenter(this, getActivity());
                    postCommentPresenter.postComment(sug_id, comment.getText().toString().trim());
                    comment.setText("");
                }
                break;
            case R.id.like_count:
                if (!IdeaHuntersApplication.connection) {
                    Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_no_internet));
                }
                else {
                    if (!Singleton.getInstance().mycount.equals("1")) {
                       Singleton.getInstance().showProgressDialog(getActivity());
                        PostLikesPresenter postLikesPresenter = new PostLikesPresenter(this, getActivity());
                        postLikesPresenter.postLikes(sug_id);

                    }
                }
                break;
            case R.id.report_idea:


                if (!IdeaHuntersApplication.connection) {
                    Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_no_internet));
                }
                else {
                    if (!strike_through){
                        reportloader.setVisibility(View.VISIBLE);
                        ReportIdeaAbusePresenterImpl reportIdeaAbusePresenter = new ReportIdeaAbusePresenterImpl(this, getActivity());
                        reportIdeaAbusePresenter.reportIdeaAbuse(sug_id);
                    }

                }
                break;


        }
    }


    @Override
    public void onSuccessfulDelete() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }


    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }


    @Override
    public void onSuccessfulReportIdea(String message) {
        strike_through=true;
        reportloader.setVisibility(View.GONE);
       reportIdea.setPaintFlags(reportIdea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnSuccessfulReportIdea(String message) {
        reportloader.setVisibility(View.GONE);
        strike_through=false;
        reportIdea.setPaintFlags(reportIdea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        getConnection(isConnected);
        IdeaHuntersApplication.connection = isConnected;
    }


    public void getConnection(Boolean isConnected) {
        if (isConnected) {
            Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_internet));

        }
        else {
            Singleton.getInstance().showSnackAlert(viewGroup,getString(R.string.dialog_message_no_internet));
        }


    }

}
