package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.util.List;

/**
 * Created by hygo03 on 2016/11/23.
 * 创建者：莫小霞
 * 分类实体
 */

public class FenLeiShangPinBean{

    private List<FenLeiErJiBean> toBeShangPin;//待支付的子元素列表
    private String fenLeiYIJI;//待支付的夫元素列表

    public List<FenLeiErJiBean> getToBeShangPin() {
        return toBeShangPin;
    }

    public void setToBeShangPin(List<FenLeiErJiBean> toBeShangPin) {
        this.toBeShangPin = toBeShangPin;
    }

    public String getFenLeiYIJI() {
        return fenLeiYIJI;
    }

    public void setFenLeiYIJI(String fenLeiYIJI) {
        this.fenLeiYIJI = fenLeiYIJI;
    }

    @Override
    public String toString() {
        return "FenLeiShangPinBean{" +
                "toBeShangPin=" + toBeShangPin.toString() +
                ", fenLeiYIJI='" + fenLeiYIJI + '\'' +
                '}';
    }

}
