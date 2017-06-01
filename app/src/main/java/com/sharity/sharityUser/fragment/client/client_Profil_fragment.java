package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client_vertical;
import com.sharity.sharityUser.Utils.Client_SharePointCall;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.DonationActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fonts.TextViewOpenSansSB;
import com.sharity.sharityUser.fragment.DashboardView;
import com.sharity.sharityUser.fragment.Updateable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

import static android.R.attr.padding;
import static android.R.attr.textColor;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sharity.sharityUser.R.id.Siret;
import static com.sharity.sharityUser.R.id.coordinatorLayout;
import static com.sharity.sharityUser.R.id.linear_toolbar;
import static com.sharity.sharityUser.R.id.nom;
import static com.sharity.sharityUser.R.id.te;
import static com.sharity.sharityUser.activity.ProfilActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Profil_fragment extends Fragment implements Updateable,ProfilActivity.OnNotificationUpdateProfil, SwipeRefreshLayout.OnRefreshListener, Adapter_profil_Sharity_client.OnItemDonateClickListener {

    private LayoutInflater vinflater;
    protected ParseUser parseUser = ProfilActivity.parseUser;
    private int recycler_position = -1;
    private Adapter_profil_Sharity_client.OnItemDonateClickListener onItemDonateClickListener;
    private ArrayList<CharityDons> list_dons = new ArrayList<CharityDons>();
    private Profile profile;
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private RecyclerView recycler_charity;
    private Adapter_profil_Sharity_client adapter2=null;
    private Adapter_profil_Sharity_client_vertical adapter_listvertical=null;

    private byte[] imageByte = null;
    private AppBarLayout appBarLayout;
    //Field donation to charity
    private String CharityName;
    private String CharityId;

    private TextViewOpenSansSB do_donationTV;
    private LinearLayout dons_view;
    private int sharepoints_user_temp;
    private int sharepoints_user_depense_temp;

    boolean donation=false;
    private  RecyclerView recycler_charity_vertical;
    private  CoordinatorLayout coordinatorLayout;
    private DashboardView dashboardClientView;
    public static client_Profil_fragment newInstance() {
        client_Profil_fragment myFragment = new client_Profil_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);
        ((ProfilActivity) getActivity()).setProfilListener(client_Profil_fragment.this);
        do_donationTV = (TextViewOpenSansSB) inflate.findViewById(R.id.do_donationTV);
        coordinatorLayout=(CoordinatorLayout)inflate.findViewById(R.id.coordinatorLayout);
        dashboardClientView = (DashboardView) inflate.findViewById(R.id.dashboardview);

        dons_view = (LinearLayout) inflate.findViewById(R.id.dons_view);
        recycler_charity = (RecyclerView) inflate.findViewById(R.id.recycler_charity);

        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
       swipeContainer.setOnRefreshListener(this);

        onItemDonateClickListener = this;
        do_donationTV.setText("Faire un don");

        do_donationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    do_donationTV.setText("Faire un don");
                    donation=true;
                    Intent intent=new Intent(getActivity(),DonationActivity.class);
                     intent.putExtra("source","Client") ;
                     getActivity().startActivityForResult(intent, 1);
            }
        });


        showNetworkDisplay();
        onRefresh();
        return inflate;
    }


    /*
    * Call Update when switch back to this screen
    * */
    @Override
    public void update() {
        Log.d("clicli","update");
            showNetworkDisplay();
            onRefresh();

    }

    private void getProfilFromParse() {
        //if user connected via Facebook, get picture profil
        if (db.getUserCount() > 0) {
                get_Charity();
                dashboardClientView.setClient_DashBoard();
        }
    }


    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }


    @Override
    public void onRefresh() {
            swipeContainer.setRefreshing(false);
            getProfilFromParse();
    }


    private void get_Charity() {
        if (Utils.isConnected(getActivity())) {
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Charity");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> commentList, ParseException e) {
                        if (commentList != null) {
                            list_dons.clear();
                            for (final ParseObject object : commentList) {
                                ParseFile image = (ParseFile) object.getParseFile("Logo");
                                final String name = object.getString("name");
                                final String description = object.getString("description");
                                final String scope = object.getString("scope");
                                final String cause = object.getString("cause");
                                final String email = object.getString("email");
                                final String addresse = object.getString("address");
                                final String siret = object.getString("SIRET");
                                final String RIB = object.getString("RIB");
                                final String telephoneNumber = object.getString("telephoneNumber");
                                ParseGeoPoint geoPoint = object.getParseGeoPoint("location");
                                //  final String url = object.getString("url");


                                final String id = object.getObjectId();
                                Log.d("obj", id);
                                try {
                                    if (image != null) {
                                        imageByte = image.getData();
                                    } else {
                                        imageByte = null;
                                    }
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                list_dons.add(new CharityDons(id, name, description, imageByte, email, addresse, "", siret, telephoneNumber, cause, scope, RIB, geoPoint));

                            }

                            if (adapter2 == null) {
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                recycler_charity.setLayoutManager(layoutManager);
                                adapter2 = new Adapter_profil_Sharity_client(getActivity(), list_dons, onItemDonateClickListener);
                                recycler_charity.setAdapter(adapter2);

                            } else {
                                adapter2.notifyDataSetChanged();
                            }
                        }
                    }
                });
            } catch (NullPointerException f) {

            }
        }else {
        }
    }



    @Override
    public void onItemClick(int item, CharityDons bo) {
        recycler_position = item;
        CharityName = bo.get_nom();
        CharityId = bo.getObjectid();
    }

    @Override
    public void TaskOnNotification(String business, String sharepoints) {
        getProfilFromParse();
    }

    private void showNetworkDisplay(){
        if (!Utils.isConnected(getActivity())) {
            Snackbar.make(coordinatorLayout, "Veuillez activer votre réseau", Snackbar.LENGTH_LONG)
                    .setAction("PARAMETRES", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    })

                    .setDuration(8000)
                    .setActionTextColor(getResources().getColor(R.color.white))
                    .show();
        }
    }

}