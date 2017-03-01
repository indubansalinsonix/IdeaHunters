package com.ideahunters.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.ideahunters.R;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.customwidgets.MaterialBetterSpinner.MaterialBetterSpinner;
import com.ideahunters.interfaces.ChangeToogleButtonIconListener;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.interfaces.OnBackPressedListener;
import com.ideahunters.interfaces.ToolbarTitleChangeListener;
import com.ideahunters.model.CategoryData;
import com.ideahunters.model.CategoryListPojo;
import com.ideahunters.model.IdeasListModel;
import com.ideahunters.model.IdeaslistData;
import com.ideahunters.model.Subcategory;
import com.ideahunters.presenter.IdeaCategoryListPresenter;
import com.ideahunters.presenter.IdeaSubmitPresenter;
import com.ideahunters.presenter.PostSubmitData;
import com.ideahunters.utils.Singleton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ideahunters.utils.Constants.CATEGORY_LIST;
import static com.ideahunters.utils.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.ideahunters.utils.Constants.OFFLINE_STORE_IDEA;

/**
 * Created by root on 6/2/17.
 */

public class IdeaSubmitFragment extends Fragment implements IdeaCategoryListPresenter.IdeaCategoryListPresenterListener, IdeaSubmitPresenter.IdeaSubmitPresenterListener,OnBackPressedListener, ConnectivityReceiveListener {


    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.input_layout_name1)
    TextInputLayout inputLayoutName1;
    @BindView(R.id.category_spinner)
    MaterialBetterSpinner categorySpinner;
    @BindView(R.id.subcategory_spinner)
    MaterialBetterSpinner subcategorySpinner;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.ideaexplainlayout)
    TextInputLayout ideaexplainlayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.key_result)
    EditText keyResult;
    @BindView(R.id.keyresultslayout)
    TextInputLayout keyresultslayout;
    @BindView(R.id.submit)
    Button submit;
    private Bitmap thumbnail;

    private IdeaCategoryListPresenter ideaCategoryListPresenter;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int REQUESTCODE = 99;
    private String userChoosenTask;
    private String category_name;
    private String subcategory_name;
    private ToolbarTitleChangeListener listener;
    public static ArrayList<CategoryData> categoryList;
    ArrayList<Subcategory> subCategoryList;
    private Bundle extras;
    private IdeaslistData data;
    private String sug_id;
    private ChangeToogleButtonIconListener mlistener;
    ViewGroup viewGroup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ToolbarTitleChangeListener) context;
            mlistener=(ChangeToogleButtonIconListener)context;
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
        View view = inflater.inflate(R.layout.submit, container, false);
        ButterKnife.bind(this, view);
        this.viewGroup=container;
        if (!IdeaHuntersApplication.connection)
            IdeaHuntersApplication.getInstance().checkConnection(getActivity());
        IdeaHuntersApplication.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                .setLabel("IdeaSubmit Screen")
                .build());

        getIntentData();
        mlistener.showBackButton(true);
        title.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        subCategoryList=new ArrayList<>();
        ideaCategoryListPresenter = new IdeaCategoryListPresenter(this, getActivity());

        if(!IdeaHuntersApplication.connection) {
            String data = Singleton.getInstance().getValue(getActivity(), CATEGORY_LIST);
            Log.e("plain", data);
            CategoryListPojo mResponseObject = new Gson().fromJson(data, CategoryListPojo.class);
           ideaCategoryListPresenter.setData(mResponseObject);
        }
        else
        {
            ideaCategoryListPresenter.getIdeaCategoryList();
        }
       categorySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(category_name))
                {
                   // categorySpinner.setEnabled(true);
                }
               else if( category_name.equals(getString(R.string.no_category_added)))
                {
                  // categorySpinner.setKeyListener(null);
                    //categorySpinner.setEnabled(false);
                    Toast.makeText(getActivity(), getString(R.string.no_category_added), Toast.LENGTH_LONG).show();
                }


            }
        });

        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    category_name = categorySpinner.getText().toString();

                    if(categoryList.get(i).getSubcategories().size()>0) {
                        subCategoryList.clear();
                        for (int j = 0; j <categoryList.get(i).getSubcategories().size(); j++) {

                            Subcategory List = new Subcategory();
                            List.setId(categoryList.get(i).getSubcategories().get(j).getId());
                            List.setSubcategory(categoryList.get(i).getSubcategories().get(j).getSubcategory());

                            subCategoryList.add(List);

                        }
                        subcategorySpinner.setVisibility(View.VISIBLE);
                        setSubCategoryList(subCategoryList);
                    }
                    else
                    {
                        subcategorySpinner.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
        subcategorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    subcategory_name = subcategorySpinner.getText().toString();
                } catch (Exception e) {

                }

            }
        });


        return view;
    }

    private void getIntentData() {
         extras = getArguments();

        if(extras!=null)
        {
            submit.setText(getString(R.string.update));

            listener.setToolbarTitle(getString(R.string.edit_idea));
            data= (IdeaslistData) extras.getSerializable("details");
            sug_id = getArguments().getString("sug_id");

            setData(data);
        }
        else
        {
            listener.setToolbarTitle(getString(R.string.add_idea));
            submit.setText(getString(R.string.submit));

        }
    }

    private void setData(IdeaslistData data) {
        title.setText(data.getIdeaTitle());
        if(!TextUtils.isEmpty(data.getCatId()))
        {
            categorySpinner.setText(data.getCatId());
            category_name=data.getCatId();
        }
        description.setText(data.getExplainIdea());
        if(!TextUtils.isEmpty(data.getImage()))
        {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc().cacheInMemory().build();
            imageLoader.displayImage(data.getImage(),image,displayImageOptions);
        }
        keyResult.setText(data.getKeyResult());



    }


    @Override
    public void setCategoryList(ArrayList<CategoryData> categoryList) {
     this.categoryList=categoryList;
        String[] values = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            values[i] = categoryList.get(i).getCategory();
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, values);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter2);
        Log.e("arr", values[0]);
        Log.e("list size", String.valueOf(categoryList.size()));

    }

    @Override
    public void setCategoryListBlankMessage() {
        category_name=getString(R.string.no_category_added);
    }

    public void setSubCategoryList(ArrayList<Subcategory> subCategoryList) {
        if (subCategoryList.size() > 0) {

            String[] values = new String[subCategoryList.size()];
            for (int i = 0; i < subCategoryList.size(); i++) {
                values[i] = subCategoryList.get(i).getSubcategory();
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, values);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subcategorySpinner.setAdapter(adapter2);
            subcategorySpinner.setVisibility(View.VISIBLE);
            Log.e("arr", values[0]);
            Log.e("list size", String.valueOf(subCategoryList.size()));
        }

    }


    private void selectImage() {


        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Singleton.getInstance().checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        String image1=data.getExtras().get("data").toString();
       // compressImage(image1);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        int nh = (int) (thumbnail.getHeight() * (512.0 / thumbnail.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(thumbnail, 512, nh, true);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        image.setImageBitmap(scaled);
    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }



    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap scaled = null;
        thumbnail = null;
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                int nh = (int) (thumbnail.getHeight() * (512.0 / thumbnail.getWidth()) );
                scaled = Bitmap.createScaledBitmap(thumbnail, 512, nh, true);
                String image1=data.getData().toString();
               // compressImage(image1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        image.setImageBitmap(scaled);
    }


    @OnClick({R.id.image, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image:
                selectImage();
                break;
            case R.id.submit:

                boolean UPDATE_IDEA=false;
                String idea_submit=description.getText().toString().trim();
                String idea_title=title.getText().toString().trim();
                String key_result=keyResult.getText().toString().trim();
                IdeaSubmitPresenter ideaSubmitPresenter = new IdeaSubmitPresenter(this, getActivity());
                if(extras!=null) {
                    UPDATE_IDEA=true;

                }

                PostSubmitData data = PostSubmitData.getInstance();
            data.setCategory_name(category_name);
                data.setSubcategory_name(subcategory_name);
                data.setIdea_submit(idea_submit);
                data.setIdea_title(idea_title);
                data.setKey_result(key_result);
                data.setThumbnail(thumbnail);
                data.setUPDATE_IDEA(UPDATE_IDEA);
                data.setSug_id(sug_id);
               if( ideaSubmitPresenter.onValidate(category_name,subcategory_name,idea_submit,idea_title,key_result))
                if(!IdeaHuntersApplication.connection) {
                    Gson gson = new Gson();
                    String stringData = gson.toJson(data);
                    Singleton.getInstance().saveValue(getActivity(), OFFLINE_STORE_IDEA, stringData);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

                }
                else {
                    ideaSubmitPresenter.submitIdea(category_name, subcategory_name, idea_submit, idea_title, key_result, thumbnail, UPDATE_IDEA, sug_id, true);
                }

                break;
        }
    }


    @Override
    public void onSuccessfulSubmit() {
       /* FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(new IdeaDetailFragment());
        trans.commit();
        manager.popBackStack();*/
        //Singleton.getInstance().myList=true;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        /*Fragment fragment=new IdeaListFragment();
        Singleton.getInstance().myList=false;
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        getActivity().getSupportFragmentManager().popBackStack();
     //   getActivity().getSupportFragmentManager().popBackStack();*/

    }

    @Override
    public void showTitleBlankError() {
        title.setError(getResources().getString(R.string.err_msg_title_blank));
        title.requestFocus();
    }

    @Override
    public void showCategoryBlankError(String category_name) {
        if(TextUtils.isEmpty(category_name)) {
            categorySpinner.setError(getResources().getString(R.string.err_msg_category));
            categorySpinner.requestFocus();
        }
        else
        {
            categorySpinner.setError(getResources().getString(R.string.no_category_added));
            categorySpinner.requestFocus();
        }
    }

    @Override
    public void showIdeaSubmitBlankError() {
        description.setError(getResources().getString(R.string.err_msg_description));
        description.requestFocus();
    }

    @Override
    public void showKeyResultBlankError() {
        keyResult.setError(getResources().getString(R.string.err_msg_keyresult));
        keyResult.requestFocus();
    }


    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        getConnection(getActivity(),isConnected);
        IdeaHuntersApplication.connection = isConnected;
    }

    public void getConnection(Context context, Boolean isConnected) {
        if (isConnected) {
            Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_internet));
            if(IdeaHuntersApplication.connection) {
                ideaCategoryListPresenter = new IdeaCategoryListPresenter(this, getActivity());
                ideaCategoryListPresenter.getIdeaCategoryList();
            }


        } else {
            Singleton.getInstance().showSnackAlert(viewGroup, getString(R.string.dialog_message_no_internet));
        }
    }

}
