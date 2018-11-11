package com.gloiot.hygo.bean;

/**
 * SocialFragment消息bean
 * Created by hygo05 on 2017/4/14 17:32
 */
public class SocialMessage {

//    public static final int VIEW_TYPE_NORMAL = 1;
//    public static final int VIEW_TYPE_SYSTEM = 2;
//    public static final int VIEW_TYPE_APPLICATION = 3;
//    public static final int VIEW_TYPE_GROUP = 4;
//    public static final int VIEW_TYPE_WULIU = 5;


    private String userid;
    private int viewType;//消息类型，分为normal,system,application,group
    private String imageUrl;//图片url
    private String title;//消息标题
    private String content;//消息内容
    private String time;//消息接收时间
    private boolean isNoDisturb;//是否勿打扰
    private String noReadNum;//未读条数

    public SocialMessage(int viewType, String userid, String imageUrl, String title, String content, String time, boolean isNoDisturb, String noReadNum) {
        this.userid = userid;
        this.viewType = viewType;
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
        this.time = time;
        this.isNoDisturb = isNoDisturb;
        this.noReadNum = noReadNum;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isNoDisturb() {
        return isNoDisturb;
    }

    public void setNoDisturb(boolean noDisturb) {
        isNoDisturb = noDisturb;
    }

    public String getNoReadNum() {
        return noReadNum;
    }

    public void setNoReadNum(String noReadNum) {
        this.noReadNum = noReadNum;
    }
}
