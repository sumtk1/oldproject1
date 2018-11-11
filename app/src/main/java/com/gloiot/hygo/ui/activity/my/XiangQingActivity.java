package com.gloiot.hygo.ui.activity.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.StringUtils;
import com.zyd.wlwsdk.utils.JSONUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by hygo03 on 2017/7/12.
 */

public class XiangQingActivity extends BaseActivity {

    @Bind(R.id.rl_zhangdan_xiangqing_1)
    RelativeLayout rl_zhangdan_xiangqing_1;
    @Bind(R.id.rl_zhangdan_xiangqing_2)
    RelativeLayout rl_zhangdan_xiangqing_2;
    @Bind(R.id.rl_zhangdan_xiangqing_3)
    RelativeLayout rl_zhangdan_xiangqing_3;
    @Bind(R.id.rl_zhangdan_xiangqing_4)
    RelativeLayout rl_zhangdan_xiangqing_4;
    @Bind(R.id.rl_zhangdan_xiangqing_5)
    RelativeLayout rl_zhangdan_xiangqing_5;
    @Bind(R.id.rl_zhangdan_xiangqing_6)
    RelativeLayout rl_zhangdan_xiangqing_6;

    @Bind(R.id.tv_content)
    TextView tv_content;
    @Bind(R.id.tv_money)
    TextView tv_money;
    @Bind(R.id.tv_jiaoyi_type)
    TextView tv_jiaoyi_type;
    @Bind(R.id.tv_zhangdan_xiangqing_1_1)
    TextView tv_zhangdan_xiangqing_1_1;
    @Bind(R.id.tv_zhangdan_xiangqing_1_2)
    TextView tv_zhangdan_xiangqing_1_2;

    @Bind(R.id.tv_zhangdan_xiangqing_2_1)
    TextView tv_zhangdan_xiangqing_2_1;
    @Bind(R.id.tv_zhangdan_xiangqing_2_2)
    TextView tv_zhangdan_xiangqing_2_2;

    @Bind(R.id.tv_zhangdan_xiangqing_3_1)
    TextView tv_zhangdan_xiangqing_3_1;
    @Bind(R.id.tv_zhangdan_xiangqing_3_2)
    TextView tv_zhangdan_xiangqing_3_2;

    @Bind(R.id.tv_zhangdan_xiangqing_4_1)
    TextView tv_zhangdan_xiangqing_4_1;
    @Bind(R.id.tv_zhangdan_xiangqing_4_2)
    TextView tv_zhangdan_xiangqing_4_2;

    @Bind(R.id.tv_zhangdan_xiangqing_5_1)
    TextView tv_zhangdan_xiangqing_5_1;
    @Bind(R.id.tv_zhangdan_xiangqing_5_2)
    TextView tv_zhangdan_xiangqing_5_2;

    @Bind(R.id.tv_zhangdan_xiangqing_6_2)
    TextView tv_zhangdan_xiangqing_6_2;

    private String type, zhanghao;

    @Override
    public int initResource() {
        return R.layout.activity_jilu_xiangqing;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "账单详情");

        String id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        if ("支付宝充值".equals(type)) {
            zhanghao = getIntent().getStringExtra("充值账户");
        }

        requestHandleArrayList.add(requestAction.p_billing_details_three(this, id, type));

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_DETAILS:

                JSONArray jsonArray = response.getJSONArray("列表");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (i == 0) {
                            tv_jiaoyi_type.setText(jsonObject.getString("交易状态"));
                            tv_money.setText(jsonObject.getString("金额"));
                            tv_content.setText(jsonObject.getString("业务名"));
                        } else {
                            switch (type) {
                                case "积分转让":
                                    rl_zhangdan_xiangqing_3.setVisibility(View.GONE);

                                    tv_zhangdan_xiangqing_1_1.setText("收款方");
                                    tv_zhangdan_xiangqing_5_1.setText("备注");

                                    tv_zhangdan_xiangqing_1_2.setText(StringUtils.isEmpyt(setPhoneType(JSONUtlis.getString(jsonObject, "入账账户"))));
                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_4_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "支付方式")));
                                    tv_zhangdan_xiangqing_5_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "备注")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));
                                    break;
                                case "积分充值":
                                    rl_zhangdan_xiangqing_1.setVisibility(View.GONE);

                                    tv_zhangdan_xiangqing_5_1.setText("赠送积分");

                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_4_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "支付方式")));
                                    tv_zhangdan_xiangqing_5_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "赠送积分")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));

                                    if (!"".equals(JSONUtlis.getString(jsonObject, "订单号"))) {
                                        tv_zhangdan_xiangqing_3_2.setText(JSONUtlis.getString(jsonObject, "订单号"));
                                    } else {
                                        rl_zhangdan_xiangqing_3.setVisibility(View.GONE);
                                    }
                                    break;

                                case "银行卡提现":
                                    rl_zhangdan_xiangqing_4.setVisibility(View.GONE);
                                    rl_zhangdan_xiangqing_5.setVisibility(View.GONE);
                                    rl_zhangdan_xiangqing_6.setVisibility(View.GONE);

                                    tv_zhangdan_xiangqing_1_1.setText("银行名称");
                                    tv_zhangdan_xiangqing_2_1.setText("银行账户");
                                    tv_zhangdan_xiangqing_3_1.setText("时间");

                                    tv_zhangdan_xiangqing_1_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "银行名称")));
                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "银行账户")));
                                    tv_zhangdan_xiangqing_3_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));
                                    break;
                                case "支付宝提现":
                                    rl_zhangdan_xiangqing_2.setVisibility(View.GONE);
                                    rl_zhangdan_xiangqing_4.setVisibility(View.GONE);
                                    rl_zhangdan_xiangqing_5.setVisibility(View.GONE);
                                    rl_zhangdan_xiangqing_6.setVisibility(View.GONE);
                                    tv_zhangdan_xiangqing_1_1.setText("支付宝账户");
                                    tv_zhangdan_xiangqing_1_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "转入账号")));
                                    tv_zhangdan_xiangqing_3_1.setText("时间");
                                    tv_zhangdan_xiangqing_3_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));
                                    break;
                                case "超级商家":
                                    rl_zhangdan_xiangqing_4.setVisibility(View.GONE);

                                    tv_zhangdan_xiangqing_1_1.setText("收款方");
                                    tv_zhangdan_xiangqing_5_1.setText("备注");

                                    tv_zhangdan_xiangqing_1_2.setText(setPhoneType(JSONUtlis.getString(jsonObject, "入账账户")));
                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_5_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "备注")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));

                                    if (!"".equals(JSONUtlis.getString(jsonObject, "订单号"))) {
                                        tv_zhangdan_xiangqing_3_2.setText(JSONUtlis.getString(jsonObject, "订单号"));
                                    } else {
                                        rl_zhangdan_xiangqing_3.setVisibility(View.GONE);
                                    }
                                    break;

                                case "充值中心":
                                    rl_zhangdan_xiangqing_1.setVisibility(View.GONE);
                                    rl_zhangdan_xiangqing_5.setVisibility(View.GONE);

                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_4_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "支付方式")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));


                                    if (!"".equals(JSONUtlis.getString(jsonObject, "订单号"))) {
                                        tv_zhangdan_xiangqing_3_2.setText(JSONUtlis.getString(jsonObject, "订单号"));
                                    } else {
                                        rl_zhangdan_xiangqing_3.setVisibility(View.GONE);
                                    }
                                    break;

                                case "商金币":
                                case "积分宝":
                                case "油费":
                                case "商品购物":
                                case "商品购物待支付":
                                case "全部":
                                    rl_zhangdan_xiangqing_1.setVisibility(View.GONE);
                                    tv_zhangdan_xiangqing_5_1.setText("备注");

                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_5_2.setText(StringUtils.isEmpyt(jsonObject.getString("备注")));
                                    tv_zhangdan_xiangqing_4_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "支付方式")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));

                                    if (!"".equals(JSONUtlis.getString(jsonObject, "订单号"))) {
                                        tv_zhangdan_xiangqing_3_2.setText(JSONUtlis.getString(jsonObject, "订单号"));
                                    } else {
                                        rl_zhangdan_xiangqing_3.setVisibility(View.GONE);
                                    }
                                    break;

                                case "支付宝充值":
                                    tv_zhangdan_xiangqing_1_1.setText("充值账户");
                                    tv_zhangdan_xiangqing_5_1.setText("备注");

                                    tv_zhangdan_xiangqing_1_2.setText(StringUtils.isEmpyt(zhanghao));
                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_4_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "支付方式")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));
                                    tv_zhangdan_xiangqing_5_2.setText(StringUtils.isEmpyt(jsonObject.getString("备注")));

                                    if (!"".equals(JSONUtlis.getString(jsonObject, "订单号"))) {
                                        tv_zhangdan_xiangqing_3_2.setText(JSONUtlis.getString(jsonObject, "订单号"));
                                    } else {
                                        rl_zhangdan_xiangqing_3.setVisibility(View.GONE);
                                    }
                                    break;
                                case "红利兑换支付宝":
                                    rl_zhangdan_xiangqing_1.setVisibility(View.GONE);
                                    tv_zhangdan_xiangqing_3_1.setText("支付方式");
                                    tv_zhangdan_xiangqing_4_1.setText("手续费");
                                    tv_zhangdan_xiangqing_5_1.setText("备注");

                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_3_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "支付方式")));
                                    tv_zhangdan_xiangqing_4_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "手续费")));
                                    tv_zhangdan_xiangqing_5_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "备注")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));
                                    break;
                                default:
                                    rl_zhangdan_xiangqing_1.setVisibility(View.GONE);
                                    tv_zhangdan_xiangqing_5_1.setText("备注");

                                    tv_zhangdan_xiangqing_2_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "说明")));
                                    tv_zhangdan_xiangqing_4_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "支付方式")));
                                    tv_zhangdan_xiangqing_5_2.setText(StringUtils.isEmpyt(jsonObject.getString("备注")));
                                    tv_zhangdan_xiangqing_6_2.setText(StringUtils.isEmpyt(JSONUtlis.getString(jsonObject, "时间")));
                                    if (!"".equals(JSONUtlis.getString(jsonObject, "订单号"))) {
                                        tv_zhangdan_xiangqing_3_2.setText(JSONUtlis.getString(jsonObject, "订单号"));
                                    } else {
                                        rl_zhangdan_xiangqing_3.setVisibility(View.GONE);
                                    }
                                    break;
                            }

                        }
                    }
                }
                break;
        }
    }

    private String setPhoneType(String s) {

        if (!TextUtils.isEmpty(s)) {
            if (s.length() >= 3) {
                int num = s.length() / 3;
                String x = "";
                String first = s.substring(0, num);
                for (int i = 0; i < num + 1; i++) {
                    x = x + "*";
                }
                String last = s.substring(2 * num + 1, s.length());
                String phone = first + x + last;
                return phone;
            } else {
                return s;
            }
        }
        return "";
    }
}
