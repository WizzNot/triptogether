package com.example.project.main;

import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.DB_URL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.se.omapi.Session;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.example.project.util.SelectListenerString;
import com.example.project.util.carsAdapter;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class search extends Fragment implements SelectListenerString ,DrivingSession.DrivingRouteListener{
    private AppBarConfiguration mAppBarConfiguration;
    private SelectListenerString selectListenerString;
    private static Point currentPoint;
    private static Point destinationPoint;
    private PlacemarkMapObject placemark;

    private MapKit mapKit;
    private  boolean PlaceListen =false;

    private PlacemarkMapObject currentPointPlacemark = null;
    private PlacemarkMapObject destinationPointPlacemark = null;
    private MapObjectCollection mapObjects;

    private Session searchSession;
    private Point SCREEN_CENTER;
    private DrivingRouter drivingRouter;
    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PHONE = "PHONE";
    SharedPreferences mSettings;
    MapView mapView;
    RecyclerView recyclerView;
    SearchManager searchManager;
    UserLocationLayer locationmapkit;
    private DrivingSession drivingSession;
    Button user;
    Button driver;
    Button walker;
    Button add;
    Button all;
    Dialog dialog;
    EditText frompo;
    EditText onepoi;
    EditText number;
    EditText cost;


    String flag = "true";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MapKitFactory.initialize(getContext());
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        onepoi = view.findViewById(R.id.onePoint);
        walker = view.findViewById(R.id.walker);
        number = view.findViewById(R.id.num);
        cost = view.findViewById(R.id.cost);
        frompo = view.findViewById(R.id.Frompoint);
        mapView = view.findViewById(R.id.mapview);
        user = view.findViewById(R.id.user);
        driver = view.findViewById(R.id.driver);
        add = view.findViewById(R.id.add);
        all = view.findViewById(R.id.all);
        Point point = new Point();


        mapView.getMap().move(new CameraPosition(new Point(55.660888, 37.476278),14.0f,0.0f,0.0f), new Animation(Animation.Type.SMOOTH, 0),null);
        mapView.getMap().getMapObjects().addPlacemark(new Point(55.660888, 37.476278),ImageProvider.fromBitmap(drawSimpleBitmap()));
        currentPoint =new Point(55.660888, 37.476278);
        String url1 = "http://maps.google.com/maps/api/geocode/json?latlng=" +
                55.660888 + "," + 37.476278 + "&sensor=true&language=ru";
        String url2;
        InputListener inputListener = new InputListener() {
            @Override
            public void onMapTap(@NonNull Map map, @NonNull Point point) {
                Log.d("MEM123", "omMapTap");
                if(PlaceListen){
                    mapView.getMap().getMapObjects().remove(placemark);
                    placemark = mapView.getMap().getMapObjects().addPlacemark(new Point(point.getLatitude(), point.getLongitude()),ImageProvider.fromBitmap(drawSimpleBitmap()));
                    Log.d("MEM123", "ifTRUE");
                }
                else{
                    placemark = mapView.getMap().getMapObjects().addPlacemark(new Point(point.getLatitude(), point.getLongitude()),ImageProvider.fromBitmap(drawSimpleBitmap()));
                    PlaceListen =true;
                    Log.d("MEM123", "ifFALSE");
                }
                destinationPoint = new Point(point.getLatitude(), point.getLongitude());
                String url2 = "http://maps.google.com/maps/api/geocode/json?latlng=" +
                        point.getLatitude() + "," + point.getLongitude() + "&sensor=true&language=ru";
            }

            @Override
            public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

            }
        };

        Log.d("MEM123", "add");
        mapView.getMap().addInputListener(inputListener);
        Log.d("MEM123", "mem");

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "false";
                cost.setHint("Цена");
                cost.setInputType(3);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "true";
                cost.setHint("Цена");
                cost.setInputType(3);
            }
        });
        walker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "walker";
                cost.setHint("Тип передвижения");
                cost.setInputType(1);
            }
        });

        add.setOnClickListener(view1 -> {
            new SingleDateAndTimePickerDialog.Builder(getContext())
                    .bottomSheet()
                    .curved()
                    .mustBeOnFuture()
                    .displayMinutes(true)
                    .displayHours(true)
                    .displayDays(false)
                    .displayMonth(true)
                    .displayYears(true)
                    .displayDaysOfMonth(true)
                    .title("Выберите дату")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {
                            Toast.makeText(getActivity(), date.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Готово", Toast.LENGTH_SHORT).show();
                            String fromPoint = frompo.getText().toString();
                            String numbers = number.getText().toString();
                            String costs = cost.getText().toString();
                            String toPoint = onepoi.getText().toString();
                            String dataa = date.toString();
                            Data data = new Data();
                            data.setMode("puttravels");
                            if(flag.equals("true")){
                                data.setDriver("empty");
                                data.setWalker("empty");
                                data.setCompanion(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                                data.setFirstadress(fromPoint);
                                data.setSecondadress(toPoint);
                                data.setPrice(costs);
                                data.setCountpass(numbers);
                                data.setDate(dataa);
                            }
                            else if (flag.equals("false")){
                                data.setCompanion("empty");
                                data.setWalker("empty");
                                data.setDriver(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                                data.setFirstadress(fromPoint);
                                data.setSecondadress(toPoint);
                                data.setPrice(costs);
                                data.setCountpass(numbers);
                                data.setDate(dataa);
                            }
                            else {
                                data.setMode("putwalkers");
                                data.setCompanion("empty");
                                data.setDriver("empty");
                                data.setWalker(mSettings.getString(APP_PREFERENCES_PHONE, ""));
                                data.setFirstadress(fromPoint);
                                data.setSecondadress(toPoint);
                                data.setPrice(costs);
                                data.setCountpass(numbers);
                                data.setDate(dataa);
                            }
                            Call<Data> call = CreateService(Service.class, DB_URL).give_date(data);
                            call.enqueue(new Callback<Data>() {
                                @Override
                                public void onResponse(Call<Data> call, Response<Data> response) {
                                    Log.d("lol", "onResponse");
                                    if (response.isSuccessful()) {
                                        Log.d("lol", "response is successful");
                                    } else
                                        Log.d("lol", "response is NOT successful");
                                }
                                @Override
                                public void onFailure(Call<Data> call, Throwable t) {
                                    Log.d("lol", "onFailure");
                                }
                            });
                        }
                    })
                    .display();
        });

        all.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity().getBaseContext(), allTrips.class);
            String strin = "";
            if (flag.equals("true")) strin = "true";
            else if (flag.equals("false"))strin = "flase";
            else strin = "walker";
            i.putExtra("flag", strin);
            getActivity().startActivity(i);
        });

    }





    void createAdapter(){
        ArrayList<String> arr = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sp", Context.MODE_PRIVATE);

        String s = sharedPreferences.getString("key", null);
        if (s != null) {
            Gson gson = new Gson();
            String[] arrString = gson.fromJson(s, String[].class);
            for(String i : arrString){
                arr.add(i);
            }
        }
        recyclerView.setAdapter(new carsAdapter(arr, this, getActivity()));
    }


    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }



    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }


    @Override
    public void onItemClicked(String string) {
        Toast.makeText(getActivity(), "Вы поедете на " + string, Toast.LENGTH_SHORT).show();
        dialog.cancel();
        //todo
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView, Context context) {

    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {

    }

    @Override
    public void onDrivingRoutesError(@NonNull java.lang.Error error) {

    }

    public Bitmap drawSimpleBitmap() {
        int picSize = 25;
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(picSize / 2, picSize / 2, picSize / 2, paint);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        return bitmap;
    }


    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        for (DrivingRoute route : list) {
            PolylineMapObject polyline = mapObjects.addPolyline(route.getGeometry());
        }
    }


    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "Неизвестная ошибка";
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }
    private void buildDriving() {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList();

        requestPoints.add(new RequestPoint(currentPoint, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(destinationPoint, RequestPointType.WAYPOINT, null));
        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this);
    }
}