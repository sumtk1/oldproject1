package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.util.ArrayList;

/**
 * Created by hygo03 on 2017/9/21.
 */

public class DiYongYouHuiBean {

    private ArrayList<DiYongBean> diYong_list;

    public ArrayList<DiYongBean> getDiYong_list() {
        return diYong_list;
    }

    public void setDiYong_list(ArrayList<DiYongBean> diYong_list) {
        this.diYong_list = diYong_list;
    }

    @Override
    public String toString() {
        return "DiYongYouHuiBean{" +
                "diYong_list=" + diYong_list +
                '}';
    }
}
