package com.example.project.reg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.R;
import com.yandex.mapkit.MapKitFactory;

public class RegActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PASSWORD = "PHONE";
    SharedPreferences mSettings;
    static public int с = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (с==0) {
            MapKitFactory.setApiKey("bdd998b5-582a-4519-ab3f-424df6763579");
            с=1;
        }
        setContentView(R.layout.reg_main);
    }
}