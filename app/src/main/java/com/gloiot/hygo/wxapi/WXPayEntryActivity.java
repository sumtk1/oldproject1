package com.gloiot.hygo.wxapi;

import android.content.Intent;
import android.util.Log;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zyd.wlwsdk.thirdpay.WXPay;
import com.zyd.wlwsdk.utils.MyToast;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    public int initResource() {
        return R.layout.pay_result;
    }

    @Override
    public void initData() {
        App.getInstance().wxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        App.getInstance().wxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("Wxxxxx", "pay");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (String.valueOf(resp.errCode).equals("0")) {
                WXPay.WXPayCallBack wxPayCallBack = WXPay.getInstance().getWxPayCallBack();
                wxPayCallBack.paySuccess();

                editor.putString(ConstantUtlis.SP_PAYTYPE, "成功");
                editor.commit();
            }
            finish();
        }

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                MyToast.getInstance().showToast(this, "取消!");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                MyToast.getInstance().showToast(this, "被拒绝");
                break;
            default:
                MyToast.getInstance().showToast(this, "失败!" + resp.errCode);
                break;
        }
        finish();
    }
}