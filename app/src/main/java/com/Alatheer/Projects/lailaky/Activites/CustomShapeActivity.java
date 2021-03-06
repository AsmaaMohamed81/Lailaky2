package com.Alatheer.Projects.lailaky.Activites;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.Alatheer.Projects.lailaky.ApiServices.Tags;
import com.Alatheer.Projects.lailaky.Models.FinalImageModel;
import com.Alatheer.Projects.lailaky.R;
import com.Alatheer.Projects.lailaky.SingleTone.FinalAlbumImage;
import com.Alatheer.Projects.lailaky.share.Common;
import com.jcmore2.collage.CollageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class CustomShapeActivity extends AppCompatActivity {
    private final int IMG_REQ = 336;
    private List<Bitmap> bitmapList;
    private CollageView collageView;
    private Button btn_save;
    private FrameLayout root;
    private FinalAlbumImage instance;
    private List<Bitmap> bitmapList2;
    private AlertDialog dialog;
    private String user_id="",offer_id="",paper_id="";
    private int album_size=0;



    @Override
    protected void attachBaseContext(Context newBase) {


        Paper.init(newBase);


        super.attachBaseContext(LanguageHelper.onAttach(newBase, Paper.book().read("language", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_shape);
        initView();
        Toast.makeText(this, ""+paper_id, Toast.LENGTH_SHORT).show();

    }

    private void initView() {
        getDataFromIntent();
        instance = FinalAlbumImage.getInstance();
        bitmapList2 = new ArrayList<>();
        bitmapList = new ArrayList<>();
        collageView = findViewById(R.id.collageView);
        root = findViewById(R.id.root);
        btn_save = findViewById(R.id.btn_save);
        collageView.setFixedCollage(false);
        displayImage();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                instance.increaseCount(1);
                if (instance.getCount()>album_size)
                {
                    instance.setCount(album_size);
                    CreateAlertDialog(album_size);

                }
                else
                {


                    FinalImageModel finalImageModel = new FinalImageModel();
                    finalImageModel.setType(Tags.type_one_page);
                    finalImageModel.setImage1(getByteArrayFromBitmap(getBitmaps()));
                    instance.addImage(finalImageModel);
                    CreateAlertDialog(instance.getCount());

                }
            }
        });

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            user_id = intent.getStringExtra("user_id");
            offer_id =intent.getStringExtra("id_offer");
            paper_id =intent.getStringExtra("paper_id");
            album_size = intent.getIntExtra("album_size",0);
        }
    }


    private byte[] getByteArrayFromBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        return outputStream.toByteArray();
    }
    private void CreateAlertDialog(int count) {
        int c = album_size-count;
        dialog = new AlertDialog.Builder(this)
                .setMessage("هل تريد اختيار صور اخرى لرفعها ؟ ام مشاهده الصور  \n عدد صور الالبوم "+album_size+"\n"+"المتبقي "+c)
                .setCancelable(true)
                .setPositiveButton("صور اخرى", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //instance.increaseCount();

                        if (instance.getCount()<album_size)
                        {

                            UpdateFragmentUi();
                            finish();

                        }else
                        {
                            instance.setCount(album_size);
                            btn_save.setVisibility(View.VISIBLE);
                            Toast.makeText(CustomShapeActivity.this, "عدد صور الالبوم لاتكفي", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("مشاهده", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        NavigatetoFinalAlbum();

                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private Bitmap getBitmaps()
    {
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);
        return bitmap;
    }
    private void UpdateFragmentUi() {
        btn_save.setVisibility(View.INVISIBLE);
    }
    public void NavigatetoFinalAlbum()
    {
        Log.e("sdfsdfsd","dddddddd");
        Intent intent = new Intent(CustomShapeActivity.this,FinalAlbumActivity.class);
        intent.putExtra("user_id",user_id);
        intent.putExtra("paper_id",paper_id);
        intent.putExtra("id_offer",offer_id);
        startActivity(intent);
        finish();
    }
    public void displayImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent.createChooser(intent,"Choose image"),IMG_REQ);


    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMG_REQ && resultCode==RESULT_OK && data!=null)
        {
            ClipData clipData = data.getClipData();
            if (clipData!=null)
            {
                if (clipData.getItemCount()<5&&bitmapList.size()==0)
                {
                    android.support.v7.app.AlertDialog alertDialog = Common.chooseAlertDialog(CustomShapeActivity.this);
                    alertDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            displayImage();

                        }
                    }).start();


                }else
                {
                    for (int i =0;i<clipData.getItemCount();i++)
                    {
                        ClipData.Item item = clipData.getItemAt(i);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(item.getUri()));
                            bitmapList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                    collageView.createCollageBitmaps(bitmapList);
                    collageView.requestLayout();
                    collageView.invalidate();
                    btn_save.setVisibility(View.VISIBLE);
                    //card_container.setVisibility(View.VISIBLE);
                }


            }else
            {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        displayImage();

                    }
                }).start();
                android.support.v7.app.AlertDialog alertDialog = Common.chooseAlertDialog(CustomShapeActivity.this);
                alertDialog.show();

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}