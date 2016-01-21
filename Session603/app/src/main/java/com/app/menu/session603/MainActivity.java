package com.app.menu.session603;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.bl){
            textview.setTextColor(Color.parseColor("#0000FF"));
        }
        else if(item.getItemId()== R.id.gr){
            textview.setTextColor(Color.parseColor("#00FF00"));
        }
        else if(item.getItemId()== R.id.rd){
            textview.setTextColor(Color.parseColor("#FF0000"));
        }
        return super.onOptionsItemSelected(item);
    }

}
