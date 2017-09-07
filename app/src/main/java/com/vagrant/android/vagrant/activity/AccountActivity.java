package com.vagrant.android.vagrant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Person;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Person bmobUser = BmobUser.getCurrentUser(Person.class);
        initViews();
        if(bmobUser != null){
            setAccountInfo(bmobUser);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Person bmobUser = BmobUser.getCurrentUser(Person.class);
        if(bmobUser != null){
            setAccountInfo(bmobUser);
        }
    }

    private void setAccountInfo(Person bmobUser){
        CircleImageView ciFace = (CircleImageView)findViewById(R.id.account_faces);
        TextView tvDescription = (TextView)findViewById(R.id.account_description);
        TextView tvUsername = (TextView)findViewById(R.id.account_username);
        TextView tvEmail = (TextView)findViewById(R.id.account_email);
        TextView tvPhone = (TextView)findViewById(R.id.account_phone);
        TextView tvGender = (TextView)findViewById(R.id.account_gender);
        String description = "";
        String username = "";
        String email = "";
        String phone = "";
        String gender = "Male";
        BmobFile face = bmobUser.getFace();
        if(face != null){
            String faceUri = face.getFileUrl();
            Glide.with(AccountActivity.this).load(faceUri).into(ciFace);
        }
        if(BmobUser.getObjectByKey("description") != null){
            description = BmobUser.getObjectByKey("description").toString();
            //description = bmobUser.getDescription();
        }
        if(BmobUser.getObjectByKey("username") != null){
            username = BmobUser.getObjectByKey("username").toString();
            //username = bmobUser.getUsername();
        }
        if(BmobUser.getObjectByKey("email") != null){
            email = BmobUser.getObjectByKey("email").toString();
            //email = bmobUser.getEmail();
        }
        if(BmobUser.getObjectByKey("mobilePhoneNumber") != null){
            phone = BmobUser.getObjectByKey("mobilePhoneNumber").toString();
            //phone = bmobUser.getMobilePhoneNumber();
        }
        if(BmobUser.getObjectByKey("gender") != null){
           if("true".equals(BmobUser.getObjectByKey("gender").toString())){
               gender = "Male";
           }else {
               gender = "Female";
           }
        }

        tvDescription.setText(description);
        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvUsername.setText(username);
        tvGender.setText(gender);
    }
    private void initViews(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.modify:
                Intent intent = new Intent(AccountActivity.this,ModifyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
