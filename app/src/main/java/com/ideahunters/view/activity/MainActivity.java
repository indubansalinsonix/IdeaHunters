package com.ideahunters.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.ideahunters.R;
import com.ideahunters.adapter.ViewPagerAdapter;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.customwidgets.sweetalertdialog.SweetAlertDialog;
import com.ideahunters.interfaces.ChangeToogleButtonIconListener;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.interfaces.OnBackPressedListener;
import com.ideahunters.interfaces.ToolbarTitleChangeListener;
import com.ideahunters.presenter.IdeaSubmitPresenter;
import com.ideahunters.presenter.PostSubmitData;
import com.ideahunters.utils.BusProvider;
import com.ideahunters.utils.Singleton;
import com.ideahunters.view.fragment.HomeFragment;
import com.ideahunters.view.fragment.IdeaDetailFragment;
import com.ideahunters.view.fragment.IdeaListFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ideahunters.utils.Constants.COMPANY_NAME;
import static com.ideahunters.utils.Constants.LOGO;
import static com.ideahunters.utils.Constants.OFFLINE_STORE_IDEA;
import static com.ideahunters.utils.Constants.USER_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ToolbarTitleChangeListener ,ChangeToogleButtonIconListener, IdeaSubmitPresenter.IdeaSubmitPresenterListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    ActionBarDrawerToggle toggle;
   @BindView(R.id.fragment_container)
    FrameLayout mainLayout;

        @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!IdeaHuntersApplication.connection)
            IdeaHuntersApplication.getInstance().checkConnection(this);


        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);
        IdeaHuntersApplication.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                .setLabel("SignUp Activity")
                .build());
        //   setupToolbar();

      //  Picasso.with(this).load(Singleton.getInstance().getValue(getApplicationContext(),LOGO)).into(toolbar.setL);
        setSupportActionBar(toolbar);
       /* if(!TextUtils.isEmpty(Singleton.getInstance().getValue(getApplicationContext(),LOGO))) {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
            displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.banner).showImageForEmptyUri(R.drawable.banner).cacheOnDisc().cacheInMemory()
                    .build();
            displayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc().cacheInMemory().postProcessor(new BitmapProcessor() {
                @Override
                public Bitmap process(Bitmap bmp) {
                    return Bitmap.createScaledBitmap(bmp, 24, 24, false);
                }
            })
                    .build();
            Bitmap bitmap=imageLoader.loadImageSync(Singleton.getInstance().getValue(getApplicationContext(), LOGO));
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 30, 30, true));
            getSupportActionBar().setIcon(d);
        }*/
        setupDrawer();


        Fragment fragment=new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();



    }



    private void setupToolbar() {
       /* Picasso.with(this)
                .load(Singleton.getInstance().getValue(getApplicationContext(),LOGO))
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        Drawable d = new BitmapDrawable(getResources(), bitmap);
                       getSupportActionBar().setIcon(d);
                      getSupportActionBar().setDisplayShowHomeEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable)
                    {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable)
                    {
                    }
                });*/
    }

    private void setupDrawer() {
         toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
   //  setBackButton();

        navView.setNavigationItemSelectedListener(this);
        View header=navView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/

        ImageView mCompanyIcon= (ImageView)header.findViewById(R.id.company_icon);
        TextView mUserName= (TextView)header. findViewById(R.id.username);
        TextView mCompanyName= (TextView)header. findViewById(R.id.company_name);
        mUserName.setText(Singleton.getInstance().getValue(getApplicationContext(),USER_NAME));
        mCompanyName.setText(Singleton.getInstance().getValue(getApplicationContext(),COMPANY_NAME));
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.banner).showImageForEmptyUri(R.drawable.banner).cacheOnDisc().cacheInMemory()
                .build();
        imageLoader.displayImage(Singleton.getInstance().getValue(getApplicationContext(),LOGO),mCompanyIcon,displayImageOptions);
    }

   /* private void setBackButton() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    Log.e("count", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }
               *//*else if (getSupportFragmentManager().getBackStackEntryCount() == 1 ) {
                    Log.e("count", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });

                }*//*
            *//*    else {
                    Log.e("count", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

                    if(Singleton.getInstance().myList)
                    {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                    }*//*
                    else {
                        //show hamburger
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                drawerLayout.openDrawer(GravityCompat.START);
                            }
                        });
                    }
                }

        });
    }*/

    private void setBackButton() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }
                else if(getSupportFragmentManager().getBackStackEntryCount() ==0){
                    if(Singleton.getInstance().myList) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                    }

                    else {
                        //  drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        //show hamburger
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                drawerLayout.openDrawer(GravityCompat.START);
                            }
                        });
                    }
                }

            }
        });
    }

    private boolean doubleBackToExitPressedOnce;


    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
Log.d("cheks", String.valueOf(Singleton.getInstance().myList));
        //    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
      else  if(currentFragment instanceof IdeaListFragment && !Singleton.getInstance().myList) {
            //Singleton.getInstance().myList = false;

           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        else if(currentFragment instanceof HomeFragment && Singleton.getInstance().selectTab==0)
        {
            if (doubleBackToExitPressedOnce) {
                Log.e("countii2", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

                //  getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
               finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        else /*if (getSupportFragmentManager().getBackStackEntryCount()>1)*/{
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            if (fragmentList != null) {
                Log.e("size11", String.valueOf(fragmentList.size()));
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof OnBackPressedListener) {
                        ((OnBackPressedListener) fragment).onBackPressed();
                    }


                }
            }
         //   ((OnBackPressedListener) this).onBackPressed();

           /* List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            if (fragmentList != null) {
                Log.e("size11", String.valueOf(fragmentList.size()));
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof OnBackPressedListener) {
                        ((OnBackPressedListener) fragment).onBackPressed();
                    }


                }
            }*/
           /* if(getSupportFragmentManager().getBackStackEntryCount()==0) {

                    //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    if (doubleBackToExitPressedOnce) {

                        super.onBackPressed();
                        return;
                    }
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);



            }
            else if(getSupportFragmentManager().getBackStackEntryCount()==1)
            {
                Singleton.getInstance().myList=false;
                getSupportFragmentManager().popBackStack();
            }
            else
            {
                getSupportFragmentManager().popBackStack();
            }*/
        }

     /*   else
        {
            if (doubleBackToExitPressedOnce) {
                Log.e("countii2", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

                //  getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }*/
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      if (id == R.id.nav_logout) {
          logoutProgressDialog();

        }  else if (id == R.id.nav_my_idea) {
          Fragment fragment=new IdeaListFragment(0);
          Singleton.getInstance().myList=true;
          getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


      } /* else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  private void  logoutProgressDialog()
    {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Warning!")
                .setContentText("You want to Logout?")
                .setCancelText("Cancel!")
                .setConfirmText("Yes!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText("Cancelled!")
                                .setContentText("You are still login :)")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);

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
                        sDialog.setTitleText("Logout!")
                                .setContentText("Are you sure you want to Logout?")
                                .setConfirmText("YES")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        //Singleton.getInstance().saveValue(getApplicationContext(),USER_ID,"");
                                        Singleton.getInstance().clearAllData(getApplicationContext());
                                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    }
                })
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void showBackButton(boolean value) {
        if(value)
        {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        else
        {
            //show hamburger
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.syncState();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

  /*  @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
       // getConnection(isConnected);
        //IdeaHuntersApplication.connection = isConnected;
    }

    private void getConnection(Boolean isConnected) {
        if (isConnected)
            showSnackAlert(toolbar, getString(R.string.dialog_message_internet));
        else
            showSnackAlert(toolbar, getString(R.string.dialog_message_no_internet));

    }*/

    public void showSnackAlert(@NonNull View view, @NonNull String message) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(Color.RED);
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    public void showSnackAlert1(@NonNull String message) {
        Snackbar snack = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(Color.RED);
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }



    @Override
    public void onSuccessfulSubmit() {

    }

    @Override
    public void showTitleBlankError() {

    }

    @Override
    public void showCategoryBlankError(String category_name) {

    }

    @Override
    public void showIdeaSubmitBlankError() {

    }

    @Override
    public void showKeyResultBlankError() {

    }
}
