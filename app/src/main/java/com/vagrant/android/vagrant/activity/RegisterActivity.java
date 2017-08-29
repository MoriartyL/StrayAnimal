package com.vagrant.android.vagrant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vagrant.android.vagrant.pojo.Person;
import com.vagrant.android.vagrant.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Txm on 2017/7/10.
 */

public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        final Button register = (Button) findViewById(R.id.btn_register);
        TextView linkLogin = (TextView)findViewById(R.id.link_login);
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
                RegisterActivity.this.startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = "";
                EditText editText1 = (EditText) findViewById(R.id.input_name);
                username = editText1.getText().toString();

                String password = "";
                EditText editText2 = (EditText) findViewById(R.id.input_password);
                password = editText2.getText().toString();

                String email = "";
                EditText editText3 = (EditText) findViewById(R.id.input_email);
                email = editText3.getText().toString();

                BmobUser bu = new BmobUser();
                bu.setUsername(username);
                bu.setEmail(email);
                bu.setPassword(password);
//注意：不能用save方法进行注册
                bu.signUp(new SaveListener<Person>() {
                    @Override
                    public void done(Person s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "添加数据成功，返回objectId为：" + s, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
        startActivity(intent);
        RegisterActivity.this.finish();
    }
}
