package com.gloiot.hygo.ui.activity.shopping.wodedingdan;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.kefu.SelectKeFuActivity;
import com.gloiot.hygo.ui.activity.my.shouhou.ShouHouChexiaoActivity;
import com.gloiot.hygo.ui.activity.my.shouhou.ShouHouFailActivity;
import com.gloiot.hygo.ui.activity.my.shouhou.ShouHouFanKuiActivity;
import com.gloiot.hygo.ui.activity.my.shouhou.ShouHouSuccessActivity;
import com.gloiot.hygo.ui.activity.my.shouhou.ShouHouTuiHuoActivity;
import com.gloiot.hygo.ui.activity.my.shouhou.TuiKuanXiangQingActivity;
import com.gloiot.hygo.ui.activity.my.shouhou.TuikuanOrTuihuo;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.PingjiaFaBiaoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.WoDeDingDan_ShangPinInfo;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.ui.activity.web.WebToPayManager;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class DingdanXiangqingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_dingdan_xiangqing_name)
    TextView tv_dingdan_xiangqing_name;
    @Bind(R.id.tv_dingdan_xiangqing_phone)
    TextView tv_dingdan_xiangqing_phone;
    @Bind(R.id.tv_dingdan_xiangqing_dizhi)
    TextView tv_dingdan_xiangqing_dizhi;

    @Bind(R.id.rv_daifukuang)
    ListView listView;
    @Bind(R.id.tv_dingdan_xiangqing_yunfei)
    TextView tv_dingdan_xiangqing_yunfei;
    @Bind(R.id.rl_jifendikou)
    RelativeLayout rl_jifendikou;
    @Bind(R.id.tv_dingdan_xiangqing_jifendikou_leixing)
    TextView tv_dingdan_xiangqing_jifendikou_leixing;
    @Bind(R.id.tv_dingdan_xiangqing_jifendikou)
    TextView tv_dingdan_xiangqing_jifendikou;
    @Bind(R.id.tv_dingdan_xiangqing_heji)
    TextView tv_dingdan_xiangqing_heji;

    @Bind(R.id.tv_dingdan_xiangqing_bianhao)
    TextView tv_dingdan_xiangqing_bianhao;

    @Bind(R.id.tv_dingdan_xiangqing_zhifu_fangshi)
    TextView tv_dingdan_xiangqing_zhifu_fangshi;
    @Bind(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;

    @Bind(R.id.tv_dingdan_xiangqing_chuangjian_shijian)
    TextView tv_dingdan_xiangqing_chuangjian_shijian;

    @Bind(R.id.tv_dingdan_xiangqing_fukuan_shijian)
    TextView tv_dingdan_xiangqing_fukuan_shijian;
    @Bind(R.id.relativeLayout4)
    RelativeLayout relativeLayout4;
    @Bind(R.id.tv_dingdan_xiangqing_fukuan_xxjy)
    TextView tv_dingdan_xiangqing_fukuan_xxjy;
    @Bind(R.id.relativeLayout5)
    RelativeLayout relativeLayout5;
    @Bind(R.id.tv_dingdanxiangqing_daojishi)
    TextView tv_dingdanxiangqing_daojishi;
    @Bind(R.id.iv_item_dingdanxiangqing_button1)
    Button iv_item_dingdanxiangqing_button1;
    @Bind(R.id.iv_item_dingdanxiangqing_button2)
    Button iv_item_dingdanxiangqing_button2;
    @Bind(R.id.rl_dingdanxiangqing_bottom)
    RelativeLayout rl_dingdanxiangqing_bottom;
    @Bind(R.id.rl_dingdanxiangqing)
    RelativeLayout rl_dingdanxiangqing;
    @Bind(R.id.tv_dingdan_xiangqing_youhuiquan)
    TextView tvDingdanXiangqingYouhuiquan;
    @Bind(R.id.tv_dingdan_xiangqing_beizhu)
    TextView tv_dingdan_xiangqing_beizhu;
    @Bind(R.id.rl_youhuiquan)
    RelativeLayout rlYouhuiquan;

    //售后订单详情新加
    @Bind(R.id.rl_shouhou_dingdanxiangqing_shouhouzhuangtai)
    RelativeLayout rl_shouhou_dingdanxiangqing_shouhouzhuangtai;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_zhuangtai)
    TextView tvShouhouDingdanxiangqingZhuangtai;
    @Bind(R.id.btn_shouhou_dingdanxiangqing_01)
    Button btnShouhouDingdanxiangqing01;
    @Bind(R.id.btn_shouhou_dingdanxiangqing_02)
    Button btnShouhouDingdanxiangqing02;
    @Bind(R.id.btn_shouhou_dingdanxiangqing_03)
    Button btnShouhouDingdanxiangqing03;
    @Bind(R.id.btn_shouhou_dingdanxiangqing_shenqingshouhou)
    Button btnShouhouDingdanxiangqingShenqingshouhou;
    @Bind(R.id.ll_shouhou_dingdanxiangqing_shouhoushenqing)
    LinearLayout llShouhouDingdanxiangqingShouhoushenqing;
    @Bind(R.id.rl_dingdanxiangqing_bottom02)
    RelativeLayout rl_dingdanxiangqing_bottom02;
    @Bind(R.id.iv_item_dingdanxiangqing_button)
    TextView iv_item_dingdanxiangqing_button;


    private Context mContext;
    private String zhuangtai, id, dingDanLeiXing, shangPinArray;
    private String shouHouZhuangtai, shouHouLeiBie, isCheXiao;
    private String shangpinID, xiaoshouID;

    private List<WoDeDingDan_ShangPinInfo> list = new ArrayList<>();
    private CommonAdapter commonAdapter;

    private String dingdan_id = null;

    private String pingjiaLeibei;

    @Override
    public int initResource() {
        return R.layout.activity_dingdan_xiangqing;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "订单详情");

        mContext = this;
        zhuangtai = getIntent().getStringExtra("订单状态");
        Log.e("zhuangtai", zhuangtai);
        id = getIntent().getStringExtra("订单id");
        dingDanLeiXing = getIntent().getStringExtra("订单类型");
        shangPinArray = getIntent().getStringExtra("订单销售id");
        shouHouZhuangtai = getIntent().getExtras().getString("售后状态", "");
        shouHouLeiBie = getIntent().getExtras().getString("售后类别", "");
        isCheXiao = getIntent().getExtras().getString("是否撤销", "否");

        requestHandleArrayList.add(requestAction.shop_c_ordersInfo(
                this, zhuangtai, id, shangPinArray));

        commonAdapter = new CommonAdapter<WoDeDingDan_ShangPinInfo>(this, R.layout.item_dingdan_xiangqing_shangpin, list) {
            @Override
            public void convert(ViewHolder holder, final WoDeDingDan_ShangPinInfo woDeDingDan_shangPinInfo) {
                PictureUtlis.loadImageViewHolder(mContext, woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_tupian_url(), R.drawable.default_image, (ImageView) holder.getView(R.id.iv_item_dingdan_tubiao));
                holder.setText(R.id.tv_item_dingdan_name, woDeDingDan_shangPinInfo.getTv_item_dingdanxiangqing_title());
                holder.setText(R.id.tv_item_dingdan_guige, woDeDingDan_shangPinInfo.getTv_item_dingdanxiangqing_dingdanyanse());
                holder.setText(R.id.tv_item_dingdan_price, woDeDingDan_shangPinInfo.getTv_item_dingdanxiangqing_danjia());
                holder.setText(R.id.tv_item_dingdan_shuliang, "×" + woDeDingDan_shangPinInfo.getTv_item_dingdanxiangqing_shuliang());

                CommonUtlis.setImageTitle(mContext, woDeDingDan_shangPinInfo.getShangPinType(), woDeDingDan_shangPinInfo.getTv_item_dingdanxiangqing_title(), (TextView) holder.getView(R.id.tv_item_dingdan_name));

                ImageView iv_item_xiangqing_zhuangtai = holder.getView(R.id.tv_item_dingdan_shangpin_zhuangtai);
                Button iv_item_dingdan_button1 = holder.getView(R.id.iv_item_dingdan_button1);

                if ("全部".equals(dingDanLeiXing) && list.size() > 1) {
                    if ("买家已付款".equals(woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_shangpinZhuangTai())) {
                        iv_item_xiangqing_zhuangtai.setVisibility(View.VISIBLE);
                        iv_item_xiangqing_zhuangtai.setImageResource(R.mipmap.ic_shangpinzhuangtai_daifahuo);
                    } else if ("卖家已发货".equals(woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_shangpinZhuangTai())) {
                        iv_item_xiangqing_zhuangtai.setVisibility(View.VISIBLE);
                        iv_item_xiangqing_zhuangtai.setImageResource(R.mipmap.ic_shangpinzhuangtai_yifahuo);
                    }
                }

                if ("是".equals(woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_wuLiuZhuangTai())) {
                    iv_item_dingdan_button1.setVisibility(View.VISIBLE);
                    iv_item_dingdan_button1.setText("查看物流");
                } else {
                    iv_item_dingdan_button1.setVisibility(View.GONE);
                }
                if ("售后订单详情_申请售后".equals(dingDanLeiXing)
                        || "售后订单详情_申请记录".equals(dingDanLeiXing)) {
                    iv_item_dingdan_button1.setVisibility(View.GONE);
                }


                iv_item_dingdan_button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        PingjiaFaBiaoBean pingjiaFaBiaoBean;
                        switch (((TextView) view).getText().toString()) {
                            case "查看物流":
                                intent = new Intent(DingdanXiangqingActivity.this, ChakanWuliuActicity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("商品id", woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_shangpinId());
                                intent.putExtra("订单销售id", woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_id());
                                //订单详情为售后进入的订单详情时传物流类型到ChakanWuliuActicity
                                if ("售后订单详情_申请售后".equals(dingDanLeiXing)
                                        || "售后订单详情_申请记录".equals(dingDanLeiXing)) {
                                    intent.putExtra("物流类型", "售后");
                                }
                                startActivity(intent);
                                break;
                        }

                    }
                });

                LinearLayout ll_item_dingdan_shangpin = holder.getView(R.id.ll_item_dingdan_shangpin);
                ll_item_dingdan_shangpin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DingdanXiangqingActivity.this, ShangPinXiangQingActivity.class);
                        intent.putExtra("id", woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_shangpinId());
                        startActivity(intent);
                    }
                });
            }
        };
        listView.setAdapter(commonAdapter);
        setListViewHeightBasedOnChildren(listView);

        if ("等待买家付款".equals(zhuangtai)) {
            rl_dingdanxiangqing.setVisibility(View.VISIBLE);
            rl_dingdanxiangqing_bottom02.setVisibility(View.GONE);
            rl_dingdanxiangqing_bottom.setVisibility(View.VISIBLE);
        } else if ("卖家已发货".equals(zhuangtai)) {
            rl_dingdanxiangqing.setVisibility(View.VISIBLE);
            rl_dingdanxiangqing_bottom.setVisibility(View.GONE);
            rl_dingdanxiangqing_bottom02.setVisibility(View.VISIBLE);
            iv_item_dingdanxiangqing_button.setText("确认收货");
        }

        if ("卖家已发货".equals(zhuangtai) || "买家已付款".equals(zhuangtai)
                || "交易完成".equals(zhuangtai) || "交易关闭".equals(zhuangtai) || "".equals(zhuangtai)) {
            relativeLayout2.setVisibility(View.VISIBLE);
            relativeLayout4.setVisibility(View.VISIBLE);
        }

        //售后进来的订单详情不能确认收货
        if ("售后订单详情_申请售后".equals(dingDanLeiXing)) {
            rl_dingdanxiangqing.setVisibility(View.GONE);
            rl_dingdanxiangqing_bottom02.setVisibility(View.GONE);
            llShouhouDingdanxiangqingShouhoushenqing.setVisibility(View.VISIBLE);
        } else if ("售后订单详情_申请记录".equals(dingDanLeiXing)) {
            rl_dingdanxiangqing.setVisibility(View.GONE);
            rl_dingdanxiangqing_bottom02.setVisibility(View.GONE);
        }

        //申请记录进来的有红色的显示模块
        if ("售后订单详情_申请记录".equals(dingDanLeiXing)) {
            rl_shouhou_dingdanxiangqing_shouhouzhuangtai.setVisibility(View.VISIBLE);
            //红色部分售后状态
            tvShouhouDingdanxiangqingZhuangtai.setText(shouHouZhuangtai);

            if ("退款".equals(shouHouLeiBie)) {
                switch (shouHouZhuangtai) {
                    case "等待商家处理退款申请":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("撤销申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "商家已同意退款":
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "商家拒绝退款申请":
                        btnShouhouDingdanxiangqing01.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing01.setText("客服介入");
                        btnShouhouDingdanxiangqing02.setText("再次申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "商家未处理":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("再次申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "已撤销退款申请":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("再次申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    default:
                        break;
                }
            } else if ("退货".equals(shouHouLeiBie)) {
                switch (shouHouZhuangtai) {
                    case "等待商家处理退货":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("撤销申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "商家未处理":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("再次申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "商家已同意退货申请":
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "用户超时未处理":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("再次申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "商家拒绝退货申请":
                        btnShouhouDingdanxiangqing01.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing01.setText("客服介入");
                        btnShouhouDingdanxiangqing02.setText("再次申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "等待商家确认并退款":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("撤销申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "商家已同意退款":
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    case "已撤销退货申请":
                        btnShouhouDingdanxiangqing02.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing03.setVisibility(View.VISIBLE);
                        btnShouhouDingdanxiangqing02.setText("再次申请");
                        btnShouhouDingdanxiangqing03.setText("查看详情");
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        Log.e("jsonObject", jsonObject.toString());
        Intent intent;
        switch (requestTag) {
            case RequestAction.TAG_SHOP_C_ORDERSINFO:
                if ("成功".equals(jsonObject.getString("状态"))) {
                    if (JSONUtlis.getString(jsonObject, "支付方式").length() > 0) {
                        relativeLayout2.setVisibility(View.VISIBLE);
                        tv_dingdan_xiangqing_zhifu_fangshi.setText(jsonObject.getString("支付方式"));
                    } else {
                        relativeLayout2.setVisibility(View.GONE);
                    }
                    if (JSONUtlis.getString(jsonObject, "付款时间").length() > 0) {
                        relativeLayout4.setVisibility(View.VISIBLE);
                        tv_dingdan_xiangqing_fukuan_shijian.setText(jsonObject.getString("付款时间"));
                    } else {
                        relativeLayout4.setVisibility(View.GONE);
                    }
                    if ("".equals(JSONUtlis.getString(jsonObject, "收货码"))) {
                        relativeLayout5.setVisibility(View.GONE);
                    } else {
                        relativeLayout5.setVisibility(View.VISIBLE);
                        tv_dingdan_xiangqing_fukuan_xxjy.setText(jsonObject.getString("收货码"));
                    }
                    if ("".equals(JSONUtlis.getString(jsonObject, "买家留言"))) {
                        tv_dingdan_xiangqing_beizhu.setText("无");
                    } else {
                        tv_dingdan_xiangqing_beizhu.setText(JSONUtlis.getString(jsonObject, "买家留言"));
                    }
                    if (!"".equals(JSONUtlis.getString(jsonObject, "抵扣类型"))) {
                        rl_jifendikou.setVisibility(View.VISIBLE);
                        tv_dingdan_xiangqing_jifendikou_leixing.setText(JSONUtlis.getString(jsonObject, "抵扣类型") + "抵扣");
                        tv_dingdan_xiangqing_jifendikou.setText("¥" + JSONUtlis.getString(jsonObject, "抵扣金额"));
                    } else {
                        rl_jifendikou.setVisibility(View.GONE);
                    }

                    //显示优惠券
                    if (!"0.00".equals(JSONUtlis.getString(jsonObject, "优惠额", "0.00"))) {
                        rlYouhuiquan.setVisibility(View.VISIBLE);
                        tvDingdanXiangqingYouhuiquan.setText("¥-" + jsonObject.getString("优惠额"));
                    } else {
                        rlYouhuiquan.setVisibility(View.GONE);
                    }


                    tv_dingdan_xiangqing_name.setText(jsonObject.getString("收货人"));
                    tv_dingdan_xiangqing_phone.setText(jsonObject.getString("收货人手机号"));
                    tv_dingdan_xiangqing_dizhi.setText(jsonObject.getString("收货地址"));

                    tv_dingdan_xiangqing_bianhao.setText(jsonObject.getString("订单id"));
                    dingdan_id = jsonObject.getString("订单id");
                    tv_dingdan_xiangqing_chuangjian_shijian.setText(jsonObject.getString("创建时间"));

                    tv_dingdan_xiangqing_yunfei.setText("¥" + jsonObject.getString("快递费"));
                    tv_dingdan_xiangqing_heji.setText("¥" + jsonObject.getString("合计"));


                    if ("交易完成".equals(zhuangtai)) {
                        //添加评价按钮
                        pingjiaLeibei = JSONUtlis.getString(jsonObject, "评价类别");
                        Log.e("评价", pingjiaLeibei);
                        if (!("售后订单详情_申请售后".equals(dingDanLeiXing)
                                || "售后订单详情_申请记录".equals(dingDanLeiXing))) {
                            if ("不可评论".equals(pingjiaLeibei)) {
                                rl_dingdanxiangqing.setVisibility(View.GONE);
                                rl_dingdanxiangqing_bottom.setVisibility(View.GONE);
                                rl_dingdanxiangqing_bottom02.setVisibility(View.GONE);
                            } else if ("评论".equals(pingjiaLeibei)) {
                                rl_dingdanxiangqing.setVisibility(View.VISIBLE);
                                rl_dingdanxiangqing_bottom.setVisibility(View.GONE);
                                rl_dingdanxiangqing_bottom02.setVisibility(View.VISIBLE);
                                iv_item_dingdanxiangqing_button.setText("评价");
                                pingjiaLeibei = "评价";
                            } else if ("追评".equals(pingjiaLeibei)) {
                                rl_dingdanxiangqing.setVisibility(View.VISIBLE);
                                rl_dingdanxiangqing_bottom.setVisibility(View.GONE);
                                rl_dingdanxiangqing_bottom02.setVisibility(View.VISIBLE);
                                iv_item_dingdanxiangqing_button.setText("追加评价");
                                pingjiaLeibei = "追加评价";
                            }
                        }

                    }

                    try {
                        shijianchuli(jsonObject.getString("创建时间"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    JSONArray shangPinArray = jsonObject.getJSONArray("商品");
                    WoDeDingDan_ShangPinInfo woDeDingDan_shangPinInfo;
                    JSONObject shangPinObj;
                    for (int i = 0; i < shangPinArray.length(); i++) {
                        shangPinObj = shangPinArray.getJSONObject(i);
                        woDeDingDan_shangPinInfo = new WoDeDingDan_ShangPinInfo();
                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_shangpinId(JSONUtlis.getString(shangPinObj, "商品id"));
                        shangpinID = JSONUtlis.getString(shangPinObj, "商品id");
                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_shuliang(JSONUtlis.getString(shangPinObj, "商品数量"));
                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_zhongleixiangxi(JSONUtlis.getString(shangPinObj, "种类详细"));
                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_pingJiaZhuangTai(JSONUtlis.getString(shangPinObj, "评价类别"));
                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_wuLiuZhuangTai(JSONUtlis.getString(shangPinObj, "物流状态"));
                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_tupian_url(JSONUtlis.getString(shangPinObj, "缩略图"));
                        woDeDingDan_shangPinInfo.setShangPinType(JSONUtlis.getString(shangPinObj, "类型"));
                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_title(JSONUtlis.getString(shangPinObj, "商品名称"));
                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_shangpinZhuangTai(JSONUtlis.getString(shangPinObj, "状态"));
                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_danjia("¥" + JSONUtlis.getString(shangPinObj, "价格"));
                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_id(JSONUtlis.getString(shangPinObj, "id"));
                        xiaoshouID = JSONUtlis.getString(shangPinObj, "id");


                        list.add(woDeDingDan_shangPinInfo);
                    }
                    setListViewHeightBasedOnChildren(listView);
                    commonAdapter.notifyDataSetChanged();
                }
                break;
            case RequestAction.TAG_DINGDANCAOZUO_QUXIAODINGDAN:
                if ("成功".equals(jsonObject.getString("状态"))) {
                    MyToast.getInstance().showToast(mContext, "取消订单" + jsonObject.getString("状态"), true);
                    finish();
                }
                break;

            case RequestAction.TAG_SHOP_REPAY:
                if ("成功".equals(jsonObject.getString("状态"))) {
                    if ("是".equals(preferences.getString(ConstantUtlis.SP_MYPWD, ""))) {
                        // 跳转收银台
                        startActivity(WebToPayManager.toCashier(preferences, mContext, jsonObject));
                        finish();
                    } else {
                        DialogUtlis.oneBtnNormal(getmDialog(), "您还未设置支付密码！", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, WangjimimaActivity.class);
                                intent.putExtra("forgetpwd", "设置支付密码");
                                startActivity(intent);
                                dismissDialog();
                            }
                        });
                    }
                }
                break;

            case RequestAction.TAG_SHOP_QUERENSHOUHUO:
                MyToast.getInstance().showToast(mContext, "确认收货" + jsonObject.getString("状态"), true);
                rl_dingdanxiangqing_bottom02.setVisibility(View.GONE);
                rl_dingdanxiangqing.setVisibility(View.GONE);
                break;

            //退款撤销
            case RequestAction.TAG_RFEDIT:
                intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                intent.putExtra("订单id", dingdan_id);
                intent.putExtra("商品id", shangpinID);
                intent.putExtra("id", xiaoshouID);
                intent.putExtra("类别", "退款");
                startActivity(intent);
                finish();
                break;

            //退货撤销
            case RequestAction.TAG_RFGOODSEDIT:
                intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                intent.putExtra("订单id", dingdan_id);
                intent.putExtra("商品id", shangpinID);
                intent.putExtra("id", xiaoshouID);
                intent.putExtra("类别", "退货");
                startActivity(intent);
                finish();
                break;
        }

    }

    /**
     * 动态改变listview高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    @OnClick({R.id.iv_item_dingdanxiangqing_button1, R.id.iv_item_dingdanxiangqing_button2, R.id.tv_dingdan_xiangqing_fuzhi, R.id.iv_item_dingdanxiangqing_button,
            R.id.btn_shouhou_dingdanxiangqing_01, R.id.btn_shouhou_dingdanxiangqing_02, R.id.btn_shouhou_dingdanxiangqing_03,
            R.id.btn_shouhou_dingdanxiangqing_shenqingshouhou})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //取消订单
            case R.id.iv_item_dingdanxiangqing_button1:
                DialogUtlis.twoBtnNormal(getmDialog(), "提示", "是否取消订单？", true, "否", "是",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                quxiaodingdan(id);
                            }
                        });
                break;

            //付款
            case R.id.iv_item_dingdanxiangqing_button2:
                requestHandleArrayList.add(requestAction.shop_repay(this, id));
                break;

            //复制
            case R.id.tv_dingdan_xiangqing_fuzhi:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tv_dingdan_xiangqing_bianhao.getText().toString().trim());
                MyToast.getInstance().showToast(DingdanXiangqingActivity.this, "已复制");
                break;

            case R.id.iv_item_dingdanxiangqing_button:
                if ("评价".equals(iv_item_dingdanxiangqing_button.getText().toString()) || "追加评价".equals(iv_item_dingdanxiangqing_button.getText().toString())) {
                    pingJia(iv_item_dingdanxiangqing_button.getText().toString());
                }
                if ("卖家已发货".equals(zhuangtai)) {
                    if (list.size() > 1) {
                        DialogUtlis.twoBtnNormal(getmDialog(), "提示", "是否已收到订单中所有商品（" + list.size() + "件）？", true, "否", "是",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                        requestHandleArrayList.add(requestAction.queRenShouHuo(DingdanXiangqingActivity.this, id));
                                    }
                                });
                    } else {
                        DialogUtlis.twoBtnNormal(getmDialog(), "提示", "是否确认收货？", true, "否", "是",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                        requestHandleArrayList.add(requestAction.queRenShouHuo(DingdanXiangqingActivity.this, id));
                                    }
                                });
                    }
                }
                break;

            //往下是售后进来的订单详情对应的按钮
            case R.id.btn_shouhou_dingdanxiangqing_01:
                Intent it = new Intent(mContext, SelectKeFuActivity.class);
                it.putExtra("客服组名称", "商城客服");
                startActivity(it);
                break;

            case R.id.btn_shouhou_dingdanxiangqing_02:
                if ("退款".equals(shouHouLeiBie)) {
                    switch (shouHouZhuangtai) {
                        case "等待商家处理退款申请":
                            if ("是".equals(isCheXiao)) {
                                DialogUtlis.twoBtnNormal(getmDialog(), "只有一次撤销申请机会，是否撤销申请？", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();

                                        requestHandleArrayList.add(requestAction.shop_rf_edit(DingdanXiangqingActivity.this,
                                                dingdan_id, shangpinID, xiaoshouID, null, null, null, null, null, "0"));
                                    }
                                });
                            } else if ("否".equals(isCheXiao)) {
                                DialogUtlis.oneBtnNormal(getmDialog(), "同一订单只允许撤销申请一次");
                            }


                            break;
                        case "商家拒绝退款申请":
                        case "商家未处理":
                        case "已撤销退款申请":
                            Intent intent = new Intent(mContext, TuikuanOrTuihuo.class);
                            intent.putExtra("订单id", dingdan_id);
                            intent.putExtra("商品id", shangpinID);
                            intent.putExtra("id", xiaoshouID);
                            intent.putExtra("订单状态", zhuangtai);
                            mContext.startActivity(intent);
                            finish();
                            break;
                        default:
                            break;
                    }
                } else if ("退货".equals(shouHouLeiBie)) {
                    switch (shouHouZhuangtai) {
                        case "等待商家处理退货":
                        case "等待商家确认并退款":
                            if ("是".equals(isCheXiao)) {

                                if ("等待商家确认收货".equals(isCheXiao)) {
                                    DialogUtlis.twoBtnNormal(getmDialog(), "你已退货给商家，是否撤销申请？", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dismissDialog();
                                            requestHandleArrayList.add(requestAction.shop_rf_goods_edit(DingdanXiangqingActivity.this,
                                                    dingdan_id, shangpinID, xiaoshouID, null, null, null, null, "0"));

                                        }
                                    });
                                } else {
                                    DialogUtlis.twoBtnNormal(getmDialog(), "只有一次撤销申请机会，是否撤销申请？", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dismissDialog();
                                            requestHandleArrayList.add(requestAction.shop_rf_goods_edit(DingdanXiangqingActivity.this,
                                                    dingdan_id, shangpinID, xiaoshouID, null, null, null, null, "0"));
                                        }
                                    });
                                }
                            } else if ("否".equals(isCheXiao)) {
                                DialogUtlis.oneBtnNormal(getmDialog(), "同一订单只允许撤销申请一次");
                            }
                            break;
                        case "商家未处理":
                        case "用户超时未处理":
                        case "商家拒绝退货申请":
                        case "已撤销退货申请":
                            Intent intent = new Intent(mContext, TuiKuanXiangQingActivity.class);
                            intent.putExtra("详情类型", shouHouLeiBie);
                            intent.putExtra("订单id", dingdan_id);
                            intent.putExtra("商品id", shangpinID);
                            intent.putExtra("id", xiaoshouID);
                            mContext.startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
                break;

            case R.id.btn_shouhou_dingdanxiangqing_03:
                Intent intent = null;
                switch (shouHouZhuangtai) {
                    case "等待商家处理退款申请":
                    case "等待商家处理退货申请":
                    case "等待商家处理退货":
                        intent = new Intent(mContext, TuiKuanXiangQingActivity.class);
                        intent.putExtra("详情类型", shouHouLeiBie);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        startActivity(intent);
                        break;
                    case "商家已同意退款":
                        intent = new Intent(mContext, ShouHouSuccessActivity.class);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        intent.putExtra("类别", shouHouLeiBie);
                        startActivity(intent);
                        break;
                    case "商家拒绝退款申请":
                    case "商家拒绝退货申请":
                        intent = new Intent(mContext, ShouHouFailActivity.class);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        intent.putExtra("申请售后", shouHouLeiBie);
                        intent.putExtra("订单状态", zhuangtai);
                        startActivity(intent);
                        break;
                    case "商家已同意退货申请":
                        intent = new Intent(mContext, ShouHouTuiHuoActivity.class);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        startActivity(intent);
                        break;
                    case "等待商家确认并退款":
                        intent = new Intent(mContext, TuiKuanXiangQingActivity.class);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        intent.putExtra("详情类型", "等待商家确认收货");
                        startActivity(intent);
                        break;
                    case "用户超时未处理":
                        intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        intent.putExtra("订单状态", zhuangtai);
                        intent.putExtra("类别", "用户未处理");
                        mContext.startActivity(intent);
                        break;
                    case "已撤销退款申请"://这两个就是退款关闭
                    case "已撤销退货申请":
                        intent = new Intent(mContext, ShouHouChexiaoActivity.class);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        intent.putExtra("订单状态", zhuangtai);
                        intent.putExtra("类别", shouHouLeiBie);
                        startActivity(intent);
                        break;
                    case "商家未处理":
                        intent = new Intent(mContext, ShouHouFanKuiActivity.class);
                        intent.putExtra("订单id", dingdan_id);
                        intent.putExtra("商品id", shangpinID);
                        intent.putExtra("id", xiaoshouID);
                        intent.putExtra("订单状态", zhuangtai);
                        intent.putExtra("类别", shouHouLeiBie);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                break;
            //申请售后按钮
            case R.id.btn_shouhou_dingdanxiangqing_shenqingshouhou:
                intent = new Intent(mContext, TuikuanOrTuihuo.class);
                intent.putExtra("订单id", dingdan_id);
                intent.putExtra("商品id", shangpinID);
                intent.putExtra("id", xiaoshouID);
                intent.putExtra("订单状态", zhuangtai);
                mContext.startActivity(intent);
                finish();
                break;
        }
    }

    public void pingJia(String pingjiaLeibei) {
        List<PingjiaFaBiaoBean> listPingJia = new ArrayList<>();
        PingjiaFaBiaoBean pingjiaFaBiaoBean;
        for (int i = 0; i < list.size(); i++) {
            pingjiaFaBiaoBean = new PingjiaFaBiaoBean();
            WoDeDingDan_ShangPinInfo woDeDingDan_shangPinInfo = list.get(i);
            pingjiaFaBiaoBean.setId(woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_id());
            pingjiaFaBiaoBean.setShangpin_id(woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_shangpinId());
            pingjiaFaBiaoBean.setShangpin_title(woDeDingDan_shangPinInfo.getTv_item_dingdanxiangqing_title());
            pingjiaFaBiaoBean.setShangpin_img_url(woDeDingDan_shangPinInfo.getIv_item_dingdanxiangqing_tupian_url());
            pingjiaFaBiaoBean.setShangpin_guigefenlei(woDeDingDan_shangPinInfo.getTv_item_dingdanxiangqing_zhongleixiangxi());
            pingjiaFaBiaoBean.setShangpin_leixing(woDeDingDan_shangPinInfo.getShangPinType());
            pingjiaFaBiaoBean.setShangpin_imgList(new ArrayList<String>(4));

            listPingJia.add(pingjiaFaBiaoBean);
        }
        Intent intent = new Intent(mContext, FaBiaoPingjiaActivity.class);
        intent.putExtra("商品信息", (Serializable) listPingJia);
        intent.putExtra("类型", pingjiaLeibei);
        startActivityForResult(intent, 1);
    }

    private void quxiaodingdan(String id) {
        requestHandleArrayList.add(requestAction.shop_wodedingdan_quxiao(this, id));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if ("追加评价".equals(data.getStringExtra("type"))) {
                        rl_dingdanxiangqing_bottom.setVisibility(View.GONE);
                        rl_dingdanxiangqing_bottom02.setVisibility(View.GONE);
                    } else {
                        rl_dingdanxiangqing_bottom.setVisibility(View.GONE);
                        rl_dingdanxiangqing_bottom02.setVisibility(View.VISIBLE);
                        iv_item_dingdanxiangqing_button.setText("追加评价");
                    }
                }
                break;
            default:
                break;
        }
    }

    //订单结束倒计时计算
    private void shijianchuli(String str) throws ParseException {
        Log.e("str", str);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long nowtime = System.currentTimeMillis();
        Date date = sdf.parse(str);
        long timeStemp = date.getTime() + 60 * 60 * 24 * 3 * 1000;

        long str1 = (timeStemp - nowtime) / (1000 * 24 * 60 * 60);
        long str2 = ((timeStemp - nowtime) % (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);

        Log.e("time", str1 + "---" + str2);

        if (str1 > -1 && str2 > -1)
            tv_dingdanxiangqing_daojishi.setText("剩" + str1 + "天" + str2 + "小时关闭");
        else tv_dingdanxiangqing_daojishi.setText("剩0天0小时关闭");
    }

}
