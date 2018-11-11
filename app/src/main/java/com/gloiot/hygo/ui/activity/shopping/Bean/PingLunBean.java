package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo02 on 2017/1/2.
 * 创建者：詹志钦
 * 详情详情——用户评价实体
 */

public class PingLunBean {
    private String Pinglun_Name;
    private String Pinglun_Neirong;
    private String Pinglun_Neirong_huifu;
    private String Pinglun_touxiang_url;
    private String Pinglun_time;
    private String Pinglun_guige;
    private String Pinglun_zhuijiapinglun;
    private String Pinglun_zhuijiahuifu;
    private String Pinglun_zhuijia_time;
    private List<String> Pinglun_list = new ArrayList<>(4);
    private List<String> Pinglun_zhuijia_list = new ArrayList<>(4);

    public String getPinglun_Name() {
        return Pinglun_Name;
    }

    public void setPinglun_Name(String pinglun_Name) {
        Pinglun_Name = pinglun_Name;
    }

    public String getPinglun_Neirong() {
        return Pinglun_Neirong;
    }

    public void setPinglun_Neirong(String pinglun_Neirong) {
        Pinglun_Neirong = pinglun_Neirong;
    }

    public String getPinglun_touxiang_url() {
        return Pinglun_touxiang_url;
    }

    public void setPinglun_touxiang_url(String pinglun_touxiang_url) {
        Pinglun_touxiang_url = pinglun_touxiang_url;
    }

    public String getPinglun_time() {
        return Pinglun_time;
    }

    public void setPinglun_time(String pinglun_time) {
        Pinglun_time = pinglun_time;
    }

    public List<String> getPinglun_list() {
        return Pinglun_list;
    }

    public String getPinglun_Neirong_huifu() {
        return Pinglun_Neirong_huifu;
    }

    public void setPinglun_Neirong_huifu(String pinglun_Neirong_huifu) {
        Pinglun_Neirong_huifu = pinglun_Neirong_huifu;
    }

    public String getPinglun_guige() {
        return Pinglun_guige;
    }

    public void setPinglun_guige(String pinglun_guige) {
        Pinglun_guige = pinglun_guige;
    }

    public String getPinglun_zhuijiapinglun() {
        return Pinglun_zhuijiapinglun;
    }

    public void setPinglun_zhuijiapinglun(String pinglun_zhuijiapinglun) {
        Pinglun_zhuijiapinglun = pinglun_zhuijiapinglun;
    }

    public String getPinglun_zhuijiahuifu() {
        return Pinglun_zhuijiahuifu;
    }

    public void setPinglun_zhuijiahuifu(String pinglun_zhuijiahuifu) {
        Pinglun_zhuijiahuifu = pinglun_zhuijiahuifu;
    }

    public String getPinglun_zhuijia_time() {
        return Pinglun_zhuijia_time;
    }

    public void setPinglun_zhuijia_time(String pinglun_zhuijiahuifu_time) {
        Pinglun_zhuijia_time = pinglun_zhuijiahuifu_time;
    }

    public List<String> getPinglun_zhuijia_list() {
        return Pinglun_zhuijia_list;
    }

    public void setPinglun_zhuijia_list(List<String> pinglun_zhuijia_list) {
        Pinglun_zhuijia_list = pinglun_zhuijia_list;
    }

    public void setPinglun_list(List<String> pinglun_list) {
        Pinglun_list = pinglun_list;
    }

    @Override
    public String toString() {
        return "PingLunBean{" +
                "Pinglun_Name='" + Pinglun_Name + '\'' +
                ", Pinglun_Neirong='" + Pinglun_Neirong + '\'' +
                ", Pinglun_Neirong_huifu='" + Pinglun_Neirong_huifu + '\'' +
                ", Pinglun_touxiang_url='" + Pinglun_touxiang_url + '\'' +
                ", Pinglun_time='" + Pinglun_time + '\'' +
                ", Pinglun_guige='" + Pinglun_guige + '\'' +
                ", Pinglun_zhuijiapinglun='" + Pinglun_zhuijiapinglun + '\'' +
                ", Pinglun_zhuijiahuifu='" + Pinglun_zhuijiahuifu + '\'' +
                ", Pinglun_zhuijiahuifu_time='" + Pinglun_zhuijia_time + '\'' +
                ", Pinglun_list=" + Pinglun_list +
                '}';
    }
}
