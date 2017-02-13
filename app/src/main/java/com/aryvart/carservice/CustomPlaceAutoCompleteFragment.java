package com.aryvart.carservice;

/**
 * Created by aryvart2 on 29/4/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.aryvart.carservice.GenericClasses.GeneralData;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Locale;

/**
 * Created by sunny on 22/12/15.
 */
public class CustomPlaceAutoCompleteFragment extends PlaceAutocompleteFragment {

    ImageView im;
    Marker mark;
    Double lat = 0.0, longi = 0.0;
    LatLng TutorialsPoints = new LatLng(lat, longi);

    Place places;
    GeneralData gD,mygD;
    GoogleMap googleMap;
    String str_address;
    ProgressDialog dialog;
    private EditText editSearch;
    private View zzaRh;
    private View zzaRi;
    private EditText zzaRj;
    @Nullable
    private LatLngBounds zzaRk;
    @Nullable
    private AutocompleteFilter zzaRl;
    @Nullable
    private PlaceSelectionListener zzaRm;
    int nScreenHeight;

    public CustomPlaceAutoCompleteFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View var4 = null;
        try {
            var4 = inflater.inflate(R.layout.user_custom_place_autocomplete, container, false);
            im = (ImageView) var4.findViewById(R.id.search_icon);
            //tv = (TextView) var4.findViewById(R.id.my_text);

            editSearch = (EditText) var4.findViewById(R.id.editWorkLocation);


            str_address = editSearch.getText().toString();
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zzzG();
                    editSearch.setVisibility(View.VISIBLE);

                }
            });
            editSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zzzG();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return var4;

        // android.support.v4.app.Fragment currentFragment = getContext().getSupportFragmentManager().findFragmentById(R.id.fragment_container);


    }


    public void onDestroyView() {
        this.zzaRh = null;
        this.zzaRi = null;
        this.editSearch = null;
        super.onDestroyView();
    }

    public void setBoundsBias(@Nullable LatLngBounds bounds) {
        this.zzaRk = bounds;
    }

    public void setFilter(@Nullable AutocompleteFilter filter) {
        this.zzaRl = filter;
    }

    public void setText(CharSequence text) {
        this.editSearch.setText(text);
        //this.zzzF();
    }

    public void setHint(CharSequence hint) {
        this.editSearch.setHint(hint);
        this.zzaRh.setContentDescription(hint);
    }

    public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
        this.zzaRm = listener;


    }

    private void zzzF() {
        boolean var1 = !this.editSearch.getText().toString().isEmpty();
        //this.zzaRi.setVisibility(var1?0:8);
    }

    private void zzzG() {
        int var1 = -1;

        try {
            Intent var2 = (new PlaceAutocomplete.IntentBuilder(2)).setBoundsBias(this.zzaRk).setFilter(this.zzaRl).zzeq(this.editSearch.getText().toString()).zzig(1).build(this.getActivity());

            this.startActivityForResult(var2, 1);
        } catch (GooglePlayServicesRepairableException var3) {
            var1 = var3.getConnectionStatusCode();
            Log.e("Places", "Could not open autocomplete activity", var3);
        } catch (GooglePlayServicesNotAvailableException var4) {
            var1 = var4.errorCode;
            Log.e("Places", "Could not open autocomplete activity", var4);
        }

        if (var1 != -1) {
            GoogleApiAvailability var5 = GoogleApiAvailability.getInstance();
            var5.showErrorDialogFragment(this.getActivity(), var1, 2);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1) {

                Log.e("PL", "i'm in onActivityResult 1 : " + resultCode);

                if (resultCode == -1) {

                    Log.e("PL", "i'm in onActivityResult 2 : " + resultCode);

                    Place var4 = PlaceAutocomplete.getPlace(this.getActivity(), data);
                    if (mark != null) {
                        mark.remove();
                    }

                    gD = new GeneralData();


                    DisplayMetrics dp = getResources().getDisplayMetrics();
                    int nHeight = dp.heightPixels;
                    nScreenHeight = (int) ((float) nHeight / (float) 2.1);

                    googleMap = gD.googleMapGeneral;
                    googleMap.clear();

                //    GetProviders findProviders = new GetProviders(String.valueOf(var4.getLatLng().latitude), String.valueOf(var4.getLatLng().longitude), "");
                 //   findProviders.execute();

                    //place marker
                    TutorialsPoints = new LatLng(Double.parseDouble(String.valueOf(var4.getLatLng().latitude)), Double.parseDouble(String.valueOf(var4.getLatLng().longitude)));
                    if (mark != null) {
                        mark.remove();
                    }

                    //Get the Address from the lat and lang

                    String result = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addressList = null;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        addressList = geocoder.getFromLocation(Double.parseDouble(String.valueOf(var4.getLatLng().latitude)), Double.parseDouble(String.valueOf(var4.getLatLng().longitude)), 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                            }
                            sb.append(address.getLocality()).append("\n");
                            sb.append(address.getPostalCode()).append("\n");
                            sb.append(address.getCountryName());
                            result = sb.toString();
                            Log.e("AddrVal", result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //  mark = gD.googleMapGeneral.addMarker(new MarkerOptions().position(TutorialsPoints).snippet(result).title("hii").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));


                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(TutorialsPoints)      // Sets the center of the map to Mountain View
                            .zoom(17)
                            .tilt(90)
                                    // Sets the zoom// Sets the orientation of the camera to east// Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    gD.googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    gD.googleMapGeneral.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(TutorialsPoints)      // Sets the center of the map to Mountain View
                                    .zoom(17)
                                    .tilt(90)
                                            // Sets the zoom// Sets the orientation of the camera to east// Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            gD.googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mark.showInfoWindow();
                            return false;
                        }
                    });


                    if (this.zzaRm != null) {
                        this.zzaRm.onPlaceSelected(var4);
                        Log.e("PL", "" + var4);
                    }

                    Log.i("GGG", "CustomPlaceAutoCompleteFragment : " + var4.getAddress().toString());


                    gD.strAddress = var4.getAddress().toString();
                    gD.strLatitutde = String.valueOf(var4.getLatLng().latitude);
                    gD.strLongitude = String.valueOf(var4.getLatLng().longitude);


                   BookService.sharedpreferences = getActivity().getSharedPreferences("myprefer", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = BookService.sharedpreferences.edit();
                    editor.putString("Lat", String.valueOf(var4.getLatLng().latitude));
                    editor.putString("Long", String.valueOf(var4.getLatLng().longitude));
                    editor.commit();

                    Log.i("LL", "place_lat" + String.valueOf(var4.getLatLng().latitude));
                    Log.i("LL", "place_long" + String.valueOf(var4.getLatLng().longitude));

                    //gD.strAddress= var4.getAddress().toString();

                    this.setText(var4.getName().toString());


                } else if (resultCode == 2) {
                    Status var5 = PlaceAutocomplete.getStatus(this.getActivity(), data);
                    if (this.zzaRm != null) {
                        this.zzaRm.onError(var5);
                        Log.e("place---->", "" + var5);
                    }
                }


            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

   /* private void drawMarker(LatLng point, final String str_address, final String strProviderId, final String strResp) {
        try {
            // Creating an instance of MarkerOptions
            final MarkerOptions markerOptions = new MarkerOptions();
            gD.googleMapGeneral.setInfoWindowAdapter(new MyInfoWindowAdapter());
            // Setting latitude and longitude for the marker
            markerOptions.position(point).snippet(str_address).title(strProviderId).icon(BitmapDescriptorFactory.fromResource(R.drawable.greennewmarker)).draggable(true);
            // Adding marker on the Google Map

            googleMap.addMarker(markerOptions);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* class GetProviders extends AsyncTask {

        String sResponse = null;
        String latitude, longitude, str_address;

        public GetProviders(String latitude, String longitude, String str_addr) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.str_address = str_addr;
        }


        @Override
        protected Object doInBackground(Object[] params) {
            try {

                String charset = "UTF-8";
                String requestURL = gD.common_baseurl+"findprovider.php";
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                multipart.addHeaderField("Content-Encoding", "gzip");
                multipart.addFormField("latitude", latitude);
                multipart.addFormField("longitude", longitude);
                multipart.addFormField("companyaddress", str_address);
                List<String> response = multipart.finish();

                StringBuilder sb = new StringBuilder();
                for (String line : response) {
                    System.out.println(line);
                    sb.append(line);
                }
                try {
                    JSONObject jsonObj = new JSONObject(sb.toString());
                    Log.i("GGG", "CustomPlaceAutoCompleteFragment : " + jsonObj.toString());
                    sResponse = jsonObj.toString();
                    Log.e("GGG:", "CustomPlaceAutoCompleteFragment : " + sResponse);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return sResponse;
        }

        @Override
        protected void onPreExecute() {
            gD.callload(getActivity(), nScreenHeight);



            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object response) {



            if(gD.alertDialog!=null) {
                gD.alertDialog.dismiss();
            }
            try {
                JSONObject jsobj = new JSONObject(sResponse);
                // Toast.makeText(getApplicationContext(), jsobj.getString("status"), Toast.LENGTH_SHORT).show();
                if (jsobj.getString("status").equalsIgnoreCase("success")) {
                    JSONArray providersJSONArray = jsobj.getJSONArray("providers");
                    for (int n = 0; n < providersJSONArray.length(); n++) {
                        JSONObject providersJSONobject = providersJSONArray.getJSONObject(n);
                        String latitude = providersJSONobject.getString("latitude");
                        String longitude = providersJSONobject.getString("longitude");
                        String str_com_addr = providersJSONobject.getString("companyaddress");
                        String strProviderId = providersJSONobject.getString("providerid");
                        drawMarker(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), str_com_addr, strProviderId, providersJSONArray.toString());
                    }
                }
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Log.i("MAP", "Marker hasbeen clicked.........1");
                        Log.i("MAP", "Marker hasbeen clicked.........2");
                        try {
                            JSONObject jsobj = new JSONObject(sResponse);
                            JSONArray jsARRes = jsobj.getJSONArray("providers");

                            for (int i = 0; i < jsARRes.length(); i++) {
                                if (jsARRes.getJSONObject(i).getString("providerid").equalsIgnoreCase(marker.getTitle())) {
                                    Log.i("rK", "providernam : " + jsARRes.getJSONObject(i).getString("providername"));
                                    Intent detailIntent = new Intent(getActivity(), UserProviderDetails.class);
                                    detailIntent.putExtra("providerid", jsARRes.getJSONObject(i).getString("providerid"));
                                    detailIntent.putExtra("providernam", jsARRes.getJSONObject(i).getString("providername"));
                                    detailIntent.putExtra("providerimg", "http://aryvartdev.com/projects/utician/upload/" + jsARRes.getJSONObject(i).getString("image_path"));
                                    detailIntent.putExtra("provideradd", marker.getSnippet());
                                    startActivity(detailIntent);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.user_current_loc_addr_alert, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.text_address));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }*/
}