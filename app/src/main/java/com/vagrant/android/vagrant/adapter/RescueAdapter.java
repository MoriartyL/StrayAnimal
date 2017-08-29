package com.vagrant.android.vagrant.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.RescueStation;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Txm on 28/08/2017.
 */

public class RescueAdapter extends RecyclerView.Adapter<RescueAdapter.ViewHolder>{
    private Context mContext;
    private List<RescueStation> mRescueList;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CircleImageView rescueImage;
        TextView rescueName;
        TextView rescueAddress;
        TextView rescueContact;
        TextView rescueDescription;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            rescueImage = (CircleImageView)view.findViewById(R.id.rescue_image);
            rescueName = (TextView)view.findViewById(R.id.rescue_name);
            rescueAddress = (TextView)view.findViewById(R.id.rescue_address);
            rescueContact = (TextView)view.findViewById(R.id.rescue_contact);
            rescueDescription = (TextView)view.findViewById(R.id.rescue_description);
        }

    }
    public RescueAdapter(List<RescueStation> list){
        mRescueList = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.rescue_items,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RescueStation rescueStation = mRescueList.get(position);
        holder.rescueName.setText(rescueStation.getName());
        holder.rescueAddress.setText(rescueStation.getAddress());
        holder.rescueDescription.setText("\b\b\b\b\b\b\b\b"+rescueStation.getDescription());
        holder.rescueContact.setText(rescueStation.getContact());
        Glide.with(mContext).load(rescueStation.getImage().getFileUrl()).into(holder.rescueImage);
    }

    @Override
    public int getItemCount() {
        return mRescueList.size();
    }
}
