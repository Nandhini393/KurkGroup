package com.aryvart.carservice.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aryvart.carservice.Bean.CommonBean;
import com.aryvart.carservice.Interfaces.ChooseServiceInterface;
import com.aryvart.carservice.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by android2 on 9/6/16.
 */
public class ChooseServiceDisplayAdapter extends BaseAdapter {
    Context context;
int n_rate;
    ChooseServiceInterface myInterface;
    List<CommonBean> serviceStationBean;
    private static LayoutInflater inflater = null;
    HashMap<String, JSONObject> hsMap = new HashMap<String, JSONObject>();

    public ChooseServiceDisplayAdapter(Context con, List<CommonBean> alBean, ChooseServiceInterface interfac) {
        context = con;
        serviceStationBean = alBean;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myInterface = interfac;

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
        rowView = inflater.inflate(R.layout.choose_service_disp_content, viewGroup, false);
        holder.txt_serviceName = (TextView) rowView.findViewById(R.id.txt_service_name);
        holder.txt_serviceRate = (TextView) rowView.findViewById(R.id.txt_service_rate);
        holder.img_delete = (ImageView) rowView.findViewById(R.id.img_delete);


        Typeface typeFace3 = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
        holder.txt_serviceName.setTypeface(typeFace3);
        holder.txt_serviceRate.setTypeface(typeFace3);

        holder.txt_serviceName.setText(serviceBean.getStr_serviceName());
        holder.txt_serviceRate.setText("" + serviceBean.getStr_servicePrice());
        Log.i("MM", "IF : "+serviceBean.getStr_servicePrice());


        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.delChoosenService(serviceBean.getN_serviceId(), serviceBean.getStr_serviceName(),serviceBean.getStr_servicePrice());
                serviceStationBean.remove(serviceBean);
                notifyDataSetChanged();

            }
        });
        return rowView;
    }

    class ViewHolder {
        public TextView txt_serviceName, txt_serviceRate;
        ImageView img_delete;
    }
}
