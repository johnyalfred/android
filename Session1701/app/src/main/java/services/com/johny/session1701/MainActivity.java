package services.com.johny.session1701;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button start,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=(Button) findViewById(R.id.start);
        stop=(Button) findViewById(R.id.stop);
    }
    public void startmusic(View v)
    {
        Intent intent=new Intent(this,MusicService.class);
        startService(intent);

    }
    public void stopmusic(View v)
    {
        Intent intent=new Intent(this,MusicService.class);
        stopService(intent);

    }

}
