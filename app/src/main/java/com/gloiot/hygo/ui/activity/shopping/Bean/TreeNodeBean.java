package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * Created by hygo02 on 2016/10/20.
 * 创建者：詹志钦
 * 我的订单实体
 */

public class TreeNodeBean implements Serializable {
    private WoDeDingDan_DingDanInfo  DingdanInfo;

    public WoDeDingDan_ShangPinInfo [] ShangpinArray;

    public TreeNodeBean(int i){
        DingdanInfo = new WoDeDingDan_DingDanInfo();
        ShangpinArray = new WoDeDingDan_ShangPinInfo[i];
    }

    public WoDeDingDan_DingDanInfo getDingdanInfo() {
        return DingdanInfo;
    }

    public void setDingdanInfo(WoDeDingDan_DingDanInfo dingdanInfo) {
        DingdanInfo = dingdanInfo;
    }

    public WoDeDingDan_ShangPinInfo[] getShangpinArray() {
        return ShangpinArray;
    }

    public void setShangpinArray(WoDeDingDan_ShangPinInfo[] shangpinArray) {
        ShangpinArray = shangpinArray;
    }
}
