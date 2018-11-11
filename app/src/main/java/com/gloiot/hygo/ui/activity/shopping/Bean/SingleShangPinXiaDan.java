package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * Created by hygo03 on 2017/6/19.
 * 单个商品下单的对象
 */

public class SingleShangPinXiaDan implements Serializable {
    private String color;//颜色
    private String size;//尺寸
    private String specification;//规格
    private String consignee;   //收货人
    private String consignee_phone; //收货人手机号
    private String address;     //收货地址
    private String shangPin_Num; //商品数量
    private String shangPin_ID; //商品ID
    private String shengFen_ID; //身份证ID

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsignee_phone() {
        return consignee_phone;
    }

    public void setConsignee_phone(String consignee_phone) {
        this.consignee_phone = consignee_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShangPin_Num() {
        return shangPin_Num;
    }

    public void setShangPin_Num(String shangPin_Num) {
        this.shangPin_Num = shangPin_Num;
    }

    public String getShangPin_ID() {
        return shangPin_ID;
    }

    public void setShangPin_ID(String shangPin_ID) {
        this.shangPin_ID = shangPin_ID;
    }

    public String getShengFen_ID() {
        return shengFen_ID;
    }

    public void setShengFen_ID(String shengFen_ID) {
        this.shengFen_ID = shengFen_ID;
    }
}
