package com.gloiot.hygo.ui.activity.shopping.Bean;

/**
 * Created by hygo03 on 2017/11/2.
 */

public class YouHuiQuanBean {

    private String name;
    private String price;
    private String id;

    public YouHuiQuanBean() {
    }


    public YouHuiQuanBean(String name, String price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "YouHuiQuanBean{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
