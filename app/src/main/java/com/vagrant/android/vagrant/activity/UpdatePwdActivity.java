package com.vagrant.android.vagrant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Person;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePwdActivity extends AppCompatActivity {
    private EditText mPrePwd;
    private EditText mNewPwd;
    private EditText mRePwd;
    private AppCompatButton mUpdatePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        initView();

    }
    private void initView(){
        mPrePwd = (EditText)findViewById(R.id.update_pwd_prepwd);
        mNewPwd = (EditText)findViewById(R.id.update_pwd_newpwd);
        mRePwd = (EditText)findViewById(R.id.update_pwd_repwd);
        mUpdatePwd = (AppCompatButton)findViewById(R.id.btn_update_pwd);
        mUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePwd();
            }
        });
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void updatePwd(){
        mUpdatePwd.setEnabled(true);
        Person person = BmobUser.getCurrentUser(Person.class);
        String prePwd = mPrePwd.getText().toString();
        String newPwd = mNewPwd.getText().toString();
        String rePwd = mRePwd.getText().toString();
        if(newPwd.equals(rePwd)) {
            person.updateCurrentUserPassword(prePwd, newPwd, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        Toast.makeText(UpdatePwdActivity.this,"修改成功,请重新登录",Toast.LENGTH_SHORT).show();
                        BmobUser.logOut();
                        Intent intent = new Intent(UpdatePwdActivity.this,SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        mUpdatePwd.setEnabled(false);
                        Toast.makeText(UpdatePwdActivity.this,"修改失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
        }
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
