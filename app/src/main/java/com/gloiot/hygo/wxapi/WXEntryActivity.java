package com.gloiot.hygo.wxapi;


import android.content.Intent;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zyd.wlwsdk.utils.MToast;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{

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
		App.getInstance().wxapi.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		switch(resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				try {
					SendAuth.Resp sendResp = (SendAuth.Resp) resp;
					editor.putString(ConstantUtlis.SP_WXCODE, sendResp.code);
				} catch (Exception e){
					editor.putString(ConstantUtlis.SP_WXCODE, null);
				}
				editor.commit();
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				MToast.showToast("取消!");
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				MToast.showToast("被拒绝");
				break;
			default:
				MToast.showToast("失败!");
				break;
		}
		finish();
	}

}
