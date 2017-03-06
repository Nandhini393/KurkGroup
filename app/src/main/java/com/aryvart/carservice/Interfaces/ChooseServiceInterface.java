package com.aryvart.carservice.Interfaces;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by android2 on 1/2/17.
 */
public interface ChooseServiceInterface {
    public void getServiceStationAddress(String str_id,String str_service_type,String str_name,String price);
    public void delChoosenService(int str_id,String str_name,String str_price);
    public void getServiceStationMainAddress(String str_id,String str_service_type,String str_name);

}
