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
        return iServiceBinder;
    }


     IService.Stub iServiceBinder = new IService.Stub() {

         @Override
         public int getNumber() throws RemoteException {
             Random random = new Random();
             return random.nextInt(100);
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

}
