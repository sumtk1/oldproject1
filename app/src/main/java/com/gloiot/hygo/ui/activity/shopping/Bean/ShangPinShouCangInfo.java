package com.gloiot.hygo.ui.activity.shopping.Bean;

import android.widget.ImageView;

/**
 * Created by hygo02 on 2016/10/14.
 * 创建者：詹志钦
 * 商品收藏——我的收藏实体
 */

public class ShangPinShouCangInfo {
    private ImageView iv_item_shangpinshoucang_dagou;
    private String iv_item_shangpinshoucang_id;
    private String iv_item_shangpinshoucang_tupian_url;
    private String tv_item_shangpinshoucang_title;
    private String tv_item_shangpinshoucang_pirce;
    private String tv_item_shangpinshoucang_leixing;
    private String tv_item_shangpinshoucang_zhuantai;//下架/售罄等


    public String getTv_item_shangpinshoucang_title() {
        return tv_item_shangpinshoucang_title;
    }

    public void setTv_item_shangpinshoucang_title(String tv_item_shangpinshoucang_title) {
        this.tv_item_shangpinshoucang_title = tv_item_shangpinshoucang_title;
    }

    public String getTv_item_shangpinshoucang_pirce() {
        return tv_item_shangpinshoucang_pirce;
    }

    public void setTv_item_shangpinshoucang_pirce(String tv_item_shangpinshoucang_pirce) {
        this.tv_item_shangpinshoucang_pirce = tv_item_shangpinshoucang_pirce;
    }

    public ImageView getIv_item_shangpinshoucang_dagou() {
        return iv_item_shangpinshoucang_dagou;
    }

    public void setIv_item_shangpinshoucang_dagou(ImageView iv_item_shangpinshoucang_dagou) {
        this.iv_item_shangpinshoucang_dagou = iv_item_shangpinshoucang_dagou;
    }

    public String getIv_item_shangpinshoucang_tupian_url() {
        return iv_item_shangpinshoucang_tupian_url;
    }

    public void setIv_item_shangpinshoucang_tupian_url(String iv_item_shangpinshoucang_tupian_url) {
        this.iv_item_shangpinshoucang_tupian_url = iv_item_shangpinshoucang_tupian_url;
    }

    public String getIv_item_shangpinshoucang_id() {
        return iv_item_shangpinshoucang_id;
    }

    public void setIv_item_shangpinshoucang_id(String iv_item_shangpinshoucang_id) {
        this.iv_item_shangpinshoucang_id = iv_item_shangpinshoucang_id;
    }

    public String getTv_item_shangpinshoucang_leixing() {
        return tv_item_shangpinshoucang_leixing;
    }

    public void setTv_item_shangpinshoucang_leixing(String tv_item_shangpinshoucang_leixing) {
        this.tv_item_shangpinshoucang_leixing = tv_item_shangpinshoucang_leixing;
    }

    public String getTv_item_shangpinshoucang_zhuantai() {
        return tv_item_shangpinshoucang_zhuantai;
    }

    public void setTv_item_shangpinshoucang_zhuantai(String tv_item_shangpinshoucang_zhuantai) {
        this.tv_item_shangpinshoucang_zhuantai = tv_item_shangpinshoucang_zhuantai;
    }

    @Override
    public String toString() {
        return "ShangPinShouCangInfo{" +
                "iv_item_shangpinshoucang_dagou=" + iv_item_shangpinshoucang_dagou +
                ", iv_item_shangpinshoucang_id='" + iv_item_shangpinshoucang_id + '\'' +
                ", iv_item_shangpinshoucang_tupian_url='" + iv_item_shangpinshoucang_tupian_url + '\'' +
                ", tv_item_shangpinshoucang_title='" + tv_item_shangpinshoucang_title + '\'' +
                ", tv_item_shangpinshoucang_pirce='" + tv_item_shangpinshoucang_pirce + '\'' +
                ", tv_item_shangpinshoucang_leixing='" + tv_item_shangpinshoucang_leixing + '\'' +
                ", tv_item_shangpinshoucang_zhuantai='" + tv_item_shangpinshoucang_zhuantai + '\'' +
                '}';
    }
}
