package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client_vertical;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.DonationActivity;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fragment.DashboardView;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.donation.Donation_container_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_PaimentStepOne_fragment;

import java.util.ArrayList;
import java.util.List;
import static com.sharity.sharityUser.activity.ProfilActivity.db;
import static com.sharity.sharityUser.activity.ProfilActivity.parseUser;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.source;


/**
 * Created by Moi on 14/11/15.
 */
public class client_donation_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener, Adapter_profil_Sharity_client_vertical.OnItemDonateClickListener, View.OnClickListener {

    private ArrayList<CharityDons> list_dons = new ArrayList<CharityDons>();
    OnCharitySelected onCharitySelected;
    private LayoutInflater vinflater;
    private Adapter_profil_Sharity_client_vertical.OnItemDonateClickListener onItemDonateClickListener;
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private RecyclerView recycler_charity;
    private Adapter_profil_Sharity_client_vertical adapter2=null;
    private byte[] imageByte = null;

    private ImageView close;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Donation_container_fragment parentContainer;

    public interface OnCharitySelected{
        public void OnSelected(CharityDons user, int i);
    }


    public static client_donation_fragment newInstance() {
        client_donation_fragment myFragment = new client_donation_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        if (source!=null){
            if (source.equals("Client")){
                inflate = inflater.inflate(R.layout.fragment_donation_client, container, false);
            }else {
                inflate = inflater.inflate(R.layout.fragment_donation_business, container, false);

            }
        }

        parentContainer = ((Donation_container_fragment) getParentFragment());
        parentContainer.dashboardClientView = (DashboardView) inflate.findViewById(R.id.dashboardview);
        close= (ImageView)inflate.findViewById(R.id.close);
        close.setOnClickListener(this);
        toolbar = (Toolbar) inflate.findViewById(R.id.toolbar);
        toolbar_title = (TextView) inflate.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Dons");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        recycler_charity = (RecyclerView) inflate.findViewById(R.id.recycler_charity);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        onItemDonateClickListener = this;


        if (source!=null){
            if (source.equals("Client")) {
                User user=db.getUser(parseUser.getObjectId());
                parentContainer.user_solde=Integer.parseInt(user.get_sharepoint());
                parentContainer.user_sharepoint_expense=Integer.parseInt(user.get_sharepoint_depense());
            }else {
                final String objectid = db.getBusinessId();
                Business business = db.getBusiness(objectid);
                parentContainer.user_solde=Integer.parseInt(business.getSolde());
                parentContainer.user_sharepoint_expense=Integer.parseInt(business.getSharepoint_depense());
            }

                if (parentContainer.sharepoint_genarated_screenTransition!=0){
                    parentContainer.dashboardClientView.setSolde(parentContainer.sharepoint_solde_screenTransition);
                    parentContainer.dashboardClientView.setCircularValue(parentContainer.sharepoint_solde_screenTransition,1500);
                    parentContainer.dashboardClientView.setGeneratedSharepoint(parentContainer.sharepoint_genarated_screenTransition);
                }else {
                    parentContainer.dashboardClientView.setSolde(parentContainer.user_solde);
                    parentContainer.dashboardClientView.setCircularValue(parentContainer.user_solde,1500);
                    parentContainer.dashboardClientView.setGeneratedSharepoint(parentContainer.user_sharepoint_expense);
                    parentContainer.sharepoint_solde_screenTransition=Integer.parseInt(parentContainer.dashboardClientView.getSolde());
                    parentContainer.sharepoint_genarated_screenTransition=Integer.parseInt(parentContainer.dashboardClientView.getGeneratedSharepoint());
                }
            }



        if (list_dons.isEmpty()){
            getProfilFromParse();
        }else {
            Initialize_ListView();
        }

        return inflate;
    }



    @Override
    public void update() {
        Log.d("clicli","update");

    }

    private void getProfilFromParse() {
        //if user connected via Facebook, get picture profil
        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            try {
                get_Charity();
               // getTransaction();
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                swipeContainer.setRefreshing(false);
            }
        }
    }




    /*
     * Get local database to display nav drawer including Profil picture etc
     **/
    private void getTransaction() {
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", parseUser.getObjectId());
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> commentList, ParseException e) {
                    if (commentList != null) {
                        int sharepoints = 0;
                        int sharepoints_depense = 0;

                        for (ParseObject object : commentList) {
                            sharepoints = object.getInt("sharepoints");
                            sharepoints_depense = object.getInt("sharepoints_depense");
                        }

                        parentContainer.sharepoints_user_depense=sharepoints_depense;
                        parentContainer.user_solde = sharepoints;
                        Log.d("sharepoints", String.valueOf(sharepoints));
                        swipeContainer.setRefreshing(false);
                    }
                }
            });
        } catch (NullPointerException f) {

        }
    }



    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            if (list_dons.isEmpty()){
                getProfilFromParse();
            }else {
                Initialize_ListView();
            }
            swipeContainer.setRefreshing(false);
        }
    }


    private void get_Charity() {
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
                            final String url = object.getString("url");
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
                            list_dons.add(new CharityDons(id, name, description, imageByte, email, addresse, url, siret, telephoneNumber, cause, scope, RIB, geoPoint));
                        }

                        if (adapter2==null){
                            Log.d("passedadapter","ada");
                            Initialize_ListView();
                        }else {
                            adapter2.notifyDataSetChanged();
                        }
                    }
                }
            });
        } catch (NullPointerException f) {

        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        // check if parent Fragment implements listener
        if (getParentFragment() instanceof client_donation_fragment.OnCharitySelected) {
            onCharitySelected = (client_donation_fragment.OnCharitySelected) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnSelection");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCharitySelected = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:{
                getActivity().finish();
            }
        }
    }

    @Override
    public void onItemClick(int item, CharityDons bo) {
        parentContainer.recycler_position = item;
        parentContainer.CharityName = bo.get_nom();
        parentContainer.CharityId = bo.getObjectid();
        onCharitySelected.OnSelected(bo,item);
    }



    private void Initialize_ListView(){
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_charity.setLayoutManager(layoutManager);
        adapter2 = new Adapter_profil_Sharity_client_vertical(getActivity(), list_dons, onItemDonateClickListener);
        recycler_charity.setAdapter(adapter2);
    }
}