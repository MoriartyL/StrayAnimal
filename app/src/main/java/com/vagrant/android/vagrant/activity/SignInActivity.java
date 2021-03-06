package com.vagrant.android.vagrant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Person;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by Txm on 2017/7/10.
 */

public class SignInActivity extends AppCompatActivity {
    private static final String MY_BMOB_ID = "2938d61a18552981c7f21bacd57b702a";
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Bmob.initialize(this, MY_BMOB_ID);
        Person bmobUser = BmobUser.getCurrentUser(Person.class);
        if(bmobUser != null){
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            SignInActivity.this.finish();
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);
        initView();
    }
    private void initView(){
        Button loginButton=(Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username="";
                EditText editText1 =(EditText)findViewById(R.id.input_email);
                username=editText1.getText().toString();
                String password="";
                EditText editText2=(EditText)findViewById(R.id.input_password);
                password=editText2.getText().toString();

                BmobUser.loginByAccount(username, password, new LogInListener<Person>() {
                    @Override
                    public void done(Person person, BmobException e) {
                        if(person != null){
                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            SignInActivity.this.finish();
                        }else {
                            Toast.makeText(SignInActivity.this,"登录失败:"+e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.e("Login:",e.toString());
                        }
                    }
                });
            }
        });

        TextView register=(TextView) findViewById(R.id.link_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,RegisterActivity.class);
                SignInActivity.this.startActivity(intent);
                SignInActivity.this.finish();
            }
        });

    }


}
