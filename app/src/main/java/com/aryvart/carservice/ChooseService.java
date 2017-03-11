package com.aryvart.carservice;

import android.app.Activity;
import android.content.ClipData;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.aryvart.carservice.Adapters.ChooseServiceDisplayAdapter;
import com.aryvart.carservice.Adapters.ChooseServiceMainAdapter;
import com.aryvart.carservice.Adapters.EditServiceAdapter;
import com.aryvart.carservice.Adapters.ServiceStationAdapter;
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.aryvart.carservice.Interfaces.ChooseServiceInterface;
import com.aryvart.carservice.Interfaces.EditBookInterface;
import com.aryvart.carservice.Interfaces.ServiceStationInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by android2 on 31/1/17.
 */
public class ChooseService extends Activity implements ChooseServiceInterface {
    Context context;
    //Listview
    private ListView listView, list_serviceDisplay;
    private ChooseServiceAdapter mAdapter;
    private ChooseServiceMainAdapter mAdapter_Main;
    //form field
    LinearLayout ll_oilChange, ll_fullService, ll_modularRep, ll_chooseSerLay;
    TextView txt_oilService, txt_fullService, txt_modularRepText;
    Button btn_confirm;
    int count = 0;
    int countM = 0;
    String str_ServiceStationId;
    RelativeLayout rl_selectServiceStation, rl_selectMainServices;
    LinearLayout ll_displayServicList;
    ImageView img_back;
    ArrayList<String> alCatId = new ArrayList<>();
    ArrayList<CommonBean> lang_list_new;
    //Rest call
    JSONArray jsonArray_my_profile, jsonArray_Main;
    String strChoosenService;
    GeneralData gD;
    String str_EditServiceId;
    ArrayList<CommonBean> beanArrayList;
    ArrayList<String> arrayList_id;
    ArrayList<CommonBean> arrayList;
    TextView txt_amt, txt_choice, txt_header, txt_selectServiceText, txt_errorMsg, txt_selectMainServiceText;
    ImageView img_oil, img_full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_service);
        context = this;
        gD = new GeneralData(context);
        lang_list_new = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_service);
        ll_oilChange = (LinearLayout) findViewById(R.id.ll_quick_service);
        ll_fullService = (LinearLayout) findViewById(R.id.ll_advance_service);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        txt_oilService = (TextView) findViewById(R.id.txt_oilChange);
        txt_fullService = (TextView) findViewById(R.id.txt_fullServ);

        //first drop down

        rl_selectMainServices = (RelativeLayout) findViewById(R.id.rl_selectMain_services);


        //second drop down
        rl_selectServiceStation = (RelativeLayout) findViewById(R.id.rl_select_service_station);


        ll_displayServicList = (LinearLayout) findViewById(R.id.ll_display_service_list);
        list_serviceDisplay = (ListView) findViewById(R.id.list_aadService);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_errorMsg = (TextView) findViewById(R.id.txt_error);


        //first drop down text
        txt_selectMainServiceText = (TextView) findViewById(R.id.txt_selectMainServiceText);

        //second drop down text
        txt_selectServiceText = (TextView) findViewById(R.id.txt_selectServiceText);


        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_amt = (TextView) findViewById(R.id.txt_amt);
        txt_choice = (TextView) findViewById(R.id.txt_choice);
        img_oil = (ImageView) findViewById(R.id.img_oil);
        img_full = (ImageView) findViewById(R.id.img_ful);

        txt_modularRepText = (TextView) findViewById(R.id.txt_modularRepText);
        ll_modularRep = (LinearLayout) findViewById(R.id.ll_modular_rep);
        ll_chooseSerLay = (LinearLayout) findViewById(R.id.ll_chooseservice);

        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_amt.setTypeface(typeFace1);
        txt_choice.setTypeface(typeFace1);
        txt_header.setTypeface(typeFace1);
        txt_oilService.setTypeface(typeFace1);
        txt_fullService.setTypeface(typeFace1);
        btn_confirm.setTypeface(typeFace1);
        txt_modularRepText.setTypeface(typeFace1);


        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
        txt_selectServiceText.setTypeface(typeFace);
        txt_selectMainServiceText.setTypeface(typeFace);


        if (gD.prefs.getString("ss_serviceChoice", null) != null) {
            Log.e("TTB", "ss_serviceChoice" + gD.prefs.getString("ss_serviceChoice", null));
        } else {
            Log.e("TTB", "ss_serviceChoice is null");
        }


        if (gD.prefs.getString("str_serviceType", null).equalsIgnoreCase("modular")) {

            strChoosenService = "3";
            txt_modularRepText.setTextColor(Color.parseColor("#0987ff"));
            jsonArray_Main=null;
            getMainServicesCall(strChoosenService);
            ll_modularRep.setVisibility(View.VISIBLE);
            ll_chooseSerLay.setVisibility(View.GONE);

        } else {

            ll_modularRep.setVisibility(View.GONE);
            ll_chooseSerLay.setVisibility(View.VISIBLE);

            // ** HIGHLIGHT THE ALREADY SELECTED SERVICE ( FULL OR OIL ) ** //

            if (gD.prefs.getString("ss_serviceChoice", null) != null) {
                Log.e("TTB", "ss_serviceChoice" + gD.prefs.getString("ss_serviceChoice", null));
                strChoosenService = gD.prefs.getString("ss_serviceChoice", null);
                jsonArray_Main=null;
                getMainServicesCall(strChoosenService);

                if (strChoosenService.equalsIgnoreCase("1")) {
                    txt_oilService.setTextColor(Color.parseColor("#0987ff"));
                    txt_fullService.setTextColor(Color.parseColor("#000000"));
                } else {
                    txt_oilService.setTextColor(Color.parseColor("#000000"));
                    txt_fullService.setTextColor(Color.parseColor("#0987ff"));
                }

            }

            // ** AT FIRST TIME THE CHOICE WILL BE NULL SO IT WILL SELECT OIL SERVICE BY DEFAULT ** //

            else {

                Log.e("TTB", "ss_serviceChoice is null");
                strChoosenService = "1";
                jsonArray_Main=null;
                getMainServicesCall(strChoosenService);
                txt_oilService.setTextColor(Color.parseColor("#0987ff"));
                txt_fullService.setTextColor(Color.parseColor("#000000"));
            }


        }

        // ** SERVICE STATION ADDRESS ** //

        str_EditServiceId = gD.prefs.getString("ss_id", null);
        Log.i("TTB", "str_EditServiceId" + str_EditServiceId);


        //for saving dats in sharedpreference

        Log.i("PP2", "id" + gD.prefs.getString("ss_id", null));

        Log.i("PP2", "ss_image" + gD.prefs.getString("ss_image", null));

        Log.i("PP2", "ss_addr" + gD.prefs.getString("ss_addr", null));

        Log.i("PP2", "ss_name" + gD.prefs.getString("ss_name", null));

        Log.i("PP2", "ss_date" + gD.prefs.getString("ss_date", null));

        // **saving the ARRAYLIST (SERIVCES ID AND NAME ) value in shared pref for back and edit btn func **//

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("key", null);
        Type type = new TypeToken<ArrayList<CommonBean>>() {
        }.getType();
        arrayList = gson.fromJson(json, type);
        String json_id = sharedPrefs.getString("key_id", null);
        Type type_id = new TypeToken<ArrayList<String>>() {
        }.getType();
        arrayList_id = gson.fromJson(json_id, type_id);

        if (arrayList != null && arrayList_id != null) {
            ll_displayServicList.setVisibility(View.VISIBLE);
            Log.i("TTB", "intial & al_bean**-->" + String.valueOf(arrayList_id.size()));
            Log.i("TTB", "inital & al_bean-->" + String.valueOf(arrayList.size()));
            Log.i("TTB", "intial & id**-->" + String.valueOf(arrayList_id));
            Log.i("TTB", "inital & id-->" + String.valueOf(arrayList));

            ChooseServiceDisplayAdapter mAdap = new ChooseServiceDisplayAdapter(context, arrayList, (ChooseServiceInterface) context);
            list_serviceDisplay.setAdapter(mAdap);
            mAdap.notifyDataSetChanged();

            lang_list_new.addAll(arrayList);
            alCatId.addAll(arrayList_id);

        }

        // ** oil change layout click event ** //

        ll_oilChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (isConnected) {
                    strChoosenService = "1";
                    getMainServicesCall(strChoosenService);
                    txt_oilService.setTextColor(Color.parseColor("#0987ff"));
                    txt_fullService.setTextColor(Color.parseColor("#000000"));

                    if (ll_fullService.isEnabled()) {
                        txt_fullService.setTextColor(Color.parseColor("#000000"));
                    } else {
                        txt_fullService.setTextColor(Color.parseColor("#7a7a7a"));
                    }


                    if (gD.prefs.getString("ss_serviceChoice", null) != null) {

                    /*   *******  CLEAR THE ARRAYLIST ,
                        TEXT OF SERVICE SELECTED AND SUB SERVICE (HIDE) WHEN THE SERVICES ARE INTERCHANGED( CAN CHOOSE EITHER OIL OR FULL NOT BOTH)********  */

                        if (gD.prefs.getString("ss_serviceChoice", null).equalsIgnoreCase("2")) {

                            txt_selectMainServiceText.setText("Select services");

                            txt_selectMainServiceText.setTextColor(Color.parseColor("#6d6d6d"));
                            rl_selectServiceStation.setVisibility(View.GONE);
                            lang_list_new.clear();
                            alCatId.clear();

                            ll_displayServicList.setVisibility(View.GONE);

                            ChooseServiceDisplayAdapter mAdap = new ChooseServiceDisplayAdapter(context, lang_list_new, (ChooseServiceInterface) context);
                            list_serviceDisplay.setAdapter(mAdap);
                            mAdap.notifyDataSetChanged();


                        }

                    }

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });


        ll_fullService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (isConnected) {
                    strChoosenService = "2";
                    getMainServicesCall(strChoosenService);

                    if (ll_oilChange.isEnabled()) {
                        txt_oilService.setTextColor(Color.parseColor("#000000"));
                    } else {
                        txt_oilService.setTextColor(Color.parseColor("#7a7a7a"));
                    }

                    txt_fullService.setTextColor(Color.parseColor("#0987ff"));

                    if (gD.prefs.getString("ss_serviceChoice", null) != null) {

                          /*   *******  CLEAR THE ARRAYLIST ,
                        TEXT OF SERVICE SELECTED AND SUB SERVICE (HIDE) WHEN THE SERVICES ARE INTERCHANGED( CAN CHOOSE EITHER OIL OR FULL NOT BOTH)********  */

                        if (gD.prefs.getString("ss_serviceChoice", null).equalsIgnoreCase("1")) {

                            txt_selectMainServiceText.setText("Select services");

                            txt_selectMainServiceText.setTextColor(Color.parseColor("#6d6d6d"));

                            rl_selectServiceStation.setVisibility(View.GONE);
                            lang_list_new.clear();
                            alCatId.clear();
                            ll_displayServicList.setVisibility(View.GONE);
                            ChooseServiceDisplayAdapter mAdap = new ChooseServiceDisplayAdapter(context, lang_list_new, (ChooseServiceInterface) context);
                            list_serviceDisplay.setAdapter(mAdap);
                            mAdap.notifyDataSetChanged();
                        }

                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });

        // ** IMAGE BACK CODING ** //

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChooseService.this, ServiceStation.class));
                finish();
            }
        });


        // ** FIRST DROP DOWN ** // ( FOR SELECTING SERVICES AS OIL OR FULL )

        rl_selectMainServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("NN", "strChoosenService-->" + strChoosenService);
                if (jsonArray_Main != null) {
                    Log.e("NNO", "jsonArray_Main-->" + jsonArray_Main);
                } else {
                    Log.e("NNO", "jsonArray_Main null");
                }


                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (strChoosenService != null) {
                    if (countM == 0) {

                        if (isConnected) {
                            listView.setVisibility(View.VISIBLE);
                            txt_errorMsg.setVisibility(View.GONE);
                            LoadLayout_Main(jsonArray_Main, strChoosenService);
                        } else {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.VISIBLE);
                            txt_errorMsg.setText("No response from server.Check your internet connection");
                            txt_errorMsg.setTextColor(Color.parseColor("#ff0000"));
                        }

                       /* if (ll_displayServicList.getVisibility() == View.VISIBLE) {
                            ll_displayServicList.setVisibility(View.GONE);
                        }*/
                        countM = 1;
                    } else if (countM == 1) {
                        if (isConnected) {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.GONE);
                            txt_errorMsg.setText("No response from server.Check your internet connection");
                            txt_errorMsg.setTextColor(Color.parseColor("#ff0000"));
                        }

                        countM = 0;
                    }
                } else {
                    Toast.makeText(ChooseService.this, "Choose your service", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // ** SECOND DROP DOWN ** /// ( FOR SELECTING SUB SERVICES --> EITHER FROM OIL OR FULL )

        rl_selectServiceStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("NNJ", "count->" + count);
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (strChoosenService != null) {
                    Log.e("NNJ", "strChoosenService->" + strChoosenService);
                    if (count == 0) {

                        if (isConnected) {
                            listView.setVisibility(View.VISIBLE);
                            txt_errorMsg.setVisibility(View.GONE);
                            LoadLayout(jsonArray_my_profile, strChoosenService);
                        } else {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.VISIBLE);
                            txt_errorMsg.setText("No response from server.Check your internet connection");
                            txt_errorMsg.setTextColor(Color.parseColor("#ff0000"));
                        }

                        count = 1;
                    } else if (count == 1) {
                        if (isConnected) {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.GONE);
                            txt_errorMsg.setText("No response from server.Check your internet connection");
                            txt_errorMsg.setTextColor(Color.parseColor("#ff0000"));
                        }

                        count = 0;
                    }
                } else {
                    Toast.makeText(ChooseService.this, "Choose your service", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ** BUTTON CONFIRM CODE ** //

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (isConnected) {
                    if (lang_list_new.size() == 0) {
                        Toast.makeText(ChooseService.this, "Choose atleast one service", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.i("HHPP", "strChoosenService : " + strChoosenService);
                        Intent i = new Intent(ChooseService.this, ServiceConfirmation.class);
                        SharedPreferences.Editor sp_Prefs = gD.prefs.edit();
                        sp_Prefs.putString("ss_serviceChoice1", gD.prefs.getString("ss_serviceChoice", null));
                        sp_Prefs.commit();

                        //i.putExtra("ss_serviceChoice",strChoosenService);
                        Log.i("HH", "array_list : " + lang_list_new);

                        //Set the values
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String json = gson.toJson(lang_list_new);
                        String json_id = gson.toJson(alCatId);
                        editor.putString("key", json);
                        editor.putString("key_id", json_id);
                        editor.commit();
                        //   i.putExtra("edit_ss_book_id",str_EditBookingId);
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
        startActivity(new Intent(ChooseService.this, ServiceStation.class));
        finish();
    }

    // ** get MAIN service REST call for specific service station ** //

    public void getMainServicesCall(final String strFromChoosen) {
        gD.showAlertDialog(context, "Loading", "Please Wait");
        Log.i("HH", "strFromChoosen : " + strFromChoosen);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "services.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(UserRegOne.this, response, Toast.LENGTH_LONG).show();
                        try {
                            Log.i("HH", "strResp : " + response);
                            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

                            JSONObject jsobj = new JSONObject(response);

                            Log.i("HH", "strResp : " + response);
                            if (jsobj.getString("code").equalsIgnoreCase("2")) {

                               /* listView.setVisibility(View.VISIBLE);
                                txt_errorMsg.setVisibility(View.GONE);*/

                                JSONArray profile_created_by = jsobj.getJSONArray("services");
                                jsonArray_Main = jsobj.getJSONArray("services");

                                Log.i("HHj", "jsonArray_Main : " + jsonArray_Main);

                                if (profile_created_by.length() > 0) {
                                    for (int i = 0; i < profile_created_by.length(); i++) {

                                        JSONObject providersServiceJSONobject = profile_created_by.getJSONObject(i);
                                        CommonBean drawerBean = new CommonBean();
                                        drawerBean.setStr_serviceName(providersServiceJSONobject.getString("service_sub_category_name"));
                                        drawerBean.setN_serviceId(Integer.parseInt(providersServiceJSONobject.getString("service_sub_category")));
                                        //drawerBean.setF_price(Integer.parseInt(providersServiceJSONobject.getString("service_rate")));
                                        beanArrayList.add(drawerBean);
                                    }
                                }

                                mAdapter_Main = new ChooseServiceMainAdapter(context, strFromChoosen, beanArrayList, (ChooseServiceInterface) context);
                                listView.setAdapter(mAdapter_Main);
                                mAdapter_Main.notifyDataSetChanged();
                                //txt_drawer_error_msg.setVisibility(View.GONE);

                                gD.altDialog.dismiss();
                            }
                            else {
                                gD.altDialog.dismiss();
                                jsonArray_Main = null;
                                Log.e("NN", "No services available");
                               /* listView.setVisibility(View.GONE);
                                txt_errorMsg.setVisibility(View.VISIBLE);*/
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChooseService.this, error.toString(), Toast.LENGTH_LONG).show();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));

                                Toast.makeText(ChooseService.this, "res : " + res, Toast.LENGTH_LONG).show();

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


                // ** strFromChoosen denotes the oil charge--> 1 and full service --> 2 **//


                if (strFromChoosen != null) {
                    if (strFromChoosen.equalsIgnoreCase("1")) {
                        params.put("category_id", "1");
                        params.put("station_id", str_EditServiceId);
                    } else if (strFromChoosen.equalsIgnoreCase("2")) {
                        params.put("category_id", "2");
                        params.put("station_id", str_EditServiceId);
                    } else if (strFromChoosen.equalsIgnoreCase("3")) {
                        params.put("category_id", "3");
                        params.put("station_id", str_EditServiceId);
                    }
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "admin", "Surf27@2016").getBytes(), Base64.DEFAULT)));
// params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };

//30Secs
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);


        RequestQueue requestQueue = Volley.newRequestQueue(ChooseService.this);
        requestQueue.add(stringRequest);

    }


    // ** get service REST call for specific service station ** //

    public void getServicesCall(final String strFromChoosen, final String strMainService) {

        gD.showAlertDialog(context, "Loading", "Please Wait");
        Log.i("HH", "strFromChoosen : " + strFromChoosen);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "services.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(UserRegOne.this, response, Toast.LENGTH_LONG).show();
                        try {
                            Log.i("HH", "strResp : " + response);
                            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

                            JSONObject jsobj = new JSONObject(response);

                            Log.i("HH", "strResp : " + response);
                            if (jsobj.getString("code").equalsIgnoreCase("2")) {

                               /* listView.setVisibility(View.VISIBLE);
                                txt_errorMsg.setVisibility(View.GONE);*/

                                JSONArray profile_created_by = jsobj.getJSONArray("services");
                                jsonArray_my_profile = jsobj.getJSONArray("services");

                                if (profile_created_by.length() > 0) {
                                    for (int i = 0; i < profile_created_by.length(); i++) {

                                        JSONObject providersServiceJSONobject = profile_created_by.getJSONObject(i);
                                        CommonBean drawerBean = new CommonBean();
                                        drawerBean.setStr_serviceName(providersServiceJSONobject.getString("service_name"));
                                        drawerBean.setN_serviceId(Integer.parseInt(providersServiceJSONobject.getString("service_id")));
                                        drawerBean.setStr_servicePrice(providersServiceJSONobject.getString("service_rate"));
                                        beanArrayList.add(drawerBean);
                                    }
                                }

                                mAdapter = new ChooseServiceAdapter(context, strFromChoosen, beanArrayList, (ChooseServiceInterface) context);
                                listView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                //txt_drawer_error_msg.setVisibility(View.GONE);
                                gD.altDialog.dismiss();

                            } else {
                                gD.altDialog.dismiss();
                                Log.e("NN", "No services available");
                                jsonArray_my_profile=null;
                               /* listView.setVisibility(View.GONE);
                                txt_errorMsg.setVisibility(View.VISIBLE);*/
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChooseService.this, error.toString(), Toast.LENGTH_LONG).show();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));

                                Toast.makeText(ChooseService.this, "res : " + res, Toast.LENGTH_LONG).show();

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


                // ** strFromChoosen denotes the oil charge--> 1 and full service --> 2 **//


                if (strFromChoosen != null) {
                    if (strFromChoosen.equalsIgnoreCase("1")) {
                        params.put("category_id", "1");
                        params.put("station_id", str_EditServiceId);
                        params.put("service_sub_id", strMainService);


                    } else if (strFromChoosen.equalsIgnoreCase("2")) {
                        params.put("category_id", "2");
                        params.put("station_id", str_EditServiceId);
                        params.put("station_id", str_EditServiceId);
                        params.put("service_sub_id", strMainService);

                    } else if (strFromChoosen.equalsIgnoreCase("3")) {
                        params.put("category_id", "3");
                        params.put("station_id", str_EditServiceId);
                        params.put("service_sub_id", strMainService);

                    }
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "admin", "Surf27@2016").getBytes(), Base64.DEFAULT)));
// params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };

//30Secs
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);


        RequestQueue requestQueue = Volley.newRequestQueue(ChooseService.this);
        requestQueue.add(stringRequest);

    }


    // ** Load layout (first drop down ) to save the services respose ** //

    private ArrayList<CommonBean> LoadLayout_Main(JSONArray providerServicesMonth, String strFromChoosen) {
        Log.e("LL", "hiii");
        Log.e("NNJ", "providerServicesMonth***->" + providerServicesMonth);

        ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();
        JSONObject jsobj = null;

        if (providerServicesMonth != null) {
            try {
                listView.setVisibility(View.VISIBLE);
                txt_errorMsg.setVisibility(View.GONE);
                if (providerServicesMonth.length() > 0) {
                    for (int i = 0; i < providerServicesMonth.length(); i++) {
                        jsobj = providerServicesMonth.getJSONObject(i);
                        CommonBean drawerBean = new CommonBean();
                        drawerBean.setStr_serviceName(jsobj.getString("service_sub_category_name"));
                        drawerBean.setN_serviceId(Integer.parseInt(jsobj.getString("service_sub_category")));
                        beanArrayList.add(drawerBean);

                    }
                }

                mAdapter_Main = new ChooseServiceMainAdapter(context, strFromChoosen, beanArrayList, (ChooseServiceInterface) context);
                listView.setAdapter(mAdapter_Main);
                mAdapter_Main.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listView.setVisibility(View.GONE);
            txt_errorMsg.setVisibility(View.VISIBLE);
        }

        return beanArrayList;

    }


    // ** Load layout (second drop down ) to save the services respose ** //

    private ArrayList<CommonBean> LoadLayout(JSONArray providerServicesMonth, String strFromChoosen) {
        Log.e("NNJ", "strChoosenService->" + strChoosenService);
        Log.e("NNJ", "providerServicesMonth->" + providerServicesMonth);
        ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

        JSONObject jsobj = null;

        if (providerServicesMonth != null) {
            try {
                listView.setVisibility(View.VISIBLE);
                txt_errorMsg.setVisibility(View.GONE);
                if (providerServicesMonth.length() > 0) {
                    for (int i = 0; i < providerServicesMonth.length(); i++) {
                        jsobj = providerServicesMonth.getJSONObject(i);
                        CommonBean drawerBean = new CommonBean();
                        drawerBean.setStr_serviceName(jsobj.getString("service_name"));
                        drawerBean.setN_serviceId(Integer.parseInt(jsobj.getString("service_id")));
                        drawerBean.setStr_servicePrice(jsobj.getString("service_rate"));
                        beanArrayList.add(drawerBean);

                    }
                }

                mAdapter = new ChooseServiceAdapter(context, strFromChoosen, beanArrayList, (ChooseServiceInterface) context);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listView.setVisibility(View.GONE);
            txt_errorMsg.setVisibility(View.VISIBLE);
        }

        return beanArrayList;

    }


    // ** while selecting add data to arraylist ** // ( FOR SECOND DROP DOWN )

    @Override
    public void getServiceStationAddress(String str_id, String str_serviceType, String str_name, String f_price) {
        Log.i("NAN", "str_serviceType-->" + strChoosenService);
        Log.i("NAN", "str_serviceType***-->" + str_serviceType);

        str_ServiceStationId = str_id;

        listView.setVisibility(View.GONE);

        count = 0;  // SET THE COUNT VALUE SO THAT IT WILL  OPEN AGAIN AFTER THE LISTVIEW IS GONE ( rl_selectServiceStation --> click listner)

        txt_errorMsg.setVisibility(View.GONE);
        ll_displayServicList.setVisibility(View.VISIBLE);
        Log.i("ver1", "f_priceAdd-->" + f_price);


        // ** DISABLE THE FULL SERVICES IF OIL DATAS ARE SELECTED  IN ARRAY ** //

        if (str_serviceType.equalsIgnoreCase("1")) {
            ll_fullService.setEnabled(false);
            ll_oilChange.setEnabled(true);
            txt_fullService.setTextColor(Color.parseColor("#7a7a7a"));
            img_full.setBackgroundResource(R.drawable.full_grey);

        }

        // ** DISABLE THE OIL SERVICES IF FULL DATAS ARE SELECTED IN ARRAY ** //

        else if (str_serviceType.equalsIgnoreCase("2")) {
            ll_oilChange.setEnabled(false);
            ll_fullService.setEnabled(true);
            txt_oilService.setTextColor(Color.parseColor("#7a7a7a"));
            img_oil.setBackgroundResource(R.drawable.oil_grey);

        }

        if (gD.prefs.getString("ss_serviceChoice", null) != null) {


            // ** CHECK THE SERVICE TYPE AND CLEAR THE ARRAY ACCORDINGLY ** //


            // ** If already selected value and currently chosen value are same then add datas to the same arraylist ** //

            if (gD.prefs.getString("ss_serviceChoice", null).equalsIgnoreCase(str_serviceType)) {
                ArrayList<CommonBean> al = new ArrayList<CommonBean>(lang_list_new);
                if (al.size() == 0) {
                    CommonBean serviceBean = new CommonBean();
                    serviceBean.setStr_serviceName(str_name);
                    serviceBean.setN_serviceId(Integer.parseInt(str_id));
                    serviceBean.setStr_servicePrice(f_price);
                    lang_list_new.add(serviceBean);

                    alCatId.add(String.valueOf(str_id));


                    Set<CommonBean> hs = new LinkedHashSet<>();
                    hs.addAll(lang_list_new);
                    lang_list_new.clear();
                    lang_list_new.addAll(hs);


                    Log.i("TT", "add & al_bean-->" + String.valueOf(lang_list_new.size()));
                    Log.i("TT", "add & bean-->" + String.valueOf(lang_list_new));
                    Log.i("TT", "checkk->" + String.valueOf(serviceBean.getStr_servicePrice()));
                    Set<String> hs1 = new LinkedHashSet<>();
                    hs1.addAll(alCatId);
                    alCatId.clear();
                    alCatId.addAll(hs1);
                    Log.i("ver", "Adding--->" + Integer.parseInt(str_id));
                    Log.i("ver", "Array Size--->" + alCatId.size());


                } else {
                    Log.i("ver1", "f_priceAddElse-->" + f_price);

                    CommonBean serviceBean = new CommonBean();
                    serviceBean.setStr_serviceName(str_name);
                    serviceBean.setN_serviceId(Integer.parseInt(str_id));
                    serviceBean.setStr_servicePrice(f_price);
                    lang_list_new.add(serviceBean);

                    alCatId.add(String.valueOf(str_id));

                    Set<CommonBean> hs = new LinkedHashSet<>();
                    hs.addAll(lang_list_new);
                    lang_list_new.clear();
                    lang_list_new.addAll(hs);
                    Log.i("TT", "add & al_bean-->" + String.valueOf(lang_list_new.size()));
                    Log.i("TT", "add & bean-->" + String.valueOf(lang_list_new));

                    Set<String> hs1 = new LinkedHashSet<>();
                    hs1.addAll(alCatId);
                    alCatId.clear();
                    alCatId.addAll(hs1);

                }
                ChooseServiceDisplayAdapter mAdap = new ChooseServiceDisplayAdapter(context, lang_list_new, (ChooseServiceInterface) context);
                list_serviceDisplay.setAdapter(mAdap);
                mAdap.notifyDataSetChanged();
            }

            // ** If already selected value and currently chosen value are NOT SAME  THEN CLEAR THE ARRAYLIST and add the new data to arraylist ** //

            else {

                lang_list_new.clear();
                alCatId.clear();

                ChooseServiceDisplayAdapter mAdapp = new ChooseServiceDisplayAdapter(context, lang_list_new, (ChooseServiceInterface) context);
                list_serviceDisplay.setAdapter(mAdapp);
                mAdapp.notifyDataSetChanged();

                SharedPreferences.Editor sp_pref = gD.prefs.edit();
                sp_pref.putString("ss_serviceChoice", str_serviceType);
                sp_pref.commit();


                ArrayList<CommonBean> al = new ArrayList<CommonBean>(lang_list_new);
                if (al.size() == 0) {
                    CommonBean serviceBean = new CommonBean();
                    serviceBean.setStr_serviceName(str_name);
                    serviceBean.setN_serviceId(Integer.parseInt(str_id));
                    serviceBean.setStr_servicePrice(f_price);
                    lang_list_new.add(serviceBean);

                    alCatId.add(String.valueOf(str_id));


                    Set<CommonBean> hs = new LinkedHashSet<>();
                    hs.addAll(lang_list_new);
                    lang_list_new.clear();
                    lang_list_new.addAll(hs);


                    Log.i("TT", "add & al_bean-->" + String.valueOf(lang_list_new.size()));
                    Log.i("TT", "add & bean-->" + String.valueOf(lang_list_new));
                    Set<String> hs1 = new LinkedHashSet<>();
                    hs1.addAll(alCatId);
                    alCatId.clear();
                    alCatId.addAll(hs1);
                    Log.i("ver", "Adding--->" + Integer.parseInt(str_id));
                    Log.i("ver", "Array Size--->" + alCatId.size());


                } else {
                    Log.i("ver1", "f_priceAddElse-->" + f_price);

                    CommonBean serviceBean = new CommonBean();
                    serviceBean.setStr_serviceName(str_name);
                    serviceBean.setN_serviceId(Integer.parseInt(str_id));
                    serviceBean.setStr_servicePrice(f_price);
                    lang_list_new.add(serviceBean);

                    alCatId.add(String.valueOf(str_id));

                    Set<CommonBean> hs = new LinkedHashSet<>();
                    hs.addAll(lang_list_new);
                    lang_list_new.clear();
                    lang_list_new.addAll(hs);
                    Log.i("TT", "add & al_bean-->" + String.valueOf(lang_list_new.size()));
                    Log.i("TT", "add & bean-->" + String.valueOf(lang_list_new));

                    Set<String> hs1 = new LinkedHashSet<>();
                    hs1.addAll(alCatId);
                    alCatId.clear();
                    alCatId.addAll(hs1);

                }
                ChooseServiceDisplayAdapter mAdap = new ChooseServiceDisplayAdapter(context, lang_list_new, (ChooseServiceInterface) context);
                list_serviceDisplay.setAdapter(mAdap);
                mAdap.notifyDataSetChanged();
            }
        }


    }

    // ** while clicking cross btn delete from arraylist and also from the view ** //

    @Override
    public void delChoosenService(int str_id, String str_name, String f_price) {
        ArrayList<CommonBean> al = new ArrayList<CommonBean>(lang_list_new);
        Log.i("ver1", "f_priceDel-->" + f_price);
        for (int i = 0; i < al.size(); i++) {

            CommonBean lb = al.get(i);
            if (lb.getN_serviceId() == str_id) {
                al.remove(lb);
            }

        }
        ArrayList<CommonBean> treelist = new ArrayList<CommonBean>(al);
        lang_list_new = treelist;

        Set<CommonBean> hs = new LinkedHashSet<>();
        hs.addAll(lang_list_new);
        lang_list_new.clear();
        lang_list_new.addAll(hs);

        Log.i("GHK", String.valueOf(lang_list_new.size()));


        // **  WHILE DELETING THE VALUES IN ARRAYLIST USING CROSS IMAGE **//

        // ** check whether the arraylist is empty , if it is then enable both the services ( oil and full ) ** //

        if (lang_list_new.size() == 0) {

            ll_displayServicList.setVisibility(View.GONE);
            ll_fullService.setEnabled(true);
            ll_oilChange.setEnabled(true);


            rl_selectServiceStation.setVisibility(View.GONE);

            txt_selectMainServiceText.setText("Select services");
            txt_selectMainServiceText.setTextColor(Color.parseColor("#6d6d6d"));

            if (gD.prefs.getString("str_serviceType", null).equalsIgnoreCase("modular")) {

                txt_oilService.setTextColor(Color.parseColor("#0987ff"));
                strChoosenService = "3";
                getMainServicesCall(strChoosenService);
            } else {

                txt_oilService.setTextColor(Color.parseColor("#0987ff"));
                strChoosenService = "1";
                getMainServicesCall(strChoosenService);
            }


            txt_fullService.setTextColor(Color.parseColor("#000000"));
            img_full.setBackgroundResource(R.drawable.car_service);
            img_oil.setBackgroundResource(R.drawable.oil_service);


        }


        ArrayList<String> alCID = new ArrayList<String>(alCatId);
        Log.i("TT", "del & alCatId-->" + String.valueOf(alCatId));

        for (int i = 0; i < alCID.size(); i++) {
            Log.i("TT", "del & alCID-->" + String.valueOf(alCID));
            Log.i("TT", "del & alCID.get(i)-->" + String.valueOf(alCID.get(i)));
            Log.i("TT", "del & str_id-->" + String.valueOf(str_id));
            if (String.valueOf(str_id).equalsIgnoreCase(alCID.get(i))) {


                alCID.remove(i);
                Log.i("TT", "removed->");

            } else {
                Log.i("TT", "not removed->");
            }
        }

        ArrayList<String> treeCat = new ArrayList<String>(alCID);
        alCatId = treeCat;
        Set<String> hs1 = new LinkedHashSet<>();
        hs1.addAll(alCatId);
        alCatId.clear();
        alCatId.addAll(hs1);


    }


    // ** while selecting add data to arraylist ** // ( FOR FIRST DROP DOWN )

    @Override
    public void getServiceStationMainAddress(String str_id, String str_service_type, String str_name) {

        Log.i("TT", "str_id->" + str_id);
        Log.i("TT", "str_service_type->" + str_service_type);
        Log.i("TT", "str_name->" + str_name);


        // SET THE COUNT VALUE TO " 0 "  SO THAT IT WILL  OPEN AGAIN AFTER THE LISTVIEW IS GONE ( rl_selectServiceMainStation --> click listner)

        countM = 0;
        txt_selectMainServiceText.setText(str_name);

        txt_selectMainServiceText.setTextColor(Color.parseColor("#0987ff"));

        listView.setVisibility(View.GONE);
        rl_selectServiceStation.setVisibility(View.VISIBLE);


        SharedPreferences.Editor sp_pref = gD.prefs.edit();
        sp_pref.putString("ss_serviceChoice", str_service_type);
        sp_pref.commit();


        getServicesCall(str_service_type, str_id);


    }

}
