package com.example.project;

import android.app.Application;
import android.util.Log;

import com.vk.api.sdk.utils.VKUtils;
import com.yandex.mapkit.MapKitFactory;

import java.util.Arrays;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("b6bec48d-523c-4475-8e83-6ea5a93feb61");
    }
}
