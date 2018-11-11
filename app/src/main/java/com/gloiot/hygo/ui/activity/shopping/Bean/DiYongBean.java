package com.gloiot.hygo.ui.activity.shopping.Bean;

/**
 * Created by hygo03 on 2017/9/21.
 */

public class DiYongBean {

    /**
     "type":"积分账户",
     "account":"8543.37",
     "offset":"6.00"
     */
    private String type;
    private String account;
    private String offset;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "DiYongBean{" +
                "type='" + type + '\'' +
                ", account='" + account + '\'' +
                ", offset='" + offset + '\'' +
                '}';
    }
}
