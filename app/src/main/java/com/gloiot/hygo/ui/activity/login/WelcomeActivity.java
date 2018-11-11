package com.gloiot.hygo.ui.activity.login;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.JSONUtlis;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.tv_version)
    TextView tvVersion;
    private String phoneId, phoneName, phoneType;
    private int i = 0;

    @Override
    public void onResume() {
        super.onResume();
        i++;
        if (i > 1) {
            setData();
        }
    }

    @Override
    public int initResource() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initData() {
        tvVersion.setText("版本:" + ConstantUtlis.VERSION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        //如果程序在运行，点home键后，点击程序图标，防止再次启动该activity
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        requestAction.getPayWebUrl(this, -1, 3);
    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        super.onSuccess(requestTag, response, showLoad);
        if (requestTag == RequestAction.TAG_PAYWEBURL) {
            editor.putString(ConstantUtlis.WEB_JS, JSONUtlis.getString(response, "内容"));
            editor.putString(ConstantUtlis.WEBPAY_URL, JSONUtlis.getString(response, "webpay_url"));
            editor.putString(ConstantUtlis.WEBCASHIER_URL, JSONUtlis.getString(response, "webcashier_url"));
            editor.commit();
        }
        setData();
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        setData();
    }

    private void setData() {
        if (!preferences.getString(ConstantUtlis.SP_VERSION, "").equals(ConstantUtlis.VERSION) || CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON).size() == 0) {
            phoneName = "Android " + Build.MODEL;
            phoneType = "Android";
            try {
                phoneId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(); // 需要获取权限
            } catch (Exception e) {
                phoneId = "";
            }
            editor.putString(ConstantUtlis.SP_PHENENAME, phoneName);
            editor.putString(ConstantUtlis.SP_VERSION, ConstantUtlis.VERSION);
            editor.commit();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("手机ID", phoneId);
            hashMap.put("手机名称", phoneName);
            hashMap.put("手机型号", phoneType);
            hashMap.put("版本号", ConstantUtlis.VERSION);
            CommonUtlis.saveMap(ConstantUtlis.SP_PHONEINFO_JSON, hashMap);

            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("账号", preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
            hashMap2.put("随机码", preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));
            hashMap2.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
            CommonUtlis.saveMap(ConstantUtlis.SP_REQUESTINFO_JSON, hashMap2);

        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String msg = getIntent().getStringExtra("msg");
                String id = getIntent().getStringExtra("id");
                // 判断是否要显示引导页
                if (preferences.getInt(ConstantUtlis.GUIDE, 0) < ConstantUtlis.GUIDETIME) {
                    goGuide(msg, id);
                } else {
                    if (msg != null && !msg.isEmpty()) {
                        Intent intent = new Intent(WelcomeActivity.this, ShangPinXiangQingActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.putExtra("跳转", "welcome");
                        intent.putExtra("selectFlag", 1);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    }
                }
            }
        }, 2000);
    }

    /**
     * 进入引导页
     */
    private void goGuide(String msg, String id) {
        Intent intent = new Intent();
        intent.setClass(this, GuideActivity.class);
        intent.putExtra("msg", msg);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
