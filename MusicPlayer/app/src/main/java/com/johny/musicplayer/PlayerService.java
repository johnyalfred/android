package com.johny.musicplayer;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.v4.app.NotificationCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RemoteViews;

import com.crashlytics.android.Crashlytics;
import com.johny.musicplayer.activity.LibraryActivity;
import com.johny.musicplayer.instances.Song;
import com.johny.musicplayer.IPlayerService;


import java.io.IOException;
import java.util.List;

public class PlayerService extends Service {

    private static final String TAG = "PlayerService";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static final int NOTIFICATION_ID = 1;

    // Intent Action & Extra names
    /**
     * Toggle between play and pause
     */
    public static final String ACTION_TOGGLE_PLAY = "com.johny.musicplayer.action.TOGGLE_PLAY";
    /**
     * Skip to the previous song
     */
    public static final String ACTION_PREV = "com.johny.musicplayer.action.PREVIOUS";
    /**
     * Skip to the next song
     */
    public static final String ACTION_NEXT = "com.johny.musicplayer.action.NEXT";
    /**
     * Stop playback and kill service
     */
    public static final String ACTION_STOP = "com.johny.musicplayer.action.STOP";

    /**
     * The service instance in use (singleton)
     */
    private static PlayerService instance;

    /**
     * Used in binding and unbinding this service to the UI process
     */
    private static IBinder binder;

    // Instance variables
    /**
     * The media player for the service instance
     */
    private Player player;
    private boolean finished = false; // Don't attempt to release resources more than once

    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new Stub();
        }
        return binder;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) Log.i(TAG, "onCreate() called");

        if (instance == null) {
            instance = this;
        } else {
            if (DEBUG) Log.w(TAG, "Attempted to create a second PlayerService");
            stopSelf();
            return;
        }

        if (player == null) {
            player = new Player(this);
        }

        player.reload();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.i(TAG, "Called onDestroy()");
        try {
            player.saveState("");
        } catch (Exception ignored) {

        }
        finish();
        super.onDestroy();
    }

    public static PlayerService getInstance() {
        return instance;
    }

    /**
     * Generate and post a notification for the current player status
     * Posts the notification by starting the service in the foreground
     */
    public void notifyNowPlaying() {
        if (DEBUG) Log.i(TAG, "notifyNowPlaying() called");

        if (player.getNowPlaying() == null) {
            if (DEBUG) Log.i(TAG, "Not showing notification -- nothing is playing");
            return;
        }

        // Create the compact view
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification);
        // Create the expanded view
        RemoteViews notificationViewExpanded =
                new RemoteViews(getPackageName(), R.layout.notification_expanded);

        // Set the artwork for the notification
        if (player.getArt() != null) {
            notificationView.setImageViewBitmap(R.id.notificationIcon, player.getArt());
            notificationViewExpanded.setImageViewBitmap(R.id.notificationIcon, player.getArt());
        } else {
            notificationView.setImageViewResource(R.id.notificationIcon, R.drawable.album_art);
            notificationViewExpanded
                    .setImageViewResource(R.id.notificationIcon, R.drawable.album_art);
        }

        // If the player is playing music, set the track info and the button intents
        if (player.getNowPlaying() != null) {
            // Update the info for the compact view
            notificationView.setTextViewText(R.id.notificationContentTitle,
                    player.getNowPlaying().getSongName());
            notificationView.setTextViewText(R.id.notificationContentText,
                    player.getNowPlaying().getAlbumName());
            notificationView.setTextViewText(R.id.notificationSubText,
                    player.getNowPlaying().getArtistName());

            // Update the info for the expanded view
            notificationViewExpanded.setTextViewText(R.id.notificationContentTitle,
                    player.getNowPlaying().getSongName());
            notificationViewExpanded.setTextViewText(R.id.notificationContentText,
                    player.getNowPlaying().getAlbumName());
            notificationViewExpanded.setTextViewText(R.id.notificationSubText,
                    player.getNowPlaying().getArtistName());
        }

        // Set the button intents for the compact view
        setNotificationButton(notificationView, R.id.notificationSkipPrevious, ACTION_PREV);
        setNotificationButton(notificationView, R.id.notificationSkipNext, ACTION_NEXT);
        setNotificationButton(notificationView, R.id.notificationPause, ACTION_TOGGLE_PLAY);
        setNotificationButton(notificationView, R.id.notificationStop, ACTION_STOP);

        // Set the button intents for the expanded view
        setNotificationButton(notificationViewExpanded, R.id.notificationSkipPrevious, ACTION_PREV);
        setNotificationButton(notificationViewExpanded, R.id.notificationSkipNext, ACTION_NEXT);
        setNotificationButton(notificationViewExpanded, R.id.notificationPause, ACTION_TOGGLE_PLAY);
        setNotificationButton(notificationViewExpanded, R.id.notificationStop, ACTION_STOP);

        // Update the play/pause button icon to reflect the player status
        if (!(player.isPlaying() || player.isPreparing())) {
            notificationView.setImageViewResource(R.id.notificationPause,
                    R.drawable.ic_play_arrow_36dp);
            notificationViewExpanded.setImageViewResource(R.id.notificationPause,
                    R.drawable.ic_play_arrow_36dp);
        } else {
            notificationView.setImageViewResource(R.id.notificationPause,
                    R.drawable.ic_pause_36dp);
            notificationViewExpanded.setImageViewResource(R.id.notificationPause,
                    R.drawable.ic_pause_36dp);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(
                        (player.isPlaying() || player.isPreparing())
                                ? R.drawable.ic_play_arrow_24dp
                                : R.drawable.ic_pause_24dp)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        new Intent(this, LibraryActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        Notification notification = builder.build();

        // Manually set the expanded and compact views
        notification.contentView = notificationView;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            notification.bigContentView = notificationViewExpanded;
        }

        startForeground(NOTIFICATION_ID, notification);
    }

    private void setNotificationButton(RemoteViews notificationView, @IdRes int viewId,
                                       String action) {
        notificationView.setOnClickPendingIntent(viewId,
                PendingIntent.getBroadcast(this, 1,
                        new Intent(this, Listener.class).setAction(action), 0));
    }

    public void stop() {
        if (DEBUG) Log.i(TAG, "stop() called");

        // If the UI process is still running, don't kill the process, only remove its notification
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos =
                activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals(BuildConfig.APPLICATION_ID)) {
                player.pause();
                stopForeground(true);
                return;
            }
        }

        // If the UI process has already ended, kill the service and close the player
        finish();
    }

    public void finish() {
        if (DEBUG) Log.i(TAG, "finish() called");
        if (!finished) {
            player.finish();
            player = null;
            stopForeground(true);
            instance = null;
            stopSelf();
            finished = true;
        }
    }

    public static class Listener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                if (DEBUG) Log.i(TAG, "Intent received (action = null)");
                return;
            }

            if (DEBUG) Log.i(TAG, "Intent received (action = \"" + intent.getAction() + "\")");

            if (instance == null) {
                if (DEBUG) Log.i(TAG, "Service not initialized");
                return;
            }

            if (instance.player.getNowPlaying() != null) {
                try {
                    instance.player.saveState(intent.getAction());
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    if (DEBUG) e.printStackTrace();
                }
            }

            switch (intent.getAction()) {
                case (ACTION_TOGGLE_PLAY):
                    instance.player.togglePlay();
                    instance.player.updateUi();
                    break;
                case (ACTION_PREV):
                    instance.player.previous();
                    instance.player.updateUi();
                    break;
                case (ACTION_NEXT):
                    instance.player.skip();
                    instance.player.updateUi();
                    break;
                case (ACTION_STOP):
                    instance.stop();
                    if (instance != null) {
                        instance.player.updateUi();
                    }
                    break;
            }
        }
    }

    /**
     * Receives media button presses from in line remotes, input devices, and other sources
     */
    public static class RemoteControlReceiver extends BroadcastReceiver {

        public RemoteControlReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle Media button Intents
            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            instance.player.togglePlay();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                            instance.player.play();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            instance.player.pause();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            instance.player.skip();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            instance.player.previous();
                            break;
                    }
                }
            }
        }
    }

    public static class Stub extends IPlayerService.Stub {

        @Override
        public void stop() throws RemoteException {
            instance.stop();
        }

        @Override
        public void skip() throws RemoteException {
            instance.player.skip();
        }

        @Override
        public void previous() throws RemoteException {
            instance.player.previous();
        }

        @Override
        public void begin() throws RemoteException {
            instance.player.begin();
        }

        @Override
        public void togglePlay() throws RemoteException {
            instance.player.togglePlay();
        }

        @Override
        public void play() throws RemoteException {
            instance.player.play();
        }

        @Override
        public void pause() throws RemoteException {
            instance.player.play();
        }

        @Override
        public void setPrefs(boolean shuffle, int repeat) throws RemoteException {
            instance.player.setPrefs(shuffle, (short) repeat);
        }

        @Override
        public void setQueue(List<Song> newQueue, int newPosition) throws RemoteException {
            instance.player.setQueue(newQueue, newPosition);
        }

        @Override
        public void changeSong(int position) throws RemoteException {
            instance.player.changeSong(position);
        }

        @Override
        public void editQueue(List<Song> newQueue, int newPosition) throws RemoteException {
            instance.player.editQueue(newQueue, newPosition);
        }

        @Override
        public void queueNext(Song song) throws RemoteException {
            instance.player.queueNext(song);
        }

        @Override
        public void queueNextList(List<Song> songs) throws RemoteException {
            instance.player.queueNext(songs);
        }

        @Override
        public void queueLast(Song song) throws RemoteException {
            instance.player.queueLast(song);
        }

        @Override
        public void queueLastList(List<Song> songs) throws RemoteException {
            instance.player.queueLast(songs);
        }

        @Override
        public void seek(int position) throws RemoteException {
            instance.player.seek(position);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return instance.player.isPlaying();
        }

        @Override
        public boolean isPreparing() throws RemoteException {
            return instance.player.isPreparing();
        }

        @Override
        public Song getNowPlaying() throws RemoteException {
            return instance.player.getNowPlaying();
        }

        @Override
        public List<Song> getQueue() throws RemoteException {
            return instance.player.getQueue();
        }

        @Override
        public int getQueuePosition() throws RemoteException {
            return instance.player.getQueuePosition();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return instance.player.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return instance.player.getDuration();
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return instance.player.getAudioSessionId();
        }
    }
}

