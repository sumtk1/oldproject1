package com.gloiot.hygo.ui.activity.gesturelock;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.PictureUtlis;


/**
 * 手势解锁界面
 */
public class Activity_GetGestureLock extends BaseActivity {

    private GetGestureLockView mGetGestureLockView;
    private TextView tv_count, tv_tishi, tv_forgetpaswd;
    private ImageView iv_head;

    @Override
    public int initResource() {
        return R.layout.activity_gesturelock_get;
    }

    @Override
    public void initData() {
        initComponent();
        String my_head = preferences.getString(ConstantUtlis.SP_USERIMG, "");
        L.e("---SP_USERIMG--", my_head);

        if (!TextUtils.isEmpty(my_head)) {
            PictureUtlis.loadCircularImageViewHolder(mContext, my_head, R.mipmap.ic_defaulthead1, iv_head);
        }
        mGetGestureLockView.setOnGestureLockViewListener(new GetGestureLockView.OnGestureLockViewListener() {
            @Override
            public void onGettureLockResult(boolean result) {
                if (result) {
                    finish();
                }
            }

            @Override
            public void onUnmatched(int mTryTimes) {
                tv_count.setText("密码错误,还可以再试" + mTryTimes + "次");
                tv_count.setTextColor(Color.parseColor("#FF0000"));
                tv_tishi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnmatchedExceedBoundary() {//错误5次
                reLogin();
            }
        });
        tv_forgetpaswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reLogin();
            }
        });
    }

    private void initComponent() {
        mGetGestureLockView = (GetGestureLockView) findViewById(R.id.id_getGestureLockView);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_tishi = (TextView) findViewById(R.id.tv_tishi);
        tv_forgetpaswd = (TextView) findViewById(R.id.tv_forgetpaswd);
        iv_head = (ImageView) findViewById(R.id.iv_head);
    }

    //退出登陆
    private void reLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        editor.putString(ConstantUtlis.SP_LOGINTYPE, "退出");
        editor.putString(ConstantUtlis.SP_GESTURELOCK_RESULT, "");
        editor.commit();
        CommonUtlis.clearPersonalData();//清除账号个人数据，重置登录状态等
        App.getInstance().mActivityStack.getLastActivity().finish();
        System.exit(0);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
