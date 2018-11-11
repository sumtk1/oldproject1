package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 时间：on 2018/1/15 15:10.
 * 作者：by hygo04
 * 功能：我的——积分钱包——选择提现方式
 */

public class XuanZeTiXianActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ll_xuanzetixian_fault)
    LinearLayout llXuanzetixianFault;

    private List<XuanZeTiXianType> mData;
    private CommonAdapter commonAdapter;

    @Override
    public int initResource() {
        return  R.layout.activity_my_qianbao_xuanzetixian;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "选择提现方式");

        setAdapter();
        requestHandleArrayList.add(requestAction.getTiXianShouXuFei(this));
    }

    private void setAdapter() {
        mData = new ArrayList<>();
        commonAdapter = new CommonAdapter<XuanZeTiXianType>(mContext, R.layout.item_my_qianbao_xuanzetixian, mData) {
            @Override
            public void convert(ViewHolder holder, XuanZeTiXianType xuanZeTiXianType) {
                PictureUtlis.loadImageViewHolder(mContext, xuanZeTiXianType.getIcon(), (ImageView) holder.getView(R.id.iv_poundage_icon));
                holder.setText(R.id.tv_poundage_card, xuanZeTiXianType.getCard());
                holder.setText(R.id.tv_poundage_money,  "手续费 " + xuanZeTiXianType.getPoundage());
            }
        };

        commonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent;
                if (mData.get(position).getCard().equals("提现到银行卡")) {
                    intent = new Intent(mContext, HongliTiXianActivity.class);
                    intent.putExtra("type","银行卡");
                    startActivity(intent);
                } else if (mData.get(position).getCard().equals("提现到支付宝")) {
                    intent = new Intent(mContext, HongliTiXianActivity.class);
                    intent.putExtra("type","支付宝");
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commonAdapter);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("XuanZeTiXian：", response.toString());
        switch (requestTag) {
            case RequestAction.TAG_GETTIXIANSHOUXUFEI:
                JSONArray array = response.getJSONArray("列表");

                XuanZeTiXianType tiXianType;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    tiXianType = new XuanZeTiXianType();
                    tiXianType.setIcon(json.getString("图标"));
                    tiXianType.setCard(json.getString("类别"));
                    tiXianType.setPoundage(json.getString("手续费"));

                    mData.add(tiXianType);
                }

                commonAdapter.notifyDataSetChanged();

                if (mData.size() < 1) {
                    recyclerView.setVisibility(View.GONE);
                    llXuanzetixianFault.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private class XuanZeTiXianType {
        private String icon;
        private String card;
        private String poundage;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getPoundage() {
            return poundage;
        }

        public void setPoundage(String poundage) {
            this.poundage = poundage;
        }
    }
}
