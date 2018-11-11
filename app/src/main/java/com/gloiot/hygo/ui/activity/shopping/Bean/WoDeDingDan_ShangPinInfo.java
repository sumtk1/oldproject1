package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * Created by hygo02 on 2016/10/14.
 * 创建者：詹志钦
 * 我的订单里面商品实体
 */

public class WoDeDingDan_ShangPinInfo implements Serializable {
    private String iv_item_dingdanxiangqing_tupian_url;   //商品图片
    private String tv_item_dingdanxiangqing_title;  //商品标题
    private String tv_item_dingdanxiangqing_danjia;  //商品单价
    private String tv_item_dingdanxiangqing_shuliang;  //商品数量
    private String tv_item_dingdanxiangqing_zhongleixiangxi;  //商品分类

    private String tv_item_dingdanxiangqing_yunfei;  //运费

    private String iv_item_dingdanxiangqing_id;  //id
    private String iv_item_dingdanxiangqing_shangpinId;  //商品id
    private String iv_item_dingdanxiangqing_shangpinZhuangTai;  //商品状态

    private String iv_item_dingdanxiangqing_pingJiaZhuangTai;  //评价状态
    private String iv_item_dingdanxiangqing_wuLiuZhuangTai;  //物流状态

    private String shangPinType;//商品类型(全球购，自营)


    public String getTv_item_dingdanxiangqing_title() {
        return tv_item_dingdanxiangqing_title;
    }

    public void setTv_item_dingdanxiangqing_title(String tv_item_dingdanxiangqing_title) {
        this.tv_item_dingdanxiangqing_title = tv_item_dingdanxiangqing_title;
    }

    public String getTv_item_dingdanxiangqing_danjia() {
        return tv_item_dingdanxiangqing_danjia;
    }

    public void setTv_item_dingdanxiangqing_danjia(String tv_item_dingdanxiangqing_danjia) {
        this.tv_item_dingdanxiangqing_danjia = tv_item_dingdanxiangqing_danjia;
    }

    public String getTv_item_dingdanxiangqing_shuliang() {
        return tv_item_dingdanxiangqing_shuliang;
    }

    public void setTv_item_dingdanxiangqing_shuliang(String tv_item_dingdanxiangqing_shuliang) {
        this.tv_item_dingdanxiangqing_shuliang = tv_item_dingdanxiangqing_shuliang;
    }

    public String getTv_item_dingdanxiangqing_dingdanyanse() {
        return tv_item_dingdanxiangqing_zhongleixiangxi;
    }

    public void setTv_item_dingdanxiangqing_zhongleixiangxi(String tv_item_dingdanxiangqing_zhongleixiangxi) {
        this.tv_item_dingdanxiangqing_zhongleixiangxi = tv_item_dingdanxiangqing_zhongleixiangxi;
    }

    public String getIv_item_dingdanxiangqing_tupian_url() {
        return iv_item_dingdanxiangqing_tupian_url;
    }

    public void setIv_item_dingdanxiangqing_tupian_url(String iv_item_dingdanxiangqing_tupian_url) {
        this.iv_item_dingdanxiangqing_tupian_url = iv_item_dingdanxiangqing_tupian_url;
    }


    public String getIv_item_dingdanxiangqing_id() {
        return iv_item_dingdanxiangqing_id;
    }

    public void setIv_item_dingdanxiangqing_id(String iv_item_dingdanxiangqing_id) {
        this.iv_item_dingdanxiangqing_id = iv_item_dingdanxiangqing_id;
    }

    public String getIv_item_dingdanxiangqing_shangpinId() {
        return iv_item_dingdanxiangqing_shangpinId;
    }

    public void setIv_item_dingdanxiangqing_shangpinId(String iv_item_dingdanxiangqing_shangpinId) {
        this.iv_item_dingdanxiangqing_shangpinId = iv_item_dingdanxiangqing_shangpinId;
    }

    public String getTv_item_dingdanxiangqing_yunfei() {
        return tv_item_dingdanxiangqing_yunfei;
    }

    public void setTv_item_dingdanxiangqing_yunfei(String tv_item_dingdanxiangqing_yunfei) {
        this.tv_item_dingdanxiangqing_yunfei = tv_item_dingdanxiangqing_yunfei;
    }

    public String getShangPinType() {
        return shangPinType;
    }

    public void setShangPinType(String shangPinType) {
        this.shangPinType = shangPinType;
    }

    public String getIv_item_dingdanxiangqing_shangpinZhuangTai() {
        return iv_item_dingdanxiangqing_shangpinZhuangTai;
    }

    public void setIv_item_dingdanxiangqing_shangpinZhuangTai(String iv_item_dingdanxiangqing_shangpinZhuangTai) {
        this.iv_item_dingdanxiangqing_shangpinZhuangTai = iv_item_dingdanxiangqing_shangpinZhuangTai;
    }

    public String getTv_item_dingdanxiangqing_zhongleixiangxi() {
        return tv_item_dingdanxiangqing_zhongleixiangxi;
    }

    public String getIv_item_dingdanxiangqing_pingJiaZhuangTai() {
        return iv_item_dingdanxiangqing_pingJiaZhuangTai;
    }

    public void setIv_item_dingdanxiangqing_pingJiaZhuangTai(String iv_item_dingdanxiangqing_pingJiaZhuangTai) {
        this.iv_item_dingdanxiangqing_pingJiaZhuangTai = iv_item_dingdanxiangqing_pingJiaZhuangTai;
    }

    public String getIv_item_dingdanxiangqing_wuLiuZhuangTai() {
        return iv_item_dingdanxiangqing_wuLiuZhuangTai;
    }

    public void setIv_item_dingdanxiangqing_wuLiuZhuangTai(String iv_item_dingdanxiangqing_wuLiuZhuangTai) {
        this.iv_item_dingdanxiangqing_wuLiuZhuangTai = iv_item_dingdanxiangqing_wuLiuZhuangTai;
    }

    @Override
    public String toString() {
        return "WoDeDingDan_ShangPinInfo{" +
                "iv_item_dingdanxiangqing_tupian_url='" + iv_item_dingdanxiangqing_tupian_url + '\'' +
                ", tv_item_dingdanxiangqing_title='" + tv_item_dingdanxiangqing_title + '\'' +
                ", tv_item_dingdanxiangqing_danjia='" + tv_item_dingdanxiangqing_danjia + '\'' +
                ", tv_item_dingdanxiangqing_shuliang='" + tv_item_dingdanxiangqing_shuliang + '\'' +
                ", tv_item_dingdanxiangqing_zhongleixiangxi='" + tv_item_dingdanxiangqing_zhongleixiangxi + '\'' +
                ", tv_item_dingdanxiangqing_yunfei='" + tv_item_dingdanxiangqing_yunfei + '\'' +
                ", iv_item_dingdanxiangqing_id='" + iv_item_dingdanxiangqing_id + '\'' +
                ", iv_item_dingdanxiangqing_shangpinId='" + iv_item_dingdanxiangqing_shangpinId + '\'' +
                ", iv_item_dingdanxiangqing_shangpinZhuangTai='" + iv_item_dingdanxiangqing_shangpinZhuangTai + '\'' +
                ", iv_item_dingdanxiangqing_pingJiaZhuangTai='" + iv_item_dingdanxiangqing_pingJiaZhuangTai + '\'' +
                ", iv_item_dingdanxiangqing_wuLiuZhuangTai='" + iv_item_dingdanxiangqing_wuLiuZhuangTai + '\'' +
                ", shangPinType='" + shangPinType + '\'' +
                '}';
    }
}
