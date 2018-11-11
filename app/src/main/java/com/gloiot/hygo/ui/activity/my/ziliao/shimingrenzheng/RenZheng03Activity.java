package com.gloiot.hygo.ui.activity.my.ziliao.shimingrenzheng;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.ziliao.ZhiLiaoActivity;
import com.gloiot.hygo.utlis.CommonUtlis;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 实名认证第三步
 */
public class RenZheng03Activity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.im_shiming_type)
    ImageView imShimingType;
    @Bind(R.id.tv_shiming_type)
    TextView tvShimingType;

    @Override
    public int initResource() {
        return R.layout.activity_renzheng03;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "完成", "", false);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("已认证")) {
            imShimingType.setImageResource(R.mipmap.ic_shiming_correct);
            tvShimingType.setText("恭喜，您已通过审核");
        } else if (type.equals("未认证")) {
            imShimingType.setImageResource(R.mipmap.ic_shiming_failure);
            tvShimingType.setText("很遗憾，您没有通过审核");
        }
    }

    @OnClick(R.id.btu_success)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_success:
                finish();
                break;
        }
    }

    /**
     * 禁掉返回事件，必须点击完成
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK: {
                return false;
            }
        }
        return true;
    }
}
