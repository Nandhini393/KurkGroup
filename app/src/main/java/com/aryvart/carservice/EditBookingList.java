package com.aryvart.carservice;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.aryvart.carservice.Adapters.EditServiceAdapter;
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.Edt_Service.ServiceStation_Edit;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.aryvart.carservice.Interfaces.EditBookInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by android2 on 6/2/17.
 */
public class EditBookingList extends Activity implements EditBookInterface {
    ListView listEditService;
    EditServiceAdapter mAdapter;
    Context context;
    GeneralData gD;
    ImageView img_back;
    TextView txt_error,txt_header;
    IntentFilter filter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book_service);
        context = this;
        gD = new GeneralData(context);

        listEditService = (ListView) findViewById(R.id.list_edit_service);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_error=(TextView)findViewById(R.id.txt_error);
        txt_header=(TextView)findViewById(R.id.txt_header);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_header.setTypeface(typeFace1);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        filter1 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, filter1);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void displayEditServicenCall() {
        gD.showAlertDialog(context, "Loading", "Please wait..");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "booking_data.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(UserRegOne.this, response, Toast.LENGTH_LONG).show();
                        try {
                            Log.i("HH", "strResp : " + response);
                            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

                            JSONObject jsobj = new JSONObject(response);

                            Log.i("HH", "strResp : " + response);
                            gD.altDialog.dismiss();


                          if (jsobj.getString("code").equalsIgnoreCase("2")) {

                              JSONObject jsobj1 = jsobj.getJSONObject("bookingdata");
                                JSONArray services_stations = jsobj1.getJSONArray("bookings");

                                if (services_stations.length() > 0) {
                                    listEditService.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < services_stations.length(); i++) {

                                        JSONObject providersServiceJSONobject = services_stations.getJSONObject(i);
                                        CommonBean drawerBean = new CommonBean();
                                        drawerBean.setStr_serviceName(providersServiceJSONobject.getString("service_station_name"));
                                        drawerBean.setStr_date(providersServiceJSONobject.getString("date"));
                                        drawerBean.setStr_serviceName(providersServiceJSONobject.getString("service_station_name"));
                                        drawerBean.setStr_serviceId(providersServiceJSONobject.getString("station_id"));
                                        drawerBean.setStr_serviceAddr(providersServiceJSONobject.getString("address"));
                                        drawerBean.setStr_Response(providersServiceJSONobject.toString());
                                        drawerBean.setStr_serviceBookingId(providersServiceJSONobject.getString("booking_id"));

                                        drawerBean.setStr_ServiceType(providersServiceJSONobject.getString("charge"));
                                        drawerBean.setStr_diagnosis_charge(providersServiceJSONobject.getString("diagnosis_charge"));
                                        drawerBean.setPickup_charge(providersServiceJSONobject.getString("pickup_charge"));
                                        drawerBean.setModular_reprogramming_charge(providersServiceJSONobject.getString("modular_reprogramming_charge"));

                                        drawerBean.setStr_pickUpAddress(providersServiceJSONobject.getString("pickup_address"));
                                        drawerBean.setStr_enableCancelBooking(providersServiceJSONobject.getString("enable"));


                                        String image = GeneralData.LOCAL_IP_IMAGE + providersServiceJSONobject.getString("image");
                                        drawerBean.setN_image(image);
                                        beanArrayList.add(drawerBean);

                                    }
                                }


                                mAdapter = new EditServiceAdapter(context, beanArrayList, (EditBookInterface) context);
                                listEditService.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }
                            else/* if(jsobj.getString("code").equalsIgnoreCase("0"))*/{
                             // listEditService.setVisibility(View.GONE);
                              listEditService.setVisibility(View.GONE);
                              txt_error.setText("No services available");
                              txt_error.setTextColor(Color.parseColor("#808080"));
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
                params.put("registerid", gD.prefs.getString("reg_id", null));

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
        RequestQueue requestQueue = Volley.newRequestQueue(EditBookingList.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }

    @Override
    public void getEditService(String str_stationId,String str_name,String str_address,String str_date,String str_image, String str_bookingId, String str_serviceArray
                               ,String str_ServiceType,String str_pickUpAmt,
                               String str_diagnoAmt,String str_modularAmt,String str_pickUpAddress,String str_enableCancelBook) {
        Log.e("NN_edit", "str_stationId->" + str_stationId);
        Log.e("NN_edit", "str_bookingId->" + str_bookingId);
        Log.e("NN_edit", "str_serviceArray->" + str_serviceArray);


        Intent i = new Intent(EditBookingList.this,ServiceStation_Edit.class);
        SharedPreferences.Editor prefEdit = gD.prefs.edit();
        prefEdit.putString("edit_ss_id", str_stationId);
        prefEdit.putString("edit_ss_book_id", str_bookingId);
        prefEdit.putString("edit_ss_serviceArray", str_serviceArray);
        prefEdit.putString("edit_ss_image", str_image);
        prefEdit.putString("edit_ss_name", str_name);
        prefEdit.putString("edit_ss_addr", str_address);

        prefEdit.putString("edit_ss_serviceType", str_ServiceType);
        prefEdit.putString("edit_ss_pickUpAmt", str_pickUpAmt);
        prefEdit.putString("edit_ss_diagnoAmt", str_diagnoAmt);
        prefEdit.putString("edit_ss_modularAmt", str_modularAmt);

        prefEdit.putString("edit_ss_pickUpAddress", str_pickUpAddress);
        prefEdit.putString("edit_ss_enableCancelBook", str_enableCancelBook);

        i.putExtra("from_edit","value");
        String [] date =str_date.split(" ");
        String str_dateOnly=date[0];
        Log.e("NN_edit", "str_ServiceType->" + str_ServiceType);
        Log.e("NN_edit", "edit_ss_pickUpAddress->" + str_pickUpAddress);
        Log.e("NN_edit", "str_dateOnly->" + str_dateOnly);
        prefEdit.putString("edit_ss_date", str_dateOnly);
        prefEdit.commit();
        startActivity(i);
        finish();
    }

    @Override
    public void getService(String str_id, String str_name, Float price) {

    }

    @Override
    public void delChoosenService(int str_id, String str_name, Float str_price) {

    }
    //internet con broadcast receiver
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
                listEditService.setVisibility(View.VISIBLE);
                displayEditServicenCall();
            } else {
                Log.i("LK", "not connected");
                listEditService.setVisibility(View.GONE);
                txt_error.setText("No response from server.Check your internet connection");
                txt_error.setTextColor(Color.parseColor("#ff0000"));
            }
        }
    };
}
