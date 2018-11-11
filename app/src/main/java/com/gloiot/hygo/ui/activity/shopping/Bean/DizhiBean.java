package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * Created by 13939 on 2016/10/18.
 */

/**
 * 收货地址实体
 * 修改者：詹志钦
 * 修改内容：变量名修改
 * 日期：2017/4/21
 */
public class DizhiBean implements Serializable{
    private String id;
    private String dizhi;
    private String shoujihao;
    private String shouhuoren;
    private String zhuangtai;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDizhi() {
        return dizhi;
    }

    public void setDizhi(String dizhi) {
        this.dizhi = dizhi;
    }

    public String getShoujihao() {
        return shoujihao;
    }

    public void setShoujihao(String shoujihao) {
        this.shoujihao = shoujihao;
    }

    public String getShouhuoren() {
        return shouhuoren;
    }

    public void setShouhuoren(String shouhuoren) {
        this.shouhuoren = shouhuoren;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public DizhiBean(String id, String dizhi, String shoujihao, String shouhuoren, String zhuangtai) {
        this.id = id;
        this.dizhi = dizhi;
        this.shoujihao = shoujihao;
        this.shouhuoren = shouhuoren;
        this.zhuangtai = zhuangtai;
    }

    @Override
    public String toString() {
        return "DizhiBean{" +
                "id='" + id + '\'' +
                ", dizhi='" + dizhi + '\'' +
                ", shoujihao='" + shoujihao + '\'' +
                ", shouhuoren='" + shouhuoren + '\'' +
                ", zhuangtai='" + zhuangtai + '\'' +
                '}';
    }
}
