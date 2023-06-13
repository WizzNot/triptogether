package com.example.project.main;

import static com.example.project.server_data.Picture_transfer.convert;
import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.APP_PREF;
import static com.example.project.server_data.config.APP_PREF_PHONE;
import static com.example.project.server_data.config.DB_URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.example.project.util.Elemensall;
import com.example.project.util.RecAdapterALL;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class allTrips extends Fragment implements SelectListener{
    private static String flag;
    private RecyclerView rec;
    String vkurl = "https://vk.com";
    private Spinner filter;
    private SharedPreferences mSettings;
    private RecAdapterALL adapter;
    private final ArrayList<Elemensall> arr = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View view1 = view;
        mSettings = getActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        filter = view1.findViewById(R.id.changeMode);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arr.clear();
                flag = getResources().getStringArray(R.array.eventType)[i];
                zagrPoezdki(view1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void zagrPoezdki(View view){
        rec = view.findViewById(R.id.recALL);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        rec.setLayoutManager(mLayoutManager);
        Data data = new Data();
        if(flag.equals("попутчик")) data.setMode("finddriver");
        else if(flag.equals("водитель")) data.setMode("findcompanion");
        else if (flag.equals("прогулка")) data.setMode("findwalker");
        else data.setMode("findadventure");
        Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("lol", "onResponse");
                if (response.isSuccessful()) {
                    Log.d("lol", "response is successful");
                    Log.d("lol", flag);
                    if(flag.equals("попутчик")){
                        String[] driversName = response.body().getDriver().split(";");
                        String[] fromAddresses = response.body().getFirstadress().split(";");
                        String[] toAddresses = response.body().getSecondadress().split(";");
                        String[] Prices = response.body().getPrice().split(";");
                        String[] countPass = response.body().getPrice().split(";");
                        String[] verif = response.body().getDriverver().split(";");
                        String[] data = response.body().getDate().split(";");
                        String[] comments = response.body().getText().split(";");
                        for (int i = 0; i < driversName.length; i++) {
                            Log.d("comment", comments[i]);
                            if(!driversName[i].isEmpty())
                            {
                                arr.add(new Elemensall(driversName[i], fromAddresses[i], toAddresses[i], "Цена: " + Prices[i], countPass[i], verif[i], data[i], comments[i]));
                            }
                        }
                        adapter = new RecAdapterALL(arr, getActivity(), allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else if (flag.equals("водитель")){
                        String[] Companions = response.body().getCompanion().split(";");
                        String[] fromAddresses = response.body().getFirstadress().split(";");
                        String[] toAddresses = response.body().getSecondadress().split(";");
                        String[] Prices = response.body().getPrice().split(";");
                        String[] countPass = response.body().getCountpass().split(";");
                        String[] verif = response.body().getCompver().split(";");
                        String[] data = response.body().getDate().split(";");
                        String[] comments = response.body().getText().split(";");
                        System.out.println(Companions.length);
                        for (int i = 0; i < Companions.length; i++) {
                            Log.d("comment", comments[i]);
                            if(!Companions[i].isEmpty()) {
                                arr.add(new Elemensall(Companions[i], fromAddresses[i], toAddresses[i], "Цена: " + Prices[i], countPass[i], verif[i], data[i], comments[i]));
                            }
                        }
                        adapter = new RecAdapterALL(arr, getActivity(), allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else if (flag.equals("прогулка")){
                        Log.d("lol", response.body().getWalker());
                        String[] Walkers = response.body().getWalker().split(";");
                        String[] fromAddresses = response.body().getFirstadress().split(";");
                        String[] toAddresses = response.body().getSecondadress().split(";");
                        String[] Prices = response.body().getPrice().split(";");
                        String[] countPass = response.body().getCountpass().split(";");
                        String[] verif = response.body().getVerification().split(";");
                        String[] data = response.body().getDate().split(";");
                        String[] comments = response.body().getText().split(";");
                        Log.d("asfjd", String.valueOf(comments.length));
                        for (int i = 0; i < Walkers.length; i++) {
                            Log.d("comment", comments[i]);
                            if(!Walkers[i].isEmpty()) {
                                arr.add(new Elemensall(Walkers[i],
                                        fromAddresses[i],
                                        toAddresses[i],
                                        "Тип транспорта: " + Prices[i],
                                        countPass[i],
                                        verif[i],
                                        data[i],
                                        comments[i]));
                            }
                        }
                        adapter = new RecAdapterALL(arr, getActivity(), allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else {
                        Log.d("lol", response.body().getWalker());
                        String[] Walkers = response.body().getWalker().split(";");
                        String[] fromAddresses = response.body().getFirstadress().split(";");
                        String[] toAddresses = response.body().getSecondadress().split(";");
                        String[] Prices = response.body().getPrice().split(";");
                        String[] countPass = response.body().getCountpass().split(";");
                        String[] verif = response.body().getVerification().split(";");
                        String[] data = response.body().getDate().split(";");
                        String[] comments = response.body().getText().split(";");
                        System.out.println(Walkers.length);
                        for (int i = 0; i < Walkers.length; i++) {
                            if(!Walkers[i].isEmpty()) {
                                arr.add(new Elemensall(Walkers[i], fromAddresses[i], toAddresses[i], "Тип транспорта: " + Prices[i], countPass[i], verif[i], data[i], comments[i]));
                            }
                        }
                        adapter = new RecAdapterALL(arr, getActivity(), allTrips.this);
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
    public void onItemClicked(Elemensall element) {
        Log.d("rectest", "click");
        vkurl = "https://vk.com";
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_alltrips);
        ImageView profilephoto = dialog.findViewById(R.id.pictureprof);
        TextView fio = dialog.findViewById(R.id.namesurname);
        TextView tphnumber = dialog.findViewById(R.id.tphnumber);
        TextView frompoint = dialog.findViewById(R.id.frompoi);
        TextView topoint = dialog.findViewById(R.id.topoi);
        TextView date = dialog.findViewById(R.id.date);
        TextView price = dialog.findViewById(R.id.price);
        TextView comment = dialog.findViewById(R.id.comment);
        ImageView podtv = dialog.findViewById(R.id.podtvpic);
        Button vkbutton = dialog.findViewById(R.id.vkbutton);
        Button addtrip = dialog.findViewById(R.id.addbut);
        Button tocontact = dialog.findViewById(R.id.addtocontbut);
        addtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data = new Data();
                Log.d("flaggg", flag);
                if(flag.equals("водитель"))
                {
                    data.setMode("putdriver");
                    data.setDriver(mSettings.getString(APP_PREF_PHONE, ""));
                    data.setCompanion(element.getDriversName());
                }
                else if (flag.equals("попутчик")){
                    data.setMode("putcompanion");
                    data.setCompanion(mSettings.getString(APP_PREF_PHONE, ""));
                    data.setDriver(element.getDriversName());
                }
                else if (flag.equals("прогулка")){
                    data.setMode("makewalk");
                    data.setWalker(element.getDriversName());
                    data.setNumber(mSettings.getString(APP_PREF_PHONE, ""));
                }
                else{
                    data.setMode("makeadventure");
                    data.setWalker(element.getDriversName());
                    data.setNumber(mSettings.getString(APP_PREF_PHONE, ""));
                }
                data.setFirstadress(element.getFromAddresses());
                data.setSecondadress(element.getToAddresses());
                data.setPrice(element.getPrices().substring(element.getPrices().indexOf(":") + 2, element.getPrices().length()));
                data.setDate(element.getData());
                data.setCountpass(element.getCountPass());
                Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
                call.enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Log.d("lol", "SUCC");
                        Toast.makeText(getContext(), "Успешно", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

            }
        });
        vkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("vkbutton", "click");
                try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vkurl));
                startActivity(browserIntent);}
                catch(Exception e)
                {
                    Log.d("vkbutton", "not_auth");
                    Toast.makeText(dialog.getContext(), "Пользователь не авторизован с помощью ВКонтакте!", Toast.LENGTH_LONG).show();
                }
            }
        });
        tocontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + element.getDriversName()));
                startActivity(intent);
            }
        });
        fio.setText(" "); //TODO
        tphnumber.setText(element.getDriversName());
        frompoint.setText(element.getFromAddresses());
        topoint.setText(element.getToAddresses());
        date.setText(element.getData());
        price.setText(element.getPrices());
        comment.setText(element.getComment());
        profilephoto.setImageResource(R.drawable.profile);
        String verif = element.getVerification();

        if(verif.equals("0"))
        {
            podtv.setImageResource(R.drawable.grey_tick);
        }
        else if(verif.equals("1"))
        {
            podtv.setImageResource(R.drawable.verif_vk_tick);
        }
        else{
            podtv.setImageResource(R.drawable.nast_verif_vk_tick);
        }
        Data data = new Data();
        data.setNumber(element.getDriversName());
        data.setMode("getprofile");
        Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data resp_Data = response.body();
                fio.setText(resp_Data.getSurname() + " " + resp_Data.getName() + " " + resp_Data.getPatronymic());
                profilephoto.setImageBitmap(convert(resp_Data.getPicture()));
                vkurl = resp_Data.getVkAccessToken();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}