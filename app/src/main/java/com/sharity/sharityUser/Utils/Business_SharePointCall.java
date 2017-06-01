package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.activity.ProfilProActivity;

import java.util.List;

import static com.sharity.sharityUser.Application.getContext;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;
import static com.sharity.sharityUser.fragment.pro.History_container_fragment.Historic_swipeContainer;

/**
 * Created by Moi on 21/11/15.
 */
public class Business_SharePointCall {

    //private variables
    private String _objectid;
    private int solde;
    private int sharepoint_depense;
    private int sharepoint_stock;
    private Context context;
    DatabaseHandler db;
    private OnDataCallBack dataCallBack;
    // Empty constructor

    public interface OnDataCallBack{
        public void data();

    }

    public Business_SharePointCall(Context context){
        this.context=context;
    }
    // constructor

    public void getPro_Sharepoints(final OnDataCallBack dataCallBack) {
        db= new DatabaseHandler(context);
        final String objectid = db.getBusinessId();
        final Business business = db.getBusiness(objectid);
        try {
            if (Utils.isConnected(getContext())) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                query.whereEqualTo("objectId", objectid);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            int solde =0;
                            int stock_SP = 0;
                            int generated_SP =0;

                            for (ParseObject object : objects) {
                                 solde = object.getInt("balance");
                                 generated_SP = object.getInt("generated_sharepoints");
                                 stock_SP = object.getInt("sharepoints_stock");
                            }

                            setSolde(solde);
                            setSharepoint_depense(generated_SP);
                            setSharepoint_stock(stock_SP);
                            db.setBusiness_DepenseSP(String.valueOf(generated_SP),business.get_id());
                            db.setBusinessSolde(String.valueOf(solde),business.get_id());
                            db.setBusiness_StockSP(String.valueOf(stock_SP),business.get_id());

                            //       Historic_swipeContainer.setRefreshing(false);
                            dataCallBack.data();
                        } else {

                        }

                    }
                });
                db.close();
            } else {

            }
        } catch (NullPointerException e) {

        }

    }

    public void setSharepoint_depense(int sharepoint_depense) {
        this.sharepoint_depense = sharepoint_depense;
    }

    public void setSharepoint_stock(int sharepoint_stock) {
        this.sharepoint_stock = sharepoint_stock;
    }

    public void setSolde(int solde) {
        this.solde = solde;
    }

    public int getSolde() {
        return solde;
    }

    public int getSharepoint_depense() {
        return sharepoint_depense;
    }

    public int getSharepoint_stock() {
        return sharepoint_stock;
    }
}
