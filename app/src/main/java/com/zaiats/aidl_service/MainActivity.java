package com.zaiats.aidl_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_number;
    private Button buttonregister;
    private Button buttonunregister;

    private String tag = "qwe";
    private IService iService;
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            iService = IService.Stub.asInterface(service);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            iService = null;
//        }
//    };


    private ICallback.Stub mCallback = new ICallback.Stub() {
        @Override
        public void onValueChanged(int number) throws RemoteException {
            runOnUiThread(() -> {
                tv_number.setText(number);
                Log.i(tag, String.valueOf(number));
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_number = findViewById(R.id.tv_number);
//        bindService();
        buttonregister = findViewById(R.id.buttonregister);
        buttonunregister = findViewById(R.id.buttonunregister);
        
                bindService(new Intent(this, RemoteService.class),
                        new ServiceConnection() {
                            @Override
                            public void onServiceConnected(ComponentName name, IBinder service) {
                                iService = IService.Stub.asInterface(service);
                                try {
                                    iService.registerCallback(mCallback);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onServiceDisconnected(ComponentName name) {

                            }
                        }, BIND_AUTO_CREATE);




//            buttonregister.setOnClickListener( v -> {
//                try {
//                    iService.registerCallback(new ICallback.Stub() {
//                        @Override
//                        public void onValueChanged(int number) throws RemoteException {
//                            tv_number.setText(number);
//                        }
//                    });
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            buttonunregister.setOnClickListener( v -> {
//                try {
//                    iService.unregisterCallback(new ICallback.Stub() {
//                        @Override
//                        public void onValueChanged(int number) throws RemoteException {
//                            tv_number.setText("Disconnected");
//                        }
//                    });
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            });




    }

//    private void bindService() {
//        Intent intent = new Intent();
//        intent.setAction("com.zaiats.aidl_service.RemoteService");
//        intent.setPackage("com.zaiats.aidl_service");
//        boolean success = bindService(intent, connection, Context.BIND_AUTO_CREATE);
//        if (success) {
//            Log.i("test", "bindService OK");
//        } else {
//            Log.i("test", "bindService Fail");
//        }
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unBindService();
//    }

//    private void unBindService() {
//        try {
//            unbindService(connection);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}