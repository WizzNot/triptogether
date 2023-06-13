package com.example.project.main;


import static com.example.project.server_data.config.APP_LAST_STATUS;
import static com.example.project.server_data.config.APP_PREF;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().popBackStack();
        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.history, R.id.search, R.id.profile, R.id.allTrips).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);

        mSettings = MainActivity.this.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        editor = mSettings.edit();
    }

    @Override
    public void onBackPressed() {
        Log.d("backpressed", "lol");
    }
    @Override
    protected void onUserLeaveHint() {
        Log.d("leavehintM", "lol");
    }

    @Override
    protected void onDestroy() {
        editor.putString(APP_LAST_STATUS, "1");
        editor.apply();

        super.onDestroy();
        Log.d("leavehintM", "destroy");
    }
}