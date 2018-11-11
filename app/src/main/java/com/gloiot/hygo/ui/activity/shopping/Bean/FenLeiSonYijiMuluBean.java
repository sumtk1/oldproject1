package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class FenLeiSonYijiMuluBean {
    private String id;
    private String name;
    private List<FeiLeiSonErjiMuluBean> erji;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<FeiLeiSonErjiMuluBean> getErji() {
        return erji;
    }
    public void setRrji(List<FeiLeiSonErjiMuluBean> erji) {
        this.erji = erji;
    }

    @Override
    public String toString() {
        return "FenLeiSonYijiMuluBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", erji=" + erji +
                '}';
    }
}
