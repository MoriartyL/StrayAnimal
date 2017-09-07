package com.vagrant.android.vagrant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.fragment.CommunicationFragment;
import com.vagrant.android.vagrant.fragment.HomeFragment;
import com.vagrant.android.vagrant.fragment.RescueFragment;
import com.vagrant.android.vagrant.pojo.Person;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    //Tab 文字
    private final int[] TAB_TITLES = new int[]{R.string.tab_title_first,R.string.tab_title_second,R.string.tab_title_third,};
    //Tab 图片
    private final int[] TAB_IMGS = new int[]{R.drawable.ic_favorite_blue_500_24dp,R.drawable.ic_home_blue_500_24dp,R.drawable.ic_question_answer_blue_500_24dp};
    //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[] {new HomeFragment(),new RescueFragment(),new CommunicationFragment()};
    //Tab 数目
    private final int COUNT = TAB_TITLES.length;
    private MyViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    //private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Person bmobUser = BmobUser.getCurrentUser(Person.class);
        initViews();
        if (bmobUser != null) {
            setUserInfo(bmobUser);
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Person bmobUser = BmobUser.getCurrentUser(Person.class);
        if(bmobUser != null){
            setUserInfo(bmobUser);
        }
    }
    private void setUserInfo(Person bmobUser){
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        CircleImageView ciFace = (CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.faces);
        TextView tvUsername = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        TextView tvEmail = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        BmobFile face = bmobUser.getFace();
        if(face != null){
            String faceUri = face.getFileUrl();
            Glide.with(MainActivity.this).load(faceUri).into(ciFace);
        }
        String username = bmobUser.getUsername();
        String email = bmobUser.getEmail();
        tvUsername.setText(username);
        tvEmail.setText(email);
    }
    private void initViews() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.nav_myInfo:
                        intent = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_myFocus:
                        intent = new Intent(MainActivity.this, FocusActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_setting:
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mTabLayout = (TabLayout)findViewById(R.id.tablayout);
        setTabs(mTabLayout,this.getLayoutInflater(),TAB_TITLES,TAB_IMGS);
        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 0){
//                    mFloatingActionButton.show();
//                }else {
//                    mFloatingActionButton.;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs){
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.tab_custom,null);
            tab.setCustomView(view);
            TextView tvTitle = (TextView)view.findViewById(R.id.tv_tab);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.signout:
                AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.dialog_warning))
                        .setMessage(getString(R.string.dialog_signout))
                        .setNegativeButton(getString(R.string.dialog_no),null)
                        .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BmobUser.logOut();
                                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).create();
                alertDialog.show();
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.parseColor("#E91E63"));
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.parseColor("#E91E63"));
                break;

            default:
                break;
        }
        return true;
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TAB_FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }


}


