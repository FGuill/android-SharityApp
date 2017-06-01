package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.DonationActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;

import java.util.ArrayList;



public class Adapter_profil_Sharity_client extends RecyclerView.Adapter<Adapter_profil_Sharity_client.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos=0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<CharityDons> AL_id_text = new ArrayList<CharityDons>();
    Context mContext;

    public interface OnItemDonateClickListener extends Adapter_profil_Sharity_client_vertical.OnItemDonateClickListener {
        void onItemClick(int item,CharityDons bo);
    }

    public Adapter_profil_Sharity_client(Context c, ArrayList<CharityDons> AL_id_text, OnItemDonateClickListener listener) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
        this.AL_id_text = AL_id_text;
        this.listener = listener;
    }

    public int getCount() {
        return AL_id_text.size();
    }

    public Object getItem(int position) {
        return AL_id_text.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    @Override
    public Adapter_profil_Sharity_client.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_sharity_h, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return AL_id_text.size();
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final CharityDons bo = (CharityDons) getItem(position);
        viewHolder.bind(position,listener,bo);
        viewHolder.image.setTag(position);

        if(selectedPos == position){
            viewHolder.background.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.background.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_green));
            } else {
                viewHolder.background.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_green));
            }
        }else{

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.background.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_grey));
            } else {
                viewHolder.background.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_grey));
            }
        }

        if (bo.get_descipriton()!=null) {
            viewHolder.description.setText(bo.get_descipriton());
        }

        if (bo.get_nom()!=null) {
            viewHolder.name.setText(bo.get_nom());
        }

        if (bo.get_image()!=null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.image.setImageBitmap(PictureProfile);
        }else {
            viewHolder.image.setImageResource(R.drawable.icon_logo);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        LinearLayout background;
        TextView name;
        TextView description;


        public ViewHolder(View itemView) {
            super(itemView);
            background = (LinearLayout) itemView.findViewById(R.id.background);

            if (mContext instanceof ProfilActivity){
                background.getLayoutParams().width = (int) (Utils.getScreenWidth(itemView.getContext()) / 1.5);
            }
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.imageView);

        }


        public void bind(final int item, final OnItemDonateClickListener listener, final CharityDons bo) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, bo);
                    notifyItemChanged(selectedPos);
                    selectedPos = item;
                    notifyItemChanged(selectedPos);
                }
            });
        }
    }
}