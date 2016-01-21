package com.app.login.session304;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText euname,epassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        euname = (EditText) findViewById(R.id.uname);
        epassword = (EditText) findViewById(R.id.pass);
    }
    public void login(View view) {
        String username = euname.getText().toString();
        String password = epassword.getText().toString();
        if (username.equals("admin") && password.equals("admin")) {
            Toast.makeText(MainActivity.this, "Valid Login Details", Toast.LENGTH_LONG).show();
        } else if(username.equals("") || password.equals("")) {
            Toast.makeText(MainActivity.this, "Null Values In Fields", Toast.LENGTH_LONG).show();
        }else if(username.equals("admin") != password.equals("admin")) {
            Toast.makeText(MainActivity.this, "Enter Valid Password", Toast.LENGTH_LONG).show();
        }
    }
}

