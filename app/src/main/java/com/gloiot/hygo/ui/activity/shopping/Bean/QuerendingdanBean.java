package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13939 on 2016/10/17.
 * 确认订单实体
 */

public class QuerendingdanBean implements Serializable {
    private List<ShangpinBean> toBeZhifuDingdan;//待支付的子元素列表
    private GroupInfoBean groupInfoBean;//待支付的夫元素列表

    public List<ShangpinBean> getToBeZhifuDingdan() {
        return toBeZhifuDingdan;
    }

    public void setToBeZhifuDingdan(List<ShangpinBean> toBeZhifuDingdan) {
        this.toBeZhifuDingdan = toBeZhifuDingdan;
    }

    public GroupInfoBean getGroupInfoBean() {
        return groupInfoBean;
    }

    public void setGroupInfoBean(GroupInfoBean groupInfoBean) {
        this.groupInfoBean = groupInfoBean;
    }
}
