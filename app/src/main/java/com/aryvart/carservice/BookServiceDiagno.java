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

    LinearLayout  ll_diagno, ll_modular;
    GeneralData gD;
    String str_ServiceType,str_FromEditBtn;
    TextView txt_diagnoText, txt_modularText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_service_diagno);
        context = this;
        txt_view_car_det = (TextView) findViewById(R.id.txt_click_car_model);
        btn_bookService = (Button) findViewById(R.id.btn_book_service);
        gD = new GeneralData(context);
        str_FromEditBtn = getIntent().getStringExtra("str_fromEdit");
        if (str_FromEditBtn != null) {
            Log.e("YKD", "not null");
        } else {
            Log.e("YKD", "null");
        }

        img_back = (ImageView) findViewById(R.id.img_back);
        img_menu = (ImageView) findViewById(R.id.img_menu);

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


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_bookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_ServiceType == null) {
                    Log.e("BN", "str_ServiceType is null");
                    Toast.makeText(BookServiceDiagno.this, "Select your service type", Toast.LENGTH_SHORT).show();
                }
                else {
                   /* if (str_FromEditBtn != null) {
                        Log.e("YKD", "not null");
                        startActivity(new Intent(BookServiceDiagno.this, ServiceStation.class));
                        finish();
                    } else {*/
                        startActivity(new Intent(BookServiceDiagno.this, ServiceStation.class));
                        Log.e("YKD", "null");
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
                        finish();
                    //}
                    Log.e("BN", "str_ServiceType->" + str_ServiceType);

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


    }


}



