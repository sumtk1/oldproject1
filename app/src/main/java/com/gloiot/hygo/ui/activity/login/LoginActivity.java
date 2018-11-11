package com.gloiot.hygo.ui.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji.TianxieYanzhengmaActivity;
import com.gloiot.hygo.ui.activity.web.WebFragment;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.InfoRequestUtils;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MD5Utlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

import static com.gloiot.hygo.server.network.RequestAction.TAG_NUMACCOUNT;
import static com.gloiot.hygo.ui.activity.MainActivity.mainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {
    @Bind(R.id.iv_01)
    ImageView iv01;
    @Bind(R.id.iv_02)
    ImageView iv02;
    @Bind(R.id.tv_other)
    TextView tvOther;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.iv_wx_login)
    ImageView ivWxLogin;
    @Bind(R.id.tv_versionCode)
    TextView tvVersionCode;
    @Bind(R.id.tv_login_forgetpwd)
    TextView tvLoginForgetpwd;
    @Bind(R.id.toptitle_back)
    ImageView toptitleBack;

    @Bind(R.id.et_login_name)
    EditText etLoginName;
    @Bind(R.id.iv_guangbi)
    ImageView ivGuangbi;
    @Bind(R.id.et_login_pwd)
    EditText etLoginPwd;
    @Bind(R.id.iv_psw)
    ImageView ivPsw;

    private String login_name, login_pwd;
    private boolean pswVisible = false; // 密码是否可见


    /**
     * 手机设备唯一ID
     */
    private String phoneId = null;

    @Override
    public int initResource() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        App.getInstance().mActivityStack.customAddActivity(this);
        CommonUtlis.setTitleBar(this, "登录", "", true);
        toptitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivityBack();
            }
        });

        // 设置界面版本号
        tvVersionCode.setText("版本号 : " + ConstantUtlis.VERSION);
        setRequestErrorCallback(this);
        // 输入账号后显示清除按钮
        etLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivGuangbi.setVisibility(View.VISIBLE);
                } else {
                    ivGuangbi.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public static final int TAG_NUMLOGIN = 0x100; // 帐号密码登录返回

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            //微信完善资料返回
            case 1:

                break;
            case TAG_NUMLOGIN:  // 帐号密码界面登录返回成功关闭当前页面
                if (data.getStringExtra("type").equals("成功")) {
                    Intent intent = new Intent();
                    intent.putExtra("onlyid", data.getStringExtra("onlyid"));
                    setResult(WebFragment.TAG_LOGIN_BACK, intent);  // 网页跳进登录页返回唯一id
                    finish();
                }
                break;
        }
    }

    @OnClick({R.id.iv_guangbi, R.id.iv_psw, R.id.btn_login, R.id.tv_login_forgetpwd,
            R.id.iv_wx_login, R.id.tv_register_overseas, R.id.iv_num_login})
    @Override
    public void onClick(View v) {
        if (onMoreClick(v)) return; // 防止连续点击
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_guangbi:
                // 清空账号密码
                etLoginName.setText("");
                etLoginPwd.setText("");
                break;
            case R.id.iv_psw:
                // 显示、隐藏密码
                if (pswVisible) {
                    pswVisible = !pswVisible;
                    ivPsw.setImageResource(R.mipmap.ic_biyan);
                    etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    pswVisible = !pswVisible;
                    ivPsw.setImageResource(R.mipmap.ic_zhenyan);
                    etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                }
                etLoginPwd.setSelection(etLoginPwd.getText().toString().length());
                break;
            case R.id.btn_login:
                // 登录
                login_name = etLoginName.getText().toString();
                login_pwd = etLoginPwd.getText().toString();
                if (TextUtils.isEmpty(login_name) && TextUtils.isEmpty(login_pwd)) {
                    MyToast.getInstance().showToast(mContext, "请输入账号和密码");
                } else if (TextUtils.isEmpty(login_name)) {
                    MyToast.getInstance().showToast(mContext, "请输入有效账号");
                } else if (TextUtils.isEmpty(login_pwd)) {
                    MyToast.getInstance().showToast(mContext, "请输入密码");
                } else {
                    getPhoneDeviceID();
                    // requestHandleArrayList.add(requestAction.numAccountLogin(NumLoginActivity.this, login_name, MD5Utlis.Md5(login_pwd)));
                }
                break;
            case R.id.iv_wx_login:

                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        phoneId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                        if (phoneId != null) {
                            editor.putString(ConstantUtlis.SP_PHENEID, phoneId);
                        } else {
                            phoneId = "";
                            editor.putString(ConstantUtlis.SP_PHENEID, "");
                        }
                        editor.commit();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("手机ID", phoneId);
                        CommonUtlis.updataMap(ConstantUtlis.SP_PHONEINFO_JSON, hashMap);

//                        isWXLogin = true;
                        if (CommonUtlis.isWXAppInstalledAndSupported(mContext)) {
                            getWxInfo();
                        } else {
                            LoadDialog.dismiss(mContext);
                        }
                    }
                }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);

//                requestHandleArrayList.add(requestAction.WXLogin(LoginActivity.this, "051XKJG22aygM01UqKH22McMG22XKJGY"));
                break;
            case R.id.tv_register_overseas://海外手机号注册
                intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login_forgetpwd: // 忘记密码
                intent = new Intent(mContext, WangjimimaActivity.class);
                intent.putExtra("forgetpwd", "忘记登录密码");
                startActivity(intent);
                break;
            case R.id.iv_num_login:
                startActivityForResult(new Intent(mContext, NumLoginActivity.class), TAG_NUMLOGIN);
                break;
        }
    }

    public void getPhoneDeviceID() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                phoneId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (phoneId != null) {
                    editor.putString(ConstantUtlis.SP_PHENEID, phoneId);
                } else {
                    phoneId = "";
                    editor.putString(ConstantUtlis.SP_PHENEID, "");
                }
                editor.commit();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("手机ID", phoneId);
                CommonUtlis.updataMap(ConstantUtlis.SP_PHONEINFO_JSON, hashMap);

                requestHandleArrayList.add(requestAction.numAccountLogin(LoginActivity.this, login_name, MD5Utlis.Md5(login_pwd), phoneId));
            }
        }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        Intent intent;
        switch (requestTag) {
            case TAG_NUMACCOUNT:
                CommonUtlis.clearPersonalData();//清除个人数据，重置登录状态等
                SocketListener.getInstance().signoutRenZheng();
                IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).ClearnData();

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
                imDB(JSONUtlis.getString(response, "账号"));
                imSocket();
                // 获取图片清单
//                InfoRequestUtils.getInstance().getPictureList();

                //SQLite
                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("shang_cheng.db", Context.MODE_PRIVATE, null);
                sqLiteDatabase.execSQL("CREATE TABLE if not exists qiehuan_zhanghao (zhanghao VARCHAR PRIMARY KEY, mima VARCHAR, imgUrl VARCHAR)");
                ContentValues contentValues = new ContentValues();
                contentValues.put("zhanghao", login_name);
                contentValues.put("mima", login_pwd);
                contentValues.put("imgUrl", JSONUtlis.getString(response, "头像"));
                sqLiteDatabase.replace("qiehuan_zhanghao", null, contentValues);


                Intent it0 = new Intent();
                it0.putExtra("onlyid", JSONUtlis.getString(response, "唯一id"));
                setResult(WebFragment.TAG_LOGIN_BACK, it0);  // 网页跳进登录页返回唯一id

                Intent intent0 = new Intent();
                intent0.putExtra("type", "成功");
                intent0.putExtra("onlyid", JSONUtlis.getString(response, "唯一id"));
                setResult(TAG_NUMLOGIN, intent0);
                finish();
                break;
            case RequestAction.TAG_WXLOGIN:
                L.e(response.toString());
                if (response.getString("账号状态").equals("未注册")) {
                    intent = new Intent(mContext, WXWanshanZiLiaoActivity.class);
                    intent.putExtra("openid", response.getString("微信openid"));
                    intent.putExtra("unionid", response.getString("微信unionid"));
                    startActivityForResult(intent, 1);
                } else if (response.getString("账号状态").equals("已注册")) {
                    editor.putString(ConstantUtlis.SP_LOGINTYPE, "成功");
                    editor.putString(ConstantUtlis.SP_HAIWAIZHUCE, "否");
                    editor.putString(ConstantUtlis.SP_USERPHONE, JSONUtlis.getString(response, "会员账号"));
                    editor.putString(ConstantUtlis.SP_BANGDINGPHONE, JSONUtlis.getString(response, "会员手机号"));
                    editor.putString(ConstantUtlis.SP_GENDER, JSONUtlis.getString(response, "性别"));
                    editor.putString(ConstantUtlis.SP_USERSMRZ, JSONUtlis.getString(response, "认证状态"));
                    editor.putString(ConstantUtlis.SP_RANDOMCODE, JSONUtlis.getString(response, "会员随机码"));
                    editor.putString(ConstantUtlis.SP_USERIMG, JSONUtlis.getString(response, "会员头像"));
                    editor.putString(ConstantUtlis.SP_NICKNAME, JSONUtlis.getString(response, "会员昵称"));
                    editor.putString(ConstantUtlis.SP_MYPWD, JSONUtlis.getString(response, "是否设置过支付密码"));
                    editor.putString(ConstantUtlis.SP_LOCATION, JSONUtlis.getString(response, "会员地址"));
                    editor.putString(ConstantUtlis.SP_ONLYID, JSONUtlis.getString(response, "唯一id"));
                    editor.putString(ConstantUtlis.SP_KEFUCENTER, JSONUtlis.getString(response, "是否显示客服中心"));
                    editor.putString(ConstantUtlis.SP_SUPERMERCHANT, JSONUtlis.getString(response, "是否显示超级商家"));
                    editor.putString(ConstantUtlis.SP_TRUENAME, JSONUtlis.getString(response, "真实名"));
                    editor.putString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "是");

                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put("账号", JSONUtlis.getString(response, "会员账号"));
                    hashMap2.put("随机码", JSONUtlis.getString(response, "会员随机码"));
                    hashMap2.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
                    CommonUtlis.saveMap(ConstantUtlis.SP_REQUESTINFO_JSON, hashMap2);
                    editor.commit();
                    imDB(JSONUtlis.getString(response, "会员账号"));
                    imSocket();
                    // 获取图片清单
//                    InfoRequestUtils.getInstance().getPictureList();

//                    intent = new Intent(mContext, MainActivity.class);
//                    intent.putExtra("selectFlag", 1);
//                    startActivity(intent);

                    Intent it1 = new Intent();
                    it1.putExtra("onlyid", JSONUtlis.getString(response, "唯一id"));
                    setResult(WebFragment.TAG_LOGIN_BACK, it1);  // 网页跳进登录页返回唯一id
                    finish();
                }
                break;
        }
    }


    @Override
    public void requestErrorcallback(int requestTag, final JSONObject response) throws Exception {
        if (response.getString("状态").equals("当前账户登录过于频繁已被锁定,请24小时之后重试")) {
            DialogUtlis.oneBtnNormal(getmDialog(), "当前账户登录过于频繁已被锁定,请24小时之后重试");
        } else if (response.getString("状态").equals("与上次登录的设备不同")) {
            String phone = response.getString("手机号");
            final String finalPhone = phone;
            Log.e("phoneeee1", finalPhone);
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
                            Log.e("phoneeee2", finalPhone);
                            dismissDialog();
                            Intent it = new Intent(LoginActivity.this, TianxieYanzhengmaActivity.class);
                            //表示是从登录验证是否常用手机处进入的
                            it.putExtra("type", "common_phone");
                            it.putExtra("newPhoneNum", finalPhone);
                            //表示是否要对账号密码进行存储
                            it.putExtra("storage", true);
                            it.putExtra("zhanghao", login_name);
                            it.putExtra("mima", login_pwd);
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

    /**
     * 获取微信信息
     */
    public void getWxInfo() {
        LoadDialog.show(mContext);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_qqwlw_test";
        App.getInstance().wxapi.sendReq(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        final String wxCode = preferences.getString(ConstantUtlis.SP_WXCODE, null);
        if (!TextUtils.isEmpty(wxCode)) {
            checkPermission(new CheckPermListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void superPermission() {
                    phoneId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    if (phoneId != null) {
                        Log.e("权限申请", "权限获取了--phoneID" + phoneId);
                        editor.putString(ConstantUtlis.SP_PHENEID, phoneId);
                        requestHandleArrayList.add(requestAction.WXLogin(LoginActivity.this, wxCode, phoneId));
                    } else {
                        Log.e("权限申请", "权限拒绝了--phoneID" + phoneId);
                    }
                }
            }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);

            editor.putString(ConstantUtlis.SP_WXCODE, null);
            editor.commit();

        } else {
            LoadDialog.dismiss(mContext);
        }
    }

    //重写系统返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //其中event.getRepeatCount() == 0 是重复次数，点返回键时，防止点的过快，触发两次后退事件，做此设置
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {//保留这个判断，增强程序健壮性
            onActivityBack();
        }
        return false;
    }

    /**
     * (点击返回)返回上一个页面
     */
    private void onActivityBack() {
        if (preferences.getString(ConstantUtlis.SP_LOGINTJIEMIANYPE, "").equals("退出")) {
            if (mainActivity != null) {
                mainActivity.finish();
            }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("selectFlag", 1);
            startActivity(intent);
        }
        editor.putString(ConstantUtlis.SP_LOGINTJIEMIANYPE, "");
        editor.commit();
        finish();
    }

    /**
     * 登录页退出效果
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_close);
    }

}
