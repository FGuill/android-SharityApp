package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fonts.TextViewNotoSansRegular;
import com.sharity.sharityUser.fonts.TextViewR;
import com.sharity.sharityUser.fonts.TextViewRobotoThin;

import java.util.ArrayList;

import static com.sharity.sharityUser.Utils.JSONParser.is;


public class AdapterHistory extends BaseAdapter {

    Context context;
    LayoutInflater inflat;
    private ArrayList<History> items;


    public AdapterHistory(Context context, ArrayList<History> objects) {
        inflat = LayoutInflater.from(context);
        items = objects;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        History it = items.get(position);
        int listViewItemType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
              if(listViewItemType == 1){
                convertView = inflat.inflate(R.layout.row_payment_body_listview, null);
                holder.payment = (TextView) convertView
                        .findViewById(R.id.payment);
                holder.date_payment = (TextView) convertView
                        .findViewById(R.id.date);
                holder.price_payment=(TextViewGeoManis)convertView.findViewById(R.id.price);
                holder.approved=(TextView)convertView.findViewById(R.id.approved);
            }
             if(listViewItemType == 3){
                convertView = inflat.inflate(R.layout.row_dons_body_listview, null);
                holder.dons = (TextView) convertView
                        .findViewById(R.id.dons);
                holder.date_dons = (TextView) convertView
                        .findViewById(R.id.date_dons);
                holder.price_dons=(TextView)convertView.findViewById(R.id.price_dons);
                holder.approved=(TextView)convertView.findViewById(R.id.approved);

            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (it != null) {
              if(listViewItemType == 1) {
                if(holder.payment!= null) {
                    if (context instanceof ProfilProActivity){
                        holder.payment.setText(it.get_businessname());
                    }else if (context instanceof ProfilActivity){
                        holder.payment.setText(it.get_businessname());

                    }
                }
                if(holder.date_payment!= null) {
                    holder.date_payment.setText(it.get_date());
                }
                if(holder.price_payment!= null) {
                    holder.price_payment.setText(it.get_prix()+"€");
                    holder.price_payment.setTextColor(context.getResources().getColor(R.color.green));

                }

                if(holder.approved!= null) {
                    if (it.isApproved()){
                        holder.approved.setVisibility(View.INVISIBLE);
                        holder.approved.setText("accepté");
                    }else {
                        holder.approved.setVisibility(View.INVISIBLE);
                        holder.approved.setText("refusé");
                    }
                }
            }

             if(listViewItemType == 3) {
                if(holder.dons!= null) {
                    if (context instanceof ProfilProActivity){
                        holder.dons.setText(it.get_businessname());
                    }else if (context instanceof ProfilActivity){
                        holder.dons.setText(it.get_businessname());
                    }
                }
                if(holder.date_dons!= null) {
                    holder.date_dons.setText(it.get_date());
                }
                if(holder.price_dons!= null) {
                    holder.price_dons.setText(it.get_prix()+"SP");
                    holder.price_dons.setTextColor(context.getResources().getColor(R.color.green));

                }

                if(holder.approved!= null) {
                    if (it.isApproved()){
                        holder.approved.setVisibility(View.INVISIBLE);
                        holder.approved.setText("accepté");
                    }else {
                        holder.approved.setVisibility(View.INVISIBLE);
                        holder.approved.setText("refusé");
                    }
                }
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public History getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView payment;
        TextView date_payment;
        TextViewGeoManis price_payment;
        TextView dons,date_dons,price_dons,approved;

        ImageView logo;

        public String toString() {
            return "-";
        }
    }

    private String Status(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("status", "");         // getting String
        return accountDisconnect;
    }
}

