package com.toast.log.session302;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void VIOLET(View v)
    {
        Toast.makeText(this, "you have clicked Violet Color", Toast.LENGTH_SHORT).show();
        Log.i("Clicked", "Violet Color");
    }
    public void INDIGO(View v)
    {
        Toast.makeText(this, "you have clicked Indigo Color",Toast.LENGTH_SHORT).show();
        Log.i("Clicked", "Indigo Color");
    }
    public void BLUE(View v)
    {
        Toast.makeText(this, "you have clicked Blue Color",Toast.LENGTH_SHORT).show();
        Log.i("Clicked", "Blue Color");
    }
    public void GREEN(View v)
    {
        Toast.makeText(this, "you have clicked Green Color",Toast.LENGTH_SHORT).show();
        Log.i("Clicked", "Green Color");
    }
    public void YELLOW(View v)
    {
        Toast.makeText(this, "you have clicked Yellow Color",Toast.LENGTH_SHORT).show();
        Log.i("Clicked", "Yellow Color");
    }
    public void ORANGE(View v)
    {
        Toast.makeText(this, "you have clicked Orange Color",Toast.LENGTH_SHORT).show();
        Log.i("Clicked", "Orange Color");
    }
    public void RED(View v)
    {
        Toast.makeText(this, "you have clicked Red Color",Toast.LENGTH_SHORT).show();
        Log.i("Clicked", "Red Color");
    }
}
