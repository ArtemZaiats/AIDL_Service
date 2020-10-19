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

    private String tag = "qwe";
    private IService iService;

    private Timer timer;
    private TimerTask timerTask;

    private ICallback.Stub mCallback = new ICallback.Stub() {
        @Override
        public int onValueChanged() throws RemoteException {
            return 0;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_number = findViewById(R.id.tv_number);
        Handler handler = new Handler();
        timer = new Timer();

        bindService(new Intent(this, RemoteService.class),
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        iService = IService.Stub.asInterface(service);
                        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                        try {
                            iService.registerCallback(mCallback);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(() -> {
                                        try {
                                            tv_number.setText(String.valueOf(iService.getNumber()));
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            };
                            timer.schedule(timerTask, 0, 5000);

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
}