package com.loginintent.session504;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Johny on 17-12-2015.
 */
public class Login extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TextView tv = (TextView) findViewById(R.id.textView);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("username");
        tv.setText("Welcome " + message + "!!");
    }
}