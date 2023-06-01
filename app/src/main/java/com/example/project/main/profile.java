package com.example.project.main;

import static com.example.project.server_data.Picture_transfer.SavePicture;
import static com.example.project.server_data.Picture_transfer.convert;
import static com.example.project.server_data.Picture_transfer.getBitmap;
import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.DB_URL;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.reg.RegActivity;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.example.project.util.newAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends Fragment {

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PHONE = "PHONE";
    SharedPreferences mSettings;
    Button exit;
    private ArrayList<String> arr = new ArrayList<>();
    private SharedPreferences sPref;    //Сохранение данных
    ImageView galochka;
    ImageView profile_photo;
    TextView name;
    Button vkAuth;
    Button SOS;
    int c = 1;
    char check_vk = '0';


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        galochka = view.findViewById(R.id.Podtverjdenie);
        name = view.findViewById(R.id.NaMe);
        vkAuth = view.findViewById(R.id.vKauth);
        profile_photo = view.findViewById(R.id.photoProfilya);
        SOS = view.findViewById(R.id.SOS);
        exit = view.findViewById(R.id.exit);
        c = 1;
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(APP_PREFERENCES_PHONE, "");
                editor.apply();
                if(mSettings.getString(APP_PREFERENCES_PHONE, "").equals("")){
                    startActivity(new Intent(getContext(), RegActivity.class));
                }
            }
        });
        Mythread mythread = new Mythread();
        mythread.start();
        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("sos", Integer.toString(c));
                if (c%3==0){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "89872584505"));
                    startActivity(intent);
                }
                else c++;
            }
        });
    }
    class Mythread extends Thread{
        @Override
        public void run() {
            Data data = new Data();
            data.setNumber(mSettings.getString(APP_PREFERENCES_PHONE, ""));
            data.setMode("getprofile");
            Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if (response.isSuccessful()) {
                        Log.d("lol", "response is successful");
                        Data resp_Data = response.body();
                        name.setText(resp_Data.getName());
                        profile_photo.setImageBitmap(convert(resp_Data.getPicture()));
                        if (resp_Data.getVerification().equals("1")){
                            check_vk = '1';
                            galochka.setImageResource(R.drawable.verif_vk_tick);
                            vkAuth.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getActivity(), vkAUTH.class));
                                }
                            });
                        } else if (resp_Data.getVerification().equals("0")) {
                            vkAuth.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getActivity(), vkAUTH.class));
                                }
                            });
                            check_vk = '0';
                            galochka.setImageResource(R.drawable.grey_tick);
                        }
                        else{
                            vkAuth.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getActivity(), "Вы уже авторизовались с помощью ВК", Toast.LENGTH_SHORT).show();
                                }
                            });
                            check_vk = '2';
                            galochka.setImageResource(R.drawable.nast_verif_vk_tick);
                        }
                    } else
                        Log.d("lol", "response is NOT successful");
                }
                @Override
                public void onFailure(Call<Data>    call, Throwable t) {
                    Log.d("lol", "onFailure");
                }
            });
        }
    }


    private void loadCars() {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String s = sharedPreferences.getString("key", null);
        if (s != null) {
            Gson gson = new Gson();
            String[] arrString = gson.fromJson(s, String[].class);
            for(String i : arrString){
                arr.add(i);
            }
        }
    }
}