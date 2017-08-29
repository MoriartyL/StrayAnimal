package com.vagrant.android.vagrant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.activity.PetActivity;
import com.vagrant.android.vagrant.pojo.Pet;

import java.util.List;

/**
 * Created by Txm on 21/08/2017.
 */

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    private Context mContext;
    private List<Pet> mPetList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView petImage;
        TextView petName;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            petImage = (ImageView)itemView.findViewById(R.id.pet_image);
            petName = (TextView)itemView.findViewById(R.id.pet_name);
        }
    }

    public PetAdapter(List<Pet> mPetList) {
        this.mPetList = mPetList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.pet_items,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Pet pet = mPetList.get(position);
                Intent intent = new Intent(mContext, PetActivity.class);
                intent.putExtra(PetActivity.PET_ID,pet.getObjectId());
                intent.putExtra(PetActivity.PET_BREED,pet.getBreed());
                intent.putExtra(PetActivity.PET_AGE,pet.getAge());
                intent.putExtra(PetActivity.PET_GENDER,pet.getGender());
                intent.putExtra(PetActivity.PET_DESCRIPTION,pet.getDescription());
                intent.putExtra(PetActivity.PET_ORIGANIZATION,pet.getOrganization());
                intent.putExtra(PetActivity.PET_NAME,pet.getName());
                intent.putExtra(PetActivity.PET_IMAGE_ID,pet.getPetImage().getFileUrl());
                intent.putExtra(PetActivity.PET_CONTACT,pet.getContact());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pet pet = mPetList.get(position);
        holder.petName.setText(pet.getName());
        Glide.with(mContext).load(pet.getPetImage().getFileUrl()).into(holder.petImage);
    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }
}
