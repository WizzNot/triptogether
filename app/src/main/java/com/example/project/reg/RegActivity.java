package com.example.project.reg;

import static com.example.project.server_data.config.APP_LAST_STATUS;
import static com.example.project.server_data.config.APP_PREF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.project.R;
import com.example.project.main.MainActivity;
import com.yandex.mapkit.MapKitFactory;

public class RegActivity extends AppCompatActivity {
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_main);
        mSettings = RegActivity.this.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        editor = mSettings.edit();
    }

    @Override
    public void onBackPressed() {
        Log.d("leavehint", "onBackPressed");
    }
    @Override
    protected void onUserLeaveHint() {
        Log.d("leavehint", "onUserLeaveHint");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("leavehint", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("leavehint", "onRestart");
        Log.d("leavehint", mSettings.getString(APP_LAST_STATUS, ""));
        if (mSettings.getString(APP_LAST_STATUS, "").equals("1")){
            editor.putString(APP_LAST_STATUS, " ");
            editor.apply();
            startActivity(new Intent(RegActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("leavehint", "onResume");
        Log.d("leavehint", mSettings.getString(APP_LAST_STATUS, ""));
        if (mSettings.getString(APP_LAST_STATUS, "").equals("1")){
            editor.putString(APP_LAST_STATUS, " ");
            editor.apply();
            startActivity(new Intent(RegActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("leavehint", "destroy");
    }
}