package com.gloiot.hygo.ui.activity.shopping.Bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by hygo02 on 2017/3/23.
 * 创建者：詹志钦
 * 商品展示实体（分类、搜索，品牌特卖模块等）
 */

public class FenLeiSonBean implements Serializable, MultiItemEntity {
    // 使用BaseRecyclerAdapter实现多布局RecyclerView添加
    public static final int OneLine = 1;
    public static final int TwoLine = 2;
    private int itemType;

    private String fenLeiSon_id;
    private String fenLeiSon_img_url;
    private String fenLeiSon_title;
    private String fenLeiSon_jiage;
    private String fenLeiSon_dizhi;
    private String fenLeiSon_leixin="";
    private String fenLeiSon_lingshoujia="";
    private String fenLeiSon_xiaoshouliang;//8/22 by hygo03添加

    public FenLeiSonBean() {
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getFenLeiSon_img_url() {
        return fenLeiSon_img_url;
    }

    public void setFenLeiSon_img_url(String fenLeiSon_img_url) {
        this.fenLeiSon_img_url = fenLeiSon_img_url;
    }

    public String getFenLeiSon_title() {
        return fenLeiSon_title;
    }

    public void setFenLeiSon_title(String fenLeiSon_title) {
        this.fenLeiSon_title = fenLeiSon_title;
    }

    public String getFenLeiSon_jiage() {
        return fenLeiSon_jiage;
    }

    public void setFenLeiSon_jiage(String fenLeiSon_jiage) {
        this.fenLeiSon_jiage = fenLeiSon_jiage;
    }

    public String getFenLeiSon_dizhi() {
        return fenLeiSon_dizhi;
    }

    public void setFenLeiSon_dizhi(String fenLeiSon_dizhi) {
        this.fenLeiSon_dizhi = fenLeiSon_dizhi;
    }

    public String getFenLeiSon_id() {
        return fenLeiSon_id;
    }

    public void setFenLeiSon_id(String fenLeiSon_id) {
        this.fenLeiSon_id = fenLeiSon_id;
    }

    public String getFenLeiSon_leixin() {
        return fenLeiSon_leixin;
    }

    public void setFenLeiSon_leixin(String fenLeiSon_leixin) {
        this.fenLeiSon_leixin = fenLeiSon_leixin;
    }

    public String getFenLeiSon_lingshoujia() {
        return fenLeiSon_lingshoujia;
    }

    public void setFenLeiSon_lingshoujia(String fenLeiSon_lingshoujia) {
        this.fenLeiSon_lingshoujia = fenLeiSon_lingshoujia;
    }

    public String getFenLeiSon_xiaoshouliang() {
        return fenLeiSon_xiaoshouliang;
    }

    public void setFenLeiSon_xiaoshouliang(String fenLeiSon_xiaoshouliang) {
        this.fenLeiSon_xiaoshouliang = fenLeiSon_xiaoshouliang;
    }

    @Override
    public String toString() {
        return "FenLeiSonBean{" +
                "fenLeiSon_id='" + fenLeiSon_id + '\'' +
                ", fenLeiSon_img_url='" + fenLeiSon_img_url + '\'' +
                ", fenLeiSon_title='" + fenLeiSon_title + '\'' +
                ", fenLeiSon_jiage='" + fenLeiSon_jiage + '\'' +
                ", fenLeiSon_dizhi='" + fenLeiSon_dizhi + '\'' +
                ", fenLeiSon_leixin='" + fenLeiSon_leixin + '\'' +
                ", fenLeiSon_lingshoujia='" + fenLeiSon_lingshoujia + '\'' +
                ", fenLeiSon_xiaoshouliang='" + fenLeiSon_xiaoshouliang + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
