package com.vagrant.android.vagrant.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.adapter.RescueAdapter;
import com.vagrant.android.vagrant.pojo.RescueStation;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Txm on 2017/7/12.
 */

public class RescueFragment extends Fragment{
    private List<RescueStation> rescueStationList = new ArrayList<RescueStation>();
    private RescueAdapter rescueAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rescue, container, false);
        if (rescueStationList.size() == 0){
            initRescue();
        }
        initView(view);
        return view;
    }
    private void initView(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_rescue);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.anim_recycler_view));
        rescueAdapter = new RescueAdapter(rescueStationList);
        recyclerView.setAdapter(rescueAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_rescue);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorSecendary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRescue();
            }
        });

    }
    private void initRescue(){
        rescueStationList.clear();
        getRescueFromBmob();

    }
    private void refreshRescue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initRescue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.anim_recycler_view));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    private void getRescueFromBmob(){
        BmobQuery<RescueStation> query = new BmobQuery<RescueStation>();
        query.setLimit(50);
        query.findObjects(new FindListener<RescueStation>() {
            @Override
            public void done(List<RescueStation> list, BmobException e) {
                if(e == null){
                    Log.e("查询成功:",String.valueOf(list.size()));
                    for (RescueStation rescueStation:list){
                        rescueStationList.add(rescueStation);
                    }
                    rescueAdapter.notifyDataSetChanged();
                }else {
                    Log.e("查询失败",e.getMessage());
                }
            }
        });
    }


}
