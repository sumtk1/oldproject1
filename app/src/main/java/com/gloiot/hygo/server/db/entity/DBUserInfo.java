package com.gloiot.hygo.server.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by hygo01 on 17/3/27.
 * 用户信息表
 */

@Entity
public class DBUserInfo {

    @Id(autoincrement = true)
    private Long id;
    @NotNull@Unique
    private String zhanghao;
    private String nicheng;

    public String getNicheng() {
        return this.nicheng;
    }
    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }
    public String getZhanghao() {
        return this.zhanghao;
    }
    public void setZhanghao(String zhanghao) {
        this.zhanghao = zhanghao;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 633469549)
    public DBUserInfo(Long id, @NotNull String zhanghao, String nicheng) {
        this.id = id;
        this.zhanghao = zhanghao;
        this.nicheng = nicheng;
    }
    @Generated(hash = 1036674729)
    public DBUserInfo() {
    }
}
