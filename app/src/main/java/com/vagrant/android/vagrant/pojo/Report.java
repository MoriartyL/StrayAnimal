package com.vagrant.android.vagrant.pojo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Txm on 07/09/2017.
 */

public class Report extends BmobObject {
    private BmobFile image;
    private String species;
    private String location;
    private String description;
    private String time;
    private Person person;

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
