package com.sharity.sharityUser.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseUser;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.donation.Donation_container_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Paiment_Confirmation_fragment;

import java.util.ArrayList;


/**
 * Created by Moi on 07/05/2016.
 */
public class PaimementActivity extends AppCompatActivity {

    public LinearLayout background;
    @Override
public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement);

    if (savedInstanceState ==null){
        background=(LinearLayout)findViewById(R.id.background);

        Intent intent = getIntent();
        String montant = intent.getStringExtra("montant");
        String clientName = intent.getStringExtra("clientName");
        boolean approved = intent.getBooleanExtra("approved",false);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,  Pro_Paiment_Confirmation_fragment.newInstance(montant,clientName,approved), "Pro_Paiment_Confirmation_fragment")
                .addToBackStack(null)
                .commit();


        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }



    @Override
    public void onPause() {
        super.onPause();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}



