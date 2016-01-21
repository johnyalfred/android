package broadcastreceiver.com.johny.session1601;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context c, Intent i) {

            //int level = i.getIntExtra("level", 0);
            int level = i.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar);

            pb.setProgress(level);

            TextView tv = (TextView) findViewById(R.id.tv1);

            tv.setText("Battery Level Remaining: " + level + "%");
        }

    };
}
