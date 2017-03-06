package com.aryvart.carservice.Edt_Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.aryvart.carservice.Adapters.DisplayServicesAdapter;
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.BookService;
import com.aryvart.carservice.GenericClasses.GeneralData;
import com.aryvart.carservice.Interfaces.ChooseServiceInterface;
import com.aryvart.carservice.R;
import com.aryvart.carservice.ServiceStation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by android2 on 2/2/17.
 */
public class ServiceConfirmation_Edit extends Activity implements ChooseServiceInterface {
    Context context;
    ImageView img_back;
    private DisplayServicesAdapter mAdapter;
    ListView listConfirm;
    ArrayList<CommonBean> arrayList;
    ArrayList<String> arrayList_id;
    ArrayList<String> arrayList_rate;
    TextView txt_total_amt, txt_pickUpCharge, txt_diagnoCharge, txt_overallAmount, txt_carRegNum;
    CheckBox cb_pickUp, cb_diagno;
    String str_category_id;
    GeneralData gD;
    Float f_overall_amount, f_pickupCharge, f_diagnoCharge;
    Button btn_confirm, btn_edit, btn_cancel;
    float nRate = 0;
    String strFrom = "insert", str_ServiceType;
    TextView txt_yourChoiceText, txt_amtText, txt_header, txt_totalText, txt_pickUpText, txt_diagnoText, txt_overAllText, txt_carNumText, txt_modularText, txt_modularAmt, txt_AddrText, txt_address;
    LinearLayout ll_cbPickLay, ll_cbDiagnoLay, ll_addressLay;
    RelativeLayout rl_modularLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_confirm);
        context = this;
        gD = new GeneralData(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        listConfirm = (ListView) findViewById(R.id.list_confirm);
        txt_total_amt = (TextView) findViewById(R.id.txt_total_amt);
        cb_pickUp = (CheckBox) findViewById(R.id.cb_pickup);
        cb_diagno = (CheckBox) findViewById(R.id.cb_diagno);
        txt_pickUpCharge = (TextView) findViewById(R.id.txt_pickup_amt);
        txt_diagnoCharge = (TextView) findViewById(R.id.txt_diagno_amt);
        txt_carRegNum = (TextView) findViewById(R.id.txt_car_reg_num);
        txt_overallAmount = (TextView) findViewById(R.id.txt_overall_amt);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        txt_yourChoiceText = (TextView) findViewById(R.id.txt_yourChoice);
        txt_amtText = (TextView) findViewById(R.id.txt_amtText);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_totalText = (TextView) findViewById(R.id.txt_totalText);
        txt_pickUpText = (TextView) findViewById(R.id.pickUpText);
        txt_diagnoText = (TextView) findViewById(R.id.diagnoText);
        txt_overAllText = (TextView) findViewById(R.id.txt_overAllText);
        txt_carNumText = (TextView) findViewById(R.id.txt_carNumText);


        ll_cbPickLay = (LinearLayout) findViewById(R.id.ll_cb_pick_lay);
        ll_cbDiagnoLay = (LinearLayout) findViewById(R.id.ll_cb_diagno_lay);

        rl_modularLay = (RelativeLayout) findViewById(R.id.rl_modularAmt);
        txt_modularText = (TextView) findViewById(R.id.txt_modularText);
        txt_modularAmt = (TextView) findViewById(R.id.txt_modularAmt);

        txt_AddrText = (TextView) findViewById(R.id.txt_addressText);
        txt_address = (TextView) findViewById(R.id.txt_address);
        ll_addressLay = (LinearLayout) findViewById(R.id.ll_address);


        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_header.setTypeface(typeFace1);
        txt_yourChoiceText.setTypeface(typeFace1);
        txt_amtText.setTypeface(typeFace1);
        btn_cancel.setTypeface(typeFace1);
        btn_edit.setTypeface(typeFace1);
        btn_confirm.setTypeface(typeFace1);

        Typeface typeFace2 = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        txt_carNumText.setTypeface(typeFace2);
        txt_address.setTypeface(typeFace2);

        Typeface typeFace3 = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        txt_totalText.setTypeface(typeFace3);
        txt_total_amt.setTypeface(typeFace3);
        cb_pickUp.setTypeface(typeFace3);
        txt_pickUpText.setTypeface(typeFace3);
        txt_pickUpCharge.setTypeface(typeFace3);
        txt_overAllText.setTypeface(typeFace3);
        txt_overallAmount.setTypeface(typeFace3);
        txt_carRegNum.setTypeface(typeFace3);
        txt_modularAmt.setTypeface(typeFace3);
        txt_modularText.setTypeface(typeFace3);
        txt_AddrText.setTypeface(typeFace3);
        txt_diagnoText.setTypeface(typeFace3);
        txt_diagnoCharge.setTypeface(typeFace3);
       /* f_pickupCharge = Float.parseFloat(gD.prefs.getString("ss_pickup_charge", null));
        f_diagnoCharge = Float.parseFloat(gD.prefs.getString("ss_diagno_charge", null));
        if(f_pickupCharge!=null){
            txt_pickUpCharge.setText("" + f_pickupCharge);
            txt_diagnoCharge.setText("" + f_diagnoCharge);
        }*/

        txt_carRegNum.setText(gD.prefs.getString("car_number", null));

        Log.e("JN_", "edit_ss_serviceType-->" + gD.prefs.getString("edit_ss_serviceType", null));
        Log.e("JN_", "edit_ss_pickUpAmt-->" + gD.prefs.getString("edit_ss_pickUpAmt", null));
        Log.e("JN_", "edit_ss_diagnoAmt-->" + gD.prefs.getString("edit_ss_diagnoAmt", null));
        Log.e("JN_", "edit_ss_modularAmt-->" + gD.prefs.getString("edit_ss_modularAmt", null));
        Log.e("JN_", "edit_ss_pickUpAddress-->" + gD.prefs.getString("edit_ss_pickUpAddress", null));


        f_pickupCharge = Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null));
        f_diagnoCharge = Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
        if (f_pickupCharge != null || f_diagnoCharge != null) {
            txt_pickUpCharge.setText("" + f_pickupCharge);
            txt_diagnoCharge.setText("" + f_diagnoCharge);
        }
        Log.i("BN_CS_EDIT", "f_pickupCharge->" + f_pickupCharge);
        Log.i("BN_CS_EDIT", "f_diagnoCharge->" + f_diagnoCharge);
        Log.i("BN_CS_EDIT", "service_choosen->" + getIntent().getStringExtra("ss_serviceChoice"));
        //  f_pickupCharge = Float.parseFloat(gD.prefs.getString("ss_pickup_charge", null));

        //  arrayList = new ArrayList<CommonBean>();

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
            Log.e("NNOP", String.valueOf(arrayList));

            Log.i("TT", "intial & al_bean1-->" + String.valueOf(arrayList_id.size()));
            Log.i("TT", "inital & al_bean_id1-->" + String.valueOf(arrayList.size()));

            Log.i("TT", "intial & al_bean-->" + String.valueOf(arrayList_id));
            Log.i("TT", "inital & al_bean_id-->" + String.valueOf(arrayList));


            mAdapter = new DisplayServicesAdapter(context, arrayList, (ChooseServiceInterface) context);
            listConfirm.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
             /* for (int i=0; i<arrayList_rate.size(); i++){

                  Log.i("TT", "totlal add rate-->" + arrayList_rate.get(i));
                        nRate+=Float.parseFloat(arrayList_rate.get(i));
                       Log.i("TT", "totlalRate-->" +nRate);
                       // txt_total_amt.setText("" + nRate);

                    }*/


            for (int i = 0; i < arrayList.size(); i++) {
                CommonBean cb = arrayList.get(i);


                // ** new changes float to string ** //

                Float x= Float.valueOf(cb.getStr_servicePrice());
                nRate += x;
                Log.i("TT", "totlalRate-->" + nRate);

            }
            txt_total_amt.setText("" + nRate);
            //  txt_overallAmount.setText("" + nRate);
            str_category_id = arrayList_id.toString().substring(1, arrayList_id.toString().length() - 1).trim();
            Log.i("TT", "str_category_id->" + str_category_id);
        }

        Log.e("JN_", gD.prefs.getString("edit_ss_pickUpAmt", null));
        Log.e("JN_", gD.prefs.getString("edit_ss_diagnoAmt", null));

        if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("pickup")) {

            txt_pickUpCharge.setVisibility(View.VISIBLE);
            txt_pickUpText.setVisibility(View.VISIBLE);

            txt_diagnoText.setVisibility(View.INVISIBLE);
            txt_diagnoCharge.setVisibility(View.INVISIBLE);

            str_ServiceType = "pickup";

            cb_pickUp.setChecked(true);
            rl_modularLay.setVisibility(View.GONE);
            listConfirm.setVisibility(View.VISIBLE);
            ll_cbDiagnoLay.setVisibility(View.GONE);

            ll_addressLay.setVisibility(View.VISIBLE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));

            f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null));
            Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
            txt_overallAmount.setText("" + f_overall_amount);
        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnostics")) {
            txt_diagnoText.setVisibility(View.VISIBLE);
            txt_diagnoCharge.setVisibility(View.VISIBLE);

            txt_pickUpCharge.setVisibility(View.INVISIBLE);
            txt_pickUpText.setVisibility(View.INVISIBLE);

            rl_modularLay.setVisibility(View.GONE);
            listConfirm.setVisibility(View.VISIBLE);
            ll_addressLay.setVisibility(View.GONE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));

            //added diagno
            cb_diagno.setChecked(true);
            str_ServiceType = "diagnostics";
            f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
            Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
            txt_overallAmount.setText("" + f_overall_amount);
          /*  f_overall_amount = nRate;
            Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
            txt_overallAmount.setText("" + f_overall_amount);*/

        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("modular")) {
            str_ServiceType = "modular";

            txt_pickUpCharge.setVisibility(View.INVISIBLE);
            txt_pickUpText.setVisibility(View.INVISIBLE);

            rl_modularLay.setVisibility(View.VISIBLE);
            listConfirm.setVisibility(View.GONE);
            ll_cbDiagnoLay.setVisibility(View.GONE);

            ll_addressLay.setVisibility(View.GONE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
           /* if (gD.prefs.getString("pickUp_address", null) != null) {
                ll_addressLay.setVisibility(View.VISIBLE);
                txt_address.setText(gD.prefs.getString("pickUp_address", null));
            }*/
            nRate = Float.parseFloat(gD.prefs.getString("edit_ss_modularAmt", null));
            Log.e("NNMod", "nRateModular->" + String.valueOf(nRate));
            txt_modularAmt.setText("" + nRate);
            f_overall_amount = nRate;
            Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
            txt_overallAmount.setText("" + f_overall_amount);
            txt_total_amt.setText(""+nRate);
        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("modularpickup")) {
            str_ServiceType = "modularpickup";

            txt_pickUpCharge.setVisibility(View.VISIBLE);
            txt_pickUpText.setVisibility(View.VISIBLE);

            rl_modularLay.setVisibility(View.VISIBLE);
            listConfirm.setVisibility(View.GONE);
            ll_cbDiagnoLay.setVisibility(View.GONE);
            cb_pickUp.setChecked(true);

            ll_addressLay.setVisibility(View.VISIBLE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));

           /* if (gD.prefs.getString("pickUp_address", null) != null) {
                ll_addressLay.setVisibility(View.VISIBLE);
                txt_address.setText(gD.prefs.getString("pickUp_address", null));
            }*/
            nRate = Float.parseFloat(gD.prefs.getString("edit_ss_modularAmt", null));
            Log.e("NNMod", "nRateModular->" + String.valueOf(nRate));
            txt_modularAmt.setText("" + nRate);
            f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null));
            Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
            txt_total_amt.setText(""+nRate);
            txt_overallAmount.setText("" + f_overall_amount);
        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("pickupNA")) {

            str_ServiceType = "pickupNA";
            txt_pickUpCharge.setVisibility(View.INVISIBLE);
            txt_pickUpText.setVisibility(View.INVISIBLE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
            rl_modularLay.setVisibility(View.GONE);
            listConfirm.setVisibility(View.VISIBLE);
            ll_cbDiagnoLay.setVisibility(View.GONE);
            ll_addressLay.setVisibility(View.GONE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
            f_overall_amount = nRate;
            Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
            txt_overallAmount.setText("" + f_overall_amount);
        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnoNA")) {

            str_ServiceType = "diagnoNA";

            txt_diagnoCharge.setVisibility(View.INVISIBLE);
            txt_diagnoText.setVisibility(View.INVISIBLE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
            rl_modularLay.setVisibility(View.GONE);
            listConfirm.setVisibility(View.VISIBLE);
            ll_addressLay.setVisibility(View.GONE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
            //added diagno

            f_overall_amount = nRate;
            Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
            txt_overallAmount.setText("" + f_overall_amount);
        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnosispickup")) {

            ll_addressLay.setVisibility(View.VISIBLE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
            str_ServiceType = "diagnosispickup";

            txt_pickUpCharge.setVisibility(View.VISIBLE);
            txt_pickUpText.setVisibility(View.VISIBLE);


            txt_diagnoCharge.setVisibility(View.VISIBLE);
            txt_diagnoText.setVisibility(View.VISIBLE);

            cb_diagno.setChecked(true);
            cb_pickUp.setChecked(true);

            f_overall_amount=nRate+Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null))+Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
            txt_overallAmount.setText(""+f_overall_amount);

        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnopickup")) {

            ll_addressLay.setVisibility(View.VISIBLE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
            str_ServiceType = "diagnopickup";

            txt_pickUpCharge.setVisibility(View.VISIBLE);
            txt_pickUpText.setVisibility(View.VISIBLE);


            txt_diagnoCharge.setVisibility(View.INVISIBLE);
            txt_diagnoText.setVisibility(View.INVISIBLE);

            cb_diagno.setChecked(false);
            cb_pickUp.setChecked(true);
            f_overall_amount=nRate+Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null));
            txt_overallAmount.setText(""+f_overall_amount);
        } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnosispickupNA")) {

            ll_addressLay.setVisibility(View.GONE);
            txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
            str_ServiceType = "diagnosispickupNA";

            txt_pickUpCharge.setVisibility(View.INVISIBLE);
            txt_pickUpText.setVisibility(View.INVISIBLE);


            txt_diagnoCharge.setVisibility(View.INVISIBLE);
            txt_diagnoText.setVisibility(View.INVISIBLE);

            cb_diagno.setChecked(false);
            cb_pickUp.setChecked(false);

            f_overall_amount=nRate;
            txt_overallAmount.setText(""+f_overall_amount);

        }


        //Checkbox
        cb_pickUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    //cb_diagno.setChecked(false);
                    txt_pickUpText.setVisibility(View.VISIBLE);
                    txt_pickUpCharge.setVisibility(View.VISIBLE);

                   /* SharedPreferences.Editor prefEdit = gD.prefs.edit();
                    prefEdit.putString("edit_ss_serviceType", "pickup");
                    prefEdit.commit();*/
                    f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null));
                    Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                    txt_overallAmount.setText("" + f_overall_amount);

                    ll_addressLay.setVisibility(View.VISIBLE);
                    txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));

                    //  cb_diagno.setChecked(false);

                    if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("modular")) {
                        str_ServiceType = "modularpickup";
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnostics")) {
                        str_ServiceType = "diagnopickup";
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("pickup")) {
                        str_ServiceType = "pickup";
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("modularpickup")) {
                        str_ServiceType = "modularpickup";
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("pickupNA")) {
                        if (cb_pickUp.isChecked()) {
                            str_ServiceType = "pickup";
                        }
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnosispickup")) {
                        if (cb_diagno.isChecked()) {
                            str_ServiceType = "diagnosispickup";
                        } else {
                            str_ServiceType = "diagnopickup";
                        }
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnopickup")) {
                        str_ServiceType = "diagnopickup";
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnosispickupNA")) {
                        if (cb_diagno.isChecked()) {
                            str_ServiceType = "diagnosispickup";
                        } else {
                            str_ServiceType = "diagnopickup";
                        }
                    }


                    if (cb_diagno.isChecked()) {
                        str_ServiceType = "diagnosispickup";
                        f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null)) + Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
                        Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                        txt_overallAmount.setText("" + f_overall_amount);
                    }

                } else {
                    txt_pickUpText.setVisibility(View.INVISIBLE);
                    txt_pickUpCharge.setVisibility(View.INVISIBLE);

                  /*  SharedPreferences.Editor prefEdit = gD.prefs.edit();
                    prefEdit.putString("edit_ss_serviceType", "diagnostics");
                    prefEdit.commit();
*/
                    ll_addressLay.setVisibility(View.GONE);
                    txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));


                    if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("modular")) {
                        str_ServiceType = "modular";
                        //txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));

                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnostics")) {
                        if (!cb_diagno.isChecked()) {
                            str_ServiceType = "diagnosispickupNA";
                        }
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("pickup")) {

                        str_ServiceType = "pickupNA";
                        // txt_address.setText(gD.prefs.getString("edit_ss_pickUpAddress", null));
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("modularpickup")) {
                        str_ServiceType = "modular";
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("pickupNA")) {
                        str_ServiceType = "pickupNA";
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnosispickup")) {
                        if (!cb_diagno.isChecked()) {
                            str_ServiceType = "diagnosispickupNA";
                        }

                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnopickup")) {
                        if (!cb_diagno.isChecked()) {
                            str_ServiceType = "diagnosispickupNA";
                        }
                    } else if (gD.prefs.getString("edit_ss_serviceType", null).equalsIgnoreCase("diagnosispickupNA")) {
                        if (!cb_diagno.isChecked()) {
                            str_ServiceType = "diagnosispickupNA";
                        }
                    }


                    f_overall_amount = nRate;
                    Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                    txt_overallAmount.setText("" + f_overall_amount);


                    if (cb_diagno.isChecked()) {
                        str_ServiceType = "diagnostics";
                        f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
                        Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                        txt_overallAmount.setText("" + f_overall_amount);
                    }
                      /*  SharedPreferences.Editor prefEdit = gD.prefs.edit();
                        prefEdit.putString("edit_ss_serviceType", "diagnostics");
                        prefEdit.commit();

                        f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
                        Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                        txt_overallAmount.setText("" + f_overall_amount);*/
                }
            }
        });
        cb_diagno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    txt_diagnoText.setVisibility(View.VISIBLE);
                    txt_diagnoCharge.setVisibility(View.VISIBLE);
                    // cb_pickUp.setChecked(false);
                    str_ServiceType = "diagnostics";
                    f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
                    Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                    txt_overallAmount.setText("" + f_overall_amount);

                    if (cb_pickUp.isChecked()) {
                        str_ServiceType = "diagnosispickup";
                        f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null)) + Float.parseFloat(gD.prefs.getString("edit_ss_diagnoAmt", null));
                        Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                        txt_overallAmount.setText("" + f_overall_amount);
                    }
                } else {
                    txt_diagnoText.setVisibility(View.INVISIBLE);
                    txt_diagnoCharge.setVisibility(View.INVISIBLE);

                    f_overall_amount = nRate;
                    Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                    txt_overallAmount.setText("" + f_overall_amount);

                    str_ServiceType = "diagnosispickupNA";

                    if (cb_pickUp.isChecked()) {
                        str_ServiceType = "diagnopickup";
                        f_overall_amount = nRate + Float.parseFloat(gD.prefs.getString("edit_ss_pickUpAmt", null));
                        Log.e("NN", "overall amount->" + String.valueOf(f_overall_amount));
                        txt_overallAmount.setText("" + f_overall_amount);
                    }
                }
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceConfirmation_Edit.this, ServiceStation_Edit.class));
                finish();

            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Log.e("NN", "reg_id-->" + gD.prefs.getString("reg_id", null) + "\t" + "services->" + str_category_id + "\t" + "overall amount-->" + nRate + "\t" + "bookingid-->" +
                        "charge-->" + gD.prefs.getString("str_serviceType", null) + "rate-->" + String.valueOf(f_overall_amount) + "date-->" + gD.prefs.getString("ss_date", null) + "stationid-->" + gD.prefs.getString("ss_id", null));

               */

                Log.e("NNE", "reg_id-->" + gD.prefs.getString("reg_id", null) + "\t" + "services->" + str_category_id + "\t" + "overall amount-->" + nRate + "\t" + "bookingid-->" + gD.prefs.getString("edit_ss_book_id", null) +
                        "  charge-->" + gD.prefs.getString("str_serviceType", null) + "  rateOverallFinal-->" + String.valueOf(f_overall_amount) + "   date-->" + gD.prefs.getString("ss_date", null) +
                        "  stationid-->" + gD.prefs.getString("edit_ss_id", null) + "   pickup address-->" + txt_address.getText().toString().trim() + "   pickupSharedPref-->" + gD.prefs.getString("edit_ss_pickUpAddress", null) + "  serviceTypeConfi-->" + str_ServiceType.toString().trim());

                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                if (isConnected) {
                    serviceConfirmCall();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ServiceConfirmation_Edit.this, ServiceStation_Edit.class));
                finish();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the values
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(null);
                String json_id = gson.toJson(null);
                editor.putString("key", json);
                editor.putString("key_id", json_id);
                editor.commit();
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
                prefEdit.commit();
                startActivity(new Intent(ServiceConfirmation_Edit.this, BookService.class));
                finish();
            }
        });
    }

    @Override
    public void getServiceStationAddress(String str_id,String str_serviceType, String str_name, String price) {

    }

    @Override
    public void delChoosenService(int str_id, String str_name, String str_price) {

    }

    @Override
    public void getServiceStationMainAddress(String str_id, String str_service_type, String str_name) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ServiceConfirmation_Edit.this, ServiceStation_Edit.class));
        finish();
    }

    public void serviceConfirmCall() {
        Log.i("HH", "strFrom : " + strFrom);
        gD.showAlertDialog(context, "Loading", "Please wait..");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "book_service.php",
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
                                txt_content.setText("Your booking has been placed successfully");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ServiceConfirmation_Edit.this, BookService.class));
                                        finish();
                                        altDialog.dismiss();
                                    }
                                });
                                altDialog.show();

                                //Set the values
                                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(null);
                                String json_id = gson.toJson(null);
                                editor.putString("key", json);
                                editor.putString("key_id", json_id);
                                editor.commit();

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
                                prefEdit.commit();
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

              /*  Log.e("NN", "reg_id-->" + gD.prefs.getString("reg_id", null) + "\t" + "services->" + str_category_id + "\t" + "overall amount-->" + nRate + "\t" + "bookingid-->" + strBookinIdEdit +
                        "charge-->" + gD.prefs.getString("str_serviceType", null) + "rate-->" + String.valueOf(f_overall_amount) + "date-->" + gD.prefs.getString("ss_date", null) + "stationid-->" + gD.prefs.getString("ss_id", null));
               */


                params.put("bookingid", gD.prefs.getString("edit_ss_book_id", null));
              /*  params.put("services", str_category_id);*/
                if (str_category_id != null) {
                    params.put("services", str_category_id);
                }
                params.put("registerid", gD.prefs.getString("reg_id", null));
                params.put("charge", str_ServiceType);
                params.put("rate", String.valueOf(f_overall_amount));
                params.put("date", gD.prefs.getString("edit_ss_date", null));
                params.put("stationid", gD.prefs.getString("edit_ss_id", null));
                params.put("address", txt_address.getText().toString().trim());

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
        RequestQueue requestQueue = Volley.newRequestQueue(ServiceConfirmation_Edit.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }
}
