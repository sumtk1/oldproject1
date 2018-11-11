package com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.SheZhiActivity;
import com.gloiot.hygo.utlis.CommonUtlis;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改成功（更换手机号）
 */
public class XiugaiChenggongActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_finish)
    TextView mTvFinish;

    @Override
    public int initResource() {
        return R.layout.activity_xiugai_chenggong;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "修改成功");
    }

    @OnClick({R.id.tv_finish})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_finish:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        startActivity(new Intent(XiugaiChenggongActivity.this, MainActivity.class));
    }
}
