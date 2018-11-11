package com.gloiot.hygo.ui.activity.my.shezhi;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;

import butterknife.Bind;
import butterknife.OnClick;

public class SheZhiActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.btn_log_out)
    Button btnLogOut;

    @Bind(R.id.rl_gywm)
    RelativeLayout rlGywm;

    @Override
    public int initResource() {
        return R.layout.activity_my_shezhi;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "设置");
    }

    @OnClick({R.id.btn_log_out, R.id.rl_gywm, R.id.btn_log_change})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_gywm:
                if (check_login_tiaozhuang()) {
                    Intent intent1 = new Intent(this, WebActivity.class);
                    intent1.putExtra("url", ConstantUtlis.ABOUT_URL);
                    startActivity(intent1);
                    break;
                }
            case R.id.btn_log_out:
                CommonUtlis.clearPersonalData();//清除个人数据，重置登录状态等

                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                SocketListener.getInstance().signoutRenZheng();
                IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).ClearnData();
                ((Activity) mContext).overridePendingTransition(R.anim.activity_open, 0);
                finish();
                break;
            case R.id.btn_log_change:
                startActivity(new Intent(SheZhiActivity.this, ZhangHaoQieHuanActivity.class));
                finish();
                break;
        }
    }
}
