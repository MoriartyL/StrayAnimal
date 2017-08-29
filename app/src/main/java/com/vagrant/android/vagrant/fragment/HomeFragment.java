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
    private List<Pet> petList = new ArrayList<Pet>();
    private PetAdapter petAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (petList.size() == 0) {
            initPets();
        }
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_pets);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_card_view));
        petAdapter = new PetAdapter(petList);
        recyclerView.setAdapter(petAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_pets);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorSecendary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPets();
            }
        });
    }

    private void initPets() {
        petList.clear();
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
                        //petAdapter.notifyDataSetChanged();
                        recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_card_view));
                        swipeRefreshLayout.setRefreshing(false);
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
                        petList.add(pet);
                    }
                    petAdapter.notifyDataSetChanged();
                } else {
                    Log.e("查询失败", e.getMessage());
                }
            }
        });

    }

}
