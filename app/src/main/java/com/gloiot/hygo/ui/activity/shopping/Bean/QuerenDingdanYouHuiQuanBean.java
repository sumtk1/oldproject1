package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * Created by hygo02 on 2017/7/10.
 */

public class QuerenDingdanYouHuiQuanBean implements Serializable {
    private String fushuXiaoxi;
    private String info;
    private YouHuiQuanShiYongBean youHuiQuanShiYongBean;

    public QuerenDingdanYouHuiQuanBean() {
    }

    public QuerenDingdanYouHuiQuanBean(String fushuXiaoxi) {
        this.fushuXiaoxi = fushuXiaoxi;
    }

    public QuerenDingdanYouHuiQuanBean(String fushuXiaoxi, String info) {
        this.fushuXiaoxi = fushuXiaoxi;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFushuXiaoxi() {
        return fushuXiaoxi;
    }

    public void setFushuXiaoxi(String fushuXiaoxi) {
        this.fushuXiaoxi = fushuXiaoxi;
    }

    public YouHuiQuanShiYongBean getYouHuiQuanShiYongBean() {
        return youHuiQuanShiYongBean;
    }

    public void setYouHuiQuanShiYongBean(YouHuiQuanShiYongBean youHuiQuanShiYongBean) {
        this.youHuiQuanShiYongBean = youHuiQuanShiYongBean;
    }

    @Override
    public String toString() {
        return "QuerenDingdanYouHuiQuanBean{" +
                "fushuXiaoxi='" + fushuXiaoxi + '\'' +
                ", info='" + info + '\'' +
                ", youHuiQuanShiYongBean=" + youHuiQuanShiYongBean +
                '}';
    }
}
