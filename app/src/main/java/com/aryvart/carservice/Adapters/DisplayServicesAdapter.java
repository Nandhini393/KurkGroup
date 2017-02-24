package com.aryvart.carservice.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class DisplayServicesAdapter extends BaseAdapter {
    Context context;
    ChooseServiceInterface myInterface;
    List<CommonBean> serviceStationBean;
    private static LayoutInflater inflater = null;
    HashMap<String, JSONObject> hsMap = new HashMap<String, JSONObject>();
    int n_rate;
    public DisplayServicesAdapter(Context con, List<CommonBean> alBean, ChooseServiceInterface interfac) {
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


        rowView = inflater.inflate(R.layout.list_choose_service_check_contents, viewGroup, false);

        final CommonBean serviceBean = serviceStationBean.get(i);
        holder.txt_service_name1 = (TextView) rowView.findViewById(R.id.txt_service_name1);
        holder.txt_service_rate1 = (TextView) rowView.findViewById(R.id.txt_service_rate1);

        Typeface typeFace3 = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
        holder.txt_service_name1.setTypeface(typeFace3);
        holder.txt_service_rate1.setTypeface(typeFace3);

        Log.e("AS", serviceBean.getStr_serviceName() + "---" + serviceBean.getN_serviceId());
        holder.txt_service_name1.setText(serviceBean.getStr_serviceName());
        holder.txt_service_rate1.setText("" + serviceBean.getF_price());
        Log.i("AS", "IF : "+ serviceBean.getF_price());
        n_rate+=serviceBean.getF_price();
        Log.i("AS!", "IF : "+n_rate);
        myInterface.delChoosenService(0,"", Float.valueOf(n_rate));
        return rowView;
    }

    class ViewHolder {
        public TextView txt_service_name1;
        TextView txt_service_rate1;
    }
}
