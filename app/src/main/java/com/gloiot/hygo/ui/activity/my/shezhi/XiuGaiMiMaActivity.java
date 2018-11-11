package com.gloiot.hygo.ui.activity.my.shezhi;


import android.content.Intent;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.StringUtils;
import com.zyd.wlwsdk.utils.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class XiuGaiMiMaActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.tv_xugai_login)
    TextView tvXugaiLogin;
    @Bind(R.id.v_xingai_login)
    View vXingaiLogin;
    @Bind(R.id.rl_changepaw_login)
    RelativeLayout rlChangepawLogin;
    @Bind(R.id.tv_xiugai_pay)
    TextView tvXiugaiPay;
    @Bind(R.id.v_xiugai_pay)
    View vXiugaiPay;
    @Bind(R.id.rl_changepaw_pay)
    RelativeLayout rlChangepawPay;
    @Bind(R.id.et_changepassword_currentPwd)
    EditText etChangepasswordCurrentPwd;
    @Bind(R.id.et_changepassword_newPwd)
    EditText etChangepasswordNewPwd;
    @Bind(R.id.et_changepassword_confirmPwd)
    EditText etChangepasswordConfirmPwd;
    @Bind(R.id.tv_forgetpwd)
    TextView tvForgetpwd;
    @Bind(R.id.tv_pwd_prompt)
    TextView tvPwdPrompt;
    @Bind(R.id.btn_log_xiugai)
    Button btnLogXiuGai;
    @Bind(R.id.ll_01)
    LinearLayout llMiMaBiaoTi;
    private String category = "登录密码";
    private String isPayPwdSeted;//是否设置过支付密码

    @Override
    public int initResource() {
        return R.layout.activity_xiugaimima;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "密码修改");

        isPayPwdSeted = preferences.getString(ConstantUtlis.SP_MYPWD, "否");
        if (isPayPwdSeted.equals("是")) {
            llMiMaBiaoTi.setVisibility(View.VISIBLE);
        } else {
            llMiMaBiaoTi.setVisibility(View.GONE);
        }

        if (category.equals("登录密码")) {
            tvForgetpwd.setVisibility(View.GONE);
        } else {
            tvForgetpwd.setVisibility(View.VISIBLE);
        }
        setTvContent(category);
    }

    @OnClick({R.id.rl_changepaw_login, R.id.rl_changepaw_pay, R.id.btn_log_xiugai, R.id.tv_forgetpwd})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_changepaw_login:
                tvXiugaiPay.setTextColor(Color.parseColor("#B4B4B4"));
                vXiugaiPay.setVisibility(View.GONE);
                vXingaiLogin.setVisibility(View.VISIBLE);
                tvXugaiLogin.setTextColor(Color.parseColor("#965aff"));
                tvForgetpwd.setVisibility(View.GONE);
                etChangepasswordCurrentPwd.setInputType(InputType.TYPE_CLASS_TEXT);//输入类型
                etChangepasswordCurrentPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)}); //最大输入长度
                etChangepasswordCurrentPwd.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
                etChangepasswordNewPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                etChangepasswordNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                etChangepasswordNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                etChangepasswordConfirmPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                etChangepasswordConfirmPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                etChangepasswordConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                category = "登录密码";
                setTvContent(category);
                break;
            case R.id.rl_changepaw_pay:
                tvXiugaiPay.setTextColor(Color.parseColor("#965aff"));
                vXingaiLogin.setVisibility(View.GONE);
                vXiugaiPay.setVisibility(View.VISIBLE);
                tvForgetpwd.setVisibility(View.VISIBLE);
                etChangepasswordCurrentPwd.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
                etChangepasswordCurrentPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                etChangepasswordCurrentPwd.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
                etChangepasswordNewPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                etChangepasswordNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                etChangepasswordNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                etChangepasswordConfirmPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                etChangepasswordConfirmPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                etChangepasswordConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                tvXugaiLogin.setTextColor(Color.parseColor("#B4B4B4"));
                category = "支付密码";
                setTvContent(category);
                break;
            case R.id.btn_log_xiugai:
                String CurrentPwd = etChangepasswordCurrentPwd.getText().toString();
                String NewPwd = etChangepasswordNewPwd.getText().toString();
                String ConfirmPwd = etChangepasswordConfirmPwd.getText().toString();
                if (category.equals("登录密码")) {
                    if (TextUtils.isEmpty(CurrentPwd)) {
                        MyToast.getInstance().showToast(mContext, "请输入原登录密码");
                    } else if (TextUtils.isEmpty(NewPwd)) {
                        MyToast.getInstance().showToast(mContext, "请输入新登录密码");
                    } else if (NewPwd.length() < 6 || NewPwd.length() > 15) {
                        MyToast.getInstance().showToast(mContext, "请输入有效新密码");
                    } else if (!StringUtils.judgePasswordFormat(NewPwd)){
                        MyToast.getInstance().showToast(mContext, "密码必须字母大小写与数字相结合");
                    }else if (TextUtils.isEmpty(ConfirmPwd)) {
                        MyToast.getInstance().showToast(mContext, "请确认密码");
                    } else if (!NewPwd.equals(ConfirmPwd)) {
                        etChangepasswordConfirmPwd.getText().clear();
                        MyToast.getInstance().showToast(mContext, "您输入的两次密码不一致，请重新输入");
                    } else {
                        requestHandleArrayList.add(requestAction.XiuGaiDengLuMiMa(XiuGaiMiMaActivity.this, CurrentPwd, NewPwd, ConfirmPwd));
                    }
                } else if (category.equals("支付密码")) {
                    if (TextUtils.isEmpty(CurrentPwd)) {
                        MyToast.getInstance().showToast(mContext, "请输入原支付密码");
                    } else if (TextUtils.isEmpty(NewPwd)) {
                        MyToast.getInstance().showToast(mContext, "请输入新支付密码");
                    } else if (NewPwd.length() != 6) {
                        MyToast.getInstance().showToast(mContext, "请输入有效新密码");
                    } else if (TextUtils.isEmpty(ConfirmPwd)) {
                        MyToast.getInstance().showToast(mContext, "请确认密码");
                    } else if (!NewPwd.equals(ConfirmPwd)) {
                        etChangepasswordConfirmPwd.getText().clear();
                        MyToast.getInstance().showToast(mContext, "您输入的两次密码不一致，请重新输入");
                    } else {
                        requestHandleArrayList.add(requestAction.XiuGaiZhiFuMiMa(XiuGaiMiMaActivity.this, CurrentPwd, NewPwd, ConfirmPwd));
                    }
                }
                break;
            case R.id.tv_forgetpwd:
                Intent intent = new Intent(mContext, WangjimimaActivity.class);
                intent.putExtra("forgetpwd", "忘记支付密码");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void setTvContent(String category) {
        etChangepasswordCurrentPwd.setText("");
        etChangepasswordNewPwd.setText("");
        etChangepasswordConfirmPwd.setText("");
        etChangepasswordCurrentPwd.setHint("请输入原" + category);
        if("登录密码".equals(category)){
            etChangepasswordNewPwd.setHint("字母大小写与数字相结合");
        }else {
            etChangepasswordNewPwd.setHint("请输入新" + category);
        }
        tvPwdPrompt.setText("注：" + category + "是您注册时候设置的密码\n若您更改过" + category + "。以您更改的密码为主。");
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_XIUGAIDENGLUMIMA:
                String type = response.getString("状态");
                MyToast.getInstance().showToast(this, type);

                setResult(0x669);
                finish();
                break;
            case RequestAction.TAG_XIUGAIZHIFUMIMA:
                String type1 = response.getString("状态");
                MyToast.getInstance().showToast(this, type1);
                finish();
                break;
            default:
                break;

        }
    }

}
