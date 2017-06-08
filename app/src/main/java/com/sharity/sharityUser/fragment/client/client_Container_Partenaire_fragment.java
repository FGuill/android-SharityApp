package com.sharity.sharityUser.fragment.client;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.Category;
import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.BO.Promo;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterGridViewCategorie;
import com.sharity.sharityUser.Utils.AdapterPartenaireClient;
import com.sharity.sharityUser.Utils.GPSservice;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fragment.MapCallback;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.pro.Pro_Paiment_StepTwo_Classique_fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.category;
import static android.app.Activity.RESULT_OK;
import static com.sharity.sharityUser.R.id.latitude;
import static com.sharity.sharityUser.R.id.nom;
import static com.sharity.sharityUser.R.id.swipeContainer;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.activity.ProfilActivity.permissionRuntime;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Container_Partenaire_fragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Updateable, MapCallback, ResultCallback<LocationSettingsResult> {

    public static GoogleApiClient mGoogleApiClient;
    protected static GPSservice gpSservice;
    protected static ArrayList<LocationBusiness> list_shop = new ArrayList<>();
    protected static ArrayList<LocationBusiness> list_shop_filtered = new ArrayList<LocationBusiness>();
    protected static ArrayList<Category> list_categorieReal = new ArrayList<Category>();

    protected static RelativeLayout recyclerFrame;
    protected static ParseGeoPoint geoPoint;
    protected static double latitude = 0.0;
    protected static double longitude = 0.0;

    private double mlatitude=0.0;
    private double mlongitude=0.0;
    private String businessName = null;
    int sizelist=0;
    int number=0;
    int count_images=0;
    public static int mSelectedItem_categories=-1;




    protected static Location mLastLocation;
    protected static Marker mCurrLocationMarker;
    private View inflate;
    protected ParseUser parseUser = ProfilActivity.parseUser;
    private byte[] imageByte = null;
    protected static boolean on=true;

    public  static  boolean isShop=true;
    protected static RelativeLayout frameCategorie;
    protected static Button search_layout;
    protected static View mViewcategorieColapse;
    protected static LayoutInflater vinflater;
    protected static GridView gridview;
    protected static ArrayList<Integer> images = new ArrayList<>();
    int REQUEST_CHECK_SETTINGS = 100;
    int size_listshopNew=0;

    protected static boolean isLocationUpdate=false;
    protected static GoogleMap mMap=null;
    public static LocationRequest mLocationRequest;
    public static AdapterGridViewCategorie gridViewCategorie;
    public static String categorie;
    protected static int countUpdate=0;

    private int Request_Interval=2500;
    private int Request_Fastest_Interval=2000;

    protected static ProgressBar progressBar;

    public static client_Container_Partenaire_fragment newInstance() {
        client_Container_Partenaire_fragment myFragment = new client_Container_Partenaire_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    protected interface DataCallBack{
        public void onSuccess();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_partenaire_container_client, container, false);

        gpSservice=new GPSservice(getActivity());
        //We instantiate the List in first, seen by user first.

        permissionRuntime = new PermissionRuntime(getActivity());

        client_PartenaireMap_fragment fragTwo = client_PartenaireMap_fragment.newInstance(true);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content, fragTwo, "client_PartenaireMap_fragment");
        ft.commit();

        return inflate;
    }

    // Callback to open map fragment when user click open map.
    @Override
    public void onOpen(ArrayList<LocationBusiness> data, boolean type) {
        FragmentManager fm = getChildFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationSlideFragment(getActivity(),fm,R.id.content,client_Partenaire_list_fragment.newInstance(),"client_Partenaire_list_fragment", Gravity.BOTTOM,true);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.content, client_Partenaire_list_fragment.newInstance(), fm, "client_Partenaire_list_fragment", true);
        }
    }

    // Callback close map when user click close map.
    @Override
    public void onClose() {
        try {
            ((ProfilActivity)getActivity()).onBackPressed();
            progressBar.setVisibility(View.INVISIBLE);

        }catch (IllegalStateException e){
        }
    }

    @Override
    public void update() {
    }

    /*
    *  Google Api Location
    * */

    public synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            if (!mGoogleApiClient.isConnected()){
                mGoogleApiClient.connect();

            }
            get_Categorie();
        }catch (NullPointerException e){

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        OpenGPSettings();

        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {

                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();

                    Log.d("latitude", String.valueOf(latitude));
                    geoPoint = new ParseGeoPoint(latitude, longitude);
                    client_Partenaire_list_fragment list_fragment = (client_Partenaire_list_fragment) getChildFragmentManager().findFragmentByTag("client_Partenaire_list_fragment");
                    if (list_fragment!=null && list_fragment.isVisible()) {
                        list_fragment.HideNetworkView();
                        list_fragment.ShowShop();
                    }
                }else {

                }
                startLocationUpdates();
            }
        }
    }

    // Trigger new location updates at interval
    public void startLocationUpdates() {
        if (getActivity() != null) {
            countUpdate=0;
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setInterval(Request_Interval);
                mLocationRequest.setFastestInterval(Request_Fastest_Interval);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                isLocationUpdate=true;
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, gpsLocationListener);
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


   public LocationListener gpsLocationListener = new com.google.android.gms.location.LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d("locationlatitude", String.valueOf(location.getLatitude()));

            countUpdate++;
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            if (Utils.isConnected(getActivity())) {
                if (geoPoint!=null){
                    SaveLocationUser(geoPoint);
                }
            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            geoPoint = new ParseGeoPoint(latitude, longitude);

            client_Partenaire_list_fragment list_fragment = (client_Partenaire_list_fragment) getChildFragmentManager().findFragmentByTag("client_Partenaire_list_fragment");
            client_PartenaireMap_fragment PartenaireMap_fragment = (client_PartenaireMap_fragment) getChildFragmentManager().findFragmentByTag("client_PartenaireMap_fragment");

            if (countUpdate<=2){
            if (list_fragment != null && list_fragment.isVisible()) {
                    if (isShop){
                        list_fragment.ShowShop();
                    }else {
                        list_fragment.ShowSPromotion();
                    }
            }

            if (PartenaireMap_fragment != null && PartenaireMap_fragment.isVisible()) {
                if (isShop){
                    PartenaireMap_fragment.ShowShop();
                }else {
                    PartenaireMap_fragment.ShowSPromotion();
                }
            }
            }else {
                Request_Interval=600000; //10 min request after getting a initial position.
                Request_Fastest_Interval=600000;
                if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
                    RemoveLocationUpdate();
                    Log.d("mGoogleApiClient", "Reconnect mew setting");
                }
            }
        }
    };

    private void OpenGPSettings(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
    }

    //Save current coordinate location in user Parse.
    private static void SaveLocationUser(ParseGeoPoint point) {
        try {
            ParseUser parseUser = ParseUser.getCurrentUser();
            parseUser.put("geoloc", point);
            parseUser.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // TODO Auto-generated method stub
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        //updated successfully
                    }
                }
            });
        } catch (NullPointerException e) {
        }
    }



    public void RemoveLocationUpdate(){

        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsLocationListener);
                isLocationUpdate=false;
            }
        }
    }

    public void ChangeTitleActivity(String title){
        ((ProfilActivity) getActivity()).toolbarTitle.setText(title);
    }



    //Call Business data
    public void GetBusiness(final DataCallBack dataCallBack) {
        try {
            if (mMap!=null){
                mMap.clear();
            }

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
            if (client_Container_Partenaire_fragment.latitude != 0.0) {
                query.whereWithinKilometers("location", geoPoint, 0.70);
            }query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        list_shop.clear();
                        list_shop_filtered.clear();
                        // Iterating over the results

                        for (int i = 0; i < objects.size(); i++) {
                            double queryLatitude = objects.get(i).getParseGeoPoint("location").getLatitude();
                            double queryLongitude = objects.get(i).getParseGeoPoint("location").getLongitude();
                            String business_name = objects.get(i).get("businessName").toString();


                            String mCategorie="";
                            if(objects.get(i).getParseObject("category")!=null){
                                ParseObject categorie=objects.get(i).getParseObject("category");
                                mCategorie=categorie.getObjectId();
                            }

                            // String addresse=  objects.get(i).get("address").toString();
                            float distance = Utils.distance(client_Container_Partenaire_fragment.latitude, longitude, queryLatitude, queryLongitude);
                            ParseFile image = objects.get(i).getParseFile("logo");
                            try {
                                if (image != null) {
                                    imageByte = image.getData();
                                } else {
                                    imageByte = null;
                                }
                            } catch (ParseException e1) {
                                imageByte = null;
                                e1.printStackTrace();
                            }
                            list_shop.add(new LocationBusiness(queryLatitude, queryLongitude, business_name,mCategorie, distance, imageByte,false, false));
                        }
                        //Place current location marker
                        list_shop_filtered.addAll(list_shop);
                        //Create offset with last business
                        if (list_shop.size() > 1) {
                            Object business = list_shop.get(list_shop.size() - 1);
                            Double lat = ((LocationBusiness) business).get_latitude();
                            Double lon = ((LocationBusiness) business).get_longitude();
                            String mbusiness = ((LocationBusiness) business).get_businessName();
                            String categorie = ((LocationBusiness) business).getCategorie();
                            float mdistance = ((LocationBusiness) business).getDistance();
                            byte[] pic = ((LocationBusiness) business).getPicture();
                            list_shop.add(new LocationBusiness(lat, lon, mbusiness,categorie, mdistance, pic,false, true));
                        }

                        if (progressBar!=null){
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        dataCallBack.onSuccess();
                    }
                }
            });
        }catch (NullPointerException e){

        }
    }

    //Call Promo data
    public void get_Promo(final DataCallBack dataCallBack) {

    final ArrayList<LocationBusiness> PromoBiz=new ArrayList<>();
        final ArrayList<LocationBusiness> promoTemp =new ArrayList<>();
        final ArrayList<String> businessPointer=new ArrayList<>();

        try {
            if (mMap!=null){
                mMap.clear();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Promo");
            query.include("Business");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList != null) {
                        sizelist=commentList.size();
                        PromoBiz.clear();
                        promoTemp.clear();
                        list_shop.clear();
                        list_shop_filtered.clear();
                        images.clear();
                        number=0;
                        ParseObject lastObj=commentList.get(commentList.size()-1);
                        final String lastitemid= lastObj.getObjectId();

                        for (final ParseObject object : commentList) {
                            //   ParseFile image = (ParseFile) object.getParseFile("Logo");
                           // final String prix = object.getString("prix");
                            final String bonusSharepoints = String.valueOf(object.getInt("bonusSharepoints"));
                            final String description = object.getString("title");
                            String amountDeductionCents=String.valueOf(object.getInt("amountDeductionCents")/100);

                            String mcategorie="";
                            promoTemp.add(new LocationBusiness(mcategorie, 0.0, 0.0, "", 0, description, "", amountDeductionCents, false));

                            ParseObject biz = object.getParseObject("business");
                            businessPointer.add(biz.getObjectId());
                            Log.d("kokobiz", String.valueOf(biz.getObjectId()));

                           /* try {
                                imageByte = image.getData();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }*/

                        }

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                        query.whereContainedIn("objectId",businessPointer);
                        query.include("Category");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> object, ParseException e) {
                                if (e == null) {
                                    list_shop.clear();
                                    list_shop_filtered.clear();
                                    // Iterating over the results
                                    for (String biz : businessPointer){
                                        for (int i = 0; i < object.size(); i++) {
                                            if (biz.equalsIgnoreCase(object.get(i).getObjectId())) {
                                                if (object.get(i).getParseGeoPoint("location") != null) {
                                                    ParseGeoPoint getParseGeoPoint = object.get(i).getParseGeoPoint("location");
                                                    mlatitude = getParseGeoPoint.getLatitude();
                                                    mlongitude = getParseGeoPoint.getLongitude();
                                                }
                                                String mCategorie="";
                                                if(object.get(i).getParseObject("category")!=null){
                                                    ParseObject categorie=object.get(i).getParseObject("category");
                                                    mCategorie=categorie.getObjectId();
                                                }
                                                businessName = object.get(i).getString("businessName");
                                                PromoBiz.add(new LocationBusiness(mCategorie,mlatitude, mlongitude, businessName,0,"","","",false));
                                            }
                                        }
                                    }

                                    for (int i = 0; i < promoTemp.size(); i++) {
                                        list_shop.add(new LocationBusiness(PromoBiz.get(i).getCategorie(),PromoBiz.get(i).get_latitude(),PromoBiz.get(i).get_longitude(),PromoBiz.get(i).get_businessName(),promoTemp.get(i).getDistance(),promoTemp.get(i).getDescription(),promoTemp.get(i).getPrix(),promoTemp.get(i).getReduction(),false));
                                    }

                                    list_shop_filtered.addAll(list_shop);

                                    if (list_shop.size()>1) {
                                        Object business = list_shop.get(list_shop.size() - 1);
                                        Double lat = ((LocationBusiness) business).get_latitude();
                                        Double lon = ((LocationBusiness) business).get_longitude();
                                        String description = ((LocationBusiness) business).getDescription();
                                        String prix = ((LocationBusiness) business).getPrix();
                                        String reduction = ((LocationBusiness) business).getReduction();
                                        String busines = ((LocationBusiness) business).get_businessName();
                                        String categorie = ((LocationBusiness) business).getCategorie();
                                        list_shop.add(new LocationBusiness(categorie,lat, lon, busines, 0 , description,prix,reduction, true));
                                    }

                                    dataCallBack.onSuccess();
                                    size_listshopNew=list_shop.size();
                                    if (progressBar!=null){
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                }
                            }
                        }
                        );
                    }
                }
            });
        } catch (NullPointerException f) {
        }
    }

    //Call Category data
    public void get_Categorie(final DataCallBack callBack) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList != null) {
                        list_categorieReal.clear();
                        for (final ParseObject object : commentList) {
                            final String objectId = object.getObjectId();
                            String name=object.getString("name");
                            ParseFile image = (ParseFile) object.getParseFile("image");
                            try {
                                imageByte = image.getData();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            list_categorieReal.add(new Category(objectId,name,imageByte));
                        }
                        callBack.onSuccess();
                    }
                }
            });
        } catch (NullPointerException f) {
        }
    }

    public void get_Categorie() {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList != null) {
                        list_categorieReal.clear();
                        for (final ParseObject object : commentList) {
                            final String objectId = object.getObjectId();
                            String name=object.getString("name");
                            ParseFile image = (ParseFile) object.getParseFile("image");
                            try {
                                imageByte = image.getData();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            list_categorieReal.add(new Category(objectId,name,imageByte));
                        }
                    }
                }
            });
        } catch (NullPointerException f) {
        }
    }
    public static ArrayList<Category> getList_categorie() {
        return list_categorieReal;
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    //failed to show dialog
                }catch (NullPointerException e){

                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }


    public static boolean isLocationUpdate() {
        return isLocationUpdate;
    }


    // when fragment container start, we check if permission is granted and lauc
    @Override
    public void onStart() {
        if (mGoogleApiClient == null) {
            try{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {try {
                    if (getActivity() instanceof ProfilActivity) {
                        if (mGoogleApiClient == null) {
                            Log.d("mGoogleApiClient", "Start connection");
                            buildGoogleApiClient();
                            if (ContextCompat.checkSelfPermission(getActivity(),
                                    permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION},
                                        permissionRuntime.Code_ACCESS_FINE_LOCATION);
                            }
                            progressBar.setVisibility(View.VISIBLE);
                        }

                    }
                }catch (NullPointerException e){

                }
                }
            }, 3200);}

            catch (NullPointerException e){
            }
        }else {
            if (!mGoogleApiClient.isConnected()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buildGoogleApiClient();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
                Log.d("mGoogleApiClient", "connection");
            }
        }


        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsLocationListener);
                Log.d("GoogleApiClient","Disconnect");
            }
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
}