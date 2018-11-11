package com.gloiot.hygo.ui.activity.my.qianbao;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.LoadDialog;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 积分充值（购买积分）明细
 */
public class JiFenChongZhiMingXiActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.pulltorefreshlayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.pullablelistview)
    ListView mListView;
    @Bind(R.id.ll_nodata)
    LinearLayout mLlNoData;

    private List<String[]> list = new ArrayList<String[]>();
    private CommonAdapter commonAdapter;
    private int page = 0;

    @Override
    public int initResource() {
        return R.layout.layout_common_mingxi;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "充值明细");
        pullToRefreshLayout.setOnRefreshListener(this);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(this);
        request(0, 0, 1, 0);
    }

    /**
     * @param requestType 请求类型，初始请求=0，刷新=1，加载=2
     * @param page        页数
     * @param requestTag  请求tag,请求成功后用于区分是哪个请求
     * @param showLoad    是否显示请求加载框 0--显示， -1--不显示
     */
    private void request(int requestType, int page, int requestTag, int showLoad) {
        requestHandleArrayList.add(requestAction.GetJifenChongzhiMingxi(this, pullToRefreshLayout, requestType, page, requestTag, showLoad));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.pullablelistview:

                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            commonAdapter.notifyDataSetChanged();
        }
        list.clear();
        request(1, 0, 2, -1);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (page > 0) {
            request(2, page, 3, -1);
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_money_jfcz_mx, list) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                if (!TextUtils.isEmpty(strings[0])) {
                    try {
                        Double chongzhi = Double.parseDouble(strings[0]);
                        holder.setText(R.id.tv_point_recharge, decimalFormat.format(chongzhi));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.setText(R.id.tv_point_recharge, strings[0]);
                }

                if (!TextUtils.isEmpty(strings[1])) {
                    try {
                        Double zengsong = Double.parseDouble(strings[1]);
                        holder.setText(R.id.tv_point_give, decimalFormat.format(zengsong));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.setText(R.id.tv_point_give, "0");
                }

                holder.setText(R.id.tv_purchase_time, strings[6]);

                String insidePaystyle = strings[2];
                String outsidePaystyle = strings[3];

                try {
                    Double insideMoney = Double.parseDouble(strings[4]);
                    Double outsideMoney = Double.parseDouble(strings[5]);

                    if (insideMoney == 0 || outsideMoney == 0) {
                        holder.setVisible(R.id.rl_paystyle_2, false);
                        holder.setVisible(R.id.rl_paystyle_1, true);

                        if (insideMoney == 0) {
                            holder.setText(R.id.tv_paystyle_1, outsidePaystyle + "(" + decimalFormat.format(outsideMoney) + ")");
                        } else {
                            holder.setText(R.id.tv_paystyle_1, insidePaystyle + "(" + decimalFormat.format(insideMoney) + ")");
                        }

                    } else {
                        holder.setVisible(R.id.rl_paystyle_1, false);
                        holder.setVisible(R.id.rl_paystyle_2, true);

                        holder.setText(R.id.tv_paystyle_2_1, outsidePaystyle + "(" + decimalFormat.format(outsideMoney) + ")");
                        holder.setText(R.id.tv_paystyle_2_2, insidePaystyle + "(" + decimalFormat.format(insideMoney) + ")");

                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        };
        mListView.setAdapter(commonAdapter);
        mListView.setDivider(new ColorDrawable(Color.parseColor("#E7E7E7")));
        mListView.setDividerHeight(30);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case 1:
                processResponseData(response, false);
                break;
            case 2:
                processResponseData(response, false);
                break;
            case 3:
                processResponseData(response, true);
                break;
        }
    }

    /**
     * 处理请求返回数据
     *
     * @param response
     * @param isLoadMore
     */
    private void processResponseData(JSONObject response, boolean isLoadMore) throws JSONException {
        L.e("积分充值明细", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int j = 0; j < jsonArray.length(); j++) {
                String[] a = new String[7];
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(j);
                a[0] = jsonObject2.getString("总金额");
                a[1] = jsonObject2.getString("赠送积分");
                a[2] = jsonObject2.getString("内部支付方式");
                a[3] = jsonObject2.getString("外部支付方式");
                a[4] = jsonObject2.getString("内部金额");
                a[5] = jsonObject2.getString("外部金额");
                a[6] = jsonObject2.getString("购买时间");
                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
            mListView.setVisibility(View.VISIBLE);
            mLlNoData.setVisibility(View.GONE);
            if (isLoadMore) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                commonAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }
        } else {
            if (isLoadMore) {
                MyToast.getInstance().showToast(mContext, "已无数据加载");
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            } else {
                mListView.setVisibility(View.GONE);
                mLlNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 取消请求，退出当前页面
     *
     * @param requestTag
     * @param showLoad
     */
    @Override
    public void onCancel(int requestTag, int showLoad) {
        super.onCancel(requestTag, showLoad);
        switch (requestTag) {
            case 1:
                LoadDialog.dismiss(this);
                finish();
                break;
            case 2:
                LoadDialog.dismiss(this);
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
