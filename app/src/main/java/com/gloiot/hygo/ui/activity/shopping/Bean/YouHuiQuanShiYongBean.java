package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * Created by hygo02 on 2017/7/10.
 */

public class YouHuiQuanShiYongBean implements Serializable{
    //优惠券id
    private String youhuiquanID;
    //商家账号
    private String shanjiaZhanghao;
    //数量
    private String shuliang;
    //优惠券名称
    private String mingcheng;
    //金额
    private String jine;
    //满减条件
    private String tiaojian;
    //红包类型
    private String leixing;
    //使用时间
    private String shijian;
    //商家限制
    private String shangjia;
    //红包描述
    private String miaoshu;
    //状态  （可使用/不可使用）
    private String zhuangTai;

    public String getYouhuiquanID() {
        return youhuiquanID;
    }

    public void setYouhuiquanID(String youhuiquanID) {
        this.youhuiquanID = youhuiquanID;
    }

    public String getShanjiaZhanghao() {
        return shanjiaZhanghao;
    }

    public void setShanjiaZhanghao(String shanjiaZhanghao) {
        this.shanjiaZhanghao = shanjiaZhanghao;
    }

    public String getShuliang() {
        return shuliang;
    }

    public void setShuliang(String shuliang) {
        this.shuliang = shuliang;
    }

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng;
    }

    public String getJine() {
        return jine;
    }

    public void setJine(String jine) {
        this.jine = jine;
    }

    public String getTiaojian() {
        return tiaojian;
    }

    public void setTiaojian(String tiaojian) {
        this.tiaojian = tiaojian;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    public String getShijian() {
        return shijian;
    }

    public void setShijian(String shijian) {
        this.shijian = shijian;
    }

    public String getShangjia() {
        return shangjia;
    }

    public void setShangjia(String shangjia) {
        this.shangjia = shangjia;
    }

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu;
    }

    public String getZhuangTai() {
        return zhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        this.zhuangTai = zhuangTai;
    }
}
