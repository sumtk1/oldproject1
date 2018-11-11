package com.gloiot.hygo.ui.activity.my.shouhou;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.widge.EmptyLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by dwj on 2017/6/19
 * 申请售后/申请记录
 */

public class ShouHouActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.pull_shouhou_dingdan)
    PullableListView pull_shouhou_dingdan;
    @Bind(R.id.ptrl_shouhou_dingdan)
    PullToRefreshLayout ptrl_shouhou_dingdan;

    @Bind(R.id.rl_dingdan_nothing)
    RelativeLayout rl_dingdan_nothing;
    @Bind(R.id.bt_apply_shouhou)
    TextView btShenqingShouhou;
    @Bind(R.id.bt_apply_jilu)
    TextView btShenqingJilu;
    @Bind(R.id.in_titlebar)
    View inTitlebar;
    @Bind(R.id.emptylayout_shouhou)
    EmptyLayout emptylayout_shouhou;


    private static final String TAG = "ShouHouActivity: ";

    private List<ShouHouBean> listShouhouDatas = new ArrayList<>(); //用于显示
    private List<ShouHouBean> listJiluDatas = new ArrayList<>(); //申请记录数据
    private List<ShouHouBean> listShenqingDatas = new ArrayList<>(); //申请售后数据
    private ShouHouAdapter mShouhouAdapter;

    private int page = 0, row = 0; //页数 条数
    private String strSQWhat = "售后申请"; //要获取的是售后的数据，还是已经申请过的记录
    private boolean isRefresh = true;

    @Override
    public int initResource() {
        return R.layout.activity_my_applyshouhou;
    }

    @Override
    public void initData() {

        CommonUtlis.setTitleButtonText(this, "申请售后", "申请记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"售后申请".equals(strSQWhat)) {
                    btShenqingShouhou.setTextColor(Color.parseColor("#ffffff"));
                    btShenqingJilu.setTextColor(Color.parseColor("#995AFF"));
                    btShenqingShouhou.setBackgroundDrawable(ContextCompat.getDrawable(ShouHouActivity.this, R.drawable.bg_btn_choose_left));
                    btShenqingJilu.setBackgroundDrawable(null);

                    strSQWhat = "售后申请";
                    page = 0;
//                    isRefresh = true;
                    qingQiuShuJu(0);
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"申请记录".equals(strSQWhat)) {
                    btShenqingShouhou.setTextColor(Color.parseColor("#995AFF"));
                    btShenqingJilu.setTextColor(Color.parseColor("#ffffff"));
                    btShenqingShouhou.setBackgroundDrawable(null);
                    btShenqingJilu.setBackgroundDrawable(ContextCompat.getDrawable(ShouHouActivity.this, R.drawable.bg_btn_choose_right));

                    strSQWhat = "申请记录";
                    page = 0;
//                    isRefresh = true;
                    qingQiuShuJu(0);
                }
            }
        });

        initComponent();
        ptrl_shouhou_dingdan.setOnRefreshListener(this);
    }

    public void initComponent() {

        inTitlebar.findViewById(R.id.toptitle_back).setOnClickListener(this);
        mShouhouAdapter = new ShouHouAdapter(this, listShouhouDatas);
        pull_shouhou_dingdan.setAdapter(mShouhouAdapter);

        //错误页面
        emptylayout_shouhou.setErrorDrawable(R.mipmap.img_error_layout);
        emptylayout_shouhou.setErrorMessage("网络出错了");
        emptylayout_shouhou.setErrorViewButtonId(R.id.buttonError);
        emptylayout_shouhou.setShowErrorButton(true);
        emptylayout_shouhou.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qingQiuShuJu(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toptitle_back:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void showNothingView(boolean flag) {
        if (flag) {
            emptylayout_shouhou.setVisibility(View.GONE);
            rl_dingdan_nothing.setVisibility(View.VISIBLE);
        } else {
            emptylayout_shouhou.setVisibility(View.VISIBLE);
            rl_dingdan_nothing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isRefresh = true;
        page = 0;
        qingQiuShuJu(0);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("requestSuccess  shouhou" + requestTag, response.toString());
        switch (requestTag) {
            //售后申请
            case RequestAction.TAG_SHOUHOU:

                if (isRefresh) {
                    listShenqingDatas.clear();
                } else {
                    ptrl_shouhou_dingdan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                //处理数据
                getSqShouhouDatas(response, listShenqingDatas);
                listShouhouDatas.clear();
                listShouhouDatas.addAll(listShenqingDatas);
                mShouhouAdapter.notifyDataSetChanged();

                if (listShouhouDatas.size() > 0) {
                    showNothingView(false);
                } else {
                    showNothingView(true);
                }
                break;
            //申请记录
            case RequestAction.TAG_SHOUHOU_JILU:

                if (isRefresh) {
                    listJiluDatas.clear();
                } else {
                    ptrl_shouhou_dingdan.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }

                getSqShouhouDatas(response, listJiluDatas);
                listShouhouDatas.clear();
                listShouhouDatas.addAll(listJiluDatas);
                mShouhouAdapter.notifyDataSetChanged();

                if (listShouhouDatas.size() > 0) {
                    showNothingView(false);
                } else {
                    showNothingView(true);
                }
                break;
        }
    }

    /**
     * 获取售后申请的数据
     *
     * @param response
     */
    private void getSqShouhouDatas(JSONObject response, List<ShouHouBean> list) {
        try {
            row = response.getInt("row");
            page = row == 10 ? response.getInt("page") : 0;
            if (row != 0) {
                JSONArray jsonArray = response.getJSONArray("list");
                ShouHouBean shenqingShouhouBean;
                JSONObject jsonObj;
                String leibie;
                String zhuangtai;
                for (int i = 0; i < row; i++) {
                    shenqingShouhouBean = new ShouHouBean();
                    jsonObj = (JSONObject) jsonArray.get(i);
//                    leibie = jsonObj.getString("类别");
                    leibie = JSONUtlis.getString(jsonObj, "类别");
                    zhuangtai = jsonObj.getString("状态");

                    shenqingShouhouBean.setLeibie(leibie);
                    shenqingShouhouBean.setZhuangtai(zhuangtai);
                    shenqingShouhouBean.setDingdanId(jsonObj.getString("订单id"));
                    shenqingShouhouBean.setDianpuMing(jsonObj.getString("店铺名"));
                    shenqingShouhouBean.setShangpinId(jsonObj.getString("商品id"));
                    shenqingShouhouBean.setId(jsonObj.getString("id"));
                    shenqingShouhouBean.setZhongleixiangxi(jsonObj.getString("种类详细"));
                    shenqingShouhouBean.setXiadanShijian(jsonObj.getString("下单时间"));
                    shenqingShouhouBean.setSuoLueTu(jsonObj.getString("缩略图"));
                    shenqingShouhouBean.setShangpinMingchen(jsonObj.getString("商品名称"));
                    shenqingShouhouBean.setShangpinShuliang(jsonObj.getString("商品数量"));
                    shenqingShouhouBean.setHeji(jsonObj.getString("合计"));
                    shenqingShouhouBean.setDingdanZhuangtai(jsonObj.getString("订单状态"));
                    shenqingShouhouBean.setIsCheXiao(jsonObj.getString("是否撤销"));
                    if ("申请记录".equals(strSQWhat)) {
                        shenqingShouhouBean.setShouhouShuoming(jsonObj.getString("售后说明"));
                    }
                    list.add(shenqingShouhouBean);
                }

                pull_shouhou_dingdan.setVisibility(View.VISIBLE);
            } else {
                if (isRefresh) {
                    pull_shouhou_dingdan.setVisibility(View.GONE);
                } else {
                    MyToast.getInstance().showToast(mContext, "已无数据加载");
                    ptrl_shouhou_dingdan.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        super.onSuccess(requestTag, response, showLoad);
        emptylayout_shouhou.hide();
        if (row != 0)
            showNothingView(false);
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        emptylayout_shouhou.showError();
        showNothingView(false);
    }

    /**
     * @param type 初始化界面为普通请求 —— 0
     *             刷新界面——1
     *             加载更多——2
     */
    private void qingQiuShuJu(int type) {
        switch (type) {
            case 0:
                requestHandleArrayList.add(requestAction.shop_oInfo_afterInfo(this, strSQWhat, page + "", ptrl_shouhou_dingdan, 0));
                break;
            case 1:
                requestHandleArrayList.add(requestAction.shop_oInfo_afterInfo(this, strSQWhat, page + "", ptrl_shouhou_dingdan, 1));
                break;
            case 2:
                requestHandleArrayList.add(requestAction.shop_oInfo_afterInfo(this, strSQWhat, page + "", ptrl_shouhou_dingdan, 2));
                break;
        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
            isRefresh = true;
            page = 0;
            qingQiuShuJu(1);
        } else {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            DialogUtlis.oneBtnNormal(getmDialog(), "请登录后查看!");
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
            //确保有数据才设置上拉加载更多监听
            if (page > 0) {
                isRefresh = false;
                qingQiuShuJu(2);
            } else {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                MyToast.getInstance().showToast(mContext, "无更多数据！");
            }
        } else {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            DialogUtlis.oneBtnNormal(getmDialog(), "请登录后查看!");
        }
    }
}
