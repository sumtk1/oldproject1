package com.gloiot.hygo.ui.activity.my.shouhou;

/**
 * Created by dwj on 2017/6/30
 * 申请售后的订单实体类
 */

public class ShouHouBean {
    private String dingdanId; //订单id
    private String zhuangtai;   //订单状态
    private String dianpuMing; //店铺名
    private String xiadanShijian; // 下单时间
    private String shangpinId; //商品id
    private String Id; //id
    private String shangpinMingchen;//商品名称
    private String shangpinShuliang;//商品数量
    private String suoLueTu; //缩略图
    private String leibie; //申请退款状态
    private String shouhouShuoming; // 售后说明:您的服务单已申请成功，待售后审核中"
    private String zhongleixiangxi;  //种类详细
    private String heji;   //合计
    private String dingdanZhuangtai; //订单状态
    private String isCheXiao; //是否撤销（是为可撤销，否不可撤销）

    public String getLeibie() {
        return leibie;
    }

    public void setLeibie(String leibie) {
        this.leibie = leibie;
    }

    public String getDingdanId() {
        return dingdanId;
    }

    public void setDingdanId(String dingdanId) {
        this.dingdanId = dingdanId;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String dingdanZhuangtai) {
        this.zhuangtai = dingdanZhuangtai;
    }

    public String getXiadanShijian() {
        return xiadanShijian;
    }

    public void setXiadanShijian(String xiadanShijian) {
        this.xiadanShijian = xiadanShijian;
    }


    public String getShangpinId() {
        return shangpinId;
    }

    public void setShangpinId(String shangpinId) {
        this.shangpinId = shangpinId;
    }

    public String getShangpinMingchen() {
        return shangpinMingchen;
    }

    public void setShangpinMingchen(String shangpinMingchen) {
        this.shangpinMingchen = shangpinMingchen;
    }

    public String getShangpinShuliang() {
        return shangpinShuliang;
    }

    public void setShangpinShuliang(String shangpinShuliang) {
        this.shangpinShuliang = shangpinShuliang;
    }

    public String getSuoLueTu() {
        return suoLueTu;
    }

    public void setSuoLueTu(String suoLueTu) {
        this.suoLueTu = suoLueTu;
    }


    public String getShouhouShuoming() {
        return shouhouShuoming;
    }

    public void setShouhouShuoming(String shouhouShuoming) {
        this.shouhouShuoming = shouhouShuoming;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDianpuMing() {
        return dianpuMing;
    }

    public void setDianpuMing(String dianpuMing) {
        this.dianpuMing = dianpuMing;
    }

    public String getZhongleixiangxi() {
        return zhongleixiangxi;
    }

    public void setZhongleixiangxi(String zhongleixiangxi) {
        this.zhongleixiangxi = zhongleixiangxi;
    }

    public String getHeji() {
        return heji;
    }

    public void setHeji(String heji) {
        this.heji = heji;
    }

    public String getDingdanZhuangtai() {
        return dingdanZhuangtai;
    }

    public void setDingdanZhuangtai(String dingdanZhuangtai) {
        this.dingdanZhuangtai = dingdanZhuangtai;
    }

    public String getIsCheXiao() {
        return isCheXiao;
    }

    public void setIsCheXiao(String isCheXiao) {
        this.isCheXiao = isCheXiao;
    }
}
