package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.bean.JiFenLeiXingBean;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.XiangQingActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShangJinBiZhangHuActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.lv_shangjinbizhanghu_listview)
    ListView lv_shangjinbizhanghu_listview;
    @Bind(R.id.pulltorefreshlayout)
    PullToRefreshLayout pulltorefreshlayout;
    @Bind(R.id.tv_shangjinbizhanghu_jifen)
    TextView tv_shangjinbizhanghu_jifen;
    @Bind(R.id.tv_shangjinbizhanghu_shuiming1)
    TextView tv_shangjinbizhanghu_shuiming1;

    private CommonAdapter<JiFenLeiXingBean> commonAdapter;
    private List<JiFenLeiXingBean> listDatas = new ArrayList<>(5);

    private int Loadlength, XiayiyeYeshu;
    private boolean refreshOrLoad;     //true为刷新，false为加载更多

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shangjinbi_zhanghu;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "商金币");

        commonAdapter = new CommonAdapter<JiFenLeiXingBean>(mContext, R.layout.item_list_jifen_leixing, listDatas) {
            char c;

            @Override
            public void convert(ViewHolder holder, JiFenLeiXingBean jiFenLeiXingBean) {
                holder.setText(R.id.tv_item_shuoming, jiFenLeiXingBean.getJiFen_ShuoMing());
                holder.setText(R.id.tv_item_shijian, jiFenLeiXingBean.getJiFen_ShiJian());
                holder.setText(R.id.tv_item_jine, jiFenLeiXingBean.getJiFen_JinE());

                c = ((TextView) holder.getView(R.id.tv_item_jine)).getText().toString().charAt(0);
                if ("-".equals(String.valueOf(c)))
                    ((TextView) holder.getView(R.id.tv_item_jine)).setTextColor(getResources().getColor(R.color.cl_ff6d63));
                else
                    ((TextView) holder.getView(R.id.tv_item_jine)).setTextColor(getResources().getColor(R.color.cl_2b9ced));
            }
        };

        //跳详情页
        lv_shangjinbizhanghu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShangJinBiZhangHuActivity.this, XiangQingActivity.class);
                intent.putExtra("id", listDatas.get(position).getJiFen_Id());
                intent.putExtra("type", listDatas.get(position).getJiFen_Type());
                startActivity(intent);
            }
        });
        lv_shangjinbizhanghu_listview.setAdapter(commonAdapter);

        pulltorefreshlayout.setOnRefreshListener(this);

//        Test();
        requestHandleArrayList.add(requestAction.shop_wodejifen_zhanghu(this, "商金币账户", "0", pulltorefreshlayout, 0));
    }

    private void Test() {
        JiFenLeiXingBean jiFenLeiXingBean;
        for (int i = 1; i <= 20; i++) {
            jiFenLeiXingBean = new JiFenLeiXingBean();
            jiFenLeiXingBean.setJiFen_ShiJian("2016-11-14 21:15:45--" + i);
            jiFenLeiXingBean.setJiFen_ShuoMing("这只是一个商金币测试而已---" + i);
            if (i % 2 == 0)
                jiFenLeiXingBean.setJiFen_JinE("+100");
            else
                jiFenLeiXingBean.setJiFen_JinE("-200");

            listDatas.add(jiFenLeiXingBean);
        }
        CommonUtlis.setListViewHeightBasedOnChildren(lv_shangjinbizhanghu_listview);
        commonAdapter.notifyDataSetChanged();
    }

    private void GetDatas(int i) {
        requestHandleArrayList.add(requestAction.shop_wodejifen_zhanghu(this, "商金币账户", String.valueOf(XiayiyeYeshu), pulltorefreshlayout, i));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOP_C_WALLETINFO:
                //{"状态":"成功","商金币总额":"7.0000","记录":[],"条数":0,"页数":1}  shop_t_walletinfo_two
                //{"状态":"成功","页数":1,"商金币账户":"0.00","条数":0,"列表":[]}    shop_t_walletinfo_three
                if ("成功".equals(response.getString("状态"))) {

                    final SpannableStringBuilder sp1 = new SpannableStringBuilder(response.getString("备注"));
                    sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_2b9ced)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_shangjinbizhanghu_shuiming1.setText(sp1);

                    tv_shangjinbizhanghu_jifen.setText(response.getString("商金币账户"));

                    if (refreshOrLoad) {
                        listDatas.clear();
                        commonAdapter.notifyDataSetChanged();
                    } else {
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    Loadlength = response.getInt("条数");

                    if (Loadlength > 0) {
                        JSONArray jsonArray = response.getJSONArray("列表");
                        JiFenLeiXingBean jiFenLeiXingBean;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            jiFenLeiXingBean = new JiFenLeiXingBean();
                            jiFenLeiXingBean.setJiFen_JinE(jsonObject.getString("积分"));
                            jiFenLeiXingBean.setJiFen_ShuoMing(jsonObject.getString("说明"));
                            jiFenLeiXingBean.setJiFen_ShiJian(jsonObject.getString("录入时间"));
                            jiFenLeiXingBean.setJiFen_Id(jsonObject.getString("id"));
                            jiFenLeiXingBean.setJiFen_Type(jsonObject.getString("交易类别"));
                            listDatas.add(jiFenLeiXingBean);
                        }
                    }
                    XiayiyeYeshu = response.getInt("页数");
                }
                CommonUtlis.setListViewHeightBasedOnChildren(lv_shangjinbizhanghu_listview);
                commonAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshOrLoad = true;
        Loadlength = 0;
        XiayiyeYeshu = 0;
        GetDatas(1);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (Loadlength > 0) {
            refreshOrLoad = false;
            GetDatas(2);
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }
}
