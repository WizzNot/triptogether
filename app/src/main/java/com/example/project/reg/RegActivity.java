package com.example.project.reg;

import static com.example.project.server_data.config.APP_LAST_STATUS;
import static com.example.project.server_data.config.APP_PREF;
import static com.example.project.server_data.config.APP_PREF_PHONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.project.R;
import com.example.project.main.MainActivity;
import com.yandex.mapkit.MapKitFactory;

public class RegActivity extends AppCompatActivity {//активити для фрагментов пакета reg
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_main);
        mSettings = RegActivity.this.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
    }//защита от нажатий назад

    @Override
    protected void onResume() {
        super.onResume();
        if (!mSettings.getString(APP_PREF_PHONE, "").equals("")){
            startActivity(new Intent(RegActivity.this, MainActivity.class));
        }
    }
}