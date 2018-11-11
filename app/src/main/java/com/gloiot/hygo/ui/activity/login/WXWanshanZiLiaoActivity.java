package com.gloiot.hygo.ui.activity.login;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.listwithheard.SelectCountriesActivity;
import com.gloiot.hygo.ui.activity.listwithheard.User;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.InfoRequestUtils;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.gloiot.hygo.utlis.StringUtils;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 微信完善资料
 */
public class WXWanshanZiLiaoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.et_gl_shoujihao)
    EditText etGlShoujihao;
    @Bind(R.id.et_gl_yanzhengma)
    EditText etGlYanzhengma;
    @Bind(R.id.tv_gl_yanzhengma)
    TextView tvGlYanzhengma;
    @Bind(R.id.btn_gl_phone)
    Button btnGlPhone;
    @Bind(R.id.et_gl_zhanghao)
    EditText et_gl_zhanghao;
    @Bind(R.id.et_gl_pwd)
    EditText etGlPwd;
    @Bind(R.id.et_gl_confirm_pwd)
    EditText etGlConfirmPwd;

    @Bind(R.id.ll_wx_wanshan_ziliao_zhanghao)
    LinearLayout ll_wx_wanshan_ziliao_zhanghao;
    @Bind(R.id.ll_wx_wanshan_ziliao_mima)
    LinearLayout ll_wx_wanshan_ziliao_mima;
    @Bind(R.id.im_gl_agreement)
    ImageView im_gl_agreement;

    @Bind(R.id.tv_gl_quhao)
    TextView tv_gl_quhao;
    @Bind(R.id.et_gl_area)
    TextView et_gl_area;

    private String shoujihao, phoneId;
    private String openid, unionid;

    private boolean isAgree = true;
    private ArrayList<User> list = null;
    private boolean isPhone = false, isCode = false,
            isZhangHao = false, isPsw = false, isPswConfig = false;

    private String Str_Guojiama = "86";  //默认中国


    /**
     * 输入判断：
     * 0：未获取验证码
     * 1：以获取验证码，且不需要重新填写密码、确认密码
     * 2：以获取验证码，但是需要重新填写密码、确认密码
     */
    private int isExistInfo = 0;

    private int i = 60;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tvGlYanzhengma.setText(i + "秒后重试");
            } else if (msg.what == -8) {
                tvGlYanzhengma.setText("获取验证码");
                tvGlYanzhengma.setClickable(true);
                i = 60;
            }
        }
    };

    @Override
    public int initResource() {
        return R.layout.activity_wx_wanshanziliao;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "完善资料");
        Intent intent = getIntent();
        openid = intent.getStringExtra("openid");
        unionid = intent.getStringExtra("unionid");

        requestHandleArrayList.add(requestAction.p_countryCode_three(this));  //查询所有国家
        MyTextWatch();

        requestErrorCallback = new RequestErrorCallback() {
            @Override
            public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
                switch (requestTag) {
                    case RequestAction.TAG_WEIXINGUANLIANSHOUJIHAO:
                        DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"), "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismissDialog();
                            }
                        });
                        break;
                    default:
                        MyToast.getInstance().showToast(mContext, response.getString("状态"));
//                        DialogUtlis.oneBtnNormal(mContext, response.getString("状态"));
                        break;

                }
            }
        };
    }

    @OnClick({R.id.btn_gl_phone, R.id.tv_gl_yanzhengma,
            R.id.ll_gl_area, R.id.rl_gl_agreement, R.id.tv_gl_agreement})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_gl_yanzhengma://获取验证码
                shoujihao = etGlShoujihao.getText().toString();
                if (TextUtils.isEmpty(shoujihao)) {
                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                } else if (shoujihao.length() < 11) {
                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                } else {
                    requestHandleArrayList.add(requestAction.YZM(WXWanshanZiLiaoActivity.this, shoujihao, "微信登录验证码"));
                }
                break;
            case R.id.btn_gl_phone:
                shoujihao = etGlShoujihao.getText().toString();
                final String yanzhengma = etGlYanzhengma.getText().toString();
                final String mima = etGlPwd.getText().toString();
                String quedingmima = etGlConfirmPwd.getText().toString();
                final String Str_Zhanghao = et_gl_zhanghao.getText().toString();

                if (TextUtils.isEmpty(shoujihao) || shoujihao.length() < 11) {
                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                } else if (TextUtils.isEmpty(yanzhengma)) {
                    MyToast.getInstance().showToast(mContext, "请输入验证码");
                } else {
                    //账号不存在，需要重新设置密码
                    if (2 == isExistInfo) {
                        if (TextUtils.isEmpty(Str_Zhanghao)) {
                            MyToast.getInstance().showToast(mContext, "请设置账号");
                        } else if (Str_Zhanghao.length() < 6) {
                            MyToast.getInstance().showToast(mContext, "请设置有效账号");
                        } else if (TextUtils.isEmpty(mima)) {
                            MyToast.getInstance().showToast(mContext, "请输入密码6-15位");
                        } else if (mima.length() < 6 || mima.length() > 15) {
                            MyToast.getInstance().showToast(mContext, "密码必须为6—15位密码");
                        } else if (TextUtils.isEmpty(quedingmima)) {
                            MyToast.getInstance().showToast(mContext, "请输入确认密码");
                        } else if (!StringUtils.judgePasswordFormat(mima)) {
                            MyToast.getInstance().showToast(mContext, "密码必须字母大小写与数字相结合");
                        } else if (!mima.equals(quedingmima)) {
                            //清除
                            etGlConfirmPwd.getText().clear();
                            MyToast.getInstance().showToast(mContext, "两次密码不一致,请重新输入!");
                        } else {
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

                                    requestHandleArrayList.add(requestAction.WeChatRelevancePhone(WXWanshanZiLiaoActivity.this, shoujihao, yanzhengma, Str_Zhanghao, openid, unionid, MD5Utlis.Md5(mima)));
                                }
                            }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);
                        }
                    } else {//账号存在，不需要重新设置密码
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

                                requestHandleArrayList.add(requestAction.WeChatRelevancePhone(WXWanshanZiLiaoActivity.this, shoujihao, yanzhengma, Str_Zhanghao, openid, unionid, ""));
                            }
                        }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);
                    }
                }
                break;

            //勾选同意协议
            case R.id.rl_gl_agreement:
                if (isAgree) {
                    im_gl_agreement.setImageResource(R.mipmap.ic_xuanzhong_no);
                    isAgree = false;
                } else {
                    im_gl_agreement.setImageResource(R.mipmap.ic_xuanzhong);
                    isAgree = true;
                }

                setButtonEnabled();

                break;

            //跳转地区
            case R.id.ll_gl_area:

                if (list != null) {
                    Intent intent1 = new Intent(mContext, SelectCountriesActivity.class);
                    intent1.putExtra("type", "微信登录完善资料");
                    intent1.putExtra("list", list);
                    startActivityForResult(intent1, 666);
                } else {
                    MyToast.getInstance().showToast(mContext, "数据重新请求中...");
                    requestHandleArrayList.add(requestAction.p_countryCode_three(this));  //查询所有国家
                }
                break;
            //跳转协议
            case R.id.tv_gl_agreement:
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", ConstantUtlis.SHOP_OVERSEAS_REGISTER_URL);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, final JSONObject response, int showLoad) throws
            JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_YZM:
                MyToast.getInstance().showToast(this, "验证码已发送");
//            {"状态":"成功","exist":"是","内容":"发送短信成功"}
                if ("否".equals(JSONUtlis.getString(response, "exist"))) {
                    ll_wx_wanshan_ziliao_zhanghao.setVisibility(View.VISIBLE);
                    ll_wx_wanshan_ziliao_mima.setVisibility(View.VISIBLE);
                    isExistInfo = 2;//需要重新填写密码、确认密码标示
                } else {
                    isExistInfo = 1;//不需要重新填写密码、确认密码标示
                }
                tvGlYanzhengma.setClickable(false);
                tvGlYanzhengma.setText(i + "秒后重试");
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
            case RequestAction.TAG_WEIXINGUANLIANSHOUJIHAO:
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
                editor.putString(ConstantUtlis.SP_SUPERMERCHANT, JSONUtlis.getString(response, "是否显示超级商家"));
                editor.putString(ConstantUtlis.SP_KEFUCENTER, JSONUtlis.getString(response, "是否显示客服中心"));
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
//                InfoRequestUtils.getInstance().getPictureList();

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("selectFlag", 1);
                startActivity(intent);
                finish();
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
            default:
                break;
        }
    }

    /**
     * 按钮是否可点击处理
     */
    private void setButtonEnabled() {
        //isExistInfo == 2  表示需要填写密码账号那些
        if (isExistInfo == 2) {
            if (isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                btnGlPhone.setEnabled(true);
            else
                btnGlPhone.setEnabled(false);
        } else {
            if (isPhone && isCode && isAgree)
                btnGlPhone.setEnabled(true);
            else
                btnGlPhone.setEnabled(false);
        }
    }

    /**
     * 为edittext添加内容是否为空判断
     */
    private void MyTextWatch() {
        //手机号
        etGlShoujihao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isPhone = false;
                else isPhone = true;

                setButtonEnabled();
            }
        });
        //验证码
        etGlYanzhengma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isCode = false;
                else isCode = true;

                setButtonEnabled();
            }
        });
        //账号
        et_gl_zhanghao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isZhangHao = false;
                else
                    isZhangHao = true;

                setButtonEnabled();
            }
        });
        //密码
        etGlPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isPsw = false;
                else isPsw = true;

                setButtonEnabled();
            }
        });
        //确认密码
        etGlConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isPswConfig = false;
                else isPswConfig = true;

                setButtonEnabled();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x666) {
            Str_Guojiama = data.getStringExtra("国家码");
            et_gl_area.setText(data.getStringExtra("国家与地区"));
            tv_gl_quhao.setText("+" + data.getStringExtra("国家码"));

//            if ("中国".equals(data.getStringExtra("国家与地区"))) {
//                et_reg_overseas_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
//            } else {
//                et_reg_overseas_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
//            }
            etGlShoujihao.setText("");
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
}
