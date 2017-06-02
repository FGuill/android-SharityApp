package com.sharity.sharityUser.fragment.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client_vertical;
import com.sharity.sharityUser.fragment.DashboardView;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.donation.Donation_container_fragment;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.source;


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
    private Donation_container_fragment parentContainer;

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
        parentContainer = ((Donation_container_fragment) getParentFragment());

        if (source!=null){
            if (source.equals("Client")){
                inflate = inflater.inflate(R.layout.fragment_donation_details_client, container, false);
            }else {
                inflate = inflater.inflate(R.layout.fragment_donation_details_business, container, false);

            }
        }

        parentContainer.dashboardClientView = (DashboardView) inflate.findViewById(R.id.dashboardview);
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
        if (charityDons.get_descipriton()!=null){
            description.setText(charityDons.get_descipriton());

            if (charityDons.getEmail()!=null) {
                email.setText(charityDons.getEmail());
            }
            if (charityDons.getUrl()!=null) {
                urlwebsite.setText(charityDons.getUrl());
            }
            if (charityDons.getAddresse()!=null) {
                address.setText(charityDons.getAddresse());
            }

            if (charityDons.get_image()!=null){
                Bitmap PictureProfile = BitmapFactory.decodeByteArray(charityDons.get_image(), 0, charityDons.get_image().length);
                logo.setImageBitmap(PictureProfile);
            }
        }

        parentContainer.dashboardClientView.setSolde(parentContainer.sharepoint_solde_screenTransition);
        parentContainer.dashboardClientView.setCircularValue(parentContainer.sharepoint_solde_screenTransition,10);
        parentContainer.dashboardClientView.setGeneratedSharepoint(parentContainer.sharepoint_genarated_screenTransition);
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