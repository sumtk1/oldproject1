package com.gloiot.hygo.ui.activity.my.kefu;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RenGongKeFuActivity extends BaseActivity {

    @Bind(R.id.gv_kehu_fuwu)
    GridView gvKehuFuwu;

    private CommonAdapter fuWuAdapter;
    private List<KeHuFuWuBean> keHuDatas = new ArrayList<>(6);

    @Override
    public int initResource() {
        return R.layout.activity_rengong_kefu;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "人工客服");

        //客户服务  adapter
        fuWuAdapter = new CommonAdapter<KeHuFuWuBean>(this, R.layout.item_kefu_kehufuwu, keHuDatas) {
            @Override
            public void convert(ViewHolder holder, KeHuFuWuBean keHuFuWuBean) {
                ImageView imageView02 = holder.getView(R.id.img_kefu_tu);
                PictureUtlis.loadImageViewHolder(mContext, keHuFuWuBean.getImg_Url(), R.drawable.default_image, imageView02);
                holder.setText(R.id.tv_kefu, keHuFuWuBean.getTitle());
            }
        };
        gvKehuFuwu.setNumColumns(3);
        gvKehuFuwu.setVerticalSpacing(1);
        gvKehuFuwu.setHorizontalSpacing(1);
        gvKehuFuwu.setAdapter(fuWuAdapter);
        CommonUtlis.reMesureGridViewHeight(gvKehuFuwu);
        gvKehuFuwu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("在线".equals(keHuDatas.get(position).getZhuangtai())) {
                    Intent intent = new Intent(mContext, SelectKeFuActivity.class);
                    intent.putExtra("客服组名称", keHuDatas.get(position).getTitle());
                    startActivity(intent);
                } else {
                    DialogUtlis.oneBtnNormal(getmDialog(), "客服专线暂未开通，请将您的问题提交至意见反馈!");
                }
            }
        });

        requestHandleArrayList.add(requestAction.RenGongKeFu(this));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess : " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SERVICE_TYPE:
                //客服
                JSONArray keHuArray = response.getJSONArray("list");
                keHuDatas.clear();
                JSONObject keHuObj;
                KeHuFuWuBean keHuFuWuBean;
                for (int i = 0; i < keHuArray.length(); i++) {
                    keHuObj = keHuArray.getJSONObject(i);
                    keHuFuWuBean = new KeHuFuWuBean();
                    keHuFuWuBean.setTitle(keHuObj.getString("客服组"));
                    keHuFuWuBean.setImg_Url(keHuObj.getString("图标"));
                    keHuFuWuBean.setZhuangtai(keHuObj.getString("状态"));
                    keHuDatas.add(keHuFuWuBean);
                }
                fuWuAdapter.notifyDataSetChanged();
                CommonUtlis.reMesureGridViewHeight(gvKehuFuwu);
                break;
            default:
                break;
        }
    }

    //客户服务Bean
    class KeHuFuWuBean {
        private String Img_Url;
        private String title;
        private String zhuangtai;

        public String getZhuangtai() {
            return zhuangtai;
        }

        public void setZhuangtai(String zhuangtai) {
            this.zhuangtai = zhuangtai;
        }

        public String getImg_Url() {
            return Img_Url;
        }

        public void setImg_Url(String img_Url) {
            Img_Url = img_Url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
