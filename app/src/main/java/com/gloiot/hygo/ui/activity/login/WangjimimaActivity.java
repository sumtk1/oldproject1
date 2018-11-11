package com.gloiot.hygo.ui.activity.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.listwithheard.SelectCountriesActivity;
import com.gloiot.hygo.ui.activity.listwithheard.User;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.gloiot.hygo.utlis.StringUtils;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class WangjimimaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_forgetPwd_phonenum)
    EditText etForgetPwdPhonenum;
    @Bind(R.id.et_forgetpwd_yzm)
    EditText etForgetpwdYzm;
    @Bind(R.id.tv_forgetpwd_yzm)
    TextView tvForgetpwdYzm;
    @Bind(R.id.tv_mimazhanghao)
    TextView tvMiMaZhangHao;
    @Bind(R.id.et_forgetpwd_newpwd)
    EditText etForgetpwdNewpwd;
    @Bind(R.id.et_forgetpwd_confirmpwd)
    EditText etForgetpwdConfirmpwd;
    @Bind(R.id.rl_shezhimima)
    RelativeLayout rlSheZhiMiMa;
    @Bind(R.id.rl_forgetpwd_01)
    RelativeLayout rlForgetPwd;
    @Bind(R.id.rl_forgetpwd_area)
    RelativeLayout rl_forgetpwd_area;
    @Bind(R.id.tv_forgetpwd_area)
    TextView tv_forgetpwd_area;
    @Bind(R.id.tv_overseas_quhao)
    TextView tv_overseas_quhao;

    private String fromFlag;
    private int i = 60;
    private String Str_Guojiama = "86";  //默认中国
    private ArrayList<User> list = null;

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tvForgetpwdYzm.setText(i + "秒后重试");
            } else if (msg.what == -8) {
                tvForgetpwdYzm.setText("获取验证码");
                tvForgetpwdYzm.setClickable(true);
                i = 60;
            }
        }
    };

    @Override
    public int initResource() {
        return R.layout.activity_wangjimima;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        fromFlag = intent.getStringExtra("forgetpwd");
        CommonUtlis.setTitleBar(this, fromFlag);

        if(fromFlag!=null) {
            if (fromFlag.equals("忘记支付密码")) {
                rlSheZhiMiMa.setVisibility(View.VISIBLE);
                rlForgetPwd.setVisibility(View.GONE);
                rl_forgetpwd_area.setVisibility(View.GONE);
                tvMiMaZhangHao.setText(preferences.getString(ConstantUtlis.SP_BANGDINGPHONE, ""));
                etForgetpwdNewpwd.setHint("请设置新支付密码");
                etForgetpwdNewpwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                etForgetpwdNewpwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                etForgetpwdNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
                etForgetpwdConfirmpwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                etForgetpwdConfirmpwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                etForgetpwdConfirmpwd.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            } else if (fromFlag.equals("设置支付密码")) {
                rlSheZhiMiMa.setVisibility(View.VISIBLE);
                rlForgetPwd.setVisibility(View.GONE);
                rl_forgetpwd_area.setVisibility(View.GONE);
                tvMiMaZhangHao.setText(preferences.getString(ConstantUtlis.SP_BANGDINGPHONE, ""));
                etForgetpwdNewpwd.setHint("请设置支付密码（6位数字）");
                etForgetpwdNewpwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                etForgetpwdNewpwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                etForgetpwdNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
                etForgetpwdConfirmpwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                etForgetpwdConfirmpwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                etForgetpwdConfirmpwd.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            } else if (fromFlag.equals("忘记登录密码")) {
                tv_overseas_quhao.setVisibility(View.VISIBLE);
                rlSheZhiMiMa.setVisibility(View.GONE);
                rlForgetPwd.setVisibility(View.VISIBLE);
                if ("86".equals(Str_Guojiama)) {
                    etForgetPwdPhonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                }
            }
        }

        requestHandleArrayList.add(requestAction.p_countryCode_three(this));  //查询所有国家
    }

    @OnClick({R.id.tv_forgetpwd_yzm, R.id.bnt_confirm, R.id.tv_forgetpwd_area})
    @Override
    public void onClick(View v) {
        String phoneNum;
        if (fromFlag.equals("忘记登录密码")) {
            phoneNum = etForgetPwdPhonenum.getText().toString().trim();
        } else {
            phoneNum = tvMiMaZhangHao.getText().toString().trim();
        }
        switch (v.getId()) {
            case R.id.tv_forgetpwd_yzm://获取验证码
                if (TextUtils.isEmpty(phoneNum)) {
                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                } else {
                    if (fromFlag.equals("忘记支付密码")) {
                        requestHandleArrayList.add(requestAction.YZM(WangjimimaActivity.this, phoneNum, "忘记密码验证码"));
                    } else if (fromFlag.equals("设置支付密码")) {
                        requestHandleArrayList.add(requestAction.YZM(WangjimimaActivity.this, phoneNum, "支付验证码"));
                    } else if (fromFlag.equals("忘记登录密码")) {
                        requestHandleArrayList.add(requestAction.YZM(WangjimimaActivity.this, Str_Guojiama + "+" + phoneNum, "忘记密码验证码"));
                    }
                }
                break;
            case R.id.bnt_confirm:
                String newPassWord = etForgetpwdNewpwd.getText().toString().trim();
                String confirmPassWord = etForgetpwdConfirmpwd.getText().toString().trim();
                String YZM = etForgetpwdYzm.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                } else if (TextUtils.isEmpty(YZM)) {
                    MyToast.getInstance().showToast(mContext, "请输入验证码");
                } else if (TextUtils.isEmpty(newPassWord)) {
                    MyToast.getInstance().showToast(mContext, "请输入新密码");
                } else if (fromFlag.equals("忘记登录密码") && (newPassWord.length() < 6 || newPassWord.length() > 15)) {
                    MyToast.getInstance().showToast(mContext, "请设置新密码6-15位");
                } else if ((fromFlag.equals("忘记支付密码") || fromFlag.equals("设置支付密码")) && newPassWord.length() != 6) {
                    MyToast.getInstance().showToast(mContext, "请设置新密码6位");
                } else if (TextUtils.isEmpty(confirmPassWord)) {
                    MyToast.getInstance().showToast(mContext, "请输入确认密码");
                } else if (!newPassWord.equals(confirmPassWord)) {
                    etForgetpwdConfirmpwd.getText().clear();
                    MyToast.getInstance().showToast(mContext, "您输入的两次密码不一致，请重新输入");
                } else {
                    if (fromFlag.equals("忘记支付密码")) {
                        requestHandleArrayList.add(requestAction.ForgetZhiFuPwd(WangjimimaActivity.this, phoneNum, MD5Utlis.Md5(newPassWord), YZM));
                    } else if (fromFlag.equals("忘记登录密码")) {
                        if(!StringUtils.judgePasswordFormat(newPassWord)){
                            MyToast.getInstance().showToast(mContext, "密码必须字母大小写与数字相结合");
                        }else
                        requestHandleArrayList.add(requestAction.ForgetPwd(WangjimimaActivity.this, Str_Guojiama.equals("86") ? phoneNum : Str_Guojiama + "+" + phoneNum, MD5Utlis.Md5(newPassWord), YZM));
                    } else if (fromFlag.equals("设置支付密码")) {
                        requestHandleArrayList.add(requestAction.SetZhiFuPwd(WangjimimaActivity.this, phoneNum, MD5Utlis.Md5(newPassWord), YZM));
                    }
                }
                break;

            //跳转地区
            case R.id.tv_forgetpwd_area:
                if (list != null) {
                    Intent intent1 = new Intent(mContext, SelectCountriesActivity.class);
                    intent1.putExtra("type", "忘记登录密码");
                    intent1.putExtra("list", list);
                    startActivityForResult(intent1, 666);
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_FORGETPWD://忘记登录密码
                MyToast.getInstance().showToast(mContext, "修改成功");
                finish();
                break;
            case RequestAction.TAG_FORGETZHIFUPWD://忘记支付密码
                MyToast.getInstance().showToast(mContext, "修改支付密码成功");
                finish();
                break;
            case RequestAction.TAG_SETZHIFUPWD://设置支付密码
                MyToast.getInstance().showToast(mContext, "设置支付密码成功");
                editor.putString(ConstantUtlis.SP_MYPWD, "是");
                editor.commit();
                finish();
                break;
            case RequestAction.TAG_YZM:
                MyToast.getInstance().showToast(this, "验证码已发送");
                tvForgetpwdYzm.setClickable(false);
                tvForgetpwdYzm.setText(i + "秒后重试");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            SystemClock.sleep(1000);
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();

                break;

            case RequestAction.TAG_P_COUNTRYCODE_THREE:
                list = new ArrayList<>();
                JSONArray jsonArray = response.getJSONArray("国家列表");
                User user;
                for (int i = 0; i < jsonArray.length(); i++) {
                    user = new User(jsonArray.getJSONObject(i).getString("中文名"), jsonArray.getJSONObject(i).getString("国家码"));
                    list.add(user);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x666) {
            Str_Guojiama = data.getStringExtra("国家码");
            tv_forgetpwd_area.setText( data.getStringExtra("国家与地区"));
            tv_overseas_quhao.setText("+" + data.getStringExtra("国家码"));
            etForgetPwdPhonenum.setText("");

            if ("86".equals(Str_Guojiama)) {
                etForgetPwdPhonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            } else {
                etForgetPwdPhonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            }
        }
    }
}
