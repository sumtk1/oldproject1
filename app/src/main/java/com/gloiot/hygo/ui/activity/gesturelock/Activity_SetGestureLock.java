package com.gloiot.hygo.ui.activity.gesturelock;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;


import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置手势锁界面
 */
public class Activity_SetGestureLock extends BaseActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SetGestureLockView mGestureLockView;
    private List<Integer> first_int = new ArrayList<Integer>();
    private TextView tv_state;
    private String gesturelock_result = "";

    @Override
    public int initResource() {
        return R.layout.activity_gesturelock_set;
    }

    private void initComponent() {
        mGestureLockView = (SetGestureLockView) findViewById(R.id.id_gestureLockView);
        tv_state = (TextView) findViewById(R.id.tv_state);
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtlis.setTitleBar(this, "设置手势密码");
        preferences = mContext.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
        editor = preferences.edit();

        mGestureLockView.setOnGestureLockViewListener(new SetGestureLockView.OnGestureLockViewListener() {

            @Override
            public void onFirstSelected(List<Integer> firstChoose, boolean firstresult) {
                if (firstresult) {
                    first_int = firstChoose;
                    Log.e("--onFirstSelected--", first_int + "");
                    tv_state.setText("确定手势密码");

                    String lockIndicator = "";
                    for (int i = 0; i < first_int.size(); i++) {
                        lockIndicator = lockIndicator + first_int.get(i);
                    }
                } else {
                    MyToast.getInstance().showToast(mContext, "密码太短，请连接至少3个点");
                }
            }

            @Override
            public void onGestureEvent(boolean result) {
                if (result) {
                    for (int i = 0; i < first_int.size(); i++) {
                        gesturelock_result = gesturelock_result + first_int.get(i);
                    }

                    editor.putString(ConstantUtlis.SP_GESTURELOCK_RESULT, gesturelock_result).commit();
                    MyToast.getInstance().showToast(mContext, "手势密码设置成功");
                    finish();
                } else {
                    MyToast.getInstance().showToast(mContext, "与上一次绘制图案不一致");
                }
            }

        });
    }
}
