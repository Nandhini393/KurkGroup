package com.aryvart.carservice.Edt_Service;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.aryvart.carservice.Interfaces.ChooseServiceInterface;
import com.aryvart.carservice.R;
import com.aryvart.carservice.ServiceConfirmation;
import com.aryvart.carservice.ServiceStation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by android2 on 31/1/17.
 */
public class ChooseService_Edit extends Activity implements ChooseServiceInterface {
    Context context;
    //Listview
    private List<CommonBean> chooseList = new ArrayList<>();
    private ListView listView, list_serviceDisplay;
    private ChooseServiceAdapter mAdapter;
    //form field
    LinearLayout ll_oilChange, ll_fullService;
    TextView txt_oilService, txt_fullService;
    Button btn_confirm;
    int count = 0;
    String str_ServiceStationId;
    RelativeLayout rl_selectServiceStation;
    LinearLayout ll_displayServicList;
    ImageView img_back;
    ArrayList<String> alCatId = new ArrayList<>();
    ArrayList<CommonBean> lang_list_new;
    //Rest call
    JSONArray jsonArray_my_profile;
    String strChoosenService;
    GeneralData gD;
    String str_EditBookingId, str_EditServiceId, str_EditServiceArrayResp;
    ArrayList<CommonBean> beanArrayList;
    ArrayList<String> beanIdList;
    ArrayList<String> arrayList_id;
    ArrayList<CommonBean> arrayList;
    TextView txt_amt,txt_choice,txt_header,txt_selectServiceText,txt_errorMsg;;
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
        rl_selectServiceStation = (RelativeLayout) findViewById(R.id.rl_select_service_station);
        ll_displayServicList = (LinearLayout) findViewById(R.id.ll_display_service_list);
        list_serviceDisplay = (ListView) findViewById(R.id.list_aadService);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_errorMsg = (TextView) findViewById(R.id.txt_error);
        txt_selectServiceText=(TextView)findViewById(R.id.txt_selectServiceText);
        txt_header=(TextView)findViewById(R.id.txt_header);
        txt_amt = (TextView) findViewById(R.id.txt_amt);
        txt_choice = (TextView) findViewById(R.id.txt_choice);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_amt.setTypeface(typeFace1);
        txt_choice.setTypeface(typeFace1);
        txt_header.setTypeface(typeFace1);
        txt_oilService.setTypeface(typeFace1);
        txt_fullService.setTypeface(typeFace1);
        btn_confirm.setTypeface(typeFace1);


        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
        txt_selectServiceText.setTypeface(typeFace);

        //  str_EditBookingId=gD.prefs.getString("edit_ss_id", null);
        //for saving dats in sharedpreference

        strChoosenService = "1";
        getServicesCall(strChoosenService);
        txt_oilService.setTextColor(Color.parseColor("#0987ff"));
        txt_fullService.setTextColor(Color.parseColor("#000000"));




        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("key", null);
        Type type = new TypeToken<ArrayList<CommonBean>>() {
        }.getType();
        arrayList = gson.fromJson(json, type);

// Log.e("NNOP", String.valueOf(arrayList));

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

       /* str_EditServiceArrayResp = getIntent().getStringExtra("edit_ss_serviceArray");


       if (str_EditServiceArrayResp != null) {
           ll_displayServicList.setVisibility(View.VISIBLE);



            Log.e("NN_editC", "str_serviceArray->" + str_EditServiceArrayResp);
           beanArrayList = new ArrayList<CommonBean>();
           beanIdList = new ArrayList<String>();

            try {
                JSONObject jsobj = new JSONObject(str_EditServiceArrayResp);

                Log.i("HH", "strResp : " + str_EditServiceArrayResp);
                // if (jsobj.getString("code").equalsIgnoreCase("2")) {
              JSONArray services_stations = jsobj.getJSONArray("services");
                str_EditBookingId = jsobj.getString("booking_id");
                if(str_EditServiceId==null){
                    str_EditServiceId=jsobj.getString("station_id");
                }
                if (str_EditServiceArrayResp.length() > 0) {

                    for (int i = 0; i < services_stations.length(); i++) {
                        CommonBean drawerBean = new CommonBean();
                        drawerBean.setStr_serviceName(services_stations.getJSONObject(i).getString("service_name"));
                        drawerBean.setN_serviceId(Integer.parseInt(services_stations.getJSONObject(i).getString("service_id")));
                        drawerBean.setF_price(Integer.parseInt(services_stations.getJSONObject(i).getString("service_rate")));
                        beanArrayList.add(drawerBean);
                        beanIdList.add(services_stations.getJSONObject(i).getString("service_id"));

                    }
                }
                ChooseServiceDisplayAdapter mAdap = new ChooseServiceDisplayAdapter(context, beanArrayList, (ChooseServiceInterface) context);
                list_serviceDisplay.setAdapter(mAdap);
                mAdap.notifyDataSetChanged();
                lang_list_new.addAll(beanArrayList);
                alCatId.addAll(beanIdList);

                Log.i("HH_edit", "lang_list_new : " + lang_list_new.toString());
                Log.i("HH_edit", "alCatId : " + alCatId);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*/
        ll_oilChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (isConnected) {
                    strChoosenService = "1";
                    getServicesCall(strChoosenService);
                    txt_oilService.setTextColor(Color.parseColor("#0987ff"));
                    txt_fullService.setTextColor(Color.parseColor("#000000"));
                }
                else {
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
                    getServicesCall(strChoosenService);
                    txt_oilService.setTextColor(Color.parseColor("#000000"));
                    txt_fullService.setTextColor(Color.parseColor("#0987ff"));
                }
                else {
                    Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseService_Edit.this,ServiceStation_Edit.class));
                finish();
            }
        });
        rl_selectServiceStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (strChoosenService != null) {
                    if (count == 0) {
                        if (isConnected) {
                            listView.setVisibility(View.VISIBLE);
                            LoadLayout(jsonArray_my_profile, "Response");
                        }
                        else {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.VISIBLE);
                            txt_errorMsg.setText("No response from server.Check your internet connection");
                            txt_errorMsg.setTextColor(Color.parseColor("#ff0000"));
                        }
                       /* if (ll_displayServicList.getVisibility() == View.VISIBLE) {
                            ll_displayServicList.setVisibility(View.GONE);
                        }*/

                        count = 1;
                    } else if (count == 1) {
                        if (isConnected) {
                            listView.setVisibility(View.GONE);
                        }
                        else {
                            listView.setVisibility(View.GONE);
                            txt_errorMsg.setVisibility(View.GONE);
                            txt_errorMsg.setText("No response from server.Check your internet connection");
                            txt_errorMsg.setTextColor(Color.parseColor("#ff0000"));
                        }
                        count = 0;
                    }
                } else {
                    Toast.makeText(ChooseService_Edit.this, "Choose your service", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                if (isConnected) {
                    if (lang_list_new.size() == 0) {
                        Toast.makeText(ChooseService_Edit.this, "Choose atleast one service", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(ChooseService_Edit.this, ServiceConfirmation_Edit.class);
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
                        //i.putExtra("edit_ss_book_id",str_EditBookingId);
                        startActivity(i);
                        finish();
                    }
                }
                else {
                    Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChooseService_Edit.this,ServiceStation_Edit.class));
        finish();
    }

    public void getServicesCall(final String strFromChoosen) {
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

                                JSONArray profile_created_by = jsobj.getJSONArray("services");
                                jsonArray_my_profile = jsobj.getJSONArray("services");

                                if (profile_created_by.length() > 0) {
                                    for (int i = 0; i < profile_created_by.length(); i++) {

                                        JSONObject providersServiceJSONobject = profile_created_by.getJSONObject(i);
                                        CommonBean drawerBean = new CommonBean();
                                        drawerBean.setStr_serviceName(providersServiceJSONobject.getString("service_name"));
                                        drawerBean.setN_serviceId(Integer.parseInt(providersServiceJSONobject.getString("service_id")));
                                        drawerBean.setF_price(Integer.parseInt(providersServiceJSONobject.getString("service_rate")));
                                        beanArrayList.add(drawerBean);
                                    }
                                }

                                mAdapter = new ChooseServiceAdapter(context, beanArrayList, (ChooseServiceInterface) context);
                                listView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                //txt_drawer_error_msg.setVisibility(View.GONE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         Toast.makeText(ChooseService_Edit.this, error.toString(), Toast.LENGTH_LONG).show();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));

                                Toast.makeText(ChooseService_Edit.this, "res : " + res, Toast.LENGTH_LONG).show();

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

                // station_id,category_id
                if (strFromChoosen != null) {
                    if (strFromChoosen.equalsIgnoreCase("1")) {
                        params.put("category_id", "1");
                        params.put("station_id", gD.prefs.getString("edit_ss_id", null));
                    } else if (strFromChoosen.equalsIgnoreCase("2")) {
                        params.put("category_id", "2");
                        params.put("station_id", gD.prefs.getString("edit_ss_id", null));
                    }
                }


//username=sathish@ansjad.com&password=testing

               /* params.put("username", edt_email.getText().toString().trim());
                params.put("password", edt_password.getText().toString().trim());*/

// params.put("username", "dineshW@adjhd.com");
// params.put("password", "123456");
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


        RequestQueue requestQueue = Volley.newRequestQueue(ChooseService_Edit.this);
        requestQueue.add(stringRequest);

    }

    private ArrayList<CommonBean> LoadLayout(JSONArray providerServicesMonth, String stridentifyEdit) {
        ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

        JSONObject jsobj = null;

        if (providerServicesMonth != null) {
            try {

                if (providerServicesMonth.length() > 0) {
                    for (int i = 0; i < providerServicesMonth.length(); i++) {
                        jsobj = providerServicesMonth.getJSONObject(i);
                        CommonBean drawerBean = new CommonBean();
                        drawerBean.setStr_serviceName(jsobj.getString("service_name"));
                        drawerBean.setN_serviceId(Integer.parseInt(jsobj.getString("service_id")));
                        drawerBean.setF_price(Integer.parseInt(jsobj.getString("service_rate")));
                        beanArrayList.add(drawerBean);

                    }
                }

                mAdapter = new ChooseServiceAdapter(context, beanArrayList, (ChooseServiceInterface) context);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return beanArrayList;

    }

    @Override
    public void getServiceStationAddress(String str_id, String str_name, Float f_price) {
        str_ServiceStationId = str_id;
        listView.setVisibility(View.GONE);
        ll_displayServicList.setVisibility(View.VISIBLE);
        Log.i("ver1", "f_priceAdd-->" + f_price);
        ArrayList<CommonBean> al = new ArrayList<CommonBean>(lang_list_new);
        if (al.size() == 0) {
            CommonBean serviceBean = new CommonBean();
            serviceBean.setStr_serviceName(str_name);
            serviceBean.setN_serviceId(Integer.parseInt(str_id));
            serviceBean.setF_price(f_price);
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
            /*for (int c = 0; c < al.size(); c++) {

            }*/

          /*  for (int c = 0; c < al.size(); c++) {
                if (al.get(c).getStr_serviceName().equalsIgnoreCase(str_name)) {
                    Toast.makeText(context, "Category already selected", Toast.LENGTH_SHORT).show();
                } else {*/
            CommonBean serviceBean = new CommonBean();
            serviceBean.setStr_serviceName(str_name);
            serviceBean.setN_serviceId(Integer.parseInt(str_id));
            serviceBean.setF_price(f_price);
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



                    /*for (int i=0; i<alCatId.size(); i++){

                        Log.i("TT", "alCatId.get(i)-->" + alCatId.get(i));
                        nRate+=Integer.parseInt(alCatId.get(i));


                        Log.i("TT", "totlal add rate-->" + nRate);
                    }*/

                   /* for (int i=0; i<alCatId.size(); i++){
                        Log.i("ver", nRate+ "----" + alCatId.get(i));*/
           /* nRate = nRate + f_price;
            Log.i("ver", "Total-->" + nRate);*/
                   /* }*/
                /*}

            }*/
        }
        ChooseServiceDisplayAdapter mAdap = new ChooseServiceDisplayAdapter(context, lang_list_new, (ChooseServiceInterface) context);
        list_serviceDisplay.setAdapter(mAdap);
        mAdap.notifyDataSetChanged();

    }

    @Override
    public void delChoosenService(int str_id, String str_name, Float f_price) {
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



      /*  *//*for (int i = 0; i < alCatId.size(); i++) {*//*
        nRate = nRate - f_price;
           *//* nRate += Integer.parseInt(alCatId.get(i));*//*
        Log.i("ver", "Delete - Total-->" + nRate);
        *//*}*/
    }


}
