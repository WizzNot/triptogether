package com.example.project.main;

import static com.example.project.server_data.ServiceConstructor.CreateService;
import static com.example.project.server_data.config.APP_PREF;
import static com.example.project.server_data.config.APP_PREF_EVENT;
import static com.example.project.server_data.config.APP_PREF_PHONE;
import static com.example.project.server_data.config.DB_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.server_data.Data;
import com.example.project.server_data.Service;
import com.example.project.util.RecAdapterSuggests;
import com.example.project.util.SelectSuggest;
import com.example.project.util.Suggests;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.BoundingBoxHelper;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.BusinessObjectMetadata;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.search.SuggestItem;
import com.yandex.mapkit.search.SuggestOptions;
import com.yandex.mapkit.search.SuggestSession;
import com.yandex.mapkit.search.SuggestType;
import com.yandex.mapkit.search.ToponymObjectMetadata;
import com.yandex.mapkit.traffic.TrafficLayer;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class search extends Fragment implements UserLocationObjectListener {

    private  boolean PlaceListenTo = false;
    private  boolean PlaceListenFrom = false;
    private boolean locationListenType = true;
    private boolean flagFrom = false;
    private boolean flagKuda = false;
    private  boolean trafficTurn = true;
    private PlacemarkMapObject placemarkTo;
    private PlacemarkMapObject placemarkFrom;
    private SuggestSession suggestSession = null;
    private SuggestSession suggestSession2 = null;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private UserLocationLayer userLocationLayer;
    private TrafficLayer trafficLayer;
    private SearchManager searchManager;
    private Session searchSession;
    private MapKit mapKit;
    private Session searchSession2;
    private CameraPosition cameraPosition;
    private RecAdapterSuggests adapterSuggests;
    private String uri;
    private String uri2;
    private SuggestItem.Type itemType;
    private SuggestItem.Type itemType2;
    private ArrayList<Suggests> suggests = new ArrayList<>();
    private LocationManager locationManager;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;
    private Point fromPoint;
    private Point kudaPoint;
    private Point myLocation;
    private Bitmap bitmap = Bitmap.createBitmap(26, 26, Bitmap.Config.ARGB_8888);
    private MapView mapView;
    private Button addAbout;
    private Button add;
    private ImageButton show_location;
    private ImageButton show_traffic;
    private ImageButton show_way;
    private ImageButton clear_map;
    private EditText frompo;
    private EditText onepoi;
    private ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<java.util.Map<String, Boolean>>() {
            @Override
            public void onActivityResult(java.util.Map<String, Boolean> result) {
            Log.e("activityResultLauncher", ""+result.toString());
            Boolean areAllGranted = true;
            for(Boolean b : result.values()) {
                areAllGranted = areAllGranted && b;
            }
            if(areAllGranted) {
                userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
                userLocationLayer.setVisible(true);
                userLocationLayer.setHeadingEnabled(true);
                userLocationLayer.setObjectListener(search.this);
            } else showSettingsAlert();
        }}
    );

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationUpdated(@NonNull Location location) {
            myLocation = location.getPosition();
        }

        @Override
        public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

        }
    };
    private final InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            if (onepoi.getText().toString().equals("")){
                bitmap = drawSimpleColorfulCircle(bitmap, Color.BLUE);
                if (PlaceListenTo) {
                    mapView.getMap().getMapObjects().remove(placemarkTo);
                    placemarkTo = mapView.getMap().getMapObjects().addPlacemark(new Point(point.getLatitude(), point.getLongitude()), ImageProvider.fromBitmap(bitmap));
                } else {
                    placemarkTo = mapView.getMap().getMapObjects().addPlacemark(new Point(point.getLatitude(), point.getLongitude()), ImageProvider.fromBitmap(bitmap));
                    PlaceListenTo = true;
                }
                searchSession = searchManager.submit(point, 16, new SearchOptions().setSearchTypes(SearchType.GEO.value), new Session.SearchListener() {
                    @Override
                    public void onSearchResponse(@NonNull com.yandex.mapkit.search.Response response) {
                        onepoi.setText(response.getCollection().getChildren().get(0).getObj().getMetadataContainer().getItem(ToponymObjectMetadata.class).getAddress().getFormattedAddress());
                    }

                    @Override
                    public void onSearchError(@NonNull Error error) {

                    }
                });
                kudaPoint = point;
            }
            else if (frompo.getText().toString().equals("")){
                bitmap = drawSimpleColorfulCircle(bitmap, Color.RED);
                if (PlaceListenFrom) {
                    mapView.getMap().getMapObjects().remove(placemarkFrom);
                    placemarkFrom = mapView.getMap().getMapObjects().addPlacemark(new Point(point.getLatitude(), point.getLongitude()), ImageProvider.fromBitmap(bitmap));
                } else {
                    placemarkFrom = mapView.getMap().getMapObjects().addPlacemark(new Point(point.getLatitude(), point.getLongitude()), ImageProvider.fromBitmap(bitmap));
                    PlaceListenFrom = true;
                }
                searchSession = searchManager.submit(point, 16, new SearchOptions().setSearchTypes(SearchType.GEO.value), new Session.SearchListener() {
                    @Override
                    public void onSearchResponse(@NonNull com.yandex.mapkit.search.Response response) {
                        frompo.setText(response.getCollection().getChildren().get(0).getObj().getMetadataContainer().getItem(ToponymObjectMetadata.class).getAddress().getFormattedAddress());
                    }

                    @Override
                    public void onSearchError(@NonNull Error error) {

                    }
                });
                fromPoint = point;
            }
        }
        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

        }
    };
    public void showSettingsAlert() {
        final AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setTitle("Геоданные")
                .setMessage("Геоданные не включены. Вы хотите перейти в меню настроек?")
                .setPositiveButton("Настройки",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getContext().startActivity(intent);
                    }
                }).setNegativeButton("Нет",new DialogInterface.OnClickListener() {
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
    private void dialogForAddress(){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet5layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        EditText editText = dialog.findViewById(R.id.edittext);
        EditText kuda = dialog.findViewById(R.id.onePoint);
        RecyclerView recyclerView = dialog.findViewById(R.id.suggests);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        suggestSession = searchManager.createSuggestSession();
        suggestSession2 = searchManager.createSuggestSession();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > i2){
                    flagFrom = false;
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                suggests.clear();
                suggestSession.suggest(editable.toString(), new BoundingBox(new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                        new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83))), new SuggestOptions().setUserPosition(myLocation).setSuggestWords(false).setSuggestTypes(SuggestType.BIZ.value), new SuggestSession.SuggestListener() {
                    @Override
                    public void onResponse(@NonNull List<SuggestItem> list) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getSubtitle() == null){
                                if (list.get(i).getUri() != null)
                                    suggests.add(new Suggests(list.get(i).getTitle().getText(), "empty", list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                            }
                            else {
                                if (list.get(i).getUri() != null) suggests.add(new Suggests(list.get(i).getTitle().getText(), list.get(i).getSubtitle().getText(), list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                            }
                        }
                        Log.d("ahjdklsf", String.valueOf(suggests.size()));
                        adapterSuggests = new RecAdapterSuggests(suggests, getActivity(), new SelectSuggest() {
                            @Override
                            public void onItemClicked(Suggests suggest) {
                                if (suggest.getAddress().equals("empty"))
                                    editText.setText(suggest.getGeoName());
                                else editText.setText(suggest.getGeoName() + ", " + suggest.getAddress());
                                uri = suggest.getGeoUri();
                                itemType = suggest.getItemType();
                                flagFrom = true;
                                if (flagFrom && flagKuda) {
                                    dialog.cancel();
                                    flagKuda = false;
                                    flagFrom = false;
                                } else Toast.makeText(dialog.getContext(), "После выбора точки нельзя изменять поле ввода! Иначе вам придется еще раз выбрать точку", Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(adapterSuggests);
                    }
                    @Override
                    public void onError(@NonNull Error error) {
                    }
                });
                suggestSession2.suggest(editable.toString(), new BoundingBox(new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                                new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83))),
                        new SuggestOptions().setUserPosition(myLocation).setSuggestTypes(SuggestType.GEO.value).setSuggestWords(false), new SuggestSession.SuggestListener() {
                            @Override
                            public void onResponse(@NonNull List<SuggestItem> list) {

                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getSubtitle() == null){
                                        if (list.get(i).getUri() != null)
                                            suggests.add(new Suggests(list.get(i).getTitle().getText(), "empty", list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                                    }
                                    else {
                                        if (list.get(i).getUri() != null) suggests.add(new Suggests(list.get(i).getTitle().getText(), list.get(i).getSubtitle().getText(), list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                                    }
                                }
                            }
                            @Override
                            public void onError(@NonNull Error error) {
                            }
                        });
            }
        });
        kuda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > i2){
                    flagKuda = false;
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                suggests.clear();
                suggestSession.suggest(editable.toString(), new BoundingBox(new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                        new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83))), new SuggestOptions().setUserPosition(myLocation).setSuggestWords(false).setSuggestTypes(SuggestType.BIZ.value), new SuggestSession.SuggestListener() {@Override
                public void onResponse(@NonNull List<SuggestItem> list) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getSubtitle() == null){
                            if (list.get(i).getUri() != null)
                                suggests.add(new Suggests(list.get(i).getTitle().getText(), "empty", list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                        }
                        else {
                            if (list.get(i).getUri() != null) suggests.add(new Suggests(list.get(i).getTitle().getText(), list.get(i).getSubtitle().getText(), list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                        }
                    }
                    Log.d("ahjdklsf", String.valueOf(suggests.size()));
                    adapterSuggests = new RecAdapterSuggests(suggests, getActivity(), new SelectSuggest() {
                        @Override
                        public void onItemClicked(Suggests suggest) {
                            if (suggest.getAddress().equals("empty"))
                                kuda.setText(suggest.getGeoName());
                            else kuda.setText(suggest.getAddress() + ", " + suggest.getGeoName());
                            uri2 = suggest.getGeoUri();
                            itemType2 = suggest.getItemType();
                            Log.d("uriCheck", itemType2.name() + "  " + uri);
                            flagKuda = true;
                            Log.d("asdlfjk", String.valueOf(flagKuda));
                            if(flagFrom && flagKuda) {
                                dialog.cancel();
                                flagKuda = false;
                                flagFrom = false;
                            } else Toast.makeText(dialog.getContext(), "После выбора точки нельзя изменять поле ввода! Иначе вам придется еще раз выбрать точку", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(adapterSuggests);
                }
                    @Override
                    public void onError(@NonNull Error error) {
                    }
                });
                suggestSession2.suggest(editable.toString(), new BoundingBox(new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                        new Point((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83))), new SuggestOptions().setUserPosition(myLocation).setSuggestTypes(SuggestType.GEO.value).setSuggestWords(false), new SuggestSession.SuggestListener() {
                    @Override
                    public void onResponse(@NonNull List<SuggestItem> list) {
                        if (list.size() == 0) Log.d("Sugests", "its nothing suggests");
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getSubtitle() == null){
                                if (list.get(i).getUri() != null)
                                    suggests.add(new Suggests(list.get(i).getTitle().getText(), "empty", list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                            }
                            else {
                                if (list.get(i).getUri() != null) suggests.add(new Suggests(list.get(i).getTitle().getText(), list.get(i).getSubtitle().getText(), list.get(i).getUri(), list.get(i).getType(), list.get(i).getCenter()));
                            }
                        }
                    }
                    @Override
                    public void onError(@NonNull Error error) {
                    }
                });
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mapView.getMap().getMapObjects().clear();

                if(uri != null && uri2 != null){
                    searchSession = searchManager.resolveURI(uri, new SearchOptions(), new Session.SearchListener() {
                        @Override
                        public void onSearchResponse(@NonNull com.yandex.mapkit.search.Response response) {
                            if (itemType == SuggestItem.Type.BUSINESS){
                                frompo.setText(response.getCollection().getChildren().get(0).getObj().getMetadataContainer().getItem(BusinessObjectMetadata.class).getAddress().getFormattedAddress());
                            }
                            else {
                                frompo.setText(response.getCollection().getChildren().get(0).getObj().getMetadataContainer().getItem(ToponymObjectMetadata.class).getAddress().getFormattedAddress());
                            }
                            kudaPoint = response.getCollection().getChildren().get(0).getObj().getGeometry().get(0).getPoint();
                            mapView.getMap().getMapObjects().addPlacemark(kudaPoint, ImageProvider.fromBitmap(drawSimpleColorfulCircle(bitmap, Color.RED)));
                        }
                        @Override
                        public void onSearchError(@NonNull Error error) {
                        }
                    });
                    searchSession2 = searchManager.resolveURI(uri2, new SearchOptions(), new Session.SearchListener() {
                        @Override
                        public void onSearchResponse(@NonNull com.yandex.mapkit.search.Response response) {
                            if (itemType2 == SuggestItem.Type.BUSINESS){
                                onepoi.setText(response.getCollection().getChildren().get(0).getObj().getMetadataContainer().getItem(BusinessObjectMetadata.class).getAddress().getFormattedAddress());
                            }
                            else {
                                onepoi.setText(response.getCollection().getChildren().get(0).getObj().getMetadataContainer().getItem(ToponymObjectMetadata.class).getAddress().getFormattedAddress());
                            }
                            fromPoint = response.getCollection().getChildren().get(0).getObj().getGeometry().get(0).getPoint();
                            mapView.getMap().getMapObjects().addPlacemark(fromPoint, ImageProvider.fromBitmap(drawSimpleColorfulCircle(bitmap, Color.RED)));
                        }
                        @Override
                        public void onSearchError(@NonNull Error error) {
                        }
                    });
                } else {
                    frompo.setText("");
                    onepoi.setText("");
                    PlaceListenFrom = false;
                    PlaceListenTo = false;
                }
            }
        });
        dialog.show();
    }

    public Point getCenterPoint(Point fromPoint, Point kudaPoint){
        return new Point((fromPoint.getLatitude() + kudaPoint.getLatitude())/2, (fromPoint.getLongitude() + kudaPoint.getLongitude())/2);
    }
    public Bitmap drawSimpleColorfulCircle(Bitmap bitmap, int color) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(13, 13, 13, paint);
        paint.setAntiAlias(true);
        return bitmap;
    }
    private void dialogForComm(){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet4layout);
        ImageView check = dialog.findViewById(R.id.check);
        Spinner eventType = dialog.findViewById(R.id.eventType);
        EditText comm = dialog.findViewById(R.id.aboutEventcomm);
        EditText countPass = dialog.findViewById(R.id.countPass);
        EditText cost = dialog.findViewById(R.id.cost);
        TextView costtxt = dialog.findViewById(R.id.textView2);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comm.getText().toString().equals(""))
                    comm.setText("Добрый день!");
                if (cost.getText().toString().equals("") && (eventType.getSelectedItem().toString().equals("путешествие") ||
                        eventType.getSelectedItem().toString().equals("прогулка"))) cost.setText("Не указан");
                else if (cost.getText().toString().equals("") && (eventType.getSelectedItem().toString().equals("попутчик") ||
                        eventType.getSelectedItem().toString().equals("водитель"))) cost.setText("Не указана");
                if (countPass.getText().toString().equals(""))
                    countPass.setText("1");
                editor.putString(APP_PREF_EVENT, eventType.getSelectedItem().toString() + "#!@" + //тип поездки
                        countPass.getText().toString() + "#!@" + //количество людей
                        cost.getText().toString() + "#!@" + //цена/тип транспорта
                        comm.getText().toString()); //коментариий к поездки
                editor.apply();
                dialog.dismiss();
            }
        });
        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] choose = getResources().getStringArray(R.array.eventType);
                if (choose[i].equals("прогулка")) costtxt.setText("Тип транспорта");
                else if (choose[i].equals("путешествие")) costtxt.setText("Тип транспорта (укажите подробнее в коментрии)");
                else costtxt.setText("Цена");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("onNothingSelected", "onNothingSelected");
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void submitDriving(Point FromPoint, Point KudaPoint){
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(FromPoint, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(KudaPoint, RequestPointType.WAYPOINT, null));
        cameraPosition = new CameraPosition();
        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, new DrivingSession.DrivingRouteListener() {
            @Override
            public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
                if (list != null && !list.isEmpty()) {
                    mapView.getMap().getMapObjects().addCollection().addPolyline(list.get(0).getGeometry());
                    cameraPosition = mapView.getMap().cameraPosition(BoundingBoxHelper.getBounds(list.get(0).getGeometry()));
                    mapView.getMap().move(new CameraPosition(cameraPosition.getTarget(), cameraPosition.getZoom() - 0.5f, cameraPosition.getAzimuth(), cameraPosition.getTilt()), new Animation(Animation.Type.SMOOTH, 0.75f), null);
                }
            }

            @Override
            public void onDrivingRoutesError(@NonNull Error error) {
                Toast.makeText(getContext(), "не возможно построить маршрут", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapKitFactory.initialize(getContext());
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSettings = getActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        editor = mSettings.edit();
        onepoi = view.findViewById(R.id.onePoint);
        addAbout = view.findViewById(R.id.aboutEvent);
        frompo = view.findViewById(R.id.Frompoint);
        mapView = view.findViewById(R.id.mapview);
        add = view.findViewById(R.id.add);
        show_location = view.findViewById(R.id.show_location);
        show_traffic =  view.findViewById(R.id.show_traffic);
        show_way = view.findViewById(R.id.show_way);
        clear_map = view.findViewById(R.id.clear_map);
        mapKit = MapKitFactory.getInstance();
        locationManager = MapKitFactory.getInstance().createLocationManager();
        mapView.getMap().addInputListener(inputListener);
        trafficLayer = mapKit.createTrafficLayer(mapView.getMapWindow());
        SearchFactory.initialize(getContext());
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE);
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();

        frompo.setOnTouchListener((view14, motionEvent) -> {
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                dialogForAddress();
            }
            return true;
        });

        onepoi.setOnTouchListener((view15, motionEvent) -> {
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                dialogForAddress();
            }
            return true;
        });

        int permissionStatus = ContextCompat.checkSelfPermission(getContext(), Manifest.permission_group.LOCATION);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "разрешение на геолакацию есть", Toast.LENGTH_SHORT).show();
            userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
            userLocationLayer.setVisible(true);
            userLocationLayer.setHeadingEnabled(true);
            userLocationLayer.setObjectListener(this);
        } else activityResultLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});

        addAbout.setOnClickListener(view12 -> dialogForComm());

        add.setOnClickListener(view1 -> {
            if (fromPoint != null && kudaPoint != null) {
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
                                String[] app_event = mSettings.getString(APP_PREF_EVENT, "").split("#!@");
                                String frompoint = frompo.getText().toString();
                                // TODO: считывание количества и цены
                                String numbers = app_event[1];
                                String costs = app_event[2];
                                String comm = app_event[3];
                                String toPoint = onepoi.getText().toString();
                                String dataa = date.toString();
                                Data data = new Data();
                                data.setMode("puttravels");
                                if (app_event[0].equals("попутчик")) {
                                    data.setDriver("empty");
                                    data.setWalker("empty");
                                    data.setCompanion(mSettings.getString(APP_PREF_PHONE, ""));
                                    //data.setFirstadress(frompoint + "#!@" + fromPoint.getLatitude() + ";" + fromPoint.getLongitude());
                                    //data.setSecondadress(toPoint + "#!@" + kudaPoint.getLatitude() + ";" + kudaPoint.getLongitude());
                                    data.setFirstadress(frompoint);
                                    data.setSecondadress(toPoint);
                                    data.setPrice(costs);
                                    data.setCountpass(numbers);
                                    data.setDate(dataa);
                                    data.setText(comm);
                                } else if (app_event[0].equals("водитель")) {
                                    data.setCompanion("empty");
                                    data.setWalker("empty");
                                    data.setDriver(mSettings.getString(APP_PREF_PHONE, ""));
                                    data.setFirstadress(frompoint);
                                    data.setSecondadress(toPoint);
                                    data.setPrice(costs);
                                    data.setCountpass(numbers);
                                    data.setDate(dataa);
                                    data.setText(comm);
                                } else if (app_event[0].equals("прогулка")) {
                                    data.setMode("putwalkers");
                                    data.setCompanion("empty");
                                    data.setDriver("empty");
                                    data.setWalker(mSettings.getString(APP_PREF_PHONE, ""));
                                    data.setFirstadress(frompoint);
                                    data.setSecondadress(toPoint);
                                    data.setPrice(costs);
                                    data.setCountpass(numbers);
                                    data.setDate(dataa);
                                    data.setText(comm);
                                } else {
                                    data.setMode("putadventure");
                                    data.setCompanion("empty");
                                    data.setDriver("empty");
                                    data.setWalker(mSettings.getString(APP_PREF_PHONE, ""));
                                    data.setFirstadress(frompoint);
                                    data.setSecondadress(toPoint);
                                    data.setPrice(costs);
                                    data.setCountpass(numbers);
                                    data.setDate(dataa);
                                    data.setText(comm);
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
            }});

        show_location.setOnClickListener(view1 -> {
            if (myLocation == null) Toast.makeText(getContext(), "подождите секунду", Toast.LENGTH_SHORT);
            else {
                mapView.getMap().move(new CameraPosition(new Point(
                        myLocation.getLatitude(),
                        myLocation.getLongitude()),
                        18.0f,0.0f,0.0f),
                        new Animation(Animation.Type.SMOOTH, 1f),null);
                if (locationListenType) {
                    locationListenType = false;
                    fromPoint = myLocation;
                    searchSession = searchManager.submit(myLocation, 16, new SearchOptions().setSearchTypes(SearchType.GEO.value), new Session.SearchListener() {
                        @Override
                        public void onSearchResponse(@NonNull com.yandex.mapkit.search.Response response) {
                            frompo.setText(response.getCollection().getChildren().get(0).getObj().getMetadataContainer().getItem(ToponymObjectMetadata.class).getAddress().getFormattedAddress());
                        }

                        @Override
                        public void onSearchError(@NonNull Error error) {

                        }
                    });
                    show_location.setBackgroundTintList(getResources().getColorStateList(R.color.zvet));
                    userLocationLayer.setAnchor(
                            new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                            new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
                    userLocationLayer.setAutoZoomEnabled(true);
                }
                else {
                    locationListenType = true;
                    show_location.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                    userLocationLayer.resetAnchor();
                    userLocationLayer.setAutoZoomEnabled(false);
                }
            }
        });

        show_traffic.setOnClickListener(view13 -> {
            if (trafficTurn){
                trafficLayer.setTrafficVisible(true);
                trafficTurn = false;
                show_traffic.setBackgroundTintList(getResources().getColorStateList(R.color.zvet));
            }
            else {
                show_traffic.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                trafficLayer.setTrafficVisible(false);
                trafficTurn = true;
            }
        });

        show_way.setOnClickListener(view16 -> {
            if (fromPoint != null && kudaPoint != null) {
                submitDriving(fromPoint, kudaPoint);
            }
            else {
                Toast.makeText(getContext(), "Укажите обе точки!!!", Toast.LENGTH_SHORT).show();
            }
        });

        clear_map.setOnClickListener(view17 -> {
            mapView.getMap().getMapObjects().clear();
            fromPoint = null;
            kudaPoint = null;
            frompo.setText("");
            onepoi.setText("");
            PlaceListenFrom = false;
            PlaceListenTo = false;
        });
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
        locationManager.subscribeForLocationUpdates(0, 1000, 1, false, FilteringMode.OFF, locationListener);
    }
    @Override
    public void onStop() {
        editor.putString(APP_PREF_EVENT, "");
        editor.apply();
        locationManager.unsubscribe(locationListener);
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
        userLocationLayer.setAutoZoomEnabled(true);
        userLocationView.getAccuracyCircle().setStrokeColor(0x00000000);
        userLocationView.getAccuracyCircle().setFillColor(0x00000000);
        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon("icon", ImageProvider.fromResource(getContext(), R.drawable.clipart2948424), new IconStyle()
                .setAnchor(new PointF(0f, 0f)).setRotationType(RotationType.NO_ROTATION).setZIndex(0f).setScale(0f));
        pinIcon.setIcon("pin", ImageProvider.fromResource(getContext(), R.drawable.clipart2948424), new IconStyle()
                .setAnchor(new PointF(0f, 0f)).setRotationType(RotationType.ROTATE).setZIndex(1f).setScale(0.5f));
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {
    }

}