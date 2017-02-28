package com.aryvart.carservice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aryvart.carservice.Adapters.ChooseServiceAdapter;
import com.aryvart.carservice.Adapters.ServiceStationAdapter;
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.aryvart.carservice.Interfaces.ChooseServiceInterface;
import com.aryvart.carservice.Interfaces.ServiceStationInterface;
import com.aryvart.carservice.imageCache.ImageLoader;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by android2 on 31/1/17.
 */
public class ServiceStation extends Activity implements ServiceStationInterface {
    Context context;
    //Listview
    private List<CommonBean> serviceList = new ArrayList<>();
    private ListView listView;
    private ServiceStationAdapter mAdapter;
    //calen
    ImageView img_calen;
    TextView txt_date;
    TextView txt_next;
    //DrawerLayout
    DrawerLayout myDrawerLayout;
    LinearLayout myLinearLayout;
    ImageView img_back;
    //form fields
    RelativeLayout ll_selectServiceStation;
    int count = 0;
    GeneralData gD;
    TextView txt_headerName, txt_title;
    LinearLayout ll_dispStastions;
    ImageView img_StationImage;
    TextView txt_ssAddr, txt_ssName, txt_selectStationText, txt_error;
    ImageLoader imgLoader;
    String str_StationName, str_StationId, str_StationImage, str_StationAddr, str_PickupCharge, str_ModularCharge, str_DiagnoCharge, str_ChoosenStationId;
    JSONArray jsonArrServiceStations;
    //intercon checking
    IntentFilter filter1;
int year,month,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_station);
        context = this;
        gD = new GeneralData(context);
        imgLoader = new ImageLoader(context);
        listView = (ListView) findViewById(R.id.listview);
        img_calen = (ImageView) findViewById(R.id.img_calen);
        txt_date = (TextView) findViewById(R.id.txt_set_date);
        txt_next = (TextView) findViewById(R.id.txt_next);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.my_list_drawer_layout);
        myLinearLayout = (LinearLayout) findViewById(R.id.llaySliderParent);
        img_back = (ImageView) findViewById(R.id.img_back);
        ll_selectServiceStation = (RelativeLayout) findViewById(R.id.rl_select_service_station);
        txt_headerName = (TextView) findViewById(R.id.txt_header);
        txt_title = (TextView) findViewById(R.id.txt_spanText);
        ll_dispStastions = (LinearLayout) findViewById(R.id.ll_disp_station);
        img_StationImage = (ImageView) findViewById(R.id.img_profile);
        txt_ssName = (TextView) findViewById(R.id.txt_comp_name);
        txt_ssAddr = (TextView) findViewById(R.id.txt_address);
        txt_selectStationText = (TextView) findViewById(R.id.txt_SelectStationText);
        txt_error = (TextView) findViewById(R.id.txt_error);

        // ** fonts and typefaces **//

        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_headerName.setTypeface(typeFace1);
        txt_next.setTypeface(typeFace1);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
        txt_date.setTypeface(typeFace);
        txt_selectStationText.setTypeface(typeFace);

        String str_txt_span = "SELECT SHELL SERVICE STATION";
        SpannableString spannableString = new SpannableString(str_txt_span);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0987ff")), 0, 12, 0);
        txt_title.setText(spannableString);
        txt_title.setTypeface(typeFace1);



        final Calendar c = Calendar.getInstance();
       /* hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);*/
//updateTime(hour, minute, "data");

        year = c.get(Calendar.YEAR);

        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);


        //** internet conn ** //

        filter1 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, filter1);


        Log.i("BN", "id" + gD.prefs.getString("pickUp_address", null));
        Log.i("BN", "str_serviceType-->" + gD.prefs.getString("str_serviceType", null));
        Log.i("PP1", "id" + gD.prefs.getString("ss_id", null));
        Log.i("PP1", "ss_image" + gD.prefs.getString("ss_image", null));
        Log.i("PP1", "ss_addr" + gD.prefs.getString("ss_addr", null));
        Log.i("PP1", "ss_name" + gD.prefs.getString("ss_name", null));
        Log.i("PP1", "ss_date" + gD.prefs.getString("ss_date", null));

        if (gD.prefs.getString("ss_addr", null) != null && gD.prefs.getString("ss_image", null) != null && gD.prefs.getString("ss_name", null) != null) {

            str_StationId = gD.prefs.getString("ss_id", null);
            str_StationName = gD.prefs.getString("ss_name", null);
            str_StationImage = gD.prefs.getString("ss_image", null);
            str_StationAddr = gD.prefs.getString("ss_addr", null);
            str_DiagnoCharge = gD.prefs.getString("ss_diagno_charge", null);
            str_PickupCharge = gD.prefs.getString("ss_pickup_charge", null);
            str_ModularCharge = gD.prefs.getString("ss_modular_reprogramming_charge", null);


            ll_dispStastions.setVisibility(View.VISIBLE);
            imgLoader.DisplayImage(gD.prefs.getString("ss_image", null), img_StationImage);
            txt_ssAddr.setText(gD.prefs.getString("ss_addr", null));
            txt_ssName.setText(gD.prefs.getString("ss_name", null));
            txt_date.setText(gD.prefs.getString("ss_date", null));


            Log.i("PP", "id" + gD.prefs.getString("ss_id", null));

            Log.i("PP", "ss_image" + gD.prefs.getString("ss_image", null));

            Log.i("PP", "ss_addr" + gD.prefs.getString("ss_addr", null));

            Log.i("PP", "ss_name" + gD.prefs.getString("ss_name", null));

            Log.i("PP", "ss_date" + gD.prefs.getString("ss_date", null));

        }

        ll_selectServiceStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();


                if (count == 0) {
                    if (isConnected) {
                        Log.i("OO", "Hiiii 1");
                        listView.setVisibility(View.VISIBLE);
                        txt_error.setVisibility(View.GONE);
                        if (str_StationName != null) {
                            Log.i("OO", "Hiiii 2");
                            if (!str_StationName.equalsIgnoreCase("select services")) {
                                Log.i("OO", "Hiiii 3");
                                LoadLayout_sel(jsonArrServiceStations, str_StationName);
                            } else {
                                Log.i("OO", "Hiiii 4");
                                LoadLayout(jsonArrServiceStations);
                            }
                        } else {
                            Log.i("OO", "Hiiii 5");
                            LoadLayout(jsonArrServiceStations);
                        }
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_error.setVisibility(View.VISIBLE);
                        txt_error.setText("No response from server.Check your internet connection");
                        txt_error.setTextColor(Color.parseColor("#ff0000"));
                    }
                    count = 1;
                } else if (count == 1) {
                    if (isConnected) {
                        listView.setVisibility(View.GONE);

                    } else {
                        listView.setVisibility(View.GONE);
                        txt_error.setVisibility(View.GONE);
                        txt_error.setText("No response from server.Check your internet connection");
                        txt_error.setTextColor(Color.parseColor("#ff0000"));
                    }
                    count = 0;
                }

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor prefEdit = gD.prefs.edit();
                prefEdit.putString("ss_name", null);
                prefEdit.putString("ss_id", null);
                prefEdit.putString("ss_image", null);
                prefEdit.putString("ss_addr", null);
                prefEdit.putString("ss_diagno_charge", null);
                prefEdit.putString("ss_pickup_charge", null);
                prefEdit.putString("ss_modular_reprogramming_charge", null);

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
            }
        });


        // ** calendar btn ** //

        img_calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });


        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        //** next btn ** //

        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("BN_SS", "str_serviceType-->" + gD.prefs.getString("str_serviceType", null));
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                if (isConnected) {

                    SharedPreferences.Editor prefEdit = gD.prefs.edit();
                    prefEdit.putString("ss_date", txt_date.getText().toString().trim());
                    prefEdit.putString("ss_name", str_StationName);

                    prefEdit.putString("ss_image", str_StationImage);
                    prefEdit.putString("ss_addr", str_StationAddr);
                    prefEdit.putString("ss_diagno_charge", str_DiagnoCharge);
                    prefEdit.putString("ss_pickup_charge", str_PickupCharge);
                    prefEdit.putString("ss_modular_reprogramming_charge", str_ModularCharge);
                    prefEdit.putString("ss_id", str_StationId);

                    if (str_StationId == null) {
                        Toast.makeText(ServiceStation.this, "Select your service station", Toast.LENGTH_SHORT).show();
                    } else if (txt_date.getText().toString().trim().length() == 0) {
                        Toast.makeText(ServiceStation.this, "Select your service date", Toast.LENGTH_SHORT).show();
                    } else {

                        /*if (gD.prefs.getString("str_serviceType", null).equalsIgnoreCase("modular") || gD.prefs.getString("str_serviceType", null).equalsIgnoreCase("modularpickup")) {
                            startActivity(new Intent(ServiceStation.this, ServiceConfirmation.class));
                            finish();


                        } */
                            Intent i = new Intent(ServiceStation.this, ChooseService.class);
                            startActivity(i);
                            finish();
                            Log.i("HH_n", "str_ChoosenStationId : " + str_ChoosenStationId);
                            Log.i("HH_n", "sharedPref : " + gD.prefs.getString("ss_id", null));

                            if (str_ChoosenStationId != null && (gD.prefs.getString("ss_id", null)) != null) {
                                if (str_ChoosenStationId.equalsIgnoreCase(gD.prefs.getString("ss_id", null))) {
                                    Log.i("HH_n1", "choosen : " + str_ChoosenStationId);
                                    Log.i("HH_n1", "sharedPref : " + gD.prefs.getString("ss_id", null));
                                    Log.i("HH_n1", "equal");
                                } else {
                                    Log.i("HH_n1", "choosen : " + str_ChoosenStationId);
                                    Log.i("HH_n1", "sharedPref : " + gD.prefs.getString("ss_id", null));
                                    Log.i("HH_n1", "not equal ");
                                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = sharedPrefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(null);
                                    String json_id = gson.toJson(null);
                                    editor.putString("key", json);
                                    editor.putString("key_id", json_id);
                                    editor.commit();
                                }

                            }



                    }

                    prefEdit.commit();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        SharedPreferences.Editor prefEdit = gD.prefs.edit();
        prefEdit.putString("ss_name", null);
        prefEdit.putString("ss_id", null);
        prefEdit.putString("ss_image", null);
        prefEdit.putString("ss_addr", null);
        prefEdit.putString("ss_diagno_charge", null);
        prefEdit.putString("ss_pickup_charge", null);
        prefEdit.putString("ss_modular_reprogramming_charge", null);

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
    }


    //*** calendar code ** //

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

      //current year
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();


       /* Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();*/

        return null;


    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub

            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        txt_date.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    // ** listview interface ** to diaplay and get specific service station values //

    @Override
    public void getServiceStationAddress(String str_id, String str_image, String str_name, String str_address, String str_diagno_charge, String str_pickUP_charge, String str_modular_charge) {

        str_ChoosenStationId = str_id;
        listView.setVisibility(View.GONE);
        ll_dispStastions.setVisibility(View.VISIBLE);
        imgLoader.DisplayImage(str_image, img_StationImage);
        txt_ssAddr.setText(str_address);
        txt_ssName.setText(str_name);
        str_StationName = str_name;
        str_StationId = str_id;

        Log.i("HH", "str_ChoosenStationId : " + str_ChoosenStationId);
        Log.i("HH", "str_StationId : " + str_StationId);

        str_StationImage = str_image;
        str_StationAddr = str_address;
        str_PickupCharge = str_pickUP_charge;
        str_ModularCharge = str_modular_charge;
        str_DiagnoCharge = str_diagno_charge;


    }


    // ** service station display REST call ** //


    public void displayServiceStationCall() {
        //gD.showAlertDialog(context, "Loading", "Please wait");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "service_station.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(UserRegOne.this, response, Toast.LENGTH_LONG).show();
                        try {
                            Log.i("HH", "strResp : " + response);
                            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

                            JSONObject jsobj = new JSONObject(response);
                            // gD.altDialog.dismiss();
                            Log.i("HH", "strResp : " + response);
                            if (jsobj.getString("code").equalsIgnoreCase("2")) {
                                JSONArray services_stations = jsobj.getJSONArray("service_stations");

                                jsonArrServiceStations = jsobj.getJSONArray("service_stations");

                                if (services_stations.length() > 0) {
                                    for (int i = 0; i < services_stations.length(); i++) {
                                        JSONObject providersServiceJSONobject = services_stations.getJSONObject(i);
                                        CommonBean drawerBean = new CommonBean();
                                        drawerBean.setStr_serviceName(providersServiceJSONobject.getString("service_station_name"));
                                        drawerBean.setStr_serviceId(providersServiceJSONobject.getString("service_station_id"));
                                        drawerBean.setStr_serviceAddr(providersServiceJSONobject.getString("address"));
                                        drawerBean.setStr_diagnosis_charge(providersServiceJSONobject.getString("diagnosis_charge"));
                                        drawerBean.setPickup_charge(providersServiceJSONobject.getString("pickup_charge"));
                                        drawerBean.setModular_reprogramming_charge(providersServiceJSONobject.getString("modular_reprogramming_charge"));
                                        String image = GeneralData.LOCAL_IP_IMAGE + providersServiceJSONobject.getString("image");
                                        drawerBean.setN_image(image);
                                        beanArrayList.add(drawerBean);

                                    }
                                } else {
                                    Log.e("HH", "No resp : ");
                                }
                                mAdapter = new ServiceStationAdapter(context, beanArrayList, (ServiceStationInterface) context);
                                listView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(UserRegOne.this, error.toString(), Toast.LENGTH_LONG).show();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));

                                Log.e("error_msg", res);

                                //  Toast.makeText(UserRegOne.this, "res : " + res, Toast.LENGTH_LONG).show();

// Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
// Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
// returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "admin", "EasyMarry2016").getBytes(), Base64.DEFAULT)));
// params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }*/

        };

//3Secs
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceStation.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }


    // ** saving service station list in JSONArray (LoadLAyout)  ** //


    private ArrayList<CommonBean> LoadLayout(JSONArray providerServicesMonth) {
        ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();
        JSONObject jsobj = null;

        if (providerServicesMonth != null) {
            try {

                if (providerServicesMonth.length() > 0) {
                    for (int i = 0; i < providerServicesMonth.length(); i++) {
                        jsobj = providerServicesMonth.getJSONObject(i);
                        CommonBean drawerBean = new CommonBean();
                        drawerBean.setStr_serviceName(jsobj.getString("service_station_name"));
                        drawerBean.setStr_serviceId(jsobj.getString("service_station_id"));
                        drawerBean.setStr_serviceAddr(jsobj.getString("address"));
                        drawerBean.setStr_diagnosis_charge(jsobj.getString("diagnosis_charge"));
                        drawerBean.setPickup_charge(jsobj.getString("pickup_charge"));
                        drawerBean.setModular_reprogramming_charge(jsobj.getString("modular_reprogramming_charge"));
                        String image = GeneralData.LOCAL_IP_IMAGE + jsobj.getString("image");
                        drawerBean.setN_image(image);
                        beanArrayList.add(drawerBean);
                    }
                }

                mAdapter = new ServiceStationAdapter(context, beanArrayList, (ServiceStationInterface) context);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return beanArrayList;

    }


    // ** LoadLayout to high light the already selected service station ** //


    private ArrayList<CommonBean> LoadLayout_sel(JSONArray providerServicesMonth, String str) {
        ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();
        JSONObject jsobj = null;

        if (providerServicesMonth != null) {
            try {

                if (providerServicesMonth.length() > 0) {
                    for (int i = 0; i < providerServicesMonth.length(); i++) {
                        jsobj = providerServicesMonth.getJSONObject(i);
                        CommonBean drawerBean = new CommonBean();
                        drawerBean.setStr_serviceName(jsobj.getString("service_station_name"));
                        drawerBean.setStr_serviceId(jsobj.getString("service_station_id"));
                        drawerBean.setStr_serviceAddr(jsobj.getString("address"));
                        drawerBean.setStr_diagnosis_charge(jsobj.getString("diagnosis_charge"));
                        drawerBean.setPickup_charge(jsobj.getString("pickup_charge"));
                        drawerBean.setModular_reprogramming_charge(jsobj.getString("modular_reprogramming_charge"));
                        String image = GeneralData.LOCAL_IP_IMAGE + jsobj.getString("image");
                        drawerBean.setN_image(image);
                        beanArrayList.add(drawerBean);
                    }
                }

                mAdapter = new ServiceStationAdapter(context, beanArrayList, (ServiceStationInterface) context, str);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return beanArrayList;

    }


    // ** internet con broadcast receiver ** //

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
            if (isConnected) {
                Log.i("LK", "connected");
                displayServiceStationCall();
            } else {
                Log.i("LK", "not connected");

            }
        }
    };

}
