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

public class allTrips extends Fragment implements SelectListener{//фрагмент просмотра всех поездок
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
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//фильтр поездок
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
    private void zagrPoezdki(View view){//запрос поездок
        rec = view.findViewById(R.id.recALL);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        rec.setLayoutManager(mLayoutManager);
        Data data = new Data();
        //на каждый из этих mode сервер выдает поездки так что в каждом поле данные поездок разделяются ";"
        if(flag.equals("попутчик")) data.setMode("finddriver");//если выбран попутчик то ставим мод при котором "ищем" события от попутчиков
        else if(flag.equals("водитель")) data.setMode("findcompanion");//если выбран водитель то ставим мод при котором "ищем" события от водителей
        else if (flag.equals("прогулка")) data.setMode("findwalker");//если выбрана прогулка то ставим мод при котором "ищем" события типа прогулки
        else data.setMode("findadventure");//если выбрана прогулка то ставим мод при котором "ищем" события типа путешествие
        Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
        call.enqueue(new Callback<Data>() {//запрос
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {//ответ
                Log.d("lol", "onResponse");
                if (response.isSuccessful()) {
                    Log.d("lol", "response is successful");
                    Log.d("lol", flag);
                    if(flag.equals("попутчик")){//если фильтр попутчик
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
                            if(!driversName[i].isEmpty()) {//проверка на пустоту что бы не высвечивалась пустая cardView
                                arr.add(new Elemensall(driversName[i], fromAddresses[i], toAddresses[i], "Цена: " + Prices[i], countPass[i], verif[i], data[i], comments[i]));
                            }
                        }
                        adapter = new RecAdapterALL(arr, getActivity(), allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else if (flag.equals("водитель")){//если фильтр водитель
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
                            if(!Companions[i].isEmpty()) {//проверка на пустоту что бы не высвечивалась пустая cardView
                                arr.add(new Elemensall(Companions[i], fromAddresses[i], toAddresses[i], "Цена: " + Prices[i], countPass[i], verif[i], data[i], comments[i]));
                            }
                        }
                        adapter = new RecAdapterALL(arr, getActivity(), allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else if (flag.equals("прогулка")){//если фильтр прогулка
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
                            if(!Walkers[i].isEmpty()) {//проверка на пустоту что бы не высвечивалась пустая cardView
                                arr.add(new Elemensall(Walkers[i], fromAddresses[i], toAddresses[i], "Тип транспорта: " + Prices[i], countPass[i], verif[i], data[i], comments[i]));
                            }
                        }
                        adapter = new RecAdapterALL(arr, getActivity(), allTrips.this);
                        rec.setAdapter(adapter);
                    }
                    else {//фильтр если путешествие
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
                            if(!Walkers[i].isEmpty()) {//проверка на пустоту что бы не высвечивалась пустая cardView
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
    public void onItemClicked(Elemensall element) { // Слушатель на элементы RecyclerView
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
        addtrip.setOnClickListener(new View.OnClickListener() {//добавление поездки в запланированные
            @Override
            public void onClick(View view) {
                Data data = new Data();
                Log.d("flaggg", flag);
                if(flag.equals("водитель"))
                {
                    data.setMode("putdriver");//ставим мод для запроса при котором в запланированные попадет событие от водителя
                    data.setDriver(mSettings.getString(APP_PREF_PHONE, ""));
                    data.setCompanion(element.getDriversName());
                }
                else if (flag.equals("попутчик")){
                    data.setMode("putcompanion");//ставим мод для запроса при котором в запланированные попадет событие от попутчика
                    data.setCompanion(mSettings.getString(APP_PREF_PHONE, ""));
                    data.setDriver(element.getDriversName());
                }
                else if (flag.equals("прогулка")){
                    data.setMode("makewalk");//ставим мод для запроса при котором в запланированные попадет прогулка
                    data.setWalker(element.getDriversName());
                    data.setNumber(mSettings.getString(APP_PREF_PHONE, ""));
                }
                else{
                    data.setMode("makeadventure");//ставим мод для запроса при котором в запланированные попадет путешествие
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
        vkbutton.setOnClickListener(new View.OnClickListener() {//интетент в браузер если пользоваетль имеет вк
            @Override
            public void onClick(View view) {
                Log.d("vkbutton", "click");
                try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vkurl));
                startActivity(browserIntent);
                } catch(Exception e) {
                    Toast.makeText(dialog.getContext(), "Пользователь не авторизован с помощью ВКонтакте!", Toast.LENGTH_LONG).show();
                }
            }
        });
        tocontact.setOnClickListener(new View.OnClickListener() {//интент в телефенную книгу с целью звонка или сохранения номера
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + element.getDriversName()));
                startActivity(intent);
            }
        });
        fio.setText(" "); //TODO
        //заполнение данных карточки более подробной информации о событии
        tphnumber.setText(element.getDriversName());
        frompoint.setText(element.getFromAddresses());
        topoint.setText(element.getToAddresses());
        date.setText(element.getData());
        price.setText(element.getPrices());
        comment.setText(element.getComment());
        profilephoto.setImageResource(R.drawable.profile);
        String verif = element.getVerification();

        //ставим картинку в imageView в зависимости от подтверждения профиля
        if(verif.equals("0")) {
            podtv.setImageResource(R.drawable.grey_tick);//профиль ни как не подтвержден
        }
        else if(verif.equals("1")) {
            podtv.setImageResource(R.drawable.verif_vk_tick);//профиль аваторизация в ВК
        }
        else{
            podtv.setImageResource(R.drawable.nast_verif_vk_tick);//профиль есть верификация в ВК
        }
        Data data = new Data();
        data.setNumber(element.getDriversName());
        data.setMode("getprofile");//ставим мод на получения профиля
        Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
        call.enqueue(new Callback<Data>() {//запрос
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {//ответ
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