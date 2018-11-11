package com.gloiot.hygo.ui.activity.my.kefu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.social.ConversationActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.SpaceItemDecorationGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SelectKeFuActivity extends BaseActivity {

    @Bind(R.id.rv_select_kefu)
    RecyclerView rvSelectKefu;

    private CommonAdapter commonAdapter;
    private List<KeFuBean> keFuDatas = new ArrayList<>(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_select_kefu;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "请选择客服", "", true);
        String keFuZu = getIntent().getStringExtra("客服组名称");

        commonAdapter = new CommonAdapter<KeFuBean>(this, R.layout.item_select_kefu, keFuDatas) {
            @Override
            public void convert(ViewHolder holder, KeFuBean keFuBean) {
                holder.setText(R.id.tv_select_kefu, keFuBean.getXingming());
            }
        };
        rvSelectKefu.setLayoutManager(new GridLayoutManager(this, 2));
        rvSelectKefu.addItemDecoration(new SpaceItemDecorationGridView(2, 2));
        rvSelectKefu.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(new Intent(mContext, ConversationActivity.class));
                intent.putExtra("receiveId", keFuDatas.get(position).getZhangHao());
                intent.putExtra("name", keFuDatas.get(position).getNickName());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });


        requestHandleArrayList.add(requestAction.SelectKeFu(this, keFuZu));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess : " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SELECT_KEFU:
                keFuDatas.clear();
                JSONArray jsonArray = response.getJSONArray("数据");
                JSONObject jsonObject;
                KeFuBean keFuBean;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    keFuBean = new KeFuBean();
                    keFuBean.setNickName(jsonObject.getString("昵称"));
                    keFuBean.setZhangHao(jsonObject.getString("账号"));
                    keFuBean.setXingming(jsonObject.getString("姓名"));
                    keFuDatas.add(keFuBean);
                }
                commonAdapter.notifyDataSetChanged();
                break;
        }
    }

    class KeFuBean {
        private String zhangHao;
        private String nickName;
        private String xingming;

        public String getZhangHao() {
            return zhangHao;
        }

        public void setZhangHao(String zhangHao) {
            this.zhangHao = zhangHao;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getXingming() {
            return xingming;
        }

        public void setXingming(String xingming) {
            this.xingming = xingming;
        }
    }
}
