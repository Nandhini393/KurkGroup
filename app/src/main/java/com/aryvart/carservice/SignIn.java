package com.aryvart.carservice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.GenericClasses.GeneralData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by android2 on 31/1/17.
 */
public class SignIn extends Activity {
    Context context;
    //form fields
    EditText ed_password, ed_carNum;
    TextView txt_login, txt_reg, txt_fogetPass;
    GeneralData gD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        context = this;
        gD = new GeneralData(context);
        //form fields
        ed_password = (EditText) findViewById(R.id.ed_pwd);
        ed_carNum = (EditText) findViewById(R.id.ed_car_num);
        txt_reg = (TextView) findViewById(R.id.txt_reg);
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_fogetPass = (TextView) findViewById(R.id.txt_forget_pass);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
        ed_carNum.setTypeface(typeFace);
        ed_password.setTypeface(typeFace);
        txt_fogetPass.setTypeface(typeFace);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_login.setTypeface(typeFace1);
        txt_reg.setTypeface(typeFace1);
        ed_carNum.setHint(getResources().getString(R.string.login_car_reg_num).toUpperCase());
        ed_password.setHint(getResources().getString(R.string.login_pass).toUpperCase());

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                if (isConnected) {
                    if (ed_carNum.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignIn.this, "Enter car number", Toast.LENGTH_SHORT).show();
                    } else if (ed_password.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                    } else {
                        signInCall();

                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });
        txt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));

            }
        });
        txt_fogetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, ForgetPassword.class));

            }
        });

        ed_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // TODO do something
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                    boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                    if (isConnected) {
                        if (ed_carNum.getText().toString().trim().length() == 0) {
                            Toast.makeText(SignIn.this, "Enter car number", Toast.LENGTH_SHORT).show();
                        } else if (ed_password.getText().toString().trim().length() == 0) {
                            Toast.makeText(SignIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                        } else {
                            signInCall();

                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
                return handled;

            }
        });
    }

    public void signInCall() {
        gD.showAlertDialog(context, "LoginIn", "Please wait");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "login.php",
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
                                JSONObject jsonData = jsobj.getJSONObject("data");
                                SharedPreferences.Editor prefEdit = gD.prefs.edit();
                                prefEdit.putString("reg_id", jsonData.getString("register_id"));
                                prefEdit.putString("name", jsonData.getString("name"));
                                prefEdit.putString("email", jsonData.getString("email"));
                                prefEdit.putString("password", jsonData.getString("password"));
                                prefEdit.putString("car_number", jsonData.getString("car_number"));
                                prefEdit.putString("car_model", jsonData.getString("car_model"));
                                prefEdit.putString("contact_number", jsonData.getString("contact_number"));
                                prefEdit.putString("manufacture_date", jsonData.getString("manufacture_date"));
                                prefEdit.putString("car_type", jsonData.getString("car_type"));
                                prefEdit.commit();
                                Toast.makeText(SignIn.this, "Login Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignIn.this, BookService.class));
                                finish();
                            } else if (jsobj.getString("code").equalsIgnoreCase("0")) {
                                gD.altDialog.dismiss();
                                Snackbar.make(findViewById(android.R.id.content), jsobj.getString("status"), Snackbar.LENGTH_LONG)
                                        .show();
                                ed_password.setText("");
                            } else if (jsobj.getString("code").equalsIgnoreCase("1")) {
                                gD.altDialog.dismiss();
                                Snackbar.make(findViewById(android.R.id.content), jsobj.getString("status"), Snackbar.LENGTH_LONG)
                                        .show();
                                ed_carNum.setText("");
                            } else if (jsobj.getString("code").equalsIgnoreCase("3")) {
                                gD.altDialog.dismiss();
                                Snackbar.make(findViewById(android.R.id.content), jsobj.getString("status"), Snackbar.LENGTH_LONG)
                                        .show();

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

                params.put("carnumber", ed_carNum.getText().toString().trim());
                params.put("password", ed_password.getText().toString().trim());

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
        RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }
}

