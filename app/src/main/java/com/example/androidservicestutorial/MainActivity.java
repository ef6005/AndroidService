package com.example.androidservicestutorial;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView mBoundedTv;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            if (binder instanceof BoundForegroundService.MyBinder) {
                BoundForegroundService boundForegroundService = ((BoundForegroundService.MyBinder) binder).getService();
                boundForegroundService.setBoundServiceListener(counter -> mBoundedTv.setText(String.valueOf(counter)));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void backgroundServiceBtn_onClick(View view) {
        startService(new Intent(this, BackgroundService.class));
    }

    private void foregroundServiceBtn_onClick(View view) {
        startService(new Intent(this, ForegroundService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoundedTv = findViewById(R.id.main_tv_boundedView);
        Button foregroundServicedBtn = findViewById(R.id.main_btn_foregroundService);
        foregroundServicedBtn.setOnClickListener(this::foregroundServiceBtn_onClick);
        Button backgroundBtn = findViewById(R.id.main_btn_backgroundService);
        backgroundBtn.setOnClickListener(this::backgroundServiceBtn_onClick);
        Button boundServiceBtn = findViewById(R.id.main_btn_boundService);
        boundServiceBtn.setOnClickListener(this::boundServiceBtn_onClick);

        //boundService
        if (BoundForegroundService.isRunning())
            bindService(new Intent(this, BoundForegroundService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    private void boundServiceBtn_onClick(View view) {
        startService(new Intent(this, BoundForegroundService.class));
        bindService(new Intent(this, BoundForegroundService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }


}
