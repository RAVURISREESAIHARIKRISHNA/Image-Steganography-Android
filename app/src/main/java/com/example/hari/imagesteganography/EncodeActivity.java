package com.example.hari.imagesteganography;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.provider.MediaStore;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.android.Utils;

public class EncodeActivity extends AppCompatActivity {

    private Uri uriPhoto;

    private static final int SELCT_IMAGE_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.loadLibrary("opencv_java3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.encode_activity_layout);

        Button selectImage = (Button) findViewById(R.id.select_image_enc_pr);
//        Button selectImage_Landscape = (Button) findViewById(R.id.select_image_enc_land);

        selectImage.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        selectImageFromGallery();
                    }
                }
        );
//        selectImage_Landscape.setOnClickListener(
//                new Button.OnClickListener(){
//                    public void onClick(View v){
//                        selectImageFromGallery();
//                    }
//                }
//        );

    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data){
        if(resultCode == RESULT_OK){
            Uri imageUri;
            if(data == null || data.getData()== null){
                imageUri = uriPhoto;
//                Log.i("URI","HERE");
            }else{
                imageUri = data.getData();
                Log.i("URI",imageUri.toString());
                Imgcodecs imageCodecs = new Imgcodecs();
                Mat obj = null;
                try{
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            imageUri);
                    obj = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
                    Utils.bitmapToMat(bmp, obj);
                }catch(Exception e){
                    Log.i("URI","Exception caught");
                }
                Log.i("URI" , "MAT OBJECT CREATED SUCCESSFULLY");
                Log.i("URI" , new Integer((int) obj.size().height).toString());
                Log.i("URI" , new Integer((int) obj.size().width).toString());
            }
            Intent intent = new Intent();
            intent.setData(imageUri);
            setResult(RESULT_OK , intent);
            finish();
        }
    }

    private void selectImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/png");
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent , SELCT_IMAGE_CODE);
        }
    }
}
