package com.vagrant.android.vagrant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Person;

import java.lang.reflect.Field;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Txm on 2017/7/10.
 */

public class RegisterActivity extends Activity {
    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private EditText mEditTextEmail;
    private Button mButtonRegister;
    private TextInputLayout textInputLayoutName;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView(){
        textInputLayoutName = (TextInputLayout)findViewById(R.id.text_input_name);

        mEditTextName = (EditText) findViewById(R.id.input_name);
        mEditTextPassword = (EditText) findViewById(R.id.input_password);
        mEditTextEmail = (EditText) findViewById(R.id.input_email);
        mButtonRegister = (Button) findViewById(R.id.btn_register);
        TextView linkLogin = (TextView)findViewById(R.id.link_login);
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
                RegisterActivity.this.startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }

        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
        startActivity(intent);
        RegisterActivity.this.finish();
    }
    private void signup(){
        String username = mEditTextName.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String email = mEditTextEmail.getText().toString();
        if(!inputValidate(username,password,email)){
            return;
        }
        mButtonRegister.setEnabled(false);
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(username);
        bmobUser.setEmail(email);
        bmobUser.setPassword(password);
        bmobUser.signUp(new SaveListener<Person>() {
            @Override
            public void done(Person s, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "注册成功" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                } else {
                    mButtonRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private boolean inputValidate(String username,String password,String email){
        boolean valid = true;
        if(username.isEmpty() || username.length() < 6){
            mEditTextName.setError("最少6个字符");
            valid = false;
        }else {
            mEditTextName.setError(null);
        }
        if(password.isEmpty() || password.length() < 6 || password.length() > 15){
            mEditTextPassword.setError("密码长度应在6到15个字符范围内");
            valid = false;
        }else {
            mEditTextPassword.setError(null);
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEditTextEmail.setError("邮箱地址不合法");
            valid = false;
        }else {
            mEditTextEmail.setError(null);
        }



        return valid;
    }
    public static void setErrorColor(TextInputLayout textInputLayout, int textColor,int backgroundColor) {
        try {
            Field fErrorView = TextInputLayout.class.getDeclaredField("mErrorView");
            fErrorView.setAccessible(true);
            TextView mErrorView = (TextView)fErrorView.get(textInputLayout);
            Log.e("mErrorView:",String.valueOf(mErrorView));
            mErrorView.setTextColor(textColor);
            mErrorView.setBackgroundColor(backgroundColor);
            mErrorView.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
