package com.gloiot.hygo.im;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.view.View;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.SocketEvent;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendBean;
import com.gloiot.chatsdk.chatui.ui.viewholder.ChatViewHolder;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.chatsdk.utlis.Constant;
import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by hygo01 on 17/5/23.
 */

public class ChatEvent implements ChatUiIM.UserInfoProvider, ChatUiIM.ConversationBehaviorListener, SocketEvent.ConnectionStatusListener, ChatUiIM.SimpleApps {

    private static ChatEvent instance;
    private Context context;
    private Context nowContext;
    private ChatUiIM chatUiIM;
    private SharedPreferences preferences;


    public ChatEvent(Context context) {
        this.context = context;
        chatUiIM = ChatUiIM.getInstance();
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        initListener();
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (ChatEvent.class) {
                if (instance == null) {
                    instance = new ChatEvent(context);
                }
            }
        }
    }

    public static ChatEvent getInstance() {
        return instance;
    }


    private void initListener() {
        chatUiIM.setUserInfoProvider(this);
        chatUiIM.setConversationBehaviorListener(this);
        chatUiIM.setSimpleApps(this);
        SocketEvent.getInstance().setConnectionStatusListener(this);
    }


    @Override
    public UserInfo getUserInfo(String userId) {
        if (!userId.equals(preferences.getString(ConstantUtlis.SP_USERPHONE, ""))) {
            UserInfoManager.getInstance(context).getUserInfo(userId);
            return null;
        } else {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
            userInfo.setId(preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
            userInfo.setUrl(preferences.getString(ConstantUtlis.SP_USERIMG, ""));
            UserInfoCache.getInstance(context).putData(userInfo.getId(), userInfo);//存入缓存
            IMDBManager.getInstance(context, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).insertUserInfo(userInfo);//存入数据库
            return userInfo;
        }
    }

    @Override
    public List<ExtendBean> addApps() {
        return AppsManager.getInstence(context).addApps();
    }

    @Override
    public void appsOnClick(int position) {
        AppsManager.getInstence(context).setAppsOnClick(position);
    }

    @Override
    public boolean onUserPortraitClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        UserInfoManager.getInstance(context).getUserInfo(imMsgBean.getSendid()); // 点击用户头像刷新头像
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public boolean onMessageClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        L.e("-onMessageClick-", "--" + imMsgBean.toString());
        MessageClickListener.MessageClick(context, imMsgBean);
        return false;
    }

    @Override
    public boolean onMessageLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public void onChanged(String result) {
        switch (result) {
            case Constant.RENZHENG_FAILURE:
                // 认证失败
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_FAULT);
                break;
            case Constant.RENZHENG_RANDOM_ERROR:
                // 当被挤下线后，立即关闭socket
                SocketListener.getInstance().signoutRenZheng();
                // 随机码不正确 其他设备登录
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        nowContext = App.getInstance().mActivityStack.getLastActivity();
                        ((Activity)nowContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!"成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))){
                                    return;
                                }
                                CommonUtlis.clearPersonalData();//清除账号个人数据，重置登录状态等
                                DialogUtlis.oneBtnNormal(((BaseActivity)nowContext).getmDialog(), "该账号在其他设备登录\n请重新登录", "确定", false, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((BaseActivity)nowContext).dismissDialog();
                                        Intent intent = new Intent(nowContext, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        nowContext.startActivity(intent);
                                        IMDBManager.getInstance(nowContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).ClearnData();
                                        ((Activity) nowContext).overridePendingTransition(R.anim.activity_open, 0);
                                    }
                                });
                            }
                        });
                    }
                }).start();
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_DONW);
                break;
            case Constant.RENZHENG_SUCCESS:
                // 认证成功
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_SUCCEED);
                break;
            default:
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_FAULT);
                t(result + "");
        }
    }

    public void t(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                MyToast.getInstance().showToast(context, s);
                Looper.loop();
            }
        }).start();
    }
}
