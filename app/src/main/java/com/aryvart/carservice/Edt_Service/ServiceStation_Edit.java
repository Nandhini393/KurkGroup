package com.aryvart.carservice.Edt_Service;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
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
import com.aryvart.carservice.Adapters.ChooseServiceDisplayAdapter;
import com.aryvart.carservice.Adapters.ServiceStationAdapter;
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.ChooseService;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.aryvart.carservice.Interfaces.ChooseServiceInterface;
import com.aryvart.carservice.Interfaces.ServiceStationInterface;
import com.aryvart.carservice.R;
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
public class ServiceStation_Edit extends Activity implements ServiceStationInterface {
    Context context;
    //Listview
    private List<CommonBean> serviceList = new ArrayList<>();
    private ListView listView;
    private ServiceStationAdapter mAdapter;
    //calen
    ImageView img_calen;
    TextView txt_date;
    TextView txt_next;
    String str_EditBookingId, str_EditServiceId;
    //DrawerLayout
    DrawerLayout myDrawerLayout;
    LinearLayout myLinearLayout;
    ImageView img_back;
    String str_ServiceStationId = "", str_from_edit;
    //form fields
    RelativeLayout ll_selectServiceStation;
    int count = 0;
    GeneralData gD;
    TextView txt_headerName, txt_title;
    LinearLayout ll_dispStastions;
    ImageView img_StationImage;
    TextView txt_ssAddr, txt_ssName;
    ImageLoader imgLoader;
    ArrayList<CommonBean> beanArrayList;
    ArrayList<String> beanIdList;

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
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_headerName.setTypeface(typeFace1);
        txt_next.setTypeface(typeFace1);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
        txt_date.setTypeface(typeFace);


        String str_txt_span = "SELECT SHELL SERVICE STATION";
        SpannableString spannableString = new SpannableString(str_txt_span);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0987ff")), 0, 12, 0);
        txt_title.setText(spannableString);
        txt_title.setTypeface(typeFace1);


        str_from_edit = getIntent().getStringExtra("from_edit");

        if (str_from_edit != null) {


            Log.i("PP", "str_from_edit" + str_from_edit);
        }

        if (gD.prefs.getString("edit_ss_serviceArray", null) != null) {

            ll_dispStastions.setVisibility(View.VISIBLE);
            str_ServiceStationId = gD.prefs.getString("edit_ss_id", null);
            imgLoader.DisplayImage(gD.prefs.getString("edit_ss_image", null), img_StationImage);
            txt_ssAddr.setText(gD.prefs.getString("edit_ss_addr", null));
            txt_ssName.setText(gD.prefs.getString("edit_ss_name", null));
            txt_date.setText(gD.prefs.getString("edit_ss_date", null));

            Log.i("PP", "edit_ss_id" + gD.prefs.getString("edit_ss_id", null));

            Log.i("PP", "edit_ss_image" + gD.prefs.getString("edit_ss_image", null));

            Log.i("PP", "edit_ss_addr" + gD.prefs.getString("edit_ss_addr", null));

            Log.i("PP", "edit_ss_name" + gD.prefs.getString("edit_ss_name", null));

            Log.i("PP", "edit_ss_date" + gD.prefs.getString("edit_ss_date", null));

        }

        ll_selectServiceStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceStation_Edit.this, "You cant edit this service station", Toast.LENGTH_SHORT).show();
               /* if (count == 0) {
                    listView.setVisibility(View.VISIBLE);
                    displayServiceStationCall();
                    // prepareMovieData();
                    count = 1;
                } else if (count == 1) {
                    listView.setVisibility(View.GONE);
                    count = 0;
                }*/
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefEdit = gD.prefs.edit();
                prefEdit.putString("edit_ss_id", null);
                prefEdit.putString("edit_ss_book_id", null);
                prefEdit.putString("edit_ss_serviceArray", null);
                prefEdit.putString("edit_ss_image", null);
                prefEdit.putString("edit_ss_name", null);
                prefEdit.putString("edit_ss_addr", null);
                prefEdit.putString("edit_ss_date", null);
                prefEdit.putString("edit_ss_serviceType", null);
                prefEdit.putString("edit_ss_pickUpAmt", null);
                prefEdit.putString("edit_ss_diagnoAmt", null);
                prefEdit.putString("edit_ss_modularAmt", null);

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
        img_calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                if (isConnected) {
                    SharedPreferences.Editor prefEdit = gD.prefs.edit();
                    prefEdit.putString("edit_ss_date", txt_date.getText().toString().trim());
                    prefEdit.commit();
                    if (str_ServiceStationId == "") {
                        Toast.makeText(ServiceStation_Edit.this, "Select your service station", Toast.LENGTH_SHORT).show();
                    } else if (txt_date.getText().toString().trim().length() == 0) {
                        Toast.makeText(ServiceStation_Edit.this, "Select your service date", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(ServiceStation_Edit.this, ChooseService_Edit.class);
                        //  i.putExtra("edit_ss_id",gD.prefs.getString("edit_ss_id", null));

                        if (str_from_edit != null) {
                            if (str_from_edit.equalsIgnoreCase("value")) {
                                if (gD.prefs.getString("edit_ss_serviceArray", null) != null) {


                                    Log.e("NN_editC", "str_serviceArray->" + gD.prefs.getString("edit_ss_serviceArray", null));
                                    beanArrayList = new ArrayList<CommonBean>();
                                    beanIdList = new ArrayList<String>();

                                    try {
                                        JSONObject jsobj = new JSONObject(gD.prefs.getString("edit_ss_serviceArray", null));

                                        Log.i("HH", "strResp : " + gD.prefs.getString("edit_ss_serviceArray", null));
                                        // if (jsobj.getString("code").equalsIgnoreCase("2")) {
                                        JSONArray services_stations = jsobj.getJSONArray("services");
                                        str_EditBookingId = jsobj.getString("booking_id");
                                        if (str_EditServiceId == null) {
                                            str_EditServiceId = jsobj.getString("station_id");
                                        }
                                        if (gD.prefs.getString("edit_ss_serviceArray", null).length() > 0) {

                                            for (int j = 0; j < services_stations.length(); j++) {
                                                CommonBean drawerBean = new CommonBean();
                                                drawerBean.setStr_serviceName(services_stations.getJSONObject(j).getString("service_name"));
                                                drawerBean.setN_serviceId(Integer.parseInt(services_stations.getJSONObject(j).getString("service_id")));
                                                drawerBean.setF_price(Integer.parseInt(services_stations.getJSONObject(j).getString("service_rate")));
                                                beanArrayList.add(drawerBean);
                                                beanIdList.add(services_stations.getJSONObject(j).getString("service_id"));

                                            }
                                        }

                                        Log.i("HH_edit", "lang_list_new : " + beanArrayList.toString());
                                        Log.i("HH_edit", "alCatId : " + beanIdList);

                                        //Set the values
                                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = sharedPrefs.edit();
                                        Gson gson = new Gson();

                                        String json = gson.toJson(beanArrayList);
                                        String json_id = gson.toJson(beanIdList);
                                        editor.putString("key", json);
                                        editor.putString("key_id", json_id);
                                        editor.commit();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        } else {
                            Log.i("HH_edit", "else--> : ");


                        }


                        startActivity(i);
                        finish();

                    }
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
        prefEdit.putString("edit_ss_id", null);
        prefEdit.putString("edit_ss_book_id", null);
        prefEdit.putString("edit_ss_serviceArray", null);
        prefEdit.putString("edit_ss_image", null);
        prefEdit.putString("edit_ss_name", null);
        prefEdit.putString("edit_ss_addr", null);
        prefEdit.putString("edit_ss_date", null);
        prefEdit.putString("edit_ss_serviceType", null);
        prefEdit.putString("edit_ss_pickUpAmt", null);
        prefEdit.putString("edit_ss_diagnoAmt", null);
        prefEdit.putString("edit_ss_modularAmt", null);

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
//calendar

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        Calendar c = Calendar.getInstance();
        c.set(1945, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

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

    @Override
    public void getServiceStationAddress(String str_id, String str_image, String str_name, String str_address, String str_diagno_charge, String str_pickUP_charge, String str_modular_charge) {

        // add coing for disable event

        str_ServiceStationId = str_id;
        listView.setVisibility(View.GONE);
        ll_dispStastions.setVisibility(View.VISIBLE);
        imgLoader.DisplayImage(str_image, img_StationImage);
        txt_ssAddr.setText(str_address);
        txt_ssName.setText(str_name);

        //Toast.makeText(ServiceStation.this, "id-" + str_id + "\n" + "name-" + str_name, Toast.LENGTH_SHORT).show();
    }

    public void displayServiceStationCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "service_station.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(UserRegOne.this, response, Toast.LENGTH_LONG).show();
                        try {
                            Log.i("HH", "strResp : " + response);
                            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

                            JSONObject jsobj = new JSONObject(response);

                            Log.i("HH", "strResp : " + response);
                            if (jsobj.getString("code").equalsIgnoreCase("2")) {
                                JSONArray services_stations = jsobj.getJSONArray("service_stations");
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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceStation_Edit.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }
}
