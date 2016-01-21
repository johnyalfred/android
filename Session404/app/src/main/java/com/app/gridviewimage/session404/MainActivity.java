package com.app.gridviewimage.session404;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Johny on 07-01-2016.
 */
public class MainActivity extends Activity {
    GridView gv;

     String [] Names={"GingerBread","Honeycomb","IceCream","JellyBean","Kitkat","Lollipop"};
    int [] Images={R.drawable.images_1,R.drawable.images_2,R.drawable.images_3,R.drawable.images_4,R.drawable.images_5,R.drawable.images_6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv=(GridView)findViewById(R.id.gridView1);
        GridViewAdapter ga= new GridViewAdapter(MainActivity.this, Names, Images);
        gv.setAdapter(ga);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " +Names[+ position], Toast.LENGTH_SHORT).show();

            }
        });

    }

}