package com.sharity.sharityUser.fragment.donation;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fragment.DashboardView;
import com.sharity.sharityUser.fragment.client.client_donation_details_fragment;
import com.sharity.sharityUser.fragment.client.client_donation_fragment;

import static com.sharity.sharityUser.activity.DonationActivity.db;
import static com.sharity.sharityUser.activity.DonationActivity.parseUser;


/**
 * Created by Moi on 14/11/15.
 */
public class Donation_container_fragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,client_donation_fragment.OnCharitySelected {

    protected client_donation_fragment.OnCharitySelected onCharitySelected;
    protected View inflate;
    protected boolean isClient=false;
    public static TextView sharepoints_moins;
    public static TextView sharepoints_plus;
    public static TextViewGeoManis points;
    public static TextView do_donationTV;
    public static int sharepoints_user_donate =0;
    public static int user_solde=0;
    public static int user_sharepoint_expense=0;
    public static int user_sharepoint_stock=0;

    public static DashboardView dashboardClientView;
    public static boolean donation=false;

    public static int recycler_position = -1;
    public static  int sharepoints_user_temp;
    public static  int sharepoints_user_depense;

    //Field donation to charity
    public static String CharityName;
    public static String CharityId;

    public static  LinearLayout dons_view;
    public static  Handler UpdateHandler = new Handler();
    public static  boolean mAutoIncrement = false;
    public static  boolean mAutoDecrement = false;
    public static  int REP_DELAY=300;
    public static  boolean isLongClick=false;

    public static int sharepoint_solde_screenTransition=0;
    public static int sharepoint_genarated_screenTransition=0;

   public static String source=null;
    public static Donation_container_fragment newInstance(String source) {
        Donation_container_fragment myFragment = new Donation_container_fragment();
        Bundle args = new Bundle();
        args.putString("source",source);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        inflate = inflater.inflate(R.layout.fragment_donation_container, container, false);

        do_donationTV = (TextView) inflate.findViewById(R.id.do_donationTV);
        points = (TextViewGeoManis) inflate.findViewById(R.id.points);
        points.setText(String.valueOf(sharepoints_user_donate));
        sharepoints_moins = (TextView) inflate.findViewById(R.id.sharepoints_moins);
        sharepoints_plus = (TextView) inflate.findViewById(R.id.sharepoints_plus);

        source=getArguments().getString("source");

        do_donationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recycler_position >= 0) {
                    if (CharityId != null) {
                        if (sharepoints_user_donate > 0) {
                            if (Utils.isConnected(getActivity())) {
                                CreateTransaction(CharityName, CharityId, String.valueOf(sharepoints_user_donate));
                            }else {
                                Toast.makeText(getActivity(), "Veuillez activer votre réseau WIFI ou réseau", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Veuillez envoyer une valeur supérieur à 0", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Veuillez séléctionner une charité", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Veuillez séléctionner une charité", Toast.LENGTH_LONG).show();
                }
            }
        });


        sharepoints_moins.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mAutoDecrement = true;
                        isLongClick=true;
                        UpdateHandler.post( new Donation_container_fragment.RptUpdater() );
                        return false;
                    }
                }
        );

        sharepoints_moins.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_DOWN)
                        ){
                    if (!isLongClick){
                        isLongClick=false;
                        mAutoDecrement = false;
                        decrement();
                    }else {
                        mAutoDecrement = true;
                    }
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    mAutoDecrement = false;
                }
                return false;
            }
        });

        sharepoints_plus.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mAutoIncrement = true;
                        isLongClick=true;
                        UpdateHandler.post( new Donation_container_fragment.RptUpdater() );
                        return false;
                    }
                }
        );

        sharepoints_plus.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_DOWN)
                        ){
                    if (!isLongClick){
                        isLongClick=false;
                        mAutoIncrement = false;
                        increment();
                    }else {
                        mAutoIncrement = true;
                    }
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    mAutoIncrement = false;
                    isLongClick=false;
                }
                return false;
            }
        });

        if (getArguments().getString("source").toString()!=null){
                Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_container);
                if (currentFagment instanceof client_donation_fragment){
                }else {
                    FragmentManager fm = getChildFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    client_donation_fragment fragTwo = new client_donation_fragment();
                    ft.add(R.id.Fragment_container, fragTwo,"client_donation_fragment");
                    ft.commit();
                }
        }

        return inflate;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void OnSelected(CharityDons user, int i) {
        FragmentManager fm = getChildFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationFadeFragment(getActivity(),fm,R.id.Fragment_container, client_donation_details_fragment.newInstance(user),"client_donation_details_fragment", Fade.OUT,true);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,client_donation_details_fragment.newInstance(user),fm,"client_donation_details_fragment",true);
        }
    }




    class RptUpdater implements Runnable {
        public void run() {
            if( mAutoIncrement ){
                increment();
                UpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
            } else if( mAutoDecrement ){
                decrement();
                UpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
            }
        }
    }

    public void decrement(){
        if (user_solde > 0){
            if (sharepoints_user_donate>0) {
                sharepoints_user_donate = sharepoints_user_donate - 10;

                if (source.equals("Client")){
                    dashboardClientView.setSolde(Integer.parseInt(dashboardClientView.getSolde()) + 10);
                    dashboardClientView.setCircularValue(Integer.parseInt(dashboardClientView.getSolde()),1);
                }

                sharepoint_solde_screenTransition=Integer.parseInt(dashboardClientView.getSolde());
                dashboardClientView.setGeneratedSharepoint(Integer.parseInt(dashboardClientView.getGeneratedSharepoint()) - 10);
                sharepoint_genarated_screenTransition=Integer.parseInt(dashboardClientView.getGeneratedSharepoint());

                sharepoints_plus.setVisibility(View.VISIBLE);
            } else {
                sharepoints_user_donate = 0;
                sharepoints_moins.setVisibility(View.INVISIBLE);
            }
        }else {
            Toast.makeText(getActivity(),"Vosu n'avez pas passez de Sharepoints pour efféctuer le don",Toast.LENGTH_LONG);
            sharepoints_user_donate = 0;
            sharepoints_moins.setVisibility(View.VISIBLE);
        }

        points.setText(String.valueOf(sharepoints_user_donate));
    }


    public void increment(){

        if (user_solde>0) {
            sharepoints_user_donate = sharepoints_user_donate + 10;
            if (sharepoints_user_donate <= user_solde) {

                if (source.equals("Client")){
                    dashboardClientView.setSolde(Integer.parseInt(dashboardClientView.getSolde()) - 10);
                    dashboardClientView.setCircularValue(Integer.parseInt(dashboardClientView.getSolde()),1);
                }
                dashboardClientView.setGeneratedSharepoint(Integer.parseInt(dashboardClientView.getGeneratedSharepoint()) + 10);
                sharepoint_solde_screenTransition=Integer.parseInt(dashboardClientView.getSolde());
                sharepoint_genarated_screenTransition=Integer.parseInt(dashboardClientView.getGeneratedSharepoint());

            }

            if (sharepoints_user_donate >= user_solde) {
                sharepoints_user_donate = user_solde;
                sharepoints_plus.setVisibility(View.INVISIBLE);
            }
            sharepoints_moins.setVisibility(View.VISIBLE);
            points.setText(String.valueOf(sharepoints_user_donate));
        }else {
            Toast.makeText(getActivity(),"Vous n'avez pas passez de Sharepoints pour efféctuer le don",Toast.LENGTH_LONG);
        }
    }



    /*
    *
    *
    * */

    private void CreateTransaction(final String charityName, String charityId, final String price) {
        final Number num = Integer.parseInt(price);
        ParseObject object =  ParseObject.create("Transaction");
        object.put("sender_name", parseUser.getUsername());
        object.put("clientDonator", ParseObject.createWithoutData("_User", parseUser.getObjectId()));
        object.put("recipient_name", charityName);
        object.put("sharepoints", num);
        object.put("amount", Integer.parseInt(price)*100);
        object.put("status", 2);
        object.put("transactionType", 2);
        object.put("currency_code", "EUR");
        object.put("charity", ParseObject.createWithoutData("Charity", charityId));

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    final Number num = sharepoints_user_temp - Integer.parseInt(price);
                    UpdateUserSharepoints(num,charityName);
                    //   ShowSuccessDonation();
                } else {
                    Log.d("Transaction", "ex" + e.getMessage());
                    PopupStateDonation(false);
                }
            }
        });
    }

    public void UpdateUserSharepoints(final Number sharepoints, final String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {

                    gameScore.put("sharepoints", Integer.parseInt(dashboardClientView.getSolde()));
                    gameScore.put("sharepoints_depense",Integer.parseInt(dashboardClientView.getGeneratedSharepoint()));
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                UpdateCharitySharepoints();
                            } else {
                                PopupStateDonation(false);
                                Log.d("okok", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    public void UpdateCharitySharepoints() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Charity");
        query.getInBackground(CharityId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {

                    int currentsharepoints = gameScore.getInt("sharepoints");
                    final Number SP = currentsharepoints + sharepoints_user_donate;
                    gameScore.put("sharepoints", SP);
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                sharepoints_user_temp=Integer.parseInt(dashboardClientView.getSolde());
                                sharepoints_user_depense=Integer.parseInt(dashboardClientView.getGeneratedSharepoint());
                                user_solde=sharepoints_user_temp;

                                db.UpdateUserSharepoints_depense(String.valueOf(sharepoints_user_depense),parseUser.getObjectId());
                                db.UpdateUserSharepoints(String.valueOf(user_solde),parseUser.getObjectId());
                                sharepoints_user_donate=0;
                                recycler_position=-1;
                                points.setText(String.valueOf(sharepoints_user_donate));
                                Log.d("UpdateCharitySharepoint", "success");
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result","success");
                                getActivity().setResult(Activity.RESULT_OK,returnIntent);
                                getActivity().finish();
//                                PopupStateDonation(true);
                            } else {
                                PopupStateDonation(false);
                            }
                        }
                    });
                }
            }
        });
    }




    private void PopupStateDonation(boolean success){
      String message;
        if (success){
            message=getResources().getString(R.string.paiement_send);
        }else {
            message=getResources().getString(R.string.paiement_refused);
        }
        Utils.showDialogPaiement(getActivity(),message,success, true, new Utils.Click() {
            @Override
            public void Ok() {

            }

            @Override
            public void Cancel() {

            }
        });
    }

    @Override
    public void onDestroy(){
        sharepoint_solde_screenTransition=0;
        sharepoint_genarated_screenTransition=0;
        sharepoints_user_donate=0;
        user_solde=0;
        user_sharepoint_expense=0;
        recycler_position = -1;
        sharepoints_user_temp=0;
        sharepoints_user_depense=0;
        user_sharepoint_stock=0;

        super.onDestroy();
    }
}