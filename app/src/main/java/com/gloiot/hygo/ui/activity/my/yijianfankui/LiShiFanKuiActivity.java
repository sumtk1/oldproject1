package com.gloiot.hygo.ui.activity.my.yijianfankui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class LiShiFanKuiActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    @Bind(R.id.prl_lishi_fankui_listview)
    PullableListView prlLishiFankuiListview;
    @Bind(R.id.prl_lishi_fankui_layout)
    PullToRefreshLayout prlLishiFankuiLayout;

    private CommonAdapter commonAdapter;
    private List<LiShiFankuiBean> listDatas = new ArrayList<>(5);

    private int XiayiyeYeshu = 0;
    private int Loadlength;

    @Override
    public int initResource() {
        return R.layout.activity_lishi_fankui;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "历史反馈");
        prlLishiFankuiLayout.setOnRefreshListener(this);

        commonAdapter = new CommonAdapter<LiShiFankuiBean>(mContext, R.layout.item_lishi_fankui, listDatas) {
            @Override
            public void convert(ViewHolder holder, LiShiFankuiBean liShiFankuiBean) {
                holder.setText(R.id.tv_item_lishi_fankui_leixing, liShiFankuiBean.LiShiFankui_Leixing);
                holder.setText(R.id.tv_item_lishi_fankui_time, liShiFankuiBean.LiShiFankui_time);
                holder.setText(R.id.btn_item_lishi_fankui_zhuangtai, liShiFankuiBean.LiShiFankui_zhuangtai);
                holder.setText(R.id.tv_item_lishi_fankui_content, liShiFankuiBean.LiShiFankui_content);

                if ("已处理".equals(liShiFankuiBean.LiShiFankui_zhuangtai)) {
                    holder.getView(R.id.btn_item_lishi_fankui_zhuangtai).setBackgroundResource(R.drawable.bg_bfbfbf_20px);
                } else {
                    holder.getView(R.id.btn_item_lishi_fankui_zhuangtai).setBackgroundResource(R.drawable.bg_ff6d63_20px);
                }
            }
        };
        prlLishiFankuiListview.setAdapter(commonAdapter);

        prlLishiFankuiListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("已处理".equals(listDatas.get(position).LiShiFankui_zhuangtai)) {
                    Intent intent = new Intent(LiShiFanKuiActivity.this, LiShiFanKuiXiangQingActivity.class);
                    intent.putExtra("反馈id", listDatas.get(position).LiShiFankui_id);
                    startActivity(intent);
                } else {
                    MyToast.getInstance().showToast(mContext, "问题处理中");
                }

            }
        });

        requestHandleArrayList.add(requestAction.history_feedback(this,
                XiayiyeYeshu + "", prlLishiFankuiLayout, 0));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess:" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_HISTORY_FEEDBACK:
                if (JSONUtlis.getInt(response, "pages", 1) == 1) {
                    listDatas.clear();
                    commonAdapter.notifyDataSetChanged();
                } else {
                    prlLishiFankuiLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }

                Loadlength = JSONUtlis.getInt(response, "num");

                JSONArray jsonArray = response.getJSONArray("list");
                LiShiFankuiBean liShiFankuiBean;
                for (int i = 0; i < jsonArray.length(); i++) {
                    liShiFankuiBean = new LiShiFankuiBean();
                    JSONObject obj = jsonArray.getJSONObject(i);
                    liShiFankuiBean.LiShiFankui_id = JSONUtlis.getString(obj, "反馈id");

                    if ("已提交".equals(JSONUtlis.getString(obj, "状态"))) {
                        liShiFankuiBean.LiShiFankui_zhuangtai = "待处理";
                    } else {
                        liShiFankuiBean.LiShiFankui_zhuangtai = JSONUtlis.getString(obj, "状态");
                    }

                    liShiFankuiBean.LiShiFankui_Leixing = "类型：" + JSONUtlis.getString(obj, "反馈类别");
                    liShiFankuiBean.LiShiFankui_content = JSONUtlis.getString(obj, "问题描述");
                    liShiFankuiBean.LiShiFankui_time = JSONUtlis.getString(obj, "录入时间");

                    listDatas.add(liShiFankuiBean);
                }
                commonAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        XiayiyeYeshu = 0;
        requestHandleArrayList.add(requestAction.history_feedback(this,
                XiayiyeYeshu + "", prlLishiFankuiLayout, 1));
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        //确保有数据才设置上拉加载更多监听
        if (Loadlength > 9) {
            requestHandleArrayList.add(requestAction.history_feedback(this,
                    XiayiyeYeshu + "", prlLishiFankuiLayout, 2));
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }


    public class LiShiFankuiBean {
        private String LiShiFankui_id;
        private String LiShiFankui_Leixing;
        private String LiShiFankui_time;
        private String LiShiFankui_zhuangtai;
        private String LiShiFankui_content;
    }
}
