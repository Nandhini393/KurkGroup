package com.aryvart.carservice.Interfaces;

/**
 * Created by android2 on 6/2/17.
 */
public interface EditBookInterface {

    public void getEditService(String str_stationId,String str_name,String str_address,
                               String str_date,String str_image,String str_bookingId,String str_serviceArray,
                               String str_ServiceType,String str_PickUpAmt,String str_DiagnoAmt,
                               String str_ModularAmount,String str_pickUpAddress,String str_enableCancelBook);
    public void getService(String str_id,String str_name,Float price);
    public void delChoosenService(int str_id,String str_name,Float str_price);
}
