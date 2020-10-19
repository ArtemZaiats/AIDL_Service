package com.zaiats.aidl_service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class RemoteService extends Service {

    private RemoteCallbackList<ICallback> mCallbacks = new RemoteCallbackList<>();

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iServiceBinder;
    }

     IService.Stub iServiceBinder = new IService.Stub() {

         @Override
        public void registerCallback(ICallback callback) {
            if (callback != null && mCallbacks != null) {
                mCallbacks.register(callback);
            }
        }

        @Override
        public void unregisterCallback(ICallback callback) {
            if (callback != null && mCallbacks != null) {
                mCallbacks.unregister(callback);
            }
        }
    };

    @Override
    public void onDestroy() {
        mCallbacks.kill();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    number();
                });
            }
        };
        timer.schedule(timerTask, 0, 5000);
        super.onCreate();
    }

    private void number() {
        Random random = new Random();
        int n = mCallbacks.beginBroadcast();
        try {
            for (int i = 0; i < n; i++) {
                mCallbacks.getBroadcastItem(i).onValueChanged(random.nextInt(100));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mCallbacks.finishBroadcast();
    }

}
