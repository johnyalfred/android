package com.intent.list.session503;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Johny on 11-01-2016.
 */
public class Mentor extends Activity {

    TextView TxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_activity);

        TxtView = (TextView) findViewById(R.id.tV1);
        Bundle bundle = getIntent().getExtras();
        String data= bundle.getString("key");
        TxtView.setText(data);
    }
}





