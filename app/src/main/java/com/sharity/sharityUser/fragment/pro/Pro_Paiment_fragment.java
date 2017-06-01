package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.DonationActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.DashboardView;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

import static com.sharity.sharityUser.fragment.pro.History_container_fragment.Historic_swipeContainer;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_fragment extends Fragment implements Updateable,Pro_PaimentStepOne_fragment.OnChildPaymentSelection, ProfilProActivity.OnConfirmationPaiment, ProfilProActivity.ListenFromActivity, SwipeRefreshLayout.OnRefreshListener, DashboardView.OnDashBoardClick {
    private View inflate;
    private Pro_PaimentStepOne_fragment.OnChildPaymentSelection onSelection;
    private DashboardView dashboardClientView;
    private SwipeRefreshLayout swipeContainer;

    public static Pro_Paiment_fragment newInstance(String source) {
        Pro_Paiment_fragment myFragment = new Pro_Paiment_fragment();
        Bundle args = new Bundle();
        args.putString("source",source);

        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paiment_pro, container, false);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        dashboardClientView = (DashboardView) inflate.findViewById(R.id.dashboardview);
        dashboardClientView.setDashBoardClickListener(this);

        ((ProfilProActivity) getActivity()).setConfirmationListener(Pro_Paiment_fragment.this);
        ((ProfilProActivity) getActivity()).setActivityListener(Pro_Paiment_fragment.this);

        dashboardClientView.setBusiness_DashBoard();


        if (getArguments().getString("source").toString()!=null){
            if (getArguments().getString("source").equals("Paiment")){
                Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_container);
                if (currentFagment instanceof Pro_PaimentStepOne_fragment ){
                }else {
                    FragmentManager fm = getChildFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Pro_PaimentStepOne_fragment fragTwo = new Pro_PaimentStepOne_fragment();
                    ft.add(R.id.Fragment_container, fragTwo);
                    ft.commit();
                }
            }
        }

        return inflate;
    }

    @Override
    public void update() {
        dashboardClientView.setBusiness_DashBoard();
        Log.d("Update","UpdateProfilPro");
    }


    /*
 * GridView user is selected.
 * */
    @Override
    public void OnSelectGrid(UserLocation user, int i, CircleImageView imageView) {
        FragmentManager fm = getChildFragmentManager();
        Pro_Paiment_StepTwo_fragment fragmentB = Pro_Paiment_StepTwo_fragment.newInstance(user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationFadeFragment(getActivity(),fm,R.id.Fragment_container,fragmentB,"Pro_Paiment_StepTwo_fragment", Fade.IN,false);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,Pro_Paiment_StepTwo_fragment.newInstance(user),fm,"Pro_Paiment_StepTwo_fragment",true);
        }
    }



    /*
* Classique Paiment selected
* */
    @Override
    public void Classique() {
        FragmentManager fm = getChildFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationFadeFragment(getActivity(),fm,R.id.Fragment_container,new Pro_Paiment_StepTwo_Classique_fragment(),"Pro_Paiment_StepTwo_Classique_fragment", Fade.IN,true);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,new Pro_Paiment_StepTwo_Classique_fragment(),fm,"Pro_Paiment_StepTwo_Classique_fragment",true);
        }
    }


    /*
  * CallBack and function to display Paiment Confirmation
  *
  * */
    private void Display_Paiment_Confirmation(String montant, String clientName,boolean approved){
        FragmentManager fm = getChildFragmentManager();
        if (fm.getBackStackEntryCount()>0){
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,Pro_Paiment_Confirmation_fragment.newInstance(montant,clientName,approved),fm,"Display_Paiment_Confirmation",false);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,Pro_Paiment_Confirmation_fragment.newInstance(montant,clientName,approved),fm,"Display_Paiment_Confirmation",true);

        }
    }


    @Override
    public void TaskOnConfirmation(String amount, String clientName,boolean approved) {
        Display_Paiment_Confirmation(amount, clientName,approved);
    }



    /*
    * CallBack to display Navigation Drawer fragment when clicked
    *
    * */

    @Override
    public void doSomethingInFragment(String pos) {
        if (pos.equals("Finalize_inscription")){
            Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_container);
            if (currentFagment instanceof Pro_Profil_fragment ){
            }else {
                Pro_Profil_Ending_Inscription_fragment fragTwo = new Pro_Profil_Ending_Inscription_fragment();
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Fragment_container, fragTwo);
                ft.addToBackStack(null);
                ft.commit();
            }
        }

        if (pos.equals("Profilinfo")){
            Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_container);
            if (currentFagment instanceof Pro_Profil_fragment ){
                Log.d("fragment Profil","Pro_Profil_fragment visible");
            }else {
                Pro_Profil_Infos_fragment fragTwo = new Pro_Profil_Infos_fragment();
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Fragment_container, fragTwo);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    @Override
    public void onRefresh() {
        dashboardClientView.setBusiness_DashBoard();
        try {
            Pro_PaimentStepOne_fragment pro_paimentStepOne_fragment = (Pro_PaimentStepOne_fragment) getChildFragmentManager().findFragmentById(R.id.Fragment_container);
            if (pro_paimentStepOne_fragment!=null && pro_paimentStepOne_fragment.isVisible()){
                pro_paimentStepOne_fragment.onRefresh();
            }
            swipeContainer.setRefreshing(false);
        }catch (ClassCastException e){

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
