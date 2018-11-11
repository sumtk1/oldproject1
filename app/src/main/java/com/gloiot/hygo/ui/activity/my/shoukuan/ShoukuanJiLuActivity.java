package com.gloiot.hygo.ui.activity.my.shoukuan;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.widge.LoadDialog;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShoukuanJiLuActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.pulltorefreshlayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.pullablelistview)
    ListView mListView;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;


    private List<String[]> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private int page = 0;

    @Override
    public int initResource() {
        return R.layout.layout_listview_pulltorefresh;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "收款记录");
        pullToRefreshLayout.setOnRefreshListener(this);

        request(0, 0, 100, 0);
    }

    /**
     * 获取收款记录
     *
     * @param requestType 请求类型，初始请求=0，刷新=1，加载=2
     * @param page        页数
     * @param requestTag  请求tag,请求成功后用于区分是哪个请求
     * @param showLoad    是否显示请求加载框 0--显示， -1--不显示
     */
    private void request(int requestType, int page, int requestTag, int showLoad) {
        requestHandleArrayList.add(requestAction.GetShoukuanJilu(this, pullToRefreshLayout, requestType, page, requestTag, showLoad));
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            commonAdapter.notifyDataSetChanged();
        }
        list.clear();
        request(1, 0, 200, -1);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (page > 0) {
            request(2, page, 300, -1);
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case 100:
                processResponseData(response, false);
                break;
            case 200:
                processResponseData(response, false);
                break;
            case 300:
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
        L.e("收款记录", response.toString());
        int num = response.getInt("条数");
        if (num != 0) {

            llNodata.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            JSONArray jsonArray = response.getJSONArray("记录列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[6];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                a[0] = jsonObject.getString("转账人头像");
                a[0] = jsonObject.getString("头像");
                a[1] = jsonObject.getString("收款金额");
                a[2] = jsonObject.getString("收款时间");
                a[3] = jsonObject.getString("备注");
                a[4] = jsonObject.getString("说明");
                a[5] = jsonObject.getString("支付平台");
                list.add(a);
            }
            page = num == 10 ? response.getInt("页数") : 0;
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
                llNodata.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_shoukuanjilu, list) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
//                PictureUtlis.loadCircularImageViewHolder(mContext, strings[0], R.mipmap.ic_head, (ImageView) holder.getView(R.id.iv_jilu_icon));
                holder.setText(R.id.tv_jilu_money, "+" + strings[1]);
                holder.setText(R.id.tv_jilu_time, strings[2]);
                if (!TextUtils.isEmpty(strings[3])) {
                    holder.setVisible(R.id.ll_jilu_beizhu, true);
                    holder.setText(R.id.tv_jilu_beizhu, strings[3]);
                } else {
                    holder.setVisible(R.id.ll_jilu_beizhu, false);
                }

                holder.setText(R.id.tv_jilu_shuoming, strings[4]);

                ImageView iv_jilu_icon = holder.getView(R.id.iv_jilu_icon);
                PictureUtlis.loadCircularImageViewHolder(mContext, R.mipmap.ic_launcher , iv_jilu_icon);
                /* if ("微信".equals(strings[5])) {
                    iv_jilu_icon.setImageResource(R.mipmap.ic_pay_wx);
                } else {
                    iv_jilu_icon.setImageResource(R.mipmap.ic_pay_ali);
                }*/

               /* if (strings[0]!=null || !"".equals(strings[0])) {
                    PictureUtlis.loadCircularImageViewHolder(mContext, strings[0], R.mipmap.ic_head, iv_jilu_icon);
                } else {
                    iv_jilu_icon.setImageResource(R.mipmap.ic_head);
                }*/
            }
        };
        mListView.setAdapter(commonAdapter);
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
        switch (showLoad) {
            case 0:
                LoadDialog.dismiss(this);
                finish();
                break;
        }
    }

}
