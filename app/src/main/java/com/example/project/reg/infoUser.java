package com.example.project.reg;

import static com.example.project.server_data.Picture_transfer.SavePicture;
import static com.example.project.server_data.Picture_transfer.convert;
import static com.example.project.server_data.Picture_transfer.getBitmap;
import static com.example.project.server_data.Picture_transfer.getBitmapClippedCircle;
import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.APP_LAST_STATUS;
import static com.example.project.server_data.config.APP_PREF;
import static com.example.project.server_data.config.APP_PREF_PHONE;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.project.R;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class infoUser extends Fragment {

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    CircleImageView imageView;
    Uri image;
    Button start;
    EditText nam;
    EditText surnam;
    EditText lolname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_user, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nam = view.findViewById(R.id.name);
        surnam = view.findViewById(R.id.secondName);
        lolname = view.findViewById(R.id.patronymic);

        imageView = view.findViewById(R.id.imageProfile);
        start = view.findViewById(R.id.start);
        mSettings = getActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        editor = mSettings.edit();
        if (mSettings.getString(APP_PREF_PHONE, "").equals(""))
            goToLogin();

        imageView.setOnClickListener(view1 -> loadImage());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ФИО
                String name = nam.getText().toString();
                String surname = surnam.getText().toString();
                String patronymic = lolname.getText().toString();
                if (name.equals(""))
                    nam.setError("Обязательное поле!");
                else {
                    if (surnam.equals(""))
                        surnam.setError("Обязательное поле!");
                    else {
                        if (lolname.equals(""))
                            lolname.setError("Обязательное поле!");
                        else {
                            Data request = new Data();
                            request.setMode("profile");
                            request.setNumber(mSettings.getString(APP_PREF_PHONE, ""));
                            request.setSurname(surname);
                            request.setName(name);
                            request.setPatronymic(patronymic);
                            // Фото
                            imageView.buildDrawingCache();
                            String Base64Str = convert(getBitmapClippedCircle(imageView.getDrawingCache()));
                            request.setPicture(Base64Str);
                            Call<Data> call = CreateService(Service.class, DB_URL).give_date(request);
                            call.enqueue(new Callback<Data>() {
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {
                                    Log.d("lol", "onResponse");
                                    if (response.isSuccessful()) {
                                        Log.d("lol", "response is successful");
                                        goToLogin();
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
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void goToLogin() {
        //todo check data
        Navigation.findNavController(getView()).navigate(R.id.action_infoUser_to_mainActivity);
    }

    private void loadImage() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet2layout);

        Button btGall = dialog.findViewById(R.id.gallery);
        Button btCam = dialog.findViewById(R.id.cam);

            //Фото из галереии
        btGall.setOnClickListener(view -> {
            ImagePicker.with(this).galleryOnly().crop().start();
            dialog.cancel();
        });

            //Фото с камеры
        btCam.setOnClickListener(view -> {
            ImagePicker.with(this).cameraOnly().crop().start();
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        image = data.getData();
        Picasso.get().load(image).into(imageView);
    }
}