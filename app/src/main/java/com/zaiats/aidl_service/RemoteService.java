package com.zaiats.aidl_service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;


import java.util.Random;


public class RemoteService extends Service {

    private static final String TAG = RemoteService.class.getSimpleName();

    private RemoteCallbackList<ICallback> mCallbacks = new RemoteCallbackList<>();

    public RemoteService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

     IService.Stub binder = new IService.Stub() {

        @Override
        public boolean isValueChanged() throws RemoteException {

            return false;
        }

        @Override
        public void registerCallback(ICallback callback) throws RemoteException {
            if (callback != null && mCallbacks != null) {
                mCallbacks.register(callback);
            }
        }

        @Override
        public void unregisterCallback(ICallback callback) throws RemoteException {
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

    private void callBack() {
        if (mCallbacks == null) {
            return;
        }
        Random random = new Random();
        mCallbacks.beginBroadcast();
        Handler handler = new Handler();
        for (int i = 0; i < mCallbacks.getRegisteredCallbackCount(); i++) {
            int finalI = i;
            int finalI1 = i;
            handler.postDelayed(() -> {
                    ICallback cb = mCallbacks.getBroadcastItem(finalI1);
                try {
                    cb.onValueChanged(random.nextInt() + 100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }, 5000);
        }
        mCallbacks.finishBroadcast();
    }

}
