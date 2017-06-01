package com.sharity.sharityUser.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Business_SharePointCall;
import com.sharity.sharityUser.Utils.Client_SharePointCall;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.DonationActivity;
import com.sharity.sharityUser.activity.ProfilActivity;

import static com.sharity.sharityUser.R.id.retirer;
import static com.sharity.sharityUser.R.id.stock_sharepoint;

public class DashboardView extends LinearLayout {

    private View rootView;
    private TextView generated_sharepoint;
    private TextView stock_SP;
    private TextView solde_euros;

    private int mGenerated_sharepoint;
    private int mStock_sharepoint;
    private int msolde_euros;
    private String type;
    public CircularProgressBar circularProgressBar;
    public Business_SharePointCall business_call;
    public Client_SharePointCall client_call;
    private Context context;
    private OnDashBoardClick onDashBoardClick;
    private Button retirer;
    private Button allouer;

    public interface OnDashBoardClick {
        public void allouer();

        public void retirer();
    }

    public DashboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.DashboardView);

        type = a.getString(
                R.styleable.DashboardView_Type);
        init(context);
    }


    //Type: Business or Client
    public DashboardView(Context context, String type) {
        super(context);
        this.context = context;
        this.type = type;
        init(context);
    }

    private void init(Context context) {
        business_call = new Business_SharePointCall(context);
        client_call = new Client_SharePointCall(context);

        if (type.equals("Business")) {
            rootView = inflate(context, R.layout.view_dashboard_pro, this);
            retirer = (Button) rootView.findViewById(R.id.retirer);
            allouer = (Button) rootView.findViewById(R.id.allouer);

            allouer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDashBoardClick != null) {
                        onDashBoardClick.allouer();
                    }
                }
            });

            retirer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDashBoardClick != null) {
                        onDashBoardClick.retirer();
                    }

                }
            });

        } else if (type.equals("Client")) {
            rootView = inflate(context, R.layout.view_dashboard_client, this);
        }

        stock_SP = (TextView) rootView.findViewById(stock_sharepoint);
        solde_euros = (TextView) rootView.findViewById(R.id.solde_euros);
        generated_sharepoint = (TextView) rootView.findViewById(R.id.generated_sharepoint);
        circularProgressBar = (CircularProgressBar) rootView.findViewById(R.id.circularprogress);
    }


    /**
     * Call to set Business Dashboard, or Client Dashboard
     */

    public void setBusiness_DashBoard() {
        if (Utils.isConnected(context)) {
            if (business_call != null) {
                business_call.getPro_Sharepoints(new Business_SharePointCall.OnDataCallBack() {
                    @Override
                    public void data() {
                        setGeneratedSharepoint(business_call.getSharepoint_depense());
                        setCircularValue(business_call.getSolde(), 1500);
                        setSolde(business_call.getSolde());
                        setStokSharepoint(business_call.getSharepoint_stock()); //stock
                    }
                });
            }
        } else {
            //Show Dasboard
            DatabaseHandler db = new DatabaseHandler(context);
            final String objectid = db.getBusinessId();
            Business business = db.getBusiness(objectid);
            Log.d("shareDB", business.getSolde());
            setCircularValue(Integer.parseInt(business.getSolde()), 10);
            setSolde(Integer.parseInt(business.getSolde()));//solde
            setGeneratedSharepoint(Integer.parseInt(business.getSharepoint_depense())); //generated
            setStokSharepoint(Integer.parseInt(business.getSharepoint_stock())); //stock
            db.close();
        }
    }


    public void setClient_DashBoard() {
        if (Utils.isConnected(context)) {
            if (client_call != null) {
                client_call.getUserSharePoints(new Client_SharePointCall.OnDataCallBack() {
                    @Override
                    public void data() {
                        setGeneratedSharepoint(client_call.getSharepoint_depense());
                        setCircularValue(client_call.getSolde(), 1500);
                        setSolde(client_call.getSolde());
                    }
                });
            }
        } else {
            //Show Dasboard
            DatabaseHandler db = new DatabaseHandler(context);
            User user = db.getUser(ProfilActivity.parseUser.getObjectId());
            Log.d("shareDB", user.get_sharepoint());
            setCircularValue(Integer.parseInt(user.get_sharepoint()), 10);
            setSolde(Integer.parseInt(user.get_sharepoint()));
            setGeneratedSharepoint(Integer.parseInt(user.get_sharepoint_depense()));
            db.close();
        }
    }

    /*
    *  Getter Setter
    * */
    public String getGeneratedSharepoint() {
        return generated_sharepoint.getText().toString();
    }

    public void setGeneratedSharepoint(int mGenerated_sharepoint) {
        this.mGenerated_sharepoint = mGenerated_sharepoint;
        generated_sharepoint.setText(String.valueOf(mGenerated_sharepoint));
    }

    public String getStockSharepoint() {
        return String.valueOf(mStock_sharepoint);
    }

    public void setStokSharepoint(int stock_sharepoint) {
        this.mStock_sharepoint = stock_sharepoint;
        stock_SP.setText(String.valueOf(mStock_sharepoint));
    }


    public String getSolde() {
        return String.valueOf(msolde_euros);
    }

    public void setSolde(int stock_sharepoint) {
        this.msolde_euros = stock_sharepoint;
        if (type.equals("Business")) {
            solde_euros.setText(String.valueOf(msolde_euros) + "€");

        } else if (type.equals("Client")) {
            solde_euros.setText(String.valueOf(msolde_euros));
        }
    }

    public void setCircularValue(int currentUserSP, int animationDurationMS) {
        //  int animationDuration = 3000; // 3000ms = 3s
        circularProgressBar.setProgress(0);
        if (currentUserSP <= 100) {
            circularProgressBar.setProgressWithAnimation(currentUserSP / 1, animationDurationMS);
        } else if (currentUserSP > 100 && currentUserSP <= 1000) {
            circularProgressBar.setProgressWithAnimation(currentUserSP / 10, animationDurationMS);
        } else if (currentUserSP > 1000 && currentUserSP < 10000) {
            circularProgressBar.setProgressWithAnimation(currentUserSP / 100, animationDurationMS);
        }
        circularProgressBar.invalidate();
    }

    public void setCircularProgressBar(float value) {
        circularProgressBar.setProgress(value);
    }


    public void setDashBoardClickListener(OnDashBoardClick click) {
        this.onDashBoardClick = click;
    }
}