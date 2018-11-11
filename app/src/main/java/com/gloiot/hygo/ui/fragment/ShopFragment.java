package com.gloiot.hygo.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.githang.statusbar.StatusBarCompat;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.hygo.R;
import com.gloiot.hygo.bean.ShopShowHeaderBean;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseFragment;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.my.youhuiquan.LingQuYouHuiQuanActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.ui.activity.shopping.JinriTejiaActivity;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.ui.activity.shopping.SousuoShangpingActivity;
import com.gloiot.hygo.ui.activity.shopping.fenlei.FenLeiSonActivity;
import com.gloiot.hygo.ui.activity.shopping.gouwuche.GouWuCheActivity;
import com.gloiot.hygo.ui.activity.shopping.wodeshoucang.ShouCangActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.GlideImageLoader;
import com.gloiot.hygo.widget.GuideView;
import com.gloiot.hygo.widget.RebateDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.adapter.recyclerview.OnItemClickListener;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.StatusBarUtil;
import com.zyd.wlwsdk.widge.EmptyLayout;
import com.zyd.wlwsdk.widge.SpaceItemDecorationGridView;
import com.zyd.wlwsdk.widge.refresh.RLoadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 时间：on 2017/12/2 09:48.
 * 作者：by hygo04
 * 功能：商城
 */

public class ShopFragment extends BaseFragment implements RLoadListener.RefreshLoadListener, View.OnClickListener {

    @Bind(R.id.empty_fragment_shop)
    EmptyLayout emptyFragmentShop;
    @Bind(R.id.sf_fragment_shop)
    SwipeRefreshLayout SwipeRefreshShop;
    @Bind(R.id.rv_fragment_shop)
    RecyclerView recyclerViewShop;
    @Bind(R.id.view_status_bar)
    View viewStatusBar;
    @Bind(R.id.dotView)
    View dotView;
    @Bind(R.id.fragment_shopping_shouye_title)
    LinearLayout titlebar;

    // 是否是第一次请求
    private boolean isFirstRequest = true;
    // 是否是刷新
    private boolean isRefresh = true;
    private int yeshu = 0, tiaoshu = 0;
    //大礼包的
    private boolean dalibaoFlag = false;
    private int red = 102, green = 102, blue = 102;

    private ShopShowHeaderBean headerBean = new ShopShowHeaderBean();
    private List<FenLeiSonBean> shopShowData = new ArrayList<>(); // 推荐商品的数据
    private ShopAdapter shopAdapter;

    private View bannerView, fenLeiView, middleView; // 所有头部View
    private Banner banner;
    private ImageView ivFenLeiShopView, ivHuoDong, ivQuanqiu, ivTuijian;
    private RelativeLayout rlQuanQiuGou;

    private RecyclerView fenLeiRecyclerView, jingXuanRecyclerView;
    private CommonAdapter fenLeiAdapter, jingXuanAdapter;

    private RLoadListener<ShopAdapter> mListener;
    private RequestOptions options;

    private int mDistance = 0;      // RecyclerView滑动位置

    // 指引View
    private GuideView guideView;
    // 指引view是否正在展示中
    public boolean isGuideViewShow = false;

    public ShopFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, view);

        initHeaderView(inflater);
        init(view);

        BroadcastManager.getInstance(mContext).addAction("com.gloiot.hygo.判断网络", new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    int i = Integer.parseInt(intent.getStringExtra("data"));
                    if (i == -1) {
                    } else {
                        if (SwipeRefreshShop != null) {
                            mListener.autoRefresh();
                        }
                        setTab();
                    }
                } catch (Exception e) {

                }
            }
        });

        BroadcastManager.getInstance(mContext).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                hongdian();
            }
        });
        return view;
    }

    private void init(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            viewStatusBar.setVisibility(View.VISIBLE);
        }

        // 初始化标题栏全透明
        setSystemBarAlpha(0);
        //滑动监听事件
        recyclerViewShop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //dy:每一次竖直滑动增量 向下为正 向上为负
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mDistance += dy;
                // 判断recyclerView是否滑倒顶部（解决未知错误）
                if (!recyclerView.canScrollVertically(-1)){
                    mDistance = 0;
                }
                setSystemBarAlpha(mDistance);
            }
        });

        // 设置空View
        emptyFragmentShop.setErrorDrawable(R.mipmap.img_error_layout);
        emptyFragmentShop.setErrorMessage("出错了");
        emptyFragmentShop.setErrorViewButtonId(R.id.buttonError);
        emptyFragmentShop.setShowErrorButton(true);
        emptyFragmentShop.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyFragmentShop.hide();
                requestData(true);
            }
        });

        // 设置Glide加载图片占位图
        options = new RequestOptions().centerCrop().placeholder(R.drawable.default_image);

        // 创建布局管理
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewShop.setLayoutManager(manager);
        recyclerViewShop.addItemDecoration(new SpaceItemDecorationGridView(10, 2));

        // 创建适配器
        shopAdapter = new ShopAdapter();
        shopAdapter.openLoadAnimation();
        shopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (shopShowData.size() > 0) {
                    Intent intent = new Intent(getActivity(), ShangPinXiangQingActivity.class);
                    intent.putExtra("id", shopShowData.get(position).getFenLeiSon_id());
                    intent.putExtra("info", shopShowData.get(position));
                    startActivity(intent);
                }
            }
        });

        // 给RecyclerView设置适配器
        recyclerViewShop.setAdapter(shopAdapter);

        // 设置下拉控件的位置、样式
        SwipeRefreshShop.setProgressViewEndTarget(true, 300);
        SwipeRefreshShop.setDistanceToTriggerSync(150);
        SwipeRefreshShop.setColorSchemeColors(getResources().getColor(R.color.swipeRefresh_color));

        // 设置刷新加载
        mListener = new RLoadListener.Builder<ShopAdapter>()
                .setSwipeRefreshLayout(SwipeRefreshShop)
                .setRecyclerView(recyclerViewShop)
                .setAdapter(shopAdapter)
                .setRefreshLoadListener(this)
                .create();

        // 加载上一次请求成功的数据
        initDatas();
        mListener.autoRefresh(); // 开启自动刷新请求
//        requestData(true);
    }

    /**
     * 设置标题栏背景透明度
     * @param alpha 透明度
     */
    private void setSystemBarAlpha(int alpha) {
        if (alpha >= 255) {
            alpha = 255;
            //获取手机型号
            String phone_type = preferences.getString(ConstantUtlis.SP_PHENENAME, "");
            //状态栏（vivo手机单独配置颜色）
            if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#B4B4B4"), true);
            } else {
                StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"), true);
            }
        } else {
            StatusBarUtil.transparencyBar(getActivity());
        }
        titlebar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));//透明效果是由参数1决定的，透明范围[0,255]
    }

    /**
     * 初始化recyclerView的所有头部View
     * @param inflater
     */
    private void initHeaderView(LayoutInflater inflater) {
        // 头部-轮播图
        bannerView = inflater.inflate(R.layout.fragment_shop_banner_view, (ViewGroup) recyclerViewShop.getParent(), false);
        // 头部-分类
        fenLeiView = inflater.inflate(R.layout.fragment_shop_view, (ViewGroup) recyclerViewShop.getParent(), false);
        // 头部-middle
        middleView = inflater.inflate(R.layout.fragment_shop_middle_view, (ViewGroup) recyclerViewShop.getParent(), false);
        initHeaderViewSon();

        GridLayoutManager fenLeiManager = new GridLayoutManager(mContext, 5);
        fenLeiRecyclerView = (RecyclerView) fenLeiView.findViewById(R.id.rv_shop_view);
        fenLeiRecyclerView.setLayoutManager(fenLeiManager);
        GridLayoutManager jingXuanManager = new GridLayoutManager(mContext, 2);
        jingXuanRecyclerView = (RecyclerView) middleView.findViewById(R.id.fragment_shop_middle_rv);
        jingXuanRecyclerView.setLayoutManager(jingXuanManager);
        jingXuanRecyclerView.addItemDecoration(new SpaceItemDecorationGridView(6, 2));
    }

    /**
     * 初始化所有头部View的控件
     */
    private void initHeaderViewSon() {
        banner = (Banner) bannerView.findViewById(R.id.fragment_shop_banner);
        // 分类背景
        ivFenLeiShopView = (ImageView) fenLeiView.findViewById(R.id.iv_shop_view);
        // 活动专区
        ivHuoDong = (ImageView) middleView.findViewById(R.id.fragment_shop_middle_iv_huodong);
        // 全球购背景
        ivQuanqiu = (ImageView) middleView.findViewById(R.id.fragment_shop_middle_iv_quanqiu);
        // 全球购整个
        rlQuanQiuGou = (RelativeLayout) middleView.findViewById(R.id.fragment_shop_middle_rl_quanqiugou);
        // 推荐商品图片
        ivTuijian = (ImageView) middleView.findViewById(R.id.fragment_shop_middle_iv_tuijian);

    }

    /**
     * 初始化头部View的Adapter
     */
    private void initHeaderAdapter() {
        //分类
        fenLeiAdapter = new CommonAdapter<ShopShowHeaderBean.ClassifyBean>(getActivity(), R.layout.item_shouye_fenleitu, headerBean.getClassify()) {
            @Override
            public void convert(ViewHolder holder, ShopShowHeaderBean.ClassifyBean classifyBean) {
                holder.setText(R.id.tv_fenlei, classifyBean.get类别名称());
                holder.setTextColor(R.id.tv_fenlei, Color.rgb(red, green, blue));
                ImageView imageView = holder.getView(R.id.img_fenlei_tu);
                Glide.with(mContext).load(classifyBean.get图标()).apply(options).into(imageView);
            }
        };
        fenLeiAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (headerBean.getClassify().size() > 0) {

                    String url = headerBean.getClassify().get(position).get图片链接();
                    if (url == null) {
                        Intent intent = new Intent(getActivity(), FenLeiSonActivity.class);
                        String strFenlei = headerBean.getClassify().get(position).get类别名称();
                        if (strFenlei.equals("全部分类")) {
                            strFenlei = "全部";
                        }
                        intent.putExtra("分类", strFenlei);
                        intent.putExtra("类型", "综合");
                        startActivity(intent);
                    } else if (!TextUtils.isEmpty(url)) {
                        if (check_login_tiaozhuang()) {
                            if (checkIsSetPwd()) {
                                Intent intent = new Intent(mContext, WebActivity.class);
                                intent.putExtra("url", mUrl(url));
                                startActivity(intent);
                            }
                        }
                    }
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        fenLeiRecyclerView.setAdapter(fenLeiAdapter);

        //精选
        jingXuanAdapter = new CommonAdapter<ShopShowHeaderBean.TableBean>(getActivity(), R.layout.item_shouye_guangli, headerBean.getTable()) {
            @Override
            public void convert(ViewHolder holder, ShopShowHeaderBean.TableBean tableBean) {
                ImageView imageView = holder.getView(R.id.img_tupian);
                Glide.with(mContext).load(tableBean.get图片()).apply(options).into(imageView);
            }
        };
        jingXuanAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (headerBean.getTable().size() > 0) {
                    Intent intent = new Intent(getActivity(), JinriTejiaActivity.class);
                    intent.putExtra("类型", headerBean.getTable().get(position).get标题());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        jingXuanRecyclerView.setAdapter(jingXuanAdapter);
    }

    private void initDatas() {
        if (null != preferences.getString(ConstantUtlis.SHANGCHENG_SHOUYE, null)) {
            try {
                JSONObject jsonObject = new JSONObject(preferences.getString(ConstantUtlis.SHANGCHENG_SHOUYE, null));
                ShuJuChuLi(-100, jsonObject);
            } catch (JSONException e) {
                L.e("商城首页解析本地数据", "商城首页解析本地数据出错！");
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.rl_sousuo, R.id.rl_shouye_gouwuche, R.id.rl_shouye_shoucang, R.id.rl_conversationList})
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_sousuo:
                intent = new Intent(getActivity(), SousuoShangpingActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_shouye_gouwuche:
                if (check_login_tiaozhuang()) {
                    intent = new Intent(getActivity(), GouWuCheActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 666);
                }
                break;
            case R.id.rl_shouye_shoucang:
                if (check_login_tiaozhuang()) {
                    intent = new Intent(getActivity(), ShouCangActivity.class);
                    startActivityForResult(intent, 666);
                }
                break;
            case R.id.rl_conversationList:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.replaceFragment();
                break;
        }
    }

    /**
     * 请求数据
     */
    private void requestData(final boolean isShouYe) {
        if (isShouYe) {
            yeshu = 0;
            requestAction.GetShouyeData(this);
        } else {
            requestAction.GetShouyeTuijianData(this, String.valueOf(yeshu), null, -1);
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_SHOUYE:
            case RequestAction.TAG_SHOP_C_RECOMMEND:
                ShuJuChuLi(requestTag, response);
                break;
            case RequestAction.TAG_SHOP_HP_SPREE:
                //大礼包
                if ("是".equals(response.getString("是否显示"))) {
                    if (!TextUtils.isEmpty(response.getString("大礼包")) && this.getUserVisibleHint()) {
                        RebateDialog.show(mContext, response.getString("大礼包"), "", new View.OnClickListener() {
                            @Override
                            public void onClick(View vX) {
                                RebateDialog.dismiss(mContext);
                                if (check_login_tiaozhuang()) {
                                    mContext.startActivity(new Intent(mContext, LingQuYouHuiQuanActivity.class));
                                } else {
                                    dalibaoFlag = false;
                                }
                            }
                        });
                    }
                }
                dalibaoFlag = true;
                break;
        }
    }

    private void ShuJuChuLi(int requestTag, final JSONObject response) throws JSONException {
        if (emptyFragmentShop != null) {
            emptyFragmentShop.hide();
        }
        switch (requestTag) {
            case -100: // -100是特殊tag，表示解析本地缓存数据
            case RequestAction.TAG_SHOUYE:
                L.e("TAG_SHOUYE：", response.toString());

                if (response.getString("状态").equals("成功")) {

                    editor.putString(ConstantUtlis.SHANGCHENG_SHOUYE, response.toString());
                    editor.apply();

                    headerBean.set首页图片(response.getString("首页图片"));
                    headerBean.set字体颜色(response.getString("字体颜色"));
                    headerBean.set活动专区(response.getString("活动专区"));
                    headerBean.set活动专区图片链接(response.getString("活动专区图片链接"));
                    headerBean.set全球购(response.getString("全球购"));
                    headerBean.set全球购条数(response.getInt("全球购条数"));
                    headerBean.set返现图(response.getString("返现图"));
                    headerBean.set返现图条数(response.getInt("返现图条数"));
                    headerBean.set推荐商品图片(response.getString("推荐商品图片"));
                    headerBean.set推荐商品图片条数(response.getInt("推荐商品图片条数"));

                    headerBean.setPagesCount(response.getInt("pagesCount"));
                    headerBean.setClassifyCount(response.getInt("classifyCount"));
                    headerBean.setTableCount(response.getInt("tableCount"));

                    // 轮播图集合
                    if (headerBean.getPagesCount() != 0) {
                        List<ShopShowHeaderBean.PagesBean> pagesList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("pages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            ShopShowHeaderBean.PagesBean pagesBean = new ShopShowHeaderBean.PagesBean();
                            pagesBean.set图片(jsonObject.getString("图片"));
                            pagesBean.set商品id(jsonObject.getString("商品id"));
                            pagesBean.set图片链接(jsonObject.getString("图片链接"));

                            pagesList.add(pagesBean);
                        }
                        headerBean.setPages(pagesList);
                    }

                    // 分类集合
                    if (headerBean.getClassifyCount() != 0) {
                        List<ShopShowHeaderBean.ClassifyBean> classifyList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("classify");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            ShopShowHeaderBean.ClassifyBean classifyBean = new ShopShowHeaderBean.ClassifyBean();
                            classifyBean.set类别名称(jsonObject.getString("类别名称"));
                            classifyBean.set图标(jsonObject.getString("图标"));
                            classifyBean.set图片链接(jsonObject.has("图片链接") ? jsonObject.getString("图片链接") : null);

                            classifyList.add(classifyBean);
                        }
                        headerBean.setClassify(classifyList);
                    }

                    // 精选集合
                    if (headerBean.getTableCount() != 0) {
                        List<ShopShowHeaderBean.TableBean> tableList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("table");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            ShopShowHeaderBean.TableBean tableBean = new ShopShowHeaderBean.TableBean();

                            tableBean.set标题(jsonObject.getString("标题"));
                            tableBean.set图片(jsonObject.getString("图片"));

                            tableList.add(tableBean);
                        }
                        headerBean.setTable(tableList);
                    }

                    //返现
                    if (!TextUtils.isEmpty(headerBean.get返现图()) && isFirstRequest && this.getUserVisibleHint()) {
                        RebateDialog.show(mContext, headerBean.get返现图());
                    }

                    initHeaderAdapter();
                    // 请求数据成功后，设置头部View和数据
                    setHeaderViewBanner();
                    setHeaderViewFenLei();
                    setHeaderViewMiddle();
                }

                L.e("requestTag:", requestTag + "-");
                // -100解析缓存数据不是网络请求操作
                if (requestTag != -100) {
                    // 刷新完成
                    mListener.setRefreshComplete();

                    requestData(false);
                }
                isFirstRequest = false;
                break;
            case RequestAction.TAG_SHOP_C_RECOMMEND:

                L.e("shop_hp_recommendC:", response.toString());
                if (response.getString("状态").equals("成功")) {
                    yeshu = response.getInt("页数");
                    tiaoshu = response.getInt("推荐条数");

                    if (isRefresh) {
                        shopShowData.clear();
                    }

                    int preEndIndex = shopShowData.size(); // 用于确定加载更多的起始位置

                    if (response.getInt("推荐条数") > 0) {
                        JSONArray json = response.getJSONArray("推荐商品");
                        for (int i = 0; i < json.length(); i++) {
                            FenLeiSonBean fenLeiSonBean = new FenLeiSonBean();
                            JSONObject jsonObject = (JSONObject) json.get(i);

                            if (jsonObject.isNull("id"))
                                continue;

                            fenLeiSonBean.setFenLeiSon_id(JSONUtlis.getString(jsonObject, "id"));
                            fenLeiSonBean.setFenLeiSon_title(JSONUtlis.getString(jsonObject, "商品名称"));
                            fenLeiSonBean.setFenLeiSon_img_url(JSONUtlis.getString(jsonObject, "缩略图"));
                            fenLeiSonBean.setFenLeiSon_jiage(JSONUtlis.getString(jsonObject, "价格"));
                            fenLeiSonBean.setFenLeiSon_dizhi(JSONUtlis.getString(jsonObject, "地址"));
                            fenLeiSonBean.setFenLeiSon_leixin(JSONUtlis.getString(jsonObject, "类型"));
                            fenLeiSonBean.setFenLeiSon_lingshoujia(JSONUtlis.getString(jsonObject, "建议零售价"));

                            shopShowData.add(fenLeiSonBean);
                        }
                    }

                    if (isRefresh) {
                        L.e("-shopAdapter-", "notifyDataSetChanged-------preEndIndex ==" + preEndIndex + "" + tiaoshu);
                        shopAdapter.notifyDataSetChanged();
                    } else {
                        L.e("-shopAdapter-", "notifyItemRangeInserted---------preEndIndex ==" + preEndIndex + "" + tiaoshu);
                        shopAdapter.notifyItemRangeInserted(preEndIndex + 1, tiaoshu);
                    }
                }

                if (!isRefresh) {
                    if (tiaoshu == 10) {
                        shopAdapter.loadMoreComplete(); // 本次加载完成
                    }
                }
                if (tiaoshu != 10) {
                    shopAdapter.loadMoreEnd(); // 没有更多
                }
                mListener.setRefreshLayoutTrue(); // 加载完成后，开启刷新
                break;
        }

    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_SHOUYE:
                mListener.setRefreshComplete(); // 刷新完成

                // 当首页已经加载过后，重新刷新时网络不好时，错误页面的处理逻辑
                if (isFirstRequest) {
                    emptyFragmentShop.showError();
                }
                break;
            case RequestAction.TAG_SHOP_C_RECOMMEND:
                // 特殊处理，当前逻辑是单独加载才会出现加载状态
                if (!isRefresh) {
                    shopAdapter.loadMoreFail(); // 本次加载失败
                }
                mListener.setRefreshLayoutTrue(); // 加载完失败，开启刷新
                break;
        }
    }

    private class ShopAdapter extends BaseQuickAdapter<FenLeiSonBean, BaseViewHolder> {

        ShopAdapter() {
            super(R.layout.item_shouye_tuijian, shopShowData);
        }

        @Override
        protected void convert(BaseViewHolder helper, FenLeiSonBean shopShowBean) {

            TextView textView = helper.getView(R.id.tv_item_shouye_title);
            CommonUtlis.setImageTitle(mContext, shopShowBean.getFenLeiSon_leixin(), shopShowBean.getFenLeiSon_title(), textView);

            helper.setText(R.id.tv_item_shouye_jiage, shopShowBean.getFenLeiSon_jiage())
                    .setText(R.id.tv_item_shouye_dizhi, shopShowBean.getFenLeiSon_dizhi());
            Glide.with(mContext).load(shopShowBean.getFenLeiSon_img_url()).apply(options).into((ImageView) helper.getView(R.id.iv_item_shouye_tupian));
        }
    }

    /**
     * 设置头部轮播图View数据
     */
    private void setHeaderViewBanner() {
        final List<ShopShowHeaderBean.PagesBean> bannerList = headerBean.getPages();
        L.e("--setHeaderViewBanner--", "size ===" + bannerList.size());

        if (bannerList.size() > 0) {
            banner.setVisibility(View.VISIBLE);

            ArrayList<String> imgList = new ArrayList<>();
            for (int i = 0; i < bannerList.size(); i++) {
                imgList.add(bannerList.get(i).get图片());
            }
            //初始化顶部轮播
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置轮播样式
            banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置
            banner.setDelayTime(4000);//设置间隔时间
            banner.setImages(imgList);//设置图片
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    L.e("--ShopFragment--banner", "position ===" + position);
                    if (!"".equals(bannerList.get(position).get商品id()) && !"0".equals(bannerList.get(position).get商品id())) {
                        startActivity(new Intent(mContext, ShangPinXiangQingActivity.class).putExtra("id", bannerList.get(position).get商品id()));
                    } else if (!"".equals(bannerList.get(position).get图片链接())) {
                        //判断URL是否合法
                        if (Patterns.WEB_URL.matcher(bannerList.get(position).get图片链接()).matches()) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", bannerList.get(position).get图片链接());
                            startActivity(intent);
                        }
                    }
                }
            });
            banner.setImageLoader(new GlideImageLoader()).start();
        } else {
            banner.setVisibility(View.GONE);
        }

        L.e("--setHeaderViewBanner--", "isFirstRequest ===" + isFirstRequest);
        if (isFirstRequest) {
            shopAdapter.addHeaderView(bannerView);
        }
    }

    /**
     * 设置头部分类View数据
     */
    private void setHeaderViewFenLei() {
        //分类背景
        if (!TextUtils.isEmpty(headerBean.get首页图片())) {
            Glide.with(mContext).load(headerBean.get首页图片()).into(ivFenLeiShopView);
        }
        //分类字体颜色
        String fenLeiTextColor = headerBean.get字体颜色();

        try {
            if (fenLeiTextColor.length() > 0) {
                String[] s = fenLeiTextColor.split("\\,");
                for (int i = 0; i < s.length; i++) {
                    red = Integer.parseInt(s[0]);
                    green = Integer.parseInt(s[1]);
                    blue = Integer.parseInt(s[2]);
                }
            }
        } catch (Exception e) {
            red = 102;
            green = 102;
            blue = 102;
        }

        fenLeiAdapter.notifyDataSetChanged();
        if (isFirstRequest) {
            shopAdapter.addHeaderView(fenLeiView);
        }
    }

    /**
     * 设置头部MiddleView数据
     */
    private void setHeaderViewMiddle() {

        if(!TextUtils.isEmpty(headerBean.get活动专区图片链接())){ // 判断点击图片跳转链接是否为空
            ivHuoDong.setVisibility(View.VISIBLE);
            //活动专区图片
            Glide.with(mContext).load(headerBean.get活动专区()).into(ivHuoDong);

            ivHuoDong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,WebActivity.class);
                    intent.putExtra("url", headerBean.get活动专区图片链接());
                    startActivity(intent);
                }
            });
        }else {
            ivHuoDong.setVisibility(View.GONE);
        }

        // 全球购背景
        Glide.with(mContext).load(headerBean.get全球购()).into(ivQuanqiu);

        rlQuanQiuGou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 全球购商品
                Intent intent = new Intent(getActivity(), JinriTejiaActivity.class);
                intent.putExtra("类型", "全球购商品");
                startActivity(intent);
            }
        });

        // 推荐商品背景
        Glide.with(mContext).load(headerBean.get推荐商品图片()).into(ivTuijian);

        jingXuanAdapter.notifyDataSetChanged();
        if (isFirstRequest) {
            shopAdapter.addHeaderView(middleView);

            if (!preferences.getBoolean(ConstantUtlis.IS_SHOW_GUIDE_VIEW, false)) {
                // 指引View
                guideView = new GuideView(mContext);
                guideView.setGuideRemoveListener(new GuideView.OnGuideChangedListener() {
                    @Override
                    public void onShow() { // 表示指引view正在展示中
                        isGuideViewShow = true;
                    }

                    @Override
                    public void onRemove() { // 表示指引view已移除
                        isGuideViewShow = false;
                        // 更改指引图显示状态为true
                        editor.putBoolean(ConstantUtlis.IS_SHOW_GUIDE_VIEW, true).commit();
                        requestMyCoupon();
                    }
                }).show(getActivity());
            }
        }
    }

    // 刷新数据
    @Override
    public void onRefresh() {
        L.e("---", "onRefresh");
        isRefresh = true;
        requestData(true);
    }

    // 加载数据
    @Override
    public void onLoading() {
        L.e("---", "onLoading");
        isRefresh = false;
        requestData(false);
    }

    /**
     * 回到顶部
     */
    public void setTab() {
        if (recyclerViewShop != null) {
            // 因为添加的头部有三个，所以滑动到下标为-3
            recyclerViewShop.smoothScrollToPosition(-3);
        }
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        try {
            // 刷新红点
            hongdian();
        } catch (Exception e) {

        }
    }
    /**
     * 消息红点
     */
    private void hongdian() {
        if (check_login() && dotView != null) {
            int num = IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).GetAllReadNum();
            if (num > 0) {
                dotView.setVisibility(View.VISIBLE);
            } else {
                dotView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 请求我的优惠券
     */
    private void requestMyCoupon() {
        if (!dalibaoFlag) {
            if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
                requestAction.shop_hp_spree(this, true);
            } else {
                requestAction.shop_hp_spree(this, false);
            }
        }
    }

    /**
     * 点击返回按钮直接取消引导View
     */
    public void setRemoveGuideView() {
        guideView.remove();
    }

    @Override
    public void onResume() {
        super.onResume();
        hongdian();

        if (preferences.getBoolean(ConstantUtlis.IS_SHOW_GUIDE_VIEW, false)) {
            requestMyCoupon();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BroadcastManager.getInstance(mContext).destroy("com.gloiot.hygo.判断网络");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        BroadcastManager.getInstance(mContext).destroy("判断网络");
    }

    // 判断地址是否有问号
    private String mUrl(String url) {
        if (url.contains("?")) {
            return url + "&onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "") + "&version=" + ConstantUtlis.VERSION;
        } else {
            return url + "?onlyID=" + preferences.getString(ConstantUtlis.SP_ONLYID, "") + "&version=" + ConstantUtlis.VERSION;
        }
    }

}
