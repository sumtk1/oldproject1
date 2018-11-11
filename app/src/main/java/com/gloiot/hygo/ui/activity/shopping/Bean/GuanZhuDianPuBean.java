package com.gloiot.hygo.ui.activity.shopping.Bean;

/**
 * Created by hygo03 on 2017/8/23.
 */

public class GuanZhuDianPuBean {

    private String dianpu_id;
    private String dianpu_name;
    private String dianpu_touxiang;

    public String getDianpu_name() {
        return dianpu_name;
    }

    public void setDianpu_name(String dianpu_name) {
        this.dianpu_name = dianpu_name;
    }

    public String getDianpu_touxiang() {
        return dianpu_touxiang;
    }

    public void setDianpu_touxiang(String dianpu_touxiang) {
        this.dianpu_touxiang = dianpu_touxiang;
    }

    public String getDianpu_id() {
        return dianpu_id;
    }

    public void setDianpu_id(String dianpu_id) {
        this.dianpu_id = dianpu_id;
    }

    @Override
    public String toString() {
        return "GuanZhuDianPuBean{" +
                "dianpu_name='" + dianpu_name + '\'' +
                ", dianpu_touxiang='" + dianpu_touxiang + '\'' +
                '}';
    }
}
