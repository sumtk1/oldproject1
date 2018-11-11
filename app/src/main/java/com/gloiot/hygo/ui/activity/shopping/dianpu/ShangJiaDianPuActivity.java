package com.gloiot.hygo.ui.activity.shopping.dianpu;

import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.YouHuiQuanBean;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.ui.activity.social.ConversationActivity;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.StatusBarUtil;
import com.zyd.wlwsdk.widge.EmptyLayout;
import com.zyd.wlwsdk.widge.SpaceItemDecorationGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hygo03 on 2017/8/16.
 */

public class ShangJiaDianPuActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_toptitle_back)
    ImageView ivToptitleBack;
    @Bind(R.id.rl_sousuo)
    RelativeLayout rlSousuo;
    @Bind(R.id.iv_toptitle_task)
    ImageView ivToptitleTask;
    @Bind(R.id.iv_dianpu_tubiao)
    ImageView ivDianpuTubiao;
    @Bind(R.id.tv_dianpu_ming)
    TextView tvDianpuMing;
    @Bind(R.id.tv_dianpu_guanzhu)
    TextView tvDianpuGuanzhu;
    @Bind(R.id.rl_dianpu_title)
    RelativeLayout rlDianpuTitle;
    @Bind(R.id.tv_dianpu_xiaoliangNum)
    TextView tvDianpuXiaoliangNum;
    @Bind(R.id.view_status_bar)
    View viewStatusBar;
    @Bind(R.id.iv_dianpu_guanzhu)
    ImageView ivDianpuGuanzhu;
    @Bind(R.id.rv_dianpu)
    RecyclerView rvDianpu;
    @Bind(R.id.emptylayout_dianpu)
    EmptyLayout emptylayoutDianpu;
    @Bind(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tv_dianpu_zonghe)
    TextView tvDianpuZonghe;
    @Bind(R.id.ll_dianpu_zonghe)
    RelativeLayout llDianpuZonghe;
    @Bind(R.id.tv_dianpu_xiaoliang)
    TextView tvDianpuXiaoliang;
    @Bind(R.id.ll_dianpu_xiaoliang)
    RelativeLayout llDianpuXiaoliang;
    @Bind(R.id.tv_dianpu_jiage)
    TextView tvDianpuJiage;
    @Bind(R.id.iv_dianpu_jiage)
    ImageView ivDianpuJiage;
    @Bind(R.id.ll_dianpu_jiage)
    RelativeLayout llDianpuJiage;
    @Bind(R.id.iv_dianpu_jiemian)
    ImageView ivDianpuJiemian;
    @Bind(R.id.ll_dianpu_qiehuanjiemian)
    LinearLayout llDianpuQiehuanjiemian;
    @Bind(R.id.ll_dianpu_menu)
    LinearLayout llDianpuMenu;
    @Bind(R.id.view_zonghe)
    View viewZonghe;
    @Bind(R.id.view_xiaoliang)
    View viewXiaoliang;
    @Bind(R.id.view_jiage)
    View viewJiage;
    @Bind(R.id.iv_dianpu_title)
    ImageView ivDianpuTitle;
    @Bind(R.id.iv_dianpu_dengji)
    ImageView iv_dianpu_dengji;
    @Bind(R.id.et_sousuo)
    EditText et_sousuo;

    private String shangPing_id, dianPu_id, guanZhu_type, shangPing_dianpu_id = "", youHuiQuanId = "";
    private String shangjia_zhanghao = "";

    private List<FenLeiSonBean> shangPingInfoList = new ArrayList<>(10);
    private List<YouHuiQuanBean> youHuiQuanInfoList = new ArrayList<>();

    private boolean flag = true;
    private DianPuRecyclerViewAdapter myRecyclerViewAdapter;
    private GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 1);
    private GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2);
    private SpaceItemDecorationGridView spaceItemDecorationGridView1 = new SpaceItemDecorationGridView(2, 1);
    private SpaceItemDecorationGridView spaceItemDecorationGridView2 = new SpaceItemDecorationGridView(6, 2);

    /**
     * shaiXuanLeixXing可以为
     * "综合排序"、"新品优先"
     * "销量"
     * "价格低"、"价格高"
     */
    private String shaiXuanLeixXing = "综合排序";
    // 0为未选择价格排序；1为从高到底；-1为从低到高
    private int jiage_zhuangtai = 0;

    //刷新加载
    private int page = 0, num = 0;
    boolean isLoading;
    private int guanzhuNum = 0;
    //搜索标识
    private boolean sousuo_flag = false;

    @Override
    public int initResource() {
        return R.layout.activity_shangjiadianpu;
    }

    @Override
    public void initData() {
        StatusBarUtil.transparencyBar(ShangJiaDianPuActivity.this);
        if (Build.VERSION.SDK_INT >= 21) {
            viewStatusBar.setVisibility(View.VISIBLE);
        }
        initComponent();

        shangPing_id = getIntent().getStringExtra("id");
        shangPing_dianpu_id = getIntent().getStringExtra("店铺id");
        if (getIntent().getStringExtra("name") != null)
            tvDianpuMing.setText(getIntent().getStringExtra("name"));
        if (getIntent().getStringExtra("guangzhuNum") != null)
            tvDianpuGuanzhu.setText("关注 " + getIntent().getStringExtra("guangzhuNum"));
        if (getIntent().getStringExtra("imgUrl") != null)
            PictureUtlis.loadRoundImageViewHolder(mContext, getIntent().getStringExtra("imgUrl"), R.drawable.default_image, ivDianpuTubiao, 10);


        if (shangPing_id == null)
            shangPing_id = "";
        if (shangPing_dianpu_id == null)
            shangPing_dianpu_id = "";

        requestHandleArrayList.add(requestAction.shop_hp_storeInfo(ShangJiaDianPuActivity.this, shangPing_id, shangPing_dianpu_id));

        requestHandleArrayList.add(requestAction.shop_sh_honnr(ShangJiaDianPuActivity.this, shangPing_id, shangPing_dianpu_id));

        //获取优惠券列表
        requestHandleArrayList.add(requestAction.shop_Create_coupon(ShangJiaDianPuActivity.this, shangPing_dianpu_id, shangPing_id));

        qingQiuShuJu("综合排序", false);

        et_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (!"".equals(et_sousuo.getText().toString().trim())) {
                        sousuo_flag = true;
                        emptylayoutDianpu.hide();
                        shangPingInfoList.clear();
                        requestHandleArrayList.add(requestAction.shop_hp_store(ShangJiaDianPuActivity.this, shangPing_id, "", 0, shangPing_dianpu_id, et_sousuo.getText().toString().trim()));
                    } else {
                        sousuo_flag = false;
                        MyToast.getInstance().showToast(mContext, "请输入搜索内容");
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public void initComponent() {
        rvDianpu.setLayoutManager(gridLayoutManager2);
        rvDianpu.addItemDecoration(spaceItemDecorationGridView2);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rvDianpu.setHasFixedSize(true);
        View header = LayoutInflater.from(this).inflate(R.layout.item_dianpu_yhq, null);
        myRecyclerViewAdapter = new DianPuRecyclerViewAdapter(this, shangPingInfoList, youHuiQuanInfoList);
        myRecyclerViewAdapter.setHeaderView(header);


        rvDianpu.setAdapter(myRecyclerViewAdapter);
        myRecyclerViewAdapter.setItemClickListener(new DianPuRecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int pos;
                if (youHuiQuanInfoList.size() > 0) {
                    pos = position - 1;
                } else {
                    pos = position;
                }
                Intent intent = new Intent(ShangJiaDianPuActivity.this, ShangPinXiangQingActivity.class);
                intent.putExtra("id", shangPingInfoList.get(pos).getFenLeiSon_id());
                intent.putExtra("info", (Serializable) shangPingInfoList.get(pos));
                startActivity(intent);
            }
        });
        processData();

        //错误页面
        emptylayoutDianpu.setErrorDrawable(R.mipmap.img_error_layout);
        emptylayoutDianpu.setErrorMessage("网络出错了");
        emptylayoutDianpu.setErrorViewButtonId(R.id.buttonError);
        emptylayoutDianpu.setShowErrorButton(true);
        emptylayoutDianpu.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qingQiuShuJu(shaiXuanLeixXing, false);
            }
        });
        //空白页面
        emptylayoutDianpu.setEmptyDrawable(R.mipmap.img_empty_layout);
        emptylayoutDianpu.setEmptyMessage("暂无此商品");
        emptylayoutDianpu.setShowEmptyButton(false);

        myRecyclerViewAdapter.setGetYouHuiQuan(new DianPuRecyclerViewAdapter.GetYouHuiQuan() {
            @Override
            public void getyouhuiquan(String id) {
                youHuiQuanId = id;
                requestHandleArrayList.add(requestAction.shop_wl_Get_coupons(ShangJiaDianPuActivity.this, id));
            }
        });
    }


    private void qingQiuShuJu(String str, boolean isLoading) {
//        String sousuo = et_sousuo.getText().toString().trim();
        if (isLoading) {
            //加载更多数据
            if (shaiXuanLeixXing.equals(str)) {
                return;
            } else {
                emptylayoutDianpu.hide();
                page = 0;
                num = 0;
                shangPingInfoList.clear();
                myRecyclerViewAdapter.notifyDataSetChanged();
                rvDianpu.scrollToPosition(0);
                //请求数据
                requestHandleArrayList.add(requestAction.shop_hp_store(ShangJiaDianPuActivity.this, shangPing_id, str, page, shangPing_dianpu_id, ""));
            }
        } else {
            emptylayoutDianpu.hide();
            requestHandleArrayList.add(requestAction.shop_hp_store(ShangJiaDianPuActivity.this, shangPing_id, str, page, shangPing_dianpu_id, ""));
        }
        shaiXuanLeixXing = str;
    }

    /**
     * 刷新加载
     */
    private void processData() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                shangPingInfoList.clear();
                myRecyclerViewAdapter.notifyDataSetChanged();
                if (!"".equals(et_sousuo.getText().toString().trim())) {
                    requestHandleArrayList.add(requestAction.shop_hp_store(ShangJiaDianPuActivity.this, shangPing_id, "", 0, shangPing_dianpu_id, et_sousuo.getText().toString().trim()));
                } else {
                    qingQiuShuJu(shaiXuanLeixXing, false);
                }
            }
        });

        rvDianpu.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滚动事件结束并且到达最底端
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition + 1 == myRecyclerViewAdapter.getItemCount()) {
                    if (!isLoading) {
                        isLoading = true;
                        if (num > 0) {
                            if (!"".equals(et_sousuo.getText().toString().trim())) {
                                requestHandleArrayList.add(requestAction.shop_hp_store(ShangJiaDianPuActivity.this, shangPing_id, "", page, shangPing_dianpu_id, et_sousuo.getText().toString().trim()));
                            } else {
                                qingQiuShuJu(shaiXuanLeixXing, false);
                            }
                        }
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (flag) {
                    lastVisibleItemPosition = gridLayoutManager2.findLastVisibleItemPosition();
                } else {
                    lastVisibleItemPosition = gridLayoutManager1.findLastVisibleItemPosition();
                }
            }
        });
    }

    /**
     * 初始化界面
     */
    private void resetView() {
        viewZonghe.setVisibility(View.GONE);
        viewJiage.setVisibility(View.GONE);
        viewXiaoliang.setVisibility(View.GONE);
        tvDianpuZonghe.setTextColor(getResources().getColor(R.color.cl_333333));
        tvDianpuXiaoliang.setTextColor(getResources().getColor(R.color.cl_333333));
        tvDianpuJiage.setTextColor(getResources().getColor(R.color.cl_333333));
        ivDianpuJiage.setImageResource(R.mipmap.ic_fenlei_jiage0);
    }

    public void changeView(int i) {
        resetView();
        switch (i) {
            case 1:
                tvDianpuZonghe.setTextColor(getResources().getColor(R.color.main_color));
                viewZonghe.setVisibility(View.VISIBLE);
                jiage_zhuangtai = 0;
                qingQiuShuJu("综合排序", true);
                break;
            case 2:
                tvDianpuXiaoliang.setTextColor(getResources().getColor(R.color.main_color));
                viewXiaoliang.setVisibility(View.VISIBLE);
                jiage_zhuangtai = 0;
                qingQiuShuJu("销量", true);
                break;
            case 3:
                tvDianpuJiage.setTextColor(getResources().getColor(R.color.main_color));
                viewJiage.setVisibility(View.VISIBLE);
                if (jiage_zhuangtai == 0) {
                    jiage_zhuangtai = -1;
                    ivDianpuJiage.setImageResource(R.mipmap.icon_shang);
                    qingQiuShuJu("价格低", true);
                } else if (jiage_zhuangtai == 1) {
                    jiage_zhuangtai = -1;
                    ivDianpuJiage.setImageResource(R.mipmap.icon_shang);
                    qingQiuShuJu("价格低", true);
                } else if (jiage_zhuangtai == -1) {
                    jiage_zhuangtai = 1;
                    ivDianpuJiage.setImageResource(R.mipmap.icon_xia);
                    qingQiuShuJu("价格高", true);
                }
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.iv_toptitle_back, R.id.rl_sousuo, R.id.iv_toptitle_task, R.id.iv_dianpu_guanzhu, R.id.ll_dianpu_zonghe, R.id.ll_dianpu_xiaoliang, R.id.ll_dianpu_jiage, R.id.ll_dianpu_qiehuanjiemian, R.id.rl_dianpu_liangxishangjia})
    @Override
    public void
    onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toptitle_back:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.iv_toptitle_task:
                Intent intent = new Intent(ShangJiaDianPuActivity.this, MainActivity.class);
                intent.putExtra("type", "聊天");
                startActivity(intent);
                finish();
                break;
            case R.id.iv_dianpu_guanzhu:
                if (!"".equals(guanZhu_type)) {
                    if ("是".equals(guanZhu_type)) {
                        requestHandleArrayList.add(requestAction.shop_hp_follow(ShangJiaDianPuActivity.this, dianPu_id, "", "是", ""));
                    } else {
                        requestHandleArrayList.add(requestAction.shop_hp_follow(ShangJiaDianPuActivity.this, dianPu_id, "是", "", ""));
                    }
                }
                break;
            //中部切换
            case R.id.ll_dianpu_zonghe:
                et_sousuo.setText("");
                changeView(1);
                break;
            case R.id.ll_dianpu_xiaoliang:
                et_sousuo.setText("");
                changeView(2);
                break;
            case R.id.ll_dianpu_jiage:
                et_sousuo.setText("");
                changeView(3);
                break;
            case R.id.ll_dianpu_qiehuanjiemian:
                int canVisPosition;
                if (flag) {
                    canVisPosition = gridLayoutManager2.findFirstVisibleItemPosition();
                    rvDianpu.setLayoutManager(gridLayoutManager1);
                    rvDianpu.removeItemDecoration(spaceItemDecorationGridView1);
                    rvDianpu.removeItemDecoration(spaceItemDecorationGridView2);
                    rvDianpu.addItemDecoration(spaceItemDecorationGridView1);
                    ivDianpuJiemian.setImageResource(R.mipmap.ic_fenlei_jiemian_1);
                    myRecyclerViewAdapter.changeItemView(flag);
                    flag = false;
                } else {
                    canVisPosition = gridLayoutManager1.findFirstVisibleItemPosition();
                    rvDianpu.setLayoutManager(gridLayoutManager2);
                    rvDianpu.removeItemDecoration(spaceItemDecorationGridView1);
                    rvDianpu.removeItemDecoration(spaceItemDecorationGridView2);
                    rvDianpu.addItemDecoration(spaceItemDecorationGridView2);
                    ivDianpuJiemian.setImageResource(R.mipmap.ic_fenlei_jiemian_2);
                    myRecyclerViewAdapter.changeItemView(flag);
                    flag = true;
                }
                rvDianpu.scrollToPosition(canVisPosition);
                break;

            case R.id.rl_dianpu_liangxishangjia:
                if (check_login_tiaozhuang())
                    startChat();
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            //店铺信息
            case RequestAction.TAG_HP_STOREINFO:
                tvDianpuMing.setText(JSONUtlis.getString(response, "店铺名称"));
                guanzhuNum = Integer.parseInt(JSONUtlis.getString(response, "关注数量"));
                tvDianpuGuanzhu.setText("关注 " + guanzhuNum);
                tvDianpuXiaoliangNum.setText("销量 " + JSONUtlis.getString(response, "销售量"));
                PictureUtlis.loadRoundImageViewHolder(mContext, JSONUtlis.getString(response, "店铺图标"), R.drawable.default_image, ivDianpuTubiao, 10);
                PictureUtlis.loadImageViewHolder(mContext, JSONUtlis.getString(response, "店招图片"), R.drawable.default_image, ivDianpuTitle);
                dianPu_id = JSONUtlis.getString(response, "店铺id");
                shangjia_zhanghao = JSONUtlis.getString(response, "商家账号");

                guanZhu_type = JSONUtlis.getString(response, "是否关注");
                if ("否".equals(JSONUtlis.getString(response, "是否关注"))) {
                    ivDianpuGuanzhu.setBackgroundResource(R.mipmap.icon_dianpu_guanzhu);
                } else {
                    ivDianpuGuanzhu.setBackgroundResource(R.mipmap.icon_dianpu_yiguanzhu);
                }
                break;
            //商品
            case RequestAction.TAG_HP_STORE:
                Log.e("商品-=------", response.toString());
                page = response.getInt("页数");
                num = response.getInt("条数");
                JSONArray jsonArray = response.getJSONArray("商品");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    FenLeiSonBean bean = new FenLeiSonBean();
                    bean.setFenLeiSon_id(jsonObj.getString("id"));
                    bean.setFenLeiSon_jiage(jsonObj.getString("价格"));
                    bean.setFenLeiSon_leixin(jsonObj.getString("类型"));
                    bean.setFenLeiSon_xiaoshouliang(jsonObj.getString("销售量"));

                    if (!jsonObj.isNull("缩略图")) {
                        bean.setFenLeiSon_img_url(jsonObj.getString("缩略图"));
                    }
                    bean.setFenLeiSon_title(jsonObj.getString("商品名称"));
                    shangPingInfoList.add(bean);
                }
                myRecyclerViewAdapter.notifyDataSetChanged();

                if (shangPingInfoList.size() == 0 && sousuo_flag) {
                    emptylayoutDianpu.showEmpty();
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    swipeRefreshLayout.setEnabled(true);
                    emptylayoutDianpu.hide();
                }

                sousuo_flag = false;
                break;
            //关注状态
            case RequestAction.TAG_HP_FOLLOW:
                if ("是".equals(guanZhu_type)) {
                    guanzhuNum = guanzhuNum - 1;
                    tvDianpuGuanzhu.setText("关注 " + guanzhuNum);
                    guanZhu_type = "无";
                    ivDianpuGuanzhu.setBackgroundResource(R.mipmap.icon_dianpu_guanzhu);
                } else {
                    guanzhuNum = guanzhuNum + 1;
                    tvDianpuGuanzhu.setText("关注 " + guanzhuNum);
                    guanZhu_type = "是";
                    ivDianpuGuanzhu.setBackgroundResource(R.mipmap.icon_yiguangzhu);
                }
                break;
            //获取商家等级
            case RequestAction.TAG_SH_HONNR:
                switch (JSONUtlis.getString(response, "等级")){
                    case"见习商家":
                        iv_dianpu_dengji.setBackgroundResource(R.mipmap.jianshi);
                        break;
                    case"荣耀商家":
                        iv_dianpu_dengji.setBackgroundResource(R.mipmap.rongyao);
                        break;
                    case"高贵商家":
                        iv_dianpu_dengji.setBackgroundResource(R.mipmap.gaogui);
                        break;
                    case"尊贵商家":
                        iv_dianpu_dengji.setBackgroundResource(R.mipmap.zungui);
                        break;
                    case"至尊商家":
                        iv_dianpu_dengji.setBackgroundResource(R.mipmap.zhizun);
                        break;
                    default:
                        break;
                }
                break;
            //获取优惠券列表
            case RequestAction.TAG_SHOP_CREATE_COUPON:
                Log.e("获取优惠券列表-", response.toString());
                youHuiQuanInfoList.clear();
                JSONArray jsonArr = response.getJSONArray("列表");
                int num = jsonArr.length();
                if (num > 0) {
                    for (int i = 0; i < num; i++) {
                        YouHuiQuanBean youHuiQuanBean = new YouHuiQuanBean();
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        youHuiQuanBean.setName(JSONUtlis.getString(jsonObj, "优惠券名称"));
                        youHuiQuanBean.setPrice(JSONUtlis.getString(jsonObj, "面额"));
                        youHuiQuanBean.setId(JSONUtlis.getString(jsonObj, "id"));
                        youHuiQuanInfoList.add(youHuiQuanBean);
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
                break;
            //领取优惠券
            case RequestAction.TAG_SHOP_WL_GET_COUPONS:
                MyToast.getInstance().showToast(mContext, "已领取");
                if ("成功".equals(response.get("状态").toString())) {
                    for (int i = 0; i < youHuiQuanInfoList.size(); i++) {
                        if (youHuiQuanId.equals(youHuiQuanInfoList.get(i).getId())) {
                            youHuiQuanInfoList.remove(i);
                        }
                    }
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    //发起聊天
    private void startChat() {
        if (!"".equals(shangjia_zhanghao)) {
            Intent intent = new Intent(new Intent(mContext, ConversationActivity.class));
            intent.putExtra("receiveId", shangjia_zhanghao);
            startActivity(intent);
        } else {
            requestHandleArrayList.add(requestAction.shop_hp_storeInfo(ShangJiaDianPuActivity.this, shangPing_id, shangPing_dianpu_id));
            MyToast.getInstance().showToast(mContext, "网络异常，正在加载数据!");
        }
    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        super.onSuccess(requestTag, response, showLoad);
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }


    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        if (page == 0)
            emptylayoutDianpu.showError();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
