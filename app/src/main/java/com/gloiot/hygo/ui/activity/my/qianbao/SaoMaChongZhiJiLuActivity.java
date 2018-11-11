package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.XiangQingActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hygo03 on 2017/7/5.
 */

public class SaoMaChongZhiJiLuActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener{

    @Bind(R.id.lv_honglijiben)
    ListView lvHonglijiben;
    @Bind(R.id.ptrl_honglijiben)
    PullToRefreshLayout ptrlHonglijiben;
    @Bind(R.id.ll_nodata)
    LinearLayout mLlNoData;

    private String onlyId;
    private CommonAdapter commonAdapter;
    private List<String[]> list = new ArrayList<String[]>();
    private int Loadlength, XiayiyeYeshu = 0;
    private boolean refreshOrLoad;

    @Override
    public int initResource() {
        return R.layout.activity_saoma_chongzhi_jilu;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "充值明细");

        onlyId  = preferences.getString(ConstantUtlis.SP_ONLYID,"");
        ptrlHonglijiben.setOnRefreshListener(this);
        getData(0,0);

        ptrlHonglijiben.setOnRefreshListener(this);

        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_saoma_chongzhi, list) {
            @Override
            public void convert(ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_explain,strings[0]);
                holder.setText(R.id.tv_time, strings[2]);
                holder.setText(R.id.tv_money, strings[1]);
            }
        };

        lvHonglijiben.setAdapter(commonAdapter);

        lvHonglijiben.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent  = new Intent(mContext, XiangQingActivity.class);
                intent.putExtra("id",list.get(position)[3]);
                intent.putExtra("type",list.get(position)[4]);
                intent.putExtra("充值账户",list.get(position)[5]);

                startActivity(intent);
            }
        });

    }


    public void getData(int requestType,int page){
        requestHandleArrayList.add(requestAction.p_recharge_three(this,ptrlHonglijiben,requestType,page,onlyId));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag){
            case RequestAction.TAG_SHOP_P_RECHARGE_THREE:
                if (refreshOrLoad) {
                    list.clear();
                    commonAdapter.notifyDataSetChanged();
                } else {
                    ptrlHonglijiben.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }

                Loadlength = response.getInt("条数");
                XiayiyeYeshu = response.getInt("页数");

                if (Loadlength != 0) {
                    JSONArray jsonArray = response.getJSONArray("数据");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                        String[] details = new String[6];
                        details[0] = jsonObj.getString("说明");
                        details[1] = jsonObj.getString("总金额");
                        details[2] = jsonObj.getString("交易时间");
                        details[3] = jsonObj.getString("id");
                        details[4] = jsonObj.getString("交易类别");
                        details[5] = jsonObj.getString("充值账户");

                        list.add(details);
                    }
                    commonAdapter.notifyDataSetChanged();
                }

                if(list.size() == 0){
                    mLlNoData.setVisibility(View.VISIBLE);
                    lvHonglijiben.setVisibility(View.GONE);
                }
                else{
                    mLlNoData.setVisibility(View.GONE);
                    lvHonglijiben.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshOrLoad = true;
        XiayiyeYeshu = 0;
        getData(1,XiayiyeYeshu);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        //确保有数据才设置上拉加载更多监听
        if (Loadlength > 0) {
            refreshOrLoad = false;
            getData(2,XiayiyeYeshu);
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }
}
