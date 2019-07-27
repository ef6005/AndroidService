package com.example.androidservicestutorial;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    private static final String TAG = "hamid";

    //Notification
    NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 5001;
    //for android Oreo and next
    private static final String NOTIFICATION_CHANNEL_ID = "com.example.androidServiceTutorial.foregroundService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        //return value for boundService
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        //Notification
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //for android Oreo and next
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Default", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");


        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Content Title")
                .setContentText("Content Text for Notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    Log.i(TAG, "run: " + i);
                    updateNotification(i);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopForeground(true);
                stopSelf();
            }
        });

        startForeground(NOTIFICATION_ID, notification);
        thread.start();
        return START_NOT_STICKY;
    }

    private void updateNotification(int i) {
        notificationManager.notify(NOTIFICATION_ID
                , new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle("MyTitle")
                        .setContentText("Counter is " + i)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .build());
    }
}
