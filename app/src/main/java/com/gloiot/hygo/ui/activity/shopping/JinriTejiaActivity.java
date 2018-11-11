package com.gloiot.hygo.ui.activity.shopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.githang.statusbar.StatusBarCompat;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.ui.activity.shopping.fenlei.FenLeiSonAdapter;
import com.gloiot.hygo.utlis.ConstantUtlis;
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


public class JinriTejiaActivity extends BaseActivity implements View.OnClickListener, RLoadListener.RefreshLoadListener {
    //中部切换
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

    //RecyclerView
    @Bind(R.id.rv_jinri_tejia)
    RecyclerView rv_jinri_tejia;

    //错误页面
    @Bind(R.id.emptylayout_jinri_tejia)
    EmptyLayout emptylayout_jinri_tejia;
    //刷新加载
    @Bind(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.iv_toptitle_back)
    ImageView iv_toptitle_back;
    @Bind(R.id.iv_toptitle_title)
    ImageView iv_toptitle_title;
    @Bind(R.id.iv_fenlei_jiemian)
    ImageView iv_fenlei_jiemianl;

    //RecyclerView
    private List<FenLeiSonBean> shangPingInfoList = new ArrayList<>(10);

    private boolean flag = true;
    private FenLeiSonAdapter mAdapter;
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
    private Fenlei_ZonghePop fenlei_zonghePop;

    //类型（今日特价，精选......）
    private String leixing;

    @Override
    public int initResource() {
        return R.layout.activity_jinri_tejia;
    }

    @Override
    public void initData() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 40);

        //获取手机型号
        String phone_type =preferences.getString(ConstantUtlis.SP_PHENENAME,"");
        //状态栏
        if(phone_type.contains("vivo")||phone_type.contains("Vivo")){
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"),true);
        }else {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"),true);
        }

        initComponent();

        mContext = this;
        leixing = getIntent().getStringExtra("类型");

        // 设置标题
        switch (leixing) {
            case "精选":
                iv_toptitle_title.setImageResource(R.mipmap.ic_title_jingxuanshangpin);
                break;
            case "品牌特卖":
                iv_toptitle_title.setImageResource(R.mipmap.ic_title_pinpaitemai);
                break;
            case "新品上市":
                iv_toptitle_title.setImageResource(R.mipmap.ic_title_xinpinshangshi);
                break;
            case "今日特价":
                iv_toptitle_title.setImageResource(R.mipmap.ic_title_jinritejia);
                break;
            case "自营专区":
                iv_toptitle_title.setImageResource(R.mipmap.ic_title_ziyingzhuanqu);
                break;
            case "全球购商品":
                iv_toptitle_title.setImageResource(R.mipmap.ic_title_quanqiugou);
                break;
            default:
                break;
        }

        qingQiuShuJu("综合排序", false);
    }

    public void initComponent() {
        rv_jinri_tejia.setLayoutManager(gridLayoutManager2);
        rv_jinri_tejia.addItemDecoration(spaceItemDecorationGridView2);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rv_jinri_tejia.setHasFixedSize(true);
        mAdapter = new FenLeiSonAdapter(shangPingInfoList);
        rv_jinri_tejia.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(JinriTejiaActivity.this, ShangPinXiangQingActivity.class);
                intent.putExtra("id", shangPingInfoList.get(position).getFenLeiSon_id());
                intent.putExtra("info", shangPingInfoList.get(position));
                startActivity(intent);
            }
        });

        processData();

        //错误页面
        emptylayout_jinri_tejia.setErrorDrawable(R.mipmap.img_error_layout);
        emptylayout_jinri_tejia.setErrorMessage("网络出错了");
        emptylayout_jinri_tejia.setErrorViewButtonId(R.id.buttonError);
        emptylayout_jinri_tejia.setShowErrorButton(true);
        emptylayout_jinri_tejia.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qingQiuShuJu(shaiXuanLeixXing, false);

            }
        });
        //空白页面
        emptylayout_jinri_tejia.setEmptyDrawable(R.mipmap.img_empty_layout);
        emptylayout_jinri_tejia.setEmptyMessage("暂无此商品");
        emptylayout_jinri_tejia.setShowEmptyButton(false);
    }

    /**
     * 刷新加载
     */
    private void processData() {
        // 设置刷新加载
        mListener = new RLoadListener.Builder<FenLeiSonAdapter>()
                .setSwipeRefreshLayout(swipeRefreshLayout)
                .setRecyclerView(rv_jinri_tejia)
                .setAdapter(mAdapter)
                .setRefreshLoadListener(this)
                .create();
    }

    private void qingQiuShuJu(String str, boolean isLoading) {
        if (isLoading) {
            //加载更多数据
            if (shaiXuanLeixXing.equals(str)) {
                return;
            } else {
                emptylayout_jinri_tejia.hide();
                //加载数据
                page = 0;
                num = 0;
                shangPingInfoList.clear();
                mAdapter.notifyDataSetChanged();
                rv_jinri_tejia.scrollToPosition(0);
                if (leixing.equals("全球购商品")) {
                    requestHandleArrayList.add(requestAction.GetQuanQiuGouInfo(this, str, page + ""));
                } else {
                    requestHandleArrayList.add(requestAction.GetJinRiTeJiaInfo(this, str, leixing, page + ""));
                }
            }
        } else {
            emptylayout_jinri_tejia.hide();
            //刷新数据
            if (leixing.equals("全球购商品")) {
                requestHandleArrayList.add(requestAction.GetQuanQiuGouInfo(this, str, page + ""));
            } else {
                requestHandleArrayList.add(requestAction.GetJinRiTeJiaInfo(this, str, leixing, page + ""));
            }
        }
        shaiXuanLeixXing = str;
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        Log.e("requestSuccess" + requestTag, "requestSuccess: " + response);
        switch (requestTag) {
            case RequestAction.TAG_SHOP_HP_CHOICE:
                showShop(response);
                break;
            case RequestAction.TAG_SHOP_QUANQIU:
                showShop(response);
                break;
        }
    }

    /**
     * 网络请求成功显示商品
     *
     * @param response
     * @throws JSONException
     */
    private void showShop(JSONObject response) throws JSONException {
        if (response.getString("状态").equals("成功")) {
            int preEndIndex = shangPingInfoList.size(); // 用于确定加载更多的起始位置

            page = Integer.parseInt(response.getString("页数"));
            num = Integer.parseInt(response.getString("条数"));

            if (num != 0) {
                JSONArray jsonArray = response.getJSONArray("列表");
                FenLeiSonBean shangpinBean;
                for (int i = 0; i < num; i++) {
                    shangpinBean = new FenLeiSonBean();
                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    shangpinBean.setFenLeiSon_id(jsonObj.getString("id"));
                    shangpinBean.setFenLeiSon_title(jsonObj.getString("商品名称"));
                    shangpinBean.setFenLeiSon_img_url(jsonObj.getString("缩略图"));
                    shangpinBean.setFenLeiSon_jiage(jsonObj.getString("价格"));
                    shangpinBean.setFenLeiSon_dizhi(jsonObj.getString("地址"));
                    shangpinBean.setFenLeiSon_leixin(jsonObj.getString("类型"));
                    //现在接口全球购的没有返回建议零售价的
                    if (!"全球购商品".equals(leixing))
                        shangpinBean.setFenLeiSon_lingshoujia(jsonObj.getString("建议零售价"));
                    shangpinBean.setItemType(flag ? FenLeiSonBean.TwoLine : FenLeiSonBean.OneLine);
                    shangPingInfoList.add(shangpinBean);
                }
            }

            /* setNewData的作用在于刷新数据时将“没有更多数据”这个状态清除 */
            Log.e("-page-", "--" + page);
            if (page == 1) {
                mAdapter.setNewData(shangPingInfoList);
                L.e("-shopAdapter-", "notifyDataSetChanged-------preEndIndex ==" + preEndIndex + "" + num);
                mAdapter.notifyDataSetChanged();
            } else {
                L.e("-shopAdapter-", "notifyItemRangeInserted---------preEndIndex ==" + preEndIndex + "" + num);
                mAdapter.notifyItemRangeInserted(preEndIndex + 1, num);
            }
        }

        if (shangPingInfoList.size() == 0) {
            emptylayout_jinri_tejia.showEmpty();
        }

        if (page == 1) { // 刷新
            mListener.setRefreshComplete();
        } else { // 加载
            if (num == 10) {
                mAdapter.loadMoreComplete(); // 本次加载完成
            }
        }
        if (num != 10) {
            mAdapter.loadMoreEnd(); // 没有更多
        }
        mListener.setRefreshLayoutTrue(); // 加载完成后，开启刷新
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        if (page == 0) {
            mListener.setRefreshComplete();
            emptylayout_jinri_tejia.showError();
        } else {
            mAdapter.loadMoreFail(); // 加载失败
            mListener.setRefreshLayoutTrue(); // 加载完成后，开启刷新
        }
    }

    /**
     * 初始化界面
     */
    private void resetView() {
        tv_fenlei_zonghe.setTextColor(getResources().getColor(R.color.cl_333333));
        tv_fenlei_xiaoliang.setTextColor(getResources().getColor(R.color.cl_333333));
        tv_fenlei_jiage.setTextColor(getResources().getColor(R.color.cl_333333));
        iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe0);
        iv_fenlei_jiage.setImageResource(R.mipmap.ic_fenlei_jiage0);
    }

    public void changeView(int i) {
        resetView();
        switch (i) {
            case 1:
                if (zonghe_zhuangtai == 0) {
                    tv_fenlei_zonghe.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tv_fenlei_zonghe.setText("综合");
                    iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                    jiage_zhuangtai = 0;
                    qingQiuShuJu("综合排序", true);
                } else if (zonghe_zhuangtai == 1) {
                    tv_fenlei_zonghe.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tv_fenlei_zonghe.setText("新品");
                    iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                    jiage_zhuangtai = 0;
                    qingQiuShuJu("新品优先", true);
                }
                break;
            case 2:
                tv_fenlei_xiaoliang.setTextColor(getResources().getColor(R.color.cl_E33333));
                jiage_zhuangtai = 0;
                qingQiuShuJu("销量", true);
                break;
            case 3:
                tv_fenlei_jiage.setTextColor(getResources().getColor(R.color.cl_E33333));
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

    @OnClick({R.id.iv_toptitle_back, R.id.ll_fenlei_zonghe, R.id.ll_fenlei_xiaoliang, R.id.ll_fenlei_jiage, R.id.ll_fenlei_qiehuanjiemian})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toptitle_back:
                finish();
                break;
            //中部切换
            case R.id.ll_fenlei_zonghe:
                resetView();
                tv_fenlei_zonghe.setTextColor(getResources().getColor(R.color.cl_E33333));
                iv_fenlei_zonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                if (fenlei_zonghePop == null) {
                    fenlei_zonghePop = new Fenlei_ZonghePop(this);
                }
                fenlei_zonghePop.showAsDropDown(ll_fenlei_zonghe);
                break;
            case R.id.ll_fenlei_xiaoliang:
                changeView(2);
                break;
            case R.id.ll_fenlei_jiage:
                changeView(3);
                break;
            case R.id.ll_fenlei_qiehuanjiemian:
                int canVisPosition;
                if (flag) {
                    canVisPosition = gridLayoutManager2.findFirstVisibleItemPosition();
                    rv_jinri_tejia.setLayoutManager(gridLayoutManager1);
                    rv_jinri_tejia.removeItemDecoration(spaceItemDecorationGridView1);
                    rv_jinri_tejia.removeItemDecoration(spaceItemDecorationGridView2);
                    rv_jinri_tejia.addItemDecoration(spaceItemDecorationGridView1);
                    iv_fenlei_jiemianl.setImageResource(R.mipmap.ic_fenlei_jiemian_1);
                    for (int i = 0; i < shangPingInfoList.size(); i++) {
                        shangPingInfoList.get(i).setItemType(FenLeiSonBean.OneLine);
                    }
                    flag = false;
                } else {
                    canVisPosition = gridLayoutManager1.findFirstVisibleItemPosition();
                    rv_jinri_tejia.setLayoutManager(gridLayoutManager2);
                    rv_jinri_tejia.removeItemDecoration(spaceItemDecorationGridView1);
                    rv_jinri_tejia.removeItemDecoration(spaceItemDecorationGridView2);
                    rv_jinri_tejia.addItemDecoration(spaceItemDecorationGridView2);
                    iv_fenlei_jiemianl.setImageResource(R.mipmap.ic_fenlei_jiemian_2);
                    for (int i = 0; i < shangPingInfoList.size(); i++) {
                        shangPingInfoList.get(i).setItemType(FenLeiSonBean.TwoLine);
                    }
                    flag = true;
                }
                rv_jinri_tejia.scrollToPosition(canVisPosition);

                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        Log.e("mAdapter：", "onRefresh: -----" + shaiXuanLeixXing);
        page = 0;
        shangPingInfoList.clear();
        mAdapter.notifyDataSetChanged();
        qingQiuShuJu(shaiXuanLeixXing, false);
    }

    @Override
    public void onLoading() {
        Log.e("mAdapter：", "onLoading: -----" + shaiXuanLeixXing);
        qingQiuShuJu(shaiXuanLeixXing, false);
    }

    public class Fenlei_ZonghePop extends PopupWindow implements View.OnClickListener {
        private View zonghePopView;
        private RelativeLayout rl_pop_zonghe_paixu, rl_pop_xinpin_youxian;
        private TextView tv_pop_zonghe_paixu, tv_pop_xinpin_youxian;
        private ImageView iv_pop_zonghe_paixu, iv_pop_xinpin_youxian;

        public Fenlei_ZonghePop(Context context) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            zonghePopView = inflater.inflate(R.layout.pop_fenlei_zonghe, null);

            rl_pop_zonghe_paixu = (RelativeLayout) zonghePopView.findViewById(R.id.rl_pop_zonghe_paixu);
            rl_pop_xinpin_youxian = (RelativeLayout) zonghePopView.findViewById(R.id.rl_pop_xinpin_youxian);
            tv_pop_zonghe_paixu = (TextView) zonghePopView.findViewById(R.id.tv_pop_zonghe_paixu);
            tv_pop_xinpin_youxian = (TextView) zonghePopView.findViewById(R.id.tv_pop_xinpin_youxian);
            iv_pop_zonghe_paixu = (ImageView) zonghePopView.findViewById(R.id.iv_pop_zonghe_paixu);
            iv_pop_xinpin_youxian = (ImageView) zonghePopView.findViewById(R.id.iv_pop_xinpin_youxian);
            rl_pop_zonghe_paixu.setOnClickListener(this);
            rl_pop_xinpin_youxian.setOnClickListener(this);

            // 设置SelectPicPopupWindow的View
            this.setContentView(zonghePopView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
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
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_pop_zonghe_paixu:
                    tv_pop_zonghe_paixu.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tv_pop_xinpin_youxian.setTextColor(getResources().getColor(R.color.cl_999999));
                    iv_pop_zonghe_paixu.setImageResource(R.mipmap.ic_xuanze);
                    iv_pop_xinpin_youxian.setImageResource(R.mipmap.ic_xuanze_no);
                    zonghe_zhuangtai = 0;
                    dismiss();
                    break;
                case R.id.rl_pop_xinpin_youxian:
                    tv_pop_xinpin_youxian.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tv_pop_zonghe_paixu.setTextColor(getResources().getColor(R.color.cl_999999));
                    iv_pop_xinpin_youxian.setImageResource(R.mipmap.ic_xuanze);
                    iv_pop_zonghe_paixu.setImageResource(R.mipmap.ic_xuanze_no);
                    zonghe_zhuangtai = 1;
                    dismiss();
                    break;
            }
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
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
            changeView(1);
        }
    }
}

