package com.gloiot.hygo.ui.activity.my;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的优惠券
 */
public class YouHuiQuanActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.pulltorefreshlayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.pullablelistview)
    ListView mListView;
    @Bind(R.id.tv_listview_no)
    TextView mTvNoData;

    private List<String[]> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private int[] bg = {R.mipmap.juan_1, R.mipmap.juan_2, R.mipmap.juan_3};
    private int page = 0;

    @Override
    public int initResource() {
        return R.layout.layout_listview_pulltorefresh;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "我的优惠券");
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
        requestHandleArrayList.add(requestAction.GetMyDiscountCoupon(this, pullToRefreshLayout, requestType, page, requestTag, showLoad));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            commonAdapter.notifyDataSetChanged();
        }
        list.clear();
        mTvNoData.setText("");
        request(1, 0, 2, -1);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (page > 0) {
            mTvNoData.setText("");
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
        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_youhuiquan, list) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_money, "¥ " + strings[2]);
                holder.setText(R.id.tv_desc, strings[5]);
                holder.setText(R.id.tv_money_desc, strings[6]);
                holder.setText(R.id.tv_date, "有效期：" + strings[4]);
                try {
                    holder.setBackgroundRes(R.id.rl_bg, bg[Integer.parseInt(strings[1])]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.setOnClickListener(R.id.tv_send, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(YouHuiQuan.this, Activity_SentCoupon.class);
//                        intent.putExtra("积分", strings[2]);
//                        intent.putExtra("限购说明", strings[5]);
//                        intent.putExtra("价格说明", strings[6]);
//                        intent.putExtra("使用期限", strings[4]);
//                        intent.putExtra("id", strings[7]);
//                        intent.putExtra("名称", strings[1]);
//                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.tv_use, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(YouHuiQuan.this, Activity_UseCoupon.class);
//                        intent.putExtra("id", strings[7]);
//                        startActivity(intent);
                    }
                });

            }
        };
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
    private void processResponseData(JSONObject response, boolean isLoadMore) throws JSONException {
        L.e("我的优惠券", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int j = 0; j < jsonArray.length(); j++) {
                String[] a = new String[8];
                JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                a[0] = jsonObject.getString("类别");
                a[1] = jsonObject.getString("名称").substring(3);
                a[2] = jsonObject.getString("积分");
                a[3] = jsonObject.getString("需购数量");
                a[4] = jsonObject.getString("使用期限");
                a[5] = jsonObject.getString("限购说明");
                a[6] = jsonObject.getString("价格说明");
                a[7] = jsonObject.getString("id");
                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
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
                mTvNoData.setText("无数据");
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
