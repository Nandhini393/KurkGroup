package com.aryvart.carservice;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
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
public class BookService extends Activity implements LocationListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    Context context;

    //form fields

    TextView txt_view_car_det;
    Button btn_bookService, btn_viewBookings;
    ImageView img_menu;
    TextView txt_headerName,txt_bookServiceLabel;
    String strname, strcartype, strcar_number, strcar_model, strpass;

    //Menu

    DrawerLayout myDrawerLayout;
    LinearLayout myLinearLayout, ll_updateProfile, ll_cancelService, ll_editBooking, ll_viewBooking, ll_logOut, ll_diagno, ll_pickUP, ll_Map;
    GeneralData gD;
    String str_ServiceType, str_EditButtonClicked;
    TextView txt_diagnoText, txt_pickUpText, txt_pickUpAddress;

    //permission to access external storage

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_WIFI_STATE
    };

    //map

    static SharedPreferences sharedpreferences;
    EditText editText;
    MapFragment mapFragment;
    Marker markerCurre = null;
    GoogleMap googleMap;
    ImageView marker_icon_view;
    TextView text;
    String completeAddresss;
    //Double d_AdrLat,d_AdrLong;
    Double lat = 0.0, longi = 0.0;
    LatLng TutorialsPoint = new LatLng(lat, longi);
    private GoogleApiClient mGoogleApiClient = null;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(16)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    public static boolean isGPSEnabled = false;
    public static boolean isNetworkEnabled = false;
    LocationManager locationManager;
    String strAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_map);

        context = this;

        btn_bookService = (Button) findViewById(R.id.btn_book_service);
        str_EditButtonClicked = getIntent().getStringExtra("btn_editClicked");

        gD = new GeneralData(context);
        txt_view_car_det = (TextView) findViewById(R.id.txt_click_car_model);

        Log.i("PP--*", "name" + gD.prefs.getString("name", null));
        Log.i("PP--*", "car_model" + gD.prefs.getString("car_model", null));
        Log.i("PP--*", "car_type" + gD.prefs.getString("car_type", null));
        Log.i("PP--*", "car_number" + gD.prefs.getString("car_number", null));


        strname = gD.prefs.getString("name", null);
        strcartype = gD.prefs.getString("car_type", null);
        strcar_number = gD.prefs.getString("car_number", null);
        strcar_model = gD.prefs.getString("car_model", null);
        strpass = gD.prefs.getString("password", null);


        // ** calling verify permission storage ** //

        if (GeneralData.n_count == 0) {
            verifyStoragePermissions(this);
            GeneralData.n_count = 1;
        }
        editText = (EditText) findViewById(R.id.editWorkLocation);
        text = (TextView) findViewById(R.id.text);
        marker_icon_view = (ImageView) findViewById(R.id.marker_icon_view);

        img_menu = (ImageView) findViewById(R.id.img_menu);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.my_list_drawer_layout);
        myLinearLayout = (LinearLayout) findViewById(R.id.llaySliderParent);
        ll_updateProfile = (LinearLayout) findViewById(R.id.ll_update_profile);
        ll_cancelService = (LinearLayout) findViewById(R.id.ll_cancel_service);
        ll_editBooking = (LinearLayout) findViewById(R.id.ll_edit_service);
        ll_logOut = (LinearLayout) findViewById(R.id.ll_logout);
        ll_viewBooking = (LinearLayout) findViewById(R.id.ll_booking);
        ll_diagno = (LinearLayout) findViewById(R.id.ll_diagno);
        ll_pickUP = (LinearLayout) findViewById(R.id.ll_pickUp);
        txt_diagnoText = (TextView) findViewById(R.id.txt_diagno_text);
        txt_pickUpText = (TextView) findViewById(R.id.txt_pickUp_text);
        txt_headerName = (TextView) findViewById(R.id.txt_header);
        btn_viewBookings = (Button) findViewById(R.id.btn_view_bookings);
        ll_Map = (LinearLayout) findViewById(R.id.ll_map);
        txt_bookServiceLabel=(TextView)findViewById(R.id.txt_bookServiceLAbel);
      //  txt_pickUpAddress = (TextView) findViewById(R.id.txt_PickUpAddr);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_headerName.setTypeface(typeFace1);
        txt_view_car_det.setTypeface(typeFace1);
        btn_bookService.setTypeface(typeFace1);
        txt_diagnoText.setTypeface(typeFace1);
        txt_pickUpText.setTypeface(typeFace1);
        btn_viewBookings.setTypeface(typeFace1);
        txt_bookServiceLabel.setTypeface(typeFace1);


        marker_icon_view.setOnClickListener(this);
        Log.i("BN", "completeAddresss->" + gD.prefs.getString("pickUp_address", null));
        Log.i("BN_BS", "str_serviceType-->" + gD.prefs.getString("str_serviceType", null));

        if (gD.prefs.getString("pickUp_address", null) != null) {
            Log.i("BNCom_onC", "completeAddresss->" + completeAddresss);
            Log.i("BNCom_onC", "pickUp_addressLat->" + gD.prefs.getString("pickUp_addressLat", null));
            Log.i("BNCom_onC", "pickUp_addressLong->" + gD.prefs.getString("pickUp_addressLong", null));
        }

        //** view car detail popup ** //

        txt_view_car_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("PP", "kalai");
                Log.i("PP--", "name" + strname);
                Log.i("PP--", "car_type" + strcartype);
                Log.i("PP--", "carmodel" + strcar_model);
                Log.i("PP--", "car_number" + strcar_number);

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
                if ((strname != null) && (strcar_number != null) && (strcar_model != null) && (strcartype != null)) {


                    txt_name.setText(strname);
                    txt_carModel.setText(strcar_model);
                    txt_carNum.setText(strcar_number);
                    txt_carType.setText(strcartype);
                } else {
                    Log.i("PP", "else--kalai");

                }
                altDialog.show();
            }
        });


        // ** map coding ** //


        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogLayout = inflater.inflate(R.layout.layout_confirmation, null);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
            alertDialogBuilder.setView(dialogLayout);
            alertDialogBuilder.setCancelable(false);


            final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            //  alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show();

            LinearLayout llayAlert = (LinearLayout) dialogLayout.findViewById(R.id.llayalertDialog);
            // TextView tv_alert_Title = (TextView) dialogLayout.findViewById(R.id.tv_alert_Title);
            TextView tv_alert_Message = (TextView) dialogLayout.findViewById(R.id.message);

            tv_alert_Message.setText("GPS has been disabled in your device. Please enable it?");

            Button btn_submit = (Button) dialogLayout.findViewById(R.id.ok);
            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.cancel);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(callGPSSettingIntent);

                    alertDialog.cancel();
                    mGoogleApiClient = new GoogleApiClient.Builder(context)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) context)
                            .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) context)
                            .build();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();


                }
            });


        } else {
            if (!isMyServiceRunning(GPSTracker.class)) {


                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) context)
                        .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) context)
                        .build();
                //to start the service
                //  startService(new Intent(User_Category.this, GPSTracker.class));
            }
            // getCurrentLocation("onload");
        }
            /*googleMap = ((CustomMapFragment) getFragmentManager().
                    findFragmentById(R.id.map)).getMap();*/

        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.uber_map));

        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // ** Drawer Layout ** //

        myLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawerLayout.setClickable(false);
                myDrawerLayout.setEnabled(false);
                myDrawerLayout.setFocusableInTouchMode(false);
                myDrawerLayout.setFocusable(false);

            }
        });

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawerLayout.openDrawer(myLinearLayout);

            }
        });

      /*  Log.e("BN!!", "completeAddresss"+gD.strAddress);

        SharedPreferences.Editor prefEditAddr = gD.prefs.edit();
        prefEditAddr.putString("pickUp_address", gD.strAddress);
        prefEditAddr.commit();


        if(gD.prefs.getString("pickUp_address",null)!=null){
            Log.e("BN!", "completeAddresss"+gD.prefs.getString("pickUp_address",null));
            strAddress=gD.prefs.getString("pickUp_address",null);
        }
        else{
            Log.e("BN!", "completeAddresss is null");
            strAddress="";
        }  */

        // ** button diagnostics code ** //

        final SharedPreferences.Editor prefEdit_D = gD.prefs.edit();
        ll_diagno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_ServiceType = "diagnostics";
                txt_diagnoText.setTextColor(Color.parseColor("#0987ff"));
                txt_pickUpText.setTextColor(Color.parseColor("#000000"));
                ll_Map.setVisibility(View.VISIBLE);

                Log.i("BNCom_onBu", "completeAddresss->" + strAddress);
                Log.i("BNCom_onBu", "str_ServiceType->" + str_ServiceType);
                Log.e("BN!!d", "completeAddresss"+gD.strAddress);

                if (str_ServiceType == null) {
                    Log.e("BN", "str_ServiceType is null");
                    Toast.makeText(BookService.this, "Select your service type", Toast.LENGTH_SHORT).show();
                } else if (gD.strAddress == null) {
                    Log.e("BN", "completeAddresss is null");
                    Toast.makeText(BookService.this, "Enter your address", Toast.LENGTH_SHORT).show();
                }else {


                     if (str_ServiceType.equalsIgnoreCase("diagnostics")) {
                         prefEdit_D.putString("pickUp_address", gD.strAddress);

                         Log.e("BNd", "completeAddresss->" + gD.strAddress);
                       /* if (str_EditButtonClicked != null) {
                            Intent i = new Intent(BookService.this, BookServiceDiagno.class);
                            i.putExtra("str_fromEdit", "edit");
                            startActivity(i);
                        } else {*/
                        startActivity(new Intent(BookService.this, BookServiceDiagno.class));
                        //}

                    }

                }

                prefEdit_D.commit();

            }
        });

        // ** button pickup code ** //

        final SharedPreferences.Editor prefEdit = gD.prefs.edit();
        ll_pickUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_Map.setVisibility(View.VISIBLE);
                txt_pickUpText.setTextColor(Color.parseColor("#0987ff"));
                txt_diagnoText.setTextColor(Color.parseColor("#000000"));
                str_ServiceType = "pickup";
                Log.e("BN!!p", "completeAddresss"+gD.strAddress);
             /*   SharedPreferences.Editor prefEdit4 = gD.prefs.edit();
                prefEdit4.putString("str_serviceType", str_ServiceType);
                prefEdit4.commit();*/


                prefEdit.putString("str_serviceType", str_ServiceType);

                prefEdit.putString("ss_serviceChoice", "1");

                //prefEdit.putString("pickUp_address", gD.strAddress);

                Log.i("BNCom_onBu", "str_ServiceType->" + str_ServiceType);

                if (str_ServiceType == null) {
                    Log.e("BN", "str_ServiceType is null");
                    Toast.makeText(BookService.this, "Select your service type", Toast.LENGTH_SHORT).show();
                } else if (gD.strAddress == null) {
                    Log.e("BN", "completeAddresss is null");
                    Toast.makeText(BookService.this, "Enter your address", Toast.LENGTH_SHORT).show();
                }
                else{

                    prefEdit.putString("pickUp_address", gD.strAddress);
                    Log.i("BNCom_onBu", "completeAddresss->" + strAddress);
                    if (str_ServiceType.equalsIgnoreCase("pickup")) {
                       /* if (str_EditButtonClicked != null) {
                            Log.e("YK", "not null");
                            Log.e("BN", "str_ServiceType->" + str_ServiceType);

                            startActivity(new Intent(BookService.this, ServiceStation.class));
                        } else {*/
                        startActivity(new Intent(BookService.this, ServiceStation.class));
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
                        //}
                    }
                }

                prefEdit.commit();
            }
        });


        // ** menu contents * //


        ll_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookService.this, EditProfile.class));
                finish();
            }
        });
        ll_cancelService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BookService.this, CancelBookingList.class));
            }
        });
        ll_editBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookService.this, EditBookingList.class));
            }
        });
        ll_viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookService.this, ViewBookingList.class));
            }
        });

        final SharedPreferences.Editor prefEditer = gD.prefs.edit();


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
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                        if (isConnected) {

                            prefEditer.putString("car_number", null);
                            prefEditer.putString("password", null);
                            prefEditer.commit();
                            startActivity(new Intent(BookService.this, SignIn.class));
                            finish();
                            finishAffinity();
                        } else {
                            Toast.makeText(BookService.this, "No internet connection", Toast.LENGTH_SHORT).show();
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


        // ** button book service code ** //
        final SharedPreferences.Editor prefBtnBook = gD.prefs.edit();

        btn_bookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_ServiceType="bookservice";
                txt_pickUpText.setTextColor(Color.parseColor("#000000"));
                txt_diagnoText.setTextColor(Color.parseColor("#000000"));

                if (str_ServiceType == null) {
                    Log.e("BN", "str_ServiceType is null");
                    Toast.makeText(BookService.this, "Select your service type", Toast.LENGTH_SHORT).show();
                } else if (gD.strAddress == null) {
                    Log.e("BN", "completeAddresss is null");
                    Toast.makeText(BookService.this, "Enter your address", Toast.LENGTH_SHORT).show();
                }

                else{

                    prefBtnBook.putString("str_serviceType", str_ServiceType);
                    prefBtnBook.putString("pickUp_address", gD.strAddress);
                    startActivity(new Intent(BookService.this, ServiceStation.class));

                    prefBtnBook.putString("ss_name", null);
                    prefBtnBook.putString("ss_id", null);
                    prefBtnBook.putString("ss_image", null);
                    prefBtnBook.putString("ss_addr", null);
                    prefBtnBook.putString("ss_diagno_charge", null);
                    prefBtnBook.putString("ss_pickup_charge", null);
                    prefBtnBook.putString("ss_modular_reprogramming_charge", null);
                    prefBtnBook.putString("edit_ss_id", null);
                    prefBtnBook.putString("edit_ss_book_id", null);
                    prefBtnBook.putString("edit_ss_serviceArray", null);
                    prefBtnBook.putString("edit_ss_image", null);
                    prefBtnBook.putString("edit_ss_name", null);
                    prefBtnBook.putString("edit_ss_addr", null);
                    prefBtnBook.putString("edit_ss_date", null);
                    prefBtnBook.putString("ss_serviceChoice", "1");

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(null);
                    String json_id = gson.toJson(null);
                    editor.putString("key", json);
                    editor.putString("key_id", json_id);
                    editor.commit();

                }
                prefBtnBook.commit();
            }
        });
/*

        final SharedPreferences.Editor prefEdit = gD.prefs.edit();
        btn_bookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.i("BNCom_onBu", "completeAddresss->" + completeAddresss);
                Log.i("BNCom_onBu", "str_ServiceType->" + str_ServiceType);

                if (str_ServiceType == null) {
                    Log.e("BN", "str_ServiceType is null");
                    Toast.makeText(BookService.this, "Select your service type", Toast.LENGTH_SHORT).show();
                } else if (completeAddresss == null) {
                    Log.e("BN", "completeAddresss is null");
                    Toast.makeText(BookService.this, "Enter your address", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("BN", "completeAddresss->" + completeAddresss);
                    prefEdit.putString("pickUp_address", completeAddresss);
                    prefEdit.putString("str_serviceType", str_ServiceType);

                    if (str_ServiceType.equalsIgnoreCase("pickup")) {
                       */
/* if (str_EditButtonClicked != null) {
                            Log.e("YK", "not null");
                            Log.e("BN", "str_ServiceType->" + str_ServiceType);

                            startActivity(new Intent(BookService.this, ServiceStation.class));
                        } else {*//*

                        startActivity(new Intent(BookService.this, ServiceStation.class));
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
                        //}
                    } else if (str_ServiceType.equalsIgnoreCase("diagnostics")) {
                       */
/* if (str_EditButtonClicked != null) {
                            Intent i = new Intent(BookService.this, BookServiceDiagno.class);
                            i.putExtra("str_fromEdit", "edit");
                            startActivity(i);


                        } else {*//*

                        startActivity(new Intent(BookService.this, BookServiceDiagno.class));
                        //}

                    }

                }
                prefEdit.commit();
            }
        });
*/


        // ** button view bookings code ** //

        btn_viewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BookService.this, ViewBookingList.class));
            }
        });

    }

    //  ** persmission method.  ** //


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


    //  ** new coding for map  ** //


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onPause() {
        Log.i("PP", "Hello");

        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onLocationChanged(Location loc) {
        try {
            // TODO Auto-generated method stub


            if (loc == null)
                return;


            if (markerCurre == null) {

                Log.i("PP", "Lat : " + loc.getLatitude() + "long : " + loc.getLongitude());


                TutorialsPoint = new LatLng(loc.getLatitude(), loc.getLongitude());


                markerCurre = gD.googleMapGeneral.addMarker(new MarkerOptions().position(TutorialsPoint).snippet(String.valueOf(TutorialsPoint)));

                markerCurre.showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(TutorialsPoint)      // Sets the center of the map to Mountain View
                        .zoom(19)                   // Sets the zoom
                        // Sets the orientation of the camera to east
                        .tilt(90)     // Sets the tilt of the camera to 30 degrees
                        .build();
                markerCurre.remove();// Creates a CameraPosition from the builder
                gD.googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            /*    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Log.i("GGGK", "HI....");
// googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
                        if (marker.getTitle().toString().length() > 0) {

                            marker.showInfoWindow();
                        }

                        return true;
                    }
                });*/

                gD.googleMapGeneral.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Toast.makeText(BookService.this, completeAddresss, Toast.LENGTH_SHORT).show();

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(TutorialsPoint)      // Sets the center of the map to Mountain View
                                .zoom(19)                   // Sets the zoom
                                // Sets the orientation of the camera to east
                                .tilt(90)     // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        gD.googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                        if (markerCurre.getTitle().toString().length() > 0) {

                            markerCurre.showInfoWindow();
                        }

                        return true;

                        //return false;

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);  // LocationListener
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapReady(GoogleMap map) {
        // TODO Auto-generated method stub
        this.gD.googleMapGeneral = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gD.googleMapGeneral.setMyLocationEnabled(true);
        gD.googleMapGeneral.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gD.googleMapGeneral.getUiSettings().setRotateGesturesEnabled(true);


        gD.googleMapGeneral.getUiSettings().setZoomControlsEnabled(true);
        gD.googleMapGeneral.getUiSettings().setCompassEnabled(false);

        gD.googleMapGeneral.getUiSettings().setScrollGesturesEnabled(true);
        gD.googleMapGeneral.getUiSettings().setTiltGesturesEnabled(true);
        gD.googleMapGeneral.getUiSettings().setMyLocationButtonEnabled(true);
        gD.googleMapGeneral.getUiSettings().setZoomGesturesEnabled(true);
        gD.googleMapGeneral.getUiSettings().setAllGesturesEnabled(true);


        gD.googleMapGeneral.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition position) {
                // TODO Auto-generated method stub

                Log.i("KALAI" + position.target.latitude, "" + position.target.longitude);
                //  mLocationMarkerText.setText("Lat : " + position.target.latitude + "," + "Long : " + position.target.longitude);

              /*  if(gD.prefs.getString("pickUp_address",null)!=null){
                    Log.i("BNCom_onC", "completeAddresss->" + completeAddresss);
                    Log.i("BNCom_onC", "pickUp_addressLat->" + gD.prefs.getString("pickUp_addressLat",null));
                    Log.i("BNCom_onC", "pickUp_addressLong->" + gD.prefs.getString("pickUp_addressLong",null));
                    updateLocationNew(Double.parseDouble(gD.prefs.getString("pickUp_addressLat",null)),Double.parseDouble(gD.prefs.getString("pickUp_addressLat",null)));
                }
                else{
                    updateLocation(position.target);
                }*/

                updateLocation(position.target);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void updateLocation(LatLng centerLatLng) {
        try {
            if (centerLatLng != null) {
                Geocoder geocoder = new Geocoder(BookService.this,
                        Locale.getDefault());
                Log.i("TT", "latitude & longitude" + centerLatLng);
                Log.i("TT", "latitude ***" + centerLatLng.latitude);
                Log.i("TT", " longitude ****" + centerLatLng.longitude);

                ArrayList<Address> addresses = new ArrayList<Address>();
                try {
                    addresses = (ArrayList<Address>) geocoder.getFromLocation(centerLatLng.latitude,
                            centerLatLng.longitude, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Log.i("SUREN", "addresses 1 : " + addresses);

                    if (addresses != null && addresses.size() > 0) {

                        String addressIndex0 = (addresses.get(0).getAddressLine(0) != null) ? addresses
                                .get(0).getAddressLine(0) : null;
                        String addressIndex1 = (addresses.get(0).getAddressLine(1) != null) ? addresses
                                .get(0).getAddressLine(1) : null;
                        String addressIndex2 = (addresses.get(0).getAddressLine(2) != null) ? addresses
                                .get(0).getAddressLine(2) : null;
                        String addressIndex3 = (addresses.get(0).getAddressLine(3) != null) ? addresses
                                .get(0).getAddressLine(3) : null;
                        Log.i("JJ", "addressIndex0" + addressIndex0);
                        Log.i("JJ", "addressIndex1" + addressIndex1);
                        Log.i("JJ", "addressIndex2" + addressIndex2);
                        Log.i("JJ", "addressIndex3" + addressIndex3);


                        String completeAddress = addressIndex0 + "," + addressIndex1;
                        completeAddresss = addressIndex0 + "," + addressIndex1;
                       /* SharedPreferences.Editor prefEditAddr = gD.prefs.edit();
                        prefEditAddr.putString("pickUp_address", completeAddresss);
                        prefEditAddr.commit();*/
                       /* txt_pickUpAddress.setText(completeAddresss);
                        txt_pickUpAddress.setVisibility(View.VISIBLE);*/

                        if (addressIndex2 != null) {
                            completeAddress += "," + addressIndex2;
                        }
                        if (addressIndex3 != null) {
                            completeAddress += "," + addressIndex3;
                        }
                        if (completeAddress != null) {

                            Log.i("SUREN", "Address : " + completeAddresss);

                            editText.setText(completeAddresss);
                            text.setText(completeAddresss);


                           /* SharedPreferences.Editor prefEditAddr = gD.prefs.edit();
                            prefEditAddr.putString("pickUp_address", editText.getText().toString().trim());
                            prefEditAddr.commit();*/


                            //Inflating the Popup using xml file
                            // popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


                            gD = new GeneralData();
                            googleMap = gD.googleMapGeneral;
                            googleMap.clear();


                            TutorialsPoint = new LatLng(Double.parseDouble(String.valueOf(centerLatLng.latitude)), Double.parseDouble(String.valueOf(centerLatLng.longitude)));


                            //  googleMap = mCustomMapFragment.getMap();


                            gD.strAddress = completeAddress;
                            gD.strLatitutde = String.valueOf(centerLatLng.latitude);
                            gD.strLongitude = String.valueOf(centerLatLng.longitude);


                            Log.i("TT", "gD.strAddress ***" + gD.strAddress);

                            Log.i("TT", "latitude ***" + centerLatLng.latitude);
                            Log.i("TT", " longitude ****" + centerLatLng.longitude);
                            BookService.sharedpreferences = getSharedPreferences("myprefer", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = BookService.sharedpreferences.edit();
                            editor.putString("Lat", String.valueOf(centerLatLng.latitude));
                            editor.putString("Long", String.valueOf(centerLatLng.longitude));
                            editor.commit();


                            //  GetProviders get_providers = new GetProviders(String.valueOf(centerLatLng.latitude), String.valueOf(centerLatLng.longitude), "", "");
                            //   get_providers.execute();

                            //mLocationTextView.setText(completeAddress);
                        }
                    }
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            View itemView1 = LayoutInflater.from(context)
                    .inflate(R.layout.logout_popup, null);
            final AlertDialog altDialog = builder.create();
            altDialog.setView(itemView1);
            TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
            Button btn_yes = (Button) itemView1.findViewById(R.id.btn_yes);
            Button btn_no = (Button) itemView1.findViewById(R.id.btn_no);
            txt_content.setText("Do you wish to exit the app?");
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                    boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                    if (isConnected) {
                        finish();
                        finishAffinity();
                    } else {
                        Toast.makeText(BookService.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
        return true;

}

    @Override
    public void onClick(View v) {
        /*text.setVisibility(View.VISIBLE);
        text.setText(completeAddresss);*/

        View itemView1;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        itemView1 = LayoutInflater.from(context)
                .inflate(R.layout.forget_pass_popup, null);
        final AlertDialog altDialog = builder.create();
        altDialog.setView(itemView1);
        TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
        Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
        txt_content.setText(completeAddresss);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altDialog.dismiss();
            }
        });
        altDialog.show();
    }
}

