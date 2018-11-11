package com.gloiot.hygo.ui.activity.my.shouhou;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.WoDeDingDan_ShangPinInfo;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import butterknife.Bind;

public class ShouHouDingDanXiangQingActivity extends BaseActivity {

    @Bind(R.id.tv_shouhou_dingdanxiangqing_zhuangtai)
    TextView tvShouhouDingdanxiangqingZhuangtai;

    @Bind(R.id.btn_shouhou_dingdanxiangqing_01)
    Button btnShouhouDingdanxiangqing01;
    @Bind(R.id.btn_shouhou_dingdanxiangqing_02)
    Button btnShouhouDingdanxiangqing02;
    @Bind(R.id.btn_shouhou_dingdanxiangqing_03)
    Button btnShouhouDingdanxiangqing03;

    @Bind(R.id.tv_shouhou_dingdanxiangqing_shoujianren_xingming)
    TextView tvShouhouDingdanxiangqingShoujianrenXingming;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_shoujianren_dianhua)
    TextView tvShouhouDingdanxiangqingShoujianrenDianhua;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_shoujianren_dizhi)
    TextView tvShouhouDingdanxiangqingShoujianrenDizhi;

    @Bind(R.id.tv_shouhou_dingdanxiangqing_shangpin_img)
    ImageView tvShouhouDingdanxiangqingShangpinImg;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_shangpin_title)
    TextView tvShouhouDingdanxiangqingShangpinTitle;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_shangpin_guige)
    TextView tvShouhouDingdanxiangqingShangpinGuige;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_shangpin_shuliang)
    TextView tvShouhouDingdanxiangqingShangpinShuliang;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_shangpin_jiage)
    TextView tvShouhouDingdanxiangqingShangpinJiage;

    @Bind(R.id.btn_shouhou_dingdanxiangqing_shenqingshouhou)
    Button btnShouhouDingdanxiangqingShenqingshouhou;
    @Bind(R.id.ll_shouhou_dingdanxiangqing_shenqingshouhou)
    LinearLayout llShouhouDingdanxiangqingShenqingshouhou;

    @Bind(R.id.tv_shouhou_dingdanxiangqing_yunfei)
    TextView tvShouhouDingdanxiangqingYunfei;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_heji)
    TextView tvShouhouDingdanxiangqingHeji;

    @Bind(R.id.btn_shouhou_dingdanxiangqing_fuzhi)
    Button btnShouhouDingdanxiangqingFuzhi;

    @Bind(R.id.tv_shouhou_dingdanxiangqing_bianhao)
    TextView tvShouhouDingdanxiangqingBianhao;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_zhifufangshi)
    TextView tvShouhouDingdanxiangqingZhifufangshi;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_chuangjianshijian)
    TextView tvShouhouDingdanxiangqingChuangjianshijian;
    @Bind(R.id.tv_shouhou_dingdanxiangqing_fukuanshijian)
    TextView tvShouhouDingdanxiangqingFukuanshijian;

    private String zhuangtai = "", id = "", xiaoShouID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shouhou_dingdan_xiangqing;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "退款详情");

        zhuangtai = getIntent().getStringExtra("订单状态");
        id = getIntent().getStringExtra("订单id");
        xiaoShouID = getIntent().getStringExtra("订单销售id");

        JSONArray idArray = new JSONArray();
        JSONObject idObj = new JSONObject();
        try {
            idObj.put("订单销售id", xiaoShouID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        idArray.put(idObj);


        requestHandleArrayList.add(requestAction.shop_c_ordersInfo(
                this, zhuangtai, id, idArray.toString()));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        L.e("requestSuccess : " + requestTag, jsonObject.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOP_C_ORDERSINFO:
                if ("成功".equals(jsonObject.getString("状态"))) {
                        tvShouhouDingdanxiangqingZhifufangshi.setText(jsonObject.getString("支付方式"));
                    tvShouhouDingdanxiangqingFukuanshijian.setText(jsonObject.getString("付款时间"));

                    tvShouhouDingdanxiangqingBianhao.setText(jsonObject.getString("订单id"));
                    tvShouhouDingdanxiangqingChuangjianshijian.setText(jsonObject.getString("创建时间"));

                    tvShouhouDingdanxiangqingShoujianrenXingming.setText(jsonObject.getString("收货人"));
                    tvShouhouDingdanxiangqingShoujianrenDianhua.setText(jsonObject.getString("收货人手机号"));
                    tvShouhouDingdanxiangqingShoujianrenDizhi.setText(jsonObject.getString("收货地址"));

                    tvShouhouDingdanxiangqingYunfei.setText("¥" + jsonObject.getString("快递费"));
                    tvShouhouDingdanxiangqingHeji.setText("¥" + jsonObject.getString("合计"));


//                    JSONArray shangPinArray = jsonObject.getJSONArray("商品");
//                    WoDeDingDan_ShangPinInfo woDeDingDan_shangPinInfo;
//                    JSONObject shangPinObj;
//                    for (int i = 0; i < shangPinArray.length(); i++) {
//                        shangPinObj = shangPinArray.getJSONObject(i);
//                        woDeDingDan_shangPinInfo = new WoDeDingDan_ShangPinInfo();
//                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_shangpinId(JSONUtlis.getString(shangPinObj, "商品id"));
//                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_shuliang(JSONUtlis.getString(shangPinObj, "商品数量"));
//                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_zhongleixiangxi(JSONUtlis.getString(shangPinObj, "种类详细"));
//                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_pingJiaZhuangTai(JSONUtlis.getString(shangPinObj, "评价类别"));
//                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_wuLiuZhuangTai(JSONUtlis.getString(shangPinObj, "物流状态"));
//                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_tupian_url(JSONUtlis.getString(shangPinObj, "缩略图"));
//                        woDeDingDan_shangPinInfo.setShangPinType(JSONUtlis.getString(shangPinObj, "类型"));
//                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_title(JSONUtlis.getString(shangPinObj, "商品名称"));
//                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_shangpinZhuangTai(JSONUtlis.getString(shangPinObj, "状态"));
//                        woDeDingDan_shangPinInfo.setTv_item_dingdanxiangqing_danjia("¥" + JSONUtlis.getString(shangPinObj, "价格"));
//                        woDeDingDan_shangPinInfo.setIv_item_dingdanxiangqing_id(JSONUtlis.getString(shangPinObj, "id"));
//
//
//                        list.add(woDeDingDan_shangPinInfo);
//                    }
//                    setListViewHeightBasedOnChildren(listView);
//                    commonAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
