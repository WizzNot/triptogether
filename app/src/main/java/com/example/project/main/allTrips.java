package com.example.project.main;

import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.DB_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.project.R;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.example.project.util.Elemensall;
import com.example.project.util.Elements;
import com.example.project.util.RecAdapterALL;

import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class allTrips extends AppCompatActivity implements SelectListener{

    public static String flag;
    RecyclerView rec;
    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PHONE = "PHONE";
    SharedPreferences mSettings;
    RecAdapterALL adapter;
    private final ArrayList<Elemensall> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trips);
        Intent i = getIntent();
        i.getStringExtra("flag");
        flag = i.getStringExtra("flag");
        mSettings = allTrips.this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        rec = findViewById(R.id.recALL);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(allTrips.this, 1);
        rec.setLayoutManager(mLayoutManager);
        Data data = new Data();
        if(flag.equals("true")) data.setMode("finddriver");
        else if(flag.equals("flase")) data.setMode("findcompanion");
        else data.setMode("findwalker");
        Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("lol", "onResponse");
                if (response.isSuccessful()) {
                    Log.d("lol", "response is successful");
                    if (flag.equals("true")){
                        String[] driversName = response.body().getDriver().split(";");
                        String[] fromAddresses = response.body().getFirstadress().split(";");
                        String[] toAddresses = response.body().getSecondadress().split(";");
                        String[] Prices = response.body().getPrice().split(";");
                        String[] countPass = response.body().getPrice().split(";");
                        String[] verif = response.body().getDriverver().split(";");
                        String[] data = response.body().getDate().split(";");
                        for (int i = 0; i < driversName.length; i++) {
                            arr.add(new Elemensall(driversName[i], fromAddresses[i], toAddresses[i], Prices[i], countPass[i], verif[i], data[i]));
                        }
                        adapter = new RecAdapterALL(arr, allTrips.this, allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else if (flag.equals("flase")){
                        String[] Companions = response.body().getCompanion().split(";");
                        String[] fromAddresses = response.body().getFirstadress().split(";");
                        String[] toAddresses = response.body().getSecondadress().split(";");
                        String[] Prices = response.body().getPrice().split(";");
                        String[] countPass = response.body().getCountpass().split(";");
                        String[] verif = response.body().getCompver().split(";");
                        String[] data = response.body().getDate().split(";");
                        System.out.println(Companions.length);
                        for (int i = 0; i < Companions.length; i++) {
                            arr.add(new Elemensall(Companions[i], fromAddresses[i], toAddresses[i], Prices[i], countPass[i], verif[i], data[i]));
                        }
                        adapter = new RecAdapterALL(arr, allTrips.this, allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else {
                        String[] Walkers = response.body().getWalker().split(";");
                        String[] fromAddresses = response.body().getFirstadress().split(";");
                        String[] toAddresses = response.body().getSecondadress().split(";");
                        String[] Prices = response.body().getPrice().split(";");
                        String[] countPass = response.body().getCountpass().split(";");
                        String[] verif = response.body().getVerification().split(";");
                        String[] data = response.body().getDate().split(";");
                        System.out.println(Walkers.length);
                        for (int i = 0; i < Walkers.length; i++) {
                            arr.add(new Elemensall(Walkers[i], fromAddresses[i], toAddresses[i], Prices[i], countPass[i], verif[i], data[i]));
                        }
                        adapter = new RecAdapterALL(arr, allTrips.this, allTrips.this);
                        rec.setAdapter(adapter);
                    }
                } else
                    Log.d("lol", "response is NOT successful");
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("lol", "onFailure");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    class MyThread extends Thread {
        Elemensall element;
        MyThread(Elemensall e)
        {
            this.element = e;
        }
        @Override
        public void run() {

            Data data = new Data();
            Log.d("flaggg", flag);
            if(flag.equals("flase"))
            {
                data.setMode("putdriver");
                data.setDriver(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                data.setCompanion(element.getDriversName());
            }
            else if (flag.equals("true")){
                data.setMode("putcompanion");
                data.setCompanion(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                data.setDriver(element.getDriversName());
            }
            else {
                data.setMode("makewalk");
                data.setWalker(element.getDriversName());
                data.setNumber(mSettings.getString(APP_PREFERENCES_PHONE, ""));
            }
            data.setFirstadress(element.getFromAddresses());
            data.setSecondadress(element.getToAddresses());
            data.setPrice(element.getPrices());
            data.setDate(element.getData());
            data.setCountpass(element.getCountPass());
            Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    Log.d("lol", "SUCC");
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.d("lol", "NOT SUCC");
                }
            });
        }
    }

    @Override
    public void onItemClicked(Elemensall element) {
        Log.d("rectest", "click");
        MyThread thr = new MyThread(element);
        thr.start();
    }
}