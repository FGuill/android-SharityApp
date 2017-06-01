package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.DonationActivity;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fragment.Profil_Solde_Callback;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.DashboardView;
import com.sharity.sharityUser.fragment.pagerHistoric.PagerFragment;


/**
 * Created by Moi on 14/11/15.
 */
public class History_container_fragment extends Fragment implements Profil_Solde_Callback, Updateable, PagerFragment.OnSelection,ProfilActivity.OnNotificationUpdateHistoric, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DashboardView.OnDashBoardClick {

    PagerFragment.OnSelection onSelection;
    Button buttonmap;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;
    private TextView  historic_status;
    private TextViewGeoManis payment;
    public static SwipeRefreshLayout Historic_swipeContainer;
    private TextView generated_sharepoint;
    private TextView stock_sharepoint;
    private TextView solde_euros;
    private CoordinatorLayout coordinatorLayout;
    private boolean isClient=false;
    private DatabaseHandler databaseHandler;
    private DashboardView dashboardClientView;
    public static History_container_fragment newInstance() {
        History_container_fragment myFragment = new History_container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() instanceof ProfilProActivity) {
            inflate = inflater.inflate(R.layout.fragment_history_container_pro, container, false);
            dashboardClientView = (DashboardView) inflate.findViewById(R.id.dashboardview);
            Historic_swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
            dashboardClientView.setBusiness_DashBoard();
            dashboardClientView.setDashBoardClickListener(this);
        }

        else if (getActivity() instanceof ProfilActivity){
            isClient=true;
            ((ProfilActivity) getActivity()).setHistoricListener(History_container_fragment.this);
            inflate = inflater.inflate(R.layout.fragment_history_container_client, container, false);
            Historic_swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
            payment=(TextViewGeoManis)inflate.findViewById(R.id.payment);
            dashboardClientView = (DashboardView) inflate.findViewById(R.id.dashboardview);
            dashboardClientView.setClient_DashBoard();

            //   TextSpawnTitle();
         }

        Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_container);
        if (currentFagment instanceof PagerFragment ){
        }else {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PagerFragment fragTwo = new PagerFragment();
            ft.add(R.id.Fragment_container, fragTwo,"PagerFragment");
            ft.commit();
        }

        historic_status = (TextView) inflate.findViewById(R.id.historic_status);
        historic_status.setText("Historique des paiements");
        Historic_swipeContainer.setOnRefreshListener(this);
        coordinatorLayout = (CoordinatorLayout) inflate.findViewById(R.id.coordinatorLayout);


        if (Utils.isConnected(getContext())){
           // getProfil();
        }else {

            Historic_swipeContainer.setRefreshing(false);
        }


        circle_slide1 = (ImageView) inflate.findViewById(R.id.circle_slide1);
        circle_slide2 = (ImageView) inflate.findViewById(R.id.circle_slide2);
        circle_slide1.setImageResource(R.drawable.circles_slide_on);
        circle_slide2.setImageResource(R.drawable.circles_slide_off);


        return inflate;
    }

    @Override
    public void update() {
         if (getActivity() instanceof ProfilActivity) {
             dashboardClientView.setClient_DashBoard();
         }
        else  if (getActivity() instanceof ProfilProActivity) {
             dashboardClientView.setBusiness_DashBoard();
         }
    }

    /*
 * CallBack when historic pager is selected
 * */
    @Override
    public void OnSelect(int i) {
        if (i == 0) {
            circle_slide1.setImageResource(R.drawable.circles_slide_on);
            circle_slide2.setImageResource(R.drawable.circles_slide_off);
            if (historic_status!=null){
                historic_status.setText("Historique des paiements");
            }
        } else {
            circle_slide1.setImageResource(R.drawable.circles_slide_off);
            circle_slide2.setImageResource(R.drawable.circles_slide_on);
            if (historic_status!=null){
                historic_status.setText("Historique des dons");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonmap:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
        }
    }

    /*
    * Refresh historic when notification received foreground. //Disabled, uncomment line 577 in ProfilActivity
    * */
    @Override
    public void TaskOnNotification(String business, String sharepoints) {
        PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentByTag("PagerFragment");
        fragment2.pager.setAdapter(fragment2.getAdapter());
        fragment2.getAdapter().FragmentOperation();
    }

    private void TextSpawnTitle(){
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String red = "1€ =";
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, red.length(), 0);
        builder.append(redSpannable);

        String white = " 1SP";
        SpannableString whiteSpannable= new SpannableString(white);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#75c33c")), 0, white.length(), 0);
        builder.append(whiteSpannable);
        payment.setText(builder, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void UpdateProfilSP() {

    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())){
         //   getProfil();
            PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentByTag("PagerFragment");
            fragment2.pager.setAdapter(fragment2.getAdapter());
            fragment2.getAdapter().FragmentOperation();
        }else {
            Snackbar.make(coordinatorLayout, "Activer votre réseau", Snackbar.LENGTH_LONG)
                    .setAction("PARAMETRES", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setDuration(10000)
                    .setActionTextColor(getResources().getColor(R.color.white))
                    .show();
            Historic_swipeContainer.setRefreshing(false);
        }
    }


     /*
    *  CallBack Dashboard Button allocated and get Sharepoint
    *
    * */

    @Override
    public void allouer() {
        Intent intent=new Intent(getActivity(),DonationActivity.class);
        intent.putExtra("source","Business") ;
        getActivity().startActivity(intent);
    }

    @Override
    public void retirer() {
        int solde = Integer.parseInt(dashboardClientView.getSolde());
        String allocate= getResources().getString(R.string.allocate);
        if (solde>=150){

        }else {
            Utils.showDialog3(getActivity(),allocate,"", true, new Utils.Click() {
                @Override
                public void Ok() {

                }

                @Override
                public void Cancel() {

                }
            });
        }
    }

}