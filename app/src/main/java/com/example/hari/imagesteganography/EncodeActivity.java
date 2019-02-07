package com.example.hari.imagesteganography;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.android.Utils;

public class EncodeActivity extends AppCompatActivity {

    private Uri uriPhoto;

    private Mat coverImage;
    private String globalSecretMessage;
    private Uri globalImageUri;

    private static final int SELCT_IMAGE_CODE = 1;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable("globalImageUri" , this.globalImageUri);
        savedInstanceState.putString("globalSecretMessage" , this.globalSecretMessage);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        this.globalImageUri = savedInstanceState.getParcelable("globalImageUri");
        this.globalSecretMessage = savedInstanceState.getString("globalSecretMessage");
        this.drawImageViewFromGlobalUri();
        EditText message = (EditText) findViewById(R.id.msg_enc_pr);
        message.setText(this.globalSecretMessage);
    }


    private void setDefaultValues(){
        this.coverImage = null;
        this.globalSecretMessage = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.loadLibrary("opencv_java3");

        this.setDefaultValues();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.encode_activity_layout);

        Button selectImage = (Button) findViewById(R.id.select_image_enc_pr);

        Button encodeImage = (Button) findViewById(R.id.enc_msg_btn_pr);

        encodeImage.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                        if(globalImageUri == null){
                            Toast.makeText(getApplicationContext(), "Please select an Image", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(((EditText) findViewById(R.id.msg_enc_pr)).getText().toString().equals("")){
                            Toast.makeText(getApplicationContext() , "Please enter the Secret Message" , Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(globalSecretMessage != null && globalImageUri != null){
                            Log.i("URI",((EditText) findViewById(R.id.msg_enc_pr)).getText().toString());
                            Log.i("URI",globalImageUri.toString());
                            setMatObjectFromGlobalUri();

//                            CALL LIB FUNC TO ENCODE

                        }
                    }
                }
        );

        final EditText secretMessage = (EditText) findViewById(R.id.msg_enc_pr);

        secretMessage.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        globalSecretMessage = secretMessage.getText().toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
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

            }else{
                imageUri = data.getData();

                this.globalImageUri = imageUri;

                this.drawImageViewFromGlobalUri();
                Log.i("URI",imageUri.toString());

            }
//            Intent intent = new Intent();
//            intent.setData(imageUri);
//            setResult(RESULT_OK , intent);
//            finish();
        }
    }

    private void selectImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/png");
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent , SELCT_IMAGE_CODE);
        }
    }

    private void drawImageViewFromGlobalUri(){
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        imageView.setImageURI(globalImageUri);
    }

    private void setMatObjectFromGlobalUri(){
        Imgcodecs imageCodecs = new Imgcodecs();
        Mat obj = null;
        try{
            Bitmap bmp = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(),
                    globalImageUri);
            obj = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(bmp, obj);
        }catch(Exception e){
            Log.i("URI","Exception caught");
        }
        this.coverImage = obj.clone();
        Log.i("URI" , "MAT OBJECT CREATED SUCCESSFULLY");
        Log.i("URI" , new Integer((int) obj.size().height).toString());
        Log.i("URI" , new Integer((int) obj.size().width).toString());
    }
}
