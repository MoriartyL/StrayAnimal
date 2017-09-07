package com.vagrant.android.vagrant.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.adapter.CommunicationAdapter;
import com.vagrant.android.vagrant.pojo.Post;
import com.vagrant.android.vagrant.pojo.RescueStation;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Txm on 2017/7/12.
 */

public class CommunicationFragment extends Fragment {
    private List<Post> mPostList = new ArrayList<Post>();
    private List<RescueStation> mPublishers = new ArrayList<RescueStation>();
    private RecyclerView mRecyclerView;
    private CommunicationAdapter mCommunicationAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        getPublisherInfo();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_communication);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.anim_recycler_view));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mCommunicationAdapter = new CommunicationAdapter(mPostList);
        mRecyclerView.setAdapter(mCommunicationAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_communication);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorSecendary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });

    }
    private void refreshPosts(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getPostsFromBmob();
                }catch (Exception e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.anim_recycler_view));
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();

    }

    private void getPostsFromBmob() {
        if(mPostList.size() != 0){
            mPostList.clear();
        }
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.setLimit(50);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    Log.e("查询成功(getPostsFromBmob):", String.valueOf(list.size()));
                    for (Post post : list) {
                        for (RescueStation rescueStation:mPublishers){
                            if (post.getPublisher().getObjectId().equals(rescueStation.getObjectId())){
                                post.setPublisherName(rescueStation.getName());
                                post.setPublisherImage(rescueStation.getImage());
                            }
                        }
                        mPostList.add(post);
                    }
                    mCommunicationAdapter.notifyDataSetChanged();
                } else {
                    Log.e("查询失败(getPostsFromBmob):", e.getMessage());
                }
            }
        });

    }
    private void getPublisherInfo() {
        mPublishers.clear();
        BmobQuery<RescueStation> query = new BmobQuery<RescueStation>();
        query.setLimit(50);
        query.findObjects(new FindListener<RescueStation>() {
            @Override
            public void done(List<RescueStation> list, BmobException e) {
                if (e == null) {
                    Log.e("查询成功(getPublisherInfo):", String.valueOf(list.size()));
                    for (RescueStation rescueStation : list) {
                        mPublishers.add(rescueStation);
                    }
                } else {
                    Log.e("查询失败(getPublisherInfo)", e.getMessage());
                }
            }
        });
        getPostsFromBmob();
    }
}
