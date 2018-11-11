package com.gloiot.hygo.ui.activity.my.qianbao;


import android.content.Intent;
import android.graphics.Color;
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
 * 红利提现明细
 */
public class HongliTixianMingxiActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    @Bind(R.id.pulltorefreshlayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.pullablelistview)
    ListView mListView;
    @Bind(R.id.ll_nodata)
    LinearLayout mLlNoData;

    private List<String[]> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    //    private HongliTixianMingxiAdapter hongliTixianMingxiAdapter;
    private int page = 0;

    @Override
    public int initResource() {
        return R.layout.layout_common_mingxi;
    }


    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "提现明细");
        pullToRefreshLayout.setOnRefreshListener(this);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(this);
        request(0, 0, 1, 0);
//        mockData();
    }

    /**
     * @param requestType 请求类型，初始请求=0，刷新=1，加载=2
     * @param page        页数
     * @param requestTag  请求tag,请求成功后用于区分是哪个请求
     * @param showLoad    是否显示请求加载框 0--显示， -1--不显示
     */
    private void request(int requestType, int page, int requestTag, int showLoad) {
        requestHandleArrayList.add(requestAction.DividendExchangeMingxi(this, pullToRefreshLayout, requestType, page, requestTag, showLoad));
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
//            hongliTixianMingxiAdapter.notifyDataSetChanged();
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
        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_money_tixian_mx, list) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_time, strings[0]);
                holder.setText(R.id.tv_bank, strings[3]);
                holder.setText(R.id.tv_money, strings[2]);
                if (strings[4].length() >= 4) {
                    String last4num = strings[4].substring(strings[4].length() - 4);
                    holder.setText(R.id.tv_cardnum, "**** **** **** " + last4num);
                }
                if (strings[1].equals("转款失败(已退积分)") || "转款失败(未退积分)".equals(strings[1])) {
                    holder.setText(R.id.tv_state, "提现失败已退回");
                    holder.setTextColor(R.id.tv_state, Color.parseColor("#FF6D63"));
                    holder.setBackgroundRes(R.id.tv_state, R.drawable.shape_imaginaryline_2_ff6d63);
                } else if (strings[1].equals("已转银行卡") || "已转支付宝".equals(strings[1])) {
                    holder.setText(R.id.tv_state, "已成功");
                    holder.setTextColor(R.id.tv_state, Color.parseColor("#965aff"));
                    holder.setBackgroundRes(R.id.tv_state, R.drawable.shape_imaginaryline_2_2b9ced);
                } else if (strings[1].equals("银行处理中") || "支付宝处理中".equals(strings[1])) {
                    holder.setText(R.id.tv_state, "处理中");
                    holder.setTextColor(R.id.tv_state, Color.parseColor("#58A926"));
                    holder.setBackgroundRes(R.id.tv_state, R.drawable.shape_imaginaryline_2_58a828);
                } else if (strings[1].equals("待转银行卡") || "代付处理中".equals(strings[1])) {
                    holder.setText(R.id.tv_state, "待处理");
                    holder.setTextColor(R.id.tv_state, Color.parseColor("#ff954e"));
                    holder.setBackgroundRes(R.id.tv_state, R.drawable.shape_imaginaryline_2_ff954e);
                }
            }
        };

        //跳详情页
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HongliTixianMingxiActivity.this, XiangQingActivity.class);
                intent.putExtra("id", list.get(position)[5]);
                intent.putExtra("type", list.get(position)[6]);
                startActivity(intent);
            }
        });
        mListView.setAdapter(commonAdapter);

//        hongliTixianMingxiAdapter = new HongliTixianMingxiAdapter(mContext, list);
//        mListView.setAdapter(hongliTixianMingxiAdapter);
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
        L.e("红利提现明细", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[7];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                a[0] = jsonObject.getString("录入时间");
                a[1] = jsonObject.getString("审批状态");
                a[2] = jsonObject.getString("积分");
                a[3] = jsonObject.getString("转入银行");
                a[4] = jsonObject.getString("转入银行账号");
                a[5] = jsonObject.getString("id");
                a[6] = jsonObject.getString("交易类别");
                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
            mListView.setVisibility(View.VISIBLE);
            mLlNoData.setVisibility(View.GONE);
            if (isLoadMore) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                commonAdapter.notifyDataSetChanged();
//                hongliTixianMingxiAdapter.notifyDataSetChanged();
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

//    /**
//     * 红利提现适配器，先不写ViewHolder,防止乱序
//     */
//    class HongliTixianMingxiAdapter extends BaseAdapter {
//
//        private Context mContext;
//        private List<String[]> list = new ArrayList<>();
//
//        public HongliTixianMingxiAdapter(Context context, List<String[]> data) {
//            this.mContext = context;
//            this.list = data;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null != list ? list.get(position) : null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View itemView = inflater.inflate(R.layout.item_money_tixian_mx, null);
//
//            TextView tv_money = (TextView) itemView.findViewById(R.id.tv_money);
//            TextView tv_state = (TextView) itemView.findViewById(R.id.tv_state);
//            TextView tv_bank = (TextView) itemView.findViewById(R.id.tv_bank);
//            TextView tv_cardnum = (TextView) itemView.findViewById(R.id.tv_cardnum);
//            TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
//
//            String[] strings = (String[]) getItem(position);
//
//            tv_money.setText(strings[2]);
//            tv_bank.setText(strings[3]);
//            tv_time.setText(strings[0]);
//
//            if (strings[4].length() >= 4) {
//                String last4num = strings[4].substring(strings[4].length() - 4);
//                tv_cardnum.setText("**** **** **** " + last4num);
//            }
//            if (strings[1].equals("转款失败(已退积分)")) {
//                tv_state.setText("提现失败已退回");
//                tv_state.setTextColor(Color.parseColor("#FF6D63"));
//                tv_state.setBackgroundResource(R.drawable.shape_imaginaryline_2_ff6d63);
//            } else if (strings[1].equals("已转银行卡")) {
//                tv_state.setText("已成功");
//                tv_state.setTextColor(Color.parseColor("#2B9CED"));
//                tv_state.setBackgroundResource(R.drawable.shape_imaginaryline_2_2b9ced);
//            } else if (strings[1].equals("银行处理中")) {
//                tv_state.setText("处理中");
//                tv_state.setTextColor(Color.parseColor("#58A926"));
//                tv_state.setBackgroundResource(R.drawable.shape_imaginaryline_2_58a828);
//            }
//
//            return itemView;
//
//        }
//
//    }
}
