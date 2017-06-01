package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.activity.ProfilActivity;

import java.util.List;

import static com.sharity.sharityUser.Application.getContext;
import static com.sharity.sharityUser.R.id.swipeContainer;
import static com.sharity.sharityUser.activity.ProfilActivity.db;

/**
 * Created by Moi on 21/11/15.
 */
public class Client_SharePointCall {

    //private variables
    private String _objectid;
    private int solde;
    private int sharepoint_depense;
    private int sharepoint_stock;
    private Context context;
    DatabaseHandler db;
    // Empty constructor

    public interface OnDataCallBack{
        public void data();

    }

    public Client_SharePointCall(Context context){
        this.context=context;
        db= new DatabaseHandler(context);
    }
    // constructor

    /*
    * Get local database to display nav drawer including Profil picture etc
    **/
    public void getUserSharePoints(final OnDataCallBack dataCallBack) {
        try {
            final User user=db.getUser(db.getUserId());
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", user.get_id());
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> commentList, ParseException e) {
                    if (commentList != null) {
                        int solde =0;
                        int generated_SP =0;

                        for (ParseObject object : commentList) {
                            solde = object.getInt("sharepoints");
                            generated_SP = object.getInt("sharepoints_depense");
                        }

                        setSolde(solde);
                        setSharepoint_depense(generated_SP);
                        db.UpdateUserSharepoints_depense(String.valueOf(generated_SP),user.get_id());
                        db.UpdateUserSharepoints(String.valueOf(solde),user.get_id());
                        dataCallBack.data();
                    }
                }
            });
        } catch (NullPointerException f) {

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
