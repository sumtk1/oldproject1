package com.gloiot.hygo.server.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hygo01 on 17/3/28.
 * 图片清单
 */
@Entity
public class DBPictureList {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    @Unique
    private String code;
    private String name;
    private String info;
    private String path;
    private String type;
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getInfo() {
        return this.info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1376239493)
    public DBPictureList(Long id, @NotNull String code, String name, String info,
            String path, String type) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.info = info;
        this.path = path;
        this.type = type;
    }
    @Generated(hash = 1672176795)
    public DBPictureList() {
    }

}
