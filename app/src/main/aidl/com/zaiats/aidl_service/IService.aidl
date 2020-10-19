// IService.aidl
package com.zaiats.aidl_service;

import com.zaiats.aidl_service.ICallback;

// Declare any non-default types here with import statements

interface IService {

    int getNumber();
    void registerCallback(ICallback cb);
    void unregisterCallback(ICallback cb);

}