package com.gloiot.hygo.ui.activity.my.youhuiquan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.SheZhiActivity;
import com.gloiot.hygo.ui.activity.my.ziliao.FenXiangEWeiMaActivity;
import com.gloiot.hygo.ui.activity.shopping.dianpu.ShangJiaDianPuActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class WoDeYouHuiQuanActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.pullList_wode_youhuiquan)
    PullableListView pullListWodeYouhuiquan;
    @Bind(R.id.prl_wode_youhuiquan)
    PullToRefreshLayout prlWodeYouhuiquan;

    private List<YouHuiQuanBean> mDatas = new ArrayList<>();
    private CommonAdapter commonAdapter;

    private int Loadlength, XiayiyeYeshu = 0;
    private boolean refreshOrLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_wode_youhuiquan;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "我的优惠券", "", true);

        prlWodeYouhuiquan.setOnRefreshListener(this);
        commonAdapter = new CommonAdapter<YouHuiQuanBean>(mContext, R.layout.item_wode_youhuiquan, mDatas) {
            @Override
            public void convert(ViewHolder holder, final YouHuiQuanBean youHuiQuanBean) {
                holder.setText(R.id.item_youhuiquan_money, youHuiQuanBean.getJine());
                holder.setText(R.id.item_youhuiquan_tiaojian, youHuiQuanBean.getTiaojian());
                holder.setText(R.id.item_youhuiquan_shuliang, "x" + youHuiQuanBean.getShuliang());
                holder.setText(R.id.item_youhuiquan_shuoming, youHuiQuanBean.getMiaoshu());
                holder.setText(R.id.item_youhuiquan_time, youHuiQuanBean.getShijian());

                if ("自营".equals(youHuiQuanBean.getShangjia_type())) {
                    holder.setText(R.id.item_youhuiquan_shangjia, "只限环游购自营店使用");
                } else if ("商家".equals(youHuiQuanBean.getShangjia_type())) {
                    holder.setText(R.id.item_youhuiquan_shangjia, "只限" + youHuiQuanBean.getDianPu_mingchen() + "店使用");
                }

                RelativeLayout item_youhuiquan_background = holder.getView(R.id.item_youhuiquan_background);
                ImageView item_youhuiquan_zhuangtai = holder.getView(R.id.item_youhuiquan_zhuangtai);
                TextView item_youhuiquan_shiyong = holder.getView(R.id.item_youhuiquan_shiyong);


                if ("快过期".equals(youHuiQuanBean.getZhuangtai())) {
                    item_youhuiquan_zhuangtai.setImageResource(R.mipmap.ic_wodeyouhuiquan_kuaiguoqi);
                    item_youhuiquan_zhuangtai.setVisibility(View.VISIBLE);
                    item_youhuiquan_shiyong.setVisibility(View.VISIBLE);
                } else if ("已使用".equals(youHuiQuanBean.getZhuangtai())) {
                    item_youhuiquan_zhuangtai.setImageResource(R.mipmap.ic_wodeyouhuiquan_yishiyong);
                    item_youhuiquan_zhuangtai.setVisibility(View.VISIBLE);
                    item_youhuiquan_shiyong.setVisibility(View.GONE);
                } else if ("已过期".equals(youHuiQuanBean.getZhuangtai())) {
                    item_youhuiquan_zhuangtai.setImageResource(R.mipmap.ic_wodeyouhuiquan_yiguoqi);
                    item_youhuiquan_zhuangtai.setVisibility(View.VISIBLE);
                    item_youhuiquan_shiyong.setVisibility(View.GONE);
                } else if ("正常".equals(youHuiQuanBean.getZhuangtai())) {
                    item_youhuiquan_zhuangtai.setVisibility(View.GONE);
                    item_youhuiquan_shiyong.setVisibility(View.VISIBLE);
                } else if ("已停止".equals(youHuiQuanBean.getZhuangtai())) {
                    item_youhuiquan_zhuangtai.setImageResource(R.mipmap.ic_wodeyouhuiquan_yitingzhi);
                    item_youhuiquan_zhuangtai.setVisibility(View.VISIBLE);
                    item_youhuiquan_shiyong.setVisibility(View.GONE);
                } else {
                    item_youhuiquan_zhuangtai.setVisibility(View.GONE);
                    item_youhuiquan_shiyong.setVisibility(View.GONE);
                }

                if ("推荐红包".equals(youHuiQuanBean.getLeixing())) {
                    ((TextView) holder.getView(R.id.item_youhuiquan_money)).setTextColor(Color.parseColor("#9c6eff"));
                    ((TextView) holder.getView(R.id.item_youhuiquan_money_fuhao)).setTextColor(Color.parseColor("#9c6eff"));
                    ((TextView) holder.getView(R.id.item_youhuiquan_shuliang)).setTextColor(Color.parseColor("#9c6eff"));
                    ((TextView) holder.getView(R.id.item_youhuiquan_shuliang)).setVisibility(View.VISIBLE);
                    item_youhuiquan_background.setBackgroundResource(R.mipmap.ic_wodeyouhuiquan_hongbaoleixing1);
                } else {
                    ((TextView) holder.getView(R.id.item_youhuiquan_money)).setTextColor(Color.parseColor("#fb4e44"));
                    ((TextView) holder.getView(R.id.item_youhuiquan_money_fuhao)).setTextColor(Color.parseColor("#fb4e44"));
                    ((TextView) holder.getView(R.id.item_youhuiquan_shuliang)).setTextColor(Color.parseColor("#fb4e44"));
                    ((TextView) holder.getView(R.id.item_youhuiquan_shuliang)).setVisibility(View.GONE);
                    item_youhuiquan_background.setBackgroundResource(R.mipmap.ic_wodeyouhuiquan_hongbaoleixing2);
                }


                //点击立即使用进入购物页面
                item_youhuiquan_shiyong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("自营".equals(youHuiQuanBean.getShangjia_type())) {
                            setResult(0x668, new Intent(WoDeYouHuiQuanActivity.this, MainActivity.class));
                            finish();
                        } else if ("商家".equals(youHuiQuanBean.getShangjia_type())) {
                            startActivity(new Intent(mContext, ShangJiaDianPuActivity.class).putExtra("店铺id", youHuiQuanBean.getDianpu_id()));
                            finish();
                        }
                    }
                });
            }
        };
        pullListWodeYouhuiquan.setAdapter(commonAdapter);

        //0为普通请求
        initListData(0);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess :" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_GETMYDISCOUNTCOUPON:
                if (refreshOrLoad) {
                    Log.e("清除了数据", "true");
                    mDatas.clear();
                    commonAdapter.notifyDataSetChanged();
                } else {
                    Log.e("请求成功", "ptrl_wodeshoucang");
                    prlWodeYouhuiquan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }

                XiayiyeYeshu = response.getInt("页数");
                Loadlength = response.getInt("条数");

                JSONArray liebiaoArray = response.getJSONArray("列表");
                YouHuiQuanBean youHuiQuanBean;
                for (int i = 0; i < liebiaoArray.length(); i++) {
                    JSONObject jsonObject = liebiaoArray.getJSONObject(i);
                    youHuiQuanBean = new YouHuiQuanBean();
                    youHuiQuanBean.setZhuangtai(jsonObject.getString("状态"));
                    youHuiQuanBean.setJine(jsonObject.getString("面额"));
                    youHuiQuanBean.setTiaojian(jsonObject.getString("使用条件"));
                    youHuiQuanBean.setLeixing(jsonObject.getString("类型"));
                    youHuiQuanBean.setShijian(jsonObject.getString("有效期"));
                    youHuiQuanBean.setShuliang(jsonObject.getString("数量"));
                    youHuiQuanBean.setMiaoshu(jsonObject.getString("说明"));
                    youHuiQuanBean.setShangjia(jsonObject.getString("商家"));
                    youHuiQuanBean.setDianpu_id(jsonObject.getString("店铺id"));
                    youHuiQuanBean.setShangjia_type(jsonObject.getString("账号类别"));
                    youHuiQuanBean.setDianPu_mingchen(jsonObject.getString("店铺名称"));
                    mDatas.add(youHuiQuanBean);
                }
                commonAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 数据请求
     *
     * @param requestType 当数据位0时表示普通请求，为1时表示刷新请求，为2时表示加载请求
     */
    private void initListData(int requestType) {
        requestHandleArrayList.add(requestAction.GetMyDiscountCoupon(this, prlWodeYouhuiquan, requestType, String.valueOf(XiayiyeYeshu)));
    }

    @OnClick({R.id.rl_wode_youhuiquan_tuijian})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_wode_youhuiquan_tuijian:
                if (preferences.getString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "").equals("否")) {
                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "必须先绑定微信才能分享二维码~", true, "取消", "去绑定",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dismissDialog();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    Intent intent = new Intent(mContext, SheZhiActivity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                } else {
                    startActivity(new Intent(WoDeYouHuiQuanActivity.this, FenXiangEWeiMaActivity.class));
                }
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshOrLoad = true;
        XiayiyeYeshu = 0;
        initListData(1);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        //确保有数据才设置上拉加载更多监听
        if (Loadlength > 9) {
            refreshOrLoad = false;
            initListData(2);
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }

    class YouHuiQuanBean {
        //金额
        private String jine;
        //满减条件
        private String tiaojian;
        //红包描述
        private String miaoshu;
        //使用时间
        private String shijian;
        //红包状态（快过期、已使用、已过期）
        private String zhuangtai;
        //红包类型
        private String leixing;
        //数量
        private String shuliang;
        //商家限制
        private String shangjia;

        private String dianpu_id;

        private String shangjia_type;

        private String dianPu_mingchen;

        public String getJine() {
            return jine;
        }

        public void setJine(String jine) {
            this.jine = jine;
        }

        public String getTiaojian() {
            return tiaojian;
        }

        public void setTiaojian(String tiaojian) {
            this.tiaojian = tiaojian;
        }

        public String getMiaoshu() {
            return miaoshu;
        }

        public void setMiaoshu(String miaoshu) {
            this.miaoshu = miaoshu;
        }

        public String getZhuangtai() {
            return zhuangtai;
        }

        public void setZhuangtai(String zhuangtai) {
            this.zhuangtai = zhuangtai;
        }

        public String getShijian() {
            return shijian;
        }

        public void setShijian(String shijian) {
            this.shijian = shijian;
        }

        public String getShuliang() {
            return shuliang;
        }

        public void setShuliang(String shuliang) {
            this.shuliang = shuliang;
        }

        public String getLeixing() {
            return leixing;
        }

        public void setLeixing(String leixing) {
            this.leixing = leixing;
        }

        public String getShangjia() {
            return shangjia;
        }

        public void setShangjia(String shangjia) {
            this.shangjia = shangjia;
        }

        public String getDianpu_id() {
            return dianpu_id;
        }

        public void setDianpu_id(String dianpu_id) {
            this.dianpu_id = dianpu_id;
        }

        public String getShangjia_type() {
            return shangjia_type;
        }

        public void setShangjia_type(String shangjia_type) {
            this.shangjia_type = shangjia_type;
        }

        public String getDianPu_mingchen() {
            return dianPu_mingchen;
        }

        public void setDianPu_mingchen(String dianPu_mingchen) {
            this.dianPu_mingchen = dianPu_mingchen;
        }
    }
}
