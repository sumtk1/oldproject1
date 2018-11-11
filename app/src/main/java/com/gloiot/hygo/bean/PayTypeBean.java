package com.gloiot.hygo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：Ljy on 2017/4/1.
 * 功能：bean——支付方式
 */


public class PayTypeBean implements Parcelable {

    private String name = "";
    private boolean isChecked;
    private String id = "";  //id只用于银行卡
    private String balance = "";//余额只用于本地支付方式

    public PayTypeBean() {
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean ischeck() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PayTypeBean{" +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeByte((byte) (isChecked ? 1 : 0));     //if isChecked == true, byte == 1
        dest.writeString(balance);
    }

    public static final Parcelable.Creator<PayTypeBean> CREATOR = new Creator<PayTypeBean>() {
        @Override
        public PayTypeBean createFromParcel(Parcel source) {
            return new PayTypeBean(source);
        }

        @Override
        public PayTypeBean[] newArray(int size) {
            return new PayTypeBean[size];
        }
    };

    public PayTypeBean(Parcel source) {
        name = source.readString();
        id = source.readString();
        isChecked = source.readByte() != 0;
        balance = source.readString();
    }

}
