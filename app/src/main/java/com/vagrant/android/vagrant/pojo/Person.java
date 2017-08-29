package com.vagrant.android.vagrant.pojo;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Txm on 18/08/2017.
 */

public class Person extends BmobUser {
    private String description;
    private Boolean gender;
    private BmobFile face;
    private BmobRelation focusedPets;

    public BmobRelation getFocusedPets() {
        return focusedPets;
    }

    public void setFocusedPets(BmobRelation focusedPets) {
        this.focusedPets = focusedPets;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public BmobFile getFace() {
        return face;
    }

    public void setFace(BmobFile face) {
        this.face = face;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
