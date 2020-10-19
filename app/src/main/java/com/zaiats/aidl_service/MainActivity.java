package com.zaiats.aidl_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView tv_number;
    private IService iService;

    private ICallback.Stub mCallback = new ICallback.Stub() {
        @Override
        public void onValueChanged(int num) {
            runOnUiThread(() -> {
                tv_number.setText(String.valueOf(num));
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_number = findViewById(R.id.tv_number);

        bindService(new Intent(this, RemoteService.class),
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        iService = IService.Stub.asInterface(service);
                        Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_SHORT).show();
                        try {
                            iService.registerCallback(mCallback);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        try {
                            iService.unregisterCallback(mCallback);
                            Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        if (iService != null) {
            try {
                iService.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}