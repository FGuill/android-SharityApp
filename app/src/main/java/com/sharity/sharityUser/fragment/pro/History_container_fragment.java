package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fonts.TextViewRobotoThin;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.pagerHistoric.PagerFragment;

import org.w3c.dom.Text;

import static com.sharity.sharityUser.R.id.historic_status;
import static com.sharity.sharityUser.R.id.payment;


/**
 * Created by Moi on 14/11/15.
 */
public class History_container_fragment extends Fragment implements Updateable, PagerFragment.OnSelection,ProfilActivity.OnNotificationUpdateHistoric, View.OnClickListener {

    PagerFragment.OnSelection onSelection;
    Button buttonmap;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;
    private TextView  historic_status;
    private TextViewGeoManis payment;

    private TextView generated_sharepoint;
    private TextView stock_sharepoint;
    private TextView solde_euros;

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
            //Top Red View
            generated_sharepoint=(TextView)inflate.findViewById(R.id.generated_sharepoint);
            stock_sharepoint=(TextView)inflate.findViewById(R.id.stock_sharepoint);
            solde_euros=(TextView)inflate.findViewById(R.id.solde_euros);

            historic_status=(TextView)inflate.findViewById(R.id.historic_status);
            historic_status.setText("Historique des paiements");
            buttonmap.setOnClickListener(this);
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
}