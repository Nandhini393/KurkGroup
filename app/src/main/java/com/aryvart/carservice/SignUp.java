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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.GenericClasses.GeneralData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by android2 on 31/1/17.
 */
public class SignUp extends Activity {
    Context context;
    //form fields
    EditText ed_name, ed_email, ed_pass, ed_carNum, ed_phone, ed_carModel, ed_manfDate, ed_carType;
    Button btn_signUp;
    //internet con
    IntentFilter filter1;
    Snackbar snackBar;
    GeneralData gD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        context = this;
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_pass = (EditText) findViewById(R.id.ed_password);
        ed_carNum = (EditText) findViewById(R.id.ed_car_num);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_carModel = (EditText) findViewById(R.id.ed_car_model);
        ed_manfDate = (EditText) findViewById(R.id.ed_manf_date);
        ed_carType = (EditText) findViewById(R.id.ed_car_type);
        btn_signUp = (Button) findViewById(R.id.btn_signup);
        ed_manfDate.setFocusable(false);
        gD = new GeneralData(context);
        snackBar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        filter1 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, filter1);

        //typeface
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");

        ed_name.setTypeface(typeFace);
        ed_email.setTypeface(typeFace);
        ed_pass.setTypeface(typeFace);
        ed_carNum.setTypeface(typeFace);
        ed_phone.setTypeface(typeFace);
        ed_carModel.setTypeface(typeFace);
        ed_manfDate.setTypeface(typeFace);
        ed_carType.setTypeface(typeFace);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        btn_signUp.setTypeface(typeFace1);

        ed_name.setHint(getResources().getString(R.string.reg_name).toUpperCase());
        ed_email.setHint(getResources().getString(R.string.reg_email).toUpperCase());
        ed_pass.setHint(getResources().getString(R.string.reg_pass).toUpperCase());
        ed_carNum.setHint(getResources().getString(R.string.reg_car_num).toUpperCase());
        ed_phone.setHint(getResources().getString(R.string.reg_contact_num).toUpperCase());
        ed_carModel.setHint(getResources().getString(R.string.reg_car_model).toUpperCase());
        ed_manfDate.setHint(getResources().getString(R.string.reg_manf_date).toUpperCase());
        ed_carType.setHint(getResources().getString(R.string.reg_car_type).toUpperCase());
        ed_manfDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

                if (isConnected) {
                    snackBar.dismiss();
                    if (ed_name.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Enter name", Toast.LENGTH_SHORT).show();
                    } else if (ed_email.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Enter email", Toast.LENGTH_SHORT).show();
                    } else if (!isValidEmail(ed_email.getText().toString().trim())) {
                        ed_email.setError("Invalid email");
                    } else if (ed_pass.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Enter password", Toast.LENGTH_SHORT).show();
                    } else if (ed_pass.getText().toString().trim().length() <= 6) {
                        ed_pass.setError("Password must be greater than 6");
                    } else if (ed_carNum.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Enter car number", Toast.LENGTH_SHORT).show();
                    } else if (ed_carModel.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Enter car model", Toast.LENGTH_SHORT).show();
                    } else if (ed_phone.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Enter phone", Toast.LENGTH_SHORT).show();
                    } else if (!isValidMobile(ed_phone.getText().toString().trim())) {
                        ed_phone.setError("Invalid phone");
                    } else if (ed_manfDate.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Select manufacture date", Toast.LENGTH_SHORT).show();
                    } else if (ed_carType.getText().toString().trim().length() == 0) {
                        Toast.makeText(SignUp.this, "Enter car type", Toast.LENGTH_SHORT).show();
                    } else {
                        signUpCall();

                    }
                } else {

                    snackBar.show();
                }
            }
        });
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
                snackBar.dismiss();
            } else {

                snackBar.show();

            }
        }
    };

    //email validation
    public boolean isValidEmail(CharSequence strEmail) {
        boolean isvalid = false;
        try {
            isvalid = false;
            try {
                if (strEmail == null) {
                    return isvalid;
                } else {
                    isvalid = android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isvalid;
    }

    //phone validation
    private boolean isValidMobile(String phone) {
        boolean check = false;
        try {
            check = false;
            try {
                if (phone == null) {
                    return check;
                } else {
                    if (phone.length() < 10 || phone.length() > 13) {
                        check = false;
                    } else {
                        check = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;
    }

    //calendar
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
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
        ed_manfDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    public void signUpCall() {
        gD.showAlertDialog(context, "Registering", "Please wait");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "register_new.php",
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
                                builder.setCancelable(true);
                                itemView1 = LayoutInflater.from(context)
                                        .inflate(R.layout.forget_pass_popup, null);
                                final AlertDialog altDialog = builder.create();
                                altDialog.setView(itemView1);
                                TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
                                Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
                                txt_content.setText("Your have been registered successfully...Login to activate your account");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(SignUp.this, SignIn.class));
                                        finish();
                                        altDialog.dismiss();
                                    }
                                });
                                altDialog.show();
                            }
                            else if (jsobj.getString("code").equalsIgnoreCase("0")){
                                gD.altDialog.dismiss();
                                View itemView1;
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setCancelable(true);
                                itemView1 = LayoutInflater.from(context)
                                        .inflate(R.layout.error_popup, null);
                                final AlertDialog altDialog = builder.create();
                                altDialog.setView(itemView1);
                                TextView txt_title = (TextView) itemView1.findViewById(R.id.txt_title);
                                TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
                                Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
                                txt_title.setText("Email Exists");
                                txt_content.setText("Enterted email-id already exists.Please register with other id");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       ed_email.setText("");
                                        altDialog.dismiss();
                                    }
                                });
                                altDialog.show();
                            }
                            else if (jsobj.getString("code").equalsIgnoreCase("1")){
                                gD.altDialog.dismiss();
                                View itemView1;
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setCancelable(true);
                                itemView1 = LayoutInflater.from(context)
                                        .inflate(R.layout.error_popup, null);
                                final AlertDialog altDialog = builder.create();
                                altDialog.setView(itemView1);
                                TextView txt_title = (TextView) itemView1.findViewById(R.id.txt_title);
                                TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
                                Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
                                txt_title.setText("Car Number Exists");
                                txt_content.setText("Enterted car number already exists.");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ed_carNum.setText("");
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
                params.put("username", ed_name.getText().toString().trim());
                params.put("carnumber", ed_carNum.getText().toString().trim());
                params.put("email",  ed_email.getText().toString().trim());
                params.put("carmodel", ed_carModel.getText().toString().trim());
                params.put("phone", ed_phone.getText().toString().trim());
                params.put("manufacturedate",  ed_manfDate.getText().toString().trim());
                params.put("cartype", ed_carType.getText().toString().trim());
                params.put("password",  ed_pass.getText().toString().trim());

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
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }
}
