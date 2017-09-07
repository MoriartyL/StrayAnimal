package com.vagrant.android.vagrant.pojo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Txm on 31/08/2017.
 */

public class Post extends BmobObject {
    private BmobFile publisherImage;
    private String publisherName;
    private RescueStation publisher;
    private String content;
    private BmobDate startTime;
    private BmobDate endTime;

    public RescueStation getPublisher() {
        return publisher;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public BmobDate getStartTime() {
        return startTime;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public BmobFile getPublisherImage() {
        return publisherImage;
    }

    public void setPublisherImage(BmobFile publisherImage) {
        this.publisherImage = publisherImage;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
