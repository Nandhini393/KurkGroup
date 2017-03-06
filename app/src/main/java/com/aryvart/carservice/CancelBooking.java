package com.aryvart.carservice;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.aryvart.carservice.Adapters.ChooseServiceAdapter;
import com.aryvart.carservice.Adapters.ChooseServiceDisplayAdapter;
import com.aryvart.carservice.Adapters.DisplayServicesAdapter;
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.aryvart.carservice.Interfaces.ChooseServiceInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by android2 on 2/2/17.
 */
public class CancelBooking extends Activity implements ChooseServiceInterface {
    Context context;
    GeneralData gD;
    ImageView img_back;
    TextView txt_carRegNum, txt_overAllAmt;
    String str_EditBookingId, str_EditServiceArrayResp;
    ListView list_serviceDisplay;
    Button btn_cancelBooking;
    TextView txt_AdditionAmtCharge, txt_totalText, txt_total_amt, txt_AddAmtText, txt_header, txt_carNumText, txt_serviceListText, txt_amtText, txt_pickUpText, txt_overAllAmtText, txt_modularText, txt_modularAmt;
    String f_overallAmount;
    RelativeLayout rl_modularLay;

    ArrayList<String> alCatId = new ArrayList<>();
    ArrayList<CommonBean> lang_list_new;
    float nRate;
    ArrayList<CommonBean> arrayList;
    ArrayList<String> arrayList_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_service);
        context = this;
        gD = new GeneralData(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_carRegNum = (TextView) findViewById(R.id.txt_car_reg_num);
        txt_overAllAmt = (TextView) findViewById(R.id.txt_overall_amt);

        lang_list_new = new ArrayList<>();



        list_serviceDisplay = (ListView) findViewById(R.id.list_aadService);


        if (!getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnostics_D")&&!getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnosispickup_D")){
            // listview scroll (lolipop)
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Runnable fitsOnScreen = new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        int last = list_serviceDisplay.getLastVisiblePosition();
                        if(last == list_serviceDisplay.getCount() - 1 && list_serviceDisplay.getChildAt(last).getBottom() <= list_serviceDisplay.getHeight()) {
                            // It fits!
                            list_serviceDisplay.setNestedScrollingEnabled(false);

                            //android:nestedScrollingEnabled="true"
                        }
                        else {
                            // It doesn't fit...
                            list_serviceDisplay.setNestedScrollingEnabled(true);
                        }
                    }
                };

                list_serviceDisplay.post(fitsOnScreen);
            }
            else {

                Runnable fitsOnScreen = new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        int last = list_serviceDisplay.getLastVisiblePosition();
                        if(last == list_serviceDisplay.getCount() - 1 && list_serviceDisplay.getChildAt(last).getBottom() <= list_serviceDisplay.getHeight()) {
                            // It fits!


                            //android:nestedScrollingEnabled="true"
                        }
                        else {
                            // It doesn't fit...
                            list_serviceDisplay.setOnTouchListener(new ListView.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    int action = event.getAction();
                                    switch (action) {

                                        case MotionEvent.ACTION_DOWN:
                                            // Disallow ScrollView to intercept touch events.
                                            v.getParent().requestDisallowInterceptTouchEvent(true);
                                            break;

                                        case MotionEvent.ACTION_UP:
                                            // Allow ScrollView to intercept touch events.
                                            v.getParent().requestDisallowInterceptTouchEvent(false);
                                            break;
                                    }

                                    // Handle ListView touch events.
                                    v.onTouchEvent(event);
                                    return true;
                                }
                            });
                        }
                    }
                };
                list_serviceDisplay.post(fitsOnScreen);

            }
        }




        btn_cancelBooking = (Button) findViewById(R.id.btn_cancel);
        txt_AdditionAmtCharge = (TextView) findViewById(R.id.txt_pickup_amt);
        txt_AddAmtText = (TextView) findViewById(R.id.pickUpText);
        txt_carRegNum.setText(gD.prefs.getString("car_number", null));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_carNumText = (TextView) findViewById(R.id.txt_carNumText);
        txt_serviceListText = (TextView) findViewById(R.id.txt_serviceListText);
        txt_amtText = (TextView) findViewById(R.id.txt_amtText);
        txt_pickUpText = (TextView) findViewById(R.id.pickUpText);
        txt_overAllAmtText = (TextView) findViewById(R.id.txt_overAllText);

        rl_modularLay = (RelativeLayout) findViewById(R.id.rl_modularAmt);
        txt_modularText = (TextView) findViewById(R.id.txt_modularText);
        txt_modularAmt = (TextView) findViewById(R.id.txt_modularAmt);

        txt_total_amt = (TextView) findViewById(R.id.txt_total_amt);
        txt_totalText = (TextView) findViewById(R.id.txt_totalText);


        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_header.setTypeface(typeFace1);
        btn_cancelBooking.setTypeface(typeFace1);
        txt_serviceListText.setTypeface(typeFace1);
        txt_amtText.setTypeface(typeFace1);

        Typeface typeFace2 = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        txt_carNumText.setTypeface(typeFace2);

        Typeface typeFace3 = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        txt_carRegNum.setTypeface(typeFace3);
        txt_overAllAmtText.setTypeface(typeFace3);
        txt_AddAmtText.setTypeface(typeFace3);
        txt_AdditionAmtCharge.setTypeface(typeFace3);
        txt_overAllAmt.setTypeface(typeFace3);
        txt_modularAmt.setTypeface(typeFace3);
        txt_modularText.setTypeface(typeFace3);
        txt_total_amt.setTypeface(typeFace3);
        txt_totalText.setTypeface(typeFace3);

        // str_EditServiceId = getIntent().getStringExtra("edit_ss_id");
        str_EditServiceArrayResp = getIntent().getStringExtra("edit_ss_serviceArray");

        Log.e("NN_edit", "edit_ss_serviceType->" + getIntent().getStringExtra("edit_ss_serviceType"));
        Log.e("NN_edit", "edit_ss_diagnoAmt->" + getIntent().getStringExtra("edit_ss_diagnoAmt"));
        Log.e("NN_edit", "edit_ss_pickUpAmt->" + getIntent().getStringExtra("edit_ss_pickUpAmt"));
        Log.e("NN_edit", "edit_ss_pickUpAddress->" + getIntent().getStringExtra("edit_ss_pickUpAddress"));
        Log.e("NN_edit", "edit_ss_modularAmt->" + getIntent().getStringExtra("edit_ss_modularAmt"));

        if (str_EditServiceArrayResp != null) {


            Log.e("NN_edit", "str_serviceArray->" + str_EditServiceArrayResp);
            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();
            ArrayList<String> beanIdList = new ArrayList<String>();

            try {
                JSONObject jsobj = new JSONObject(str_EditServiceArrayResp);
                f_overallAmount = String.valueOf(jsobj.getString("rate"));
                str_EditBookingId = jsobj.getString("booking_id");
                Log.i("HH", "f_overallAmount : " + f_overallAmount);
                Log.i("HH", "str_EditBookingId : " + str_EditBookingId);
                txt_overAllAmt.setText("" + f_overallAmount);
                Log.i("HH", "strResp : " + str_EditServiceArrayResp);
                if (!getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnostics_D") && !getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnosispickup_D")) {

                    // if (jsobj.getString("code").equalsIgnoreCase("2")) {
                    JSONArray services_stations = jsobj.getJSONArray("services");

                    if (str_EditServiceArrayResp.length() > 0) {

                        for (int i = 0; i < services_stations.length(); i++) {
                            CommonBean drawerBean = new CommonBean();
                            drawerBean.setStr_serviceName(services_stations.getJSONObject(i).getString("service_name"));
                            drawerBean.setN_serviceId(Integer.parseInt(services_stations.getJSONObject(i).getString("service_id")));
                            drawerBean.setStr_servicePrice(services_stations.getJSONObject(i).getString("service_rate"));

                            beanArrayList.add(drawerBean);

                            beanIdList.add(services_stations.getJSONObject(i).getString("service_id"));
                            nRate += Float.parseFloat(services_stations.getJSONObject(i).getString("service_rate").replaceAll(",",""));
                            Log.e("SUR",""+nRate);

                            txt_total_amt.setText("" + NumberFormat.getNumberInstance(new Locale("en", "in")).format(nRate));

                        }
                    }
                    DisplayServicesAdapter mAdap = new DisplayServicesAdapter(context, beanArrayList, (ChooseServiceInterface) context);
                    list_serviceDisplay.setAdapter(mAdap);
                    mAdap.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }



   /*  SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
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



            Log.e("NNOP", String.valueOf(arrayList));

            Log.i("TT", "intial & al_bean1-->" + String.valueOf(arrayList_id.size()));
            Log.i("TT", "inital & al_bean_id1-->" + String.valueOf(arrayList.size()));

            Log.i("TT", "intial & al_bean-->" + String.valueOf(arrayList_id));
            Log.i("TT", "inital & al_bean_id-->" + String.valueOf(arrayList));




            for (int i = 0; i < arrayList.size(); i++) {

                CommonBean cb = arrayList.get(i);

                // ** new changes float to string ** //
                try {

                    Float x=Float.valueOf(cb.getStr_servicePrice().replaceAll(",", ""));
                    nRate += x;
                    Log.i("TT", "totlalRate-->" + nRate);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            NumberFormat.getNumberInstance(new Locale("en", "in")).format(nRate).toString();
            Log.i("TTO", "currency2-->->" +  NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(nRate).toString());
            Log.i("TTO", "currency3-->->" +   NumberFormat.getNumberInstance(new Locale("en", "in")).format(nRate).toString());
            // Log.i("TTO", "currency3-->->" +  NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(nRate).replace("Rs.",""));

            // ** ADD COMMA TO NUMBER BASED ON ITS HUNDREDS THOUSANDS VALUE ** //
            txt_total_amt.setText("" + NumberFormat.getNumberInstance(new Locale("en", "in")).format(nRate));



        }
        else{
            Log.e("NAND","arraylist null");
        }*/






        // ** diagnostics ** //

        if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnostics_D")) {
            rl_modularLay.setVisibility(View.VISIBLE);
            list_serviceDisplay.setVisibility(View.GONE);
            txt_modularAmt.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")));

            txt_AddAmtText.setText("");
            // txt_AdditionAmtCharge.setText("" + Float.parseFloat(getIntent().getStringExtra("edit_ss_diagnoAmt")));


            txt_total_amt.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")));
            txt_overAllAmt.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")));

        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnosispickup_D")) {
            rl_modularLay.setVisibility(View.VISIBLE);
            list_serviceDisplay.setVisibility(View.GONE);

            txt_AddAmtText.setText("Pickup service charge");

            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt")) + "\n");

            txt_modularAmt.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")));

            txt_total_amt.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")));


            Float val1 = Float.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt").replaceAll(",",""));
            Float val2 = Float.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt").replaceAll(",",""));
            Float total = val1 + val2;

            txt_overAllAmt.setText("" + NumberFormat.getNumberInstance(new Locale("en", "in")).format(total));

        }


        // ** pickup ** //

        else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("pickup_P")) {
            rl_modularLay.setVisibility(View.GONE);
            list_serviceDisplay.setVisibility(View.VISIBLE);
            txt_AddAmtText.setText("Pickup service charge");

            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt")));

        }
        else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnosispickup_P")) {

            txt_AddAmtText.setText("Pickup service charge\n\nDiagnostics service charge\n");



            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt")) + "\n\n" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")) + "\n");

        }


//** modular ** //

        else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("modular")) {
            txt_AddAmtText.setText("");
            txt_AdditionAmtCharge.setText("");

            rl_modularLay.setVisibility(View.GONE);
            list_serviceDisplay.setVisibility(View.VISIBLE);

            //txt_total_amt.setText("" + Float.valueOf(getIntent().getStringExtra("edit_ss_modularAmt")));
            //txt_modularAmt.setText("" + Float.valueOf(getIntent().getStringExtra("edit_ss_modularAmt")));

        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("modularpickup")) {

            rl_modularLay.setVisibility(View.GONE);
            list_serviceDisplay.setVisibility(View.VISIBLE);

            //txt_total_amt.setText("" + Float.valueOf(getIntent().getStringExtra("edit_ss_modularAmt")));
            //txt_modularAmt.setText("" + Float.valueOf(getIntent().getStringExtra("edit_ss_modularAmt")));
            txt_AddAmtText.setText("Pickup service charge");
            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt")));

            Log.e("MK", "total->" + txt_total_amt.getText().toString());

           /* Float total=Float.valueOf(txt_total_amt.getText().toString())+Float.parseFloat(getIntent().getStringExtra("edit_ss_pickUpAmt"));
            txt_overAllAmt.setText(""+total);*/

        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnostics")) {
            rl_modularLay.setVisibility(View.GONE);
            list_serviceDisplay.setVisibility(View.VISIBLE);
            txt_AddAmtText.setText("Diagnostics service charge");

            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")));

        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnosispickup")) {
            txt_AddAmtText.setText("Pickup service charge\n\nDiagnostics service charge\n");
            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt")) + "\n\n" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")) + "\n");

        }


        // ** book a service ** //


        else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("pickup_B")) {
            rl_modularLay.setVisibility(View.GONE);
            list_serviceDisplay.setVisibility(View.VISIBLE);
            txt_AddAmtText.setText("Pickup service charge");

            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt")));

        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnostics_B")) {
            rl_modularLay.setVisibility(View.GONE);
            list_serviceDisplay.setVisibility(View.VISIBLE);
            txt_AddAmtText.setText("Diagnostics service charge");

            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")));

        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnosispickup_B")) {
            txt_AddAmtText.setText("Pickup service charge\n\nDiagnostics service charge\n");
            txt_AdditionAmtCharge.setText("" + String.valueOf(getIntent().getStringExtra("edit_ss_pickUpAmt")) + "\n\n" + String.valueOf(getIntent().getStringExtra("edit_ss_diagnoAmt")) + "\n");

        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnosispickupNA")) {
            txt_AddAmtText.setText("");
            txt_AdditionAmtCharge.setText("");

        }




        /*else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("pickupNA")) {
            txt_AddAmtText.setText("");
            txt_AdditionAmtCharge.setText("");
        } else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnoNA")) {
            txt_AddAmtText.setText("");
            txt_AdditionAmtCharge.setText("");
        }




        else if (getIntent().getStringExtra("edit_ss_serviceType").equalsIgnoreCase("diagnopickup")) {
            txt_AddAmtText.setText("Pickup service Charge");
            txt_AdditionAmtCharge.setText("" + Float.parseFloat(getIntent().getStringExtra("edit_ss_pickUpAmt")));

        }
*/
        btn_cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (isConnected) {
                    cancelBookingCall();
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

        finish();
    }

    @Override
    public void getServiceStationAddress(String str_id, String str_serviceType, String str_name, String price) {

    }

    @Override
    public void delChoosenService(int str_id, String str_name, String str_price) {


    /*    CommonBean serviceBean = new CommonBean();
        serviceBean.setStr_serviceName(str_name);
        serviceBean.setN_serviceId(str_id);
        serviceBean.setStr_servicePrice(str_price);



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
        Log.i("ver", "Adding--->" + str_id);
        Log.i("ver", "Array Size--->" + alCatId.size());


       //Set the values
        SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor1 = sharedPrefs1.edit();
        Gson gson1 = new Gson();

        String json1 = gson1.toJson(lang_list_new);
        String json_id1 = gson1.toJson(alCatId);
        editor1.putString("key", json1);
        editor1.putString("key_id", json_id1);
        editor1.commit();*/

    }

    @Override
    public void getServiceStationMainAddress(String str_id, String str_service_type, String str_name) {

    }

    public void cancelBookingCall() {
        gD.showAlertDialog(context, "Loading", "Please wait..");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "booking_cancel.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(UserRegOne.this, response, Toast.LENGTH_LONG).show();
                        try {
                            Log.i("HH", "strResp : " + response);
                            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

                            JSONObject jsobj = new JSONObject(response);

                            Log.i("HH", "strResp : " + response);
                            if (jsobj.getString("status").equalsIgnoreCase("success")) {


                                gD.altDialog.dismiss();
                                View itemView1;
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setCancelable(false);
                                itemView1 = LayoutInflater.from(context)
                                        .inflate(R.layout.forget_pass_popup, null);
                                final AlertDialog altDialog = builder.create();
                                altDialog.setView(itemView1);
                                TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
                                Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
                                txt_content.setText("Your booking has been cancelled ");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(CancelBooking.this, BookService.class));
                                        finish();
                                        altDialog.dismiss();
                                    }
                                });
                                altDialog.show();

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


                params.put("bookingid", str_EditBookingId);

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
        RequestQueue requestQueue = Volley.newRequestQueue(CancelBooking.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }
}
