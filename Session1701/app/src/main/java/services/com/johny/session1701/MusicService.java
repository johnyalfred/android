package services.com.johny.session1701;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;


/**
 * Created by Johny on 24-01-2016.
 */
public class MusicService extends Service {

    MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.farewell);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        Toast.makeText(this,"Music Started",Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        mPlayer.stop();
        Toast.makeText(this, "Music Stoped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
