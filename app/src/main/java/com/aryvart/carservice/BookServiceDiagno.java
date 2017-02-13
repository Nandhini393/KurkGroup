package com.aryvart.carservice;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.carservice.GenericClasses.GPSTracker;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by android2 on 31/1/17.
 */
public class BookServiceDiagno extends Activity {
    Context context;
    //form fields
    TextView txt_view_car_det;
    Button btn_bookService;
    ImageView img_menu, img_back;
    TextView txt_headerName;
    //Menu
    Double lat = 0.0, longi = 0.0;
    LatLng TutorialsPoint = new LatLng(lat, longi);
    //Menu
    DrawerLayout myDrawerLayout;
    LinearLayout myLinearLayout, ll_updateProfile, ll_cancelService, ll_editBooking, ll_viewBooking, ll_logOut, ll_diagno, ll_modular;
    GeneralData gD;
    String str_ServiceType;
    TextView txt_diagnoText, txt_modularText;
    //permission to access external storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_WIFI_STATE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_service_diagno);
        context = this;
        txt_view_car_det = (TextView) findViewById(R.id.txt_click_car_model);
        btn_bookService = (Button) findViewById(R.id.btn_book_service);
        gD = new GeneralData(context);


        img_back = (ImageView) findViewById(R.id.img_back);
        img_menu = (ImageView) findViewById(R.id.img_menu);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.my_list_drawer_layout);
        myLinearLayout = (LinearLayout) findViewById(R.id.llaySliderParent);
        ll_updateProfile = (LinearLayout) findViewById(R.id.ll_update_profile);
        ll_cancelService = (LinearLayout) findViewById(R.id.ll_cancel_service);
        ll_editBooking = (LinearLayout) findViewById(R.id.ll_edit_service);
        ll_logOut = (LinearLayout) findViewById(R.id.ll_logout);
        ll_viewBooking = (LinearLayout) findViewById(R.id.ll_booking);
        ll_diagno = (LinearLayout) findViewById(R.id.ll_diagno);
        ll_modular = (LinearLayout) findViewById(R.id.ll_modular);
        txt_diagnoText = (TextView) findViewById(R.id.txt_diagno_text);
        txt_modularText = (TextView) findViewById(R.id.txt_modular_text);
        txt_headerName = (TextView) findViewById(R.id.txt_header);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_headerName.setTypeface(typeFace1);
        txt_view_car_det.setTypeface(typeFace1);
        btn_bookService.setTypeface(typeFace1);
        txt_diagnoText.setTypeface(typeFace1);
        txt_modularText.setTypeface(typeFace1);

        myLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawerLayout.setClickable(false);
                myDrawerLayout.setEnabled(false);
                myDrawerLayout.setFocusableInTouchMode(false);
                myDrawerLayout.setFocusable(false);

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawerLayout.openDrawer(myLinearLayout);

            }
        });

        btn_bookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_ServiceType == null) {
                    Log.e("BN", "str_ServiceType is null");
                    Toast.makeText(BookServiceDiagno.this, "Select your service type", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("BN", "str_ServiceType->" + str_ServiceType);
                    startActivity(new Intent(BookServiceDiagno.this, ServiceStation.class));

                    SharedPreferences.Editor prefEdit = gD.prefs.edit();
                    prefEdit.putString("ss_name", null);
                    prefEdit.putString("ss_id", null);
                    prefEdit.putString("ss_image", null);
                    prefEdit.putString("ss_addr", null);
                    prefEdit.putString("ss_diagno_charge", null);
                    prefEdit.putString("ss_pickup_charge", null);
                    prefEdit.putString("ss_modular_reprogramming_charge", null);


                    prefEdit.putString("edit_ss_id", null);
                    prefEdit.putString("edit_ss_book_id", null);
                    prefEdit.putString("edit_ss_serviceArray", null);
                    prefEdit.putString("edit_ss_image", null);
                    prefEdit.putString("edit_ss_name", null);
                    prefEdit.putString("edit_ss_addr", null);
                    prefEdit.putString("edit_ss_date", null);


                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(null);
                    String json_id = gson.toJson(null);
                    editor.putString("key", json);
                    editor.putString("key_id", json_id);
                    editor.commit();


                    prefEdit.commit();
                }

            }
        });
        ll_diagno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_diagnoText.setTextColor(Color.parseColor("#0987ff"));
                txt_modularText.setTextColor(Color.parseColor("#000000"));
                str_ServiceType = "diagnostics";
                //btn_bookService.setVisibility(View.VISIBLE);
                SharedPreferences.Editor prefEdit = gD.prefs.edit();
                prefEdit.putString("str_serviceType", str_ServiceType);
                prefEdit.commit();
            }
        });
        ll_modular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_modularText.setTextColor(Color.parseColor("#0987ff"));
                txt_diagnoText.setTextColor(Color.parseColor("#000000"));
               // btn_bookService.setVisibility(View.VISIBLE);
                str_ServiceType = "modular";
                SharedPreferences.Editor prefEdit = gD.prefs.edit();
                prefEdit.putString("str_serviceType", str_ServiceType);
                prefEdit.commit();
            }
        });
        txt_view_car_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View itemView1;
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                itemView1 = LayoutInflater.from(context)
                        .inflate(R.layout.car_model_popup, null);
                final AlertDialog altDialog = builder.create();
                altDialog.setView(itemView1);
                TextView txt_name = (TextView) itemView1.findViewById(R.id.txt_name);
                TextView txt_carModel = (TextView) itemView1.findViewById(R.id.txt_car_model);
                TextView txt_carNum = (TextView) itemView1.findViewById(R.id.txt_car_num);
                TextView txt_carType = (TextView) itemView1.findViewById(R.id.txt_car_type);
                txt_name.setText(gD.prefs.getString("name", null));
                txt_carModel.setText(gD.prefs.getString("car_model", null));
                txt_carNum.setText(gD.prefs.getString("car_number", null));
                txt_carType.setText(gD.prefs.getString("car_type", null));
                altDialog.show();
            }
        });
        ll_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookServiceDiagno.this, EditProfile.class));
            }
        });
        ll_cancelService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookServiceDiagno.this, CancelBookingList.class));
            }
        });
        ll_editBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookServiceDiagno.this, EditBookingList.class));
            }
        });
        ll_viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookServiceDiagno.this, ViewBookingList.class));
            }
        });
        ll_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View itemView1;
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                itemView1 = LayoutInflater.from(context)
                        .inflate(R.layout.logout_popup, null);
                final AlertDialog altDialog = builder.create();
                altDialog.setView(itemView1);
                Button btn_yes = (Button) itemView1.findViewById(R.id.btn_yes);
                Button btn_no = (Button) itemView1.findViewById(R.id.btn_no);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                        if (isConnected) {
                            SharedPreferences.Editor prefEdit = gD.prefs.edit();
                            prefEdit.putString("car_number", null);
                            prefEdit.putString("password", null);
                            prefEdit.commit();
                            startActivity(new Intent(BookServiceDiagno.this, SignIn.class));
                            finish();
                        } else {
                            Toast.makeText(BookServiceDiagno.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        altDialog.dismiss();
                    }
                });
                altDialog.show();


            }
        });


    }

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int internetPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int access_network_Permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);
        int access_fine_loc_Permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int access_coarse_loc_Permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int wifiPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || internetPermission != PackageManager.PERMISSION_GRANTED ||
                access_network_Permission != PackageManager.PERMISSION_GRANTED ||
                access_fine_loc_Permission != PackageManager.PERMISSION_GRANTED ||
                access_coarse_loc_Permission != PackageManager.PERMISSION_GRANTED ||
                wifiPermission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}



