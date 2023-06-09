package com.example.project.reg;

import static com.example.project.reg.Valid.isValid;
import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.APP_PREF;
import static com.example.project.server_data.config.APP_PREF_PHONE;
import static com.example.project.server_data.config.DB_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class regUser extends Fragment { //Фрагмент регистрации пользователя
    SharedPreferences mSettings;

    Button reg;
    EditText phone_number;
    EditText password;
    EditText clone_of_password;
    TextView goToLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reg_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSettings = getActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();

        reg = view.findViewById(R.id.reg);
        goToLogin = view.findViewById(R.id.goToLoginFragment);
        phone_number = view.findViewById(R.id.numReg);
        password = view.findViewById(R.id.password);
        clone_of_password = view.findViewById(R.id.password2);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phone_number.getText().toString();
                if (!Patterns.PHONE.matcher(phone).matches()) // провекра соответсвия паттера инпут тайпа на Patterns.PHONE
                    phone_number.setError("Введен не коректный номер");
                else {
                    String password1 = password.getText().toString();
                    String password2 = clone_of_password.getText().toString();
                    if (!isValid(password1).equals("Подходящий пароль"))// провекра соответсвия собственного паттера пароля(isValid())
                        password.setError(isValid(password1));
                    else {
                        if (!password1.equals(password2))
                            clone_of_password.setError("пароли не совпадают");
                        else {
                            Data data = new Data();
                            data.setMode("reg");//ставим мод при котором в теле передаем заполненные данные профлиля
                            data.setNumber(phone);
                            data.setPassword(password1);
                            data.setSecondPassword(password2);
                            Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);//запрос
                            call.enqueue(new Callback<Data>() {
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {//ответ
                                    Log.d("lol", "onResponse");
                                    if (response.isSuccessful()) {
                                        Log.d("lol", "response is successful");
                                        if (response.body().getReg()) { //проверка успешности регестрации
                                            Navigation.findNavController(view).navigate(R.id.action_regUser_to_infoUser);//кладем номер телефона в SharedPreferences
                                            editor.putString(APP_PREF_PHONE, phone);
                                            editor.apply();
                                            Log.d("lol",mSettings.getString(APP_PREF_PHONE, ""));
                                        } else
                                            phone_number.setError("Ваш номер телефона уже зарегистрирован! Попробуйте войти");
                                    } else
                                        Log.d("lol", "response is NOT successful");
                                }
                                @Override
                                public void onFailure(Call<Data> call, Throwable t) {
                                    Log.d("lol", "onFailure");
                                }
                            });
                        }
                    }
                }
            }
        });goToLogin.setOnClickListener(view1 -> Navigation.findNavController(view).navigate(R.id.action_regUser_to_loginUser));
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}