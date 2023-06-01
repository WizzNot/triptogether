package com.example.project.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yandex.mapkit.user_location.UserLocationView;

public interface SelectListenerString {        //Слушатель кликов
    void onItemClicked(String string);

    void onObjectAdded(UserLocationView userLocationView, Context context);

    void onObjectAdded(UserLocationView userLocationView);

    void onDrivingRoutesError(@NonNull Error error);
}
