package com.gloiot.hygo.ui.activity.my;


import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.bean.MyBillBean;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的账单
 */
public class MyBillActivity extends BaseActivity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener, BaseActivity.RequestErrorCallback {

    /**
     * 顶部标题
     */
    @Bind(R.id.toptitle_tv)
    TextView toptitle_tv;
    /**
     * 顶部回退键
     */
    @Bind(R.id.toptitle_back)
    ImageView toptitle_back;
    /**
     * 顶部筛选
     */
    @Bind(R.id.tv_screen_type)
    TextView tv_screen_type;
    /**
     * 没有数据布局
     */
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @Bind(R.id.ptrl_wode_zhangdan)
    PullToRefreshLayout ptrl_wode_zhangdan;
    @Bind(R.id.pull_wode_zhangdan)
    PullableListView pull_wode_zhangdan;


    /**
     * 我的账单适配器
     */
    private CommonAdapter mCommonAdapter;
    /**
     * 记录账户类别的数组--用于筛选
     */
    private ArrayList<String> screenData = new ArrayList<>();

    /**
     * 筛选的类型
     */
    private String type = "";

    /**
     * 页数，默认为1
     */
    private int page = 0;

    /**
     * 我的订单实体数组
     */
    private ArrayList<MyBillBean> mDataList = new ArrayList<MyBillBean>();

    @Override
    public int initResource() {
        return R.layout.activity_my_bill;
    }

    @Override
    public void initData() {
        initAdapter();
        initListener();
        ptrl_wode_zhangdan.setOnRefreshListener(this);
        requestHandleArrayList.add(requestAction.p_sel_details_three(this, type, page + ""));
        requestHandleArrayList.add(requestAction.getLeiBieList(this));
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        toptitle_tv.setText("我的账单");
        mCommonAdapter = new CommonAdapter<MyBillBean>(mContext, R.layout.item_qianbao_new, mDataList) {
            @Override
            public void convert(ViewHolder holder, MyBillBean bean) {
                /** 说明*/
                TextView tv_explain = holder.getView(R.id.tv_explain);
                /** 备注*/
                TextView tv_remark = holder.getView(R.id.tv_remark);
                /** 时间*/
                TextView tv_time = holder.getView(R.id.tv_time);
                /** 交易价格（积分）*/
                TextView tv_money = holder.getView(R.id.tv_money);
                /** 账户类型*/
                TextView tv_type = holder.getView(R.id.tv_type);

                if (bean.getRemarks().equals("")) {
                    tv_remark.setVisibility(View.GONE);
                } else {
                    tv_remark.setMaxLines(12);
                    tv_remark.setVisibility(View.VISIBLE);
                    tv_remark.setText("备注：" + bean.getRemarks());
                }
                if (bean.getIntegral().contains("-")) {
                    holder.setTextColor(R.id.tv_money, Color.parseColor("#FF6D63"));
                } else {
                    holder.setTextColor(R.id.tv_money, getResources().getColor(R.color.main_color));
                }
                tv_explain.setText(bean.getExplain());
                tv_time.setText(bean.getEntryTime());
                tv_money.setText(bean.getIntegral());
                tv_type.setText(bean.getTransactionAccount());
            }
        };
        pull_wode_zhangdan.setAdapter(mCommonAdapter);
    }

    private void initListener() {
        pull_wode_zhangdan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mDataList.get(i) == null) {
                    MyToast.getInstance().showToast(MyBillActivity.this, "数据获取有误，请重新获取");
                    return;
                }
                Intent it = new Intent(MyBillActivity.this, XiangQingActivity.class);
                it.putExtra("id", mDataList.get(i).getId());
                it.putExtra("type", mDataList.get(i).getTransactionType());
                startActivity(it);
            }
        });
    }

    /**
     * 显示弹框
     */
    private void showDialog() {
        if (screenData.size() <= 0) {
            requestHandleArrayList.add(requestAction.getLeiBieList(this));
            return;
        }

        DialogUtlis.customListView(getmDialog(), "选择记录类型", screenData, new MDialogInterface.ListViewOnClickInter() {
            @Override
            public void onItemClick(String data, int position) {
                if (data.equals(type) || (data.equals("全部") && type.equals(""))) {
                    //若点击的筛选与上一次相同，则不加载数据,因为要保持当前加载的页码（有可能已经加载了很多页）
                    return;
                } else {
                    //若点击的筛选与上一次不同，则先把页码=0再加载数据（与上一次筛选不同，不需要考虑上一个筛选数据加载多少页的问题）
                    page = 0;
                }

                if (data.equals("全部")) {
                    type = "";
                } else {
                    type = data;
                }
                requestHandleArrayList.add(requestAction.p_sel_details_three(MyBillActivity.this, type, page + ""));
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);

        switch (requestTag) {
            case RequestAction.TAG_MY_BILL:
                Log.e("JSON数据：", response.toString());
                if (response.getInt("页数") == 1) {
                    mDataList.clear();
                }
                JSONArray arr = response.getJSONArray("积分列表");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    MyBillBean bean = new MyBillBean();
                    bean.setId(obj.getString("id"));
                    bean.setExplain(obj.getString("说明"));
                    bean.setIntegral(obj.getString("积分"));
                    bean.setEntryTime(obj.getString("录入时间"));
                    bean.setBalance(obj.getString("余额"));
                    bean.setRemarks(obj.getString("备注"));
                    bean.setTransactionAccount(obj.getString("交易账户"));
                    bean.setTransactionType(obj.getString("交易类别"));
                    mDataList.add(bean);
                }
                if (response.getInt("条数") == 0) {
                    if (page > 0) {
                        MyToast.getInstance().showToast(mContext, "已无更多数据加载");
                    } else {
                        ptrl_wode_zhangdan.setVisibility(View.GONE);
                        ll_nodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    ptrl_wode_zhangdan.setVisibility(View.VISIBLE);
                    ll_nodata.setVisibility(View.GONE);
                }
                mCommonAdapter.notifyDataSetChanged();
                break;
            case RequestAction.TAG_GETLEIBIELIST:
                Log.e("选择类型的数据：", response.toString());
                JSONArray arr1 = response.getJSONArray("数据");
                screenData.clear();
                for (int j = 0; j < arr1.length(); j++) {
                    screenData.add(arr1.getString(j));
                }
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_MY_BILL:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                ptrl_wode_zhangdan.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.VISIBLE);
                break;
            case RequestAction.TAG_GETLEIBIELIST:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                break;
        }
    }


    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 0;
                requestHandleArrayList.add(requestAction.p_sel_details_three(MyBillActivity.this, type, page + ""));
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                requestHandleArrayList.add(requestAction.p_sel_details_three(MyBillActivity.this, type, page + ""));
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 1000);
    }

    @OnClick({R.id.toptitle_back, R.id.tv_screen_type})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toptitle_back:
                finish();
                break;
            case R.id.tv_screen_type:
                showDialog();
                break;
        }
    }


}
