package com.aryvart.carservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by android2 on 6/6/16.
 */
public class ForgetPassword extends Activity {
    Button btn_forget_pwd;
    EditText ed_get_email;
    ImageView img_back;
    TextView txt_error_msg1;
    TextView txt_headerName;
    Context context;
GeneralData gD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        context = this;
        gD= new GeneralData(context);
        btn_forget_pwd = (Button) findViewById(R.id.btn_reset_pass);
        ed_get_email = (EditText) findViewById(R.id.ed_email);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_headerName=(TextView)findViewById(R.id.txt_header);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.otf");
        txt_headerName.setTypeface(typeFace1);
        btn_forget_pwd.setTypeface(typeFace1);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
        ed_get_email.setTypeface(typeFace);

        btn_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if((ed_get_email.getText().toString().trim().equals("hi"))||(ed_get_email.getText().toString().trim().equals("123456"))){
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
                    if (ed_get_email.getText().toString().trim().length() == 0) {
                        Toast.makeText(ForgetPassword.this, "Enter email", Toast.LENGTH_SHORT).show();
                    } else if (!isValidEmail(ed_get_email.getText().toString().trim())) {
                        ed_get_email.setError("Invalid email");
                    } else {
                        if(isConnected){
                        forgotPassCall();
                        }
                        else{
                            Toast.makeText(ForgetPassword.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });
    }

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
    public void forgotPassCall() {
        gD.showAlertDialog(context, "Loading", "Please wait");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GeneralData.LOCAL_IP + "forgotpassword.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(UserRegOne.this, response, Toast.LENGTH_LONG).show();
                        try {
                            Log.i("HH", "strResp : " + response);
                            ArrayList<CommonBean> beanArrayList = new ArrayList<CommonBean>();

                            JSONObject jsobj = new JSONObject(response);
                            gD.altDialog.dismiss();
                            Log.i("HH", "strResp : " + response);
                            if (jsobj.getString("code").equalsIgnoreCase("2")) {

                                View itemView1;
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setCancelable(true);
                                itemView1 = LayoutInflater.from(context)
                                        .inflate(R.layout.forget_pass_popup, null);
                                final AlertDialog altDialog = builder.create();
                                altDialog.setView(itemView1);
                                Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
                                TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
                                txt_content.setText("We have sent a new password to your registered email-id");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ForgetPassword.this,SignIn.class));
                                        finish();
                                        altDialog.dismiss();
                                    }
                                });
                                altDialog.show();
                            }
                            else if (jsobj.getString("code").equalsIgnoreCase("0")) {
// gD.altDialog.dismiss();



                                View itemView1;
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setCancelable(true);
                                itemView1 = LayoutInflater.from(context)
                                        .inflate(R.layout.forget_pass_popup, null);
                                final AlertDialog altDialog = builder.create();
                                altDialog.setView(itemView1);
                                Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
                                TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
                                txt_content.setText(jsobj.getString("status"));
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ed_get_email.setText("");

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

                params.put("email", ed_get_email.getText().toString().trim());


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
        RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
        requestQueue.add(stringRequest);

        RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

    }

}
