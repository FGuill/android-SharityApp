package com.sharity.sharityUser.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterDrawer;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.fragment.client.client_donation_fragment;
import com.sharity.sharityUser.fragment.donation.Donation_container_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Paiment_Confirmation_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Partenaire_fragment;

import java.util.ArrayList;

import static com.sharity.sharityUser.R.id.bottomBar;
import static com.sharity.sharityUser.R.id.toolbar_title;
import static com.sharity.sharityUser.activity.ProfilActivity.clientProfilActivity;
import static com.sharity.sharityUser.activity.ProfilActivity.permissionRuntime;
import static com.sharity.sharityUser.fragment.donation.Donation_container_fragment.source;


/**
 * Created by Moi on 07/05/2016.
 */
public class DonationActivity extends AppCompatActivity {

    @Override
public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

    if (savedInstanceState ==null){
        Intent intent = getIntent();
        Bundle bundle= intent.getBundleExtra("bundle");
        String source = bundle.getString("source");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,  Donation_container_fragment.newInstance(source), "Donation_container_fragment")
                .commit();
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
        SparseArray<FragmentManager> managers = new SparseArray<>();
        traverseManagers(getSupportFragmentManager(), managers, 0);
        if (managers.size() > 0) {
            managers.valueAt(managers.size() - 1).popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    private void traverseManagers(FragmentManager manager, SparseArray<FragmentManager> managers, int intent) {
        if (manager.getBackStackEntryCount() > 0) {
            managers.put(intent, manager);
        }
        if (manager.getFragments() == null) {
            return;
        }
        for (Fragment fragment : manager.getFragments()) {
            if (fragment != null)
                traverseManagers(fragment.getChildFragmentManager(), managers, intent + 1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}



