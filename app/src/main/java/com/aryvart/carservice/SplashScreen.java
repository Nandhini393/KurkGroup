package com.aryvart.carservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        Thread timethread = new Thread() {

            public void run() {
                try {
                    sleep(3000);
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