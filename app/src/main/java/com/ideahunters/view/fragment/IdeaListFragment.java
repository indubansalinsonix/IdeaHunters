package com.ideahunters.view.fragment;

import android.app.MediaRouteButton;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.ideahunters.R;
import com.ideahunters.adapter.IdeasListAdapter;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.interfaces.ChangeToogleButtonIconListener;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.interfaces.ToolbarTitleChangeListener;
import com.ideahunters.model.IdeasListModel;
import com.ideahunters.model.IdeaslistData;
import com.ideahunters.presenter.IdeasListPresenterImpl;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.DividerItemDecoration;
import com.ideahunters.utils.Singleton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 2/2/17.
 */

public class IdeaListFragment extends Fragment implements IdeasListPresenterImpl.IdeasListPresenterListener, Constants, SearchView.OnQueryTextListener, ConnectivityReceiveListener {


    @BindView(R.id.mRecycleIdeaList)
    RecyclerView mRecycleIdeaList;
    @BindView(R.id.nocomment)
    TextView noComment;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;
    private IdeasListAdapter myRecyclerViewAdapter;
    private IdeasListPresenterImpl ideasListPresenterImpl;
    private ToolbarTitleChangeListener listener;
    private ChangeToogleButtonIconListener mlistener;
  @BindView(R.id.loader)
   ProgressBar bar;
    View view;
    boolean showProgress = false;
    ViewGroup viewGroup;
   int selectedTab=0;

    public IdeaListFragment(int i) {

        selectedTab=i;

    }
    // String tabCheck;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ToolbarTitleChangeListener) context;
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
        view = inflater.inflate(R.layout.fragment_ideas_list, container, false);
        this.viewGroup = container;
        ButterKnife.bind(this, view);

        if (!IdeaHuntersApplication.connection)
            IdeaHuntersApplication.getInstance().checkConnection(getActivity());
        setHasOptionsMenu(true);
        IdeaHuntersApplication.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                .setLabel("IdeaList Screen")
                .build());
        getBundleData();
        ideasListPresenterImpl = new IdeasListPresenterImpl(this, getActivity());

        filterList( );
      /*  if (IdeaHuntersApplication.connection) {
            if (TextUtils.isEmpty(Singleton.getInstance().getValue(getActivity(), IDEA_LIST))) {
                showProgress = true;
                ideasListPresenterImpl.getIdeasList(true);

            } else {
                String data = Singleton.getInstance().getValue(getActivity(), IDEA_LIST);
                Log.e("plain", data);
                IdeasListModel mResponseObject = new Gson().fromJson(data, IdeasListModel.class);
                showProgress = false;
                ideasListPresenterImpl.setData(mResponseObject);
                ideasListPresenterImpl.getIdeasList(true);


            }

        }*/
        setRecyclerView(view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!IdeaHuntersApplication.connection) {
                    Singleton.getInstance().showSnackAlert(mainLayout, getString(R.string.dialog_message_no_internet));
                    // Toast.makeText(getActivity(),getString(R.string.dialog_message_no_internet),Toast.LENGTH_LONG).show();

                }
            }
        }, 1000);
        return view;
    }

    private void filterList() {
       // Singleton.getInstance().selectTab=selectedTab;

        if (selectedTab==1) {
             if (TextUtils.isEmpty(Singleton.getInstance().getValue(getActivity(), MY_IDEA_LIST)) && IdeaHuntersApplication.connection) {
                 bar.setVisibility(View.VISIBLE);
                 ideasListPresenterImpl.getIdeasList(1,true);

             }
             else {
            String data = Singleton.getInstance().getValue(getActivity(), MY_IDEA_LIST);
            Log.e("plain", data);
            IdeasListModel mResponseObject = new Gson().fromJson(data, IdeasListModel.class);
            showProgress = false;
            ideasListPresenterImpl.setData(mResponseObject);
                 if(IdeaHuntersApplication.connection)
                 ideasListPresenterImpl.getIdeasList(1, false);

             }
    }
       else if (selectedTab==2) {
            if (TextUtils.isEmpty(Singleton.getInstance().getValue(getActivity(), MOST_LIKED_IDEA_LIST)) && IdeaHuntersApplication.connection){
                showProgress = true;
               bar.setVisibility(View.VISIBLE);

                ideasListPresenterImpl.getIdeasList(2, true);

            } else {
                String data = Singleton.getInstance().getValue(getActivity(), MOST_LIKED_IDEA_LIST);
                Log.e("plain", data);
                IdeasListModel mResponseObject = new Gson().fromJson(data, IdeasListModel.class);
                showProgress = false;
                ideasListPresenterImpl.setData(mResponseObject);
                if(IdeaHuntersApplication.connection)
                    ideasListPresenterImpl.getIdeasList(2, false);
            }
        }

      else  if (selectedTab==3) {
            if (TextUtils.isEmpty(Singleton.getInstance().getValue(getActivity(), LIKED_BY_ME_LIST)) && IdeaHuntersApplication.connection) {
                showProgress = true;
              bar.setVisibility(View.VISIBLE);

                ideasListPresenterImpl.getIdeasList(3, true);

            } else {
                String data = Singleton.getInstance().getValue(getActivity(), LIKED_BY_ME_LIST);
                Log.e("plain", data);
                IdeasListModel mResponseObject = new Gson().fromJson(data, IdeasListModel.class);
                showProgress = false;
                ideasListPresenterImpl.setData(mResponseObject);
                if(IdeaHuntersApplication.connection)
                    ideasListPresenterImpl.getIdeasList(3, false);
            }
        }



    else
   {        if (TextUtils.isEmpty(Singleton.getInstance().getValue(getActivity(), IDEA_LIST)) && IdeaHuntersApplication.connection) {
            showProgress = true;
      bar.setVisibility(View.VISIBLE);
       ideasListPresenterImpl.getIdeasList(0, true);
   } else {
            String data = Singleton.getInstance().getValue(getActivity(), IDEA_LIST);
            Log.e("plain", data);
            IdeasListModel mResponseObject = new Gson().fromJson(data, IdeasListModel.class);
            showProgress = false;
            ideasListPresenterImpl.setData(mResponseObject);
       if(IdeaHuntersApplication.connection)
                ideasListPresenterImpl.getIdeasList(0, false);

        }
    }


}






    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getBundleData() {
        if (Singleton.getInstance().myList) {
            listener.setToolbarTitle(getString(R.string.my_ideas));
            mlistener.showBackButton(true);
            Singleton.getInstance().myList=false;
        } else {
            listener.setToolbarTitle(getString(R.string.app_name));
            mlistener.showBackButton(false);

        }
    }

    private void setRecyclerView(View view) {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecycleIdeaList.setLayoutManager(mLayoutManager);
        mRecycleIdeaList.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecycleIdeaList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
   }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       menu.clear();
        //getActivity().invalidateOptionsMenu();
        inflater.inflate(R.menu.main2, menu);
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);
                MenuItemCompat.setOnActionExpandListener(item,
                        new MenuItemCompat.OnActionExpandListener() {
                            @Override
                            public boolean onMenuItemActionCollapse(MenuItem item) {
                                Log.d("Nodarta11","no data");
//                                ArrayList<IdeaslistData> filteredlist=new ArrayList<>();
//
//                                // Do something when collapsed
//                             //  myRecyclerViewAdapter.notifyDataSetChanged();
//                                if (selectedTab==3)
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList3);
//                                }
//                                else  if (selectedTab==1)
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList1);
//                                }
//                                else  if (selectedTab==2)
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList2);
//                                }
//                                else
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList);
//                                }
//                                myRecyclerViewAdapter.setFilter(filteredlist);
                               // myRecyclerViewAdapter.notifyDataSetChanged();

                                return true; // Return true to collapse action view
                            }

                            @Override
                            public boolean onMenuItemActionExpand(MenuItem item) {
                                Log.d("Nodarta222","no data");

//                                ArrayList<IdeaslistData> filteredlist=new ArrayList<>();
//                                if (selectedTab==3)
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList3);
//                                }
//                                else  if (selectedTab==1)
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList1);
//                                }
//                                else  if (selectedTab==2)
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList2);
//                                }
//                                else
//                                {
//                                    filteredlist.addAll(Singleton.getInstance().filterideaList);
//                                }
                              //  myRecyclerViewAdapter.setFilter(filteredlist);
                               // myRecyclerViewAdapter.notifyDataSetChanged();
                                // Do something when expanded
                                return true; // Return true to expand action view
                            }
                        });
                break;


            case R.id.notification:

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void setIdeasList(ArrayList<IdeaslistData> ideasList) {
        if(bar.getVisibility()==View.VISIBLE){
        bar.setVisibility(View.GONE);}
        Singleton.getInstance().ideaList = ideasList;
        myRecyclerViewAdapter = new IdeasListAdapter(getActivity(), ideasList);
        mRecycleIdeaList.setAdapter(myRecyclerViewAdapter);
        mRecycleIdeaList.setNestedScrollingEnabled(false);

    }


    @OnClick(R.id.fab)
    public void onClick() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IdeaSubmitFragment()).addToBackStack("submit").commit();


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ArrayList<IdeaslistData> filteredlist=new ArrayList<>();
        Log.e("value", String.valueOf(selectedTab));
        if (selectedTab==3)
        {
            filteredlist.addAll(Singleton.getInstance().filterideaList3);
        }
        else  if (selectedTab==1)
        {
            filteredlist.addAll(Singleton.getInstance().filterideaList1);
        }
        else  if (selectedTab==2)
        {
            filteredlist.addAll(Singleton.getInstance().filterideaList2);
        }
        else
        {
            filteredlist.addAll(Singleton.getInstance().filterideaList);
        }
        mRecycleIdeaList.setVisibility(View.VISIBLE);
        noComment.setVisibility(View.GONE);
        final ArrayList<IdeaslistData> filteredModelList = filter(filteredlist, newText);
        if (filteredModelList.size()>0){
            myRecyclerViewAdapter = new IdeasListAdapter(getActivity(), Singleton.getInstance().ideaList);
            mRecycleIdeaList.setAdapter(myRecyclerViewAdapter);
            myRecyclerViewAdapter.setFilter(filteredModelList);
            return true;
        }
        else {
            mRecycleIdeaList.setVisibility(View.GONE);
            Log.d("Nodarta","no data");
            noComment.setVisibility(View.VISIBLE);
            return false;
    //        Toast.makeText(getActivity(),"no data",Toast.LENGTH_LONG).show();
        }

       // return true;
    }


    private ArrayList<IdeaslistData> filter(ArrayList<IdeaslistData> models, String query) {
        query = query.toLowerCase();

        final ArrayList<IdeaslistData> filteredModelList = new ArrayList<>();
        for (IdeaslistData model : models) {
            final String text = model.getIdeaTitle().toLowerCase();
            final String text1 = model.getExplainIdea().toLowerCase();
            if (text.contains(query) || text1.contains(query)) {
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e("calling","override");

        getConnection(getActivity(),isConnected);
       IdeaHuntersApplication.connection = isConnected;
    }


    public void getConnection(Context context, Boolean isConnected) {
try {
    if (isConnected) {
        Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_internet));

    } else {
        Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_no_internet));
    }
}
catch (Exception e){

}


    }




}

