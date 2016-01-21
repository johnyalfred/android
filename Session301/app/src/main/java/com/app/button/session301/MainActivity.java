package com.app.button.session301;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    ImageView image;
     View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        image = (ImageView) findViewById(R.id.imageView);
        btn1 = (Button) findViewById(R.id.button);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setVisibility(View.INVISIBLE);
                btn1.setText("Show");



            }
        });
        }}