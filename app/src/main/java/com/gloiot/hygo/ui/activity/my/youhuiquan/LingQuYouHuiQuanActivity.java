package com.gloiot.hygo.ui.activity.my.youhuiquan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class LingQuYouHuiQuanActivity extends BaseActivity {
    @Bind(R.id.lv_lingqu_youhuiquan)
    ListView lvLingquYouhuiquan;

    private CommonAdapter commonAdapter;
    private List<LingQuYouHuiQuanBean> mDatas = new ArrayList<>(5);
    private View viewFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_lingqu_youhuiquan;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "领取优惠券", "", true);

        commonAdapter = new CommonAdapter<LingQuYouHuiQuanBean>(mContext, R.layout.item_list_lingqu_youhuiquan, mDatas) {
            @Override
            public void convert(ViewHolder holder, LingQuYouHuiQuanBean lingQuYouHuiQuanBean) {
                holder.setText(R.id.item_lingqu_youhuiquan_jine, lingQuYouHuiQuanBean.getJinE());
                holder.setText(R.id.item_lingqu_youhuiquan_shangjia, lingQuYouHuiQuanBean.getShangJia());
                holder.setText(R.id.item_lingqu_youhuiquan_tianjian, lingQuYouHuiQuanBean.getTiaoJian());
                holder.setText(R.id.item_lingqu_youhuiquan_shijian, lingQuYouHuiQuanBean.getShiJian());
                holder.setText(R.id.item_lingqu_youhuiquan_shuoming, lingQuYouHuiQuanBean.getShuoming());

                RelativeLayout item_lingqu_youhuiquan_bg = holder.getView(R.id.item_lingqu_youhuiquan_bg);

                if ((lingQuYouHuiQuanBean.getIndex() % 3) == 0) {
                    item_lingqu_youhuiquan_bg.setBackgroundResource(R.mipmap.ic_lingqu_youhuiquan_red);
                } else if ((lingQuYouHuiQuanBean.getIndex() % 3) == 1) {
                    item_lingqu_youhuiquan_bg.setBackgroundResource(R.mipmap.ic_lingqu_youhuiquan_yellow);
                } else if ((lingQuYouHuiQuanBean.getIndex() % 3) == 2) {
                    item_lingqu_youhuiquan_bg.setBackgroundResource(R.mipmap.ic_lingqu_youhuiquan_bule);
                }
            }
        };
        viewFooter = View.inflate(this, R.layout.item_list_lingqu_youhuiquan_footer, null);
        Button btnLingquYouhuiquan = (Button) viewFooter.findViewById(R.id.btn_lingqu_youhuiquan);
        lvLingquYouhuiquan.addFooterView(viewFooter);

        btnLingquYouhuiquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHandleArrayList.add(requestAction.shop_coupon_receive(LingQuYouHuiQuanActivity.this));
            }
        });
        lvLingquYouhuiquan.setAdapter(commonAdapter);

        requestHandleArrayList.add(requestAction.shop_coupon_findAll(this));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_COUPONFINDALL:
                JSONArray jsonArray = response.getJSONArray("优惠券");

                LingQuYouHuiQuanBean lingQuYouHuiQuanBean;
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    lingQuYouHuiQuanBean = new LingQuYouHuiQuanBean();
                    lingQuYouHuiQuanBean.setIndex(i);
                    lingQuYouHuiQuanBean.setJinE(jsonObject.getString("面额"));
                    lingQuYouHuiQuanBean.setTiaoJian(jsonObject.getString("使用条件"));
                    lingQuYouHuiQuanBean.setShiJian(jsonObject.getString("有效期"));
                    lingQuYouHuiQuanBean.setShangJia(jsonObject.getString("商家"));
                    lingQuYouHuiQuanBean.setShuoming(jsonObject.getString("说明"));

                    mDatas.add(lingQuYouHuiQuanBean);
                }
                if (mDatas.size() > 0) {
                    lvLingquYouhuiquan.setVisibility(View.VISIBLE);
                } else {
                    lvLingquYouhuiquan.setVisibility(View.GONE);
                }
                commonAdapter.notifyDataSetChanged();

                break;

            case RequestAction.TAG_COUPONRECEIVE:
                if ("领取成功".equals(response.getString("结果"))) {
                    MyToast.getInstance().showToast(mContext, "领取成功");
                    finish();
                }

                break;
        }
    }

    class LingQuYouHuiQuanBean {
        private int index;
        private String jinE;
        private String shangJia;
        private String tiaoJian;
        private String shiJian;
        private String shuoming;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getJinE() {
            return jinE;
        }

        public void setJinE(String jinE) {
            this.jinE = jinE;
        }

        public String getShangJia() {
            return shangJia;
        }

        public void setShangJia(String shangJia) {
            this.shangJia = shangJia;
        }

        public String getTiaoJian() {
            return tiaoJian;
        }

        public void setTiaoJian(String tiaoJian) {
            this.tiaoJian = tiaoJian;
        }

        public String getShiJian() {
            return shiJian;
        }

        public void setShiJian(String shiJian) {
            this.shiJian = shiJian;
        }

        public String getShuoming() {
            return shuoming;
        }

        public void setShuoming(String shuoming) {
            this.shuoming = shuoming;
        }
    }
}
