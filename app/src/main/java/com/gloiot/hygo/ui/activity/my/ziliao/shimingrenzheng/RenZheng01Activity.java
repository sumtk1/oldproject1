package com.gloiot.hygo.ui.activity.my.ziliao.shimingrenzheng;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
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
 * 实名认证第一步
 */
public class RenZheng01Activity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {

    @Bind(R.id.et_real_name)
    EditText mEtRealName;
    @Bind(R.id.et_sfz_num)
    EditText mEtSFZNum;
    @Bind(R.id.btn_next)
    Button btuNext;

    @Override
    public int initResource() {
        return R.layout.activity_renzheng01;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "实名认证");
        setRequestErrorCallback(this);
        if (mEtRealName.getText().toString().equals("") && mEtSFZNum.getText().toString().equals("")) {
            btuNext.setEnabled(false);
            btuNext.setBackgroundResource(R.drawable.bg_no_but);
        }

        final String regx = "[0-9]{17}([0-9]|x|X)";
        mEtRealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable1) {
                if (editable1.toString().equals("") || editable1.toString().length() < 2) {
                    btuNext.setEnabled(false);
                    btuNext.setBackgroundResource(R.drawable.bg_no_but);
                } else {
                    if (!mEtSFZNum.getText().toString().equals("") && mEtSFZNum.getText().toString().matches(regx)) {
                        btuNext.setEnabled(true);
                        btuNext.setBackgroundResource(R.drawable.bg_btn_violet_square);
                    }
                }

            }
        });
        mEtSFZNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("") || !editable.toString().matches(regx)) {
                    btuNext.setEnabled(false);
                    btuNext.setBackgroundResource(R.drawable.bg_no_but);
                } else {
                    if (!mEtRealName.getText().toString().equals("") && mEtRealName.getText().toString().length() >= 2) {
                        btuNext.setEnabled(true);
                        btuNext.setBackgroundResource(R.drawable.bg_btn_violet_square);
                    }
                }
            }
        });


    }

    @OnClick(R.id.btn_next)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (preferences.getString(ConstantUtlis.SP_MYPWD, "否").equals("否")) {
                    CommonUtlis.toSetPwd(mContext, getmDialog());
                } else {
                    final String phoneNum = preferences.getString(ConstantUtlis.SP_USERPHONE, "");
                    final String name = mEtRealName.getText().toString().trim();
                    final String sfz = mEtSFZNum.getText().toString().trim();
                    DialogUtlis.customPwd(getmDialog(), "身份验证", false, new MDialogInterface.PasswordInter() {
                        @Override
                        public void callback(String data) {
                            requestHandleArrayList.add(requestAction.GoRealame(RenZheng01Activity.this, phoneNum, name, sfz, MD5Utlis.Md5(data), preferences.getString(ConstantUtlis.SP_ONLYID, "")));
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
            case RequestAction.TAG_GOREALNAME:
                if (response.getString("实名认证").equals("已认证")) {
                    editor.putString(ConstantUtlis.SP_USERSMRZ, "已认证").commit();
                    editor.putString(ConstantUtlis.SP_TRUENAME,mEtRealName.getText().toString().trim()).commit();
                } else {
                    editor.putString(ConstantUtlis.SP_USERSMRZ, "未认证").commit();
                }
                Intent intent = new Intent(RenZheng01Activity.this, RenZheng03Activity.class);
                intent.putExtra("type", response.getString("实名认证"));
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_GOREALNAME:
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
                                            final String phoneNum = preferences.getString(ConstantUtlis.SP_USERPHONE, "");
                                            final String name = mEtRealName.getText().toString().trim();
                                            final String sfz = mEtSFZNum.getText().toString().trim();
                                            requestHandleArrayList.add(requestAction.GoRealame(RenZheng01Activity.this, phoneNum, name, sfz, MD5Utlis.Md5(data), preferences.getString(ConstantUtlis.SP_ONLYID, "")));

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
