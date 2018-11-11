package com.gloiot.hygo.ui.activity.shopping.Bean;

/**
 * Created by hygo03 on 2016/11/23.
 * 创建者：莫小霞
 * 分类——二级分类实体
 */

public class FenLeiErJiBean {

    private String mingzi;
    private String tupian;

    public String getMingzi() {
        return mingzi;
    }

    public void setMingzi(String mingzi) {
        this.mingzi = mingzi;
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian;
    }

    @Override
    public String toString() {
        return "FenLeiErJiBean{" +
                "mingzi='" + mingzi + '\'' +
                ", tupian='" + tupian + '\'' +
                '}';
    }
}
