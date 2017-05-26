package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fonts.TextViewRobotoThin;
import com.sharity.sharityUser.fragment.Profil_Solde_Callback;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.pagerHistoric.PagerFragment;

import org.w3c.dom.Text;

import java.util.List;

import static com.sharity.sharityUser.R.id.RIB_value;
import static com.sharity.sharityUser.R.id.coordinatorLayout;
import static com.sharity.sharityUser.R.id.historic_status;
import static com.sharity.sharityUser.R.id.payment;
import static com.sharity.sharityUser.R.id.swipeContainer;
import static com.sharity.sharityUser.R.id.username;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class History_container_fragment extends Fragment implements Profil_Solde_Callback, Updateable, PagerFragment.OnSelection,ProfilActivity.OnNotificationUpdateHistoric, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

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
            buttonmap = (Button) inflate.findViewById(R.id.buttonmap);
            Historic_swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
            coordinatorLayout = (CoordinatorLayout) inflate.findViewById(R.id.coordinatorLayout);

            Historic_swipeContainer.setOnRefreshListener(this);
            //Top Red View
            generated_sharepoint=(TextView)inflate.findViewById(R.id.generated_sharepoint);
            stock_sharepoint=(TextView)inflate.findViewById(R.id.stock_sharepoint);
            solde_euros=(TextView)inflate.findViewById(R.id.solde_euros);

            historic_status=(TextView)inflate.findViewById(R.id.historic_status);
            historic_status.setText("Historique des paiements");
            buttonmap.setOnClickListener(this);

            if (Utils.isConnected(getContext())){
                getProfil();
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

        } else {
            ((ProfilActivity) getActivity()).setHistoricListener(History_container_fragment.this);
            inflate = inflater.inflate(R.layout.fragment_history_container_client, container, false);
            payment=(TextViewGeoManis)inflate.findViewById(R.id.payment);
            TextSpawnTitle();

        }
        circle_slide1 = (ImageView) inflate.findViewById(R.id.circle_slide1);
        circle_slide2 = (ImageView) inflate.findViewById(R.id.circle_slide2);
        circle_slide1.setImageResource(R.drawable.circles_slide_on);
        circle_slide2.setImageResource(R.drawable.circles_slide_off);

        return inflate;
    }

    @Override
    public void update() {
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
    * Refresh historic when notification received foreground.
    * */
    @Override
    public void TaskOnNotification(String business, String sharepoints) {
        PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
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
            getProfil();
            PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
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


    private void getProfil() {
        final String objectid = db.getBusinessId();
        Business business = db.getBusiness(objectid);
        try {
            if (Utils.isConnected(getContext())) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                query.whereEqualTo("objectId", objectid);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            int solde = 0;
                            int stockSP = 0;
                            int _generated_sharepoints = 0;

                            for (ParseObject object : objects) {
                                _generated_sharepoints = object.getInt("generated_sharepoints");
                            }

                            generated_sharepoint.setText(String.valueOf(_generated_sharepoints));
                            Historic_swipeContainer.setRefreshing(false);

                        } else {

                        }

                    }
                });

                Log.d("emailVerified", business.getEmailveried());
                db.close();
            } else {

            }
        } catch (NullPointerException e) {

        }

    }
}