package com.vagrant.android.vagrant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.vagrant.android.vagrant.activity.ReportActivity;
import com.vagrant.android.vagrant.adapter.PetAdapter;
import com.vagrant.android.vagrant.pojo.Pet;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Txm on 2017/7/12.
 */

public class HomeFragment extends Fragment {
    private List<Pet> mPetList = new ArrayList<Pet>();
    private PetAdapter mPetAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    @Override
    public void onResume() {
        super.onResume();
        mFloatingActionButton.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFloatingActionButton.hide();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (mFloatingActionButton != null) {
//            if (isVisibleToUser) {
//                mFloatingActionButton.show();
//            } else {
//                mFloatingActionButton.hide();
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (mPetList.size() == 0) {
            initPets();
        }
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_pets);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_recycler_view));
        mPetAdapter = new PetAdapter(mPetList);
        mRecyclerView.setAdapter(mPetAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_pets);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorSecendary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPets();
            }
        });
        mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.pet_floating_action_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPets() {
        mPetList.clear();
        getPetFromBmob();
    }

    private void refreshPets() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initPets();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //mPetAdapter.notifyDataSetChanged();
                        mRecyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_recycler_view));
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void getPetFromBmob() {
        BmobQuery<Pet> query = new BmobQuery<Pet>();
        query.addWhereEqualTo("isAdopted", false);
        query.setLimit(50);
        query.findObjects(new FindListener<Pet>() {
            @Override
            public void done(List<Pet> list, BmobException e) {
                if (e == null) {
                    Log.e("查询成功:", String.valueOf(list.size()));
                    for (Pet pet : list) {
                        mPetList.add(pet);
                    }
                    mPetAdapter.notifyDataSetChanged();
                } else {
                    Log.e("查询失败", e.getMessage());
                }
            }
        });

    }

}
