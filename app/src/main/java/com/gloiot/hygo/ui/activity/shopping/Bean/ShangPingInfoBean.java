package com.gloiot.hygo.ui.activity.shopping.Bean;

/**
 * Created by hygo03 on 2016/10/17.
 * 创建者：莫小霞
 * 商品详情——商品实体
 *
 * 修改：詹志钦
 * 时间：2017-5-9
 * 单独提取封装商品属性实体，删除该实体不必要的参数
 */

public class ShangPingInfoBean {


    private String id;  //id
    private String yuexiao; //月销
    private String img_URL; //图片
    private String jiage; //价格
    private String dizhi;//地址
    private String name;//商品名称
    private String fenlei;//分类
    private String kuaidifei;//快递费
    private String kuchun;//库存
    private String chicun; //尺寸
    private String yanse; //颜色
    private String dianpu;
    private String fukuanNum;//付款数量
    private String guige;//规格
    private String dianpuId;
    private String dianputubiao;
    private String dianpumingcheng;
    private String leixin;//全球购与普通
    private String huoDongShangPin = "";

    public ShangPingInfoBean() {
    }

    @Override
    public String toString() {
        return "ShangPingInfo{" +
                "id='" + id + '\'' +
                ", yuexiao='" + yuexiao + '\'' +
                ", img_URL='" + img_URL + '\'' +
                ", jiage='" + jiage + '\'' +
                ", dizhi='" + dizhi + '\'' +
                ", name='" + name + '\'' +
                ", fenlei='" + fenlei + '\'' +
                ", kuaidifei='" + kuaidifei + '\'' +
                ", kuchun='" + kuchun + '\'' +
                ", chicun='" + chicun + '\'' +
                ", yanse='" + yanse + '\'' +
                ", dianpu='" + dianpu + '\'' +
                ", fukuanNum='" + fukuanNum + '\'' +
                ", guige='" + guige + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_URL() {
        return img_URL;
    }

    public void setImg_URL(String img_URL) {
        this.img_URL = img_URL;
    }

    public String getJiage() {
        return jiage;
    }

    public void setJiage(String jiage) {
        this.jiage = jiage;
    }

    public String getDizhi() {
        return dizhi;
    }

    public void setDizhi(String dizhi) {
        this.dizhi = dizhi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }

    public String getKuaidifei() {
        return kuaidifei;
    }

    public void setKuaidifei(String kuaidifei) {
        this.kuaidifei = kuaidifei;
    }

    public String getKuchun() {
        return kuchun;
    }

    public void setKuchun(String kuchun) {
        this.kuchun = kuchun;
    }

    public String getChicun() {
        return chicun;
    }

    public void setChicun(String chicun) {
        this.chicun = chicun;
    }

    public String getYanse() {
        return yanse;
    }

    public void setYanse(String yanse) {
        this.yanse = yanse;
    }

    public String getDianpu() {
        return dianpu;
    }

    public void setDianpu(String dianpu) {
        this.dianpu = dianpu;
    }

    public String getFukuanNum() {
        return fukuanNum;
    }

    public void setFukuanNum(String fukuanNum) {
        this.fukuanNum = fukuanNum;
    }

    public String getGuige() {
        return guige;
    }

    public void setGuige(String guige) {
        this.guige = guige;
    }

    public String getDianpuId() {
        return dianpuId;
    }

    public void setDianpuId(String dianpuId) {
        this.dianpuId = dianpuId;
    }

    public String getDianputubiao() {
        return dianputubiao;
    }

    public void setDianputubiao(String dianputubiao) {
        this.dianputubiao = dianputubiao;
    }

    public String getDianpumingcheng() {
        return dianpumingcheng;
    }

    public void setDianpumingcheng(String dianpumingcheng) {
        this.dianpumingcheng = dianpumingcheng;
    }

    public String getLeixin() {
        return leixin;
    }

    public void setLeixin(String leixin) {
        this.leixin = leixin;
    }

    public String getHuoDongShangPin() {
        return huoDongShangPin;
    }

    public void setHuoDongShangPin(String huoDongShangPin) {
        this.huoDongShangPin = huoDongShangPin;
    }
}
