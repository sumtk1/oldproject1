package com.gloiot.hygo.ui.activity.my.shouhou;

import android.content.Intent;
import android.view.View;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;

import butterknife.OnClick;


/**
 * Created by dwj on 2017/6/9 .
 */

public class TuikuanOrTuihuo extends BaseActivity implements View.OnClickListener {
    @Override
    public int initResource() {
        return R.layout.activity_tuikuan_or_tuihuo;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "退款");
        if ("买家已付款".equals(getIntent().getStringExtra("订单状态"))){
            findViewById(R.id.rl_tuihuo).setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.rl_tuihuo, R.id.rl_tuikuan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_tuikuan:
                startActivity("退款");
                break;

            case R.id.rl_tuihuo:
                startActivity("退货");
                break;
        }
    }

    private void startActivity(String type) {
        Intent intent = new Intent(TuikuanOrTuihuo.this, TuiKuanActivity.class);
        intent.putExtra("订单id", getIntent().getStringExtra("订单id"));
        intent.putExtra("商品id", getIntent().getStringExtra("商品id"));
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("申请售后", type);
        startActivity(intent);
        finish();
    }
}
