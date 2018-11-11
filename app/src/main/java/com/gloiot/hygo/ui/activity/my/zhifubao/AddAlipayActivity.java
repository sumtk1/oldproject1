package com.gloiot.hygo.ui.activity.my.zhifubao;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

public class AddAlipayActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_add_alipay_name)
    TextView tvAddAlipayName;
    @Bind(R.id.et_add_alipay_zhanghao)
    EditText etAddAlipayZhanghao;

    @Override
    public int initResource() {
        return R.layout.activity_add_alipay;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "添加支付宝账户");
        tvAddAlipayName.setText(preferences.getString(ConstantUtlis.SP_TRUENAME, ""));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

        L.e("requestSuccess: " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOP_ALIACC_EDIT:
                MyToast.getInstance().showToast(mContext, "添加支付宝成功");
                finish();
                break;
        }
    }


    @OnClick({R.id.btn_add_alipay_confirm})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_alipay_confirm:
                if (etAddAlipayZhanghao.getText().toString().trim().length() == 0) {
                    MyToast.getInstance().showToast(mContext, "请填写支付宝账号");
                } else {
                    requestHandleArrayList.add(requestAction.shop_aliacc_edit(AddAlipayActivity.this, "add",
                            "", etAddAlipayZhanghao.getText().toString().replaceAll(" ", ""),
                            tvAddAlipayName.getText().toString(), "", preferences.getString(ConstantUtlis.SP_ONLYID, "")));
                }
                break;
        }
    }
}
