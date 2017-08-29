package com.vagrant.android.vagrant.pojo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Txm on 21/08/2017.
 */

public class Pet extends BmobObject {
    private String breed;
    private String description;
    private String organization;
    private String contact;
    private Integer age;
    private Boolean gender;
    private BmobFile petImage;
    private Boolean isAdopted;
    private String name;

    public Pet() {
    }


    public String getBreed() {
        return breed;
    }


    public String getDescription() {
        return description;
    }


    public String getOrganization() {
        return organization;
    }


    public Integer getAge() {
        return age;
    }


    public Boolean getGender() {
        return gender;
    }


    public BmobFile getPetImage() {
        return petImage;
    }

    public Boolean getAdopted() {
        return isAdopted;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
    //public void setName(String name) {this.name = name;}

}
