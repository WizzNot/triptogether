package com.example.project.main;

import static com.example.project.server_data.Picture_transfer.convert;
import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.DB_URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.reg.RegActivity;
import com.example.project.reg.Valid;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends Fragment {

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PHONE = "PHONE";
    SharedPreferences mSettings;
    private Button exit;
    private ArrayList<String> arr = new ArrayList<>();
    private SharedPreferences sPref;    //Сохранение данных
    private ImageView galochka;
    private ImageView profile_photo;
    private TextView name;
    private Button vkAuth;
    private ImageButton safebutton;
    private Button SOS;
    private String password1;
    private String password2;
    private Button changeprofilestart;
    private EditText changeprofiletext;
    private TextView changeprofileview;
    private ConstraintLayout changePassword;
    private ConstraintLayout changeName;
    private EditText secondedit;
    private EditText thirdedit;
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
        changePassword = view.findViewById(R.id.changePassword);
        changeName = view.findViewById(R.id.changeName);
        safebutton = view.findViewById(R.id.safety_button);

        safebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog
                        .Builder(getContext())
                        .setTitle("Руководство по безопасности!")
                        .setMessage("Существует 3 вида галочек: серая, синяя, зелёная. Человек с серой галочкой не подтверждал аккаунт и ездить с ним следует на свой страх и риск. Человек с синей галочкой подтвердил аккаунт с помощью вконтакте, но его аккаунт не подтвержден с помощью госуслуг/id аккаунта в банке. Человек с зеленой галочкой подтвердил подлинность своих данных, вы можете ему доверять.")
                        .setPositiveButton("Я понял",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.setCancelable(false);

                if((getActivity() != null) && ! (getActivity().isFinishing())) {
                    if(! dialog.isShowing()) {
                        dialog.show();
                    }
                }
                dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        c = 1;
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        request();
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
        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("sos", Integer.toString(c));
                if (c%3==0){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "112"));
                    startActivity(intent);
                }
                else c++;
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("dfkajsd","fdsiaf");
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.profile_change);
                    changeprofilestart = dialog.findViewById(R.id.changeprofilebut);
                    changeprofiletext = dialog.findViewById(R.id.profilechangeedit3);
                    changeprofileview = dialog.findViewById(R.id.changeprofiletxt);
                    secondedit = dialog.findViewById(R.id.changeprofileedit2);
                    thirdedit = dialog.findViewById(R.id.changeprofileedit);
                    changeprofileview.setText("Изменение пароля");
                    changeprofiletext.setHint("Старый пароль");
                    secondedit.setHint("Новый пароль");
                    thirdedit.setHint("Повторите пароль");
                    secondedit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    thirdedit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    changeprofiletext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    changeprofilestart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("change", "changepass");
                            password1 = secondedit.getText().toString();
                            password2 = thirdedit.getText().toString();
                            if(!password2.equals(password1))
                            {
                                changeprofileview.setText("Пароли не совпадают");
                            }
                            String valid = Valid.isValid(password1);
                            if(!valid.equals("Подходящий пароль"))
                            {
                                changeprofileview.setText(valid);
                            }
                            else{
                                Data data = new Data();
                                data.setMode("changepass");
                                data.setNumber(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                                data.setPassword(password1);
                                data.setSecondPassword(changeprofiletext.getText().toString());

                                Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
                                call.enqueue(new Callback<Data>() {
                                    @Override
                                    public void onResponse(Call<Data> call, Response<Data> response) {
                                        changeprofileview.setText(response.body().getText());
                                    }

                                    @Override
                                    public void onFailure(Call<Data> call, Throwable t) {

                                    }
                                });
                            }
                            //TODO
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                }
            }
        );
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.profile_change);
                changeprofilestart = dialog.findViewById(R.id.changeprofilebut);
                changeprofiletext = dialog.findViewById(R.id.profilechangeedit3);
                changeprofileview = dialog.findViewById(R.id.changeprofiletxt);
                secondedit = dialog.findViewById(R.id.changeprofileedit2);
                thirdedit = dialog.findViewById(R.id.changeprofileedit);
                changeprofileview.setText("Изменение данных");
                changeprofiletext.setHint("Введите имя");
                secondedit.setHint("Введите фамилию");
                thirdedit.setHint("Введите отчество");
                changeprofiletext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                secondedit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                thirdedit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                changeprofilestart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("change", "changename");
                        Data data = new Data();
                        data.setMode("changename");
                        data.setNumber(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                        data.setName(changeprofiletext.getText().toString());
                        data.setSurname(secondedit.getText().toString());
                        data.setPatronymic(thirdedit.getText().toString());
                        Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
                        call.enqueue(new Callback<Data>() {
                            @Override
                            public void onResponse(Call<Data> call, Response<Data> response) {
                                dialog.dismiss();
                            }
                            @Override
                            public void onFailure(Call<Data> call, Throwable t) {
                                Log.d("change", "failure");
                            }
                        });
                        //TODO
                    }
                });
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
    }
    private void request(){
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