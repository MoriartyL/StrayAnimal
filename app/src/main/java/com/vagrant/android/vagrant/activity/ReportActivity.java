package com.vagrant.android.vagrant.activity;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Person;
import com.vagrant.android.vagrant.pojo.Report;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ReportActivity extends AppCompatActivity {
    public static final int CHOOSE_PHOTO = 1;
    private FloatingActionButton mFloatingActionButton;
    private AppCompatButton mAppCompatButton;
    private ImageView mImageView;
    private String mImagePath;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initView();
    }
    private void initView(){
        mProgressDialog = new ProgressDialog(ReportActivity.this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mImageView = (ImageView)findViewById(R.id.report_show_image);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.report_image);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ReportActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ReportActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
        mAppCompatButton = (AppCompatButton)findViewById(R.id.report_confirm);
        mAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage(getString(R.string.report_loading));
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                uploadInformation();
            }
        });
    }
    private void uploadInformation(){
        mAppCompatButton.setEnabled(false);
        Person person = BmobUser.getCurrentUser(Person.class);
        final Report report = new Report();
        EditText editTextSpecies = (EditText)findViewById(R.id.report_species);
        EditText editTextLocation = (EditText)findViewById(R.id.report_loaction);
        EditText editTextDescription = (EditText)findViewById(R.id.report_description);
        EditText editTextTime = (EditText)findViewById(R.id.report_time);

        String species = editTextSpecies.getText().toString();
        String location = editTextLocation.getText().toString();
        String description = editTextDescription.getText().toString();
        String time = editTextTime.getText().toString();

        if(species.length() == 0 || location.length() == 0 || description.length() == 0 || time.length() == 0 || mImagePath == null){
            Toast.makeText(ReportActivity.this,getString(R.string.report_invalid),Toast.LENGTH_SHORT).show();
            mAppCompatButton.setEnabled(true);

        }else {
            report.setSpecies(species);
            report.setLocation(location);
            report.setDescription(description);
            report.setTime(time);
            report.setPerson(person);
            final BmobFile bmobFile = new BmobFile(new File(mImagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        Log.e("上传成功","Report Image");
                        report.setImage(bmobFile);
                        report.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    Log.e("上传成功","Report");
                                    Toast.makeText(ReportActivity.this,getString(R.string.report_success),Toast.LENGTH_SHORT).show();
                                    mProgressDialog.dismiss();
                                    finish();
                                }else{
                                    Log.e("上传失败",e.getMessage());
                                    mProgressDialog.dismiss();
                                    mAppCompatButton.setEnabled(true);
                                }
                            }
                        });
                    }else {
                        Log.e("上传失败",e.getMessage());
                        mProgressDialog.dismiss();
                        mAppCompatButton.setEnabled(true);
                    }
                }
            });

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
        mImagePath = imagePath;
        displayImage(imagePath);
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
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mImageView.setImageBitmap(bitmap);
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
}
