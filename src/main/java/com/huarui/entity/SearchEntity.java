package com.huarui.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lihui on 2019/1/30.
 */
public class SearchEntity implements Serializable {

    private String id;

    private Date createTime;

    public SearchEntity(){}
    public SearchEntity(String id, Date createTime) {
        this.id = id;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
