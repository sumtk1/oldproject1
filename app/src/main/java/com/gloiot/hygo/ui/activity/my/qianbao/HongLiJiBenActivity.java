package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.XiangQingActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 红利积分记录/基本积分记录
 */
public class HongLiJiBenActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.lv_honglijiben)
    ListView lvHonglijiben;
    @Bind(R.id.ptrl_honglijiben)
    PullToRefreshLayout ptrlHonglijiben;
    // @Bind(R.id.ll_nodata)
    // LinearLayout mLlNoData;
    @Bind(R.id.tv_honglizhanghu_jifen)
    TextView tvZhanghuJifen;
    @Bind(R.id.tv_honglizhanghu_shuoming)
    TextView tvShuoming;
    @Bind(R.id.tv_honglizhanghu_biaoti)
    TextView tvHonglizhanghuBiaoti;

    private List<String[]> list = new ArrayList<String[]>();
    private int page = 0;
    private String title = "", type = "";
    private CommonAdapter<String[]> commonAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_my_honglijiben;
    }

    @Override
    public void initData() {
        title = getIntent().getStringExtra("type");
        if ("红利积分".equals(title)) {
            type = "红利";
            CommonUtlis.setTitleBar(this, "市场奖励账户");
            tvHonglizhanghuBiaoti.setText("市场奖励账户");

//            tvShuoming.setText("注:该账户可直接用于提现与转让，并可在生活模块进行消费使用，也可用来充值积分账户；");
//            final SpannableStringBuilder sp1 = new SpannableStringBuilder(tvShuoming.getText().toString());
//            sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_2b9ced)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tvShuoming.setText(sp1);

        } else if ("基本积分".equals(title)) {
            type = "积分";
            CommonUtlis.setTitleBar(this, "积分赠送账户");
            tvHonglizhanghuBiaoti.setText("积分赠送账户 ");
//            tvShuoming.setText("注:充值的积分会存储在该账户，该账户可在购物模块与生活模块进行消费使用，并可进行转让；");
//            final SpannableStringBuilder sp1 = new SpannableStringBuilder(tvShuoming.getText().toString());
//            sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_2b9ced)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tvShuoming.setText(sp1);
        } else if ("商城积分".equals(title)) {
            type = "商城";
            CommonUtlis.setTitleBar(this, "商城账户");
            tvHonglizhanghuBiaoti.setText("商城账户");
//            tvShuoming.setText("注:用户购买商城消费套餐后，套餐内赠送的积分可提取至该账户，用于环游购商城购物消费；");
//            final SpannableStringBuilder sp1 = new SpannableStringBuilder(tvShuoming.getText().toString());
//            sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_2b9ced)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tvShuoming.setText(sp1);
        } else if ("省钱账户".equals(title)) {
            type = "省钱";
            CommonUtlis.setTitleBar(this, title);
            tvHonglizhanghuBiaoti.setText(title);
//            tvShuoming.setText("注:旅游套餐赠送，赠送积分只能用于环游购自营商城消费；");
//            final SpannableStringBuilder sp1 = new SpannableStringBuilder(tvShuoming.getText().toString());
//            sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_2b9ced)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tvShuoming.setText(sp1);
        }

        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_qianbao, list) {
            @Override
            public void convert(ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_shuoming, strings[0]);
                holder.setText(R.id.tv_time, strings[1]);

                if (strings[2].contains("-")) {
                    holder.setText(R.id.tv_jifen, strings[2]);
                    holder.setTextColor(R.id.tv_jifen, Color.parseColor("#FF6D63"));
                } else {
                    holder.setText(R.id.tv_jifen, "+" + strings[2]);
                    holder.setTextColor(R.id.tv_jifen, Color.parseColor("#965aff"));
                }
            }
        };

        //跳详情页
        lvHonglijiben.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HongLiJiBenActivity.this, XiangQingActivity.class);
                intent.putExtra("id", list.get(position)[3]);
                intent.putExtra("type", list.get(position)[4]);
                startActivity(intent);
            }
        });
        lvHonglijiben.setAdapter(commonAdapter);
        ptrlHonglijiben.setOnRefreshListener(this);
        getData(100, 0, 0, 0, type);
    }

    /**
     * @param requestTAG  请求标识符，自定义，requestSuccess方法用来判断是哪个请求
     * @param isDialog    弹出框标识符，-1表示不弹出不收起，0表示正常弹出收起，1表示之弹出，2表示之收起
     * @param requestType 请求类别，列表型数据请求特有，0表示普通请求，1表示刷新请求，2表示加载请求
     * @param page        请求页数，列表型数据请求特有，表示当前请求的是那一页，0为第一页
     * @param type        获取记录数据的类别，传空时代表获取所有数据
     */
    private void getData(int requestTAG, int isDialog, int requestType, int page, String type) {
        requestHandleArrayList.add(requestAction.getHongLiJiBen(requestTAG, isDialog, HongLiJiBenActivity.this, ptrlHonglijiben, requestType, page, type));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess", response.toString());
        switch (requestTag) {
            case 100:
                showInfo(response, false);
                if ("红利".equals(type))
                    tvZhanghuJifen.setText(response.getString("红利账户"));
                else if ("积分".equals(type))
                    tvZhanghuJifen.setText(response.getString("积分账户"));
                else if ("商城".equals(type))
                    tvZhanghuJifen.setText(response.getString("商城账户"));
                else if ("省钱".equals(type))
                    tvZhanghuJifen.setText(response.getString("积分账户"));

            case 200:  //下拉刷新
                list.clear();
                showInfo(response, false);
                break;
            case 300:  //上拉加载
                showInfo(response, true);
                break;
        }
    }

    /**
     * 对请求的数据进行解析存储
     *
     * @param response   返回的数据集
     * @param isLoadMore 判断解析完成的操作，true表示展示数据，false表示收起加载栏、更新数据
     */
    public void showInfo(JSONObject response, boolean isLoadMore) {
        final SpannableStringBuilder sp1;
        try {
            sp1 = new SpannableStringBuilder(response.getString("备注"));
            sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_2b9ced)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvShuoming.setText(sp1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int count = 0;
        try {
            if (isLoadMore) {
                ptrlHonglijiben.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }

            count = response.getInt("条数");
            if (count != 0) {

                JSONArray jsonArray = response.getJSONArray("列表");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    Log.e("jsonObj", jsonObj.toString());
                    String[] details = new String[jsonObj.length()];
                    details[0] = jsonObj.getString("说明");
                    details[1] = jsonObj.getString("录入时间");
                    details[2] = jsonObj.getString("积分");
//                    details[3] = jsonObj.getString("余额");
                    details[3] = jsonObj.getString("id");
                    details[4] = jsonObj.getString("交易类别");
                    list.add(details);
                }
                page = count == 10 ? Integer.parseInt(response.getString("页数")) : 0;
                lvHonglijiben.setVisibility(View.VISIBLE);
                //   mLlNoData.setVisibility(View.GONE);
                Log.e("showInfo", page + "");
                CommonUtlis.setListViewHeightBasedOnChildren(lvHonglijiben);
                commonAdapter.notifyDataSetChanged();
            } else {
                if (isLoadMore) {
                    MyToast.getInstance().showToast(mContext, "已无数据加载");
                    ptrlHonglijiben.loadmoreFinish(PullToRefreshLayout.FAIL);
                } else {
                    lvHonglijiben.setVisibility(View.GONE);
                    //  mLlNoData.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogUtlis.oneBtnNormal(getmDialog(), "数据异常-1\n请稍后再试");
        }
    }

    /**
     * 列表刷新
     */
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getData(200, -1, 1, 0, type);
    }

    /**
     * 列表加载
     */
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (page > 0) {
            getData(300, -1, 2, page, type);
        } else {
            MyToast.getInstance().showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }
}
