package com.aryvart.carservice.Bean;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by android2 on 31/1/17.
 */
public class CommonBean implements Comparable<CommonBean> {
    String str_serviceId;
    String str_serviceName;
    String str_serviceAddr;
    String n_image;
    int n_serviceId;
    String str_servicePrice;
    float f_price;
    String str_diagnosis_charge;
    String pickup_charge;
    String modular_reprogramming_charge;
    String str_Response;
    String str_serviceBookingId;
    String str_BookStatus;
    String str_date;
    String str_ServiceType;
    String str_pickUpAddress;
    String str_enableCancelBooking;

    public String getStr_enableCancelBooking() {
        return str_enableCancelBooking;
    }

    public void setStr_enableCancelBooking(String str_enableCancelBooking) {
        this.str_enableCancelBooking = str_enableCancelBooking;
    }

    public String getStr_pickUpAddress() {
        return str_pickUpAddress;
    }

    public void setStr_pickUpAddress(String str_pickUpAddress) {
        this.str_pickUpAddress = str_pickUpAddress;
    }

    public String getStr_ServiceType() {
        return str_ServiceType;
    }

    public void setStr_ServiceType(String str_ServiceType) {
        this.str_ServiceType = str_ServiceType;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getStr_BookStatus() {
        return str_BookStatus;
    }

    public void setStr_BookStatus(String str_BookStatus) {
        this.str_BookStatus = str_BookStatus;
    }

    public String getStr_serviceBookingId() {
        return str_serviceBookingId;
    }

    public void setStr_serviceBookingId(String str_serviceBookingId) {
        this.str_serviceBookingId = str_serviceBookingId;
    }

    public String getStr_Response() {
        return str_Response;
    }

    public void setStr_Response(String str_Response) {
        this.str_Response = str_Response;
    }

    public String getStr_diagnosis_charge() {
        return str_diagnosis_charge;
    }

    public void setStr_diagnosis_charge(String str_diagnosis_charge) {
        this.str_diagnosis_charge = str_diagnosis_charge;
    }

    public String getPickup_charge() {
        return pickup_charge;
    }

    public void setPickup_charge(String pickup_charge) {
        this.pickup_charge = pickup_charge;
    }

    public String getModular_reprogramming_charge() {
        return modular_reprogramming_charge;
    }

    public void setModular_reprogramming_charge(String modular_reprogramming_charge) {
        this.modular_reprogramming_charge = modular_reprogramming_charge;
    }

    public float getF_price() {
        return f_price;
    }

    public void setF_price(float f_price) {
        this.f_price = f_price;
    }


    public int getN_serviceId() {
        return n_serviceId;
    }

    public void setN_serviceId(int n_serviceId) {
        this.n_serviceId = n_serviceId;
    }
    public String getStr_servicePrice() {
        return str_servicePrice;
    }

    public void setStr_servicePrice(String str_servicePrice) {
        this.str_servicePrice = str_servicePrice;
    }

    public String getN_image() {
        return n_image;
    }

    public void setN_image(String n_image) {
        this.n_image = n_image;
    }


    public CommonBean(String str_id,String str_name, String str_price) {

        this.str_serviceName = str_name;
        this.str_servicePrice = str_price;
        this.str_serviceId = str_id;

    }
    public CommonBean() {


    }

    public String getStr_serviceId() {
        return str_serviceId;
    }

    public void setStr_serviceId(String str_serviceId) {
        this.str_serviceId = str_serviceId;
    }

    public String getStr_serviceName() {
        return str_serviceName;
    }

    public void setStr_serviceName(String str_serviceName) {
        this.str_serviceName = str_serviceName;
    }

    public String getStr_serviceAddr() {
        return str_serviceAddr;
    }

    public void setStr_serviceAddr(String str_serviceAddr) {
        this.str_serviceAddr = str_serviceAddr;
    }

    public boolean equals(Object o) {

        Log.i("janavi", "In equals " + "value is :" + this.str_serviceName);
        //Toast.makeText(context, "You have already chosen this " + this.languages + " category please check !", Toast.LENGTH_SHORT).show();
        Log.i("KK", "Choose same**# " + "value is :");
        CommonBean lBean = (CommonBean) o;
        if (lBean.getStr_serviceName().equals(this.str_serviceName)) {
            Log.i("KK", "Choose same " + "value is :");
            //       Toast.makeText(context, "You have already chosen this  category please check !", Toast.LENGTH_SHORT).show();
            return true;
        }
        Log.i("KK", "Choose same** " + "value is :");


        //   Toast.makeText(context, "You have already chosen this " + this.languages + " category please check !", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public int hashCode() {
        System.out.println("In hashcode " + "value is :" + this.str_serviceName);
        int hash = 3;
        hash = 7 * hash + this.str_serviceName.hashCode();
        // Toast.makeText(context, "You have already chosen this " + this.languages + " category please check !", Toast.LENGTH_SHORT).show();
        return hash;
    }

    @Override
    public int compareTo(CommonBean another) {
       //Toast.makeText(getAc, "You have already chosen this " + this.str_serviceName + " category please check !", Toast.LENGTH_SHORT).show();
        return n_serviceId - another.n_serviceId;
    }
}
