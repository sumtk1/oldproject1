package com.gloiot.hygo.ui.activity.shopping.Bean;

import com.gloiot.hygo.ui.activity.shopping.wodedingdan.MyGVAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hygo02 on 2017/1/2.
 * 创建者：詹志钦
 * 订单完成时，对商品发表评价实体
 */

public class PingjiaFaBiaoBean implements Serializable {
    private String id;
    private String shangpin_id;
    private String Shangpin_img_url;
    private String Shangpin_title;
    private String Shangpin_pingjianeirong = "";
    private String Shangpin_guigefenlei;
    private String Shangpin_leixing;  //类型(全球购，自营等)
    private String Shangpin_star = "3";

    private List<String> Shangpin_imgList;
    private MyGVAdapter myGVAdapter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShangpin_img_url() {
        return Shangpin_img_url;
    }

    public void setShangpin_img_url(String shangpin_img_url) {
        Shangpin_img_url = shangpin_img_url;
    }

    public String getShangpin_title() {
        return Shangpin_title;
    }

    public void setShangpin_title(String shangpin_title) {
        Shangpin_title = shangpin_title;
    }

    public String getShangpin_pingjianeirong() {
        return Shangpin_pingjianeirong;
    }

    public void setShangpin_pingjianeirong(String shangpin_pingjianeirong) {
        Shangpin_pingjianeirong = shangpin_pingjianeirong;
    }

    public String getShangpin_id() {
        return shangpin_id;
    }

    public void setShangpin_id(String shangpin_id) {
        this.shangpin_id = shangpin_id;
    }

    public List<String> getShangpin_imgList() {
        return Shangpin_imgList;
    }

    public void setShangpin_imgList(List<String> shangpin_imgList) {
        Shangpin_imgList = shangpin_imgList;
    }

    public MyGVAdapter getMyGVAdapter() {
        return myGVAdapter;
    }

    public void setMyGVAdapter(MyGVAdapter myGVAdapter) {
        this.myGVAdapter = myGVAdapter;
    }

    public String getShangpin_guigefenlei() {
        return Shangpin_guigefenlei;
    }

    public void setShangpin_guigefenlei(String shangpin_guigefenlei) {
        Shangpin_guigefenlei = shangpin_guigefenlei;
    }

    public String getShangpin_leixing() {
        return Shangpin_leixing;
    }

    public void setShangpin_leixing(String shangpin_leixing) {
        Shangpin_leixing = shangpin_leixing;
    }

    public String getShangpin_star() {
        return Shangpin_star;
    }

    public void setShangpin_star(String shangpin_star) {
        Shangpin_star = shangpin_star;
    }

    @Override
    public String toString() {
        return "PingjiaFaBiaoBean{" +
                "id='" + id + '\'' +
                ", shangpin_id='" + shangpin_id + '\'' +
                ", Shangpin_img_url='" + Shangpin_img_url + '\'' +
                ", Shangpin_title='" + Shangpin_title + '\'' +
                ", Shangpin_pingjianeirong='" + Shangpin_pingjianeirong + '\'' +
                ", Shangpin_guigefenlei='" + Shangpin_guigefenlei + '\'' +
                ", Shangpin_leixing='" + Shangpin_leixing + '\'' +
                ", Shangpin_star='" + Shangpin_star + '\'' +
                ", Shangpin_imgList=" + Shangpin_imgList +
                ", myGVAdapter=" + myGVAdapter +
                '}';
    }
}
