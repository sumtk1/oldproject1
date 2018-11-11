package com.gloiot.hygo.ui.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.listwithheard.SelectCountriesActivity;
import com.gloiot.hygo.ui.activity.listwithheard.User;
import com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji.TianxieYanzhengmaActivity;
import com.gloiot.hygo.ui.activity.web.WebFragment;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.InfoRequestUtils;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

import static com.gloiot.hygo.server.network.RequestAction.TAG_NUMACCOUNT;
import static com.gloiot.hygo.ui.activity.login.LoginActivity.TAG_NUMLOGIN;

/**
 * 帐号密码登录
 * 作者：林锦洲
 * 时间：2017/06/26
 *
 * 修改时间：2017/11/16
 */
public class NumLoginActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {


    @Bind(R.id.tv_overseas_area)
    TextView tvOverseasArea;
    @Bind(R.id.tv_overseas_quhao)
    TextView tvOverseasQuhao;
    @Bind(R.id.et_login_overseas_phone)
    EditText etLoginOverseasPhone;
    @Bind(R.id.et_login_overseas_code)
    EditText etLoginOverseasCode;
    @Bind(R.id.tv_login_overseas_code)
    TextView tvLoginOverseasCode;


    private ArrayList<User> list = null;
    private String Str_Guojiama = "86";  //默认中国


    /**
     * 手机设备唯一ID
     */
    private String phoneId = null;

    @Override
    public int initResource() {
        return R.layout.activity_numlogin;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "手机验证码登录");
        setRequestErrorCallback(this); // 请求其他状态监听
        requestHandleArrayList.add(requestAction.p_countryCode_three(this));  //查询所有国家

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            //国家选择
            case 0x666:
                Log.e("0x666", data.getStringExtra("国家与地区"));
                Str_Guojiama = data.getStringExtra("国家码");
                tvOverseasArea.setText(data.getStringExtra("国家与地区"));
                tvOverseasQuhao.setText("+" + data.getStringExtra("国家码"));
                break;
            case TAG_NUMLOGIN:  // 帐号密码界面登录返回成功关闭当前页面
                if (data.getStringExtra("type").equals("成功")) {
                    Intent intent = new Intent();
                    intent.putExtra("type", "成功");
                    intent.putExtra("onlyid", data.getStringExtra("onlyid"));
                    setResult(TAG_NUMLOGIN, intent);
                    finish();
                }
        }
    }

    @OnClick({R.id.tv_overseas_area, R.id.tv_login_overseas_code, R.id.btn_login})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_overseas_area:
                //跳转地区
                if (list != null) {
                    Intent intent1 = new Intent(mContext, SelectCountriesActivity.class);
                    intent1.putExtra("type", "登录");
                    intent1.putExtra("list", list);
                    startActivityForResult(intent1, 666);
                } else {
                    MyToast.getInstance().showToast(mContext, "数据重新请求中...");
                    requestHandleArrayList.add(requestAction.p_countryCode_three(this));  //查询所有国家
                }
                break;
            case R.id.tv_login_overseas_code: // 获取验证码
                String Str_Phone = etLoginOverseasPhone.getText().toString().trim();
                if (TextUtils.isEmpty(Str_Guojiama)) {
                    MyToast.getInstance().showToast(mContext, "请设置国家与地区");
                } else if (TextUtils.isEmpty(Str_Phone)) {
                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                } else {
                    if (tvOverseasArea.getText().equals("中国")) {
                        if (etLoginOverseasPhone.getText().toString().length() != 11) {
                            MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                        } else {
                            requestHandleArrayList.add(requestAction.YZM(NumLoginActivity.this, Str_Phone, "登录验证码"));
                        }
                    } else {
                        requestHandleArrayList.add(requestAction.YZM(NumLoginActivity.this, Str_Guojiama + "+" + Str_Phone, "登录验证码"));
                    }
                }
                break;
            case R.id.btn_login:
                userLogin();
                break;
        }
    }


    /**
     * 用户登录
     */
    private void userLogin() {
        if (TextUtils.isEmpty(etLoginOverseasPhone.getText().toString()) && TextUtils.isEmpty(etLoginOverseasCode.getText().toString())) {
            MyToast.getInstance().showToast(mContext, "请输入有效手机号与验证码");
        } else if (TextUtils.isEmpty(etLoginOverseasPhone.getText().toString())) {
            MyToast.getInstance().showToast(mContext, "请输入有效手机号");
        } else if (TextUtils.isEmpty(etLoginOverseasCode.getText().toString())) {
            MyToast.getInstance().showToast(mContext, "请输入验证码");
        } else {
            checkPermission(new CheckPermListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void superPermission() {
                    phoneId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    if (phoneId != null) {
                        Log.e("权限申请", "权限获取了--phoneID" + phoneId);
                        editor.putString(ConstantUtlis.SP_PHENEID, phoneId);
                    } else {
                        Log.e("权限申请", "权限拒绝了--phoneID" + phoneId);
                        editor.putString(ConstantUtlis.SP_PHENEID, "");
                    }
                    if (tvOverseasArea.getText().equals("中国")) {
                        if (etLoginOverseasPhone.getText().toString().length() != 11) {
                            MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                        } else {
                            Log.e("手机号=", etLoginOverseasPhone.getText().toString().trim());
                            requestHandleArrayList.add(requestAction.UserLogin(NumLoginActivity.this, etLoginOverseasPhone.getText().toString().trim(), etLoginOverseasCode.getText().toString(), phoneId));
                        }
                    } else {
                        String phone = Str_Guojiama + "+" + etLoginOverseasPhone.getText().toString().trim();
                        requestHandleArrayList.add(requestAction.UserLogin(NumLoginActivity.this, phone, etLoginOverseasCode.getText().toString(), phoneId));
                    }
                }
            }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);

        }

    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_USERLOGIN:
                L.e(response.toString());
                editor.putString(ConstantUtlis.SP_BANGDINGPHONE, JSONUtlis.getString(response, "手机号"));
                editor.putString(ConstantUtlis.SP_HAIWAIZHUCE, JSONUtlis.getString(response, "是否海外注册"));
                editor.putString(ConstantUtlis.SP_LOGINTYPE, "成功");
                editor.putString(ConstantUtlis.SP_USERPHONE, JSONUtlis.getString(response, "账号"));
                editor.putString(ConstantUtlis.SP_USERSMRZ, JSONUtlis.getString(response, "实名认证"));
                editor.putString(ConstantUtlis.SP_RANDOMCODE, JSONUtlis.getString(response, "随机码"));
                editor.putString(ConstantUtlis.SP_USERIMG, JSONUtlis.getString(response, "头像"));
                editor.putString(ConstantUtlis.SP_NICKNAME, JSONUtlis.getString(response, "昵称"));
                editor.putString(ConstantUtlis.SP_GENDER, JSONUtlis.getString(response, "性别"));
                editor.putString(ConstantUtlis.SP_LOCATION, JSONUtlis.getString(response, "地址"));
                editor.putString(ConstantUtlis.SP_MYPWD, JSONUtlis.getString(response, "是否设置过支付密码"));
                editor.putString(ConstantUtlis.SP_ONLYID, JSONUtlis.getString(response, "唯一id"));
                editor.putString(ConstantUtlis.SP_SUPERMERCHANT, JSONUtlis.getString(response, "是否显示超级商家"));
                editor.putString(ConstantUtlis.SP_KEFUCENTER, JSONUtlis.getString(response, "是否显示客服中心"));
                editor.putString(ConstantUtlis.SP_TRUENAME, JSONUtlis.getString(response, "真实名"));
                editor.putString(ConstantUtlis.SP_LOGINZHANHAO, JSONUtlis.getString(response, "账号"));
                editor.putString(ConstantUtlis.SP_SHIFOUBANGDINGWX, JSONUtlis.getString(response, "是否绑定微信"));

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("账号", JSONUtlis.getString(response, "账号"));
                hashMap.put("随机码", JSONUtlis.getString(response, "随机码"));
                hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
                CommonUtlis.saveMap(ConstantUtlis.SP_REQUESTINFO_JSON, hashMap);
                editor.commit();
                imDB(etLoginOverseasPhone.getText().toString().trim());
                imSocket();
                // 获取图片清单
//                InfoRequestUtils.getInstance().getPictureList();

                Intent intent = new Intent();
                intent.putExtra("type", "成功");
                intent.putExtra("onlyid", JSONUtlis.getString(response, "唯一id"));
                setResult(TAG_NUMLOGIN, intent);

                finish();
                break;
            case RequestAction.TAG_YZM:
                MyToast.getInstance().showToast(this, "验证码已发送");

                tvLoginOverseasCode.setClickable(false);
                tvLoginOverseasCode.setText(i + "秒后重试");
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
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        Log.e("登录失败返回 JSON", response.toString());
        if (response.getString("状态").equals("当前账户登录过于频繁已被锁定,请24小时之后重试")) {
            DialogUtlis.oneBtnNormal(getmDialog(), "当前账户登录过于频繁已被锁定,请24小时之后重试");
        } else if (response.getString("状态").equals("与上次登录的设备不同")) {
            String phone = response.getString("手机号");
            final String finalPhone = phone;
            if (phone.length() != 11) {
                phone = "****" + phone.substring(phone.length() - 4, phone.length());
            } else {
                phone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
            }
            DialogUtlis.twoBtnNormal(getmDialog(), "提示", "由于你在新设备登录，我们需要验证你的身份，是否发送验证码到该账号绑定的手机号" + phone, true, "取消", "确定",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissDialog();
                            App.getInstance().mActivityStack.customAddActivity(NumLoginActivity.this);
                            Intent it = new Intent(NumLoginActivity.this, TianxieYanzhengmaActivity.class);
                            //表示是从登录验证是否常用手机处进入的
                            it.putExtra("type", "common_phone");
                            it.putExtra("newPhoneNum", finalPhone);
                            startActivityForResult(it, TAG_NUMLOGIN);
                        }
                    });
        } else {
            MyToast.getInstance().showToast(mContext, response.getString("状态"));
        }
    }

    private void imDB(String account) {
        IMDBManager.getInstance(mContext, account);
    }

    // socket认证
    private void imSocket() {
        if (!TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_USERPHONE, ""))) {
            SocketListener.getInstance().connectionRenZheng(preferences.getString(ConstantUtlis.SP_USERPHONE, ""), preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));
        }
    }


    private int i = 60;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tvLoginOverseasCode.setText(i + "秒后重试");
            } else if (msg.what == -8) {
                tvLoginOverseasCode.setText("获取验证码");
                tvLoginOverseasCode.setClickable(true);
                i = 60;
            }
        }
    };

}
