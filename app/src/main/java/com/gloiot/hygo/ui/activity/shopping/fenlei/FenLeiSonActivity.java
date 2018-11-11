package com.gloiot.hygo.ui.activity.shopping.fenlei;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.FeiLeiSonErjiMuluBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonYijiMuluBean;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.ui.activity.shopping.gouwuche.GouWuCheActivity;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.widget.MaxHeightListView;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.widge.EmptyLayout;
import com.zyd.wlwsdk.widge.SpaceItemDecorationGridView;
import com.zyd.wlwsdk.widge.refresh.RLoadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品分类点进来的activity
 */
public class FenLeiSonActivity extends BaseActivity implements View.OnClickListener, RLoadListener.RefreshLoadListener {
    @Bind(R.id.iv_toptitle_back)
    ImageView iv_toptitle_back;
    @Bind(R.id.et_sousuo_info)
    EditText et_sousuo_info;
    @Bind(R.id.tv_fenlei_zonghe)
    TextView tv_fenlei_zonghe;
    @Bind(R.id.iv_fenlei_zonghe)
    ImageView iv_fenlei_zonghe;
    @Bind(R.id.ll_fenlei_zonghe)
    LinearLayout ll_fenlei_zonghe;
    @Bind(R.id.tv_fenlei_xiaoliang)
    TextView tv_fenlei_xiaoliang;
    @Bind(R.id.ll_fenlei_xiaoliang)
    LinearLayout ll_fenlei_xiaoliang;
    @Bind(R.id.tv_fenlei_jiage)
    TextView tv_fenlei_jiage;
    @Bind(R.id.iv_fenlei_jiage)
    ImageView iv_fenlei_jiage;
    @Bind(R.id.ll_fenlei_jiage)
    LinearLayout ll_fenlei_jiage;
    @Bind(R.id.ll_fenlei_qiehuanjiemian)
    LinearLayout ll_fenlei_qiehuanjiemian;
    @Bind(R.id.rc_fenleison_shangpin)
    RecyclerView rc_fenleison_shangpin;
    @Bind(R.id.emptylayout_fenleison)
    EmptyLayout emptylayout_fenleison;
    @Bind(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.iv_fenlei_jiemian)
    ImageView iv_fenlei_jiemianl;
    @Bind(R.id.rl_shouye_gouwuche)
    RelativeLayout rlGouWuChe;


    //leixing为  分类/搜索 其中一个值
    private String leixing = "分类";
    //关键字
    private String fenlei_guanjianzi = "";

    //区分全球购与普通商品的类型
    //private String fenlei;

    //RecyclerView
    private List<FenLeiSonBean> listDatas = new ArrayList<>(10);

    private CommonAdapter adapter;
    private CommonAdapter adapter2;
    private boolean flag = true;
    private FenLeiSonAdapter mFenLeiSonAdapter;
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
    //0为综合排序，1为新品优先
    private int zonghe_zhuangtai = 0;
    // 0为未选择价格排序；1为从高到底；-1为从低到高
    private int jiage_zhuangtai = 0;

    //刷新加载
    private int page = 0, num = 0;
    private RLoadListener<FenLeiSonAdapter> mListener;


    //PopupWindow
    private FenleiMuluPop fenleiMuluPop;
    private FenleiMuluPop2 fenleiMuluPop2;
    //分类目录数据
    private List<FenLeiSonYijiMuluBean> listMuluData = new ArrayList<>();
    //一级目录的类别
    private String strYijiMulu = "全部";
    //二级目录的类别
    private String strErjiMulu = "全部";
    private boolean isShowErijiMulu = true;
    //目录菜单的最大高度
    private int maxListViewHight;
    private int firstComeinSeclct = -1;
    private String jsonMulu;
    private InputMethodManager imm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_fenleison;
    }

    @Override
    public void initData() {
        initComponent();

        leixing = getIntent().getStringExtra("类型");
        // fenlei = getIntent().getStringExtra("分类");
        fenlei_guanjianzi = getIntent().getStringExtra("关键字");
        Log.e("FenLeiSonActivity", "leixing:   " + leixing + "---fenlei_guanjianzi:   " + fenlei_guanjianzi);
        strYijiMulu = getIntent().getStringExtra("分类");

        try {
            jsonMulu = preferences.getString(ConstantUtlis.FENLEI_MULU, "");
            jiexiJsonMulu(new JSONObject(jsonMulu));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestHandleArrayList.add(requestAction.getFenLeiSonInfoF1(this));//请求分类目录列表

//        qingQiuShuJu("综合排序", false);
        mListener.autoRefresh();

        if ("搜索".equals(leixing)) {   //从搜索框进入
            isShowErijiMulu = false;
            tv_fenlei_zonghe.setText("分类");
        } else {
            tv_fenlei_zonghe.setText(strYijiMulu);
        }

    }

    public void initComponent() {

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //RecyclerView
        rc_fenleison_shangpin.setLayoutManager(gridLayoutManager2);
        rc_fenleison_shangpin.addItemDecoration(spaceItemDecorationGridView2);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rc_fenleison_shangpin.setHasFixedSize(true);
        mFenLeiSonAdapter = new FenLeiSonAdapter(listDatas);
        rc_fenleison_shangpin.setAdapter(mFenLeiSonAdapter);
        mFenLeiSonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(FenLeiSonActivity.this, ShangPinXiangQingActivity.class);
                intent.putExtra("id", listDatas.get(position).getFenLeiSon_id());
                intent.putExtra("info", listDatas.get(position));
                startActivity(intent);
            }
        });

        processData();

        //错误页面
        emptylayout_fenleison.setErrorDrawable(R.mipmap.img_error_layout);
        emptylayout_fenleison.setErrorMessage("网络出错了");
        emptylayout_fenleison.setErrorViewButtonId(R.id.buttonError);
        emptylayout_fenleison.setShowErrorButton(true);
        emptylayout_fenleison.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qingQiuShuJu(shaiXuanLeixXing, false);

            }
        });
        //空白页面
        emptylayout_fenleison.setEmptyDrawable(R.mipmap.img_empty_layout);
        emptylayout_fenleison.setEmptyMessage("暂无此商品");
        emptylayout_fenleison.setShowEmptyButton(false);

        /**
         * 软键盘搜索监听
         * */
        et_sousuo_info.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (et_sousuo_info.getText().toString().trim().length() != 0) {
                        hideSoftKeyboard();
                        leixing = "搜索";
                        isShowErijiMulu = false;
                        strYijiMulu = "分类";
                        fenlei_guanjianzi = et_sousuo_info.getText().toString();
                        page = 0;
                        listDatas.clear();
                        mFenLeiSonAdapter.notifyDataSetChanged();
                        tv_fenlei_zonghe.setText("分类");
                        rc_fenleison_shangpin.scrollToPosition(0);
                        requestHandleArrayList.add(requestAction.getSouSuoData(FenLeiSonActivity.this, shaiXuanLeixXing, fenlei_guanjianzi, page + ""));
                    } else {
                        et_sousuo_info.setText("");
                        DialogUtlis.oneBtnNormal(getmDialog(), "请输入关键字!");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 刷新加载
     */
    private void processData() {
        // 设置刷新加载
        mListener = new RLoadListener.Builder<FenLeiSonAdapter>()
                .setSwipeRefreshLayout(swipeRefreshLayout)
                .setRecyclerView(rc_fenleison_shangpin)
                .setAdapter(mFenLeiSonAdapter)
                .setRefreshLoadListener(this)
                .create();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        Log.e("----" + requestTag, response.toString());
        switch (requestTag) {
            //搜索进来的数据解析
            case RequestAction.TAG_GETSOUSUODATA:
                if (response.getString("状态").equals("成功")) {
                    num = Integer.parseInt(response.getString("条数"));
                    page = Integer.parseInt(response.getString("页数"));
                    if (num != 0) {
                        JSONArray jsonArray1 = response.getJSONArray("列表");
                        FenLeiSonBean fenLeiSonBean1;
                        JSONObject jsonObj;
                        for (int i = 0; i < num; i++) {
                            fenLeiSonBean1 = new FenLeiSonBean();
                            jsonObj = (JSONObject) jsonArray1.get(i);
                            L.e("0909jsonObj", jsonObj.toString());
                            fenLeiSonBean1.setFenLeiSon_title(jsonObj.getString("商品名称"));
                            fenLeiSonBean1.setFenLeiSon_id(jsonObj.getString("id"));
                            fenLeiSonBean1.setFenLeiSon_dizhi(jsonObj.getString("地址"));
                            fenLeiSonBean1.setFenLeiSon_jiage(jsonObj.getString("价格"));
                            fenLeiSonBean1.setFenLeiSon_img_url(jsonObj.getString("缩略图"));
                            fenLeiSonBean1.setFenLeiSon_leixin(jsonObj.getString("类型"));
                            fenLeiSonBean1.setFenLeiSon_lingshoujia(jsonObj.getString("建议零售价"));
                            fenLeiSonBean1.setItemType(flag ? FenLeiSonBean.TwoLine : FenLeiSonBean.OneLine);
                            listDatas.add(fenLeiSonBean1);
                        }
                    }
                }

                /* setNewData的作用在于刷新数据时将“没有更多数据”这个状态清除 */
                if (page == 1) {
                    mFenLeiSonAdapter.setNewData(listDatas);
                }

                mFenLeiSonAdapter.notifyDataSetChanged();
                if (listDatas.size() == 0) {
                    emptylayout_fenleison.showEmpty();
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    swipeRefreshLayout.setEnabled(true);
                    emptylayout_fenleison.hide();
                }

                if (page == 1) { // 刷新
                    mListener.setRefreshComplete();
                } else { // 加载
                    if (num == 10) {
                        mFenLeiSonAdapter.loadMoreComplete(); // 本次加载完成
                    }
                }
                if (num != 10) {
                    mFenLeiSonAdapter.loadMoreEnd(); // 没有更多
                }
                mListener.setRefreshLayoutTrue(); // 加载完成后，开启刷新
                break;

            case RequestAction.TAG_SHOP_FENLEISONINFO:   //分类商品信息

                if (1 == response.getInt("页数")){
                    listDatas.clear();
                    mFenLeiSonAdapter.notifyDataSetChanged();
                }
                JSONArray jsonArray = response.getJSONArray("数据");
                JSONObject shangPinObj;
                FenLeiSonBean fenLeiSonBean;
                for (int i = 0; i < jsonArray.length(); i++) {
                    shangPinObj = jsonArray.getJSONObject(i);
                    Log.e("0909shangPinObj", shangPinObj.toString());
                    fenLeiSonBean = new FenLeiSonBean();
                    fenLeiSonBean.setFenLeiSon_title(shangPinObj.getString("商品名称"));
                    fenLeiSonBean.setFenLeiSon_id(shangPinObj.getString("id"));
                    fenLeiSonBean.setFenLeiSon_dizhi(shangPinObj.getString("地址"));
                    fenLeiSonBean.setFenLeiSon_jiage(shangPinObj.getString("价格"));
                    fenLeiSonBean.setFenLeiSon_img_url(shangPinObj.getString("缩略图"));
                    fenLeiSonBean.setFenLeiSon_leixin(shangPinObj.getString("类型"));
                    fenLeiSonBean.setFenLeiSon_lingshoujia(shangPinObj.getString("建议零售价"));
                    fenLeiSonBean.setItemType(flag ? FenLeiSonBean.TwoLine : FenLeiSonBean.OneLine);
                    listDatas.add(fenLeiSonBean);
                }
                num = response.getInt("条数");
                page = response.getInt("页数");

                Log.e("-page2-", "--" + page);
                if (page == 1) {
                    Log.e("-page2-", "--" + page);
                    mFenLeiSonAdapter.setNewData(listDatas);
                }

                mFenLeiSonAdapter.notifyDataSetChanged();

                if (listDatas.size() == 0) {
                    emptylayout_fenleison.showEmpty();
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    swipeRefreshLayout.setEnabled(true);
                    emptylayout_fenleison.hide();
                }

                if (page == 1) { // 刷新
                    mListener.setRefreshComplete();
                } else { // 加载
                    if (num == 10) {
                        mFenLeiSonAdapter.loadMoreComplete(); // 本次加载完成
                    }
                }
                if (num != 10) {
                    mFenLeiSonAdapter.loadMoreEnd(); // 没有更多
                }
                mListener.setRefreshLayoutTrue(); // 加载完成后，开启刷新
                break;

            case RequestAction.TAG_SHOP_FENLEISONINFOF1:   //分类二级目录解析

                // listMuluData.clear();
                jiexiJsonMulu(response);
                preferences.edit().putString(ConstantUtlis.FENLEI_MULU, response.toString()).apply();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        switch(requestTag) {
            //搜索进来的数据解析
            case RequestAction.TAG_GETSOUSUODATA:
            case RequestAction.TAG_SHOP_FENLEISONINFO:
                if (page == 0) {
                    mListener.setRefreshComplete();
                    emptylayout_fenleison.showError();
                } else {
                    mFenLeiSonAdapter.loadMoreFail(); // 加载失败
                    mListener.setRefreshLayoutTrue(); // 加载完成后，开启刷新
                }
                break;
        }
    }

    /**
     * 初始化界面
     */
    private void resetView() {
        tv_fenlei_zonghe.setTextColor(Color.parseColor("#333333"));
        tv_fenlei_xiaoliang.setTextColor(Color.parseColor("#333333"));
        tv_fenlei_jiage.setTextColor(Color.parseColor("#333333"));
        iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe0);
        iv_fenlei_jiage.setImageResource(R.mipmap.ic_fenlei_jiage0);
    }

    public void changeView(int i) {
        resetView();
        switch (i) {
            case 1:
                if (zonghe_zhuangtai == 0) {
                    tv_fenlei_zonghe.setTextColor(Color.parseColor("#E33333"));
                    tv_fenlei_zonghe.setText("分类");
                    iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                    jiage_zhuangtai = 0;
                    qingQiuShuJu("综合排序", true);
                } else if (zonghe_zhuangtai == 1) {
                    tv_fenlei_zonghe.setTextColor(Color.parseColor("#E33333"));
                    tv_fenlei_zonghe.setText("新品");
                    iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                    jiage_zhuangtai = 0;
                    qingQiuShuJu("新品优先", true);
                }
                break;
            case 2:
                tv_fenlei_xiaoliang.setTextColor(Color.parseColor("#E33333"));
                jiage_zhuangtai = 0;
                qingQiuShuJu("销量", true);
                break;
            case 3:
                tv_fenlei_jiage.setTextColor(Color.parseColor("#E33333"));
                if (jiage_zhuangtai == 0) {
                    jiage_zhuangtai = -1;
                    iv_fenlei_jiage.setImageResource(R.mipmap.ic_fenlei_jiage2);
                    qingQiuShuJu("价格低", true);
                } else if (jiage_zhuangtai == 1) {
                    jiage_zhuangtai = -1;
                    iv_fenlei_jiage.setImageResource(R.mipmap.ic_fenlei_jiage2);
                    qingQiuShuJu("价格低", true);
                } else if (jiage_zhuangtai == -1) {
                    jiage_zhuangtai = 1;
                    iv_fenlei_jiage.setImageResource(R.mipmap.ic_fenlei_jiage1);
                    qingQiuShuJu("价格高", true);
                }
                break;
            default:
                break;
        }
    }


    private void qingQiuShuJu(String str, boolean isLoading) {
        if ("综合".equals(leixing)) {
            if (isLoading) {
                //加载更多数据
                if (shaiXuanLeixXing.equals(str)) {
                    return;
                } else {
                    emptylayout_fenleison.hide();
                    //加载数据
                    page = 0;
                    num = 0;
                    listDatas.clear();
                    mFenLeiSonAdapter.notifyDataSetChanged();
                    rc_fenleison_shangpin.scrollToPosition(0);
                    // requestHandleArrayList.add(requestAction.shop_hp_spfl(this, str, fenlei_guanjianzi, page + "",fenlei));
                    requestHandleArrayList.add(requestAction.getFenLeiSonInfo(this, str, strYijiMulu, strErjiMulu, page + ""));
                }
            } else {
                emptylayout_fenleison.hide();
                //刷新数据
                // requestHandleArrayList.add(requestAction.shop_hp_spfl(this, str, fenlei_guanjianzi, page + "",fenlei));
                requestHandleArrayList.add(requestAction.getFenLeiSonInfo(this, str, strYijiMulu, strErjiMulu, page + ""));
            }
            shaiXuanLeixXing = str;
        } else if ("搜索".equals(leixing)) {
            if (isLoading) {
                //加载更多数据
                if (shaiXuanLeixXing.equals(str)) {
                    return;
                } else {
                    emptylayout_fenleison.hide();
                    //加载数据
                    page = 0;
                    num = 0;
                    listDatas.clear();
                    mFenLeiSonAdapter.notifyDataSetChanged();
                    rc_fenleison_shangpin.scrollToPosition(0);
                    requestHandleArrayList.add(requestAction.getSouSuoData(this, str, fenlei_guanjianzi, page + ""));
                }
            } else {
                emptylayout_fenleison.hide();
                //刷新数据
                requestHandleArrayList.add(requestAction.getSouSuoData(this, str, fenlei_guanjianzi, page + ""));
            }
            shaiXuanLeixXing = str;
        }
    }

    @OnClick({R.id.iv_toptitle_back, R.id.ll_fenlei_zonghe, R.id.ll_fenlei_xiaoliang, R.id.ll_fenlei_jiage, R.id.ll_fenlei_qiehuanjiemian, R.id.rl_shouye_gouwuche})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //顶部搜索
            case R.id.iv_toptitle_back:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.rl_shouye_gouwuche:  //购物车
                if (check_login_tiaozhuang()) {
                    Intent intent = new Intent(FenLeiSonActivity.this, GouWuCheActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 666);
                }
                break;
//            case R.id.tv_sousuo:
//                hideSoftKeyboard();
//                leixing = "搜索";
//                isShowErijiMulu = false;
//                strYijiMulu = "分类";
//                fenlei_guanjianzi = et_sousuo_info.getText().toString();
//                page = 0;
//                listDatas.clear();
//                myRecyclerViewAdapter.notifyDataSetChanged();
//                tv_fenlei_zonghe.setText("分类");
////                qingQiuShuJu("价格高", true);
//                rc_fenleison_shangpin.scrollToPosition(0);
//                requestHandleArrayList.add(requestAction.getSouSuoData(this, shaiXuanLeixXing, fenlei_guanjianzi, page + ""));
//                break;

            //显示分类目录
            case R.id.ll_fenlei_zonghe:
                hideSoftKeyboard();
                resetView();
                tv_fenlei_zonghe.setTextColor(Color.parseColor("#E33333")); //分类字段
                iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);

                //设置listview的高度为屏幕高度的2/3
                if (fenleiMuluPop == null) {
                    //  maxListViewHight = rlGouWuChe.getTop() - ll_fenlei_zonghe.getBottom() * 2 - 50;
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    maxListViewHight = dm.heightPixels / 3 * 2;
                    fenleiMuluPop = new FenleiMuluPop(this);
                }

                //显示popupwindown
                if (listMuluData.size() > 0) {    //目录集合不为空
                    fenleiMuluPop.showAsDropDown(ll_fenlei_zonghe, 20, -15);  //展示一级目录
                    if (!"全部".equals(strYijiMulu) && isShowErijiMulu) {  //一级目录不点击全部时，显示二级目录
                        if (fenleiMuluPop2 == null) {
                            for (int i = 0; i < listMuluData.size(); i++) {  //第一次进入时，获取跳转过来的类目位于list中的位置
                                if (listMuluData.get(i).getName().equals(strYijiMulu)) {
                                    firstComeinSeclct = i;
                                    break;
                                }
                            }
                            fenleiMuluPop2 = new FenleiMuluPop2(FenLeiSonActivity.this, listMuluData.get(firstComeinSeclct).getErji());
                        }
                        fenleiMuluPop2.showAsDropDown(ll_fenlei_xiaoliang, 60, -5);
                    }
                }
                break;
            case R.id.ll_fenlei_xiaoliang:
                hideSoftKeyboard();
                changeView(2);
                break;
            case R.id.ll_fenlei_jiage:
                hideSoftKeyboard();
                changeView(3);
                break;
            case R.id.ll_fenlei_qiehuanjiemian:
                hideSoftKeyboard();
                int canVisPosition;
                if (flag) {
                    canVisPosition = gridLayoutManager2.findFirstVisibleItemPosition();
                    rc_fenleison_shangpin.setLayoutManager(gridLayoutManager1);
                    rc_fenleison_shangpin.removeItemDecoration(spaceItemDecorationGridView1);
                    rc_fenleison_shangpin.removeItemDecoration(spaceItemDecorationGridView2);
                    rc_fenleison_shangpin.addItemDecoration(spaceItemDecorationGridView1);
                    iv_fenlei_jiemianl.setImageResource(R.mipmap.ic_fenlei_jiemian_1);
                    for (int i = 0; i < listDatas.size(); i++) {
                        listDatas.get(i).setItemType(FenLeiSonBean.OneLine);
                    }
                    flag = false;
                } else {
                    canVisPosition = gridLayoutManager1.findFirstVisibleItemPosition();
                    rc_fenleison_shangpin.setLayoutManager(gridLayoutManager2);
                    rc_fenleison_shangpin.removeItemDecoration(spaceItemDecorationGridView1);
                    rc_fenleison_shangpin.removeItemDecoration(spaceItemDecorationGridView2);
                    rc_fenleison_shangpin.addItemDecoration(spaceItemDecorationGridView2);
                    iv_fenlei_jiemianl.setImageResource(R.mipmap.ic_fenlei_jiemian_2);
                    for (int i = 0; i < listDatas.size(); i++) {
                        listDatas.get(i).setItemType(FenLeiSonBean.TwoLine);
                    }
                    flag = true;
                }
                rc_fenleison_shangpin.scrollToPosition(canVisPosition);

                break;
            default:
                break;
        }
    }

    private void hideSoftKeyboard(){
        imm.hideSoftInputFromWindow(et_sousuo_info.getWindowToken(), 0);
        et_sousuo_info.clearFocus();
    }

    @Override
    public void onRefresh() {
        page = 0;
        listDatas.clear();
        mFenLeiSonAdapter.notifyDataSetChanged();
        Log.e("onRefresh", "onRefresh: -----" + shaiXuanLeixXing);

        qingQiuShuJu(shaiXuanLeixXing, false);
    }

    @Override
    public void onLoading() {
        qingQiuShuJu(shaiXuanLeixXing, false);
    }

    /**
     * 分类:一级目录
     */
    private class FenleiMuluPop extends PopupWindow {
        private View yijiPopView;

        @SuppressLint("NewApi")
        private FenleiMuluPop(Context context) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            yijiPopView = inflater.inflate(R.layout.pop_fenleison_listview, null);
            MaxHeightListView maxHeightListViewYiji = (MaxHeightListView) yijiPopView.findViewById(R.id.maxheightlistview);

            // 设置SelectPicPopupWindow的View
            this.setContentView(yijiPopView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            // 设置popupWindow以外可以触摸
            this.setOutsideTouchable(true);
            // 以下两个设置点击空白处时，隐藏掉pop窗口
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(1f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());

            maxHeightListViewYiji.setListViewHeight(maxListViewHight + 10); //listview最大高度
            maxHeightListViewYiji.setBackground(ContextCompat.getDrawable(context, R.mipmap.shang));
            adapter = new CommonAdapter<FenLeiSonYijiMuluBean>(FenLeiSonActivity.this, R.layout.fenleison_erji_item, listMuluData) {
                @Override
                public void convert(ViewHolder holder, FenLeiSonYijiMuluBean fenLeiSonYijiMuluBean) {
                    TextView textView = holder.getView(R.id.tv);
                    textView.setText(fenLeiSonYijiMuluBean.getName());

                    if (fenLeiSonYijiMuluBean.getName().equals(strYijiMulu))
                        textView.setTextColor(Color.parseColor("#c01923"));
                    else
                        textView.setTextColor(Color.parseColor("#666666"));
                }
            };

            maxHeightListViewYiji.setAdapter(adapter);

            maxHeightListViewYiji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    strYijiMulu = listMuluData.get(position).getName();
                    strErjiMulu = "全部";
                    leixing = "综合";
                    page = 0;
                    isShowErijiMulu = false;

                    //点击全部加载数据
                    if (strYijiMulu.equals("全部")) {
                        tv_fenlei_zonghe.setText(strYijiMulu);
                        isShowErijiMulu = false;
                        fenleiMuluPop.dismiss();
                        qingQiuShuJu("综合排序", false);
                    } else {
                        tv_fenlei_zonghe.setText(strYijiMulu);
                        fenleiMuluPop2 = new FenleiMuluPop2(FenLeiSonActivity.this, listMuluData.get(position).getErji()); //弹出二级目录
                        fenleiMuluPop2.showAsDropDown(ll_fenlei_xiaoliang, 60, -5);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 分类二级目录
     */
    private class FenleiMuluPop2 extends PopupWindow {
        private View erjiPopView;

        @SuppressLint("NewApi")
        private FenleiMuluPop2(Context context, final List<FeiLeiSonErjiMuluBean> erjiList) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            erjiPopView = inflater.inflate(R.layout.pop_fenleison_listview, null);
            MaxHeightListView maxHeightListViewErji = (MaxHeightListView) erjiPopView.findViewById(R.id.maxheightlistview);

            // 设置SelectPicPopupWindow的View
            this.setContentView(erjiPopView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置popupWindow以外可以触摸,不获取焦点
            this.setOutsideTouchable(true);
            if (Build.VERSION.SDK_INT <= 18)
                this.setFocusable(true);
            else
                this.setFocusable(false);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(1f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());

            maxHeightListViewErji.setListViewHeight(maxListViewHight); //listview最大高度
            maxHeightListViewErji.setBackground(ContextCompat.getDrawable(context, R.mipmap.zuo));

            adapter2 = new CommonAdapter<FeiLeiSonErjiMuluBean>(FenLeiSonActivity.this, R.layout.fenleison_erji_item, erjiList) {
                @Override
                public void convert(ViewHolder holder, FeiLeiSonErjiMuluBean feiLeiSonErjiMuluBean) {
                    TextView textView = holder.getView(R.id.tv);
                    textView.setText(feiLeiSonErjiMuluBean.getName());

                    if (feiLeiSonErjiMuluBean.getName().equals(strErjiMulu))
                        textView.setTextColor(Color.parseColor("#c01923"));
                    else
                        textView.setTextColor(Color.parseColor("#666666"));
                }
            };

            maxHeightListViewErji.setAdapter(adapter2);
            maxHeightListViewErji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    strErjiMulu = erjiList.get(position).getName();
                    leixing = "综合";
                    page = 0;
                    isShowErijiMulu = true;

                    qingQiuShuJu("综合排序", false);
                    if (strErjiMulu.equals("全部")) {
                        tv_fenlei_zonghe.setText(strYijiMulu);
                    } else {
                        tv_fenlei_zonghe.setText(strErjiMulu);
                    }
                    if (fenleiMuluPop.isShowing())
                        fenleiMuluPop.dismiss();
                    if (fenleiMuluPop2.isShowing())
                        fenleiMuluPop2.dismiss();
                    adapter2.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    private class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
            try {
                if (!fenleiMuluPop.isShowing() && fenleiMuluPop2.isShowing()) {
                    fenleiMuluPop.dismiss();
                    fenleiMuluPop2.dismiss();
                }
            } catch (Exception e) {

            }
            // changeView(1);
        }
    }

    /**
     * 解析目录列表
     *
     * @param response
     */
    private void jiexiJsonMulu(JSONObject response) {
        listMuluData.clear();
        try {
            JSONArray jsonArraylist = response.getJSONArray("列表");
            JSONObject jsonObj;
            FenLeiSonYijiMuluBean fenLeiSonYijiMuluBean;

            for (int i = 0; i < jsonArraylist.length(); i++) {
                jsonObj = jsonArraylist.getJSONObject(i);
                fenLeiSonYijiMuluBean = new FenLeiSonYijiMuluBean();
                fenLeiSonYijiMuluBean.setName(jsonObj.getString("一级分类"));

                JSONArray jsonArray2 = jsonObj.getJSONArray("二级分类");
                List<FeiLeiSonErjiMuluBean> listMuluData2 = new ArrayList<>();
                FeiLeiSonErjiMuluBean fenLeiSonErjiMuluBean;
                for (int j = 0; j < jsonArray2.length(); j++) {
                    fenLeiSonErjiMuluBean = new FeiLeiSonErjiMuluBean();
                    fenLeiSonErjiMuluBean.setName(jsonArray2.get(j).toString());
                    listMuluData2.add(fenLeiSonErjiMuluBean);
                    fenLeiSonYijiMuluBean.setRrji(listMuluData2);
                }
                listMuluData.add(fenLeiSonYijiMuluBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
