package com.vagrant.android.vagrant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vagrant.android.vagrant.R;
import com.vagrant.android.vagrant.pojo.Person;
import com.vagrant.android.vagrant.pojo.Pet;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class PetActivity extends AppCompatActivity {
    public static final String PET_NAME = "pet_name";
    public static final String PET_IMAGE_ID = "pet_image_id";
    public static final String PET_BREED = "pet_breed";
    public static final String PET_AGE = "pet_age";
    public static final String PET_GENDER = "pet_gender";
    public static final String PET_ORIGANIZATION = "pet_organization";
    public static final String PET_DESCRIPTION = "pet_description";
    public static final String PET_CONTACT = "pet_contact";
    public static final String PET_ID = "pet_id";
    private Boolean isFocused = false;
    private List<String> focusId = new ArrayList<String>();
    private FloatingActionButton  floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        Intent intent = getIntent();
        final Person person = BmobUser.getCurrentUser(Person.class);

        final String petId = intent.getStringExtra(PET_ID);
        String petName = intent.getStringExtra(PET_NAME);
        String petImageId = intent.getStringExtra(PET_IMAGE_ID);
        String petBreed = intent.getStringExtra(PET_BREED);
        String petContact = intent.getStringExtra(PET_CONTACT);
        int petAge = intent.getIntExtra(PET_AGE, 0);
        String petOrganization = intent.getStringExtra(PET_ORIGANIZATION);
        String petDescription = intent.getStringExtra(PET_DESCRIPTION);
        Boolean petGender = intent.getBooleanExtra(PET_GENDER, true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toobar_pet);
        ImageView petImageView = (ImageView) findViewById(R.id.pet_header_image);
        TextView petContentText = (TextView) findViewById(R.id.pet_content_text);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.pet_floating_action_button);

        if (person != null){
            getFocusID(person,petId);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFocused) {
                    cancelFocus(person,petId);
                }else {
                    addFocus(person, petId);
                }
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(petName);
        Glide.with(this).load(petImageId).into(petImageView);
        String petContent = petContent(petName, petBreed, petOrganization, petDescription,petContact,petAge, petGender);
        petContentText.setText(petContent);

    }
    @NonNull
    private String petContent(String petName, String petBreed, String petOrganization, String petDescription, String petContact, int petAge, Boolean petGender) {
        StringBuilder petContent = new StringBuilder();
        petContent.append("名字:\n\b\b\b\b\b\b\b\b\b\b\b"+petName);
        petContent.append("\n");
        petContent.append("品种:\n\b\b\b\b\b\b\b\b\b\b\b"+petBreed);
        petContent.append("\n");
        petContent.append("年龄:\n\b\b\b\b\b\b\b\b\b\b\b"+petAge);
        petContent.append("\n");
        if (petGender == true) {
            petContent.append("性别:\n\b\b\b\b\b\b\b\b\b\b\b雄");
        }else {
            petContent.append("性别:\n\b\b\b\b\b\b\b\b\b\b\b雌");
        }
        petContent.append("\n");
        petContent.append("机构:\n\b\b\b\b\b\b\b\b\b\b\b"+petOrganization);
        petContent.append("\n");
        petContent.append("介绍:\n\b\b\b\b\b\b\b\b\b\b\b"+petDescription);
        petContent.append("\n");
        petContent.append("联系方式:\n\b\b\b\b\b\b\b\b\b\b\b"+petContact);
        return petContent.toString();
    }
    private void addFocus(Person bmobUser,String petId){
        Pet pet = new Pet();
        pet.setObjectId(petId);
        BmobRelation relation = new BmobRelation();
        relation.add(pet);
        bmobUser.setFocusedPets(relation);
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.e("关注成功","");
                }else {
                    Log.e("关注失败",e.getMessage());
                }
            }
        });
        floatingActionButton.setImageResource(R.drawable.ic_turned_in_pink_500_24dp);
        focusId.add(petId);
        isFocused = true;


    }
    private void cancelFocus(Person bmobUser,String petId){
        Pet pet = new Pet();
        pet.setObjectId(petId);
        BmobRelation relation = new BmobRelation();
        relation.remove(pet);
        bmobUser.setFocusedPets(relation);
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.e("关系删除成功","");
                }else {
                    Log.e("关系删除失败",e.getMessage());
                }
            }
        });
        floatingActionButton.setImageResource(R.drawable.ic_turned_in_not_pink_500_24dp);
        focusId.remove(petId);
        isFocused = false;

    }
    private void getFocusID(Person bmobUser, final String petId){
        focusId.clear();
        BmobQuery<Pet> query = new BmobQuery<Pet>();
        query.setLimit(50);
        query.addWhereRelatedTo("focusedPets",new BmobPointer(bmobUser));
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<Pet>() {
            @Override
            public void done(List<Pet> list, BmobException e) {
                if(e == null){
                    Log.e("查询关注ID成功:",String.valueOf(list.size()));
                    for (Pet pet:list){
                        focusId.add(pet.getObjectId());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (focusId.contains(petId)){
                                isFocused = true;
                                floatingActionButton.setImageResource(R.drawable.ic_turned_in_pink_500_24dp);
                            }else {
                                floatingActionButton.setImageResource(R.drawable.ic_turned_in_not_pink_500_24dp);
                            }
                        }
                    });
                }else {
                    Log.e("查询关注ID失败:",e.getMessage());
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
