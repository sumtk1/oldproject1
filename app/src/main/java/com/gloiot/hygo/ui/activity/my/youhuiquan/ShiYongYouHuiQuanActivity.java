package com.gloiot.hygo.ui.activity.my.youhuiquan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerenDingdanYouHuiQuanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.YouHuiQuanShiYongBean;
import com.gloiot.hygo.ui.activity.shopping.gouwuche.QuerenDingdanActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShiYongYouHuiQuanActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.lv_shiyong_youhuiquan)
    ListView lvShiyongYouhuiquan;

    @Bind(R.id.bt_apply_shouhou)
    TextView btShenqingShouhou;
    @Bind(R.id.bt_apply_jilu)
    TextView btShenqingJilu;


    private List<YouHuiQuanShiYongBean> mDatas = new ArrayList<>();
    private CommonAdapter commonAdapter;

    private List<YouHuiQuanShiYongBean> data1 = new ArrayList<>();
    private List<YouHuiQuanShiYongBean> data2 = new ArrayList<>();

    private QuerenDingdanYouHuiQuanBean querenDingdanYouHuiQuanBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shiyong_youhuiquan;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleButtonText(this, "可使用", "不可使用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btShenqingShouhou.setTextColor(Color.parseColor("#ffffff"));
                btShenqingJilu.setTextColor(Color.parseColor("#995AFF"));
                btShenqingShouhou.setBackgroundDrawable(ContextCompat.getDrawable(ShiYongYouHuiQuanActivity.this, R.drawable.bg_btn_choose_right));
                btShenqingJilu.setBackgroundDrawable(null);

                mDatas.clear();
                mDatas.addAll(data1);
                commonAdapter.notifyDataSetChanged();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btShenqingShouhou.setTextColor(Color.parseColor("#995AFF"));
                btShenqingJilu.setTextColor(Color.parseColor("#ffffff"));
                btShenqingJilu.setBackgroundDrawable(ContextCompat.getDrawable(ShiYongYouHuiQuanActivity.this, R.drawable.bg_btn_choose_left));
                btShenqingShouhou.setBackgroundDrawable(null);

                mDatas.clear();
                mDatas.addAll(data2);
                commonAdapter.notifyDataSetChanged();
            }
        });

        querenDingdanYouHuiQuanBean = (QuerenDingdanYouHuiQuanBean) getIntent().getSerializableExtra("Obj");

        commonAdapter = new CommonAdapter<YouHuiQuanShiYongBean>(mContext, R.layout.item_shiyong_youhuiquan, mDatas) {
            @Override
            public void convert(ViewHolder holder, final YouHuiQuanShiYongBean youHuiQuanShiYongBean) {
                holder.setText(R.id.item_shiyong_youhuiquan_money, youHuiQuanShiYongBean.getJine());
                holder.setText(R.id.item_shiyong_youhuiquan_tiaojian, youHuiQuanShiYongBean.getTiaojian());
                holder.setText(R.id.item_shiyong_youhuiquan_shuliang, "x" + youHuiQuanShiYongBean.getShuliang());
                holder.setText(R.id.item_shiyong_youhuiquan_shuoming, youHuiQuanShiYongBean.getMiaoshu());
                holder.setText(R.id.item_shiyong_youhuiquan_shangjia, youHuiQuanShiYongBean.getShangjia());
                holder.setText(R.id.item_shiyong_youhuiquan_time, youHuiQuanShiYongBean.getShijian());

                RelativeLayout item_youhuiquan_background = holder.getView(R.id.item_shiyong_youhuiquan_background);

                if ("推荐红包".equals(youHuiQuanShiYongBean.getLeixing())) {
                    holder.getView(R.id.item_shiyong_youhuiquan_shuliang).setVisibility(View.VISIBLE);

                    if ("可使用".equals(youHuiQuanShiYongBean.getZhuangTai())) {
                        item_youhuiquan_background.setBackgroundResource(R.mipmap.ic_wodeyouhuiquan_hongbaoleixing1);
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money)).setTextColor(Color.parseColor("#9c6eff"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money_fuhao)).setTextColor(Color.parseColor("#9c6eff"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_shuliang)).setTextColor(Color.parseColor("#9c6eff"));
                    } else if ("不可使用".equals(youHuiQuanShiYongBean.getZhuangTai())) {
                        item_youhuiquan_background.setBackgroundResource(R.mipmap.ic_wodeyouhuiquan_hongbaoleixing1_no);

                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money)).setTextColor(Color.parseColor("#999999"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money_fuhao)).setTextColor(Color.parseColor("#999999"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_shuliang)).setTextColor(Color.parseColor("#999999"));
                    }

                } else {
                    holder.getView(R.id.item_shiyong_youhuiquan_shuliang).setVisibility(View.GONE);

                    if ("可使用".equals(youHuiQuanShiYongBean.getZhuangTai())) {
                        item_youhuiquan_background.setBackgroundResource(R.mipmap.ic_wodeyouhuiquan_hongbaoleixing2);

                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money)).setTextColor(Color.parseColor("#fb4e44"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money_fuhao)).setTextColor(Color.parseColor("#fb4e44"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_shuliang)).setTextColor(Color.parseColor("#fb4e44"));
                    } else if ("不可使用".equals(youHuiQuanShiYongBean.getZhuangTai())) {
                        item_youhuiquan_background.setBackgroundResource(R.mipmap.ic_wodeyouhuiquan_hongbaoleixing2_no);

                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money)).setTextColor(Color.parseColor("#999999"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_money_fuhao)).setTextColor(Color.parseColor("#999999"));
                        ((TextView) holder.getView(R.id.item_shiyong_youhuiquan_shuliang)).setTextColor(Color.parseColor("#999999"));
                    }
                }
            }
        };
        lvShiyongYouhuiquan.setAdapter(commonAdapter);

        lvShiyongYouhuiquan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("可使用".equals(mDatas.get(position).getZhuangTai())) {
                    querenDingdanYouHuiQuanBean.setYouHuiQuanShiYongBean(mDatas.get(position));

                    setResult(2, new Intent(ShiYongYouHuiQuanActivity.this, QuerenDingdanActivity.class).
                            putExtra("Obj", (Serializable) querenDingdanYouHuiQuanBean));

                    finish();
                } else {
                    return;
                }
            }
        });

        try {
            JSONObject jsonObject = new JSONObject(querenDingdanYouHuiQuanBean.getInfo());

            JSONObject keyongObj = jsonObject.getJSONObject("可用优惠券");
            JSONArray keyongArray = keyongObj.getJSONArray("列表");
            YouHuiQuanShiYongBean youHuiQuanShiYongBean1;
            JSONObject obj;
            for (int i = 0; i < keyongArray.length(); i++) {
                obj = keyongArray.getJSONObject(i);
                youHuiQuanShiYongBean1 = new YouHuiQuanShiYongBean();
                youHuiQuanShiYongBean1.setYouhuiquanID(obj.getString("优惠券id"));
                youHuiQuanShiYongBean1.setShanjiaZhanghao(obj.getString("商家账号"));
                youHuiQuanShiYongBean1.setShuliang(obj.getString("数量"));
                youHuiQuanShiYongBean1.setMingcheng(obj.getString("优惠券名称"));
                youHuiQuanShiYongBean1.setJine(obj.getString("面额"));
                youHuiQuanShiYongBean1.setLeixing(obj.getString("类型"));
                youHuiQuanShiYongBean1.setTiaojian(obj.getString("使用条件"));
                youHuiQuanShiYongBean1.setShijian(obj.getString("有效期"));
                youHuiQuanShiYongBean1.setMiaoshu(obj.getString("说明"));
                youHuiQuanShiYongBean1.setShangjia(obj.getString("商家"));
                youHuiQuanShiYongBean1.setZhuangTai("可使用");

                data1.add(youHuiQuanShiYongBean1);
            }

            JSONObject bukeyongObj = jsonObject.getJSONObject("不可用优惠券");
            JSONArray bukeyongArray = bukeyongObj.getJSONArray("列表");
            YouHuiQuanShiYongBean youHuiQuanShiYongBean2;
            JSONObject obj1;
            for (int i = 0; i < bukeyongArray.length(); i++) {
                obj1 = bukeyongArray.getJSONObject(i);
                youHuiQuanShiYongBean2 = new YouHuiQuanShiYongBean();
                youHuiQuanShiYongBean2.setYouhuiquanID(obj1.getString("优惠券id"));
                youHuiQuanShiYongBean2.setShanjiaZhanghao(obj1.getString("商家账号"));
                youHuiQuanShiYongBean2.setShuliang(obj1.getString("数量"));
                youHuiQuanShiYongBean2.setMingcheng(obj1.getString("优惠券名称"));
                youHuiQuanShiYongBean2.setJine(obj1.getString("面额"));
                youHuiQuanShiYongBean2.setLeixing(obj1.getString("类型"));
                youHuiQuanShiYongBean2.setTiaojian(obj1.getString("使用条件"));
                youHuiQuanShiYongBean2.setShijian(obj1.getString("有效期"));
                youHuiQuanShiYongBean2.setMiaoshu(obj1.getString("说明"));
                youHuiQuanShiYongBean2.setShangjia(obj1.getString("商家"));
                youHuiQuanShiYongBean2.setZhuangTai("不可使用");

                data2.add(youHuiQuanShiYongBean2);
            }
            mDatas.addAll(data1);
            commonAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.toptitle_back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toptitle_back:
                setResult(2, new Intent(ShiYongYouHuiQuanActivity.this, QuerenDingdanActivity.class).
                        putExtra("Obj", (Serializable) querenDingdanYouHuiQuanBean));

                finish();
                break;
        }
    }
}
