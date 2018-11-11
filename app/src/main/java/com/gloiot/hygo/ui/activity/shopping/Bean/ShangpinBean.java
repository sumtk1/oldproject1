package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * 商品信息实体类
 *
 * @author chenjunsen
 *         2015年8月17日下午11:23:04
 */
public class ShangpinBean extends GouwucheBean implements Serializable {

    private String id;
    private String shangpin_id;
    private String shangpin_mingcheng;
    private String shangpin_guige;
    private String shangpin_leixing = "";
    private int shangpin_shuliang;
    private double danjia;
    private String suolvtu;
    private double kuaidifei;
    private String kucun;
    private int position;//绝对位置，只在ListView构造的购物车中，在删除时有效
    private int p_position; //父元素
    private String isHuoDongShangPin = "否"; //是否是活动商品
    private String diKouLv = ""; //活动抵扣率

    public ShangpinBean(String id, String shangpin_id, String shangpin_mingcheng, String shangpin_guige, int shangpin_shuliang, double danjia, String suolvtu, double kuaidifei, String kucun, int position, int p_position, String leixing) {
        this.id = id;
        this.shangpin_id = shangpin_id;
        this.shangpin_mingcheng = shangpin_mingcheng;
        this.shangpin_guige = shangpin_guige;
        this.shangpin_shuliang = shangpin_shuliang;
        this.danjia = danjia;
        this.suolvtu = suolvtu;
        this.kuaidifei = kuaidifei;
        this.kucun = kucun;
        this.position = position;
        this.p_position = p_position;
        this.shangpin_leixing = leixing;
        this.isHuoDongShangPin = "否";
        this.diKouLv = "";
    }

    public ShangpinBean(String id, String shangpin_id, String shangpin_mingcheng, String shangpin_guige, int shangpin_shuliang, double danjia, String suolvtu, double kuaidifei,
                        String kucun, int position, int p_position, String leixing, String isHuoDongShangPin, String diKouLv) {
        this.id = id;
        this.shangpin_id = shangpin_id;
        this.shangpin_mingcheng = shangpin_mingcheng;
        this.shangpin_guige = shangpin_guige;
        this.shangpin_shuliang = shangpin_shuliang;
        this.danjia = danjia;
        this.suolvtu = suolvtu;
        this.kuaidifei = kuaidifei;
        this.kucun = kucun;
        this.position = position;
        this.p_position = p_position;
        this.shangpin_leixing = leixing;
        this.isHuoDongShangPin = isHuoDongShangPin;
        this.diKouLv = diKouLv;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getShangpin_id() {
        return shangpin_id;
    }

    public void setShangpin_id(String shangpin_id) {
        this.shangpin_id = shangpin_id;
    }

    public String getShangpin_mingcheng() {
        return shangpin_mingcheng;
    }

    public void setShangpin_mingcheng(String shangpin_mingcheng) {
        this.shangpin_mingcheng = shangpin_mingcheng;
    }

    public String getShangpin_guige() {
        return shangpin_guige;
    }

    public void setShangpin_guige(String shangpin_guige) {
        this.shangpin_guige = shangpin_guige;
    }

    public int getShangpin_shuliang() {
        return shangpin_shuliang;
    }

    public void setShangpin_shuliang(int shangpin_shuliang) {
        this.shangpin_shuliang = shangpin_shuliang;
    }

    public double getDanjia() {
        return danjia;
    }

    public void setDanjia(double danjia) {
        this.danjia = danjia;
    }

    public String getSuolvtu() {
        return suolvtu;
    }

    public void setSuolvtu(String suolvtu) {
        this.suolvtu = suolvtu;
    }

    public double getKuaidifei() {
        return kuaidifei;
    }

    public void setKuaidifei(double kuaidifei) {
        this.kuaidifei = kuaidifei;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getP_position() {
        return p_position;
    }

    public void setP_position(int p_position) {
        this.p_position = p_position;
    }

    public String getShangpin_leixing() {
        return shangpin_leixing;
    }

    public void setShangpin_leixing(String shangpin_leixing) {
        this.shangpin_leixing = shangpin_leixing;
    }

    public String getIsHuoDongShangPin() {
        return isHuoDongShangPin;
    }

    public void setIsHuoDongShangPin(String isHuoDongShangPin) {
        this.isHuoDongShangPin = isHuoDongShangPin;
    }

    public String getDiKouLv() {
        return diKouLv;
    }

    public void setDiKouLv(String diKouLv) {
        this.diKouLv = diKouLv;
    }

    @Override
    public String toString() {
        return "ShangpinBean{" +
                "id='" + id + '\'' +
                ", shangpin_id='" + shangpin_id + '\'' +
                ", shangpin_mingcheng='" + shangpin_mingcheng + '\'' +
                ", shangpin_guige='" + shangpin_guige + '\'' +
                ", shangpin_leixing='" + shangpin_leixing + '\'' +
                ", shangpin_shuliang=" + shangpin_shuliang +
                ", danjia=" + danjia +
                ", suolvtu='" + suolvtu + '\'' +
                ", kuaidifei=" + kuaidifei +
                ", kucun='" + kucun + '\'' +
                ", position=" + position +
                ", p_position=" + p_position +
                ", isHuoDongShangPin='" + isHuoDongShangPin + '\'' +
                ", diKouLv='" + diKouLv + '\'' +
                '}';
    }
}
