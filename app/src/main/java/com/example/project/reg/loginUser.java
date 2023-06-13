package com.example.project.reg;

import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.APP_PREF;
import static com.example.project.server_data.config.APP_PREF_PHONE;
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

public class loginUser extends Fragment { //фрагмент логина пользователя
    SharedPreferences mSettings;
    TextView goToReg;
    Button login;
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
        phone_number = view.findViewById(R.id.numLog);
        password1 = view.findViewById(R.id.passwordLog);
        mSettings = getActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();

        if(mSettings.getString(APP_PREF_PHONE, "").equals("")){//если в SharedPreferences есть сохраненный номер телефона значит уходим в фрагмент search(MainActivity)
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = phone_number.getText().toString();
                    String password = password1.getText().toString();
                    if(phone.equals("") && password.equals("")) phone_number.setError("Введите номер телефона и пароль");//(1)проваерка полей в 1, 2, 3
                    else {
                        if(phone.equals("")) phone_number.setError("Введите номер");//(2)
                        else {
                            if (password.equals("")) password1.setError("Введите пароль");//(3)
                            Data data = new Data();
                            data.setMode("login");//ставим мод при котором в теле передаем телефон и пароль
                            data.setNumber(phone);
                            data.setPassword(password);
                            Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
                            call.enqueue(new Callback<Data>() {//запрос
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {//ответ
                                    Log.d("lol", "onResponse");
                                    if (response.isSuccessful()) {
                                        Log.d("lol", "response is successful");
                                        if (response.body().getLogin()){//если логин прошел успешно то перемещаемся в фрагмент search и в SharedPreferences кладем телефон пользоваетеля
                                            Navigation.findNavController(view).navigate(R.id.action_loginUser_to_mainActivity);
                                            editor.putString(APP_PREF_PHONE, phone);
                                            editor.apply();
                                        }
                                        else phone_number.setError("Неверно указаны логин или пароль");
                                    } else
                                        Log.d("lol", "response is NOT successful");
                                }
                                @Override
                                public void onFailure(Call<Data>call, Throwable t) {

                                }
                            });
                        }
                    }
                }
            });
        }
        else
            Navigation.findNavController(view).navigate(R.id.action_loginUser_to_mainActivity);
        goToReg.setOnClickListener(view1 -> Navigation.findNavController(view).navigate(R.id.action_loginUser_to_regUser));
    }
}