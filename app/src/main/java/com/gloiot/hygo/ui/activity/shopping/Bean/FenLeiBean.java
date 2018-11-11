package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * Created by Admin on 2016/9/18.
 *
 * 商城模块首页实体（分类、品牌特卖、推荐商品）
 * 修改者：詹志钦
 * 修改内容：整理
 * 日期：2017/4/21
 */
public class FenLeiBean implements Serializable{
    private String imgUrl;
    private String title;
    private String name;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFenlei() {
        return title;
    }

    public void setFenlei(String fenlei) {
        this.title = fenlei;
    }


}
