package com.vagrant.android.vagrant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Post;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Txm on 31/08/2017.
 */

public class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.ViewHolder>{
    public static final String DATEFORMAT = "yyyy-MM-dd";
    public static final String NOTEWORDS = "活动时间:";
    public static final String BLANK = "\b\b\b\b\b\b";
    private Context context;
    private List<Post> postList;

    public CommunicationAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.communication_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = postList.get(position);
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        String content = setContent(post.getContent(),post.getStartTime(),post.getEndTime());
        holder.expandableTextView.setText(content,sparseBooleanArray,position);
        holder.rescueNameTextView.setText(post.getPublisherName());
        holder.createdTimeTextView.setText(post.getCreatedAt());
        if (post.getPublisherImage() != null) {
            Glide.with(context).load(post.getPublisherImage().getFileUrl()).into(holder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ExpandableTextView expandableTextView;
        CircleImageView circleImageView;
        TextView rescueNameTextView;
        TextView createdTimeTextView;
        public ViewHolder(View view) {
            super(view);
            expandableTextView = (ExpandableTextView)view;
            circleImageView = (CircleImageView)view.findViewById(R.id.rescue_image);
            rescueNameTextView = (TextView)view.findViewById(R.id.rescue_name);
            createdTimeTextView = (TextView)view.findViewById(R.id.created_date);

        }
    }
    private String setContent(String content, BmobDate startDate, BmobDate endDate){
        String newContent = content;
        if(content.length() != 0){
            String date = getDate(startDate,endDate);
            newContent = BLANK + content + "\n" + BLANK + NOTEWORDS + date;
        }
        return newContent;
    }
    private String getDate(BmobDate startDate, BmobDate endDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        StringBuilder date = new StringBuilder();
        try {
            String formatStartDate = dateFormat.format(dateFormat.parse(startDate.getDate()));
            String formatEndDate = dateFormat.format(dateFormat.parse(endDate.getDate()));
            date.append(formatStartDate);
            date.append("\b至\b");
            date.append(formatEndDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date.toString();
    }


}
