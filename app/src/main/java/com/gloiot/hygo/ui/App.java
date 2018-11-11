package com.gloiot.hygo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.gloiot.chatsdk.SocketEvent;
import com.gloiot.chatsdk.socket.LinkServer;
import com.gloiot.chatsdk.utlis.ChatNotification;
import com.gloiot.hygo.im.ChatEvent;
import com.gloiot.hygo.server.db.DBHelper;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zyd.wlwsdk.autolayout.config.AutoLayoutConifg;
import com.zyd.wlwsdk.server.network.HttpManager;
import com.zyd.wlwsdk.utils.MToast;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;

import java.util.Map;


/**
 * App
 */
public class App extends MultiDexApplication {

    private static App instance;

    public IWXAPI wxapi;    // 微信api
    public ActivityStack mActivityStack = null; // Activity 栈
    public static Map<String, Long> timeMap;    // 用于存放倒计时时间

    // 分割 Dex 支持
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public synchronized static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);

//        /**
//         * 日记调试打印
//         * 测试 true，正式 false。
//         */
//        L.isDebug = true;
//        /**
//         * Bugly初始化
//         * 测试 true，正式 false。
//         */
//        CrashReport.initCrashReport(getApplicationContext(),ConstantUtlis.BUGLY_KEY, false);

        instance = this;
        mActivityStack = new ActivityStack();   // 初始化Activity 栈
        SharedPreferencesUtils.init(this, ConstantUtlis.MYSP);
        // 网络请求
        HttpManager.init(ConstantUtlis.URL);
        // 自动适配
        AutoLayoutConifg.getInstance().useDeviceSize();
        // 初始化toast
        MToast.init(this);
        // 初始化数据库
        DBHelper.init(this);
        // 连接消息服务器
        LinkServer.init();
        SocketEvent.init(this);
        // 初始化聊天事件
        ChatEvent.init(this);
        // 初始化消息通知
        ChatNotification.init(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chatSelectFlag", 4);
        ChatNotification.getInstance().setIntent(intent);
        // 注册微信api
        regToWx();
    }


    private void regToWx(){
        wxapi = WXAPIFactory.createWXAPI(this, ConstantUtlis.WX_APP_ID);
        wxapi.registerApp(ConstantUtlis.WX_APP_ID);
    }

}
