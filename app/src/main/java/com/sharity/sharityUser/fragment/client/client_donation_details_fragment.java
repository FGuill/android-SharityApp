package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client_vertical;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fragment.DashboardView;
import com.sharity.sharityUser.fragment.Updateable;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sharity.sharityUser.R.id.dashboardview;
import static com.sharity.sharityUser.R.id.recycler_charity;
import static com.sharity.sharityUser.R.id.toolbar_title;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.activity.DonationActivity.db;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.CharityId;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.CharityName;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.REP_DELAY;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.UpdateHandler;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.dashboardClientView;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.do_donationTV;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.donation;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.isLongClick;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.mAutoDecrement;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.mAutoIncrement;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.points;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.recycler_position;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.sharepoint_genarated_screenTransition;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.sharepoint_solde_screenTransition;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.sharepoints_moins;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.sharepoints_plus;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.sharepoints_user_depense;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.sharepoints_user_donate;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.sharepoints_user_temp;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.source;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.user_sharepoint_expense;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.user_solde;


/**
 * Created by Moi on 14/11/15.
 */
public class client_donation_details_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener {

    OnCharitySelected onCharitySelected;
    private LayoutInflater vinflater;
    private Adapter_profil_Sharity_client_vertical.OnItemDonateClickListener onItemDonateClickListener;
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private Adapter_profil_Sharity_client_vertical adapter2=null;
    private LinearLayout dons_view;
    CharityDons charityDons=null;
    private ImageView close;
    private Toolbar toolbar;
    private TextView toolbar_title;
    @Override
    public void onRefresh() {

    }

    public interface OnCharitySelected{
        public void OnSelected(CharityDons user, int i);

    }

    public static client_donation_details_fragment newInstance(CharityDons object) {
        client_donation_details_fragment myFragment = new client_donation_details_fragment();
        Bundle args = new Bundle();
        args.putSerializable("Charity",object);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        if (source!=null){
            if (source.equals("Client")){
                inflate = inflater.inflate(R.layout.fragment_donation_details_client, container, false);
            }else {
                inflate = inflater.inflate(R.layout.fragment_donation_details_business, container, false);

            }
        }

        dashboardClientView = (DashboardView) inflate.findViewById(R.id.dashboardview);
        TextView urlwebsite = (TextView) inflate.findViewById(R.id.urlwebsite);
        TextView address = (TextView) inflate.findViewById(R.id.address);
        TextView email = (TextView) inflate.findViewById(R.id.email);
        TextView description = (TextView) inflate.findViewById(R.id.description);
        ImageView logo = (ImageView) inflate.findViewById(R.id.logo);
        toolbar = (Toolbar) inflate.findViewById(R.id.toolbar);

        toolbar_title = (TextView) inflate.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Dons");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        charityDons= (CharityDons) getArguments().getSerializable("Charity");
        if (charityDons!=null){
            description.setText(charityDons.get_descipriton());

            if (charityDons.get_image()!=null){
                Bitmap PictureProfile = BitmapFactory.decodeByteArray(charityDons.get_image(), 0, charityDons.get_image().length);
                logo.setImageBitmap(PictureProfile);
            }
        }


        dashboardClientView.setSolde(sharepoint_solde_screenTransition);
        dashboardClientView.setCircularValue(sharepoint_solde_screenTransition,10);
        dashboardClientView.setGeneratedSharepoint(sharepoint_genarated_screenTransition);
        return inflate;
    }



    @Override
    public void update() {
        Log.d("clicli","update");

    }


    /*
     * Get local database to display nav drawer including Profil picture etc
     **/
}