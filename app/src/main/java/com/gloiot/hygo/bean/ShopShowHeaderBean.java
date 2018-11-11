package com.gloiot.hygo.bean;

import java.util.List;

/**
 * 时间：on 2017/12/2 09:48.
 * 作者：by hygo04
 * 功能：首页头部数据bean
 */

public class ShopShowHeaderBean {

    /**
     * 首页图片 :
     * 字体颜色 :
     * 活动专区 : http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017092511193770902.jpg
     * 活动专区图片链接 : http://121.201.67.222:18080/Activity/activityZone.xhtml
     * 全球购 : http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111241368950.png
     * 全球购条数 : 2
     * pages : [{"标题":"主页轮换","图片":"http://zyk-temp.oss-cn-shenzhen.aliyuncs.com/2017060210291593883.png","图片链接":"https://www.baidu.com/","商品id":"147","图片数量":"1"},{"标题":"主页轮换","图片":"http://zyk-temp.oss-cn-shenzhen.aliyuncs.com/2017060210302395750.png","图片链接":"https://www.baidu.com/","商品id":"148","图片数量":"4"}]
     * classify : [{"类别名称":"食品生鲜","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111203451749.png","商城图标":""},{"类别名称":"时尚服装","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111200711627.png","商城图标":""},{"类别名称":"日用百货","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111195778891.png","商城图标":""},{"类别名称":"个护化妆","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111194823784.png","商城图标":""},{"类别名称":"鞋靴箱包","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111193692538.png","商城图标":""},{"类别名称":"3C数码","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111190618130.png","商城图标":""},{"类别名称":"品牌授权","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111185768973.png","商城图标":""},{"类别名称":"全部分类","图标":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111180234872.png","商城图标":""}]
     * table : [{"标题":"今日特价","图片":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111262137569.png"},{"标题":"新品上市","图片":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111260392711.png"},{"标题":"自营专区","图片":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111253833598.png"},{"标题":"精选","图片":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111251692776.png"}]
     * pagesCount : 2
     * classifyCount : 8
     * tableCount : 4
     * 返现图 :
     * 返现图条数 : 0
     */

    private String 首页图片;
    private String 字体颜色;
    private String 活动专区;
    private String 活动专区图片链接;
    private String 全球购;
    private int 全球购条数;
    private int pagesCount;
    private int classifyCount;
    private int tableCount;
    private String 返现图;
    private int 返现图条数;
    private String 推荐商品图片;
    private int 推荐商品图片条数;
    private List<PagesBean> pages;
    private List<ClassifyBean> classify;
    private List<TableBean> table;

    public String get首页图片() {
        return 首页图片;
    }

    public void set首页图片(String 首页图片) {
        this.首页图片 = 首页图片;
    }

    public String get字体颜色() {
        return 字体颜色;
    }

    public void set字体颜色(String 字体颜色) {
        this.字体颜色 = 字体颜色;
    }

    public String get活动专区() {
        return 活动专区;
    }

    public void set活动专区(String 活动专区) {
        this.活动专区 = 活动专区;
    }

    public String get活动专区图片链接() {
        return 活动专区图片链接;
    }

    public void set活动专区图片链接(String 活动专区图片链接) {
        this.活动专区图片链接 = 活动专区图片链接;
    }

    public String get全球购() {
        return 全球购;
    }

    public void set全球购(String 全球购) {
        this.全球购 = 全球购;
    }

    public int get全球购条数() {
        return 全球购条数;
    }

    public void set全球购条数(int 全球购条数) {
        this.全球购条数 = 全球购条数;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public int getClassifyCount() {
        return classifyCount;
    }

    public void setClassifyCount(int classifyCount) {
        this.classifyCount = classifyCount;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public String get返现图() {
        return 返现图;
    }

    public void set返现图(String 返现图) {
        this.返现图 = 返现图;
    }

    public int get返现图条数() {
        return 返现图条数;
    }

    public void set返现图条数(int 返现图条数) {
        this.返现图条数 = 返现图条数;
    }

    public String get推荐商品图片() {
        return 推荐商品图片;
    }

    public void set推荐商品图片(String 推荐商品图片) {
        this.推荐商品图片 = 推荐商品图片;
    }

    public int get推荐商品图片条数() {
        return 推荐商品图片条数;
    }

    public void set推荐商品图片条数(int 推荐商品图片条数) {
        this.推荐商品图片条数 = 推荐商品图片条数;
    }

    public List<PagesBean> getPages() {
        return pages;
    }

    public void setPages(List<PagesBean> pages) {
        this.pages = pages;
    }

    public List<ClassifyBean> getClassify() {
        return classify;
    }

    public void setClassify(List<ClassifyBean> classify) {
        this.classify = classify;
    }

    public List<TableBean> getTable() {
        return table;
    }

    public void setTable(List<TableBean> table) {
        this.table = table;
    }

    public static class PagesBean {
        /**
         * 标题 : 主页轮换
         * 图片 : http://zyk-temp.oss-cn-shenzhen.aliyuncs.com/2017060210291593883.png
         * 图片链接 : https://www.baidu.com/
         * 商品id : 147
         * 图片数量 : 1
         */

        private String 图片;
        private String 图片链接;
        private String 商品id;

        public String get图片() {
            return 图片;
        }

        public void set图片(String 图片) {
            this.图片 = 图片;
        }

        public String get图片链接() {
            return 图片链接;
        }

        public void set图片链接(String 图片链接) {
            this.图片链接 = 图片链接;
        }

        public String get商品id() {
            return 商品id;
        }

        public void set商品id(String 商品id) {
            this.商品id = 商品id;
        }
    }

    public static class ClassifyBean {
        /**
         * 类别名称 : 食品生鲜
         * 图标 : http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111203451749.png
         * 商城图标 :
         */

        private String 类别名称;
        private String 图标;
        private String 图片链接;


        public String get类别名称() {
            return 类别名称;
        }

        public void set类别名称(String 类别名称) {
            this.类别名称 = 类别名称;
        }

        public String get图标() {
            return 图标;
        }

        public void set图标(String 图标) {
            this.图标 = 图标;
        }

        public String get图片链接() {
            return 图片链接;
        }

        public void set图片链接(String 图片链接) {
            this.图片链接 = 图片链接;
        }
    }

    public static class TableBean {
        /**
         * 标题 : 今日特价
         * 图片 : http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017073111262137569.png
         */

        private String 标题;
        private String 图片;

        public String get标题() {
            return 标题;
        }

        public void set标题(String 标题) {
            this.标题 = 标题;
        }

        public String get图片() {
            return 图片;
        }

        public void set图片(String 图片) {
            this.图片 = 图片;
        }
    }
}
