package com.aryvart.carservice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.Interfaces.EditBookInterface;
import com.aryvart.carservice.R;
import com.aryvart.carservice.SignIn;
import com.aryvart.carservice.imageCache.ImageLoader;

import java.util.List;

/**
 * Created by android2 on 9/6/16.
 */
public class CancelBookAdapter extends BaseAdapter {
    Context context;
    EditBookInterface myInterface;
    List<CommonBean> serviceStationBean;
    private static LayoutInflater inflater = null;
ImageLoader img_carImage;

    public CancelBookAdapter(Context con, List<CommonBean> alBean, EditBookInterface interfac) {
        context = con;
        serviceStationBean = alBean;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myInterface = interfac;
        img_carImage= new ImageLoader(context);

    }

    @Override
    public int getCount() {
        return serviceStationBean.size();
        //listDrawerBeen.size()
    }

    @Override
    public Object getItem(int i) {
        return serviceStationBean.get(i);
        //listDrawerBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        View rowView = null;
        final CommonBean serviceBean = serviceStationBean.get(i);
        Log.i("MM", "IF : ");
        rowView = inflater.inflate(R.layout.cancel_book_contents, viewGroup, false);
        holder.txt_comp_name = (TextView) rowView.findViewById(R.id.txt_comp_name);
        holder.txt_comp_addr = (TextView) rowView.findViewById(R.id.txt_address);
        holder.img_profile = (ImageView) rowView.findViewById(R.id.img_profile);
        holder.rl_myLay=(RelativeLayout)rowView.findViewById(R.id.rl_lay);
        holder.txt_cancel=(TextView) rowView.findViewById(R.id.txt_cancel);
        Typeface typeFace1 = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Bold.otf");
        holder.txt_comp_name.setTypeface(typeFace1);
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/Oswald-Regular.ttf");
        holder.txt_comp_addr.setTypeface(typeFace);
        holder.txt_comp_name.setText(serviceBean.getStr_serviceName());
        holder.txt_comp_addr.setText(serviceBean.getStr_serviceAddr());
        img_carImage.DisplayImage(serviceBean.getN_image(),holder.img_profile);
       // holder.img_profile.setBackgroundResource(serviceBean.getN_image());

        holder.txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(serviceBean.getStr_enableCancelBooking().equalsIgnoreCase("0")){
                    myInterface.getEditService(serviceBean.getStr_serviceId(),"","","","",
                            serviceBean.getStr_serviceBookingId(),serviceBean.getStr_Response(), serviceBean.getStr_ServiceType(),
                            serviceBean.getPickup_charge(),serviceBean.getStr_diagnosis_charge(),serviceBean.getModular_reprogramming_charge(),serviceBean.getStr_pickUpAddress(),serviceBean.getStr_enableCancelBooking());

                }
                else if(serviceBean.getStr_enableCancelBooking().equalsIgnoreCase("1")){
                    View itemView1;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    itemView1 = LayoutInflater.from(context)
                            .inflate(R.layout.forget_pass_popup, null);
                    final AlertDialog altDialog = builder.create();
                    altDialog.setView(itemView1);
                    Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
                    TextView txt_content = (TextView) itemView1.findViewById(R.id.txt_content);
                    txt_content.setText("You can't cancel this service now");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            altDialog.dismiss();
                        }
                    });
                    altDialog.show();
                }

            }
        });

        return rowView;
    }

    class ViewHolder {
        public ImageView img_profile;
        public TextView txt_comp_name, txt_comp_addr;
        RelativeLayout rl_myLay;
        TextView txt_cancel;


    }
}
