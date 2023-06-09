package com.example.project.main;

import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.DB_URL;
import static com.example.project.server_data.config.VK_API_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.example.project.server_data.VkResponse;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vkAUTH extends Activity { //активит в котором запускается активити для авторизации Вконтакте
    Button vkAuth;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PHONE = "PHONE";
    SharedPreferences mSettings;
    String auth_index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth_index = getIntent().getStringExtra("vklol");
        ThreadRequestVk threadRequestVk = new ThreadRequestVk();
        threadRequestVk.start();
        mSettings = vkAUTH.this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() { //Обработчик результата активности вохода
            @Override
            public void onLogin(@NonNull VKAccessToken vkAccessToken) { //если авторизация прошла успешно, то получаем access token по которому в дольнейшем сможем получать данные из аккаунта в ВК
                To_verif to_verif = new To_verif(vkAccessToken, mSettings.edit());
                to_verif.start(); //запускаем проверку верификации пользователся и последующую загрузку данных на сервер
                startActivity(new Intent(vkAUTH.this, MainActivity.class));
            }
            @Override
            public void onLoginFailed(int i) { //если авторизация прошла неуспешно(пользователь вышел с окна авторизации или нажал не разрешить)
                startActivity(new Intent(vkAUTH.this, MainActivity.class));
            }
        })) super.onActivityResult(requestCode, resultCode, data);
    }
    class ThreadRequestVk extends Thread{
        @Override
        public void run() {
            VK.login(vkAUTH.this);
        }
    }
    class To_verif extends Thread{ //поток с запуском авторизации
        VKAccessToken vkAccessToken;
        SharedPreferences.Editor editor;
        To_verif(VKAccessToken vkAccessToken, SharedPreferences.Editor editor){
            this.vkAccessToken = vkAccessToken;
            this.editor = editor;
        }

        @Override
        public void run() {
            Call<VkResponse> call = CreateService(Service.class, VK_API_URL).vkRequest("users.get",
                    vkAccessToken.getAccessToken(), "oauth_verification", "5.204");//проверяем поле "oauth_verification" если пусто то пользователь не верефицирован
            call.enqueue(new Callback<VkResponse>() {
                @Override
                public void onResponse(Call<VkResponse> call, Response<VkResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getResponse().get(0).getOauth_verification().isEmpty()){//проверяем поле "oauth_verification" если пусто то пользователь не верефицирован, инче верефикация есть
                            Data data = new Data();
                            data.setMode("verification");//ставим мод для передачи  ссылки на вк
                            data.setVkAccessToken("https://vk.com/id" + Integer.toString(vkAccessToken.getUserId()));
                            if (auth_index != null) {
                                data.setNumber(vkAccessToken.getAccessToken());
                                editor.putString(APP_PREFERENCES_PHONE, vkAccessToken.getAccessToken());
                                editor.apply();
                            } //передаем ссылку на страницу в вк по vkID, полученный по access token
                            else data.setNumber("empty");
                            data.setNumber(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                            Call<Data> call1 = CreateService(Service.class, DB_URL).give_date(data);
                            call1.enqueue(new Callback<Data>() {
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {
                                    if (response.isSuccessful()) {
                                    } else Log.d("verification_1", "response is NOT successful");
                                }
                                @Override
                                public void onFailure(Call<Data>call, Throwable t) {
                                    Log.d("verification_1", "onFailure");
                                }
                            });
                        } else {
                            Data data = new Data();
                            data.setMode("verification");
                            Log.d("verification_1", "thread_run");
                            if (auth_index != null) {
                                data.setNumber(vkAccessToken.getAccessToken());
                                editor.putString(APP_PREFERENCES_PHONE, vkAccessToken.getAccessToken());
                                editor.apply();
                            }
                            else data.setNumber("empty");
                            data.setMode("trueverification");//ставим мод если у человек есть верефикация
                            data.setVkAccessToken("https://vk.com/id" + Integer.toString(vkAccessToken.getUserId()));
                            Log.d("verification_1", mSettings.getString(APP_PREFERENCES_PHONE, ""));
                            data.setNumber(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                            Data trueverif = new Data();
                            trueverif.setMode("verification");
                            trueverif.setVkAccessToken("https://vk.com/id" + Integer.toString(vkAccessToken.getUserId()));
                            trueverif.setNumber(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                            Call<Data> trueverifdata = CreateService(Service.class, DB_URL).give_date(trueverif);
                            trueverifdata.enqueue(new Callback<Data>() { // запрос на сервер для зеленой галочки
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {
                                    Log.d("true_verification", "success");
                                }

                                @Override
                                public void onFailure(Call<Data> call, Throwable t) {

                                }
                            });
                            Call<Data> call1 = CreateService(Service.class, DB_URL).give_date(data);
                            call1.enqueue(new Callback<Data>() {
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("verification_1", "response is successful");
                                    } else Log.d("verification_1", "response is NOT successful");
                                }
                                @Override
                                public void onFailure(Call<Data>call, Throwable t) {
                                    Log.d("verification_1", "onFailure");
                                }
                            });
                        }
                        Log.d("verification_2", response.body().toString());
                    } else Log.d("verification_2", "response is NOT successful");
                }
                @Override
                public void onFailure(Call<VkResponse>call, Throwable t) {
                    Log.d("verification_2", "onFailure");
                }
            });
        }
    }
}















