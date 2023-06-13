package com.example.project.main;

import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.DB_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project.R;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.example.project.util.Elements;
import com.example.project.util.RecAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class history extends Fragment {
    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PHONE = "PHONE";
    SharedPreferences mSettings;

    RecyclerView rec;
    RecAdapter adapter;
    Button active;
    Button history;
    String number;
    private final ArrayList<Elements> arr = new ArrayList<>();
    private final ArrayList<Elements> mas = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        number = mSettings.getString(APP_PREFERENCES_PHONE, "");
        rec = view.findViewById(R.id.rec);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        rec.setLayoutManager(mLayoutManager);
        active = view.findViewById(R.id.active);
        history = view.findViewById(R.id.history);
        Log.d("lol", "created");
        Data data = new Data();
        Data dataWalker = new Data();
        data.setNumber(number);
        data.setMode("gettravels");
        dataWalker.setNumber(number);
        dataWalker.setMode("getwalks");
        Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
        Call<Data> callWalker = CreateService(Service.class, DB_URL).give_date(dataWalker);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(response.isSuccessful()) {
                    Log.d("lol", "successful");
                    String[] drivers = response.body().getDriver().split(";");
                    String[] companions = response.body().getCompanion().split(";");
                    String[] frst = response.body().getFirstadress().split(";");
                    String[] scnd = response.body().getSecondadress().split(";");
                    String[] date = response.body().getDate().split(";");
                    String[] price = response.body().getPrice().split(";");
                    Log.d("lol", Integer.toString(drivers.length));
                    if (!drivers[0].equals("")) {
                        for (int i = 0; i < drivers.length; i++) {
                            if (!drivers[i].equals("empty") && !drivers[i].equals(number))
                                mas.add(new Elements(drivers[i], "Цена:" + price[i], frst[i], scnd[i], date[i]));
                        }
                    }
                    if (!companions[0].equals("")) {
                        for (int i = 0; i < companions.length; i++) {
                            if (!companions[i].equals("empty") && !companions[i].equals(number))
                                mas.add(new Elements(companions[i], "Цена:" + price[i], frst[i], scnd[i], date[i]));
                        }
                    }
                }
                else {
                    Log.d("lol", "not successful");}
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("lol", "failure");
            }
        });
        callWalker.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(response.isSuccessful()) {
                    Log.d("lol", "successful");
                    String[] drivers = response.body().getDriver().split(";");
                    String[] companions = response.body().getCompanion().split(";");
                    String[] frst = response.body().getFirstadress().split(";");
                    String[] scnd = response.body().getSecondadress().split(";");
                    String[] date = response.body().getDate().split(";");
                    String[] price = response.body().getPrice().split(";");
                    Log.d("lol", Integer.toString(drivers.length));
                    if (!drivers[0].equals("")) {
                        for (int i = 0; i < drivers.length; i++) {
                            mas.add(new Elements(drivers[i], "Тип:" + price[i], frst[i], scnd[i], date[i]));
                        }
                    }
                    else if (!companions[0].equals("")) {
                        for (int i = 0; i < companions.length; i++) {
                            if (!companions[i].equals("empty") && !companions[i].equals(number))
                                mas.add(new Elements(companions[i], "Цена:" + price[i], frst[i], scnd[i], date[i]));
                        }
                    }
                }
                else {
                    Log.d("lol", "not successful");}
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("lol", "failure");
            }
        });
        Data historydata = new Data();
        historydata.setNumber(number);
        historydata.setMode("gethistory");
        Call<Data> datacall = CreateService(Service.class, DB_URL).give_date(historydata);
        datacall.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(response.isSuccessful()) {
                    Log.d("lol", "successful");
                    String[] drivers = response.body().getDriver().split(";");
                    String[] companions = response.body().getCompanion().split(";");
                    String[] frst = response.body().getFirstadress().split(";");
                    String[] scnd = response.body().getSecondadress().split(";");
                    String[] date = response.body().getDate().split(";");
                    String[] price = response.body().getPrice().split(";");
                    Log.d("lol", Integer.toString(drivers.length));
                    if (!drivers[0].equals("")) {
                        for (int i = 0; i < drivers.length; i++) {
                            if (!drivers[i].equals("empty") && !drivers[i].equals(number))
                                arr.add(new Elements(drivers[i], "Цена:" + price[i], frst[i], scnd[i], date[i]));
                        }
                    }
                    if (!companions[0].equals("")) {
                        for (int i = 0; i < companions.length; i++) {
                            if (!companions[i].equals("empty") && !companions[i].equals(number))
                                arr.add(new Elements(companions[i], "Цена:" + price[i], frst[i], scnd[i], date[i]));
                        }
                    }
                }
                else {
                    Log.d("lol", "not successful");}
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
        adapter = new RecAdapter(mas, getActivity());
        rec.setAdapter(adapter);
        active.setOnClickListener(view1 -> {
            adapter = new RecAdapter(mas, getActivity());
            rec.setAdapter(adapter);
        });
        history.setOnClickListener(view1 -> {
            adapter = new RecAdapter(arr, getActivity());
            rec.setAdapter(adapter);
        });
    }
}