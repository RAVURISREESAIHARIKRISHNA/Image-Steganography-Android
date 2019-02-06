package com.example.hari.imagesteganography;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button encodeButton = (Button) findViewById(R.id.encode_button_id);
        Button decodeButton = (Button) findViewById(R.id.decode_button_id);

        encodeButton.setOnClickListener(
          new Button.OnClickListener(){
              public void onClick(View v){
                Log.i(TAG , "Encode Button Press");
//                setContentView(R.layout.encode_activity_layout);

                Intent encode = new Intent(MainActivity.this , EncodeActivity.class);
                startActivity(encode);
              }
          }
        );

        decodeButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Log.i(TAG , "Decode Button Press");
//                        setContentView(R.layout.decode_activity_layout);

                        Intent decode = new Intent(MainActivity.this , DecodeActivity.class);
                        startActivity(decode);
                    }
                }
        );
    }


}
