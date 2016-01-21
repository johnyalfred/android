package com.loginintent.session504;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    EditText euname, epassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        euname = (EditText) findViewById(R.id.uname);
        epassword = (EditText) findViewById(R.id.pswd);

    }


    public void login(View view) {
        String username = euname.getText().toString();
        String password = epassword.getText().toString();
        if (username.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
           intent.putExtra("username",username);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Invalid Usename password pair.", Toast.LENGTH_LONG).show();
        }
    }
}
