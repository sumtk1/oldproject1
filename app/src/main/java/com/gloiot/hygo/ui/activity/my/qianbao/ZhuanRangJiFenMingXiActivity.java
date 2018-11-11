package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.XiangQingActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 转让积分明细
 */
public class ZhuanRangJiFenMingXiActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    @Bind(R.id.pulltorefreshlayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.pullablelistview)
    ListView mListView;
    @Bind(R.id.ll_nodata)
    LinearLayout mLlNoData;

    private List<String[]> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private int page = 0;

    @Override
    public int initResource() {
        return R.layout.layout_common_mingxi;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "转让明细");
        pullToRefreshLayout.setOnRefreshListener(this);
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
        requestHandleArrayList.add(requestAction.GetJifenZhuanrangMingxi(this, pullToRefreshLayout, requestType, page, requestTag, showLoad));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.pulltorefreshlayout:

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
        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_money_jfzr_mx_new, list) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_explain, "说明：" + strings[2]);
                holder.setText(R.id.tv_time, strings[3]);
                try {
                    if (strings[0].substring(0, 1).equals("-")) {
                        holder.setText(R.id.tv_credits, strings[0]);
                        holder.setTextColor(R.id.tv_credits, Color.parseColor("#FF6D63"));
                    } else {
                        holder.setText(R.id.tv_credits, strings[0]);
                        holder.setTextColor(R.id.tv_credits, Color.parseColor("#965aff"));
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(strings[4])) {
                    holder.setVisible(R.id.tv_remark, true);
                    holder.setText(R.id.tv_remark, "备注：" + strings[4]);
                } else {
                    holder.setVisible(R.id.tv_remark, false);
                }
                if (!TextUtils.isEmpty(strings[5])) {
                    holder.setText(R.id.tv_type, strings[5]);
                }

            }
        };
        //跳详情页
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ZhuanRangJiFenMingXiActivity.this, XiangQingActivity.class);
                intent.putExtra("id", list.get(position)[6]);
                intent.putExtra("type", list.get(position)[7]);
                startActivity(intent);
            }
        });
        mListView.setAdapter(commonAdapter);
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
    private void processResponseData(JSONObject response, final boolean isLoadMore) throws JSONException {
        L.e("积分转让明细", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[8];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                a[0] = jsonObject.getString("转让积分");
                a[1] = jsonObject.getString("余额");
                a[2] = jsonObject.getString("转让说明");
                a[3] = jsonObject.getString("时间");
                a[4] = jsonObject.getString("备注");
                a[5] = jsonObject.getString("交易账户");
                a[6] = jsonObject.getString("id");
                a[7] = jsonObject.getString("交易类别");
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
}
