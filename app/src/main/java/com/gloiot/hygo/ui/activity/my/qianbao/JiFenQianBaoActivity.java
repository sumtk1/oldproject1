package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.gloiot.hygo.R;
import com.gloiot.hygo.bean.MyBillBean;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.XiangQingActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.StatusBarUtil;
import com.zyd.wlwsdk.widge.MyScrollView;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class JiFenQianBaoActivity extends BaseActivity implements View.OnClickListener, MyScrollView.OnScrollListener,
        PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_jifenqianbao_hongli)  //红利账户
    TextView tv_jifenqianbao_hongli;
    @Bind(R.id.tv_jifenqianbao_jifen)  //积分账户
    TextView tv_jifenqianbao_jifen;
    @Bind(R.id.tv_jifenqianbao_ziying_zhanghu)      //其他账户
    TextView tv_jifenqianbao_ziying_zhanghu;
    @Bind(R.id.tv_jifenqianbao_youfei_zhanghu)      //油费账户
    TextView tv_jifenqianbao_youfei_zhanghu;
    @Bind(R.id.tv_jifenbaozhanghu)      //积分宝账户
    TextView tv_jifenbaozhanghu;
    @Bind(R.id.tv_jifenqianbao_ziying_name)      //其他账户名称
    TextView tv_jifenqianbao_ziying_name;
    @Bind(R.id.view_status_bar)
    View viewStatusBar;

    @Bind(R.id.msv_jifen_qianbao)
    MyScrollView msv_jifen_qianbao;
    @Bind(R.id.test1)
    RelativeLayout test1;
    @Bind(R.id.test2)
    RelativeLayout test2;
    @Bind(R.id.view_status_bar1)
    View view_status_bar1;
    @Bind(R.id.rl_00)
    RelativeLayout rl_00;
    @Bind(R.id.lv_jifen_qianbao_zhangdan)
    ListView lv_jifen_qianbao_zhangdan;
    @Bind(R.id.ptrl_jifen_qianbao)
    PullToRefreshLayout ptrl_jifen_qianbao;

    /**
     * 我的账单适配器
     */
    private CommonAdapter mCommonAdapter;
    /**
     * 记录账户类别的数组--用于筛选
     */
    private ArrayList<String> screenData = new ArrayList<>();
    /**
     * 我的订单实体数组
     */
    private ArrayList<MyBillBean> mDataList = new ArrayList<MyBillBean>();
    /**
     * 筛选的类型
     */
    private String type = "";
    /**
     * 页数，默认为1
     */
    private int page = 0;

    private boolean isFirst = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_my_jifen_qianbao;
    }

    @Override
    public void initData() {
        StatusBarUtil.transparencyBar(this);
        if (Build.VERSION.SDK_INT >= 21) {
            viewStatusBar.setVisibility(View.VISIBLE);
        }

        lv_jifen_qianbao_zhangdan.setFocusable(false);
        mCommonAdapter = new CommonAdapter<MyBillBean>(mContext, R.layout.item_qianbao_new, mDataList) {
            @Override
            public void convert(ViewHolder holder, MyBillBean bean) {
                /** 说明*/
                TextView tv_explain = holder.getView(R.id.tv_explain);
                /** 备注*/
                TextView tv_remark = holder.getView(R.id.tv_remark);
                /** 时间*/
                TextView tv_time = holder.getView(R.id.tv_time);
                /** 交易价格（积分）*/
                TextView tv_money = holder.getView(R.id.tv_money);
                /** 账户类型*/
                TextView tv_type = holder.getView(R.id.tv_type);

                if (bean.getRemarks().equals("")) {
                    tv_remark.setVisibility(View.GONE);
                } else {
                    tv_remark.setMaxLines(12);
                    tv_remark.setVisibility(View.VISIBLE);
                    tv_remark.setText("备注：" + bean.getRemarks());
                }
                if (bean.getIntegral().contains("-")) {
                    holder.setTextColor(R.id.tv_money, Color.parseColor("#FF6D63"));
                } else {
                    holder.setTextColor(R.id.tv_money, getResources().getColor(R.color.main_color));
                }
                tv_explain.setText(bean.getExplain());
                tv_time.setText(bean.getEntryTime());
                tv_money.setText(bean.getIntegral());
                tv_type.setText(bean.getTransactionAccount());
            }
        };
        lv_jifen_qianbao_zhangdan.setAdapter(mCommonAdapter);

        lv_jifen_qianbao_zhangdan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("----", "--- " + mDataList.get(i).getTransactionAccount());
                if (mDataList.get(i) == null) {
                    MyToast.getInstance().showToast(JiFenQianBaoActivity.this, "数据获取有误，请重新获取");
                    return;
                } else if (mDataList.get(i).getTransactionAccount().contains("省钱账户")) {
                    return;
                }
                Intent it = new Intent(JiFenQianBaoActivity.this, XiangQingActivity.class);
                it.putExtra("id", mDataList.get(i).getId());
                it.putExtra("type", mDataList.get(i).getTransactionType());
                startActivity(it);
            }
        });

        ptrl_jifen_qianbao.setOnRefreshListener(this);
        msv_jifen_qianbao.setOnScrollListener(this);
        msv_jifen_qianbao.smoothScrollTo(0, 20);
        requestHandleArrayList.add(requestAction.getMoneyThree(JiFenQianBaoActivity.this, 0));

        //账单记录那些
        requestHandleArrayList.add(requestAction.p_sel_details_three(this, type, page + ""));
        requestHandleArrayList.add(requestAction.getLeiBieList(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst)
            requestHandleArrayList.add(requestAction.getMoneyThree(JiFenQianBaoActivity.this, 0));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_GETMONEYTHREE:
                isFirst = true;
                tv_jifenqianbao_hongli.setText(response.getString("红利账户"));
                tv_jifenqianbao_jifen.setText(response.getString("积分账户"));
                tv_jifenbaozhanghu.setText(response.getJSONObject("积分宝账户").getString("积分宝账户"));
//                tv_jshangjinbi_zhanghu.setText(response.getString("商金币账户"));
                tv_jifenqianbao_youfei_zhanghu.setText(response.getString("油费账户"));
//                tv_jifenqianbao_shangcheng_zhanghu.setText(response.getString("商城账户"));
                tv_jifenqianbao_ziying_name.setText(response.getString("其他账户"));
                tv_jifenqianbao_ziying_zhanghu.setText(response.getString("其他账户余额"));
                break;

            case RequestAction.TAG_MY_BILL:
                Log.e("JSON数据：", response.toString());
                if (response.getInt("页数") == 1) {
                    mDataList.clear();
                    //恢复界面
                    msv_jifen_qianbao.smoothScrollTo(0, 20);
                    test2.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= 21) {
                        view_status_bar1.setVisibility(View.GONE);
                    }
                }
                JSONArray arr = response.getJSONArray("积分列表");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    MyBillBean bean = new MyBillBean();
                    bean.setId(obj.getString("id"));
                    bean.setExplain(obj.getString("说明"));
                    bean.setIntegral(obj.getString("积分"));
                    bean.setEntryTime(obj.getString("录入时间"));
                    bean.setBalance(obj.getString("余额"));
                    bean.setRemarks(obj.getString("备注"));
                    bean.setTransactionAccount(obj.getString("交易账户"));
                    bean.setTransactionType(obj.getString("交易类别"));
                    mDataList.add(bean);
                }
                if (response.getInt("条数") == 0) {
                    if (page > 0) {
                        MyToast.getInstance().showToast(mContext, "已无更多数据加载");
                    }
                }
                mCommonAdapter.notifyDataSetChanged();
                CommonUtlis.setListViewHeightBasedOnChildren(lv_jifen_qianbao_zhangdan);
                break;
            case RequestAction.TAG_GETLEIBIELIST:
                Log.e("选择类型的数据：", response.toString());
                JSONArray arr1 = response.getJSONArray("数据");
                screenData.clear();
                for (int j = 0; j < arr1.length(); j++) {
                    screenData.add(arr1.getString(j));
                }
                break;

        }
    }

    @OnClick({R.id.iv_toptitle_back, R.id.rl_jifenqianbao_zhuanrang, R.id.rl_jifenqianbao_tiqu,
            R.id.rl_jifenqianbao_hongli, R.id.rl_jifenqianbao_jifen,
            R.id.rl_jifenqianbao_ziying_zhanghu, R.id.rl_jifenqianbao_youfei_zhanghu,
            R.id.rl_jifenbao_zhanghu, R.id.tv_jifen_qianbao_shaixuan_1, R.id.tv_jifen_qianbao_shaixuan_2
    })
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toptitle_back:  //头部返回键
                finish();
                break;
            case R.id.rl_jifenqianbao_zhuanrang:    //积分转让
                startActivity(new Intent(JiFenQianBaoActivity.this, ZhuanrangJifenActivity.class));
                break;
            case R.id.rl_jifenqianbao_tiqu:     //红利提取
//                startActivity(new Intent(JiFenQianBaoActivity.this, HongliTiXianActivity.class));
                startActivity(new Intent(JiFenQianBaoActivity.this, XuanZeTiXianActivity.class));
                break;
            case R.id.rl_jifenqianbao_hongli:       //红利账户
                startActivity(new Intent(mContext, HongLiJiBenActivity.class).putExtra("type", "红利积分"));
                break;
            case R.id.rl_jifenqianbao_jifen:       //积分账户
                startActivity(new Intent(mContext, HongLiJiBenActivity.class).putExtra("type", "基本积分"));
                break;
            case R.id.rl_jifenqianbao_ziying_zhanghu:       //其他账户
                startActivity(new Intent(mContext, HongLiJiBenActivity.class)
                        .putExtra("type", tv_jifenqianbao_ziying_name.getText().toString()));
                break;
            case R.id.rl_jifenqianbao_youfei_zhanghu:       //油费账户
                startActivity(new Intent(mContext, YoufeizhanghuActivity.class));
                break;
            case R.id.rl_jifenbao_zhanghu:      //积分宝账户
                startActivity(new Intent(mContext, JifenbaoZhanghuActivity.class));
                break;
            //刷新
            case R.id.tv_jifen_qianbao_shaixuan_1:
                showDialog();
                break;
            case R.id.tv_jifen_qianbao_shaixuan_2:
                showDialog();
                break;
        }
    }


    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= test1.getTop() - view_status_bar1.getBottom()) {
            test2.setVisibility(View.VISIBLE);
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
            view_status_bar1.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            test2.setVisibility(View.GONE);
            StatusBarUtil.transparencyBar(this);
            view_status_bar1.setBackgroundColor(Color.parseColor("#00ffffff"));
        }

        if (scrollY > rl_00.getTop()) {
            if (Build.VERSION.SDK_INT >= 21) {
                view_status_bar1.setVisibility(View.VISIBLE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                view_status_bar1.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示弹框
     */
    private void showDialog() {
        if (screenData.size() <= 0) {
            requestHandleArrayList.add(requestAction.getLeiBieList(this));
            return;
        }
        DialogUtlis.customListView(getmDialog(), "选择记录类型", screenData, new MDialogInterface.ListViewOnClickInter() {
            @Override
            public void onItemClick(String data, int position) {
                if (data.equals(type) || (data.equals("全部") && type.equals(""))) {
                    //若点击的筛选与上一次相同，则不加载数据,因为要保持当前加载的页码（有可能已经加载了很多页）
                    return;
                } else {
                    //若点击的筛选与上一次不同，则先把页码=0再加载数据（与上一次筛选不同，不需要考虑上一个筛选数据加载多少页的问题）
                    page = 0;
                }

                if (data.equals("全部")) {
                    type = "";
                } else {
                    type = data;
                }
                requestHandleArrayList.add(requestAction.p_sel_details_three(JiFenQianBaoActivity.this, type, page + ""));
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 0;
                requestHandleArrayList.add(requestAction.p_sel_details_three(JiFenQianBaoActivity.this, type, page + ""));
                ptrl_jifen_qianbao.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                requestHandleArrayList.add(requestAction.p_sel_details_three(JiFenQianBaoActivity.this, type, page + ""));
                ptrl_jifen_qianbao.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 1000);
    }
}
