package com.vagrant.android.vagrant.pojo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Txm on 28/08/2017.
 */

public class RescueStation extends BmobObject {
    private BmobFile image;
    private String name;
    private String address;
    private String contact;
    private String area;
    private String description;

    public BmobFile getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getArea() {
        return area;
    }

    public String getDescription() {
        return description;
    }
}
