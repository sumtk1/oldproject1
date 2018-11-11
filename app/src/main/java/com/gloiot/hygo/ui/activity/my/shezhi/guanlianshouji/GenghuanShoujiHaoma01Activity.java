package com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 更换手机号码第一步
 */
public class GenghuanShoujiHaoma01Activity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {

    @Bind(R.id.tv_original_phonenum)
    TextView mTvOriginalPhoneNum;
    @Bind(R.id.btn_next)
    Button mBtnNext;
    private String originalPhoneNum, onlyID;

    @Override
    public int initResource() {
        return R.layout.activity_genghuan_shouji_haoma;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "更换手机号码");
        if (TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_BANGDINGPHONE, ""))) {
            originalPhoneNum = preferences.getString(ConstantUtlis.SP_USERPHONE, "");//为空，取登录手机号
        } else {
            originalPhoneNum = preferences.getString(ConstantUtlis.SP_BANGDINGPHONE, "");
        }
        onlyID = preferences.getString(ConstantUtlis.SP_ONLYID, "");
        mTvOriginalPhoneNum.setText(originalPhoneNum);
        setRequestErrorCallback(this);
    }

    @OnClick({R.id.btn_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (preferences.getString(ConstantUtlis.SP_MYPWD, "").equals("是")) {
                    DialogUtlis.customPwd(getmDialog(), "身份验证", false, new MDialogInterface.PasswordInter() {
                        @Override
                        public void callback(String data) {
                            requestHandleArrayList.add(requestAction.VerifyPaypwd(GenghuanShoujiHaoma01Activity.this, onlyID, MD5Utlis.Md5(data)));
                        }
                    });
                } else {
                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "请设置支付密码保障您的账户安全！", true, "取消", "立即设置",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    Intent intent = new Intent(mContext, WangjimimaActivity.class);
                                    intent.putExtra("forgetpwd", "设置支付密码");
                                    mContext.startActivity(intent);
                                }
                            });
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_VERIFYPAYPWD:
                Intent intent = new Intent(GenghuanShoujiHaoma01Activity.this, GenghuanShoujiHaoma02Activity.class);
                intent.putExtra("originalPhoneNum",originalPhoneNum);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_VERIFYPAYPWD:
                if (response.getString("状态").equals("支付密码输入错误")) {
                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "支付密码输入错误", true, "取消", "重试",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    DialogUtlis.customPwd(getmDialog(), "身份验证", false, new MDialogInterface.PasswordInter() {
                                        @Override
                                        public void callback(String data) {
                                            requestHandleArrayList.add(requestAction.VerifyPaypwd(GenghuanShoujiHaoma01Activity.this, onlyID, MD5Utlis.Md5(data)));
                                        }
                                    });
                                }
                            });
                } else {
                    MyToast.getInstance().showToast(mContext, response.getString("状态"));
                }
                break;
            default:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                break;
        }
    }
}
