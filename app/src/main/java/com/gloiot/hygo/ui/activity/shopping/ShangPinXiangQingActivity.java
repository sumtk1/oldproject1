package com.gloiot.hygo.ui.activity.shopping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.chatsdk.utlis.Constant;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.FenLeiSonBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.GouwucheBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.GroupInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.PingLunBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerendingdanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangPinShuXingBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangPingInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.ui.activity.shopping.dianpu.ShangJiaDianPuActivity;
import com.gloiot.hygo.ui.activity.shopping.gouwuche.GouWuCheActivity;
import com.gloiot.hygo.ui.activity.shopping.gouwuche.QQGQueRenDingDanActivity;
import com.gloiot.hygo.ui.activity.shopping.gouwuche.QuerenDingdanActivity;
import com.gloiot.hygo.ui.activity.shopping.xianqing.Attribute;
import com.gloiot.hygo.ui.activity.shopping.xianqing.FlowLayout;
import com.gloiot.hygo.ui.activity.shopping.xianqing.TagAdapter;
import com.gloiot.hygo.ui.activity.shopping.xianqing.TagFlowLayout;
import com.gloiot.hygo.ui.activity.social.ConversationActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.imagepicker.ImagePreviewSeeActivity;
import com.zyd.wlwsdk.widge.StickyScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

public class ShangPinXiangQingActivity extends BaseActivity implements View.OnClickListener, StickyScrollView.OnScrollChangedListener {
    @Bind(R.id.btn_gouwuche_jiaru)
    Button btn_gouwuche_jiaru;
    @Bind(R.id.btn_gouwuche_liji_goumai)
    Button btn_gouwuche_liji_goumai;
    @Bind(R.id.rl_jiaru_goumai)
    RelativeLayout rl_jiaru_goumai;
    @Bind(R.id.banner_detail)
    Banner banner_detail;
    @Bind(R.id.tv_shangpin_dianpyu_mingcheng)
    TextView tv_shangpin_dianpyu_mingcheng;
    @Bind(R.id.img_xin)
    ImageView img_xin;
    @Bind(R.id.tv_jiage)
    TextView tv_jiage;
    @Bind(R.id.tv_shangpin_dianpyu_kuaidi)
    TextView tv_shangpin_dianpyu_kuaidi;
    //    @Bind(R.id.tv_shangpin_dianpyu_yuexiao)
//    TextView tv_shangpin_dianpyu_yuexiao;
    @Bind(R.id.tv_shangpin_dianpyu_dizhi)
    TextView tv_shangpin_dianpyu_dizhi;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.rl_shangpin_xiangqing_xuanze)
    RelativeLayout rl_shangpin_xiangqing_xuanze;
    @Bind(R.id.tv_shangpin_xiangqing_pingjia)
    TextView tv_shangpin_xiangqing_pingjia;
    @Bind(R.id.tv_shangpin_xiangqing_pingjiashu)
    TextView tv_shangpin_xiangqing_pingjiashu;
    @Bind(R.id.rl_shangpin_xiangqing_pingjia_top)
    RelativeLayout rl_shangpin_xiangqing_pingjia_top;
    @Bind(R.id.listview_shangpin_xiangqing_pingjia)
    ListView listview_shangpin_xiangqing_pinglun;
    @Bind(R.id.img_shangpin_xiangqing_dianpuxinxi)
    ImageView img_shangpin_xiangqing_dianpuxinxi;
    @Bind(R.id.tv_shangpin_xiangqing_dianpuxinxi)
    TextView tv_shangpin_xiangqing_dianpuxinxi;
    @Bind(R.id.tv_shangpin_xiangqing_guanzhurenshu)
    TextView tv_shangpin_xiangqing_guanzhurenshu;
    @Bind(R.id.tv_shangpin_xiangqing_shangpinshu)
    TextView tv_shangpin_xiangqing_shangpinshu;
    @Bind(R.id.tv_shangpin_xiangqing_dianpuxiaoshou)
    TextView tv_shangpin_xiangqing_dianpuxiaoshou;
    @Bind(R.id.rl_shangpin_xiangqing_lianximaijia)
    RelativeLayout rl_shangpin_xiangqing_lianximaijia;
    @Bind(R.id.rl_shangpin_xiangqing_jinrudianpu)
    RelativeLayout rl_shangpin_xiangqing_jinrudianpu;
    @Bind(R.id.ll_xin)
    LinearLayout llXin;
    @Bind(R.id.ll_button_all)
    LinearLayout ll_button_all;
    @Bind(R.id.relativeLayout4)
    RelativeLayout relativeLayout4;
    @Bind(R.id.imageView20)
    ImageView imageView20;
    @Bind(R.id.img_shangpin_xiangqing_dianpudengji)
    ImageView img_shangpin_xiangqing_dianpudengji;
    @Bind(R.id.rl_shangpin_xiangqing_pingjia)
    RelativeLayout rlShangpinXiangqingPingjia;
    @Bind(R.id.lv_shangpin_xiangqing_dianpuxinxi)
    RelativeLayout lvShangpinXiangqingDianpuxinxi;
    @Bind(R.id.ll_shangpin_xiangqing_xiangxixinxi)
    LinearLayout llShangpinXiangqingXiangxixinxi;
    @Bind(R.id.img_shangpin_xiangqing_lianximaijia)
    ImageView imgShangpinXiangqingLianximaijia;
    @Bind(R.id.img_shangpin_xiangqing_jinrudianpu)
    ImageView imgShangpinXiangqingJinrudianpu;
    @Bind(R.id.rl_shangpin_xiangqing_anniu)
    LinearLayout rlShangpinXiangqingAnniu;
    @Bind(R.id.rl_shangpin_xiangqing_dianpuxinxi)
    RelativeLayout rlShangpinXiangqingDianpuxinxi;
    @Bind(R.id.toptitle_back)
    ImageView toptitleBack;
    @Bind(R.id.tv_yixiajia)
    TextView tvYixiajia;
    @Bind(R.id.tv_jianyilingshoujia)
    TextView tvJianyilingshoujia;
    @Bind(R.id.wv_shop)
    WebView wv_shop;

    private Animation animation;

    private Intent intent;
    private String id = "";
    private Context mContext;
    private ShangPingInfoBean shangPingInfoBean;   //商品详情实体
    private ArrayList<ShangPinShuXingBean> shangPing_list = new ArrayList<>(); //商品属性
    private ArrayList<String> photoList = new ArrayList<>();  //图片轮播图


    private Set<String> yanse_Set = new HashSet<>();
    private Set<String> chicun_Set = new HashSet<>();
    private ArrayList<String> arr_all_guige;
    private boolean flag = false;
    private boolean goumai_flag = false;
    private int type_flag;
    private DecimalFormat df;
    private boolean onlyGuige;

    private List<PingLunBean> pingjiaList = new ArrayList<>();
    private CommonAdapter<PingLunBean> PinglunAdapter;

    private ArrayList<String> guiGeList = new ArrayList<>();

    private String renshu = "0";
    private boolean xiajia = false;

    private String shangPin_leixin;

    private String touxiang_url = "";


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ShangPingInfoBean info = (ShangPingInfoBean) msg.obj;
            switch (msg.arg1) {
                case 0:
                    requestHandleArrayList.add(requestAction.AddGouWuChe(ShangPinXiangQingActivity.this,
                            id, info.getYanse(), info.getChicun(), info.getGuige(), info.getJiage(), info.getFukuanNum(), info.getKuchun(), info.getKuaidifei()));
                    break;
            }
        }
    };

    @Override
    public int initResource() {
        return R.layout.activity_shangpin_xiangqing;
    }

    @Override
    public void initData() {

        //获取手机型号
        String phone_type = preferences.getString(ConstantUtlis.SP_PHENENAME, "");
        //状态栏
        if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
        } else {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
        }


        initComponent();

        mContext = this;
        CommonUtlis.setTitleBar(this, true, "商品详情", "");
        df = new DecimalFormat("######0.00");
        animation = AnimationUtils.loadAnimation(this, R.anim.gouwuche_xuanze_in);
        intent = this.getIntent();
        id = intent.getStringExtra("id");

        if (intent.getSerializableExtra("info") != null) {
            //上个页面带进来的展示数据，请求成功会被覆盖
            FenLeiSonBean fenLeiSonBean = (FenLeiSonBean) intent.getSerializableExtra("info");

            //判断显示商品类型图标(全球购，自营)
            shangPin_leixin = fenLeiSonBean.getFenLeiSon_leixin();
            setShangPinLeiXin(shangPin_leixin, fenLeiSonBean.getFenLeiSon_title());

            tv_jiage.setText(fenLeiSonBean.getFenLeiSon_jiage());
            tv_shangpin_dianpyu_dizhi.setText(fenLeiSonBean.getFenLeiSon_dizhi());
            if (fenLeiSonBean.getFenLeiSon_leixin().equals("全球购") || fenLeiSonBean.getFenLeiSon_leixin().equals("全球购-自营")) {
                tvJianyilingshoujia.setVisibility(View.GONE);
            } else {
                try {
                    float jiage = Float.parseFloat(fenLeiSonBean.getFenLeiSon_jiage());
                    float lingshoujia;

                    if (fenLeiSonBean.getFenLeiSon_lingshoujia() == null) {
                        lingshoujia = Float.parseFloat(fenLeiSonBean.getFenLeiSon_jiage());
                    } else {
                        lingshoujia = Float.parseFloat(fenLeiSonBean.getFenLeiSon_lingshoujia());
                    }
                    if (jiage != lingshoujia) {
                        tvJianyilingshoujia.setText("￥" + fenLeiSonBean.getFenLeiSon_lingshoujia());
                        tvJianyilingshoujia.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间横线
                        tvJianyilingshoujia.getPaint().setAntiAlias(true);// 抗锯齿
                    } else {
                        tvJianyilingshoujia.setText("");
                    }
                } catch (Exception e) {
                    tvJianyilingshoujia.setText("");
                }
            }

            photoList.add(fenLeiSonBean.getFenLeiSon_jiage());
            initBanner(photoList);
        }

        //根据id去获取商品详情数据
        getData(id);

        //获取店铺等级
        requestHandleArrayList.add(requestAction.shop_sh_honnr(ShangPinXiangQingActivity.this, id, ""));

        WebSettings ws = wv_shop.getSettings();
        ws.setDefaultTextEncodingName("utf-8");
        ws.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        ws.setJavaScriptEnabled(true);  //支持js
        ws.setBlockNetworkImage(false);
        ws.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        ws.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        ws.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        wv_shop.loadUrl(ConstantUtlis.SHOP_URL + id);
        wv_shop.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                return true;
            }
        });

        initListeners();
        stickyScrollView.setOnScrollListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (check_login()) {
            getShouCang();
        }
    }

    public void initComponent() {
        mInflater = LayoutInflater.from(ShangPinXiangQingActivity.this);


        PinglunAdapter = new CommonAdapter<PingLunBean>(this, R.layout.item_list_shangpinxiangqing_pingjia, pingjiaList) {
            @Override
            public void convert(ViewHolder holder, final PingLunBean pingjiaBean) {
                PictureUtlis.loadCircularImageViewHolder(mContext, pingjiaBean.getPinglun_touxiang_url(), R.drawable.default_image, (ImageView) holder.getView(R.id.pinglun_img));
                holder.setText(R.id.pinglun_title, pingjiaBean.getPinglun_Name());
                holder.setText(R.id.pinglun_time, pingjiaBean.getPinglun_time());

                TextView pinglun_neirong = holder.getView(R.id.pinglun_neirong);
                pinglun_neirong.setText(pingjiaBean.getPinglun_Neirong());
                if ("".equals(pingjiaBean.getPinglun_Neirong())) {
                    pinglun_neirong.setVisibility(View.GONE);
                } else {
                    pinglun_neirong.setVisibility(View.VISIBLE);
                }
                ImageView pinglun_img_01 = holder.getView(R.id.pinglun_img_01);
                ImageView pinglun_img_02 = holder.getView(R.id.pinglun_img_02);
                ImageView pinglun_img_03 = holder.getView(R.id.pinglun_img_03);
                ImageView pinglun_img_04 = holder.getView(R.id.pinglun_img_04);

                for (int i = 0; i < (pingjiaBean.getPinglun_list().size() >= 4 ? 4 : pingjiaBean.getPinglun_list().size()); i++) {
                    if (i == 0) {
                        PictureUtlis.loadImageViewHolder(mContext, pingjiaBean.getPinglun_list().get(i), R.drawable.default_image, pinglun_img_01);
                    } else if (i == 1) {
                        PictureUtlis.loadImageViewHolder(mContext, pingjiaBean.getPinglun_list().get(i), R.drawable.default_image, pinglun_img_02);
                    } else if (i == 2) {
                        PictureUtlis.loadImageViewHolder(mContext, pingjiaBean.getPinglun_list().get(i), R.drawable.default_image, pinglun_img_03);
                    } else if (i == 3) {
                        PictureUtlis.loadImageViewHolder(mContext, pingjiaBean.getPinglun_list().get(i), R.drawable.default_image, pinglun_img_04);
                    }
                }
                switch (pingjiaBean.getPinglun_list().size()) {
                    case 1:
                        pinglun_img_01.setVisibility(View.VISIBLE);
                        pinglun_img_02.setVisibility(View.GONE);
                        pinglun_img_03.setVisibility(View.GONE);
                        pinglun_img_04.setVisibility(View.GONE);
                        break;
                    case 2:
                        pinglun_img_01.setVisibility(View.VISIBLE);
                        pinglun_img_02.setVisibility(View.VISIBLE);
                        pinglun_img_03.setVisibility(View.GONE);
                        pinglun_img_04.setVisibility(View.GONE);
                        break;
                    case 3:
                        pinglun_img_01.setVisibility(View.VISIBLE);
                        pinglun_img_02.setVisibility(View.VISIBLE);
                        pinglun_img_03.setVisibility(View.VISIBLE);
                        pinglun_img_04.setVisibility(View.GONE);
                        break;
                    case 4:
                        pinglun_img_01.setVisibility(View.VISIBLE);
                        pinglun_img_02.setVisibility(View.VISIBLE);
                        pinglun_img_03.setVisibility(View.VISIBLE);
                        pinglun_img_04.setVisibility(View.VISIBLE);
                        break;

                    case 0:
                    default:
                        pinglun_img_01.setVisibility(View.GONE);
                        pinglun_img_02.setVisibility(View.GONE);
                        pinglun_img_03.setVisibility(View.GONE);
                        pinglun_img_04.setVisibility(View.GONE);
                        break;
                }
                pinglun_img_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickImageView(0, (ArrayList<String>) pingjiaBean.getPinglun_list());
                    }
                });
                pinglun_img_02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickImageView(1, (ArrayList<String>) pingjiaBean.getPinglun_list());
                    }
                });
                pinglun_img_03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickImageView(2, (ArrayList<String>) pingjiaBean.getPinglun_list());
                    }
                });
                pinglun_img_04.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickImageView(3, (ArrayList<String>) pingjiaBean.getPinglun_list());
                    }
                });

            }
        };
        listview_shangpin_xiangqing_pinglun.setAdapter(PinglunAdapter);
        setListViewHeightBasedOnChildren(listview_shangpin_xiangqing_pinglun);
        PinglunAdapter.notifyDataSetChanged();
    }

    private void initBanner(ArrayList list) {
        //初始化顶部轮播
        banner_detail.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)//设置轮播样式
                .setIndicatorGravity(BannerConfig.CENTER)//设置指示器位置
                .setImages(list)//设置图片
                .isAutoPlay(false)
                .setImageLoader(new GlideImageLoader()).start();
    }

    private void onClickImageView(int position, ArrayList<String> list) {
        if (position < 0 || position > 4)
            position = 0;

        Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
        intentPreview.putExtra("imageList", list);
        intentPreview.putExtra("position", position);
        startActivity(intentPreview);
    }

    //发起聊天
    private void startChat() {
        if (shangPingInfoBean != null) {
            L.e("店铺-", "--" + shangPingInfoBean.getDianpuId() + "--" + shangPingInfoBean.getDianpu());

            Bundle bundle = new Bundle();
            bundle.putString("topType", Constant.CHAT_TOP_TYPE);// 是否有悬浮，""代表没有悬浮，悬浮类别:shangpin = 商品
            bundle.putString("id", shangPingInfoBean.getId());
            bundle.putString("icon", shangPingInfoBean.getImg_URL());
            bundle.putString("title", shangPingInfoBean.getName());
            bundle.putString("money", shangPingInfoBean.getJiage());
            bundle.putBoolean("single", true); // 默认只有一个商品
            bundle.putString("extra", "");

            Intent intent = new Intent(mContext, ConversationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("receiveId", shangPingInfoBean.getDianpuId());
            intent.putExtra("name", shangPingInfoBean.getDianpu());
            intent.putExtra("data", bundle);
            startActivity(intent);
        } else {
            getData(id);
            MyToast.getInstance().showToast(mContext, "网络异常，正在加载数据!");
        }
    }

    @OnClick({R.id.btn_gouwuche_jiaru, R.id.btn_gouwuche_liji_goumai, R.id.rl_shangpin_xiangqing_xuanze,
            R.id.ll_xin, R.id.rl_shangpin_xiangqing_lianximaijia, R.id.rl_shangpin_xiangqing_jinrudianpu,
            R.id.ll_xiaoxi, R.id.ll_store, R.id.rl_shangpin_xiangqing_pingjia_top, R.id.tv_yixiajia, R.id.rl_shangpinxiangqing_gouwuche})
    @Override
    public void onClick(View v) {
        int[] location = new int[2];
        rl_jiaru_goumai.getLocationOnScreen(location);

        //多次点击处理
        if (onMoreClick(v)) return;

        switch (v.getId()) {
            case R.id.btn_gouwuche_jiaru:
                goumai_flag = true;
                ColorAtt.aliasName.clear();
                SizeAtt.aliasName.clear();
                guiGeAtt.aliasName.clear();
                guiGeList.clear();
                ColorAtt.FailureAliasName.clear();
                SizeAtt.FailureAliasName.clear();
                mFailureSkuDate.clear();
                type_flag = 1;
                getShangpingData(id);
                break;
            case R.id.btn_gouwuche_liji_goumai:
                goumai_flag = false;
                ColorAtt.aliasName.clear();
                SizeAtt.aliasName.clear();
                guiGeAtt.aliasName.clear();
                ColorAtt.FailureAliasName.clear();
                SizeAtt.FailureAliasName.clear();
                guiGeList.clear();
                mFailureSkuDate.clear();
                type_flag = 1;
                getShangpingData(id);
                break;
            case R.id.rl_shangpin_xiangqing_xuanze:
                if (!xiajia) {
                    ColorAtt.aliasName.clear();
                    SizeAtt.aliasName.clear();
                    guiGeAtt.aliasName.clear();
                    ColorAtt.FailureAliasName.clear();
                    SizeAtt.FailureAliasName.clear();
                    mFailureSkuDate.clear();
                    guiGeList.clear();
                    type_flag = 2;
                    getShangpingData(id);
                } else {
                    MyToast.getInstance().showToast(mContext, "该商品已下架");
                }
                break;
            case R.id.ll_xin:
                if (check_login_tiaozhuang()) {
                    ConstantUtlis.CHECK_CHANGE_SHOUCANG = true;
                    if (!flag) {
//                        img_xin.setImageResource(R.mipmap.ic_hongxin);
                        shoucan(id);
                        flag = true;
                    } else {
//                        img_xin.setImageResource(R.mipmap.ic_hongxin_no);
                        quXiaoshoucan(id);
                        flag = false;
                    }
                }
                break;

            case R.id.rl_shangpin_xiangqing_lianximaijia:
//                MyToast.getInstance().showToast(this, "消息正在努力抢修中~");
                if (check_login_tiaozhuang())
                    startChat();
                break;
            case R.id.rl_shangpin_xiangqing_jinrudianpu:
                if (check_login_tiaozhuang()) {
                    if (shangPingInfoBean != null) {
                        Intent intent2 = new Intent(mContext, ShangJiaDianPuActivity.class);
                        intent2.putExtra("name", shangPingInfoBean.getDianpu());
                        intent2.putExtra("guangzhuNum", tv_shangpin_xiangqing_guanzhurenshu.getText().toString());
                        intent2.putExtra("imgUrl", touxiang_url);
                        intent2.putExtra("id", id);
                        //如果启动的Activity已存在，则将该Activity之上的Activity全部销毁掉
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent2);
                        startActivityForResult(intent2, 0);
                    }
                }
                break;
            case R.id.ll_xiaoxi:
//                MyToast.getInstance().showToast(this, "消息正在努力抢修中~");
                if (check_login_tiaozhuang())
                    startChat();
                break;

            case R.id.ll_store:
                if (check_login_tiaozhuang()) {
                    if (shangPingInfoBean != null) {
                        Intent intent1 = new Intent(mContext, ShangJiaDianPuActivity.class);
                        intent1.putExtra("name", shangPingInfoBean.getDianpu());
                        intent1.putExtra("guangzhuNum", tv_shangpin_xiangqing_guanzhurenshu.getText().toString());
                        intent1.putExtra("imgUrl", touxiang_url);
                        intent1.putExtra("id", id);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent1, 0);
                    }
                }
                break;

            case R.id.rl_shangpin_xiangqing_pingjia_top:
                if (pingjiaList.size() == 0) {
                    return;
                } else {
                    startActivity(new Intent(ShangPinXiangQingActivity.this, YonghuPingjiaActivity.class).putExtra("id", id));
                }
                break;

            //已下架
            case R.id.tv_yixiajia:

                break;

            //跳转购物车
            case R.id.rl_shangpinxiangqing_gouwuche:
                if (check_login_tiaozhuang()) {
                    intent = new Intent(this, GouWuCheActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    public void shoucan(String id) {
        requestHandleArrayList.add(requestAction.ShouCangShangpin(this, id));
    }

    public void quXiaoshoucan(String id) {
        requestHandleArrayList.add(requestAction.QuXiaoShouCangShangpin(this, id));
    }

    public void getData(String id) {
        requestHandleArrayList.add(requestAction.GetShangpinXiangqing(this, id));
    }

    public void getShangpingData(String id) {
        requestHandleArrayList.add(requestAction.GetShangpingData(this, id));
    }

    public void getShouCang() {
        requestHandleArrayList.add(requestAction.GetShouyeShouCang(this));
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("requestSuccess" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHANGPINGDETAIL:
                if (response.getString("状态").equals("成功")) {
                    shangPingInfoBean = new ShangPingInfoBean();
                    shangPingInfoBean.setFenlei(response.getString("一级分类"));

                    //商品图片List
                    JSONArray photoArray = new JSONArray(response.getString("商品图片"));
                    photoList.clear();
                    for (int i = 0; i < photoArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) photoArray.get(i);
                        photoList.add(jsonObject.getString("图片"));
                    }
                    initBanner(photoList);

                    shangPingInfoBean.setImg_URL(JSONUtlis.getString(response, "缩略图"));
                    shangPingInfoBean.setName(JSONUtlis.getString(response, "商品名称"));
//                    shangPingInfoBean.setJiage(JSONUtlis.getString(response, "结算价"));
                    shangPingInfoBean.setJiage(JSONUtlis.getString(response, "价格"));
                    shangPingInfoBean.setId(JSONUtlis.getString(response, "商品id"));
                    shangPingInfoBean.setDianpu(JSONUtlis.getString(response, "店铺"));
                    shangPingInfoBean.setKuaidifei(JSONUtlis.getString(response, "快递费"));
                    shangPingInfoBean.setKuchun(JSONUtlis.getString(response, "库存"));
                    shangPingInfoBean.setDianpuId(JSONUtlis.getString(response, "userId"));
                    shangPingInfoBean.setDianputubiao(JSONUtlis.getString(response, "店铺图标"));
                    shangPingInfoBean.setDianpumingcheng(JSONUtlis.getString(response, "name"));
                    shangPingInfoBean.setLeixin(JSONUtlis.getString(response, "类型"));
                    shangPingInfoBean.setHuoDongShangPin(JSONUtlis.getString(response, "是否参与活动", "否"));

                    try {
                        if (check_login()) {
                            UserInfo userInfo = new UserInfo(
                                    JSONUtlis.getString(response, "userId"),
                                    JSONUtlis.getString(response, "name"),
                                    JSONUtlis.getString(response, "店铺图标"));

                            UserInfo oldUserInfo = UserInfoCache.getInstance(mContext).getUserInfo("userId", preferences.getString(ConstantUtlis.SP_USERPHONE, ""));

                            if (oldUserInfo != null) {
                                if (!oldUserInfo.getName().equals(JSONUtlis.getString(response, "name")) ||
                                        !oldUserInfo.getUrl().equals(JSONUtlis.getString(response, "店铺图标"))) {

                                    UserInfoCache.getInstance(mContext).putData(JSONUtlis.getString(response, "userId"), userInfo);//存入缓存
                                    IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).insertUserInfo(userInfo);//存入数据库
                                }
                            } else {
                                UserInfoCache.getInstance(mContext).putData(JSONUtlis.getString(response, "userId"), userInfo);//存入缓存
                                IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).insertUserInfo(userInfo);//存入数据库
                            }
                        }
                    } catch (Exception e) {

                    }
//                    tv_shangpin_dianpyu_mingcheng.setText("");
                    setShangPinLeiXin(JSONUtlis.getString(response, "类型"), JSONUtlis.getString(response, "商品名称"));

                    switch (JSONUtlis.getString(response, "类型")) {
                        case "全球购":
                        case "全球购-自营":
                            tvJianyilingshoujia.setVisibility(View.GONE);
                            break;
                        default:
                            try {
                                float jiage = Float.parseFloat(JSONUtlis.getString(response, "价格"));
                                float lingshoujia = Float.parseFloat(JSONUtlis.getString(response, "建议零售价"));

                                if (jiage != lingshoujia) {
                                    tvJianyilingshoujia.setText("￥" + JSONUtlis.getString(response, "建议零售价"));
                                    tvJianyilingshoujia.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间横线
                                    tvJianyilingshoujia.getPaint().setAntiAlias(true);// 抗锯齿
                                } else {
                                    tvJianyilingshoujia.setText("");
                                }
                            } catch (Exception e) {
                                tvJianyilingshoujia.setText("");
                            }
                            break;
                    }


                    tv_jiage.setText(JSONUtlis.getString(response, "价格"));
                    tv_shangpin_dianpyu_kuaidi.setText(JSONUtlis.getString(response, "快递费"));
//                    tv_shangpin_dianpyu_yuexiao.setText(JSONUtlis.getString(response, "月销"));
                    textView3.setText(JSONUtlis.getString(response, "颜色规格"));

                    tv_shangpin_xiangqing_pingjiashu.setText("(" + JSONUtlis.getString(response, "commentSum") + ")");
                    tv_shangpin_xiangqing_dianpuxinxi.setText(JSONUtlis.getString(response, "店铺"));
                    tv_shangpin_xiangqing_guanzhurenshu.setText(JSONUtlis.getString(response, "关注人数"));
                    renshu = JSONUtlis.getString(response, "关注人数");
                    touxiang_url = JSONUtlis.getString(response, "店铺图标");
                    tv_shangpin_xiangqing_shangpinshu.setText(JSONUtlis.getString(response, "商品数"));
                    tv_shangpin_xiangqing_dianpuxiaoshou.setText(JSONUtlis.getString(response, "店铺销售"));
                    PictureUtlis.loadRoundImageViewHolder(mContext, JSONUtlis.getString(response, "店铺图标"), R.drawable.default_image, img_shangpin_xiangqing_dianpuxinxi, 10);
                    tv_shangpin_dianpyu_dizhi.setText(JSONUtlis.getString(response, "地址"));

                    if ("否".equals(JSONUtlis.getString(response, "上架状态"))) {
                        ll_button_all.setVisibility(View.GONE);
                        xiajia = true;
                    }

                    if ("是".equals(JSONUtlis.getString(response, "是否参与活动"))) {
                        btn_gouwuche_jiaru.setEnabled(false);
                        btn_gouwuche_jiaru.setBackgroundResource(R.drawable.btn_style_jiarugouwuche_noenabled);
                    }

                    JSONArray commentArray = response.getJSONArray("comment");
                    PingLunBean pingLunBean;
                    JSONObject commentObj;

                    pingjiaList.clear();
                    setListViewHeightBasedOnChildren(listview_shangpin_xiangqing_pinglun);
                    PinglunAdapter.notifyDataSetChanged();

                    String nicheng;
                    StringBuilder sb;
                    for (int i = 0; i < commentArray.length(); i++) {
                        commentObj = (JSONObject) commentArray.get(i);
                        pingLunBean = new PingLunBean();

                        nicheng = JSONUtlis.getString(commentObj, "昵称");
                        StringBuilder hideStr = new StringBuilder();
                        if (nicheng.length() > 3) {
                            for (int j = 0; j < nicheng.length() - 2; j++) {
                                hideStr.append("*");
                            }
                        } else if (nicheng.length() >= 2) {
                            hideStr.append("*");
                        }
                        sb = new StringBuilder(nicheng);
                        if (nicheng.length() > 2) {
                            sb.replace(1, nicheng.length() - 1, hideStr.toString());
                        } else if (nicheng.length() > 0) {
                            sb.replace(1, 2, hideStr.toString());
                        }

                        pingLunBean.setPinglun_Name(sb.toString());
                        pingLunBean.setPinglun_Neirong(JSONUtlis.getString(commentObj, "评论"));
                        pingLunBean.setPinglun_touxiang_url(JSONUtlis.getString(commentObj, "头像"));
                        pingLunBean.setPinglun_time(JSONUtlis.getString(commentObj, "录入时间"));
                        JSONArray imageArray = commentObj.getJSONArray("图片");
                        JSONObject imgObj;
                        for (int imgLength = 0; imgLength < imageArray.length(); imgLength++) {
                            imgObj = imageArray.getJSONObject(imgLength);
                            pingLunBean.getPinglun_list().add(imgObj.getString("imgUrl"));
                        }
                        pingjiaList.add(pingLunBean);
                    }
                    if (pingjiaList.size() == 0) {
                        tv_shangpin_xiangqing_pingjia.setText("暂无评论");
                        tv_shangpin_xiangqing_pingjiashu.setVisibility(View.GONE);
                        listview_shangpin_xiangqing_pinglun.setVisibility(View.GONE);
                    } else {
                        listview_shangpin_xiangqing_pinglun.setVisibility(View.VISIBLE);
                        tv_shangpin_xiangqing_pingjia.setText("评价");
                        tv_shangpin_xiangqing_pingjiashu.setVisibility(View.VISIBLE);
                    }
                    setListViewHeightBasedOnChildren(listview_shangpin_xiangqing_pinglun);
                    PinglunAdapter.notifyDataSetChanged();
                    stickyScrollView.smoothScrollTo(0, 20);
                }
                break;
            case RequestAction.TAG_SHANGPINGDATA:
                Log.e("商品可选择类型信息", response.toString());
                if (response.getString("状态").equals("成功")) {

                    ColorAtt.aliasName.clear();
                    SizeAtt.aliasName.clear();
                    guiGeAtt.aliasName.clear();
                    ColorAtt.FailureAliasName.clear();
                    SizeAtt.FailureAliasName.clear();
                    guiGeList.clear();
                    mFailureSkuDate.clear();

                    int num = Integer.parseInt(response.getString("条数"));
                    if (num != 0) {
                        arr_all_guige = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("属性");
                        for (int i = 0; i < num; i++) {

                            ShangPinShuXingBean shangPinShuXingBean = new ShangPinShuXingBean();
                            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            shangPinShuXingBean.setYanse(jsonObj.getString("颜色"));
                            shangPinShuXingBean.setChicun(jsonObj.getString("尺寸"));
                            shangPinShuXingBean.setJiage(jsonObj.getString("价格"));
                            shangPinShuXingBean.setKuchun(jsonObj.getString("库存"));
                            shangPinShuXingBean.setGuige(jsonObj.getString("规格"));
                            if (!onlyGuige) {
                                if (!jsonObj.getString("规格").equals("")) {
                                    onlyGuige = true;
                                }
                            }
                            guiGeList.add(jsonObj.getString("规格"));
                            shangPing_list.add(shangPinShuXingBean);
                            if (!yanse_Set.contains(jsonObj.getString("颜色"))) {
                                yanse_Set.add(jsonObj.getString("颜色"));
                            }
                            if (!chicun_Set.contains(jsonObj.getString("尺寸"))) {
                                chicun_Set.add(jsonObj.getString("尺寸"));
                            }
                            arr_all_guige.add(jsonObj.getString("颜色") + ":" + jsonObj.getString("尺寸"));

                        }


                        //2.guigeArrayList对比arr_all_guige的颜色 将对应的尺寸可选设置为true
                        if (!onlyGuige) {
                            ColorAtt.aliasName.addAll(yanse_Set);
                            SizeAtt.aliasName.addAll(chicun_Set);
                        } else {
                            guiGeAtt.aliasName.addAll(guiGeList);
                        }
                        for (String s : ColorAtt.aliasName) {
                            Log.e("颜色属性", s);
                        }
                        for (String s : SizeAtt.aliasName) {
                            Log.e("尺寸属性", s);
                        }

                        if (shangPingInfoBean != null) {
                            CommodityAttribute mCommodityAttribute = new CommodityAttribute(ShangPinXiangQingActivity.this, shangPingInfoBean);
                            mCommodityAttribute.showAtLocation(btn_gouwuche_jiaru, Gravity.BOTTOM, 0, 0);
                        } else {
                            getData(id);
                            MyToast.getInstance().showToast(mContext, "网络异常，正在加载数据!");
                        }
                    }
                } else {
                    Toast.makeText(mContext, "无数据", Toast.LENGTH_SHORT);
                }
                break;

            case RequestAction.TAG_SHOUCANG:
                img_xin.setImageResource(R.mipmap.ic_hongxin);
                ConstantUtlis.CHECK_CHANGE_SHOUCANG = true;
                MyToast.getInstance().showToast(mContext, "收藏" + response.getString("状态"), true);
//                midToast("收藏" + response.getString("状态"), Toast.LENGTH_SHORT, true);
                renshu = Integer.parseInt(renshu) + 1 + "";
                tv_shangpin_xiangqing_guanzhurenshu.setText(renshu);
                break;
            case RequestAction.TAG_QUXIAOSHOUCANG:
                img_xin.setImageResource(R.mipmap.ic_hongxin_no);
                ConstantUtlis.CHECK_CHANGE_SHOUCANG = true;
                MyToast.getInstance().showToast(mContext, "取消收藏" + response.getString("状态"), true);
//                midToast("取消收藏" + response.getString("状态"), Toast.LENGTH_SHORT, true);
                renshu = Integer.parseInt(renshu) - 1 + "";
                tv_shangpin_xiangqing_guanzhurenshu.setText(renshu);
                break;
            case RequestAction.TAG_ADDGOUWUCHE:
                if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
                    MyToast.getInstance().showToast(mContext, "加入购物车成功", true);
                    ConstantUtlis.CHECK_ADD_GOUWUCHE = true;
                } else {
                    MyToast.getInstance().showToast(mContext, response.getString("状态"), true);
                }
                break;

            case RequestAction.TAG_GEISHOUCANG:
                if (response.getString("状态").equals("成功")) {
                    int num = Integer.parseInt(response.getString("条数"));
                    if (num != 0) {
                        JSONArray jsonArray = response.getJSONArray("列表");
                        for (int i = 0; i < num; i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            if (jsonObject.getString("商品id").equals(id)) {
                                img_xin.setImageResource(R.mipmap.ic_hongxin);
                                flag = true;
                            }
                        }
                    }
                }
                break;
            case RequestAction.TAG_SH_HONNR:
                switch (JSONUtlis.getString(response, "等级")) {
                    case "见习商家":
                        img_shangpin_xiangqing_dianpudengji.setBackgroundResource(R.mipmap.jianshi_da);
                        break;
                    case "荣耀商家":
                        img_shangpin_xiangqing_dianpudengji.setBackgroundResource(R.mipmap.rongyao_da);
                        break;
                    case "高贵商家":
                        img_shangpin_xiangqing_dianpudengji.setBackgroundResource(R.mipmap.gaogui_da);
                        break;
                    case "尊贵商家":
                        img_shangpin_xiangqing_dianpudengji.setBackgroundResource(R.mipmap.zungui_da);
                        break;
                    case "至尊商家":
                        img_shangpin_xiangqing_dianpudengji.setBackgroundResource(R.mipmap.zhizun_da);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(desiredWidth, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private List<String> mFailureSkuDate = new ArrayList<>();//无库存或不能选的所有组合sku
    private Set<String> mFailureSkuDate_set;
    private List<String> mAllSkuDate;//所有的组合sku


    private LayoutInflater mInflater;

    private Attribute SizeAtt = new Attribute();
    private Attribute ColorAtt = new Attribute();
    private Attribute guiGeAtt = new Attribute();

    private int DefaultColor;//临时记录的颜色
    private int DefaultSize;//临时记录的大小

    private String ColorStr = "";
    private String SizeStr = "";
    private String guiGeStr = "";
    private String firstSelect = "";


    private String Sku;


    /**
     * 商品属性PopupWind
     */
    public class CommodityAttribute extends PopupWindow implements View.OnClickListener {
        private View CommodityAttributeView;
        private TagFlowLayout mTfSize;
        private TagFlowLayout mTfColor;
        private Button btn_queren, btn_gouwuche_jiaru, btn_gouwuche_liji_goumai;
        private LinearLayout ll_jiaru_goumai;
        private mTagAdapter mSizeAdapter;
        private mTagAdapter mColorAdapter;
        private mTagAdapter mGuiGeAdapter;
        private TextView tv_jiaqian, tv_kucun, tv_xuanze, tv_xiangqing_yanse, tv_xiangqing_mashu, tv_xiangqing_fenlei, tv_num;
        private ImageButton img_btn_jian, img_btn_jia;
        private ImageView img_tupian, iv_pop_delete;
        private TagFlowLayout mTfGuiGe;
        private View llGuiGe;
        private View llColor;
        private View llSize;
        private String mypurchase_num;
        private int card_num;
        private int guiGePosition = -1;

        public CommodityAttribute(Activity context, final ShangPingInfoBean shangPingInfo) {
//            Select(Sku);
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CommodityAttributeView = inflater.inflate(R.layout.shangpin_xiangqing_xuanze, null);
            mTfSize = (TagFlowLayout) CommodityAttributeView.findViewById(R.id.tf_size);
            mTfColor = (TagFlowLayout) CommodityAttributeView.findViewById(R.id.tf_color);
            btn_queren = (Button) CommodityAttributeView.findViewById(R.id.btn_queren);
            tv_jiaqian = (TextView) CommodityAttributeView.findViewById(R.id.tv_jiaqian);
            img_tupian = (ImageView) CommodityAttributeView.findViewById(R.id.img_tupian);
            tv_kucun = (TextView) CommodityAttributeView.findViewById(R.id.tv_kucun);
            tv_xuanze = (TextView) CommodityAttributeView.findViewById(R.id.tv_xuanze);
            iv_pop_delete = (ImageView) CommodityAttributeView.findViewById(R.id.iv_pop_delete);
            tv_xiangqing_yanse = (TextView) CommodityAttributeView.findViewById(R.id.tv_xiangqing_yanse);
            tv_xiangqing_mashu = (TextView) CommodityAttributeView.findViewById(R.id.tv_xiangqing_mashu);
            tv_xiangqing_fenlei = (TextView) CommodityAttributeView.findViewById(R.id.tv_xiangqing_fenlei);
            tv_num = (TextView) CommodityAttributeView.findViewById(R.id.tv_num);
            img_btn_jia = (ImageButton) CommodityAttributeView.findViewById(R.id.img_btn_jia);
            img_btn_jian = (ImageButton) CommodityAttributeView.findViewById(R.id.img_btn_jian);
            ll_jiaru_goumai = (LinearLayout) CommodityAttributeView.findViewById(R.id.ll_jiaru_goumai);
            btn_gouwuche_liji_goumai = (Button) CommodityAttributeView.findViewById(R.id.btn_gouwuche_liji_goumai);
            btn_gouwuche_jiaru = (Button) CommodityAttributeView.findViewById(R.id.btn_gouwuche_jiaru);
            mTfGuiGe = (TagFlowLayout) CommodityAttributeView.findViewById(R.id.tf_guige);
            llGuiGe = CommodityAttributeView.findViewById(R.id.llGuiGe);
            llColor = CommodityAttributeView.findViewById(R.id.llColor);
            llSize = CommodityAttributeView.findViewById(R.id.llSize);

            img_btn_jia.setOnClickListener(this);
            img_btn_jian.setOnClickListener(this);
            btn_queren.setOnClickListener(this);
            btn_gouwuche_liji_goumai.setOnClickListener(this);
            btn_gouwuche_jiaru.setOnClickListener(this);
            iv_pop_delete.setOnClickListener(this);

            //活动商品设置不可加入购物车
            if ("是".equals(shangPingInfo.getHuoDongShangPin())) {
                btn_gouwuche_jiaru.setEnabled(false);
                btn_gouwuche_jiaru.setBackgroundResource(R.drawable.btn_style_jiarugouwuche_noenabled);
            }

            PictureUtlis.loadImageViewHolder(mContext, shangPingInfo.getImg_URL(), R.drawable.default_image, img_tupian);
            tv_jiaqian.setText(shangPingInfo.getJiage());
            tv_kucun.setText(shangPingInfo.getKuchun());
            //如果该商品只有规格，则只显示规格，不显示颜色、尺寸
            if (onlyGuige) {
                guiGeStr = "";
                tv_xiangqing_yanse.setText("规格");
                tv_xiangqing_fenlei.setVisibility(View.GONE);
                llGuiGe.setVisibility(View.VISIBLE);
                mGuiGeAdapter = new mTagAdapter(guiGeAtt);
                mTfGuiGe.setAdapter(mGuiGeAdapter);
                mTfGuiGe.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                    @Override
                    public void onSelected(Set<Integer> selectPosSet) {
                        if (selectPosSet.isEmpty()) {
                            tv_xuanze.setText("请选择");
                            tv_xiangqing_yanse.setText("规格");
                            tv_xiangqing_yanse.setVisibility(View.VISIBLE);
                            tv_xiangqing_fenlei.setVisibility(View.GONE);
                            tv_jiaqian.setText(shangPingInfo.getJiage());
                            tv_kucun.setText(shangPingInfo.getKuchun());
                        }

                    }
                });
                mTfGuiGe.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        if (guiGePosition != position) {
                            tv_xuanze.setText("已选择");
                            tv_xiangqing_yanse.setVisibility(View.GONE);
                            tv_xiangqing_fenlei.setVisibility(View.VISIBLE);
                            tv_jiaqian.setText(shangPing_list.get(position).getJiage().toString());
                            tv_kucun.setText(shangPing_list.get(position).getKuchun().toString());
                            tv_xiangqing_fenlei.setText("\"" + shangPing_list.get(position).getGuige() + "\"");
                            guiGePosition = position;
                            guiGeStr = shangPing_list.get(position).getGuige();
                        } else {
                            guiGePosition = -1;
                            guiGeStr = "";
                            tv_jiaqian.setText(shangPingInfo.getJiage());
                            tv_kucun.setText(shangPingInfo.getKuchun());
                        }
                        return true;
                    }
                });
            } else {
                llColor.setVisibility(View.VISIBLE);
                llSize.setVisibility(View.VISIBLE);
                DefaultColor = -1;
                DefaultSize = -1;
                ColorStr = "";
                SizeStr = "";
                firstSelect = "";
                mColorAdapter = new mTagAdapter(ColorAtt);
                mTfColor.setAdapter(mColorAdapter);
                mSizeAdapter = new mTagAdapter(SizeAtt);
                mTfSize.setAdapter(mSizeAdapter);
            }

            if (type_flag == 1) {
                btn_queren.setVisibility(View.VISIBLE);
                ll_jiaru_goumai.setVisibility(View.GONE);
            } else {
                ll_jiaru_goumai.setVisibility(View.VISIBLE);
                btn_queren.setVisibility(View.GONE);
            }

            //颜色属性标签点击事件
            mTfColor.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                boolean is;

                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    is = false;
                    //从Base类中拿到不可点击的属性名称进行比较
                    List<String> st = ColorAtt.getFailureAliasName();
                    if (st.contains(ColorAtt.getAliasName().get(position))) {
                        is = true;
                    }
                    //如是不可点击就进接return 这样就形成了不可点击的假像，达到了我们的目的
                    if (is) {
                        Log.e("TAG", "mTfColorreturn");
                        return true;
                    }
                    Log.e("TAG", "position = " + position + " DefaultColor = " + DefaultColor);
                    Log.e("SizeStr", "SizeStr = " + SizeStr + " firstSelect = " + firstSelect);
                    if (position == DefaultColor) {
                        DefaultColor = -1;
                        ColorStr = "";
                        tv_xuanze.setText("请选择");
                        tv_xiangqing_yanse.setText("款式");
                        tv_jiaqian.setText(shangPingInfo.getJiage());
                        tv_kucun.setText(shangPingInfo.getKuchun());
                        tv_xiangqing_yanse.setVisibility(View.VISIBLE);
                        tv_xiangqing_fenlei.setVisibility(View.GONE);
                        if (!SizeStr.isEmpty() && firstSelect.equals("Size")) {
                            SizeAtt.FailureAliasName.clear();
                            Log.e("SizeStr", "部位空1");
                        } else if (firstSelect.equals("Color")) {
                            Log.e("SizeStr", "为空");
                            ColorStr = "";
                            SizeStr = "";
                            firstSelect = "";
                            SizeAtt.FailureAliasName.clear();
                            tv_xuanze.setText("请选择");
                            Log.e("mTfColor", "mTfColor3");
                            DefaultSize = -1;
                            Log.e("颜色位置", " 位置为 :" + DefaultSize);
                            TagAdapNotify(mSizeAdapter, DefaultSize);
                        }

                        return true;
                    } else {
                        if (firstSelect.equals("")) {
                            firstSelect = "Color";
                        }
                        DefaultColor = position;
                        ColorStr = ColorAtt.getAliasName().get(position);
                        if (firstSelect.equals("Color")) {
                            SizeAtt.FailureAliasName.clear();
                            HashSet<String> tempSet = new HashSet<>();

                            for (ShangPinShuXingBean info : shangPing_list) {
                                if (info.getYanse().equals(ColorStr)) {
                                    tempSet.add(info.getChicun());
                                    if (info.getKuchun().equals("0")) {
                                        if (!SizeAtt.FailureAliasName.contains(info.getChicun())) {
                                            SizeAtt.FailureAliasName.add(info.getChicun());
                                        }
                                    }
                                }
                            }
                            for (String s : chicun_Set) {
                                if (!tempSet.contains(s)) {
                                    SizeAtt.FailureAliasName.add(s);
                                }
                            }
                            if (DefaultSize != -1 && SizeAtt.FailureAliasName.contains(SizeAtt.aliasName.get(DefaultSize))) {
                                SizeStr = "";
                                DefaultSize = -1;
                            }
                            if (SizeAtt.FailureAliasName.size() == SizeAtt.aliasName.size()) {
                                tv_kucun.setText("0");
                            }

                            TagAdapNotify(mSizeAdapter, DefaultSize);
                        }

                        if (SizeStr.isEmpty()) {
                            tv_xuanze.setText("请选择");
                            tv_xiangqing_yanse.setText("尺寸");
                            tv_jiaqian.setText(shangPingInfo.getJiage());
                            tv_kucun.setText(shangPingInfo.getKuchun());
                            tv_xiangqing_yanse.setVisibility(View.VISIBLE);
                            tv_xiangqing_fenlei.setVisibility(View.GONE);
                        } else {
                            tv_xuanze.setText("已选择");
                            tv_xiangqing_fenlei.setText("\"" + ColorStr + "\" " + "\"" + SizeStr + "\"");
                            tv_xiangqing_fenlei.setVisibility(View.VISIBLE);
                            tv_xiangqing_yanse.setVisibility(View.GONE);
                            for (int i = 0; i < shangPing_list.size(); i++) {
                                if (shangPing_list.get(i).getYanse().equals(ColorStr) && shangPing_list.get(i).getChicun().equals(SizeStr)) {
                                    tv_jiaqian.setText(shangPing_list.get(i).getJiage().toString());
                                    tv_kucun.setText(shangPing_list.get(i).getKuchun().toString());
                                    break;
                                }
                            }
                        }
                        return true;
                    }
                }
            });

            //大小属性标签点击事件
            mTfSize.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                boolean is;

                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {


                    is = false;
                    List<String> st = SizeAtt.getFailureAliasName();
                    if (st.contains(SizeAtt.getAliasName().get(position))) {
                        is = true;
                    }
                    if (is) {
                        return true;
                    }
                    Log.e("TAG", "position = " + position + "DefaultSize = " + DefaultSize);
                    Log.e("ColorStr", "ColorStr = " + ColorStr + " firstSelect = " + firstSelect);
                    if (position == DefaultSize) {
                        DefaultSize = -1;
                        SizeStr = "";
                        tv_xuanze.setText("请选择");
                        tv_xiangqing_yanse.setText("尺寸");
                        tv_jiaqian.setText(shangPingInfo.getJiage());
                        tv_kucun.setText(shangPingInfo.getKuchun());
                        tv_xiangqing_yanse.setVisibility(View.VISIBLE);
                        tv_xiangqing_fenlei.setVisibility(View.GONE);
                        if (!ColorStr.isEmpty() && firstSelect.equals("Size")) {
                            SizeAtt.FailureAliasName.clear();
                            ColorAtt.FailureAliasName.clear();
                            HashSet<String> tempSet = new HashSet<>();
                            for (ShangPinShuXingBean info : shangPing_list) {
                                if (info.getYanse().equals(ColorStr)) {
                                    tempSet.add(info.getChicun());
                                    if (info.getKuchun().equals("0")) {
                                        if (!SizeAtt.FailureAliasName.contains(info.getChicun())) {
                                            SizeAtt.FailureAliasName.add(info.getChicun());
                                        }
                                    }
                                }
                            }
                            for (String s : chicun_Set) {
                                if (!tempSet.contains(s)) {
                                    SizeAtt.FailureAliasName.add(s);
                                }
                            }
                            TagAdapNotify(mColorAdapter, DefaultColor);
                            TagAdapNotify(mSizeAdapter, -1);
                            firstSelect = "Color";
                        } else if (ColorStr.isEmpty() && firstSelect.equals("Size")) {
                            firstSelect = "";
                            DefaultColor = -1;
                            ColorAtt.FailureAliasName.clear();
                            TagAdapNotify(mColorAdapter, -1);
                        }
                        return true;
                    } else {
                        if (firstSelect.equals("")) {
                            firstSelect = "Size";
                        }
                        DefaultSize = position;
                        SizeStr = SizeAtt.getAliasName().get(position);
                        Log.e("选中尺寸", SizeStr);
                        //反选
                        if (firstSelect.equals("Size")) {
                            ColorAtt.FailureAliasName.clear();

                            HashSet<String> tempSet = new HashSet<>();

                            for (ShangPinShuXingBean info : shangPing_list) {
                                if (info.getChicun().equals(SizeStr)) {
                                    tempSet.add(info.getYanse());
                                    if (info.getKuchun().equals("0")) {
                                        if (!ColorAtt.FailureAliasName.contains(info.getYanse())) {
                                            ColorAtt.FailureAliasName.add(info.getYanse());
                                        }
                                    }
                                }
                            }
                            for (String s : yanse_Set) {
                                if (!tempSet.contains(s)) {
                                    ColorAtt.FailureAliasName.add(s);
                                }
                            }

                            if (DefaultColor != -1 && ColorAtt.FailureAliasName.contains(ColorAtt.aliasName.get(DefaultColor))) {
                                ColorStr = "";
                                DefaultColor = -1;
                            }

                            if (ColorAtt.FailureAliasName.size() == ColorAtt.aliasName.size()) {
                                tv_kucun.setText("0");
                            }
                            TagAdapNotify(mColorAdapter, DefaultColor);
                            //反选end
                        }
                        if (ColorStr.isEmpty()) {
                            tv_xuanze.setText("请选择");
                            tv_xiangqing_yanse.setText("款式");
                            tv_jiaqian.setText(shangPingInfo.getJiage());
                            tv_kucun.setText(shangPingInfo.getKuchun());
                            tv_xiangqing_yanse.setVisibility(View.VISIBLE);
                            tv_xiangqing_fenlei.setVisibility(View.GONE);
                        } else {
                            tv_xuanze.setText("已选择");
                            tv_xiangqing_fenlei.setText("\"" + ColorStr + "\" " + "\"" + SizeStr + "\"");
                            tv_xiangqing_fenlei.setVisibility(View.VISIBLE);
                            tv_xiangqing_yanse.setVisibility(View.GONE);
                            for (int i = 0; i < shangPing_list.size(); i++) {
                                if (shangPing_list.get(i).getYanse().equals(ColorStr) && shangPing_list.get(i).getChicun().equals(SizeStr)) {
                                    tv_jiaqian.setText(shangPing_list.get(i).getJiage().toString());
                                    tv_kucun.setText(shangPing_list.get(i).getKuchun().toString());
                                    break;
                                }
                            }
                        }
                        return true;
                    }
                }
            });


            // 设置SelectPicPopupWindow的View
            this.setContentView(CommodityAttributeView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置popupWindow以外可以触摸
            this.setOutsideTouchable(true);
            // 以下两个设置点击空白处时，隐藏掉pop窗口
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(0.35f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());
            // 设置动画--这里按需求设置成系统输入法动画
            this.setAnimationStyle(R.style.AnimBottom);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            CommodityAttributeView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = CommodityAttributeView.findViewById(R.id.pop_layout)
                            .getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            ShangPingInfoBean info = new ShangPingInfoBean();
            info.setYanse(ColorStr);
            info.setKuchun(tv_kucun.getText().toString());
            info.setChicun(SizeStr);
            info.setGuige(guiGeStr);
            info.setJiage(tv_jiaqian.getText().toString());
            info.setKuaidifei(shangPingInfoBean.getKuaidifei());
            info.setFukuanNum(tv_num.getText().toString());
            GouwucheBean gouwucheBean;
            ShangpinBean shangpinBean;
            QuerendingdanBean querendingdanBean;
            List<ShangpinBean> shangpinBeenList;
            List<QuerendingdanBean> querendingdanBeanList;
            Intent intent;
            Bundle bundle;
            double price;
            int xuanze_num = 0;
            int kucun = 0;
            try {
                if (!"".equals(tv_num.getText().toString()) && !"".equals(tv_kucun.getText().toString())) {
                    xuanze_num = Integer.parseInt(tv_num.getText().toString());
                    kucun = Integer.parseInt(tv_kucun.getText().toString());
                }
            } catch (Exception e) {
                Log.e("e", e + "");
            }

            switch (v.getId()) {
                case R.id.img_btn_jian:
                    mypurchase_num = tv_num.getText().toString();
                    card_num = Integer.parseInt(mypurchase_num);
                    if (card_num > 1) {
                        card_num = card_num - 1;
                        tv_num.setText(card_num + "");
                    }
                    break;
                case R.id.img_btn_jia:
                    mypurchase_num = tv_num.getText().toString();
                    card_num = Integer.parseInt(mypurchase_num);
                    card_num = card_num + 1;
                    tv_num.setText(card_num + "");
                    break;

                case R.id.btn_gouwuche_jiaru:
                    if (check_login_tiaozhuang()) {
                        if (onlyGuige) {
                            if (guiGeStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择规格", false);
//                                midToast("请选择规格", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (xuanze_num > kucun) {
                                MyToast.getInstance().showToast(mContext, "购买数量超出库存", false);
//                                midToast("购买数量超出库存", Toast.LENGTH_SHORT, false);
                                return;
                            }
                        } else {
                            if (ColorStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择款式", false);
//                                midToast("请选择款式", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (SizeStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择尺寸", false);
//                                midToast("请选择尺寸", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (tv_xiangqing_mashu.getText().toString().isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择尺寸", false);
//                                midToast("请选择尺寸", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (xuanze_num > kucun) {
                                MyToast.getInstance().showToast(mContext, "购买数量超出库存", false);
//                                midToast("购买数量超出库存", Toast.LENGTH_SHORT, false);
                                return;
                            }
                        }
                        Message msg_02 = new Message();
                        msg_02.arg1 = 0;
                        msg_02.obj = info;
                        handler.sendMessage(msg_02);
                    }
                    break;
                case R.id.btn_gouwuche_liji_goumai:
                    if (check_login_tiaozhuang()) {
                        if (onlyGuige) {
                            if (guiGeStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择规格", false);
//                                midToast("请选择规格", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (xuanze_num > kucun) {
                                MyToast.getInstance().showToast(mContext, "购买数量超出库存", false);
//                                midToast("购买数量超出库存", Toast.LENGTH_SHORT, false);
                                return;
                            }
                        } else {
                            if (ColorStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择款式", false);
//                                midToast("请选择款式", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (SizeStr.isEmpty()) {
                                Log.e("SizeStr", SizeStr);
                                MyToast.getInstance().showToast(mContext, "请选择尺寸", false);
//                                midToast("请选择尺寸", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (xuanze_num > kucun) {
                                MyToast.getInstance().showToast(mContext, "购买数量超出库存", false);
//                                midToast("购买数量超出库存", Toast.LENGTH_SHORT, false);
                                return;
                            }
                        }
                        Log.e("startActivity", "before");

                        gouwucheBean = new GroupInfoBean();
                        gouwucheBean.setChoosed(false);
                        gouwucheBean.setDianpuming(shangPingInfoBean.getDianpu());
                        gouwucheBean.setId("0");
                        shangpinBean = new ShangpinBean("0", shangPingInfoBean.getId(), shangPingInfoBean.getName(),
                                shangPingInfoBean.getFenlei(), Integer.parseInt(tv_num.getText().toString()),
                                Double.parseDouble(tv_jiaqian.getText().toString()), shangPingInfoBean.getImg_URL(),
                                Double.parseDouble(shangPingInfoBean.getKuaidifei()), shangPingInfoBean.getKuchun(), 0, 0, shangPingInfoBean.getLeixin());
                        shangpinBeenList = new ArrayList<>();
                        shangpinBeenList.add(shangpinBean);
                        querendingdanBean = new QuerendingdanBean();
                        querendingdanBean.setGroupInfoBean((GroupInfoBean) gouwucheBean);
                        querendingdanBean.setToBeZhifuDingdan(shangpinBeenList);
                        querendingdanBeanList = new ArrayList<>();
                        querendingdanBeanList.add(querendingdanBean);
                        price = Integer.parseInt(tv_num.getText().toString()) * Double.parseDouble(tv_jiaqian.getText().toString());

                        if (shangPingInfoBean != null) {
                            if ("全球购".equals(shangPingInfoBean.getLeixin()) || "全球购-自营".equals(shangPingInfoBean.getLeixin())) {
                                intent = new Intent(ShangPinXiangQingActivity.this, QQGQueRenDingDanActivity.class);
                                bundle = new Bundle();
                                bundle.putString("类型", "单个");
                                bundle.putString("颜色", ColorStr);
                                bundle.putString("尺寸", SizeStr);
                                bundle.putString("规格", guiGeStr);
                                bundle.putSerializable("querendingdanList", (Serializable) querendingdanBeanList);
                                bundle.putString("totalPrice", String.valueOf(price));
                                intent.putExtras(bundle);
                                startActivity(intent);
                                Log.e("startActivity", "after");
                            } else {
                                intent = new Intent(ShangPinXiangQingActivity.this, QuerenDingdanActivity.class);
                                bundle = new Bundle();
                                bundle.putString("类型", "单个");
                                bundle.putString("颜色", ColorStr);
                                bundle.putString("尺寸", SizeStr);
                                bundle.putString("规格", guiGeStr);
                                bundle.putSerializable("querendingdanList", (Serializable) querendingdanBeanList);
                                bundle.putString("totalPrice", String.valueOf(price));
                                intent.putExtras(bundle);
                                startActivity(intent);
                                Log.e("startActivity", "after");
                            }
                        } else {
                            getData(id);
                            MyToast.getInstance().showToast(mContext, "网络异常，正在加载数据!");
                        }
                    }
                    break;
                case R.id.btn_queren:
                    Log.e("onClick", "onClick: " + ColorStr + SizeStr);
                    if (check_login_tiaozhuang()) {

                        if (onlyGuige) {
                            if (guiGeStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择规格", false);
//                                midToast("请选择规格", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (xuanze_num > kucun) {
                                MyToast.getInstance().showToast(mContext, "购买数量超出库存", false);
//                                midToast("购买数量超出库存", Toast.LENGTH_SHORT, false);
                                return;
                            }
                        } else {
                            if (ColorStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择款式", false);
//                                midToast("请选择款式", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (SizeStr.isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择尺寸", false);
//                                midToast("请选择尺寸", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (tv_xiangqing_mashu.getText().toString().isEmpty()) {
                                MyToast.getInstance().showToast(mContext, "请选择尺寸", false);
//                                midToast("请选择尺寸", Toast.LENGTH_SHORT, false);
                                return;
                            } else if (xuanze_num > kucun) {
                                MyToast.getInstance().showToast(mContext, "购买数量超出库存", false);
//                                midToast("购买数量超出库存", Toast.LENGTH_SHORT, false);
                                return;
                            }
                        }
                        Sku = ColorStr + ":" + SizeStr;
//                    Size = DefaultSize;
//                    Color = DefaultColor;

                        if (goumai_flag) {
                            Message msg = new Message();
                            msg.arg1 = 0;
                            msg.obj = info;
                            handler.sendMessage(msg);
                        } else {
                            gouwucheBean = new GroupInfoBean();
                            gouwucheBean.setChoosed(false);
                            gouwucheBean.setDianpuming(shangPingInfoBean.getDianpu());
                            gouwucheBean.setId("0");
                            shangpinBean = new ShangpinBean("0", shangPingInfoBean.getId(), shangPingInfoBean.getName(),
                                    shangPingInfoBean.getFenlei(), Integer.parseInt(tv_num.getText().toString()),
                                    Double.parseDouble(tv_jiaqian.getText().toString()), shangPingInfoBean.getImg_URL(),
                                    Double.parseDouble(shangPingInfoBean.getKuaidifei()), shangPingInfoBean.getKuchun(), 0, 0, shangPingInfoBean.getLeixin());
                            shangpinBeenList = new ArrayList<>();
                            shangpinBeenList.add(shangpinBean);
                            querendingdanBean = new QuerendingdanBean();
                            querendingdanBean.setGroupInfoBean((GroupInfoBean) gouwucheBean);
                            querendingdanBean.setToBeZhifuDingdan(shangpinBeenList);
                            querendingdanBeanList = new ArrayList<>();
                            querendingdanBeanList.add(querendingdanBean);
//                                price = Double.parseDouble(tv_jiaqian.getText().toString());
                            price = Integer.parseInt(tv_num.getText().toString()) * Double.parseDouble(tv_jiaqian.getText().toString());

                            if (shangPingInfoBean != null) {
                                if ("全球购".equals(shangPingInfoBean.getLeixin()) || "全球购-自营".equals(shangPingInfoBean.getLeixin())) {
                                    intent = new Intent(ShangPinXiangQingActivity.this, QQGQueRenDingDanActivity.class);
                                    bundle = new Bundle();
                                    bundle.putString("类型", "单个");
                                    bundle.putString("颜色", ColorStr);
                                    bundle.putString("尺寸", SizeStr);
                                    bundle.putString("规格", guiGeStr);
                                    bundle.putSerializable("querendingdanList", (Serializable) querendingdanBeanList);
                                    bundle.putString("totalPrice", String.valueOf(price));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(ShangPinXiangQingActivity.this, QuerenDingdanActivity.class);
                                    bundle = new Bundle();
                                    bundle.putString("类型", "单个");
                                    bundle.putString("颜色", ColorStr);
                                    bundle.putString("尺寸", SizeStr);
                                    bundle.putString("规格", guiGeStr);
                                    bundle.putSerializable("querendingdanList", (Serializable) querendingdanBeanList);
                                    bundle.putString("totalPrice", String.valueOf(price));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } else {
                                getData(id);
                                MyToast.getInstance().showToast(mContext, "网络异常，正在加载数据!");
                            }
                        }
                        dismiss();
                    }
                    break;
                case R.id.iv_pop_delete:
                    dismiss();
                    break;
            }

        }

    }

    public void TagAdapNotify(mTagAdapter Adapter, int CcInt) {
        Adapter.getPreCheckedList().clear();
        if (CcInt != -1) {
            Adapter.setSelectedList(CcInt);
        }
        Adapter.notifyDataChanged();
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
        }
    }

    /**
     * 尺寸的流式布局Adatper
     *
     * @author itzwh
     */
    class mTagAdapter extends TagAdapter<String> {

        private TextView tv;

        public mTagAdapter(Attribute ab) {
            super(ab);
        }

        @Override
        public View getView(FlowLayout parent, int position, Attribute t) {
            boolean is = false;
            if (onlyGuige) {
                List<String> guiGeList = t.getAliasName();
                tv = (TextView) mInflater.inflate(R.layout.popupwindow_tv, parent, false);
                tv.setText(guiGeList.get(position));
            } else {
                //两个布局,一个是可点击的布局，一个是不可点击的布局
                List<String> st = t.FailureAliasName;
                if (st != null) {
                    for (int i = 0; i < st.size(); i++) {
                        if (st.get(i).equals(t.aliasName.get(position))) {
                            is = true;
                        }
                    }
                }
                if (!is) {
                    tv = (TextView) mInflater.inflate(R.layout.popupwindow_tv, parent, false);
                    tv.setText(t.aliasName.get(position));
                } else {
                    tv = (TextView) mInflater.inflate(R.layout.popupwindow_tv1, parent, false);
                    tv.setText(t.aliasName.get(position));
                }
            }

            return tv;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Bind(R.id.rl_01)
    RelativeLayout llTitle;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.tabMainContainer)
    LinearLayout frameLayout;
    @Bind(R.id.scrollView)
    StickyScrollView stickyScrollView;
    @Bind(R.id.toptitle_title)
    TextView toptitle_title;
    private int height;

    private void initListeners() {
        //获取内容总高度
        final ViewTreeObserver vto = llContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = llContent.getHeight();
                //注意要移除
                llContent.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);

            }
        });

        //获取Fragment高度
        ViewTreeObserver viewTreeObserver = frameLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = height - frameLayout.getHeight();
                //注意要移除
                frameLayout.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });

        //获取title高度
        ViewTreeObserver viewTreeObserver1 = llTitle.getViewTreeObserver();
        viewTreeObserver1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = height - llTitle.getHeight() - getStatusHeight();//计算滑动的总距离
                stickyScrollView.setStickTop(llTitle.getHeight() + getStatusHeight());//设置距离多少悬浮
                //注意要移除
                llTitle.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });


    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            llTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            toptitle_title.setTextColor(Color.argb((int) 0, 255, 255, 255));
        } else if (t > 0 && t <= height) {
            float scale = (float) t / height;
            int alpha = (int) (255 * scale);
            llTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));//设置标题栏的透明度及颜色
            toptitle_title.setTextColor(Color.argb((int) alpha, 120, 120, 120));
        } else {
            llTitle.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            toptitle_title.setTextColor(Color.argb((int) 255, 120, 120, 120));
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusHeight() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);

    }

    private void setShangPinLeiXin(String shangpin_leixin, String title) {
        CommonUtlis.setImageTitle(mContext, shangpin_leixin, title, tv_shangpin_dianpyu_mingcheng);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    getData(id);
                }
                break;
        }
    }
}
