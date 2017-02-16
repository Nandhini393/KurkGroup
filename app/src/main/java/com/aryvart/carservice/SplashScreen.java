package com.aryvart.carservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aryvart.carservice.GenericClasses.GeneralData;


public class SplashScreen extends Activity {
    Context context;
    GeneralData gD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        context = this;
        gD = new GeneralData(context);
   /*     SharedPreferences.Editor prefEdit = gD.prefs.edit();
        prefEdit.putString("pickUp_address", null);
        prefEdit.commit();*/
        Thread timethread = new Thread() {

            public void run() {
                try {
                    sleep(3000);


                    Log.i("PP--","name" +gD.prefs.getString("name", null));
                    Log.i("PP--","car_model" +gD.prefs.getString("car_model", null));
                    Log.i("PP--","car_type" +gD.prefs.getString("car_type", null));
                    Log.i("PP--","car_number" +gD.prefs.getString("car_number", null));
                    if(gD.prefs.getString("car_number",null)!=null&&gD.prefs.getString("password",null)!=null)
                    {
                        Intent nextpage = new Intent(SplashScreen.this, BookService.class);
                        startActivity(nextpage);
                        finish();
                    }
                    else{
                        Intent nextpage = new Intent(SplashScreen.this, SignIn.class);
                        startActivity(nextpage);
                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timethread.start();

    }
}