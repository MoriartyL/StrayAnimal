package com.vagrant.android.vagrant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.activity.PetActivity;
import com.vagrant.android.vagrant.pojo.Pet;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Txm on 23/08/2017.
 */

public class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.ViewHolder> {
    private Context mContext;
    private List<Pet> mPetList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CircleImageView petImage;
        TextView petName;
        TextView petOrganization;
        TextView isAdopted;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            petImage = (CircleImageView) view.findViewById(R.id.focus_image);
            petName = (TextView) view.findViewById(R.id.pet_name);
            petOrganization = (TextView) view.findViewById(R.id.pet_organization);
            isAdopted = (TextView) view.findViewById(R.id.isAdopted_text);
        }
    }
    public FocusAdapter(List<Pet> list){
        mPetList = list;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.focus_items,parent,false);
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
        Log.e("关注数",String.valueOf(position));
        Pet pet = mPetList.get(position);
        holder.petName.setText(pet.getName());
        holder.petOrganization.setText(pet.getOrganization());
        if (pet.getAdopted()) {
            holder.isAdopted.setText("已领养");
            holder.isAdopted.setCompoundDrawables(null,null,mContext.getDrawable(R.drawable.ic_star_pink_500_24dp),null);
        }else {
            holder.isAdopted.setText("未领养");
            holder.isAdopted.setCompoundDrawables(null,null,mContext.getDrawable(R.drawable.ic_star_border_pink_500_24dp),null);
        }
        Glide.with(mContext).load(pet.getPetImage().getFileUrl()).into(holder.petImage);
    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }
}
