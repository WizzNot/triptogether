package com.example.project.reg;

import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.DB_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.main.vkAUTH;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginUser extends Fragment {

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PHONE = "PHONE";
    SharedPreferences mSettings;
    TextView goToReg;
    Button login;
    Button loginVK;
    EditText phone_number;
    EditText password1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goToReg = view.findViewById(R.id.goToRegFragment);
        login = view.findViewById(R.id.loginLog);
        //loginVK = view.findViewById(R.id.loginVK);
        phone_number = view.findViewById(R.id.numLog);
        password1 = view.findViewById(R.id.passwordLog);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();

        if(mSettings.getString(APP_PREFERENCES_PHONE, "").equals("")){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = phone_number.getText().toString();
                    String password = password1.getText().toString();
                    if(phone.equals("") && password.equals("")) phone_number.setError("Введите номер телефона и пароль");
                    else {
                        if(phone.equals("")) phone_number.setError("Введите номер");
                        else {
                            if (password.equals("")) password1.setError("Введите пароль");
                            Data data = new Data();
                            data.setMode("login");
                            data.setNumber(phone);
                            data.setPassword(password);
                            Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
                            call.enqueue(new Callback<Data>() {
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {
                                    Log.d("lol", "onResponse");
                                    if (response.isSuccessful()) {
                                        Log.d("lol", "response is successful");
                                        if (response.body().getLogin()){
                                            Navigation.findNavController(view).navigate(R.id.action_loginUser_to_mainActivity);
                                            editor.putString(APP_PREFERENCES_PHONE, phone);
                                            editor.apply();
                                        }
                                        else phone_number.setError("Неверно указаны логин или пароль");
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
                }
            });
//            loginVK.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(getActivity(), vkAUTH.class);
//                    i.putExtra("vklol", "1");
//                    startActivity(i);
//                }
//            });
        }
        else
            Navigation.findNavController(view).navigate(R.id.action_loginUser_to_mainActivity);
        goToReg.setOnClickListener(view1 -> Navigation.findNavController(view).navigate(R.id.action_loginUser_to_regUser));
    }
}