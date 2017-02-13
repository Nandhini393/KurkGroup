package com.aryvart.carservice.GenericClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aryvart.carservice.R;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by android2 on 14/9/16.
 */
public class GeneralData {
    public static Context context;
    public static String LOCAL_IP = "http://aryvartdev.com/carservice/api/";
    public static String LOCAL_IP_IMAGE = "http://aryvartdev.com/carservice/wp-content/themes/uploads/";
    public SharedPreferences prefs;
    public AlertDialog altDialog;
    public static int n_count=0;
    public static GoogleMap googleMapGeneral;
    public static String strAddress;
    public static String strLatitutde;
    public static String strLongitude;


    public GeneralData() {

    }

    public GeneralData(Context con) {
        context = con;
        prefs = con.getSharedPreferences("Registerprefs", Context.MODE_PRIVATE);

    }


    public void showAlertDialog(final Context context, String strTitle, String strContent) {
        View itemView1;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        itemView1 = LayoutInflater.from(context)
                .inflate(R.layout.progress_loading_popup, null);
       altDialog = builder.create();
        altDialog.setView(itemView1);
        ProgressBar pb=(ProgressBar)itemView1.findViewById(R.id.progressBar);
        pb.setIndeterminate(true);
        pb.getIndeterminateDrawable().setColorFilter(Color.parseColor("#0987ff"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        TextView txt_title=(TextView)itemView1.findViewById(R.id.txt_title);
        TextView txt_content=(TextView)itemView1.findViewById(R.id.txt_content);
        txt_title.setText(strTitle);
        txt_content.setText(strContent);
        altDialog.show();
    }
}
