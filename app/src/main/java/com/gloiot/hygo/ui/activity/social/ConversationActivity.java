package com.gloiot.hygo.ui.activity.social;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.fragment.ConversationFragment;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.L;

public class ConversationActivity extends BaseActivity {

    private UserInfo userInfo = null; // 传过来的用户信息
    private ConversationFragment conversationFragment;  // 聊天Fragment
    private boolean requestPermissionFalg = false; // 记录语音权限请求结果

    @Override
    public int initResource() {
        return R.layout.activity_conversation;
    }

    @Override
    public void initData() {
//        requestPermission();
        String receiveId = getIntent().getExtras().getString("receiveId", "");
        String name = getIntent().getExtras().getString("name", "");
        CommonUtlis.setTitleBar(this, name);

        userInfo = UserInfoCache.getInstance(mContext).getUserInfo(receiveId, preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
        if (userInfo != null){
            CommonUtlis.setTitleBar(this, TextUtils.isEmpty(userInfo.getName()) ? receiveId : userInfo.getName());
        }

        Bundle bundle = new Bundle();
        bundle.putString("receiveid", receiveId);
        bundle.putString("userid", preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
        bundle.putString("random", preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));
        bundle.putString("nick", preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
        bundle.putBundle("data", getIntent().getExtras().getBundle("data"));

        L.e("12321", "" + getIntent().getExtras().getBundle("data"));

        FragmentTransaction trans1 = getFragmentManager().beginTransaction();
        conversationFragment = new ConversationFragment();
        conversationFragment.setArguments(bundle);
        trans1.replace(R.id.container, conversationFragment);
        trans1.commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        conversationFragment.onActivityResult(requestCode, resultCode, data); // 返回的数据传给Fragment
    }

    /**
     * 请求语音,SD卡权限，点击发送语音的按钮时触发
     * @return   权限请求成功时返回true
     */
    public boolean requestPermission() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                requestPermissionFalg = true;
            }
        }, R.string.perm_recordaudio, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
//        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        return requestPermissionFalg;
    }
}
