package com.vagrant.android.vagrant.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Person;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyActivity extends AppCompatActivity {
    private static Map preAccountInfo = new HashMap();
    public static final int CHOOSE_PHOTO = 1;
    private CircleImageView mCircleImage;
    private String newImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Person bmobUser = BmobUser.getCurrentUser(Person.class);
        initViews();
        if (bmobUser != null) {
            setAccountInfo(bmobUser);
        }

    }
    private void handleImage(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.adnroid.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        newImage = imagePath;
        Log.e("NewImage",newImage);
        displayImage(imagePath);
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mCircleImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;

    }
    private void updateAccountInfo(){
        Person newUser = new Person();
        EditText etDescription = (EditText) findViewById(R.id.modify_description);
        //EditText etUsername = (EditText) findViewById(R.id.modify_username);
        EditText etEmail = (EditText) findViewById(R.id.modify_email);
        EditText etPhone = (EditText) findViewById(R.id.modify_phone);
        RadioGroup rgGender = (RadioGroup)findViewById(R.id.modify_gender);
        RadioButton rbChecked = (RadioButton)findViewById(rgGender.getCheckedRadioButtonId());
        String description = etDescription.getText().toString();
        //String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String gender = rbChecked.getText().toString();
        if(gender.equals("男生")){
            gender = "Male";
        }else {
            gender = "Female";
        }
        if(preAccountInfo.containsKey("description")){
            if(!(preAccountInfo.get("description").toString().equals(description))){
                newUser.setDescription(description);
            }
        }else {
            newUser.setDescription(description);
        }
//        if(preAccountInfo.containsKey("username")){
//            if (!(preAccountInfo.get("username").toString().equals(username))){
//                newUser.setUsername(username);
//            }
//        }else {
//            newUser.setUsername(username);
//        }
        if(preAccountInfo.containsKey("email")){
            if (!(preAccountInfo.get("email").toString().equals(email))){
                newUser.setEmail(email);
            }
        }else {
            newUser.setEmail(email);
        }
        if(preAccountInfo.containsKey("phone")){
            if (!(preAccountInfo.get("phone").toString().equals(phone))){
                newUser.setMobilePhoneNumber(phone);
            }
        }else {
            newUser.setMobilePhoneNumber(phone);
        }
        if(preAccountInfo.containsKey("gender")){
            if (!(preAccountInfo.get("gender").toString().equals(gender))){
                newUser.setGender(gender.equals("Male") ? true:false);
            }
        }else{
            newUser.setGender(gender.equals("Male") ? true:false);
        }
        Person bmobUser = BmobUser.getCurrentUser(Person.class);
        if(newImage != null){
            updateFace(newImage,bmobUser);
        }
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Toast.makeText(ModifyActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ModifyActivity.this,AccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ModifyActivity.this, "更新失败" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void updateFace(String path, final Person bmobUser){
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.e("上传成功:",bmobFile.getFileUrl());
                    Person newUser = new Person();
                    newUser.setFace(bmobFile);
                    newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.e("更新头像成功:","6666");
                            }else {
                                Log.e("更新头像失败:","2333"+e.getMessage());
                            }
                        }
                    });
                }else {
                    Log.e("上传失败:","23333");
                }
            }
        });



    }
    private void setAccountInfo(Person bmobUser) {
        CircleImageView ciFace = (CircleImageView)findViewById(R.id.modify_face);
        EditText etDescription = (EditText) findViewById(R.id.modify_description);
        //EditText etUsername = (EditText) findViewById(R.id.modify_username);
        EditText etEmail = (EditText) findViewById(R.id.modify_email);
        EditText etPhone = (EditText) findViewById(R.id.modify_phone);
        //RadioGroup rgGender = (RadioGroup)findViewById(R.id.modify_gender);
        RadioButton rbMale = (RadioButton) findViewById(R.id.modify_gender_male);
        RadioButton rbFemale = (RadioButton) findViewById(R.id.modify_gender_female);
        String  textMale = "Male";
        if(!"男生".equals(rbMale.getText())) {
            textMale = "Female";
        }


        String description = "Wubba Lubba Dub Dub!";
        String email = "";
        String phone = "";
        String gender = "";
        BmobFile face = bmobUser.getFace();
        if(face != null){
            String faceUri = face.getFileUrl();
            Glide.with(ModifyActivity.this).load(faceUri).into(ciFace);
        }
        if (BmobUser.getObjectByKey("description") != null) {
            description = BmobUser.getObjectByKey("description").toString();
            preAccountInfo.put("description",description);
        }
//        if (BmobUser.getObjectByKey("username") != null) {
//            username = BmobUser.getObjectByKey("username").toString();
//            preAccountInfo.put("username",username);
//        }
        if (BmobUser.getObjectByKey("email") != null) {
            email = BmobUser.getObjectByKey("email").toString();
            preAccountInfo.put("email",email);
        }
        if (BmobUser.getObjectByKey("mobilePhoneNumber") != null) {
            phone = BmobUser.getObjectByKey("mobilePhoneNumber").toString();
            preAccountInfo.put("phone",phone);
        }
        if (BmobUser.getObjectByKey("gender") != null) {
            if ("true".equals(BmobUser.getObjectByKey("gender").toString())) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            preAccountInfo.put("gender",gender);
        }
        etDescription.setText(description);
        //etUsername.setText(username);
        etEmail.setText(email);
        etPhone.setText(phone);
        if (gender.equals(textMale)) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }


    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mCircleImage = (CircleImageView)findViewById(R.id.modify_face);
        mCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ModifyActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ModifyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(ModifyActivity.this, AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.confirm_modification:
                updateAccountInfo();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    handleImage(data);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this, "您拒绝了请求", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
