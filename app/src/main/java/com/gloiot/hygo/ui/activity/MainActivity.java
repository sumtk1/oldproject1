package com.gloiot.hygo.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.gloiot.chatsdk.DataBase.DataBaseCallBack;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.chatsdk.socket.SocketServer;
import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.gesturelock.Activity_SetGestureLock;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.ui.fragment.DiscoverFragment;
import com.gloiot.hygo.ui.fragment.LifeFragment;
import com.gloiot.hygo.ui.fragment.MyFragment;
import com.gloiot.hygo.ui.fragment.ShopFragment;
import com.gloiot.hygo.ui.fragment.SocialFragment;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.NetBroadcastReceiver;
import com.gloiot.hygo.utlis.NetEvent;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.StatusBarUtil;
import com.zyd.wlwsdk.widge.NoHorizontalScrolledViewPager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

//import cn.jzvd.JZVideoPlayer;

public class MainActivity extends BaseActivity implements View.OnClickListener, NetEvent {
    @Bind(R.id.id_indicator_one_iv)
    ImageView idIndicatorOneIv;
    @Bind(R.id.id_indicator_one_tv)
    TextView idIndicatorOneTv;
    @Bind(R.id.id_indicator_one)
    LinearLayout idIndicatorOne;
    @Bind(R.id.id_indicator_two_iv)
    ImageView idIndicatorTwoIv;
    @Bind(R.id.id_indicator_two_tv)
    TextView idIndicatorTwoTv;
    @Bind(R.id.id_indicator_two)
    LinearLayout idIndicatorTwo;
    @Bind(R.id.id_indicator_three_iv)
    ImageView idIndicatorThreeIv;
    @Bind(R.id.id_indicator_three_tv)
    TextView idIndicatorThreeTv;
    @Bind(R.id.id_indicator_three)
    LinearLayout idIndicatorThree;
    @Bind(R.id.id_indicator_four_iv)
    ImageView idIndicatorFourIv;
    @Bind(R.id.id_indicator_four_tv)
    TextView idIndicatorFourTv;
    @Bind(R.id.id_indicator_four)
    LinearLayout idIndicatorFour;
    @Bind(R.id.id_indicator_five_iv)
    ImageView idIndicatorFiveIv;
    @Bind(R.id.id_indicator_five_tv)
    TextView idIndicatorFiveTv;
    @Bind(R.id.id_indicator_five)
    LinearLayout idIndicatorFive;
    @Bind(R.id.id_badge)
    RelativeLayout idBadge;

    private NoHorizontalScrolledViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private int selectFlag = 1;//要求选中哪个fragment,默认是1
    public static Activity mainActivity;
    private ShopFragment shoppingFragment;
    //    private DiscoverFragment discoverFragment;
    private DiscoverFragment discoverFragment;
    private LifeFragment lifeFragment;
    private SocialFragment socialFragment;
    private String phone_type = "";


    /**
     * 监控网络的广播
     */
    private NetBroadcastReceiver netBroadcastReceiver;

    private int count = 0;


    @Override
    public int initResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (check_login())
            badge.setBadgeNumber(IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).GetAllReadNum()); // 获取未读条数

        Log.e("main", getIntent().getIntExtra("selectFlag", 1) + "--");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int p = intent.getIntExtra("chatSelectFlag", -1);
        if (p != -1) {
            reSetIcon(p);
            mViewPager.setCurrentItem(p - 1, false);
            //状态栏
            if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
            } else {
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
            }
        }

        if (intent.getStringExtra("type") != null && intent.getStringExtra("type").equals("聊天")) {
            replaceFragment();
        }
    }

    @Override
    public void initData() {

        if (Build.VERSION.SDK_INT >= 21) {
            StatusBarUtil.transparencyBar(this);
        }

        if (check_login())
            IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, ""));

        if (!TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_USERPHONE, "")) &&
                !TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""))) {

            SocketListener.getInstance().staredData(preferences.getString(ConstantUtlis.SP_USERPHONE, ""),
                    preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));

        }
        // 连接
        SocketServer.socketOnConnect();

        mainActivity = this;
        try {
            selectFlag = getIntent().getIntExtra("selectFlag", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setOverflowShowingAlways();
        mViewPager = (NoHorizontalScrolledViewPager) findViewById(R.id.id_viewpager);
        initDatas();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);

        //获取手机型号
        phone_type = preferences.getString(ConstantUtlis.SP_PHENENAME, "");

        switch (selectFlag) {
            case 1:
                reSetIcon(1);
                mViewPager.setCurrentItem(0, false);

                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                    StatusBarUtil.transparencyBar(this);
                }
                break;
            case 2:
                reSetIcon(2);
                mViewPager.setCurrentItem(1, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                }
                break;
            case 3:
                reSetIcon(3);
                mViewPager.setCurrentItem(2, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                }
                break;
            case 4:
                reSetIcon(4);
                mViewPager.setCurrentItem(3, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                }
                break;
            case 5:
                reSetIcon(5);
                mViewPager.setCurrentItem(4, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                    StatusBarUtil.transparencyBar(this);
                }
                break;
        }


        /**
         * 忘记密码重新登陆跳转设置手势密码
         */
        if (preferences.getString(ConstantUtlis.SP_GESTURELOCK_ISOPEN, "").equals("开启")) {
            String state = "";
            try {
                state = getIntent().getStringExtra("跳转");
            } catch (Exception e) {
            }

            if (!"welcome".equals(state)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, Activity_SetGestureLock.class);
                        startActivity(intent);
                    }
                }, 100);
            }
        }

        //注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netBroadcastReceiver = new NetBroadcastReceiver();
        mContext.registerReceiver(netBroadcastReceiver, filter);

        //设置监听
        netBroadcastReceiver.setNetEvent(this);

        BroadcastManager.getInstance(mContext).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                badge.setBadgeNumber(IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).GetAllReadNum());
            }
        });
    }

    private RelativeLayout id_badge;
    private Badge badge;

    private void badge() {
        id_badge = (RelativeLayout) findViewById(R.id.id_badge);
        badge = new QBadgeView(mContext).bindTarget(id_badge);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
//            badge.setGravityOffset(-10, -6, false);
        badge.setBadgeBackgroundColor(Color.parseColor("#FF6D63"));
        badge.setBadgeTextSize(10, true);
        badge.setBadgePadding(6, true);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, final Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {
                    IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).CleanAllReadNum(new DataBaseCallBack() {
                        @Override
                        public void operationState(boolean flag) {
                            badge.setBadgeNumber(0); // 将未读消息的条数置零
                            socialFragment.refresh(); // 刷新聊天界面
                            shoppingFragment.refresh(); // 刷新购物页面消息红点
                        }
                    });
                }
            }
        });

        if (check_login())
            badge.setBadgeNumber(IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).GetAllReadNum());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            //商品订单——回跳
            case 0x666:
                reSetIcon(1);
                mViewPager.setCurrentItem(0, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                    StatusBarUtil.transparencyBar(this);
                }

                break;
            //购物车——回跳
            case 0x667:
                reSetIcon(1);
                mViewPager.setCurrentItem(0, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                    StatusBarUtil.transparencyBar(this);
                }
                break;
            //我的优惠券，立即使用——回跳
            case 0x668:
                reSetIcon(1);
                mViewPager.setCurrentItem(0, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                    StatusBarUtil.transparencyBar(this);
                }
                break;

            //重新设置密码——回调
            case 0x669:
                CommonUtlis.clearPersonalData();
                startActivity(new Intent(mContext, LoginActivity.class));
                ((Activity) mContext).overridePendingTransition(R.anim.activity_open, 0);
                break;

            default:
                break;
        }

    }

    private void initDatas() {
        shoppingFragment = new ShopFragment();
        lifeFragment = new LifeFragment();
        socialFragment = new SocialFragment();
        MyFragment myFragment = new MyFragment();
        discoverFragment = new DiscoverFragment();
        mTabs.add(shoppingFragment);
        mTabs.add(lifeFragment);
        mTabs.add(discoverFragment);
        mTabs.add(socialFragment);
        mTabs.add(myFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };
        badge();
    }


    private void reSetIcon(int i) {
        idIndicatorOneIv.setImageResource(R.mipmap.ic_menu_shopping);
        idIndicatorTwoIv.setImageResource(R.mipmap.ic_menu_life);
        idIndicatorThreeIv.setImageResource(R.mipmap.ic_menu_discover);
        idIndicatorFourIv.setImageResource(R.mipmap.ic_menu_social);
        idIndicatorFiveIv.setImageResource(R.mipmap.ic_menu_my);

        idIndicatorOneTv.setTextColor(getResources().getColor(R.color.cl_999));
        idIndicatorTwoTv.setTextColor(getResources().getColor(R.color.cl_999));
        idIndicatorThreeTv.setTextColor(getResources().getColor(R.color.cl_999));
        idIndicatorFourTv.setTextColor(getResources().getColor(R.color.cl_999));
        idIndicatorFiveTv.setTextColor(getResources().getColor(R.color.cl_999));
        switch (i) {
            case 1:
                idIndicatorOneIv.setImageResource(R.mipmap.icon_menu_shopping);
                idIndicatorOneTv.setTextColor(Color.parseColor("#965aff"));
                JCVideoPlayer.releaseAllVideos();
                break;
            case 2:
                idIndicatorTwoIv.setImageResource(R.mipmap.icon_menu_life);
                idIndicatorTwoTv.setTextColor(Color.parseColor("#965aff"));
                JCVideoPlayer.releaseAllVideos();
                break;
            case 3:
                idIndicatorThreeIv.setImageResource(R.mipmap.icon_menu_discover);
                idIndicatorThreeTv.setTextColor(Color.parseColor("#965aff"));
                break;
            case 4:
                idIndicatorFourIv.setImageResource(R.mipmap.icon_menu_social);
                idIndicatorFourTv.setTextColor(Color.parseColor("#965aff"));
                JCVideoPlayer.releaseAllVideos();
                break;
            case 5:
                idIndicatorFiveIv.setImageResource(R.mipmap.icon_menu_my);
                idIndicatorFiveTv.setTextColor(Color.parseColor("#965aff"));
                JCVideoPlayer.releaseAllVideos();
                break;
        }
    }

    @OnClick({R.id.id_indicator_one, R.id.id_indicator_two, R.id.id_indicator_three, R.id.id_indicator_four, R.id.id_indicator_five})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_indicator_one:
                count++;
                if (count == 2) {
                    count = 0;
                    shoppingFragment.setTab();
                }
                reSetIcon(1);
                mViewPager.setCurrentItem(0, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                    StatusBarUtil.transparencyBar(this);
                }
                break;
            case R.id.id_indicator_two:
                reSetIcon(2);
                mViewPager.setCurrentItem(1, false);
                if (lifeFragment != null) {
                    lifeFragment.refresh();
                }
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                }
                break;
            case R.id.id_indicator_three:
                reSetIcon(3);
                mViewPager.setCurrentItem(2, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                }
                break;
            case R.id.id_indicator_four:
                reSetIcon(4);
                mViewPager.setCurrentItem(3, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                }
                break;
            case R.id.id_indicator_five:
                reSetIcon(5);
                mViewPager.setCurrentItem(4, false);
                //状态栏
                if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
                } else {
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
                    StatusBarUtil.transparencyBar(this);
                }

                break;

        }

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击返回按钮不退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (JCVideoPlayer.backPress()) {
                JCVideoPlayer.releaseAllVideos();
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    if (shoppingFragment.isGuideViewShow) {
                        shoppingFragment.setRemoveGuideView();
                    } else {
                        MyToast.getInstance().showToast(this, "再按一次退出程序");
                        mExitTime = System.currentTimeMillis();
                    }
                } else {
                    App.getInstance().mActivityStack.AppExit();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNetChange(int netMobile) {
        switch (netMobile) {
            case 1://wifi
            case 0://移动数据
            case -1://没有网络
                BroadcastManager.getInstance(this).sendBroadcast("com.gloiot.hygo.判断网络", netMobile + "");
                break;
        }
    }

    //跳转
    public void replaceFragment() {
        reSetIcon(4);
        mViewPager.setCurrentItem(3, false);
        //状态栏
        if (phone_type.contains("vivo") || phone_type.contains("Vivo")) {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"), true);
        } else {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
        }
    }

    public void getBadge() {
        if (check_login())
            badge.setBadgeNumber(IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).GetAllReadNum()); // 获取未读条数
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netBroadcastReceiver != null) {
            mContext.unregisterReceiver(netBroadcastReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}

