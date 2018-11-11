package com.gloiot.hygo.ui.activity.shopping.Bean;


import java.io.Serializable;

/**
 * Created by Admin on 2016/8/30.
 * 创建者：刘再君
 * 商品详情——商品信息实体
 */
public class MiaoShuBean implements Serializable {

    private  String miaoshu;
    private  String tupian;

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu;
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian;
    }

    public MiaoShuBean(String miaoshu, String tupian) {
        this.miaoshu = miaoshu;
        this.tupian = tupian;
    }

    public MiaoShuBean() {
    }
}
