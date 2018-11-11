package com.gloiot.hygo.ui.activity.shopping.wodedingdan;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.WuliuBean;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class ChakanWuliuActicity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_chakan_wuliu_chengyun_laiyuan)
    TextView tv_chakan_wuliu_chengyun_laiyuan;
    @Bind(R.id.tv_chakan_wuliu_yundan_bianhao)
    TextView tv_chakan_wuliu_yundan_bianhao;
    @Bind(R.id.tv_chakan_wuliu_guanfang_dianhua)
    TextView tv_chakan_wuliu_guanfang_dianhua;
    @Bind(R.id.lv_chakan_wuliu)
    ListView lv_chakan_wuliu;
    @Bind(R.id.ll_nowuliu)
    RelativeLayout llNowuliu;
    @Bind(R.id.tv_tuidang)
    TextView tvTuidang;
    @Bind(R.id.rl_chakan_wuliu_cuidan)
    RelativeLayout rlChakanWuliuCuidan;


    private String dingdanid, suolvtu, wuliu, kuaidigongsi, kuaididanhao, shouhuoren, guanfangdianhua;
    private String shangPiId = "";
    private String wuliuLeixing = "";
    private String dingdanXiaoshouID = "";
    private List<WuliuBean> allWuliu = new ArrayList<>();
    private CommonAdapter adapter;

    @Override
    public int initResource() {
        return R.layout.activity_chakan_wuliu_new;
    }

    @Override
    public void initData() {
        mContext = this;

        CommonUtlis.setTitleBar(this, "查看物流");

        dingdanid = getIntent().getStringExtra("id");
        shangPiId = getIntent().getStringExtra("商品id");
        wuliuLeixing = getIntent().getStringExtra("物流类型");
        dingdanXiaoshouID = getIntent().getExtras().getString("订单销售id","");

        requestHandleArrayList.add(requestAction.shop_kuaidi_query(ChakanWuliuActicity.this, dingdanid, shangPiId, wuliuLeixing, dingdanXiaoshouID));

        requestErrorCallback = new RequestErrorCallback() {
            @Override
            public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
                switch (requestTag) {
                    case RequestAction.TAG_CHAKANWULIU:
                        llNowuliu.setVisibility(View.VISIBLE);
                        lv_chakan_wuliu.setVisibility(View.GONE);
                }
            }
        };

    }


    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_CHAKANWULIU:

                if (jsonObject.getString("状态").equals("成功")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("快递");

                    JSONObject obj = jsonObject.getJSONObject("列表");
                    shouhuoren = obj.getString("收货人");
                    kuaidigongsi = obj.getString("快递公司");
                    kuaididanhao = obj.getString("快递单号");
                    guanfangdianhua = obj.getString("联系方式");
                    wuliu = obj.getString("物流状态");
                    tv_chakan_wuliu_chengyun_laiyuan.setText(kuaidigongsi);
                    tv_chakan_wuliu_yundan_bianhao.setText(kuaididanhao);
                    tv_chakan_wuliu_guanfang_dianhua.setText(guanfangdianhua);

                    if (jsonArray.length() > 0) {

                        llNowuliu.setVisibility(View.GONE);
                        lv_chakan_wuliu.setVisibility(View.VISIBLE);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject kuaidiObj = jsonArray.getJSONObject(i);

                            String time = kuaidiObj.getString("time");
                            String context = kuaidiObj.getString("context");
                            String ftime = kuaidiObj.getString("ftime");

                            WuliuBean wuliuBean = new WuliuBean(time, context, ftime);
                            allWuliu.add(wuliuBean);
                        }
                        allWuliu.get(0).setFirst(true);
                        adapter = new CommonAdapter<WuliuBean>(this, R.layout.item_dingdan_chakanwuliu, allWuliu) {
                            @Override
                            public void convert(ViewHolder holder, WuliuBean wuliuBean) {

                                if (holder.getmPosition() == 0) {
                                    ImageView view = holder.getView(R.id.item_dingdan_chakanwuliu_dian);
                                    if (view.getTag() != null) {
                                        if (view.getTag().equals("first")) {
                                            holder.setBackgroundColor(R.id.item_dingdan_chakanwuliu_xian, Color.rgb(255, 255, 255));
                                            holder.setImageResource(R.id.item_dingdan_chakanwuliu_dian, R.mipmap.ic_wancheng);
                                        }
                                    } else {
                                        holder.setBackgroundColor(R.id.item_dingdan_chakanwuliu_xian, Color.rgb(238, 238, 238));
                                        holder.setImageResource(R.id.item_dingdan_chakanwuliu_dian, R.mipmap.ic_quanquan);
                                        view.setTag("first");
                                    }
                                } else {
                                    holder.setBackgroundColor(R.id.item_dingdan_chakanwuliu_xian, Color.rgb(153, 153, 153));
                                    holder.setImageResource(R.id.item_dingdan_chakanwuliu_dian, R.mipmap.ic_quanquan);
                                }
                                holder.setText(R.id.tv_chakan_wuliu_context, wuliuBean.getContext());
                                holder.setText(R.id.tv_chakan_wuliu_time, wuliuBean.getTime());
                            }
                        };
                        lv_chakan_wuliu.setAdapter(adapter);
                    } else {
                        llNowuliu.setVisibility(View.VISIBLE);
                        lv_chakan_wuliu.setVisibility(View.GONE);
                    }
                }
                break;
            case RequestAction.TAG_REMINDER:
                MyToast.getInstance().showToast(mContext, "催单成功");
                Log.e("code", jsonObject.toString());
                if (jsonObject.getString("code").equals("1")) {
                    Log.e("code==", jsonObject.getString("code"));
                    rlChakanWuliuCuidan.setEnabled(false);
                    rlChakanWuliuCuidan.setBackgroundResource(R.drawable.bg_rl_cuidan_no);
                    tvTuidang.setTextColor(Color.parseColor("#999999"));
                    tvTuidang.setText("已催单");
                } else {
                    rlChakanWuliuCuidan.setEnabled(true);
                }
                break;
        }

    }

    @OnClick({R.id.rl_chakan_wuliu_cuidan, R.id.iv_toptitle_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //催单按钮
            case R.id.rl_chakan_wuliu_cuidan:
                requestHandleArrayList.add(requestAction.shop_oinfo_reminder(this, kuaididanhao));
                break;
            case R.id.iv_toptitle_back:
                finish();
                break;
        }

    }


//    @Override
//    public void onSuccess(int requestTag, JSONObject jsonObject, int showLoad) {
//        if (showLoad == 0 || showLoad == 2) {
//            LoadDialog.dismiss(mContext);
//        }
//        switch (requestTag) {
//            case RequestAction.TAG_CHAKANWULIU:
//                Log.e("查看物流请求成功", jsonObject.toString() + "-----------");
//                try {
//                    if (jsonObject.getString("状态").equals("成功")) {
//
//                        llNowuliu.setVisibility(View.GONE);
//                        lv_chakan_wuliu.setVisibility(View.VISIBLE);
//                        rlChakanWuliu.setVisibility(View.VISIBLE);
//
//                        JSONObject obj = jsonObject.getJSONObject("列表");
//                        shouhuoren = obj.getString("收货人");
//                        kuaidigongsi = obj.getString("快递公司");
//                        kuaididanhao = obj.getString("快递单号");
//                        guanfangdianhua = obj.getString("联系方式");
//                        wuliu = obj.getString("物流状态");
//
//                        tv_chakan_wuliu_zhuangtai.setText(wuliu);
//                        tv_chakan_wuliu_chengyun_laiyuan.setText("承运来源: " + kuaidigongsi);
//                        tv_chakan_wuliu_yundan_bianhao.setText("运单编号: " + kuaididanhao);
//                        tv_chakan_wuliu_guanfang_dianhua.setText("官方电话: " + guanfangdianhua);
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("快递");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject kuaidiObj = jsonArray.getJSONObject(i);
//
//                            String time = kuaidiObj.getString("time");
//                            String context = kuaidiObj.getString("context");
//                            String ftime = kuaidiObj.getString("ftime");
//
//                            WuliuBean wuliuBean = new WuliuBean(time, context, ftime);
//                            allWuliu.add(wuliuBean);
//                        }
//                        allWuliu.get(0).setFirst(true);
//                        Log.e("物流ALLWULIU数据", allWuliu.size() + "----------------" + allWuliu.toString());
//                        adapter = new CommonAdapter<WuliuBean>(this, R.layout.item_dingdan_chakanwuliu, allWuliu) {
//
//                            @Override
//                            public void convert(ViewHolder holder, WuliuBean wuliuBean) {
//                                Log.e("设置查看物流数据", wuliuBean.toString());
//
//                                if (holder.getmPosition() == 0) {
//                                    ImageView view = holder.getView(R.id.item_dingdan_chakanwuliu_dian);
//                                    if (view.getTag() != null) {
//                                        if (view.getTag().equals("first")) {
//                                            holder.setBackgroundColor(R.id.item_dingdan_chakanwuliu_xian, Color.rgb(255, 255, 255));
//                                            holder.setImageResource(R.id.item_dingdan_chakanwuliu_dian, R.mipmap.ic_wancheng);
//                                        }
//                                    } else {
//                                        holder.setBackgroundColor(R.id.item_dingdan_chakanwuliu_xian, Color.rgb(238, 238, 238));
//                                        holder.setImageResource(R.id.item_dingdan_chakanwuliu_dian, R.mipmap.ic_quanquan);
//                                        view.setTag("first");
//                                    }
//                                } else {
//                                    holder.setBackgroundColor(R.id.item_dingdan_chakanwuliu_xian, Color.rgb(153, 153, 153));
//                                    holder.setImageResource(R.id.item_dingdan_chakanwuliu_dian, R.mipmap.ic_quanquan);
//                                }
//                                holder.setText(R.id.tv_chakan_wuliu_context, wuliuBean.getContext());
//                                holder.setText(R.id.tv_chakan_wuliu_time, wuliuBean.getTime());
//                            }
//                        };
//                        lv_chakan_wuliu.setAdapter(adapter);
//
//                    } else if (jsonObject.getString("状态").equals("随机码不正确")) {
//
//                        DialogUtlis.oneBtnNormal(mContext, "该账号在其他设备登录\n请重新登录", "确定", false, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                DialogUtlis.dismissDialog();
//                                CommonUtlis.clearPersonalData();//清除账号个人数据，重置登录状态等
//                                Intent intent = new Intent(mContext, LoginActivity.class);
//                                mContext.startActivity(intent);
//                                App.getInstance().mActivityStack.getLastActivity().finish();
//                                System.exit(0);
//                                ((Activity) mContext).overridePendingTransition(R.anim.activity_open, 0);
//                            }
//                        });
//                    } else {
//                        if (requestErrorCallback != null) {
//                            requestErrorCallback.requestErrorcallback(requestTag, jsonObject);
//                        } else {
//                            llNowuliu.setVisibility(View.VISIBLE);
//                            lv_chakan_wuliu.setVisibility(View.GONE);
//                            rlChakanWuliu.setVisibility(View.GONE);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    DialogUtlis.oneBtnNormal(mContext, "数据异常-1\n请稍后再试");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    DialogUtlis.oneBtnNormal(mContext, "数据异常-2\n请稍后再试");
//                }
//                break;
//        }
//    }

}
