package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Adapter_profil_Sharity_client_vertical;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.fragment.DashboardView;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.donation.Donation_container_fragment;

import java.util.ArrayList;
import java.util.List;

import static com.sharity.sharityUser.R.id.active_network;
import static com.sharity.sharityUser.R.id.animation_nonetwork;
import static com.sharity.sharityUser.R.id.animation_view;
import static com.sharity.sharityUser.R.id.container;
import static com.sharity.sharityUser.R.id.frame_nonetwork;
import static com.sharity.sharityUser.activity.ProfilActivity.db;
import static com.sharity.sharityUser.activity.ProfilActivity.parseUser;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.source;


/**
 * Created by Moi on 14/11/15.
 */
public class client_mission_fragment extends Fragment {


    private View inflate;
    private LottieAnimationView animationView;

    public static client_mission_fragment newInstance() {
        client_mission_fragment myFragment = new client_mission_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_mission_client, container, false);
        animationView=(LottieAnimationView)inflate.findViewById(R.id.animation_view);

        ShowAnimation();

        return inflate;
    }

    public void ShowAnimation(){

        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("loading.json");
        animationView.loop(true);
        animationView.playAnimation();
    }
}