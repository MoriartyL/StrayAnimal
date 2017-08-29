package com.vagrant.android.vagrant.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.adapter.FocusAdapter;
import com.vagrant.android.vagrant.pojo.Person;
import com.vagrant.android.vagrant.pojo.Pet;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FocusActivity extends AppCompatActivity {
    private List<Pet> petList = new ArrayList<Pet>();
    private FocusAdapter focusAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        final Person person = BmobUser.getCurrentUser(Person.class);
        if ((petList.size() == 0) && person != null) {
            initFocus(person);
        }
        initViews();
//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                Log.e("!!!!!","-----");
//                initViews((ArrayList<Pet>)msg.obj);
//            }
//        };
//        Log.e("petList1:",String.valueOf(petList.size()));

    }
    private void initViews(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view_focus);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_card_view));
        Log.e("PetList2:",String.valueOf(petList.size()));
        focusAdapter = new FocusAdapter(petList);
        recyclerView.setAdapter(focusAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_focus);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorSecendary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFoucus();
            }
        });
    }
    private void refreshFoucus(){
        final Person person = BmobUser.getCurrentUser(Person.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    initFocus(person);
                }catch (Exception e){
                    e.printStackTrace();
                }
                FocusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        }).start();
    }
    private void initFocus(Person bmobUser){
        petList.clear();
        getFocusFromBmob(bmobUser);

    }
    private void getFocusFromBmob(Person bmobUser){
        BmobQuery<Pet> query = new BmobQuery<Pet>();
        Log.e("UserId",bmobUser.getObjectId());
        query.setLimit(50);
        query.addWhereRelatedTo("focusedPets",new BmobPointer(bmobUser));
        query.findObjects(new FindListener<Pet>() {
            @Override
            public void done(List<Pet> list, BmobException e) {
                if(e == null){
                    Log.e("查询关注成功:",String.valueOf(list.size()));
                    for (Pet pet:list){
                        petList.add(pet);
                    }
                    Log.e("PetList3",String.valueOf(petList.size()));
                    focusAdapter.notifyDataSetChanged();

                }else {
                    Log.e("查询关注失败:",e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
